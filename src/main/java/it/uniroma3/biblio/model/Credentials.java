package it.uniroma3.biblio.model;

import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Credentials {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credentials_seq")
	@SequenceGenerator(name = "credentials_seq", sequenceName = "credentials_seq", allocationSize = 1)
    private Long id;
	
	@NotBlank
    @Column(unique = true, nullable = false)
    private String username;
	
	@NotBlank
	@Size(min = 3)
    @Column(nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    private RuoloUtente ruolo;
    
    @Valid
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    private Utente utente;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public RuoloUtente getRuolo() {
		return ruolo;
	}

	public void setRuolo(RuoloUtente ruolo) {
		this.ruolo = ruolo;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Credentials other = (Credentials) obj;
		return Objects.equals(username, other.username);
	}
}
