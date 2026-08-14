package org.ing.ispw.unifix.dao.json;

import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.model.Aula;
import org.ing.ispw.unifix.model.AulaId;

import java.util.ArrayList;
import java.util.List;

public class JsonAulaDao
        extends JsonDao<AulaId, Aula>
        implements AulaDao {

    public JsonAulaDao() {
        super("aule.json", Aula.class);
    }

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
    public List<Aula> getAllAule() {
        return new ArrayList<>(loadAll());
    }

    @Override
    public List<String> getAllEdifici() {
        List<String> edifici = new ArrayList<>();

        for (Aula aula : loadAll()) {
            String edificio = aula.getEdificio();

            if (edificio != null && !edifici.contains(edificio)) {
                edifici.add(edificio);
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
