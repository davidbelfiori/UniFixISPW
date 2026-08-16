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

    /**
     * Recupera tutte le segnalazioni inviate dal docente loggato e le converte in oggetti SegnalazioneBean.
     * @return Lista di SegnalazioneBean rappresentanti le segnalazioni inviate dal docente.
     * @throws NessunaSegnalazioneException se non ci sono segnalazioni nel sistema.
     * @throws NessunSegnalazioneDocenteException se il docente loggato non ha inviato alcuna segnalazione.
     * @throws org.ing.ispw.unifix.exception.PersistenceException se si verifica un errore durante l'accesso ai dati.
     */
    public List<SegnalazioneBean> visualizzaSegnalazioniDocente() {
        SegnalazioneDao segnalazioneDao = DaoFactory.getInstance().getSegnalazioneDao();
        UserBean loggetUser = SessionManager.getInstance().getCurrentUser();
        if (loggetUser == null) {
            throw new IllegalStateException("Nessun docente loggato");
        }
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

            segnalazioniBeanList.add(bean);
        }

        return segnalazioniBeanList;

    }

}
