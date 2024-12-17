package org.uniroma2.ing.ispw.unifix.dao;

import org.uniroma2.ing.ispw.unifix.model.User;

public interface UserDao extends Dao<String, User> {

    User create(String email);
}
