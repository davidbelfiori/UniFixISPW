package org.ing.ispw.unifix.model;

import java.util.Locale;

public record AulaId(String idAula, String edificio) {

    public AulaId {
        edificio = normalize(edificio, "edificio");
        idAula = normalize(idAula, "idAula");
    }

    private static String normalize(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " non può essere nullo o vuoto"
            );
        }

        return value.trim().toLowerCase(Locale.ROOT);
    }
}
