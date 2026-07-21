package it.uniroma3.biblio.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.biblio.model.Autore;

public interface AutoreRepository extends CrudRepository<Autore, Long> {

    public boolean existsByNomeAndCognome(String nome, String cognome);
    
    public Optional<Autore> findByNomeAndCognome(String nome, String cognome);
}
