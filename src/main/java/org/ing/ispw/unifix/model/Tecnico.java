package org.ing.ispw.unifix.model;

import org.ing.ispw.unifix.utils.Printer;
import org.ing.ispw.unifix.utils.StatoSegnalazione;
import org.ing.ispw.unifix.utils.UserType;
import org.ing.ispw.unifix.utils.observer.Observer;

public class Tecnico extends User implements Observer {
    private int numeroSegnalazioni;

    public Tecnico(String email) {
        super(email);
    }

    public Tecnico(String email, String password, String nome, String cognome, UserType ruolo, int numeroSegnalazioni) {
        super(email, password, nome, cognome, ruolo);
        this.numeroSegnalazioni = numeroSegnalazioni;
    }

    public Tecnico(String emailTecnico, String nomeTecnico, String cognomeTecnico) {
        super(emailTecnico, nomeTecnico, cognomeTecnico);
    }

    public int getNumeroSegnalazioni() {
        return numeroSegnalazioni;
    }

    public void setNumeroSegnalazioni(int numeroSegnalazioni) {
        this.numeroSegnalazioni = numeroSegnalazioni;
    }

    public void incrementaSegnalazioni() {
        this.numeroSegnalazioni++;
    }

    // Method to decrement the number of reports
    public void decrementaSegnalazioni() {
        if (this.numeroSegnalazioni > 0) {
            this.numeroSegnalazioni--;
        }
    }

    @Override
    public void update() {
        // Vuoto: per le notifiche email usiamo update(Object eventData)

    }

    @Override
    public void update(Object eventData) {
        if (eventData instanceof Segnalazione s && s.getStato() == StatoSegnalazione.APERTA && s.getTecnico().equals(this)) {
                Printer.print("[SIMULAZIONE EMAIL TECNICO -> " + getEmail() + "]: " +
                        "Ti è stata assegnata una nuova segnalazione #" + s.getIdSegnalazione() +
                        " per l'aula " + s.getAula());
            }

    }
}
