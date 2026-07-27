package it.uniroma3.biblio.startup;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import it.uniroma3.biblio.model.Libro;
import it.uniroma3.biblio.repository.LibroRepository;
import it.uniroma3.biblio.service.CopertineCacheService;

/**
 * Ad ogni avvio dell'applicazione, DOPO che Hibernate ha (ri)creato lo schema
 * ed eseguito import.sql (che inserisce i 38 libri con copertina_url vuota),
 * questo componente legge la cache locale ISBN -> URL Cloudinary e ripristina
 * automaticamente gli URL corretti, individuando i libri per ISBN.
 *
 * I CommandLineRunner vengono eseguiti da Spring Boot dopo il completo refresh
 * del contesto applicativo, quindi girano sempre DOPO import.sql: l'ordine è
 * garantito senza bisogno di configurazioni aggiuntive.
 */
@Component
public class CopertineInitializer implements CommandLineRunner {

    private final LibroRepository libroRepository;
    private final CopertineCacheService copertineCacheService;

    public CopertineInitializer(LibroRepository libroRepository, CopertineCacheService copertineCacheService) {
        this.libroRepository = libroRepository;
        this.copertineCacheService = copertineCacheService;
    }

    @Override
    public void run(String... args) {
        Properties cache = this.copertineCacheService.carica();

        if (cache.isEmpty()) {
            return;
        }

        int aggiornati = 0;

        for (Map.Entry<Object, Object> entry : cache.entrySet()) {
            String isbn = (String) entry.getKey();
            String url = (String) entry.getValue();

            Optional<Libro> libroOpt = this.libroRepository.findByIsbn(isbn);
            if (libroOpt.isPresent()) {
                Libro libro = libroOpt.get();
                if (!url.equals(libro.getCopertinaUrl())) {
                    libro.setCopertinaUrl(url);
                    this.libroRepository.save(libro);
                    aggiornati++;
                }
            }
        }

        if (aggiornati > 0) {
            System.out.println("[CopertineInitializer] Ripristinate " + aggiornati + " copertine da cache locale.");
        }
    }
}