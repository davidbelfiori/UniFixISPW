package org.ing.ispw.unifix.dao;

import org.ing.ispw.unifix.model.Aula;

public interface AulaDao extends Dao<String, Aula> {

    Aula create(String idAula);
}
