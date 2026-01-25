package org.ing.ispw.unifix.bean;

import java.sql.Timestamp;

public class NotaSegnalazioneBean {
    private String idSegnalazione;
    private String testoNota;
    private Timestamp dataCreazione;

    public NotaSegnalazioneBean(String idSegnalazione, String testoNota) {
        this.idSegnalazione = idSegnalazione;
        this.testoNota = testoNota;
    }

    public NotaSegnalazioneBean(String idSegnalazione, Timestamp dataCreazione, String testoNota) {
        this.idSegnalazione = idSegnalazione;
        this.dataCreazione = dataCreazione;
        this.testoNota = testoNota;
    }

    public Timestamp getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(Timestamp dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public String getIdSegnalazione() {
        return idSegnalazione;
    }

    public void setIdSegnalazione(String idSegnalazione) {
        this.idSegnalazione = idSegnalazione;
    }

    public String getTestoNota() {
        return testoNota;
    }

    public void setTestoNota(String testoNota) {
        this.testoNota = testoNota;
    }

}
