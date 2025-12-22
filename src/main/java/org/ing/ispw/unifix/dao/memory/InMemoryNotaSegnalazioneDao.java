package org.ing.ispw.unifix.dao.memory;

import org.ing.ispw.unifix.dao.NotaSegnalazioneDao;
import org.ing.ispw.unifix.model.NotaSegnalazione;

import java.util.ArrayList;
import java.util.List;

public class InMemoryNotaSegnalazioneDao  extends InMemoryDao<String, NotaSegnalazione> implements NotaSegnalazioneDao {

    private static InMemoryNotaSegnalazioneDao instance;

    private InMemoryNotaSegnalazioneDao() {}

    public static synchronized InMemoryNotaSegnalazioneDao getInstance() {
        if (instance == null) {
            instance = new InMemoryNotaSegnalazioneDao();
        }
        return instance;
    }



    @Override
    public List<NotaSegnalazione> getAllNotaSegnalazioneById(String idSegnalazione) {
        List<NotaSegnalazione> note = new ArrayList<>();
        for (NotaSegnalazione nota : loadAll()) {
            if (nota.getSegnalazione().getIdSegnalzione().equals(idSegnalazione)) {
                note.add(nota);
            }

        }
        return  note;
    }

    @Override
    public NotaSegnalazione create(String id) {
        return new NotaSegnalazione(id);
    }



    @Override
    protected String getKey(NotaSegnalazione value) {
        return  value.getUuid();
    }


}
