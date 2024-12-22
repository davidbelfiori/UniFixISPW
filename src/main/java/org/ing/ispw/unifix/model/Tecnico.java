package org.ing.ispw.unifix.model;

public class Tecnico extends User{
    private int numeroSegnalazioni;

    public Tecnico(String email) {
        super(email);
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
