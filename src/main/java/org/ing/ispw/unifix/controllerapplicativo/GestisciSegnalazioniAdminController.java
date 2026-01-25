package org.ing.ispw.unifix.controllerapplicativo;

import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.exception.NessunaSegnalazioneException;
import org.ing.ispw.unifix.model.Aula;
import org.ing.ispw.unifix.model.Segnalazione;

import java.util.ArrayList;
import java.util.List;

public class GestisciSegnalazioniAdminController {


    public String visualizzaSegnalazioniAttiveAdmin() throws NessunaSegnalazioneException {
        List<Segnalazione> segnalazioni = null;
        int count = 0;
        String result = "";
        SegnalazioneDao segnalazioneDao = DaoFactory.getInstance().getSegnalazioneDao();
        segnalazioni= segnalazioneDao.getAllSegnalazioni();
        if (segnalazioni.isEmpty()) return "0";
        for (Segnalazione segnalazione : segnalazioni) {
            if(segnalazione.getStato().equals("APERTA") || segnalazione.getStato().equals("IN LAVORAZIONE")){
                count++;
            }
        }
        result = String.valueOf(count);
        return result;

    }

    public String  visualizzaSegnalazioniRisolteAdmin() throws NessunaSegnalazioneException {
        List<Segnalazione> segnalazioni = null;
        int count = 0;
        SegnalazioneDao segnalazioneDao = DaoFactory.getInstance().getSegnalazioneDao();
        segnalazioni= segnalazioneDao.getAllSegnalazioni();
        if (segnalazioni.isEmpty()) return "0";
        for (Segnalazione segnalazione : segnalazioni) {
            if(segnalazione.getStato().equals("CHIUSA")){
                count++;
            }
        }

        return String.valueOf(count);

    }

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

    public String visualizzaEdificiGestiti (){
        InviaSegnalazioneController inviaSegnalazioneController = new InviaSegnalazioneController();
        List<String> edifici = inviaSegnalazioneController.getEdifici();
        return String.valueOf(edifici.size());
    }

    public String visualizzaNumeroaule(){
        AulaDao aulaDao = DaoFactory.getInstance().getAulaDao();
        List<Aula> aule = aulaDao.getAllAule();
        return String.valueOf(aule.size());
    }

}
