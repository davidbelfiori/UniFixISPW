package org.ing.ispw.unifix.model;

import java.util.Date;

public class Segnalazione {

    private int idSegnalzione;
    private Date dataCreazione;
    private String oggettoGuasto;
    private User user;
    private enum Stato{APERTA,INLAVORAZIONE,CHIUSA}
    private String descrizone;
    private String aula;

    public int getIdSegnalzione() {
        return idSegnalzione;
    }

    public void setIdSegnalzione(int idSegnalzione) {
        this.idSegnalzione = idSegnalzione;
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public String getOggettoGuasto() {
        return oggettoGuasto;
    }

    public void setOggettoGuasto(String oggettoGuasto) {
        this.oggettoGuasto = oggettoGuasto;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescrizone() {
        return descrizone;
    }

    public void setDescrizone(String descrizone) {
        this.descrizone = descrizone;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

}
