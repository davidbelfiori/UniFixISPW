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
}
