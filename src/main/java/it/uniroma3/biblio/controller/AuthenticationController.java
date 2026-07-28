package it.uniroma3.biblio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.biblio.model.Credentials;
import it.uniroma3.biblio.model.RuoloUtente;
import it.uniroma3.biblio.model.Utente;
import it.uniroma3.biblio.service.CredentialsService;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {
	
	private final CredentialsService credentialsService;
	
	public AuthenticationController(CredentialsService credentialsService) {
		this.credentialsService = credentialsService;
	}
	
	/* LOGIN */
	
	@GetMapping("/login")
	public String mostraFormLogin(Model model) {
		return "authentication/login";
	}
	
	/* REGISTRAZIONE */
	
	@GetMapping("/register")
	public String mostraFormRegistrazione(Model model) {
		
		Credentials credentials = new Credentials();
		credentials.setUtente(new Utente());
		
		model.addAttribute("credentials", credentials);
		
		return "authentication/register";
	}
	
	@PostMapping("/register")
	public String registraUtente(@Valid @ModelAttribute("credentials") Credentials credentials,
			BindingResult bindingResult) {

		if(this.credentialsService.existsByUsername(credentials.getUsername())) {
			bindingResult.rejectValue("username", "credentials.duplicateUsername");
			return "authentication/register";
		}
		
		if(credentials.getUtente() != null &&
				this.credentialsService.existsByUtenteEmail(credentials.getUtente().getEmail())) {
			bindingResult.rejectValue("utente.email", "utente.duplicateEmail");
			return "authentication/register";
		}
		
		if(bindingResult.hasErrors())
			return "authentication/register";
		
		credentials.setRuolo(RuoloUtente.USER);
		this.credentialsService.saveCredentials(credentials);

		return "redirect:/login?registered";
	}
	
	/* ADMIN */
	
	@GetMapping("/admin/index")
	public String indexAdmin() {
		return "admin/index";
	}
}
