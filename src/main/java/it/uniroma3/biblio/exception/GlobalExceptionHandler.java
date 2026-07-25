package it.uniroma3.biblio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(LibroGiaInLibreriaException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleLibroGiaInLibreria(LibroGiaInLibreriaException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/libreria";
    }

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public String handleAccessDeniedException(AccessDeniedException e, Model model) {
		model.addAttribute("errorMessage", e.getMessage());
		return "error/403";
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleNoResourceFound(ResourceNotFoundException e, Model model) {
		model.addAttribute("errorMessage", e.getMessage());
		return "error/404";
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleGenericException(Exception e, Model model) {
		model.addAttribute("errorMessage", e.getMessage());
		return "error/500";
	}
}