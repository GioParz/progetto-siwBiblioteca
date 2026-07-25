package it.uniroma3.biblio.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.biblio.exception.LibroInUsoException;
import it.uniroma3.biblio.exception.ResourceNotFoundException;
import it.uniroma3.biblio.model.Genere;
import it.uniroma3.biblio.model.Libro;
import it.uniroma3.biblio.repository.ElementoLibreriaRepository;
import it.uniroma3.biblio.repository.GenereRepository;
import it.uniroma3.biblio.repository.LibroRepository;

@Service
@Transactional(readOnly = true)
public class LibroService {

    private final LibroRepository libroRepository;
    private final GenereRepository genereRepository;
    private final ElementoLibreriaRepository elementoLibreriaRepository;

    public LibroService(LibroRepository libroRepository, GenereRepository genereRepository,
    		ElementoLibreriaRepository elementoLibreriaRepository) {
		this.libroRepository = libroRepository;
		this.genereRepository = genereRepository;
		this.elementoLibreriaRepository = elementoLibreriaRepository;
	}

    public List<Libro> findAll() {
        return (List<Libro>) this.libroRepository.findAll();
    }

    public Libro findById(Long id) {
        return this.libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro con id: " + id + " non trovato."));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Libro save(Libro libro) {
        return this.libroRepository.save(libro);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteById(Long id) {
        if (!this.libroRepository.existsById(id))
            throw new ResourceNotFoundException("Impossibile eliminare: Libro con id: " + id + " non trovato.");
        
        if(this.elementoLibreriaRepository.existsByLibroId(id))
        	throw new LibroInUsoException("Impossibile eliminare il libro: è presente nella libreria personale di uno o più utenti.");
        
        this.libroRepository.deleteById(id);
    }

    // SUPPORTS: Se c'è una transazione attiva usa quella, altrimenti esegue in modalità non-transazionale
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Libro> cercaLibri(String query, Long genereId) {
        if ((query == null || query.isBlank()) && genereId == null) {
            return this.findAll();
        }

        Genere genere = null;
        if (genereId != null) {
            genere = this.genereRepository.findById(genereId).orElse(null);
        }

        String queryPulita = (query != null && !query.isBlank()) ? query.trim() : null;

        return this.libroRepository.findByGenereEQuery(genere, queryPulita);
    }
}