package org.ing.ispw.unifix.dao;

import org.ing.ispw.unifix.model.Segnalazione;

import java.util.List;

public interface SegnalazioneDao extends Dao<String, Segnalazione> {

    Segnalazione create(String idSegnalazione);

    List<Segnalazione> getAllSegnalazioni();

    Segnalazione getSegnalazione(String idSegnalazione);

}
