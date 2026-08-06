package org.ing.ispw.unifix.dao.memory;

import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.model.Aula;

import java.util.ArrayList;
import java.util.List;

public class InMemoryAulaDao extends InMemoryDao<String, Aula> implements AulaDao {

    @Override
    public Aula create(String idAula) {
        return new Aula(idAula);
    }

    @Override
    public List<Aula> getAllAule() {
        return new ArrayList<>(loadAll());
    }

    @Override
    public Aula load(String edificio, String idAula) {
        String compositeKey = (edificio + "_" + idAula).toLowerCase();
        return super.load(compositeKey);
    }

    @Override
    public int countAule() {
        return loadAll().size();
    }

    @Override
    public int countEdificiGestiti() {
        List<String> edifici = new ArrayList<>();
        for (Aula aula : getAllAule()) {
            if (!edifici.contains(aula.getEdificio())) {
                edifici.add(aula.getEdificio());
            }
        }
        return edifici.size();
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
    public List<String> getAllEdifici() {
        List<String> edifici = new ArrayList<>();
        for (Aula aula : getAllAule()) {
            if (!edifici.contains(aula.getEdificio())) {
                edifici.add(aula.getEdificio());
            }
        }
        return edifici;
    }
}
