package it.uniroma3.biblio.controller;

import java.io.IOException;
import java.security.Principal;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.biblio.exception.LibroDuplicatoException;
import it.uniroma3.biblio.exception.LibroInUsoException;
import it.uniroma3.biblio.exception.ResourceNotFoundException;
import it.uniroma3.biblio.model.Libro;
import it.uniroma3.biblio.model.Utente;
import it.uniroma3.biblio.service.AutoreService;
import it.uniroma3.biblio.service.CloudinaryService;
import it.uniroma3.biblio.service.CopertineCacheService;
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
	private final CloudinaryService cloudinaryService;
	private final CopertineCacheService copertineCacheService;

	public LibroController(LibroService libroService, AutoreService autoreService, GenereService genereService,
	        ElementoLibreriaService elementoLibreriaService, CredentialsService credentialsService,
	        CloudinaryService cloudinaryService, CopertineCacheService copertineCacheService) {
	    this.libroService = libroService;
	    this.autoreService = autoreService;
	    this.genereService = genereService;
	    this.elementoLibreriaService = elementoLibreriaService;
	    this.credentialsService = credentialsService;
	    this.cloudinaryService = cloudinaryService;
	    this.copertineCacheService = copertineCacheService;
	}

	/* CONSULTAZIONE PUBBLICA DEL CATALOGO (con ricerca, compresa quella live di React) */

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
			Principal principal) {

		Libro libro = this.libroService.findById(id);

		model.addAttribute("libro", libro);
		model.addAttribute("mediaVoto", this.elementoLibreriaService.calcolaMediaVotoLibro(libro));

		// se l'utente è loggato, verifica se il libro è già nella sua libreria personale
		if (principal != null) {
			Utente utenteLoggato = this.credentialsService.getCredentialsByUsername(principal.getName()).getUtente();
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
	public String saveLibro(@Valid @ModelAttribute("libro") Libro libro, BindingResult bindingResult,
	        @RequestParam(value = "copertinaFile", required = false) MultipartFile copertinaFile,
	        Model model) {

	    if (copertinaFile != null && !copertinaFile.isEmpty()) {
	        try {
	            String url = this.cloudinaryService.caricaCopertina(copertinaFile);
	            libro.setCopertinaUrl(url);
	        } catch (IOException e) {
	            model.addAttribute("errorMessage", "Errore durante il caricamento dell'immagine.");
	            model.addAttribute("autori", this.autoreService.findAll());
	            model.addAttribute("generi", this.genereService.findAll());
	            return "admin/libri/form";
	        }
	    }

	    if (bindingResult.hasErrors()) {
	        model.addAttribute("autori", this.autoreService.findAll());
	        model.addAttribute("generi", this.genereService.findAll());
	        return "admin/libri/form";
	    }

	    try {
	        this.libroService.save(libro);
	    } catch (LibroDuplicatoException e) {
	    	bindingResult.reject("libro.duplicate", e.getMessage());
	    	model.addAttribute("autori", this.autoreService.findAll());
	        model.addAttribute("generi", this.genereService.findAll());
	        return "admin/libri/form";
	    } catch (DataIntegrityViolationException e) {
	        bindingResult.rejectValue("isbn", "libro.isbnDuplicato");
	        model.addAttribute("autori", this.autoreService.findAll());
	        model.addAttribute("generi", this.genereService.findAll());
	        return "admin/libri/form";
	    }

	    // Salvataggio riuscito: se c'è una copertina, la ricordiamo nella cache locale
	    // così sopravvive ai riavvii
	    this.copertineCacheService.salva(libro.getIsbn(), libro.getCopertinaUrl());

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
	        BindingResult bindingResult,
	        @RequestParam(value = "copertinaFile", required = false) MultipartFile copertinaFile,
	        Model model) {

	    if (copertinaFile != null && !copertinaFile.isEmpty()) {
	        try {
	            String url = this.cloudinaryService.caricaCopertina(copertinaFile);
	            libroModificato.setCopertinaUrl(url);
	        } catch (IOException e) {
	            model.addAttribute("errorMessage", "Errore durante il caricamento dell'immagine.");
	            model.addAttribute("autori", this.autoreService.findAll());
	            model.addAttribute("generi", this.genereService.findAll());
	            return "admin/libri/form";
	        }
	    } else {
	        // nessun nuovo file caricato: mantieni la copertina già esistente
	        Libro libroEsistente = this.libroService.findById(id);
	        libroModificato.setCopertinaUrl(libroEsistente.getCopertinaUrl());
	    }

	    if (bindingResult.hasErrors()) {
	        model.addAttribute("autori", this.autoreService.findAll());
	        model.addAttribute("generi", this.genereService.findAll());
	        return "admin/libri/form";
	    }

	    libroModificato.setId(id);

	    try {
	        this.libroService.save(libroModificato);
	    } catch (LibroDuplicatoException e) {
	    	bindingResult.reject("libro.duplicato", e.getMessage());
	    	model.addAttribute("autori", this.autoreService.findAll());
	        model.addAttribute("generi", this.genereService.findAll());
	        return "admin/libri/form";
	    } catch (DataIntegrityViolationException e) {
	        bindingResult.rejectValue("isbn", "libro.isbnDuplicato", "Esiste già un libro con questo ISBN.");
	        model.addAttribute("autori", this.autoreService.findAll());
	        model.addAttribute("generi", this.genereService.findAll());
	        return "admin/libri/form";
	    }

	    // Salvataggio riuscito: aggiorniamo/confermiamo la cache locale ISBN -> URL.
	    this.copertineCacheService.salva(libroModificato.getIsbn(), libroModificato.getCopertinaUrl());

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