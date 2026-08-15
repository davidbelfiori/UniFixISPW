package org.ing.ispw.unifix.bean;


import java.util.ArrayList;
import java.util.List;

public class AulaBean {
    private String idAula;
    private int piano;
    private String edificio;
    private List<String> oggetti;

    //costruttore con validazione per input utente
    public AulaBean() {
       //empty constructor

    }


    public String getIdAula() {
        return idAula;
    }

    public void setIdAula(String idAula) {
        if(idAula == null || idAula.trim().isEmpty()) {
            throw new IllegalArgumentException("ID Aula non può essere vuoto");
        }
        this.idAula = idAula;
    }

    public int getPiano() {
        return piano;
    }

    public void setPiano(int piano) {
        if(piano < -5 || piano > 100) {
            throw new IllegalArgumentException("Il piano deve essere compreso tra -5 e 100");
        }
            this.piano = piano;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        if(edificio == null || edificio.trim().isEmpty()) {
            throw new IllegalArgumentException("Edificio non può essere vuoto");
        } else {
            this.edificio = edificio;
        }
    }

    public List<String> getOggetti() {
        return oggetti;
    }

    public void setOggetti(List<String> oggetti) {
        if (oggetti == null || oggetti.isEmpty()) {
            this.oggetti = new ArrayList<>();
        } else {
            this.oggetti = new ArrayList<>(oggetti);
        }
    }
}
