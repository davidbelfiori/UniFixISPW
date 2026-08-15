package org.ing.ispw.unifix.dao.memory;

import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.model.Tecnico;
import org.ing.ispw.unifix.model.User;

import java.util.ArrayList;
import java.util.List;
/**
 * DAO in memoria degli utenti, indicizzati tramite email.
 * I dati esistono soltanto durante la vita dell'istanza della factory in memoria.
 */
public class InMemoryUserDao extends InMemoryDao<String, User> implements UserDao {


    public String getKey(User user) {
        return user.getEmail();
    }


    public List<Tecnico> getAllTecnici() {
        List<Tecnico> tecnico = new ArrayList<Tecnico>();
        for (User user : loadAll()) {
            if (user instanceof Tecnico tecnicoUser) {
                tecnico.add(tecnicoUser);
            }
        }
        return tecnico;
    }

    @Override
    public final void update(Tecnico tecnico) {
        super.update(tecnico);
    }




}
