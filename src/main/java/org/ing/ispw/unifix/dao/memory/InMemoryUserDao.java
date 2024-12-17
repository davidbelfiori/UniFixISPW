package org.ing.ispw.unifix.dao.memory;

import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.model.User;

public class InMemoryUserDao extends InMemoryDao<String, User> implements UserDao {

    private static InMemoryUserDao instance;

    public static InMemoryUserDao getInstance(){
        if(instance == null){
            instance = new InMemoryUserDao();
        }
        return instance;
    }

    public String getKey(User user) {
        return user.getEmail();
    }

    public User create(String email){
        return new User(email);
    }

}