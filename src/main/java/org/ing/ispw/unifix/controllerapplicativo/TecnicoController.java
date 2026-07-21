package org.ing.ispw.unifix.controllerapplicativo;


import org.ing.ispw.unifix.bean.InfoTecnicoBean;
import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.exception.NessunaSegnalazioneException;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.model.Tecnico;

public class TecnicoController {

    private static TecnicoController instance;
    private final SegnalazioneDao segnalazioneDao ;
    private final UserDao userDao;


    public static TecnicoController getInstance() {
        if(instance == null) {
            instance = new TecnicoController();
        }
        return instance;
    }

    private TecnicoController() {
        segnalazioneDao = DaoFactory.getInstance().getSegnalazioneDao();
        userDao = DaoFactory.getInstance().getUserDao();
    }

    public InfoTecnicoBean getTecnicoInformation(){

        Tecnico currentUser = (Tecnico) LoginController.getInstance().getCurrentUser();
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
                .user(segnalazione.getDocente())
                .stato(segnalazione.getStato())
                .descrizione(segnalazione.getDescrizione())
                .aula(segnalazione.getAula())
                .edificio(segnalazione.getEdificio())
                .tecnico(segnalazione.getTecnico())
                .build();
    }


    public void chiudiSegnalazione(String idSegnalazione) {
        Segnalazione segnalazione = segnalazioneDao.getSegnalazione(idSegnalazione);
        segnalazione.chiudi();
        segnalazioneDao.update(segnalazione);

        //Quando una segnalazione viene chiusa (l'operazione è irreversibile) il numero di interventi del tecnico viene decementato
        Tecnico currentUser = (Tecnico) LoginController.getInstance().getCurrentUser();
        if(currentUser != null) {
            currentUser.decrementaSegnalazioni();
            userDao.update(currentUser);
        }

    }

    public void inLavorazioneSegnalazione(String idSegnalazione) {
        Segnalazione segnalazione = segnalazioneDao.getSegnalazione(idSegnalazione);
        segnalazione.inLavorazione();
        segnalazioneDao.update(segnalazione);
    }


}
