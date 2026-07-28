package it.uniroma3.biblio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.biblio.exception.AutoreDuplicatoException;
import it.uniroma3.biblio.model.Autore;
import it.uniroma3.biblio.service.AutoreService;
import jakarta.validation.Valid;

@Controller
public class AutoreController {

	private final AutoreService autoreService;

	public AutoreController(AutoreService autoreService) {
		this.autoreService = autoreService;
	}

	/* CONSULTAZIONE AUTORI */

	@GetMapping("/autori")
	public String getAutori(Model model) {

		model.addAttribute("autori", this.autoreService.findAll());

		return "autori/list";
	}

	@GetMapping("/autore/{id}")
	public String getAutore(@PathVariable("id") Long id, Model model) {

		Autore autore = this.autoreService.findByIdConLibri(id);

		model.addAttribute("autore", autore);
		model.addAttribute("libri", autore.getLibri());

		return "autori/show";
	}

	/* INSERIMENTO NUOVO AUTORE */

	@GetMapping("/admin/autore/new")
	public String mostraFormAutore(Model model) {

		model.addAttribute("autore", new Autore());

		return "admin/autori/form";
	}

	@PostMapping("/admin/autori")
	public String saveAutore(@Valid @ModelAttribute("autore") Autore autore, BindingResult bindingResult) {

		if (bindingResult.hasErrors())
			return "admin/autori/form";

		try {
			this.autoreService.save(autore);
		} catch (AutoreDuplicatoException e) {
			bindingResult.reject("autore.duplicato", e.getMessage());
			return "admin/autori/form";
		}

		return "redirect:/autori";
	}
}