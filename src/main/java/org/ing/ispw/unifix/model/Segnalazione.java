package org.ing.ispw.unifix.model;


import org.ing.ispw.unifix.model.state.StateSegnalazione;
import org.ing.ispw.unifix.model.state.StatoAperta;
import org.ing.ispw.unifix.model.state.StatoChiusa;
import org.ing.ispw.unifix.model.state.StatoInLavorazione;
import org.ing.ispw.unifix.utils.StatoSegnalazione;
import org.ing.ispw.unifix.utils.observer.Subject;

import java.sql.Date;


public class Segnalazione extends Subject {

    private String idSegnalazione;
    private Date dataCreazione;
    private String oggettoGuasto;
    private Docente docente;
    private StateSegnalazione stato;
    private String descrizione;
    private String aula;
    private String edificio;
    private Tecnico tecnico;


    public Segnalazione(String idSegnalazione) {
        this.idSegnalazione =idSegnalazione;
        this.stato = new StatoAperta();
    }



    public void setStato(StateSegnalazione stato) {
        this.stato = stato;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public String getIdSegnalazione() {
        return idSegnalazione;
    }

    public void setIdSegnalazione(String idSegnalazione) {
        this.idSegnalazione = idSegnalazione;
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

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public Tecnico getTecnico() {
        return tecnico;
    }

    public void setTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public StatoSegnalazione getStato() {
        return stato != null ? stato.getStatoEnum() : null;
    }
    public void setStato(StatoSegnalazione stato) {
        if (stato == null) return;
        switch (stato) {
            case APERTA -> this.stato = new StatoAperta();
            case IN_LAVORAZIONE -> this.stato = new StatoInLavorazione();
            case CHIUSA -> this.stato = new StatoChiusa();
        }
    }

    public void chiudi() {
       this.stato.chiudi(this);
    }

    public void inLavorazione() {
        this.stato.inLavorazione(this);
    }






}
