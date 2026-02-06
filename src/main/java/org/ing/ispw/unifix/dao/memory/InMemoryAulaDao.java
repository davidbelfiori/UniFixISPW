package org.ing.ispw.unifix.dao.memory;

import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.model.Aula;

import java.util.ArrayList;
import java.util.List;

public class InMemoryAulaDao extends InMemoryDao<String, Aula> implements AulaDao {


    public String getKey(Aula aula) {
        return aula.getIdAula();
    }

    public Aula create(String idAula){
        return new Aula(idAula);
    }

    public List<Aula> getAllAule(){
        return  new ArrayList<>(loadAll());
    }

    public List<String> getAulaOggetti(String id){
        return load(id.toLowerCase()).getOggetti();
    }


    public List<String> getAllEdifici() {
        List<String> edifici = new ArrayList<>();
        for (Aula aula : getAllAule())
            edifici.add(aula.getEdificio());
        return edifici;
    }

}
