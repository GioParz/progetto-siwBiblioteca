package it.uniroma3.biblio.controller;

import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.biblio.model.Libro;
import it.uniroma3.biblio.model.Utente;
import it.uniroma3.biblio.service.ConsigliService;
import it.uniroma3.biblio.service.CredentialsService;
import it.uniroma3.biblio.service.GenereService;
import it.uniroma3.biblio.service.LibroService;

@Controller
public class HomeController {

    private final LibroService libroService;
    private final GenereService genereService;
    private final ConsigliService consigliService;
    private final CredentialsService credentialsService;

    public HomeController(LibroService libroService, GenereService genereService, ConsigliService consigliService,
			CredentialsService credentialsService) {
		this.libroService = libroService;
		this.genereService = genereService;
		this.consigliService = consigliService;
		this.credentialsService = credentialsService;
	}

	@GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        
        model.addAttribute("generi", this.genereService.findAll());

        if (userDetails != null) {
            // Se l'utente è autenticato, mostriamo i libri raccomandati per lui
        	Utente utente = this.credentialsService.getCredentialsByUsername(userDetails.getUsername()).getUtente();
            List<Libro> consigliati = this.consigliService.calcolaLibriConsigliati(utente);
            model.addAttribute("libriConsigliati", consigliati);
        } else {
            // Se visitatore anonimo, mostriamo i libri più recenti/generici
            model.addAttribute("libriConsigliati", this.libroService.findAll().stream().limit(6).toList());
        }

        return "index";
    }
}