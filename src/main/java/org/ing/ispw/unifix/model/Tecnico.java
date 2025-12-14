package org.ing.ispw.unifix.model;

import org.ing.ispw.unifix.utils.UserType;

public class Tecnico extends User{
    private int numeroSegnalazioni;

    public Tecnico(String email) {
        super(email);
    }

    public Tecnico(String email, String password, String nome, String cognome, UserType ruolo, int numeroSegnalazioni) {
        super(email, password, nome, cognome, ruolo);
        this.numeroSegnalazioni = numeroSegnalazioni;
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
}
