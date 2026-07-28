package it.uniroma3.biblio.authentication;

import java.util.UUID;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.biblio.model.Credentials;
import it.uniroma3.biblio.model.RuoloUtente;
import it.uniroma3.biblio.model.Utente;
import it.uniroma3.biblio.repository.CredentialsRepository;
import it.uniroma3.biblio.repository.UtenteRepository;

                                             //estende la classe di spring security che gestisce il protocollo di OpenID Connect
@Service									 //permette di fare override di loadUser e definire la mia logica personalizzata		
public class CustomOAuth2UserService extends OidcUserService {

	private final CredentialsRepository credentialsRepository;
	private final UtenteRepository utenteRepository;
	private final PasswordEncoder passwordEncoder;

	public CustomOAuth2UserService(CredentialsRepository credentialsRepository,
			UtenteRepository utenteRepository, PasswordEncoder passwordEncoder) {
		this.credentialsRepository = credentialsRepository;
		this.utenteRepository = utenteRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional
	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		
		//fa fare a spring la prima chiamata a google per scaricare i dati del profilo
		OidcUser oidcUser = super.loadUser(userRequest);
		
		//estrae i dati dalla risposta json di google
		String email = oidcUser.getAttribute("email");
		String nome = oidcUser.getAttribute("given_name");
		String cognome = oidcUser.getAttribute("family_name");

		if (email == null) {
			throw new OAuth2AuthenticationException("Email non fornita da Google");
		}
		
		//cerca se ci sono già credenziali con quell'email come username
		Credentials credentials = this.credentialsRepository.findByUsername(email).orElse(null);

		if (credentials == null) {
			//cerca se già esisteva l'utente con quell'email
			Utente utente = this.utenteRepository.findByEmail(email).orElse(null);
			
			//se non esisteva ne crea uno nuovo con mail e nome/cognome di google
			if (utente == null) {
				utente = new Utente();
				utente.setEmail(email);
				utente.setNome(nome != null && !nome.isBlank() ? nome : "Utente");
				utente.setCognome(cognome != null && !cognome.isBlank() ? cognome : "Google");
			}
			
			//crea anche le credenziali associate all'utente
			credentials = new Credentials();
			credentials.setUsername(email);
			//genera una password segreta impossibile da indovinare (perche il DB richiede una password non null)
			credentials.setPassword(this.passwordEncoder.encode(UUID.randomUUID().toString()));
			credentials.setRuolo(RuoloUtente.USER);
			credentials.setOauth(true);
			credentials.setUtente(utente);

			this.credentialsRepository.save(credentials);
		}

		/**
		 * crea un oggetto DefaultOidcUser personalizzato con i ruoli del mio database (ADMIN/USER)
		 */
		return new DefaultOidcUser(
				java.util.List.of(new SimpleGrantedAuthority(credentials.getRuolo().name())),
				oidcUser.getIdToken(),
				oidcUser.getUserInfo(),
				"email");
	}
}