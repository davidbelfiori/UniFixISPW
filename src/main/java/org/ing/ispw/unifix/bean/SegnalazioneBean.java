package org.ing.ispw.unifix.bean;

import org.ing.ispw.unifix.model.Aula;
import org.ing.ispw.unifix.model.Tecnico;
import org.ing.ispw.unifix.model.User;
import org.ing.ispw.unifix.utils.StatoSegnalazione;


import java.sql.Date;
import java.util.List;

public class SegnalazioneBean {

    private String idSegnalazione;
    private Date dataCreazione;
    private String oggettoGuasto;
    private User user;
    private StatoSegnalazione stato;
    private String descrizione;
    private String aula;
    private String edificio;
    private Tecnico tecnico;


    private List<Aula> aule;

    public SegnalazioneBean(Date dataCreazione, String aula, String edificio, String oggettoGuasto, String descrizione) {
        this.dataCreazione = dataCreazione;
        this.aula = aula;
        this.edificio = edificio;
        this.oggettoGuasto = oggettoGuasto;
        this.descrizione = descrizione;
    }



    private SegnalazioneBean(Builder builder) {
        this.idSegnalazione = builder.idSegnalazione;
        this.dataCreazione = builder.dataCreazione;
        this.oggettoGuasto = builder.oggettoGuasto;
        this.user = builder.user;
        this.stato = builder.stato;
        this.descrizione = builder.descrizione;
        this.aula = builder.aula;
        this.edificio = builder.edificio;
        this.tecnico = builder.tecnico;
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

    public StatoSegnalazione getStato() {
        return stato;
    }

    public void setStato(StatoSegnalazione statoSegnalazione) {
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
        private User user;
        private StatoSegnalazione stato;
        private String descrizione;
        private String aula;
        private String edificio;
        private Tecnico tecnico;

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

        public Builder user(User user) {
            this.user = user;
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

        public Builder tecnico(Tecnico tecnico) {
            this.tecnico = tecnico;
            return this;
        }

        // --- Metodo finale per creare l'oggetto ---
        public SegnalazioneBean build() {
            return new SegnalazioneBean(this);
        }
    }


}
