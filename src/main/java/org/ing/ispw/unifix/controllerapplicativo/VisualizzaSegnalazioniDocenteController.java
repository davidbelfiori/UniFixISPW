package org.ing.ispw.unifix.controllerapplicativo;

import org.ing.ispw.unifix.bean.InfoDocenteBean;
import org.ing.ispw.unifix.bean.InfoTecnicoBean;
import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.bean.UserBean;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.exception.NessunSegnalazioneDocenteException;
import org.ing.ispw.unifix.exception.NessunaSegnalazioneException;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.model.User;
import org.ing.ispw.unifix.sessionmanager.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class VisualizzaSegnalazioniDocenteController {

    private  final UserDao userDao;

    public VisualizzaSegnalazioniDocenteController() {
        userDao = DaoFactory.getInstance().getUserDao();
    }

    //visualizza le segnalazioni inviate dal docente
    public List<SegnalazioneBean> visualizzaSegnalazioniDocente() throws NessunaSegnalazioneException, NessunSegnalazioneDocenteException {
        SegnalazioneDao segnalazioneDao = DaoFactory.getInstance().getSegnalazioneDao();
        UserBean loggetUser = SessionManager.getInstance().getCurrentUser();
        User currentUser = userDao.load(loggetUser.getEmail());
        if (currentUser == null) {
            throw new IllegalStateException("Nessun docente loggato");
        }
        String docenteMail = currentUser.getEmail();
        List<Segnalazione> segnalazioniDocente = segnalazioneDao.getSegnalazioniByDocente(docenteMail);

        if (segnalazioniDocente.isEmpty()) throw new NessunSegnalazioneDocenteException("Nessuna segnalazione inviata dal docente");

        //converti le segnalazioni in bean per la view (paradigma MVC)
        List<SegnalazioneBean> segnalazioniBeanList = new ArrayList<>();
        for (Segnalazione segnalazione : segnalazioniDocente) {
            SegnalazioneBean bean = new SegnalazioneBean.Builder(segnalazione.getIdSegnalazione())
                    .dataCreazione(segnalazione.getDataCreazione())
                    .oggettoGuasto(segnalazione.getOggettoGuasto())
                    .user(new InfoDocenteBean(
                            segnalazione.getDocente().getNome(),
                            segnalazione.getDocente().getCognome(),
                            segnalazione.getDocente().getEmail()))
                    .stato(segnalazione.getStato())
                    .descrizione(segnalazione.getDescrizione())
                    .aula(segnalazione.getAula())
                    .edificio(segnalazione.getEdificio())
                    .tecnico(new InfoTecnicoBean(
                            segnalazione.getTecnico().getNumeroSegnalazioni(),
                            segnalazione.getTecnico().getEmail(),
                            segnalazione.getTecnico().getCognome(),
                            segnalazione.getTecnico().getNome()))
                    .build();
            segnalazioniBeanList.add(bean);
        }
        return segnalazioniBeanList;

    }

}
