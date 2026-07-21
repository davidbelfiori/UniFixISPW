package org.ing.ispw.unifix.bean;

import java.sql.Timestamp;

public class NotaSegnalazioneBean {
    private String idSegnalazione;
    private String testoNota;
    private Timestamp dataCreazione;


    public NotaSegnalazioneBean() {
        //empty constructor
    }

    public Timestamp getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(Timestamp dataCreazione) {
        if(dataCreazione == null) {
            throw new IllegalArgumentException("dataCreazione cannot be null");
        }
        this.dataCreazione = dataCreazione;
    }

    public String getIdSegnalazione() {
        return idSegnalazione;
    }

    public void setIdSegnalazione(String idSegnalazione) {
        if(idSegnalazione == null || idSegnalazione.isEmpty()) {
            throw new IllegalArgumentException("idSegnalazione cannot be null or empty");
        }
        this.idSegnalazione = idSegnalazione;
    }

    public String getTestoNota() {
        return testoNota;
    }

    public void setTestoNota(String testoNota) {
        if(testoNota == null || testoNota.isEmpty()) {
            throw new IllegalArgumentException("testoNota cannot be null or empty");
        }
        this.testoNota = testoNota;
    }

}
