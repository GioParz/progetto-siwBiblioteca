package it.uniroma3.biblio.model;

import java.util.Objects;

import it.uniroma3.biblio.validation.NotAnnoFuturo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Libro {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "libro_seq")
	@SequenceGenerator(name = "libro_seq", sequenceName = "libro_seq", allocationSize = 1)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String titolo;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String isbn;

    @NotNull
    @NotAnnoFuturo
    @Column(nullable = false)
    private Integer annoPubblicazione;

    @Column(length = 2000, nullable = true)
    private String trama;
    
    @Size(max = 2048, message = "L'URL dell'immagine è troppo lungo (max 2048 caratteri)")
	@Pattern(regexp = "(https?://.*)?", message = "Deve essere un URL valido (es. http://... o https://...) o rimanere vuoto")
    @Column(nullable = true)
    private String copertinaUrl;

    @ManyToOne
    @NotNull
    @JoinColumn(nullable = false)
    private Autore autore;

    @ManyToOne
    @NotNull
    @JoinColumn(nullable = false)
    private Genere genere;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Integer getAnnoPubblicazione() {
		return annoPubblicazione;
	}

	public void setAnnoPubblicazione(Integer annoPubblicazione) {
		this.annoPubblicazione = annoPubblicazione;
	}

	public String getTrama() {
		return trama;
	}

	public void setTrama(String trama) {
		this.trama = trama;
	}

	public String getCopertinaUrl() {
		return copertinaUrl;
	}

	public void setCopertinaUrl(String copertinaUrl) {
		this.copertinaUrl = copertinaUrl;
	}

	public Autore getAutore() {
		return autore;
	}

	public void setAutore(Autore autore) {
		this.autore = autore;
	}

	public Genere getGenere() {
		return genere;
	}

	public void setGenere(Genere genere) {
		this.genere = genere;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(isbn);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Libro other = (Libro) obj;
		return Objects.equals(isbn, other.isbn);
	}
}
