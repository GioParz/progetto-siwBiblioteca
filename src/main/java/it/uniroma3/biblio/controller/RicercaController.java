package it.uniroma3.biblio.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.biblio.model.Autore;
import it.uniroma3.biblio.model.Genere;
import it.uniroma3.biblio.model.Libro;
import it.uniroma3.biblio.service.AutoreService;
import it.uniroma3.biblio.service.GenereService;
import it.uniroma3.biblio.service.LibroService;

/**
 * Gestisce la barra di ricerca globale presente in tutte le pagine del sito: 
 * cerca contemporaneamente tra libri, autori e generi 
 * e mostra tutti i risultati raggruppati in un'unica pagina.
 */
@Controller
public class RicercaController {

	private final LibroService libroService;
	private final AutoreService autoreService;
	private final GenereService genereService;

	public RicercaController(LibroService libroService, AutoreService autoreService, GenereService genereService) {
		this.libroService = libroService;
		this.autoreService = autoreService;
		this.genereService = genereService;
	}

	@GetMapping("/ricerca")
	public String ricercaGlobale(@RequestParam(value = "q", required = false) String q, Model model) {

		String query = (q != null) ? q.trim() : "";

		List<Libro> libri = List.of();
		List<Autore> autori = List.of();
		List<Genere> generi = List.of();

		if (!query.isBlank()) {
			libri = this.libroService.cercaGlobale(query);
			autori = this.autoreService.cerca(query);
			generi = this.genereService.cerca(query);
		}

		model.addAttribute("query", q);
		model.addAttribute("libri", libri);
		model.addAttribute("autori", autori);
		model.addAttribute("generi", generi);

		return "ricerca/risultati";
	}
}