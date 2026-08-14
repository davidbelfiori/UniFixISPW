package org.ing.ispw.unifix.controllerapplicativo;


import org.ing.ispw.unifix.bean.InfoDocenteBean;
import org.ing.ispw.unifix.bean.InfoTecnicoBean;
import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.bean.UserBean;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.exception.NessunaSegnalazioneException;
import org.ing.ispw.unifix.exception.InvalidStateTransitionException;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.model.Tecnico;
import org.ing.ispw.unifix.sessionmanager.SessionManager;
import org.ing.ispw.unifix.utils.observer.EmailNotificationService;

public class TecnicoController {

    private final SegnalazioneDao segnalazioneDao ;
    private final UserDao userDao;



    public TecnicoController() {
        segnalazioneDao = DaoFactory.getInstance().getSegnalazioneDao();
        userDao = DaoFactory.getInstance().getUserDao();
    }

    public InfoTecnicoBean getTecnicoInformation(){
        UserBean loggetUser = SessionManager.getInstance().getCurrentUser();
        if (loggetUser == null) {
            throw new IllegalStateException("Nessun tecnico loggato.");
        }
        Tecnico currentUser = (Tecnico) userDao.load(loggetUser.getEmail());
        if (currentUser == null) {
            throw new IllegalStateException("Nessun tecnico loggato.");
        }
        InfoTecnicoBean infoTecnico = new InfoTecnicoBean();
        infoTecnico.setNome(currentUser.getNome());
        infoTecnico.setCognome(currentUser.getCognome());
        infoTecnico.setEmail(currentUser.getEmail());
        infoTecnico.setRuolo(currentUser.getRuolo());
        infoTecnico.setNumeroSegnalazioni(currentUser.getNumeroSegnalazioni());
        return infoTecnico;
    }

    public SegnalazioneBean getSegnalazione(String idSegnalazione) {
        Segnalazione segnalazione = segnalazioneDao.load(idSegnalazione);
        if (segnalazione == null) {
            throw new NessunaSegnalazioneException("Segnalazione non trovata con ID: " + idSegnalazione);
        }

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

        return bean;
    }


    public void chiudiSegnalazione(String idSegnalazione) throws InvalidStateTransitionException {
        Segnalazione segnalazione = segnalazioneDao.load(idSegnalazione);
        segnalazione.chiudi();
        segnalazioneDao.update(segnalazione);

        //Quando una segnalazione viene chiusa (l'operazione è irreversibile) il numero di interventi del tecnico viene decementato
        UserBean loggetUser = SessionManager.getInstance().getCurrentUser();
        if (loggetUser != null) {
            Tecnico currentUser = (Tecnico) userDao.load(loggetUser.getEmail());
            if(currentUser != null) {
                currentUser.decrementaSegnalazioni();
                userDao.update(currentUser);
            }
        }

        segnalazione.attach(new EmailNotificationService());
        segnalazione.notifyObservers(segnalazione);

    }

    public void inLavorazioneSegnalazione(String idSegnalazione) throws InvalidStateTransitionException{
        Segnalazione segnalazione = segnalazioneDao.load(idSegnalazione);
        segnalazione.inLavorazione();
        segnalazioneDao.update(segnalazione);
        segnalazione.attach(new EmailNotificationService());
        segnalazione.notifyObservers(segnalazione);

    }


}
