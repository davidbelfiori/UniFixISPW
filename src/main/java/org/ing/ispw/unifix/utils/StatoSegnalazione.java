package org.ing.ispw.unifix.utils;

public enum StatoSegnalazione {
    APERTA("APERTA"),
    IN_LAVORAZIONE("IN LAVORAZIONE"),
    CHIUSA("CHIUSA");
    private final String displayName;
    StatoSegnalazione(String displayName) { this.displayName = displayName; }

    public static StatoSegnalazione fromString(String value) {
        if (value == null) return null;
        return valueOf(value.replace(" ", "_").toUpperCase());
    }
}
