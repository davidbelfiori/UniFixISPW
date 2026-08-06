package org.ing.ispw.unifix.dao;

import org.ing.ispw.unifix.model.Aula;

import java.util.List;

public interface AulaDao extends Dao<String, Aula> {

    Aula create(String idAula);

    Aula load(String edificio, String idAula);

    List<Aula> getAllAule();

    List<String> getAllEdifici();

    // Aggiornato: ora riceve sia l'edificio che l'idAula
    List<String> getAulaOggetti(String edificio, String idAula);

    // Nuovo metodo per verificare l'esistenza nell'edificio specifico
    boolean exists(String edificio, String idAula);

    int countAule();
    int countEdificiGestiti();
}
