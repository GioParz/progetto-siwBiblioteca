package it.uniroma3.biblio.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.biblio.model.Genere;

public interface GenereRepository extends CrudRepository<Genere, Long> {

    public boolean existsByNome(String nome);
    
    public Optional<Genere> findByNome(String nome);
}
