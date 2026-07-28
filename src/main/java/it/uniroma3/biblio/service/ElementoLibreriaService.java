package it.uniroma3.biblio.service;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.biblio.exception.LibroGiaInLibreriaException;
import it.uniroma3.biblio.exception.ResourceNotFoundException;
import it.uniroma3.biblio.model.ElementoLibreria;
import it.uniroma3.biblio.model.Libro;
import it.uniroma3.biblio.model.StatoLettura;
import it.uniroma3.biblio.model.Utente;
import it.uniroma3.biblio.repository.ElementoLibreriaRepository;
import it.uniroma3.biblio.repository.LibroRepository;
import it.uniroma3.biblio.repository.UtenteRepository;

@Service
@Transactional(readOnly = true)
public class ElementoLibreriaService {

    private final ElementoLibreriaRepository elementoLibreriaRepository;
    private final LibroRepository libroRepository;
    private final UtenteRepository utenteRepository;

    public ElementoLibreriaService(ElementoLibreriaRepository elementoLibreriaRepository,
			LibroRepository libroRepository, UtenteRepository utenteRepository) {
		this.elementoLibreriaRepository = elementoLibreriaRepository;
		this.libroRepository = libroRepository;
		this.utenteRepository = utenteRepository;
	}

    public List<ElementoLibreria> findByUtente(Utente utente) {
        return this.elementoLibreriaRepository.findByUtenteWithLibroEAutore(utente);
    }

    public ElementoLibreria findById(Long id) {
        return this.elementoLibreriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Elemento con id: " + id + " non trovato."));
    }
    
    public Double calcolaMediaVotoLibro(Libro libro) {
    	return this.elementoLibreriaRepository.findMediaValutazionePerLibro(libro);
    }
    
    public boolean isLibroInLibreria(Utente utente, Libro libro) {
    	return this.elementoLibreriaRepository.existsByUtenteAndLibro(utente, libro);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ElementoLibreria aggiungiLibroALibreria(Long utenteId, Long libroId) {
    	Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato"));
        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new ResourceNotFoundException("Libro non trovato"));

        if (this.elementoLibreriaRepository.existsByUtenteAndLibro(utente, libro)) {
            throw new LibroGiaInLibreriaException("Il libro '" + libro.getTitolo() + "' è già presente nella tua libreria!");
        }

        ElementoLibreria elemento = new ElementoLibreria();
        elemento.setUtente(utente);
        elemento.setLibro(libro);

        return this.elementoLibreriaRepository.save(elemento);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ElementoLibreria aggiornaSchedaLettura(Long elementoId, StatoLettura nuovoStato, 
    		Integer nuovaValutazione, Utente utenteLoggato) {
    	
    	ElementoLibreria elemento = elementoLibreriaRepository.findById(elementoId)
                .orElseThrow(() -> new ResourceNotFoundException("Elemento non trovato"));

        // Verifico che l'utente sia effettivamente il proprietario di questa scheda
        if (!elemento.getUtente().getId().equals(utenteLoggato.getId())) {
            throw new AccessDeniedException("Non sei autorizzato a modificare questa scheda");
        }

        elemento.setStatoLettura(nuovoStato);
        elemento.setValutazione(nuovaValutazione);
        
        return elementoLibreriaRepository.save(elemento);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void rimuoviDaLibreria(Long elementoId, Utente utenteLoggato) {
    	ElementoLibreria elemento = elementoLibreriaRepository.findById(elementoId)
                .orElseThrow(() -> new ResourceNotFoundException("Elemento non trovato"));

        if (!elemento.getUtente().getId().equals(utenteLoggato.getId())) {
            throw new AccessDeniedException("Non sei autorizzato a cancellare questa scheda");
        }

        elementoLibreriaRepository.delete(elemento);
    }
}
