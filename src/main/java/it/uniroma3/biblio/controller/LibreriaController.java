package it.uniroma3.biblio.controller;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.biblio.exception.LibroGiaInLibreriaException;
import it.uniroma3.biblio.model.Credentials;
import it.uniroma3.biblio.model.ElementoLibreria;
import it.uniroma3.biblio.model.StatoLettura;
import it.uniroma3.biblio.model.Utente;
import it.uniroma3.biblio.service.ConsigliService;
import it.uniroma3.biblio.service.CredentialsService;
import it.uniroma3.biblio.service.ElementoLibreriaService;

@Controller
public class LibreriaController {

	private static final int ANTEPRIMA_CONSIGLI_IN_LIBRERIA = 3;

	private final ElementoLibreriaService elementoLibreriaService;
	private final ConsigliService consigliService;
	private final CredentialsService credentialsService;

	public LibreriaController(ElementoLibreriaService elementoLibreriaService, ConsigliService consigliService,
			CredentialsService credentialsService) {
		this.elementoLibreriaService = elementoLibreriaService;
		this.consigliService = consigliService;
		this.credentialsService = credentialsService;
	}

	// metodo helper per recuperare l'Utente di dominio a partire dall'utente autenticato da Spring Security
	private Utente getUtenteLoggato(UserDetails userDetails) {
		Credentials credentials = this.credentialsService.getCredentialsByUsername(userDetails.getUsername());
		return credentials.getUtente();
	}

	/* VISUALIZZAZIONE LIBRERIA PERSONALE */

	@GetMapping("/libreria")
	public String getLibreria(@AuthenticationPrincipal UserDetails userDetails, Model model) {

		Utente utente = this.getUtenteLoggato(userDetails);

		List<ElementoLibreria> libreria = this.elementoLibreriaService.findByUtente(utente);

		model.addAttribute("libreria", libreria);
		model.addAttribute("statiLettura", StatoLettura.values());

		// Il motore dei consigli (funzionalità riservata) compare qui, dentro la libreria
		// personale, come piccola anteprima: il link "Vedi tutti i consigli" porta alla
		// pagina dedicata /consigli con la lista completa.
		model.addAttribute("anteprimaConsigli",
				this.consigliService.calcolaLibriConsigliati(utente, ANTEPRIMA_CONSIGLI_IN_LIBRERIA));

		return "libreria/list";
	}

	/* AGGIUNTA DI UN LIBRO ALLA LIBRERIA PERSONALE */

	@PostMapping("/libreria/aggiungi/{libroId}")
	public String aggiungiALibreria(@PathVariable("libroId") Long libroId,
			@AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {

		Long utenteId = this.getUtenteLoggato(userDetails).getId();

		try {
			this.elementoLibreriaService.aggiungiLibroALibreria(utenteId, libroId);
			redirectAttributes.addFlashAttribute("successMessage", "Libro aggiunto alla tua libreria!");
		} catch (LibroGiaInLibreriaException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}

		return "redirect:/libro/" + libroId;
	}

	/* AGGIORNAMENTO STATO LETTURA E VALUTAZIONE */

	@PostMapping("/libreria/{elementoId}/aggiorna")
	public String aggiornaSchedaLettura(@PathVariable("elementoId") Long elementoId,
			@RequestParam(value = "statoLettura", required = false) StatoLettura statoLettura,
			@RequestParam(value = "valutazione", required = false) Integer valutazione,
			@AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {

		Utente utente = this.getUtenteLoggato(userDetails);

		try {
			this.elementoLibreriaService.aggiornaSchedaLettura(elementoId, statoLettura, valutazione, utente);
			redirectAttributes.addFlashAttribute("successMessage", "Scheda di lettura aggiornata.");
		} catch (SecurityException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}

		return "redirect:/libreria";
	}

	/* RIMOZIONE DI UN LIBRO DALLA LIBRERIA PERSONALE */

	@PostMapping("/libreria/{elementoId}/rimuovi")
	public String rimuoviDaLibreria(@PathVariable("elementoId") Long elementoId,
			@AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {

		Utente utente = this.getUtenteLoggato(userDetails);

		try {
			this.elementoLibreriaService.rimuoviDaLibreria(elementoId, utente);
			redirectAttributes.addFlashAttribute("successMessage", "Libro rimosso dalla tua libreria.");
		} catch (AccessDeniedException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}

		return "redirect:/libreria";
	}

	/* MOTORE DI RACCOMANDAZIONE (pagina dedicata, lista completa) */

	@GetMapping("/consigli")
	public String getConsigli(@AuthenticationPrincipal UserDetails userDetails, Model model) {

		Utente utente = this.getUtenteLoggato(userDetails);

		model.addAttribute("libriConsigliati", this.consigliService.calcolaLibriConsigliati(utente));

		return "libreria/consigli";
	}
}