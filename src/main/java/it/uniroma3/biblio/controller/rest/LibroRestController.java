package it.uniroma3.biblio.controller.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.biblio.model.Libro;
import it.uniroma3.biblio.service.LibroService;

@RestController
@RequestMapping("/api/libri")
public class LibroRestController {

	private final LibroService libroService;

	public LibroRestController(LibroService libroService) {
		this.libroService = libroService;
	}

	// metodo helper per evitare che Jackson vada in loop (Libro -> Autore -> libri -> Autore...)
	// serializzando una semplice entità JPA con relazioni bidirezionali
	private Map<String, Object> mappaLibro(Libro libro) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", libro.getId());
		map.put("titolo", libro.getTitolo());
		map.put("isbn", libro.getIsbn());
		map.put("annoPubblicazione", libro.getAnnoPubblicazione());
		map.put("trama", libro.getTrama());
		map.put("copertinaUrl", libro.getCopertinaUrl());

		if (libro.getAutore() != null) {
			Map<String, Object> autoreMap = new HashMap<>();
			autoreMap.put("id", libro.getAutore().getId());
			autoreMap.put("nome", libro.getAutore().getNome());
			autoreMap.put("cognome", libro.getAutore().getCognome());
			map.put("autore", autoreMap);
		}

		if (libro.getGenere() != null) {
			Map<String, Object> genereMap = new HashMap<>();
			genereMap.put("id", libro.getGenere().getId());
			genereMap.put("nome", libro.getGenere().getNome());
			map.put("genere", genereMap);
		}

		return map; // Spring/Jackson trasformerà la mappa in JSON
	}

	/* CU-5: RICERCA DINAMICA DEL CATALOGO (consumata dal componente React) */

	@GetMapping
	public ResponseEntity<?> cercaLibri(@RequestParam(value = "ricerca", required = false) String ricerca,
			@RequestParam(value = "genereId", required = false) Long genereId) {

		List<Libro> risultati = this.libroService.cercaLibri(ricerca, genereId);

		List<Map<String, Object>> risposta = risultati.stream()
				.map(this::mappaLibro)
				.toList();

		return ResponseEntity.ok(risposta);
	}
}