package org.uniroma2.ing.ispw.unifix.dao.memory;

import org.uniroma2.ing.ispw.unifix.dao.UserDao;
import org.uniroma2.ing.ispw.unifix.model.User;

public class inMemoryUserDao extends InMemoryDao<String, User> implements UserDao {

    private static inMemoryUserDao instance;

    public static inMemoryUserDao getInstance(){
        if(instance == null){
            instance = new inMemoryUserDao();
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