package org.ing.ispw.unifix.dao.memory;

import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.model.Aula;

import java.util.ArrayList;
import java.util.List;

public class InMemoryAulaDao extends InMemoryDao<String, Aula> implements AulaDao {



    private static InMemoryAulaDao instance;

    public static InMemoryAulaDao getInstance(){
        if(instance == null){
            instance = new InMemoryAulaDao();
        }
        return instance;
    }

    public String getKey(Aula aula) {
        return aula.getIdAula();
    }

    public Aula create(String idAula){
        return new Aula(idAula);
    }

    public List<Aula> getAllAule(){
        return  new ArrayList<>(loadAll());
    }
}
