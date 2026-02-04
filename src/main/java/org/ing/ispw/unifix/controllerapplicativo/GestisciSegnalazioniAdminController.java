package org.ing.ispw.unifix.controllerapplicativo;

import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.model.Segnalazione;

import java.util.ArrayList;
import java.util.List;

public class GestisciSegnalazioniAdminController {

    public List<SegnalazioneBean> getAllSegnalazioni(){

        SegnalazioneDao segnalazioneDao = DaoFactory.getInstance().getSegnalazioneDao();
        List<Segnalazione> segnalazioni = segnalazioneDao.getAllSegnalazioni();
        List<SegnalazioneBean> segnalazioneBeanList = new ArrayList<>();
        for (Segnalazione segnalazione : segnalazioni) {
            SegnalazioneBean bean = new SegnalazioneBean.Builder(segnalazione.getIdSegnalazione())
                    .dataCreazione(segnalazione.getDataCreazione())
                    .oggettoGuasto(segnalazione.getOggettoGuasto())
                    .user(segnalazione.getDocente())
                    .stato(segnalazione.getStato())
                    .descrizione(segnalazione.getDescrizione())
                    .aula(segnalazione.getAula())
                    .edificio(segnalazione.getEdificio())
                    .tecnico(segnalazione.getTecnico())
                    .build();
            segnalazioneBeanList.add(bean);

        }
        return segnalazioneBeanList;
    }



}
