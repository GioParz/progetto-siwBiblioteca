package it.uniroma3.biblio.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.biblio.model.Utente;
import it.uniroma3.biblio.repository.UtenteRepository;

@Service
@Transactional(readOnly = true)
public class UtenteService {
	
	private final UtenteRepository utenteRepository;
	
	public UtenteService(UtenteRepository utenteRepository) {
		this.utenteRepository = utenteRepository;
	}

	public Utente getUtente(Long id) {
		return this.utenteRepository.findById(id).orElse(null);
	}
	
	public List<Utente> getAllUtenti() {
		return (List<Utente>) this.utenteRepository.findAll();
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public Utente saveUtente(Utente utente) {
		return this.utenteRepository.save(utente);
	}
}
