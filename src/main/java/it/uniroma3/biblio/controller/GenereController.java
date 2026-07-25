package it.uniroma3.biblio.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.biblio.model.Genere;
import it.uniroma3.biblio.service.GenereService;
import jakarta.validation.Valid;

@Controller
public class GenereController {

	private final GenereService genereService;

	public GenereController(GenereService genereService) {
		this.genereService = genereService;
	}

	/* CONSULTAZIONE GENERI */

	@GetMapping("/generi")
	public String getGeneri(Model model) {

		model.addAttribute("generi", this.genereService.findAll());

		return "generi/list";
	}

	@GetMapping("/genere/{id}")
	public String getGenere(@PathVariable("id") Long id, Model model) {

		Genere genere = this.genereService.findById(id);

		model.addAttribute("genere", genere);
		model.addAttribute("libri", genere.getLibri());

		return "generi/show";
	}

	/* INSERIMENTO NUOVO GENERE (Admin, usato anche dal form di creazione Libro) */

	@GetMapping("/admin/genere/new")
	public String mostraFormGenere(Model model) {

		model.addAttribute("genere", new Genere());

		return "admin/generi/form";
	}

	@PostMapping("/admin/generi")
	public String saveGenere(@Valid @ModelAttribute("genere") Genere genere, BindingResult bindingResult) {

		if (bindingResult.hasErrors())
			return "admin/generi/form";

		try {
			this.genereService.save(genere);
		} catch (DataIntegrityViolationException e) {
			bindingResult.rejectValue("nome", "genere.duplicato", "Esiste già un genere con questo nome.");
			return "admin/generi/form";
		}

		return "redirect:/generi";
	}
}