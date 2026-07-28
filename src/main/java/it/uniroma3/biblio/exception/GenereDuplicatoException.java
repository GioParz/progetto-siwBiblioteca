package it.uniroma3.biblio.exception;

public class GenereDuplicatoException extends RuntimeException {
	public GenereDuplicatoException(String nome) {
		super("Il genere '" + nome + "' è già presente nel sistema.");
	}
}
