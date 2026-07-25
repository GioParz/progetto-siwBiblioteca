package it.uniroma3.biblio.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.biblio.exception.LibroInUsoException;
import it.uniroma3.biblio.exception.ResourceNotFoundException;
import it.uniroma3.biblio.model.Libro;
import it.uniroma3.biblio.model.Utente;
import it.uniroma3.biblio.service.AutoreService;
import it.uniroma3.biblio.service.CredentialsService;
import it.uniroma3.biblio.service.ElementoLibreriaService;
import it.uniroma3.biblio.service.GenereService;
import it.uniroma3.biblio.service.LibroService;
import jakarta.validation.Valid;

@Controller
public class LibroController {

	private final LibroService libroService;
	private final AutoreService autoreService;
	private final GenereService genereService;
	private final ElementoLibreriaService elementoLibreriaService;
	private final CredentialsService credentialsService;

	public LibroController(LibroService libroService, AutoreService autoreService, GenereService genereService,
			ElementoLibreriaService elementoLibreriaService, CredentialsService credentialsService) {
		this.libroService = libroService;
		this.autoreService = autoreService;
		this.genereService = genereService;
		this.elementoLibreriaService = elementoLibreriaService;
		this.credentialsService = credentialsService;
	}

	/* CONSULTAZIONE PUBBLICA DEL CATALOGO (fallback server-side, oltre alla ricerca live React) */

	@GetMapping("/libri")
	public String getLibri(@RequestParam(value = "ricerca", required = false) String ricerca,
			@RequestParam(value = "genereId", required = false) Long genereId, Model model) {

		model.addAttribute("libri", this.libroService.cercaLibri(ricerca, genereId));
		model.addAttribute("generi", this.genereService.findAll());
		model.addAttribute("ricerca", ricerca);
		model.addAttribute("genereId", genereId);

		return "libri/list";
	}

	/* DETTAGLIO LIBRO */

	@GetMapping("/libro/{id}")
	public String getLibro(@PathVariable("id") Long id, Model model,
			@AuthenticationPrincipal UserDetails userDetails) {

		Libro libro = this.libroService.findById(id);

		model.addAttribute("libro", libro);
		model.addAttribute("mediaVoto", this.elementoLibreriaService.calcolaMediaVotoLibro(libro));

		// se l'utente è loggato, verifica se il libro è già nella sua libreria personale
		if (userDetails != null) {
			Utente utenteLoggato = this.credentialsService.getCredentialsByUsername(userDetails.getUsername()).getUtente();
			boolean giaInLibreria = this.elementoLibreriaService.isLibroInLibreria(utenteLoggato, libro);
			model.addAttribute("giaInLibreria", giaInLibreria);
		}

		return "libri/show";
	}

	/* INSERIMENTO NUOVO LIBRO */

	@GetMapping("/admin/libro/new")
	public String mostraFormLibro(Model model) {

		model.addAttribute("libro", new Libro());
		model.addAttribute("autori", this.autoreService.findAll());
		model.addAttribute("generi", this.genereService.findAll());

		return "admin/libri/form";
	}

	@PostMapping("/admin/libri")
	public String saveLibro(@Valid @ModelAttribute("libro") Libro libro, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("autori", this.autoreService.findAll());
			model.addAttribute("generi", this.genereService.findAll());
			return "admin/libri/form";
		}

		try {
			this.libroService.save(libro);
		} catch (DataIntegrityViolationException e) {
			bindingResult.rejectValue("isbn", "libro.isbnDuplicato");
			model.addAttribute("autori", this.autoreService.findAll());
			model.addAttribute("generi", this.genereService.findAll());
			return "admin/libri/form";
		}

		return "redirect:/libri";
	}

	/* MODIFICA LIBRO */

	@GetMapping("/admin/libro/{id}/edit")
	public String mostraFormModifica(@PathVariable("id") Long id, Model model) {

		Libro libro = this.libroService.findById(id);

		model.addAttribute("libro", libro);
		model.addAttribute("autori", this.autoreService.findAll());
		model.addAttribute("generi", this.genereService.findAll());

		return "admin/libri/form";
	}

	@PostMapping("/admin/libro/{id}/edit")
	public String modificaLibro(@PathVariable("id") Long id, @Valid @ModelAttribute("libro") Libro libroModificato,
			BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("autori", this.autoreService.findAll());
			model.addAttribute("generi", this.genereService.findAll());
			return "admin/libri/form";
		}

		libroModificato.setId(id); // garantisce che sia un update e non un insert

		try {
			this.libroService.save(libroModificato);
		} catch (DataIntegrityViolationException e) {
			bindingResult.rejectValue("isbn", "libro.isbnDuplicato", "Esiste già un libro con questo ISBN.");
			model.addAttribute("autori", this.autoreService.findAll());
			model.addAttribute("generi", this.genereService.findAll());
			return "admin/libri/form";
		}

		return "redirect:/libro/" + id;
	}

	/* ELIMINAZIONE LIBRO */

	@PostMapping("/admin/libro/{id}/delete")
	public String eliminaLibro(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

		try {
			this.libroService.deleteById(id);
			redirectAttributes.addFlashAttribute("successMessage", "Libro eliminato con successo.");
		} catch (ResourceNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		} catch (LibroInUsoException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}

		return "redirect:/libri";
	}
}