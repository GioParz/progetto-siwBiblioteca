package it.uniroma3.biblio.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.biblio.exception.ResourceNotFoundException;
import it.uniroma3.biblio.model.Genere;
import it.uniroma3.biblio.repository.GenereRepository;

@Service
@Transactional(readOnly = true)
public class GenereService {

    private final GenereRepository genereRepository;

    public GenereService(GenereRepository genereRepository) {
		this.genereRepository = genereRepository;
	}

	public List<Genere> findAll() {
        return (List<Genere>) this.genereRepository.findAll();
    }

    public Genere findById(Long id) {
        return this.genereRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genere con id: " + id + " non trovato."));
    }

    /**
     * Usato dalla pagina di dettaglio (/genere/{id}), che mostra anche l'elenco dei libri
     * del genere con il rispettivo autore: carica tutto con un'unica query invece di
     * generare una query aggiuntiva per la collezione "libri" e una per ogni "autore".
     */
    public Genere findByIdConLibri(Long id) {
        return this.genereRepository.findByIdWithLibriEAutori(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genere con id: " + id + " non trovato."));
    }

    /** Usato dalla ricerca globale (barra di ricerca presente in tutte le pagine). */
    public List<Genere> cerca(String query) {
        return this.genereRepository.findByNomeContainingIgnoreCase(query);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Genere save(Genere genere) {
        return this.genereRepository.save(genere);
    }
}