package org.ing.ispw.unifix.utils;

public enum Answer {
    ERRORE("Errore"),
    SUCCESSO("Successo");

    private final String value;

    Answer(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
