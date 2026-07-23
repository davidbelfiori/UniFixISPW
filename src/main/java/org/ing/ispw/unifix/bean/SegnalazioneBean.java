package org.ing.ispw.unifix.bean;

import org.ing.ispw.unifix.utils.StatoSegnalazione;


import java.sql.Date;
import java.util.List;

public class SegnalazioneBean {

    private String idSegnalazione;
    private Date dataCreazione;
    private String oggettoGuasto;
    private InfoDocenteBean docenteSegnalatore;
    private StatoSegnalazione stato;
    private String descrizione;
    private String aula;
    private String edificio;
    private InfoTecnicoBean tecnico;
    private List<AulaBean> aule;


    public SegnalazioneBean(){
        //intentionaly empty
    }




    private SegnalazioneBean(Builder builder) {
        this.idSegnalazione = builder.idSegnalazione;
        this.dataCreazione = builder.dataCreazione;
        this.oggettoGuasto = builder.oggettoGuasto;
        this.docenteSegnalatore = builder.docenteSegnalatore;
        this.stato = builder.stato;
        this.descrizione = builder.descrizione;
        this.aula = builder.aula;
        this.edificio = builder.edificio;
        this.tecnico = builder.tecnico;
    }


    public InfoTecnicoBean getTecnico() {
        return tecnico;
    }

    public void setTecnico(InfoTecnicoBean tecnico) {
        this.tecnico = tecnico;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }



    public List<AulaBean> getAule() {
        return aule;
    }

    public void setAule(List<AulaBean> aule) {
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

    public StatoSegnalazione getStato() {
        return stato;
    }

    public void setStato(StatoSegnalazione statoSegnalazione) {
        stato = statoSegnalazione;
    }

    public InfoDocenteBean getUser() {
        return docenteSegnalatore;
    }

    public void setUser(InfoDocenteBean docenteSegnalatore) {
        this.docenteSegnalatore = docenteSegnalatore;
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

    public String getIdSegnalazione() {
        return idSegnalazione;
    }

    public void setIdSegnalazione(String idSegnalazione) {
        this.idSegnalazione = idSegnalazione;
    }

    // ---  Classe statica interna "Builder" ---
    public static class Builder {
        // Copia degli stessi campi della classe principale
        private String idSegnalazione;
        private Date dataCreazione;
        private String oggettoGuasto;
        private InfoDocenteBean docenteSegnalatore;
        private StatoSegnalazione stato;
        private String descrizione;
        private String aula;
        private String edificio;
        private InfoTecnicoBean tecnico;

        // Costruttore del Builder
        public Builder(String idSegnalazione) {
            this.idSegnalazione = idSegnalazione;
        }

        // --- Metodi "Setter" che ritornano il Builder stesso ---

        public Builder dataCreazione(Date dataCreazione) {
            this.dataCreazione = dataCreazione;
            return this;
        }

        public Builder oggettoGuasto(String oggettoGuasto) {
            this.oggettoGuasto = oggettoGuasto;
            return this;
        }

        public Builder user(InfoDocenteBean docenteSegnalatore) {
            this.docenteSegnalatore = docenteSegnalatore;
            return this;
        }

        public Builder stato(StatoSegnalazione stato) {
            this.stato = stato;
            return this;
        }

        public Builder descrizione(String descrizione) {
            this.descrizione = descrizione;
            return this;
        }

        public Builder aula(String aula) {
            this.aula = aula;
            return this;
        }

        public Builder edificio(String edificio) {
            this.edificio = edificio;
            return this;
        }

        public Builder tecnico(InfoTecnicoBean tecnico) {
            this.tecnico = tecnico;
            return this;
        }

        // --- Metodo finale per creare l'oggetto ---
        public SegnalazioneBean build() {
            return new SegnalazioneBean(this);
        }
    }


}
