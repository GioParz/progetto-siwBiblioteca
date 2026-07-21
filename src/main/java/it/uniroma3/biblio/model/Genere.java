package it.uniroma3.biblio.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Genere {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genere_seq")
	@SequenceGenerator(name = "genere_seq", sequenceName = "genere_seq", allocationSize = 1)
	private Long id;
	
	@NotBlank
    @Column(nullable = false, unique = true)
    private String nome;

    @Column(length = 1000, nullable = true)
    private String descrizione;

    @OneToMany(mappedBy = "genere")
    private List<Libro> libri = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public List<Libro> getLibri() {
		return libri;
	}

	public void setLibri(List<Libro> libri) {
		this.libri = libri;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Genere other = (Genere) obj;
		return Objects.equals(nome, other.nome);
	}
}
