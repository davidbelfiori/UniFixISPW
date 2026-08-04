package org.ing.ispw.unifix.controllerapplicativo;

import org.ing.ispw.unifix.bean.InfoDocenteBean;
import org.ing.ispw.unifix.bean.InfoTecnicoBean;
import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.model.Segnalazione;

import java.util.ArrayList;
import java.util.List;

public class GestisciSegnalazioniAdminController {

    private final SegnalazioneDao segnalazioneDao;

    public GestisciSegnalazioniAdminController(){
        this.segnalazioneDao = DaoFactory.getInstance().getSegnalazioneDao();
    }


    public List<SegnalazioneBean> getAllSegnalazioni() {
        List<Segnalazione> segnalazioni = segnalazioneDao.getAllSegnalazioni();
        List<SegnalazioneBean> segnalazioneBeanList = new ArrayList<>();

        for (Segnalazione segnalazione : segnalazioni) {
            SegnalazioneBean bean = new SegnalazioneBean();
            bean.setIdSegnalazione(segnalazione.getIdSegnalazione());
            bean.setDataCreazione(segnalazione.getDataCreazione());
            bean.setOggettoGuasto(segnalazione.getOggettoGuasto());

            if (segnalazione.getDocente() != null) {
                bean.setUser(new InfoDocenteBean(
                        segnalazione.getDocente().getNome(),
                        segnalazione.getDocente().getCognome(),
                        segnalazione.getDocente().getEmail()));
            }

            bean.setStato(segnalazione.getStato());
            bean.setDescrizione(segnalazione.getDescrizione());
            bean.setAula(segnalazione.getAula());
            bean.setEdificio(segnalazione.getEdificio());

            if (segnalazione.getTecnico() != null) {
                bean.setTecnico(new InfoTecnicoBean(
                        segnalazione.getTecnico().getNumeroSegnalazioni(),
                        segnalazione.getTecnico().getEmail(),
                        segnalazione.getTecnico().getCognome(),
                        segnalazione.getTecnico().getNome()));
            }

            segnalazioneBeanList.add(bean);
        }

        return segnalazioneBeanList;
    }


}
