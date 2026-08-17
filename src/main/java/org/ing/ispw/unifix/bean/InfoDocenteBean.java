package org.ing.ispw.unifix.bean;

public class InfoDocenteBean {
    private String nome;
    private String cognome;
    private String email;

    public InfoDocenteBean() {
        //empty constructor
    }

    public InfoDocenteBean(String nome, String cognome, String email) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("nome cannot be null or empty");
        }

        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        if (cognome == null || cognome.trim().isEmpty()) {
            throw new IllegalArgumentException("cognome cannot be null or empty");
        }
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("email cannot be null or empty");
        }
        this.email = email;
    }
}
