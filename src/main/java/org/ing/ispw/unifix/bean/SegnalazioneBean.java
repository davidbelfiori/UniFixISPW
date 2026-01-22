package org.ing.ispw.unifix.bean;

import org.ing.ispw.unifix.model.Aula;
import org.ing.ispw.unifix.model.Tecnico;
import org.ing.ispw.unifix.model.User;


import java.sql.Date;
import java.util.List;

public class SegnalazioneBean {

    private String idSegnalzione;
    private Date dataCreazione;
    private String oggettoGuasto;
    private User user;
    private String stato;
    private String descrizione;
    private String aula;
    private String edificio;
    private Tecnico tecnico;


    private List<Aula> aule;
    private List<String> edificiUnici;

    public SegnalazioneBean(Date dataCreazione, String aula, String edificio, String oggettoGuasto, String descrizione) {
        this.dataCreazione = dataCreazione;
        this.aula = aula;
        this.edificio = edificio;
        this.oggettoGuasto = oggettoGuasto;
        this.descrizione = descrizione;
    }

    public SegnalazioneBean(String idSegnalzione, String stato) {
        this.idSegnalzione = idSegnalzione;
        this.stato = stato;
    }

    public SegnalazioneBean(String idSegnalzione, Date dataCreazione, String oggettoGuasto, User user, String statoSegnalazione, String descrizione, String aula, Tecnico tecnico) {
        this.idSegnalzione = idSegnalzione;
        this.dataCreazione = dataCreazione;
        this.oggettoGuasto = oggettoGuasto;
        this.user = user;
        this.stato = statoSegnalazione;
        this.descrizione = descrizione;
        this.aula = aula;
        this.tecnico = tecnico;
    }


    public Tecnico getTecnico() {
        return tecnico;
    }

    public void setTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
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

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
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

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public String getIdSegnalzione() {
        return idSegnalzione;
    }

    public void setIdSegnalzione(String idSegnalzione) {
        this.idSegnalzione = idSegnalzione;
    }


    public List<String> getEdificiUnici() {
        return edificiUnici;
    }

    public void setEdificiUnici(List<String> edifici) {
        this.edificiUnici = edifici;
    }

}
