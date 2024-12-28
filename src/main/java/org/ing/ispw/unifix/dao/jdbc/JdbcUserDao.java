package org.ing.ispw.unifix.dao.jdbc;

import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.model.Tecnico;
import org.ing.ispw.unifix.model.User;

import java.util.List;

public class JdbcUserDao extends PersitenceDao<String , User>  implements UserDao {

    private static JdbcUserDao instance;

    public static JdbcUserDao getInstance(){
        if(instance == null){
            instance = new JdbcUserDao();
        }
        return instance;
    }

    @Override
    public List<Tecnico> getAllTecnici() {
        return List.of();
    }
}
