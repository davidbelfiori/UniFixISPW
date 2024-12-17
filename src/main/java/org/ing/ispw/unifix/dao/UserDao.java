package org.ing.ispw.unifix.dao;

import org.ing.ispw.unifix.model.User;

public interface UserDao extends Dao<String, User> {

    User create(String email);
}
