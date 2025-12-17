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



    public List<Tecnico> getAllTecnici() {
        return loadAll().stream()
                .filter(Tecnico.class::isInstance)
                .map(Tecnico.class::cast).toList();
    }

    @Override
    public final void update(Tecnico tecnico) {
        super.update(tecnico);
    }




}