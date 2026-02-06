package org.ing.ispw.unifix.model;

import org.ing.ispw.unifix.utils.EmailParserService;
import org.ing.ispw.unifix.utils.UserType;


/*
 Perchè uso una factory per creare gli utenti?
    La UserFactory incapsula la logica di creazione degli oggetti User, permettendo di centralizzare e semplificare il processo di istanziazione.
    Questo approccio migliora la manutenibilità del codice, poiché eventuali modifiche alla logica di creazione degli utenti possono essere apportate in un unico punto.
    Inoltre, la factory consente di astrarre i dettagli di implementazione delle sottoclassi di User (come Docente, Tecnico, Sysadmin), facilitando l'estensione futura del sistema con nuovi tipi di utenti senza modificare il codice client.
 */
public class UserFactory {

    private final EmailParserService emailParserService;

    public UserFactory(EmailParserService emailParserService) {
        this.emailParserService = emailParserService;
    }

    public User createUser(String email, String password, UserType ruolo) {
        String nome = emailParserService.extractNome(email);
        String cognome = emailParserService.extractCognome(email);

        return switch (ruolo) {
            case DOCENTE -> new Docente(email, password, nome, cognome, ruolo);
            case TECNICO -> new Tecnico(email, password, nome, cognome, ruolo, 0);
            case SYSADMIN -> new Sysadmin(email, password, nome, cognome, ruolo);
            default -> throw new IllegalArgumentException("Ruolo non supportato: " + ruolo);
        };
    }
}
