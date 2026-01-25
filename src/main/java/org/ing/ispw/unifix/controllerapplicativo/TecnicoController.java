package org.ing.ispw.unifix.controllerapplicativo;


import org.ing.ispw.unifix.bean.InfoTecnicoBean;
import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.model.Tecnico;

public class TecnicoController {

    private static TecnicoController instance;

    public static TecnicoController getInstance() {
        if(instance == null) {
            instance = new TecnicoController();
        }
        return instance;
    }

    private TecnicoController() {

    }

    public InfoTecnicoBean getTecnicoInformation(){

        Tecnico currentUser = (Tecnico) LoginController.getInstance().getCurrentUser();
        return new InfoTecnicoBean(currentUser.getNome(), currentUser.getCognome(), currentUser.getEmail(), currentUser.getPassword(),currentUser.getRuolo(), currentUser.getNumeroSegnalazioni());

    }

    public SegnalazioneBean getSegnalazione(String idSegnalazione) {
        SegnalazioneDao segnalazioneDao = DaoFactory.getInstance().getSegnalazioneDao();
        Segnalazione segnalazione = segnalazioneDao.getSegnalazione(idSegnalazione);
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


    public void updateSegnalazione(SegnalazioneBean segnalazioneBean) {
        SegnalazioneDao segnalazioneDao = DaoFactory.getInstance().getSegnalazioneDao();
        Segnalazione segnalazione= segnalazioneDao.getSegnalazione(segnalazioneBean.getIdSegnalazione());
        segnalazione.setStato(segnalazioneBean.getStato());
        segnalazioneDao.update(segnalazione);
    }

}
