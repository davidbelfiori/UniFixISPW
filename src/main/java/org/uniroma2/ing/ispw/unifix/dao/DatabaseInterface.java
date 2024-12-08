package org.uniroma2.ing.ispw.unifix.dao;

import org.uniroma2.ing.ispw.unifix.model.User;

public interface DatabaseInterface {
    boolean aggiungiUtente(User user);
    User autenticaUtente(String email, String password);
}
