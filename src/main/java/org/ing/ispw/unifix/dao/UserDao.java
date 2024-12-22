package org.ing.ispw.unifix.dao;

import org.ing.ispw.unifix.model.Tecnico;
import org.ing.ispw.unifix.model.User;

import java.util.List;

public interface UserDao extends Dao<String, User> {

    User create(String email);

    List<Tecnico> getAllTecnici();
}
