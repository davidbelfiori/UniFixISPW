package org.ing.ispw.unifix.controllerapplicativo;

import org.ing.ispw.unifix.bean.InfoDocenteBean;
import org.ing.ispw.unifix.bean.InfoTecnicoBean;
import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.bean.UserBean;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.exception.NessunaSegnalazioneException;
import org.ing.ispw.unifix.exception.NessunaSegnalazioneTecnicoException;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.model.User;
import org.ing.ispw.unifix.sessionmanager.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class VisualizzaSegnalazioniTecnicoController {
    private  final UserDao userDao;
    private final SegnalazioneDao segnalazioneDao;


    public VisualizzaSegnalazioniTecnicoController(){
        this.userDao = DaoFactory.getInstance().getUserDao();
        this.segnalazioneDao = DaoFactory.getInstance().getSegnalazioneDao();
    }

    //visualizza le segnalazioni assegnate al tecnico
    public List<SegnalazioneBean> visualizzaSegnalazioniTecnico() throws NessunaSegnalazioneException, NessunaSegnalazioneTecnicoException {
        List<Segnalazione> segnalazioniAll;
        UserBean loggedUser = SessionManager.getInstance().getCurrentUser();
        if (loggedUser == null) {
            throw new IllegalStateException("Nessun tecnico loggato");
        }
        User currentUser = userDao.load(loggedUser.getEmail());
        if (currentUser == null) {
            throw new IllegalStateException("Nessun tecnicoMail loggato");
        }
        String tecnicoMail = currentUser.getEmail();
        segnalazioniAll = segnalazioneDao.getSegnalazioniByTecnico(tecnicoMail);
        List<SegnalazioneBean> segnalazioniTecnico = new ArrayList<>();
        //prendi solo quelle assegnate al tecnicoMail
        if (segnalazioniAll.isEmpty()) throw new NessunaSegnalazioneException("Nessuna segnalazione presente");
        for (Segnalazione segnalazione : segnalazioniAll) {
                SegnalazioneBean segnalazioneBean = new SegnalazioneBean();
                segnalazioneBean.setIdSegnalazione(segnalazione.getIdSegnalazione());
                segnalazioneBean.setDataCreazione(segnalazione.getDataCreazione());
                segnalazioneBean.setOggettoGuasto(segnalazione.getOggettoGuasto());
                segnalazioneBean.setUser(new InfoDocenteBean(segnalazione.getDocente().getNome(), segnalazione.getDocente().getCognome(), segnalazione.getDocente().getEmail()));
                segnalazioneBean.setStato(segnalazione.getStato());
                segnalazioneBean.setDescrizione(segnalazione.getDescrizione());
                segnalazioneBean.setAula(segnalazione.getAula());
                segnalazioneBean.setEdificio(segnalazione.getEdificio());
                segnalazioneBean.setTecnico(new InfoTecnicoBean(
                        segnalazione.getTecnico().getNumeroSegnalazioni(),
                        segnalazione.getTecnico().getEmail(),
                        segnalazione.getTecnico().getCognome(),
                        segnalazione.getTecnico().getNome()));
                segnalazioniTecnico.add(segnalazioneBean);
        }
        return segnalazioniTecnico;

    }
}
