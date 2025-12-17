package org.ing.ispw.unifix.dao;

import org.checkerframework.checker.units.qual.A;
import org.ing.ispw.unifix.model.Aula;

import java.util.List;

public interface AulaDao extends Dao<String, Aula> {

    Aula create(String idAula);

    List<Aula> getAllAule();

    List<String> getAllEdifici();

    List<String> getAulaOggetti(String idAula);
}
