package org.ing.ispw.unifix.dao.memory;

import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.model.Tecnico;
import org.ing.ispw.unifix.model.User;

import java.util.List;
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

    @Override
    public void update(Tecnico entity) {
        // Specific update logic for Tecnico if needed, otherwise rely on the generic update
        super.update(entity); // Calls the update(V entity) method in InMemoryDao
    }

    public List<Tecnico> getAllTecnici() {
        return loadAll().stream()
                .filter(Tecnico.class::isInstance)
                .map(Tecnico.class::cast).toList();
    }



}