package it.uniroma3.biblio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.biblio.model.Autore;
import it.uniroma3.biblio.model.Genere;
import it.uniroma3.biblio.model.Libro;
import it.uniroma3.biblio.model.Utente;

public interface LibroRepository extends CrudRepository<Libro, Long> {

    public boolean existsByIsbn(String isbn);

    public Optional<Libro> findByIsbn(String isbn);

    /**
     * Filtro combinato per Genere e Ricerca Testuale (usato da /libri quando ci sono filtri
     * attivi, e dalla API REST per la ricerca live di React).
     */
    @Query("SELECT DISTINCT l FROM Libro l " +
           "LEFT JOIN FETCH l.autore " +
           "LEFT JOIN FETCH l.genere " +
           "WHERE " +
           "(:genere IS NULL OR l.genere = :genere) AND " +
           "(:query IS NULL OR " +
           " LOWER(l.titolo) LIKE LOWER(CONCAT('%', CAST(:query AS string), '%')) OR " +
           " LOWER(l.autore.cognome) LIKE LOWER(CONCAT('%', CAST(:query AS string), '%')))")
    public List<Libro> findByGenereEQuery(@Param("genere") Genere genere, @Param("query") String query);
    
    // Query con JOIN FETCH per evitare il problema N+1 sull'intero catalogo.
    @Query("SELECT DISTINCT l FROM Libro l LEFT JOIN FETCH l.autore LEFT JOIN FETCH l.genere")
    List<Libro> findAllWithAutoreAndGenere();

    /**
     * Carica in modo efficiente le relazioni per un singolo id (usato dal dettaglio libro
     * e dal form di modifica).
     */
    @EntityGraph(attributePaths = {"autore", "genere"})
    Optional<Libro> findById(Long id);
    
    // SUGGERIMENTO 1: libri dei generi preferiti dall'utente non ancora in libreria.
    @Query("SELECT DISTINCT l FROM Libro l " +
           "LEFT JOIN FETCH l.autore " +
           "LEFT JOIN FETCH l.genere " +
           "WHERE l.genere IN :generi " +
           "AND l NOT IN (SELECT el.libro FROM ElementoLibreria el WHERE el.utente = :utente)")
    public List<Libro> findSuggeritiPerGeneriEUtente(@Param("generi") List<Genere> generi, @Param("utente") Utente utente);

    /**
     * SUGGERIMENTO 2: libri scritti dagli autori preferiti dall'utente (autori con
     * più libri valutati 4+ stelle) non ancora presenti nella sua libreria.
     */
    @Query("SELECT DISTINCT l FROM Libro l " +
           "LEFT JOIN FETCH l.autore " +
           "LEFT JOIN FETCH l.genere " +
           "WHERE l.autore IN :autori " +
           "AND l NOT IN (SELECT el.libro FROM ElementoLibreria el WHERE el.utente = :utente)")
    List<Libro> findSuggeritiPerAutoriEUtente(@Param("autori") List<Autore> autori, @Param("utente") Utente utente);

    /**
     * Usato dalla barra di ricerca globale (presente in tutte le pagine): cerca per titolo,
     * nome/cognome dell'autore o nome del genere.
     */
    @Query("SELECT DISTINCT l FROM Libro l " +
           "LEFT JOIN FETCH l.autore " +
           "LEFT JOIN FETCH l.genere " +
           "WHERE LOWER(l.titolo) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(l.autore.nome) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(l.autore.cognome) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(l.genere.nome) LIKE LOWER(CONCAT('%', :query, '%'))")
    public List<Libro> findByTitoloOAutoreOGenereContaining(@Param("query") String query);
}