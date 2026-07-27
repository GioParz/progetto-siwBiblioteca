package it.uniroma3.biblio.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.biblio.model.Autore;
import it.uniroma3.biblio.model.Genere;
import it.uniroma3.biblio.model.Libro;
import it.uniroma3.biblio.model.Utente;
import it.uniroma3.biblio.repository.ElementoLibreriaRepository;
import it.uniroma3.biblio.repository.ElementoLibreriaRepository.AutorePreferito;
import it.uniroma3.biblio.repository.ElementoLibreriaRepository.GenerePreferito;
import it.uniroma3.biblio.repository.LibroRepository;

/**
 * Motore dei consigli di lettura. Funzionalità riservata agli utenti registrati: i libri
 * candidati vengono divisi in 3 FASCE di rilevanza, sempre nello stesso ordine di priorità:
 *
 *  1) genere preferito + autore preferito (il segnale più forte: entrambi coincidono)
 *  2) genere preferito, autore diverso
 *  3) autore preferito, genere diverso
 *
 * Da ciascuna fascia si prende un numero massimo di libri proporzionale al limite
 * richiesto (quote 3/2/1 quando il limite è 6), ordinati per rilevanza interna alla
 * fascia. Se una fascia ha meno libri disponibili della sua quota, il totale restituito
 * è semplicemente più corto: non c'è "riempimento" da altre fasce.
 *
 * Se l'utente non ha ancora valutato positivamente nessun libro, la lista dei consigli è
 * vuota (nessun fallback con libri generici).
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

    public List<Libro> calcolaLibriConsigliati(Utente utente, int limite) {

        List<AutorePreferito> autoriPreferiti = this.elementoLibreriaRepository.findAutoriPreferitiConPesoDaUtente(utente);
        List<GenerePreferito> generiPreferiti = this.elementoLibreriaRepository.findGeneriPreferitiConPesoDaUtente(utente);

        if (generiPreferiti.isEmpty() && autoriPreferiti.isEmpty()) {
            return List.of();
        }

        // peso per autore: in base alla posizione nella lista già ordinata per rilevanza
        // (findAutoriPreferitiDaUtente è già ORDER BY COUNT DESC)
        Map<Autore, Long> pesoAutore = new HashMap<>();
        for (AutorePreferito ap : autoriPreferiti) {
            pesoAutore.put(ap.getAutore(), ap.getPeso());
        }

        // peso per genere: numero di libri di quel genere valutati 4-5 stelle
        Map<Genere, Long> pesoGenere = new HashMap<>();
        for (GenerePreferito gp : generiPreferiti) {
            pesoGenere.put(gp.getGenere(), gp.getPeso());
        }

        // pool di candidati: unione (senza duplicati) di tutto ciò che matcha per autore
        // o per genere preferito, non ancora in libreria
        Set<Libro> candidati = new LinkedHashSet<>();
        if (!autoriPreferiti.isEmpty()) {
            candidati.addAll(this.libroRepository.findSuggeritiPerAutoriEUtente(
            		List.copyOf(pesoAutore.keySet()), utente));
        }
        if (!pesoGenere.isEmpty()) {
            candidati.addAll(this.libroRepository.findSuggeritiPerGeneriEUtente(
                    List.copyOf(pesoGenere.keySet()), utente));
        }

        // suddivisione in 3 fasce di rilevanza
        List<Libro> genereEAutore = new ArrayList<>();
        List<Libro> soloGenere = new ArrayList<>();
        List<Libro> soloAutore = new ArrayList<>();

        for (Libro libro : candidati) {
            boolean genereMatch = pesoGenere.containsKey(libro.getGenere());
            boolean autoreMatch = pesoAutore.containsKey(libro.getAutore());

            if (genereMatch && autoreMatch) {
                genereEAutore.add(libro);
            } else if (genereMatch) {
                soloGenere.add(libro);
            } else if (autoreMatch) {
                soloAutore.add(libro);
            }
            // un candidato che non matcha né genere né autore non può esistere qui,
            // perché arriva solo dalle due query di suggerimento sopra
        }

        // ordinamento interno a ciascuna fascia, dal più rilevante
        Comparator<Libro> perRilevanza = Comparator
                .comparingLong((Libro l) -> pesoGenere.getOrDefault(l.getGenere(), 0L)
                        + pesoAutore.getOrDefault(l.getAutore(), 0L))
                .reversed();

        genereEAutore.sort(perRilevanza);
        soloGenere.sort(perRilevanza);
        soloAutore.sort(perRilevanza);

        // quote proporzionali al limite richiesto: con limite=6 danno esattamente 3/2/1
        int quotaGenereEAutore = (int) Math.ceil(limite / 2.0);
        int quotaSoloGenere = (int) Math.ceil(limite / 3.0);
        int quotaSoloAutore = Math.max(limite - quotaGenereEAutore - quotaSoloGenere, 0);

        List<Libro> risultato = new ArrayList<>();
        risultato.addAll(genereEAutore.stream().limit(quotaGenereEAutore).toList());
        risultato.addAll(soloGenere.stream().limit(quotaSoloGenere).toList());
        risultato.addAll(soloAutore.stream().limit(quotaSoloAutore).toList());

        return risultato;
    }
}