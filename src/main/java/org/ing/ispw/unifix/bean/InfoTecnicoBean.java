package org.ing.ispw.unifix.bean;

import org.ing.ispw.unifix.utils.UserType;

public class InfoTecnicoBean {
    private String nome;
    private String cognome;
    private String email;
    private String password;
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

    public String getPassword() {
        return password;
    }

    public UserType getRuolo() {
        return ruolo;
    }

    public int getNumeroSegnalazioni() {
        return numeroSegnalazioni;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRuolo(UserType ruolo) {
        this.ruolo = ruolo;
    }

    public void setNumeroSegnalazioni(int numeroSegnalazioni) {
        this.numeroSegnalazioni = numeroSegnalazioni;
    }
}
