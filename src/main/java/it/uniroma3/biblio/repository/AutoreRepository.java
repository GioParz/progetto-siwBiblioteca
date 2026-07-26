package it.uniroma3.biblio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.biblio.model.Autore;

public interface AutoreRepository extends CrudRepository<Autore, Long> {

    public boolean existsByNomeAndCognome(String nome, String cognome);
    
    public Optional<Autore> findByNomeAndCognome(String nome, String cognome);

    /**
     * Usato dalla ricerca globale (barra di ricerca presente in tutte le pagine): trova gli
     * autori il cui nome o cognome contiene il testo cercato.
     */
    List<Autore> findByNomeContainingIgnoreCaseOrCognomeContainingIgnoreCase(String nome, String cognome);

    /**
     * Caso d'uso: "visualizzazione del dettaglio di un autore" (pagina /autore/{id}), che
     * mostra l'elenco delle opere dell'autore con, per ciascuna, il nome del genere.
     */
    @Query("SELECT DISTINCT a FROM Autore a " +
           "LEFT JOIN FETCH a.libri l " +
           "LEFT JOIN FETCH l.genere " +
           "WHERE a.id = :id")
    Optional<Autore> findByIdWithLibriEGeneri(@Param("id") Long id);
}