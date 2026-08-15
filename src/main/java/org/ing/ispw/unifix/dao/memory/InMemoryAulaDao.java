package org.ing.ispw.unifix.dao.memory;

import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.model.Aula;
import org.ing.ispw.unifix.model.AulaId;

import java.util.ArrayList;
import java.util.List;

public class InMemoryAulaDao extends InMemoryDao<AulaId, Aula> implements AulaDao {

    @Override
    public Aula create(String idAula) {
        return new Aula(idAula);
    }

    @Override
    protected AulaId getKey(Aula aula) {
        return new AulaId(
                aula.getIdAula(),
                aula.getEdificio()
        );
    }



    @Override
    public List<String> getAllEdifici() {
        List<String> edifici = new ArrayList<>();

        for (Aula aula : loadAll()) {
            if (aula.getEdificio() != null
                    && !edifici.contains(aula.getEdificio())) {
                edifici.add(aula.getEdificio());
            }
        }

        return edifici;
    }

    @Override
    public List<String> getAulaOggetti(AulaId id) {
        Aula aula = load(id);

        if (aula == null || aula.getOggetti() == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(aula.getOggetti());
    }

    @Override
    public int countAule() {
        return loadAll().size();
    }

    @Override
    public int countEdificiGestiti() {
        return getAllEdifici().size();
    }


}
