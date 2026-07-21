package it.uniroma3.biblio.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.biblio.model.Utente;

public interface UtenteRepository extends CrudRepository<Utente, Long> {

    public boolean existsByEmail(String email);
    
    public Optional<Utente> findByEmail(String email);
}
