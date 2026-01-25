package org.ing.ispw.unifix.controllerapplicativo;

import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.exception.NessunaSegnalazioneException;
import org.ing.ispw.unifix.exception.NessunaSegnalazioneTecnicoException;
import org.ing.ispw.unifix.model.Segnalazione;

import java.util.ArrayList;
import java.util.List;

public class VisualizzaSegnalazioniTecnicoController {


    //visualizza le segnalazioni assegnate al tecnico
    public List<SegnalazioneBean> visualizzaSegnalazioniTecnico() throws NessunaSegnalazioneException, NessunaSegnalazioneTecnicoException {
        List<Segnalazione> segnalazioniAll = null;
        SegnalazioneDao segnalazioneDao = DaoFactory.getInstance().getSegnalazioneDao();
        LoginController loginController = LoginController.getInstance();
        String tecnico = loginController.getCurrentUser().getEmail();
        segnalazioniAll = segnalazioneDao.getAllSegnalazioni();
        List<SegnalazioneBean> segnalazioniTecnico = new ArrayList<>();
        //prendi solo quelle assegnate al tecnico
        if (segnalazioniAll.isEmpty()) throw new NessunaSegnalazioneException("Nessuna segnalazione presente");
        for (Segnalazione segnalazione : segnalazioniAll) {
            if (segnalazione.getTecnico().getEmail().equals(tecnico)) {
                SegnalazioneBean segnalazioneBean = new SegnalazioneBean.Builder(segnalazione.getIdSegnalazione())
                        .dataCreazione(segnalazione.getDataCreazione())
                        .oggettoGuasto(segnalazione.getOggettoGuasto())
                        .user(segnalazione.getDocente())
                        .stato(segnalazione.getStato())
                        .descrizione(segnalazione.getDescrizione())
                        .aula(segnalazione.getAula())
                        .edificio(segnalazione.getEdificio())
                        .tecnico(segnalazione.getTecnico())
                        .build();
                segnalazioniTecnico.add(segnalazioneBean);
            }
        }
        if (segnalazioniTecnico.isEmpty()) throw new NessunaSegnalazioneTecnicoException("Nessuna segnalazione assegnata al tecnico");
        return segnalazioniTecnico;

    }
}
