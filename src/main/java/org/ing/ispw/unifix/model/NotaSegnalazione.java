package org.ing.ispw.unifix.model;


import java.sql.Timestamp;

public class NotaSegnalazione {

    private String uuid;
    private Segnalazione segnalazione;
    private Timestamp dataCreazione;
    private Tecnico tecnico;
    private String testo;

    public NotaSegnalazione(String uuid, Segnalazione segnalazione, Timestamp dataCreazione, Tecnico tecnico, String testo) {
        this.uuid = uuid;
        this.segnalazione = segnalazione;
        this.dataCreazione = dataCreazione;
        this.tecnico = tecnico;
        this.testo = testo;
    }

    public NotaSegnalazione(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Segnalazione getSegnalazione() {
        return segnalazione;
    }

    public void setSegnalazione(Segnalazione segnalazione) {
        this.segnalazione = segnalazione;
    }

    public Timestamp getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(Timestamp dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Tecnico getTecnico() {
        return tecnico;
    }

    public void setTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }
}
