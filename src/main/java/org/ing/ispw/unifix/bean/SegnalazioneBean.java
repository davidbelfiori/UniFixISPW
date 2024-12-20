package org.ing.ispw.unifix.bean;

import org.ing.ispw.unifix.model.Aula;
import org.ing.ispw.unifix.model.User;

import java.util.Date;
import java.util.List;

public class SegnalazioneBean {

    private int idSegnalzione;
    private Date dataCreazione;
    private String oggettoGuasto;
    private User user;
    private String Stato;
    private String descrizone;
    private String aula;
    private List<Aula> aule;
    private List<String> edifici;

    public SegnalazioneBean(int idSegnalzione, Date dataCreazione, String oggettoGuasto, User user, String stato, String descrizone, String aula) {
        this.idSegnalzione = idSegnalzione;
        this.dataCreazione = dataCreazione;
        this.oggettoGuasto = oggettoGuasto;
        this.user = user;
        Stato = stato;
        this.descrizone = descrizone;
        this.aula = aula;
    }

    public SegnalazioneBean() {
    }

    public List<Aula> getAule() {
        return aule;
    }

    public void setAule(List<Aula> aule) {
        this.aule = aule;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getDescrizone() {
        return descrizone;
    }

    public void setDescrizone(String descrizone) {
        this.descrizone = descrizone;
    }

    public String getStato() {
        return Stato;
    }

    public void setStato(String stato) {
        Stato = stato;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOggettoGuasto() {
        return oggettoGuasto;
    }

    public void setOggettoGuasto(String oggettoGuasto) {
        this.oggettoGuasto = oggettoGuasto;
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public int getIdSegnalzione() {
        return idSegnalzione;
    }

    public void setIdSegnalzione(int idSegnalzione) {
        this.idSegnalzione = idSegnalzione;
    }


    public List<String> getEdifici() {
        return edifici;
    }

    public void setEdifici(List<String> edifici) {
        this.edifici = edifici;
    }
}
