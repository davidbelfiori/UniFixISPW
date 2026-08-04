package org.ing.ispw.unifix.controllerapplicativo;

import org.ing.ispw.unifix.bean.NotaSegnalazioneBean;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.NotaSegnalazioneDao;
import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.exception.NotaStatoSegnalazioneLavorazioneException;
import org.ing.ispw.unifix.exception.SegnalazioneNonTrovataException;
import org.ing.ispw.unifix.model.NotaSegnalazione;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.utils.StatoSegnalazione;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InserisciNotaSegnalazioneController {

    private final SegnalazioneDao segnalazioneDao;
    private final NotaSegnalazioneDao notaSegnalazioneDao;

    public InserisciNotaSegnalazioneController(){
        this.segnalazioneDao = DaoFactory.getInstance().getSegnalazioneDao();
        this.notaSegnalazioneDao = DaoFactory.getInstance().getNotaSegnalazioneDao();
    }


/**
    Add new note for a segnalazione
    @param nsb che è di tipo NotaSegnalazioneBean
    @throws NotaStatoSegnalazioneLavorazioneException se l'intervento non è in lavorazione
    @return void
* */
    public void inserisciNotaSegnalazione(NotaSegnalazioneBean nsb){
        Date date = new Date();
        Segnalazione segnalazione = segnalazioneDao.load(nsb.getIdSegnalazione());
        if (segnalazione == null) {
            throw new SegnalazioneNonTrovataException("Segnalazione non trovata con ID: " + nsb.getIdSegnalazione());
        }
        if (segnalazione.getStato() != StatoSegnalazione.IN_LAVORAZIONE) {
            throw new NotaStatoSegnalazioneLavorazioneException("L'intervento deve essere in lavorazione per aggiungere una nota");
        }
        String chiave = "IdSegnalazione" + nsb.getIdSegnalazione().trim() + "_NotaFrom"+segnalazione.getTecnico().getEmail()+"_Date"+System.currentTimeMillis();
        NotaSegnalazione ns = notaSegnalazioneDao.create(chiave);
        ns.setUuid(chiave);
        ns.setSegnalazione(segnalazione);
        ns.setTesto(nsb.getTestoNota());
        ns.setTecnico(segnalazione.getTecnico());
        ns.setDataCreazione(new Timestamp(date.getTime()));
        notaSegnalazioneDao.store(ns);
    }

    /**
     * Recupera tutte le note associate a una segnalazione.
     *
     * @param idSegnalazione l'ID della segnalazione
     * @return lista delle note, può essere vuota se non ci sono note
     * @throws IllegalArgumentException se l'ID è null o vuoto
     */
    public List<NotaSegnalazioneBean> getNoteForSegnalazione(String idSegnalazione) {
        if (idSegnalazione == null || idSegnalazione.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID della segnalazione non può essere vuoto");
        }

        List<NotaSegnalazione> note = notaSegnalazioneDao.getAllNotaSegnalazioneById(idSegnalazione);
        List<NotaSegnalazioneBean> notaSegnalazioneBeanList = new ArrayList<>();
        for (NotaSegnalazione ns : note) {
            NotaSegnalazioneBean bean = new NotaSegnalazioneBean();
            bean.setTestoNota(ns.getTesto());
            bean.setIdSegnalazione(ns.getSegnalazione().getIdSegnalazione());
            bean.setDataCreazione(ns.getDataCreazione());
            notaSegnalazioneBeanList.add(bean);
        }
        return notaSegnalazioneBeanList;
    }

}
