package org.ing.ispw.unifix.utils;

import org.ing.ispw.unifix.exception.RuoloNonTrovatoException;

public class EmailParserService {

    public String extractNome(String email) {
        String[] nameParts = getLocalPart(email).split("\\.");
        if (nameParts.length != 2) {
            throw new IllegalArgumentException("Formato del nome non valido");
        }
        return capitalize(nameParts[0]);
    }

    public String extractCognome(String email) {
        String[] nameParts = getLocalPart(email).split("\\.");
        if (nameParts.length != 2) {
            throw new IllegalArgumentException("Formato del cognome non valido");
        }
        return capitalize(nameParts[1]);
    }

    public UserType extractRuolo(String email) throws RuoloNonTrovatoException {
        String dominio = getDomainPart(email);
        return switch (dominio) {
            case "uniroma2.eu" -> UserType.DOCENTE;
            case "tec.uniroma2.eu" -> UserType.TECNICO;
            case "sys.uniroma2.eu" -> UserType.SYSADMIN;
            default -> throw new RuoloNonTrovatoException("Dominio non riconosciuto");
        };
    }

    private String getLocalPart(String email) {
        validateEmail(email);
        return email.split("@")[0];
    }

    private String getDomainPart(String email) {
        validateEmail(email);
        return email.split("@")[1];
    }

    private void validateEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email non valida");
        }
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
