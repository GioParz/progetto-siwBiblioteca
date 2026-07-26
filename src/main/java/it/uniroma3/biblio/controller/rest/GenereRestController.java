package it.uniroma3.biblio.controller.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.biblio.model.Genere;
import it.uniroma3.biblio.service.GenereService;

/**
 * Endpoint minimale pensato per il componente React (biblioteca-frontend): restituisce
 * l'elenco dei generi (solo id e nome) per popolare un eventuale filtro a tendina accanto
 * alla barra di ricerca live dei libri, in modo analogo a quanto già offre libri/list.html
 * lato Thymeleaf.
 */
@RestController
@RequestMapping("/api/generi")
public class GenereRestController {

	private final GenereService genereService;

	public GenereRestController(GenereService genereService) {
		this.genereService = genereService;
	}

	@GetMapping
	public ResponseEntity<?> getGeneri() {

		List<Map<String, Object>> risposta = this.genereService.findAll().stream()
				.map(this::mappaGenere)
				.toList();

		return ResponseEntity.ok(risposta);
	}

	private Map<String, Object> mappaGenere(Genere genere) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", genere.getId());
		map.put("nome", genere.getNome());
		return map;
	}
}