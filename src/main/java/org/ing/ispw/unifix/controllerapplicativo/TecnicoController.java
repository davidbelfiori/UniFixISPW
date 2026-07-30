package org.ing.ispw.unifix.controllerapplicativo;


import org.ing.ispw.unifix.bean.InfoDocenteBean;
import org.ing.ispw.unifix.bean.InfoTecnicoBean;
import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.bean.UserBean;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.exception.NessunaSegnalazioneException;
import org.ing.ispw.unifix.exception.StateExecption;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.model.Tecnico;
import org.ing.ispw.unifix.sessionmanager.SessionManager;

public class TecnicoController {

    private final SegnalazioneDao segnalazioneDao ;
    private final UserDao userDao;



    public TecnicoController() {
        segnalazioneDao = DaoFactory.getInstance().getSegnalazioneDao();
        userDao = DaoFactory.getInstance().getUserDao();
    }

    public InfoTecnicoBean getTecnicoInformation(){
        UserBean loggetUser = SessionManager.getInstance().getCurrentUser();
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
        Segnalazione segnalazione = segnalazioneDao.getSegnalazione(idSegnalazione);
        if (segnalazione == null) {
            throw new NessunaSegnalazioneException("Segnalazione non trovata con ID: " + idSegnalazione);
        }
        return new SegnalazioneBean.Builder(segnalazione.getIdSegnalazione()).dataCreazione(segnalazione.getDataCreazione())
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
    }


    public void chiudiSegnalazione(String idSegnalazione) throws StateExecption {
        Segnalazione segnalazione = segnalazioneDao.getSegnalazione(idSegnalazione);
        segnalazione.chiudi();
        segnalazioneDao.update(segnalazione);

        //Quando una segnalazione viene chiusa (l'operazione è irreversibile) il numero di interventi del tecnico viene decementato
        UserBean loggetUser = SessionManager.getInstance().getCurrentUser();
        Tecnico currentUser = (Tecnico) userDao.load(loggetUser.getEmail());
        if(currentUser != null) {
            currentUser.decrementaSegnalazioni();
            userDao.update(currentUser);
        }

        segnalazione.attach((segnalazione.getDocente()));
        segnalazione.attach(segnalazione.getTecnico());
        segnalazione.notifyObservers(segnalazione);

    }

    public void inLavorazioneSegnalazione(String idSegnalazione) {
        Segnalazione segnalazione = segnalazioneDao.getSegnalazione(idSegnalazione);
        segnalazione.inLavorazione();
        segnalazioneDao.update(segnalazione);
        segnalazione.attach((segnalazione.getDocente()));
        segnalazione.attach(segnalazione.getTecnico());
        segnalazione.notifyObservers(segnalazione);

    }


}
