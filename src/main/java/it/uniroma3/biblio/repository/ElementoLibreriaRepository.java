package it.uniroma3.biblio.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.biblio.model.ElementoLibreria;
import it.uniroma3.biblio.model.Genere;
import it.uniroma3.biblio.model.Libro;
import it.uniroma3.biblio.model.Utente;

public interface ElementoLibreriaRepository extends CrudRepository<ElementoLibreria, Long> {

    public List<ElementoLibreria> findByUtente(Utente utente);

    public Optional<ElementoLibreria> findByUtenteAndLibro(Utente utente, Libro libro);

    public boolean existsByUtenteAndLibro(Utente utente, Libro libro);

    // Estrae i generi preferiti dall'utente (libri con voto >= 4 o salvati in libreria)
    @Query("SELECT DISTINCT el.libro.genere FROM ElementoLibreria el " +
           "WHERE el.utente = :utente AND (el.valutazione >= 4 OR el.valutazione IS NULL)")
    public List<Genere> findGeneriPreferitiDaUtente(@Param("utente") Utente utente);

    // Calcola la media voti complessiva di un libro (utilizzata nella scheda dettaglio libro)
    @Query("SELECT AVG(el.valutazione) FROM ElementoLibreria el WHERE el.libro = :libro AND el.valutazione IS NOT NULL")
    public Double findMediaValutazionePerLibro(@Param("libro") Libro libro);
}