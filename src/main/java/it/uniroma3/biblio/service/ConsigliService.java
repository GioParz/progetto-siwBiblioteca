package it.uniroma3.biblio.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.biblio.model.Autore;
import it.uniroma3.biblio.model.Genere;
import it.uniroma3.biblio.model.Libro;
import it.uniroma3.biblio.model.Utente;
import it.uniroma3.biblio.repository.ElementoLibreriaRepository;
import it.uniroma3.biblio.repository.LibroRepository;

/**
 * Motore dei consigli di lettura. Funzionalità riservata agli utenti registrati: propone
 * libri in base a due criteri, combinati insieme:
 *  - i GENERI preferiti (i generi dei libri che l'utente ha valutato con 4 o 5 stelle)
 *  - gli AUTORI preferiti (gli autori con più libri valutati 4-5 stelle dall'utente,
 *    in ordine di preferenza)
 *
 * Se l'utente non ha ancora valutato positivamente nessun libro, la lista dei consigli è
 * semplicemente vuota (nessun fallback con libri generici): il motore dei consigli è
 * intenzionalmente "silenzioso" finché non ha dati sufficienti per essere davvero
 * personalizzato. Le pagine che lo usano (/consigli e la sezione nella libreria personale)
 * mostrano in quel caso un messaggio che invita l'utente a valutare qualche libro.
 */
@Service
@Transactional(readOnly = true)
public class ConsigliService {

    private final ElementoLibreriaRepository elementoLibreriaRepository;
    private final LibroRepository libroRepository;

    public ConsigliService(ElementoLibreriaRepository elementoLibreriaRepository, LibroRepository libroRepository) {
		this.elementoLibreriaRepository = elementoLibreriaRepository;
		this.libroRepository = libroRepository;
	}

    public List<Libro> calcolaLibriConsigliati(Utente utente) {
        return calcolaLibriConsigliati(utente, 6);
    }

    /**
     * Variante con limite personalizzabile, usata per l'anteprima compatta mostrata nella
     * pagina /libreria (limite più basso) rispetto alla pagina /consigli dedicata (limite
     * più alto).
     */
    public List<Libro> calcolaLibriConsigliati(Utente utente, int limite) {

        List<Genere> generiPreferiti = this.elementoLibreriaRepository.findGeneriPreferitiDaUtente(utente);
        List<Autore> autoriPreferiti = this.elementoLibreriaRepository.findAutoriPreferitiDaUtente(utente);

        if (generiPreferiti.isEmpty() && autoriPreferiti.isEmpty()) {
            return List.of();
        }

        // I libri di un autore preferito sono un segnale più forte di un semplice genere
        // preferito, quindi vengono aggiunti per primi al set (ordine di inserimento
        // preservato da LinkedHashSet); l'eventuale sovrapposizione tra le due liste viene
        // eliminata automaticamente grazie all'equals()/hashCode() di Libro basato sull'ISBN.
        Set<Libro> consigliati = new LinkedHashSet<>();

        if (!autoriPreferiti.isEmpty()) {
            consigliati.addAll(this.libroRepository.findSuggeritiPerAutoriEUtente(autoriPreferiti, utente));
        }
        if (!generiPreferiti.isEmpty()) {
            consigliati.addAll(this.libroRepository.findSuggeritiPerGeneriEUtente(generiPreferiti, utente));
        }

        return consigliati.stream().limit(limite).toList();
    }
}