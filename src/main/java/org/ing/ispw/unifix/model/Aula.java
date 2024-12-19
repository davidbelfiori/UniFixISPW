package org.ing.ispw.unifix.model;

import java.util.Collections;
import java.util.List;

public class Aula {
    private String idAula;
    private int piano;
    private String edificio;
    private List<String> oggetti;

    public Aula(String idAula) {
        this.idAula=idAula;
    }

    public List<String> getOggettiById(String idAula) {
        if (this.idAula.equals(idAula)) {
            return this.oggetti;
        }
        return Collections.emptyList();
    }

    public List<String> getOggetti() {
        return oggetti;
    }

    public void setOggetti(List<String> oggetti) {
        this.oggetti = oggetti;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public int getPiano() {
        return piano;
    }

    public void setPiano(int piano) {
        this.piano = piano;
    }

    public String getIdAula() {
        return idAula;
    }

    public void setIdAula(String idAula) {
        this.idAula = idAula;
    }



    @Override
    public String toString() {
        return "Aula{" +
                "idAula='" + idAula + '\'' +
                ", piano=" + piano +
                ", edificio='" + edificio + '\'' +
                ", oggetti=" + oggetti +
                '}';
    }
}
