package org.ing.ispw.unifix.dao;

import org.ing.ispw.unifix.model.NotaSegnalazione;

import java.util.List;

public interface NotaSegnalazioneDao extends Dao<String, NotaSegnalazione>{

    NotaSegnalazione create(String uuid);

    List<NotaSegnalazione> getAllNotaSegnalazioneById(String idSegnalazione);


    void store(NotaSegnalazione nota);


}
