package org.uniroma2.ing.ispw.unifix.dao;

import org.uniroma2.ing.ispw.unifix.model.User;

import java.util.HashMap;
import java.util.Map;

public class DatabaseInMemoria implements DatabaseInterface{
    private Map<String, User> users = new HashMap<>();

    @Override
    public boolean aggiungiUtente(User user) {
        if (users.containsKey(user.getEmail())) {
            return false; // Utente già esistente
        }
        users.put(user.getEmail(), user);
        return true;
    }

    @Override
    public User autenticaUtente(String email, String password) {
        User user = users.get(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
