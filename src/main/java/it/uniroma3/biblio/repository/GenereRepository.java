package it.uniroma3.biblio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.biblio.model.Genere;

public interface GenereRepository extends CrudRepository<Genere, Long> {

    public boolean existsByNome(String nome);

    /**
     * Usato dalla ricerca globale (barra di ricerca presente in tutte le pagine): trova i
     * generi il cui nome contiene il testo cercato.
     */
    List<Genere> findByNomeContainingIgnoreCase(String nome);

    /**
     * Caso d'uso: "visualizzazione del dettaglio di un genere" (pagina /genere/{id}), che
     * mostra l'elenco dei libri del genere con, per ciascuno, il nome dell'autore.
     *
     * Senza questo metodo, GenereController.getGenere() farebbe:
     *   1) una query per il Genere (findById)
     *   2) una query per la collezione genere.libri (@OneToMany LAZY)
     *   3) una query aggiuntiva per OGNI libro, per caricarne l'autore
     *      (Libro.autore è @ManyToOne senza "fetch" esplicito => EAGER di default, ma
     *      senza join risolto con una SELECT separata per riga: è comunque un N+1)
     *
     * Con questo metodo tutto avviene in un'unica query.
     */
    @Query("SELECT DISTINCT g FROM Genere g " +
           "LEFT JOIN FETCH g.libri l " +
           "LEFT JOIN FETCH l.autore " +
           "WHERE g.id = :id")
    Optional<Genere> findByIdWithLibriEAutori(@Param("id") Long id);
}