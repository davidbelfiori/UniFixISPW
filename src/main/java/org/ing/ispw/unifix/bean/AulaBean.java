package org.ing.ispw.unifix.bean;

import org.ing.ispw.unifix.exception.DatiAulaNonValidiException;

import java.util.List;

public class AulaBean {
    private String idAula;
    private int piano;
    private String edificio;
    private List<String> oggetti;

    //costruttore con validazione per input utente
    public AulaBean(String idAula, String edificio, int piano, List<String> oggetti) throws DatiAulaNonValidiException {
        //validazione dati
        if (idAula == null || idAula.trim().isEmpty()) {
            throw new DatiAulaNonValidiException("ID Aula non può essere vuoto");
        }
        if (edificio == null || edificio.trim().isEmpty()) {
            throw new DatiAulaNonValidiException("Edificio non può essere vuoto");
        }
        if (piano < 0) {
            throw new DatiAulaNonValidiException("Piano non può essere negativo");
        }
        if (oggetti == null || oggetti.isEmpty()) {
            throw new DatiAulaNonValidiException("Deve essere presente almeno un oggetto");
        }

        this.idAula = idAula;
        this.edificio = edificio;
        this.piano = piano;
        this.oggetti = oggetti;
    }


    public String getIdAula() {
        return idAula;
    }

    public void setIdAula(String idAula) {
        this.idAula = idAula;
    }

    public int getPiano() {
        return piano;
    }

    public void setPiano(int piano) {
        this.piano = piano;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public List<String> getOggetti() {
        return oggetti;
    }

    public void setOggetti(List<String> oggetti) {
        this.oggetti = oggetti;
    }
}
