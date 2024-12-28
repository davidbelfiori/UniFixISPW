package org.ing.ispw.unifix.dao.jdbc;


import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.model.Aula;

import java.util.List;

public class JdbcAulaDao extends PersitenceDao<String , Aula>  implements AulaDao {
    private static JdbcAulaDao instance;

    public static JdbcAulaDao getInstance(){
        if(instance == null){
            instance = new JdbcAulaDao();
        }
        return instance;
    }

    @Override
    public List<Aula> getAllAule() {
        return List.of();
    }

    @Override
    public List<String> getAllEdifici() {
        return List.of();
    }
}
