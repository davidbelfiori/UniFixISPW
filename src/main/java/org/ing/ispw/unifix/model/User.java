package org.ing.ispw.unifix.model;

import org.ing.ispw.unifix.utils.UserType;

public class User {

    private String nome;
    private String cognome;
    private String email;
    private String password;
    private UserType ruolo;

    public User(String email) {
        this.email = email;
    }

    public User(String email, String password, String nome, String cognome, UserType ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
    }

    public User(String email, String nome, String cognome) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getRuolo() {
        return ruolo;
    }

    public void setRuolo(UserType ruolo) {
        this.ruolo = ruolo;
    }
}
