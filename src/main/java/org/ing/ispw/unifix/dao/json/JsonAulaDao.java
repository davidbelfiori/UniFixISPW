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
    public Aula create(String idAula) {
        return new Aula(idAula);
    }


    @Override
    public Aula load(String edificio, String idAula) {
        String compositeKey = (edificio + "_" + idAula).toLowerCase();
        return super.load(compositeKey);
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
    protected String getKey(Aula aula) {
        return (aula.getEdificio() + "_" + aula.getIdAula()).toLowerCase();
    }
    @Override
    public boolean exists(String edificio, String idAula) {
        String compositeKey = (edificio + "_" + idAula).toLowerCase();
        return super.exists(compositeKey);
    }
    @Override
    public List<String> getAulaOggetti(String edificio, String idAula) {
        String compositeKey = (edificio + "_" + idAula).toLowerCase();
        Aula aula = load(compositeKey);
        return (aula != null && aula.getOggetti() != null) ? aula.getOggetti() : new ArrayList<>();
    }


    @Override
    public int countAule() {
        return loadAll().size();
    }

    @Override
    public int countEdificiGestiti() {
        List<String> edifici = new ArrayList<>();
        for (Aula aula : getAllAule()) {
            String edificio = aula.getEdificio();
            if (edificio != null && !edifici.contains(edificio)) {
                edifici.add(edificio);
            }
        }
        return edifici.size();
    }
}

