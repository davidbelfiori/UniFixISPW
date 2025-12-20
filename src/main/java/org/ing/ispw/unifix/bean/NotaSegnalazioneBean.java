package org.ing.ispw.unifix.bean;

public class NotaSegnalazioneBean {
    private String idSegnalazione;
    private String testoNota;

    public NotaSegnalazioneBean(String idSegnalazione, String testoNota) {
        this.idSegnalazione = idSegnalazione;
        this.testoNota = testoNota;
    }

    public String getIdSegnalazione() {
        return idSegnalazione;
    }

    public void setIdSegnalazione(String idSegnalazione) {
        this.idSegnalazione = idSegnalazione;
    }

    public String getTestoNota() {
        return testoNota;
    }

    public void setTestoNota(String testoNota) {
        this.testoNota = testoNota;
    }

}
