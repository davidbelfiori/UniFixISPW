package org.uniroma2.ing.ispw.unifix.bean;

import org.uniroma2.ing.ispw.unifix.exception.DoppiaChiocciolaException;
import org.uniroma2.ing.ispw.unifix.exception.DoppiaVirgolaException;
import org.uniroma2.ing.ispw.unifix.exception.TerminatoreEmailException;

public class RegistrazioneBean {

    private String email;
    private String password;
    private String confirmPassword;

    public RegistrazioneBean(String email, String password) {
        this.email = email;
        this.password = password;
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


}
