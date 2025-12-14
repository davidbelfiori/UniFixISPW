package org.ing.ispw.unifix.model;

import org.ing.ispw.unifix.utils.UserType;

public class Sysadmin extends User{

    public Sysadmin(String email) {
        super(email);
    }

    public Sysadmin(String email, String password, String nome, String cognome, UserType ruolo) {
        super(email, password, nome, cognome, ruolo);
    }
}
