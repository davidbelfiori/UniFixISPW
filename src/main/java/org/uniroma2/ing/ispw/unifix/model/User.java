package org.uniroma2.ing.ispw.unifix.model;

public class User {

    private String nome;
    private String cogmome;
    private String email;
    private String password;
    private String ruolo;

    public User(String nome, String cogmome, String email, String password, String ruolo) {
       setNome(nome);
       setCogmome(cogmome);
       setEmail(email);
       setPassword(password);
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }
}
