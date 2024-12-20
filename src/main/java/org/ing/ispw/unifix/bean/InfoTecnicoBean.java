package org.ing.ispw.unifix.bean;

public class InfoTecnicoBean {
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String ruolo;
    private int numeroSegnalazioni;

    public InfoTecnicoBean(String nome, String cognome, String email, String password, String ruolo, int numeroSegnalazioni) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
        this.numeroSegnalazioni = numeroSegnalazioni;
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

    public String getRuolo() {
        return ruolo;
    }

    public int getNumeroSegnalazioni() {
        return numeroSegnalazioni;
    }
}
