package org.ing.ispw.unifix.model;

import org.ing.ispw.unifix.utils.Printer;
import org.ing.ispw.unifix.utils.StatoSegnalazione;
import org.ing.ispw.unifix.utils.UserType;
import org.ing.ispw.unifix.utils.observer.Observer;

public class Docente extends User implements Observer {


    public Docente(String email) {
        super(email);
    }

    public Docente(String email, String password, String nome, String cognome, UserType ruolo) {
        super(email, password, nome, cognome, ruolo);
    }

    public Docente(String emailDocente, String nomeDocente, String cognomeDocente) {
        super(emailDocente, nomeDocente, cognomeDocente);
    }

    @Override
    public void update() {
        // Vuoto: per le notifiche email usiamo update(Object eventData)
    }
    @Override
    public void update(Object eventData) {
        if (eventData instanceof Segnalazione s && s.getDocente().equals(this) && s.getStato() != StatoSegnalazione.APERTA) {
                Printer.print("[SIMULAZIONE EMAIL DOCENTE -> " + getEmail() + "]: " +
                        "Lo stato della tua segnalazione #" + s.getIdSegnalazione() +
                        " è stato aggiornato a: " + s.getStato());
            }

    }
}

