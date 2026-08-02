package org.ing.ispw.unifix.bean;

import org.ing.ispw.unifix.utils.UserType;

public class UserBean {

    private String email;
    private UserType ruolo;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getRuolo() {
        return ruolo;
    }

    public void setRuolo(UserType ruolo) {
        this.ruolo = ruolo;
    }
}
