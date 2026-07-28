package it.uniroma3.biblio.exception;

public class LibroDuplicatoException extends RuntimeException {
	public LibroDuplicatoException(String titolo) {
		super("Il libro '"+ titolo +"' è già presente nel catalogo.");
	}
}
