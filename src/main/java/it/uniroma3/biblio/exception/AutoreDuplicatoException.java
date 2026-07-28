package it.uniroma3.biblio.exception;

public class AutoreDuplicatoException extends RuntimeException {
	public AutoreDuplicatoException(String nome, String cognome) {
		super("L'autore " + nome + " " + cognome + " è già presente nel sistema.");
	}
}
