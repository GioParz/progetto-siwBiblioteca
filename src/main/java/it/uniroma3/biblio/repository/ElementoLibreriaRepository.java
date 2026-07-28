package it.uniroma3.biblio.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.biblio.model.Autore;
import it.uniroma3.biblio.model.ElementoLibreria;
import it.uniroma3.biblio.model.Genere;
import it.uniroma3.biblio.model.Libro;
import it.uniroma3.biblio.model.Utente;

public interface ElementoLibreriaRepository extends CrudRepository<ElementoLibreria, Long> {
	
	boolean existsByUtenteAndLibro(Utente utente, Libro libro);
    
    boolean existsByLibroId(Long libroId);
    
    /**
     * Caso d'uso: "visualizzazione della libreria personale" (pagina /libreria), che per
     * ogni scheda di lettura mostra titolo, copertina e autore del libro collegato.
     *
     * Senza questo metodo, ElementoLibreriaService.findByUtente() genera un N+1 "a due
     * livelli": ElementoLibreria.libro è @ManyToOne EAGER non joinato (1 query aggiuntiva
     * per ogni elemento), e a sua volta Libro.autore è anch'esso @ManyToOne EAGER non
     * joinato (un'ulteriore query aggiuntiva per ogni libro). Per una libreria con N libri,
     * nel caso peggiore si arriva a 1 + 2N query invece di 1.
     */
    @Query("SELECT DISTINCT el FROM ElementoLibreria el " +
           "LEFT JOIN FETCH el.libro l " +
           "LEFT JOIN FETCH l.autore " +
           "WHERE el.utente = :utente")
    List<ElementoLibreria> findByUtenteWithLibroEAutore(@Param("utente") Utente utente);
    
    /**
     * Estrae i generi preferiti dall'utente: i generi con più libri valutati 4+ stelle, con peso
     */
    @Query("SELECT el.libro.genere AS genere, COUNT(el.libro.genere) AS peso FROM ElementoLibreria el " +
           "WHERE el.utente = :utente AND el.valutazione >= 4 " +
           "GROUP BY el.libro.genere " +
           "ORDER BY COUNT(el.libro.genere) DESC")
    List<GenerePreferito> findGeneriPreferitiConPesoDaUtente(@Param("utente") Utente utente);
    
    /* projection jpa */
    interface GenerePreferito {
        Genere getGenere();
        Long getPeso();
    }

    /**
     * Estrae gli autori preferiti dall'utente: gli autori con più libri valutati 4+ stelle, con peso
     */
    @Query("SELECT el.libro.autore AS autore, COUNT(el.libro.autore) AS peso FROM ElementoLibreria el " +
            "WHERE el.utente = :utente AND el.valutazione >= 4 " +
            "GROUP BY el.libro.autore " +
            "ORDER BY COUNT(el.libro.autore) DESC")
     List<AutorePreferito> findAutoriPreferitiConPesoDaUtente(@Param("utente") Utente utente);
    
    interface AutorePreferito {
        Autore getAutore();
        Long getPeso();
    }

    // Calcola la media voti complessiva di un libro (utilizzata nella scheda dettaglio libro)
    @Query("SELECT AVG(el.valutazione) FROM ElementoLibreria el WHERE el.libro = :libro AND el.valutazione IS NOT NULL")
    Double findMediaValutazionePerLibro(@Param("libro") Libro libro);
}