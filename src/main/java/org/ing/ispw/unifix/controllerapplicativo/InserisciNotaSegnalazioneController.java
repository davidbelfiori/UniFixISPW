package org.ing.ispw.unifix.controllerapplicativo;

import org.ing.ispw.unifix.bean.NotaSegnalazioneBean;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.NotaSegnalazioneDao;
import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.model.NotaSegnalazione;
import org.ing.ispw.unifix.model.Segnalazione;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InserisciNotaSegnalazioneController {


    public void inserisciNotaSegnalazione(NotaSegnalazioneBean nsb){
        Date date = new Date();
        SegnalazioneDao segnalazioneDao = DaoFactory.getInstance().getSegnalazioneDao();
        Segnalazione segnalazione = segnalazioneDao.load(nsb.getIdSegnalazione());
        NotaSegnalazioneDao notaSegnalazioneDao = DaoFactory.getInstance().getNotaSegnalazioneDao(); // This line is unchanged
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

        NotaSegnalazioneDao notaSegnalazioneDao = DaoFactory.getInstance().getNotaSegnalazioneDao();
        List<NotaSegnalazione> note = notaSegnalazioneDao.getAllNotaSegnalazioneById(idSegnalazione);
        List<NotaSegnalazioneBean> notaSegnalazioneBeanList = new ArrayList<>();
        for (NotaSegnalazione ns : note) {
            NotaSegnalazioneBean bean = new NotaSegnalazioneBean(idSegnalazione, ns.getDataCreazione(), ns.getTesto());
            notaSegnalazioneBeanList.add(bean);
        }
        return notaSegnalazioneBeanList;
    }

}
