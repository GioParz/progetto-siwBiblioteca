package it.uniroma3.biblio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.biblio.model.Genere;
import it.uniroma3.biblio.model.Libro;
import it.uniroma3.biblio.model.Utente;

public interface LibroRepository extends CrudRepository<Libro, Long> {

    public boolean existsByIsbn(String isbn);

    public Optional<Libro> findByIsbn(String isbn);

    // Ricerca live dinamica (usata dalle API REST per React)
    @Query("SELECT l FROM Libro l WHERE " +
           "LOWER(l.titolo) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(l.autore.nome) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(l.autore.cognome) LIKE LOWER(CONCAT('%', :query, '%'))")
    public List<Libro> findByTitoloOAutoreContaining(@Param("query") String query);

    // Filtro combinato per Genere e Ricerca Testuale
    @Query("SELECT l FROM Libro l WHERE " +
           "(:genere IS NULL OR l.genere = :genere) AND " +
           "(:query IS NULL OR LOWER(l.titolo) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(l.autore.cognome) LIKE LOWER(CONCAT('%', :query, '%')))")
    public List<Libro> findByGenereEQuery(@Param("genere") Genere genere, @Param("query") String query);

    // SUGGERIMENTO 1: Trova libri dei generi preferiti dall'utente non ancora presente nella sua libreria
    @Query("SELECT DISTINCT l FROM Libro l WHERE l.genere IN :generi " +
           "AND l NOT IN (SELECT el.libro FROM ElementoLibreria el WHERE el.utente = :utente)")
    public List<Libro> findSuggeritiPerGeneriEUtente(@Param("generi") List<Genere> generi, @Param("utente") Utente utente);
    
    // Query con JOIN FETCH per evitare il problema N+1
    @Query("SELECT DISTINCT l FROM Libro l LEFT JOIN FETCH l.autore LEFT JOIN FETCH l.genere")
    List<Libro> findAllWithAutoreAndGenere();

    // In alternativa, carica in modo efficiente le relazioni EAGER per un singolo id
    @EntityGraph(attributePaths = {"autore", "genere"})
    Optional<Libro> findById(Long id);

    List<Libro> findByTitoloContainingIgnoreCase(String titolo);

    // Cerca libri del genere preferito che NON sono ancora nella libreria dell'utente
    @Query("SELECT l FROM Libro l WHERE l.genere.id = :genereId AND l.id NOT IN " +
           "(SELECT e.libro.id FROM ElementoLibreria e WHERE e.utente.id = :utenteId)")
    List<Libro> findConsigliatiByGenere(@Param("genereId") Long genereId, @Param("utenteId") Long utenteId);
}