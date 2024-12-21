package org.ing.ispw.unifix.bean;

import org.ing.ispw.unifix.model.Aula;
import org.ing.ispw.unifix.model.User;

import java.util.Date;
import java.util.List;

public class SegnalazioneBean {

    private int idSegnalzione;
    private long dataCreazione;
    private String oggettoGuasto;
    private User user;
    private String stato;
    private String descrizone;
    private String aula;
    private String edificio;


    private List<Aula> aule;
    private List<String> edificiUnici;

    public SegnalazioneBean(long dataCreazione, String aula, String edificio, String oggettoGuasto,String descrizone) {
        this.dataCreazione = dataCreazione;
        this.aula = aula;
        this.edificio = edificio;
        this.oggettoGuasto = oggettoGuasto;
        this.descrizone=descrizone;
    }

    public SegnalazioneBean(int idSegnalzione, long dataCreazione, String oggettoGuasto, User user, String statoSegnalazione, String descrizone, String aula) {
        this.idSegnalzione = idSegnalzione;
        this.dataCreazione = dataCreazione;
        this.oggettoGuasto = oggettoGuasto;
        this.user = user;
        this.stato = statoSegnalazione;
        this.descrizone = descrizone;
        this.aula = aula;
    }


    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
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
        return stato;
    }

    public void setStato(String statoSegnalazione) {
        stato = statoSegnalazione;
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

    public long getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(long dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public int getIdSegnalzione() {
        return idSegnalzione;
    }

    public void setIdSegnalzione(int idSegnalzione) {
        this.idSegnalzione = idSegnalzione;
    }


    public List<String> getEdificiUnici() {
        return edificiUnici;
    }

    public void setEdificiUnici(List<String> edifici) {
        this.edificiUnici = edifici;
    }
}
