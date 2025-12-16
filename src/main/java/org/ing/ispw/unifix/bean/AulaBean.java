package org.ing.ispw.unifix.bean;

import java.util.List;

public class AulaBean {
    private String idAula;
    private int piano;
    private String edificio;
    private List<String> oggetti;



    public String getIdAula() {
        return idAula;
    }

    public void setIdAula(String idAula) {
        this.idAula = idAula;
    }

    public int getPiano() {
        return piano;
    }

    public void setPiano(int piano) {
        this.piano = piano;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public List<String> getOggetti() {
        return oggetti;
    }

    public void setOggetti(List<String> oggetti) {
        this.oggetti = oggetti;
    }
}
