package org.ing.ispw.unifix.dao.memory;

import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.model.Tecnico;
import org.ing.ispw.unifix.model.User;

import java.util.List;
public class InMemoryUserDao extends InMemoryDao<String, User> implements UserDao {


    public String getKey(User user) {
        return user.getEmail();
    }

    public User create(String email){
        //Poiche User è astratto, la creazione di un utente deve essere gestita da UserFactory
        throw new UnsupportedOperationException("User è astratto: la creazione è gestita da UserFactory");
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