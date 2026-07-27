package it.uniroma3.biblio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.biblio.service.GenereService;

@Controller
public class HomeController {

    private final GenereService genereService;

    public HomeController(GenereService genereService) {
		this.genereService = genereService;
	}

	@GetMapping("/")
    public String index(Model model) {
        
        model.addAttribute("generi", this.genereService.findAll());

        return "index";
    }
}