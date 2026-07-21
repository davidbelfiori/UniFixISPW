package org.ing.ispw.unifix.bean;

public class CredentialBean {
    private String email;
    private String password;

    public CredentialBean() {
        //empty constructor
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("L'email non può essere vuota.");
        }
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La password non può essere vuota.");
        }
        this.password = password;
    }
}