package org.ing.ispw.unifix.controllerapplicativo;

import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.exception.NessunSegnalazioneDocenteException;
import org.ing.ispw.unifix.exception.NessunaSegnalazioneException;
import org.ing.ispw.unifix.model.Segnalazione;

import java.util.ArrayList;
import java.util.List;

public class VisualizzaSegnalazioniDocenteController {

    private static VisualizzaSegnalazioniDocenteController instance;



    private VisualizzaSegnalazioniDocenteController() {}

    public static VisualizzaSegnalazioniDocenteController getInstance() {
        if (instance == null) {
            instance = new VisualizzaSegnalazioniDocenteController();
        }
        return instance;
    }

    //visualizza le segnalazioni inviate dal docente
    public List<Segnalazione> visualizzaSegnalazioniDocente() throws NessunaSegnalazioneException, NessunSegnalazioneDocenteException {
        List<Segnalazione> segnalazioniAll = null;
        SegnalazioneDao segnalazioneDao = DaoFactory.getInstance().getSegnalazioneDao();
        LoginController loginController = LoginController.getInstance();
        String docente = loginController.getCurrentUser().getEmail();
       segnalazioniAll = segnalazioneDao.getAllSegnalazioni();
       List<Segnalazione> segnalazioniDocente = new ArrayList<>();
       //prendi solo quelle inviate dal docente
        if (segnalazioniAll.isEmpty()) throw new NessunaSegnalazioneException("Nessuna segnalazione presente");
        for (Segnalazione segnalazione : segnalazioniAll) {
            if (segnalazione.getDocente().getEmail().equals(docente)) {
                segnalazioniDocente.add(segnalazione);
            }
        }
        if (segnalazioniDocente.isEmpty()) throw new NessunSegnalazioneDocenteException("Nessuna segnalazione inviata dal docente");
        return segnalazioniDocente;

    }

}
