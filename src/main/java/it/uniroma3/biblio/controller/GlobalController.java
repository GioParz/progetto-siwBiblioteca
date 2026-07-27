package it.uniroma3.biblio.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalController {
    
	@ModelAttribute("userDetails")
	public UserDetails getUser() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
	        return null;
	    }

	    Object principal = authentication.getPrincipal();

	    // Con Google il principal è un DefaultOidcUser, non un UserDetails
	    if (principal instanceof UserDetails) {
	        return (UserDetails) principal;
	    }

	    return null;
	}
}
