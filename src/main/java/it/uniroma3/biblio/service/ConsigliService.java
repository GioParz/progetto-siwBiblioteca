package it.uniroma3.biblio.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.biblio.model.Genere;
import it.uniroma3.biblio.model.Libro;
import it.uniroma3.biblio.model.Utente;
import it.uniroma3.biblio.repository.ElementoLibreriaRepository;
import it.uniroma3.biblio.repository.LibroRepository;

@Service
public class ConsigliService {

    private ElementoLibreriaRepository elementoLibreriaRepository;
    private LibroRepository libroRepository;
    private LibroService libroService;

    public ConsigliService(ElementoLibreriaRepository elementoLibreriaRepository, LibroRepository libroRepository,
			LibroService libroService) {
		this.elementoLibreriaRepository = elementoLibreriaRepository;
		this.libroRepository = libroRepository;
		this.libroService = libroService;
	}

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Libro> calcolaLibriConsigliati(Utente utente) {
        List<Genere> generiPreferiti = this.elementoLibreriaRepository.findGeneriPreferitiDaUtente(utente);

        if (generiPreferiti.isEmpty()) {
            return this.libroService.findAll().stream().limit(5).toList();
        }

        List<Libro> consigliati = this.libroRepository.findSuggeritiPerGeneriEUtente(generiPreferiti, utente);
        return consigliati.stream().limit(6).toList();
    }
}