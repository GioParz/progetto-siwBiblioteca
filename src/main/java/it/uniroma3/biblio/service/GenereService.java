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

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Genere save(Genere genere) {
        return this.genereRepository.save(genere);
    }
}