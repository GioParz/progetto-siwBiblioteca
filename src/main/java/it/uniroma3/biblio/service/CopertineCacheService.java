package it.uniroma3.biblio.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Mantiene, in un file esterno al classpath (quindi non toccato da mvn clean
 * né dalla ricreazione dello schema), la corrispondenza ISBN -> URL Cloudinary
 * delle copertine caricate.
 *
 * Serve a "sopravvivere" ai riavvii dell'app quando
 * spring.jpa.hibernate.ddl-auto=create ricrea lo schema e import.sql
 * reinserisce i libri con copertina_url vuota: dopo l'avvio, CopertineInitializer
 * legge questa cache e ripristina gli URL corretti cercando i libri per ISBN.
 */
@Service
public class CopertineCacheService {

    // Default: file "copertine-cache.properties" nella cartella da cui parte l'app
    // (fuori da src/, quindi sopravvive a mvn clean, ricompilazioni e ddl-auto=create).
    @Value("${app.copertine.cache-path:data/copertine-cache.properties}")
    private String cachePath;

    public synchronized void salva(String isbn, String url) {
        if (isbn == null || isbn.isBlank() || url == null || url.isBlank()) {
            return;
        }
        
        /* carica la mappa di file .properties */
        Properties props = carica();
        props.setProperty(isbn, url);
        
        /* controlla se la cartella data esiste altrimenti la crea */
        File file = new File(this.cachePath);
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        
        /* apre il file e salva le coppie di proprietà */
        try (FileOutputStream out = new FileOutputStream(file)) {
            props.store(out, "Cache ISBN -> URL Cloudinary copertine (generata automaticamente, non modificare a mano)");
        } catch (IOException e) {
            // Non blocchiamo il salvataggio del libro solo perché la cache locale
            // non è scrivibile: logghiamo e andiamo avanti.
            System.err.println("[CopertineCacheService] Impossibile scrivere la cache copertine: " + e.getMessage());
        }
    }
    
    /**
     * legge il file da disco se esiste
     * @return un oggetto properties (una Map<String, String> nativa di java per leggere file .properties
     */
    public synchronized Properties carica() {
        Properties props = new Properties();
        File file = new File(this.cachePath);
        if (file.exists()) {
            try (FileInputStream in = new FileInputStream(file)) {
                props.load(in);
            } catch (IOException e) {
                System.err.println("[CopertineCacheService] Impossibile leggere la cache copertine: " + e.getMessage());
            }
        }
        return props;
    }
}