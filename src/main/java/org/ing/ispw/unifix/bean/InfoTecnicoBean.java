package org.ing.ispw.unifix.bean;

import org.ing.ispw.unifix.utils.UserType;

public class InfoTecnicoBean {
    private String nome;
    private String cognome;
    private String email;
    private UserType ruolo;
    private int numeroSegnalazioni;

    public InfoTecnicoBean() {
        //empty constructor
    }

    public InfoTecnicoBean(int numeroSegnalazioni, String email, String cognome, String nome) {
        this.numeroSegnalazioni = numeroSegnalazioni;
        this.email = email;
        this.cognome = cognome;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }


    public UserType getRuolo() {
        return ruolo;
    }

    public int getNumeroSegnalazioni() {
        return numeroSegnalazioni;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("nome cannot be null or empty");
        }

        this.nome = nome;
    }

    public void setCognome(String cognome) {
        if (cognome == null || cognome.trim().isEmpty()) {
            throw new IllegalArgumentException("cognome cannot be null or empty");
        }
        this.cognome = cognome;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("email cannot be null or empty");
        }
        this.email = email;
    }

    public void setRuolo(UserType ruolo) {
        this.ruolo = ruolo;
    }

    public void setNumeroSegnalazioni(int numeroSegnalazioni) {
        if(numeroSegnalazioni < 0) {
            throw new IllegalArgumentException("numeroSegnalazioni cannot be negative");
        }
        this.numeroSegnalazioni = numeroSegnalazioni;
    }
}
