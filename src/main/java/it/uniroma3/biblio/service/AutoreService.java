package it.uniroma3.biblio.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.biblio.exception.ResourceNotFoundException;
import it.uniroma3.biblio.model.Autore;
import it.uniroma3.biblio.repository.AutoreRepository;

@Service
@Transactional(readOnly = true)
public class AutoreService {

    private final AutoreRepository autoreRepository;

    public AutoreService(AutoreRepository autoreRepository) {
		this.autoreRepository = autoreRepository;
	}

    public List<Autore> findAll() {
        return (List<Autore>) this.autoreRepository.findAll();
    }

    public Autore findById(Long id) {
        return this.autoreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autore non trovato con id: " + id));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Autore save(Autore autore) {
        return this.autoreRepository.save(autore);
    }
}