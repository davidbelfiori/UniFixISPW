package org.ing.ispw.unifix.dao.memory;

import org.ing.ispw.unifix.dao.NotaSegnalazioneDao;
import org.ing.ispw.unifix.model.NotaSegnalazione;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO in memoria delle note, indicizzate tramite UUID.
 * La ricerca per segnalazione filtra le note presenti nella mappa della classe base.
 */
public class InMemoryNotaSegnalazioneDao  extends InMemoryDao<String, NotaSegnalazione> implements NotaSegnalazioneDao {


    @Override
    public List<NotaSegnalazione> getAllNotaSegnalazioneById(String idSegnalazione) {
        List<NotaSegnalazione> note = new ArrayList<>();
        for (NotaSegnalazione nota : loadAll()) {
            if (nota.getSegnalazione().getIdSegnalazione().equals(idSegnalazione)) {
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
