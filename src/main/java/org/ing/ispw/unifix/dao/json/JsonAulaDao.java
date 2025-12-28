package org.ing.ispw.unifix.dao.json;

import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.model.Aula;

import java.util.ArrayList;
import java.util.List;

public class JsonAulaDao extends JsonDao<String, Aula> implements AulaDao {

    public JsonAulaDao() {
        super("aule.json", Aula.class);
    }


    @Override
    protected String getKey(Aula aula) {
        return aula.getIdAula().toLowerCase();
    }

    @Override
    public Aula create(String idAula) {
        return new Aula(idAula);
    }

    @Override
    public Aula load(String id) {
        return super.load(id.toLowerCase());
    }

    @Override
    public boolean exists(String id) {
        return super.exists(id.toLowerCase());
    }

    @Override
    public void delete(String id) {
        super.delete(id.toLowerCase());
    }

    @Override
    public List<Aula> getAllAule() {
        return new ArrayList<>(loadAll());
    }

    @Override
    public List<String> getAllEdifici() {
        List<String> edifici = new ArrayList<>();
        for (Aula aula : getAllAule()) {
            String edificio = aula.getEdificio();
            if (edificio != null && !edifici.contains(edificio)) {
                edifici.add(edificio);
            }
        }
        return edifici;
    }

    @Override
    public List<String> getAulaOggetti(String idAula) {
        Aula aula = load(idAula.toLowerCase());
        if (aula != null && aula.getOggetti() != null) {
            return aula.getOggetti();
        }
        return new ArrayList<>();
    }
}

