package org.ing.ispw.unifix.model;


import java.sql.Date;


public class Segnalazione {

    private String idSegnalazione;
    private Date dataCreazione;
    private String oggettoGuasto;
    private Docente docente;
    private String  stato;
    private String descrizione;
    private String aula;
    private String edificio;
    private Tecnico tecnico;


    public Segnalazione(String idSegnalazione) {
        this.idSegnalazione =idSegnalazione;
    }


    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Dettagli Segnalazione:\n");
        sb.append("  Data Creazione: ").append(dataCreazione).append("\n");
        sb.append("  Oggetto Guasto: ").append(oggettoGuasto).append("\n");
        if (docente != null) {
            sb.append("  Docente: ").append(docente.getNome()).append(" ").append(docente.getCognome()).append("\n");
        } else {
            sb.append("  Docente: Non assegnato\n");
        }
        sb.append("  Stato: ").append(stato).append("\n");
        sb.append("  Descrizione: ").append(descrizione).append("\n");
        sb.append("  Aula: ").append(aula).append("\n");
        sb.append("  Edificio: ").append(edificio).append("\n");
        if (tecnico != null) {
            sb.append("  Tecnico Assegnato: ").append(tecnico.getNome()).append(" ").append(tecnico.getCognome()).append("\n");
        } else {
            sb.append("  Tecnico Assegnato: Nessuno\n");
        }
        return sb.toString();
    }


}
