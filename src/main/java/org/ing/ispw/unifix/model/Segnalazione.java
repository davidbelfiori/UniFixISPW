package org.ing.ispw.unifix.model;



public class Segnalazione {

    private String idSegnalzione;
    private Long dataCreazione;
    private String oggettoGuasto;
    private Docente docente;
    private String  stato;
    private String descrizione;
    private String aula;
    private String edificio;
    private Tecnico tecnico;


    public Segnalazione(String idSegnalazione) {
        this.idSegnalzione=idSegnalazione;
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

    public String getIdSegnalzione() {
        return idSegnalzione;
    }

    public void setIdSegnalzione(String idSegnalzione) {
        this.idSegnalzione = idSegnalzione;
    }

    public long getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(long dataCreazione) {
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
        return "Segnalazione{" +
                "idSegnalzione='" + idSegnalzione + '\'' +
                ", dataCreazione=" + dataCreazione +
                ", oggettoGuasto='" + oggettoGuasto + '\'' +
                ", docente=" + docente.getNome() + " " + docente.getCognome() +
                ", stato='" + stato + '\'' +
                ", descrizone='" + descrizione + '\'' +
                ", aula='" + aula + '\'' +
                ", edifico='" + edificio + '\'' +
                ", tecnico=" + tecnico.getNome() + " " + tecnico.getCognome() +
                '}';
    }
}
