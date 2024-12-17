package org.uniroma2.ing.ispw.unifix.model;

import java.util.List;

public class Aula {
    private String idAula;
    private int piano;
    private String edificio;
    private List<String> oggetti;

    public List<String> getOggettiById(String idAula) {
        if (this.idAula.equals(idAula)) {
            return this.oggetti;
        }
        return null;
    }

}
