package org.uniroma2.ing.ispw.unifix.model;

public class User {

    private String nome;
    private String cogmome;
    private String email;
    private String passwprd;
    private String ruolo;

    public User(String nome, String cogmome, String email, String passwprd, String ruolo) {
       setNome(nome);
       setCogmome(cogmome);
       setEmail(email);
       setPasswprd(passwprd);
       setRuolo(ruolo);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCogmome() {
        return cogmome;
    }

    public void setCogmome(String cogmome) {
        this.cogmome = cogmome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswprd() {
        return passwprd;
    }

    public void setPasswprd(String passwprd) {
        this.passwprd = passwprd;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }
}
