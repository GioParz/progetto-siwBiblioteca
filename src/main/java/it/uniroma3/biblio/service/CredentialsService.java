package it.uniroma3.biblio.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.biblio.model.Credentials;
import it.uniroma3.biblio.repository.CredentialsRepository;
import it.uniroma3.biblio.repository.UtenteRepository;

@Service
@Transactional(readOnly = true)
public class CredentialsService {
	
	private final CredentialsRepository credentialsRepository;
	private final UtenteRepository utenteRepository;
	private final PasswordEncoder passwordEncoder;
	
	public CredentialsService(CredentialsRepository credentialsRepository,
			UtenteRepository utenteRepository,
			PasswordEncoder passwordEncoder) {
		this.credentialsRepository = credentialsRepository;
		this.utenteRepository = utenteRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public Credentials getCredentialsById(Long id) {
		return this.credentialsRepository.findById(id).orElse(null);
	}
	
	public Credentials getCredentialsByUsername(String username) {
		return this.credentialsRepository.findByUsername(username).orElse(null);
	}
	
	public boolean existsByUsername(String username) {
		return this.credentialsRepository.existsByUsername(username);
	}
	
	public boolean existsByUtenteEmail(String email) {
		return this.utenteRepository.existsByEmail(email);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public Credentials saveCredentials(Credentials credentials) {
		
		String passwordInChiaro = credentials.getPassword();
		String passwordCifrata = this.passwordEncoder.encode(passwordInChiaro);
		
		credentials.setPassword(passwordCifrata);
		
		return this.credentialsRepository.save(credentials);
	}
}
