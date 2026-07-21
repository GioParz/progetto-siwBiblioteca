package it.uniroma3.biblio.model;

public enum StatoLettura {
	
	DA_LEGGERE("Da Leggere"),
    IN_LETTURA("In Lettura"),
    COMPLETATO("Completato");

    private final String etichetta;

    StatoLettura(String etichetta) {
        this.etichetta = etichetta;
    }

    public String getEtichetta() {
        return etichetta;
    }
}
