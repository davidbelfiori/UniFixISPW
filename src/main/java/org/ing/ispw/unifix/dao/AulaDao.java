package org.ing.ispw.unifix.dao;

import org.ing.ispw.unifix.model.Aula;
import org.ing.ispw.unifix.model.AulaId;

import java.util.List;

public interface AulaDao extends Dao<AulaId, Aula> {

    Aula create(String idAula);
    List<String> getAllEdifici();
    List<String> getAulaOggetti(AulaId aulaId);
    int countAule();
    int countEdificiGestiti();
}
