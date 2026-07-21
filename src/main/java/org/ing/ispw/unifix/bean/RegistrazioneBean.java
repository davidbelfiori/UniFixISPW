package org.ing.ispw.unifix.bean;

public class RegistrazioneBean {

    private String email;
    private String password;
    private String confirmPassword;

    public RegistrazioneBean() {
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


    public void setConfirmPassword(String confirmPassword) {
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("La conferma della password non può essere vuota.");
        }
        if (!confirmPassword.equals(this.password)) {
            throw new IllegalArgumentException("La conferma della password non corrisponde alla password inserita.");
        }
        this.confirmPassword = confirmPassword;
    }

}
