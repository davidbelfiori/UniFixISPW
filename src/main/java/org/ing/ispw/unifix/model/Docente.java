package org.ing.ispw.unifix.model;

import org.ing.ispw.unifix.utils.UserType;

public class Docente extends User{

    public Docente(String email) {
        super(email);
    }

    public Docente(String email, String password, String nome, String cognome, UserType ruolo) {
        super(email, password, nome, cognome, ruolo);
    }
}
