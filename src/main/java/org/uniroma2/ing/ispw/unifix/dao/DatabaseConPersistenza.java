package org.uniroma2.ing.ispw.unifix.dao;

import org.uniroma2.ing.ispw.unifix.model.User;

public class DatabaseConPersistenza implements DatabaseInterface{

    @Override
    public boolean aggiungiUtente(User user) {
        return false;
    }

    @Override
    public User autenticaUtente(String email, String password) {
        return null;
    }
}
