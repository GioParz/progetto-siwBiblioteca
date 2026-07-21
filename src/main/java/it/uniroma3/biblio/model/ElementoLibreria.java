package it.uniroma3.biblio.model;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(
    name = "elemento_libreria",
    uniqueConstraints = @UniqueConstraint(columnNames = {"utente_id", "libro_id"})
)
public class ElementoLibreria {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "elementolibreria_seq")
	@SequenceGenerator(name = "elementolibreria_seq", sequenceName = "elementolibreria_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @ManyToOne
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoLettura statoLettura;
    
    @Min(1)
    @Max(5)
    @Column(nullable = true)
    private Integer valutazione; // Valutazione in stelle (1-5)
    
    private LocalDate dataAggiunta;
    
    @PrePersist
    public void prePersist() {
        if (this.dataAggiunta == null) {
            this.dataAggiunta = LocalDate.now();
        }
        if (this.statoLettura == null) {
            this.statoLettura = StatoLettura.DA_LEGGERE;
        }
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public Libro getLibro() {
		return libro;
	}

	public void setLibro(Libro libro) {
		this.libro = libro;
	}

	public StatoLettura getStatoLettura() {
		return statoLettura;
	}

	public void setStatoLettura(StatoLettura statoLettura) {
		this.statoLettura = statoLettura;
	}

	public Integer getValutazione() {
		return valutazione;
	}

	public void setValutazione(Integer valutazione) {
		this.valutazione = valutazione;
	}

	public LocalDate getDataAggiunta() {
		return dataAggiunta;
	}

	public void setDataAggiunta(LocalDate dataAggiunta) {
		this.dataAggiunta = dataAggiunta;
	}

	@Override
	public int hashCode() {
		return Objects.hash(libro, utente);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		ElementoLibreria other = (ElementoLibreria) obj;
		return Objects.equals(libro, other.libro) && Objects.equals(utente, other.utente);
	}

}
