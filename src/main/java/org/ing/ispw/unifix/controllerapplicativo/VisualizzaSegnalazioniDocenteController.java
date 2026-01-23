package org.ing.ispw.unifix.controllerapplicativo;

import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.exception.NessunSegnalazioneDocenteException;
import org.ing.ispw.unifix.exception.NessunaSegnalazioneException;
import org.ing.ispw.unifix.model.Segnalazione;

import java.util.ArrayList;
import java.util.List;

public class VisualizzaSegnalazioniDocenteController {



    //visualizza le segnalazioni inviate dal docente
    public List<SegnalazioneBean> visualizzaSegnalazioniDocente() throws NessunaSegnalazioneException, NessunSegnalazioneDocenteException {
        List<Segnalazione> segnalazioniAll = null;
        SegnalazioneDao segnalazioneDao = DaoFactory.getInstance().getSegnalazioneDao();
        LoginController loginController = LoginController.getInstance();
        String docente = loginController.getCurrentUser().getEmail();
        segnalazioniAll = segnalazioneDao.getAllSegnalazioni();
        List<Segnalazione> segnalazioniDocente = new ArrayList<>();
        //prendi solo quelle inviate dal docente
        if (segnalazioniAll.isEmpty()) throw new NessunaSegnalazioneException("Nessuna segnalazione presente");
        for (Segnalazione segnalazione : segnalazioniAll) {
            if (segnalazione.getDocente().getEmail().equals(docente)) {
                segnalazioniDocente.add(segnalazione);
            }
        }
        if (segnalazioniDocente.isEmpty()) throw new NessunSegnalazioneDocenteException("Nessuna segnalazione inviata dal docente");

        //converti le segnalazioni in bean per la view (paradigma MVC)
        List<SegnalazioneBean> segnalazioniBeanList = new ArrayList<>();
        for (Segnalazione segnalazione : segnalazioniDocente) {
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
            bean.setEdificio(segnalazione.getEdificio());
            segnalazioniBeanList.add(bean);
        }
        return segnalazioniBeanList;

    }

}
