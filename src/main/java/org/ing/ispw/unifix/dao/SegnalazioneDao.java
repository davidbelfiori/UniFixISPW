package org.ing.ispw.unifix.dao;

import org.ing.ispw.unifix.model.Segnalazione;

public interface SegnalazioneDao extends Dao<String, Segnalazione> {

    Segnalazione create(String idSegnalazione);

}
