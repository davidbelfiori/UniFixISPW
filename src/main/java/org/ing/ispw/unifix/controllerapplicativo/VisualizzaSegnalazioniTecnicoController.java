package org.ing.ispw.unifix.controllerapplicativo;

import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.exception.NessunaSegnalazioneException;
import org.ing.ispw.unifix.exception.NessunaSegnalazioneTecnicoException;
import org.ing.ispw.unifix.model.Segnalazione;

import java.util.ArrayList;
import java.util.List;

public class VisualizzaSegnalazioniTecnicoController {
    private static VisualizzaSegnalazioniTecnicoController instance;



    private VisualizzaSegnalazioniTecnicoController() {}

    public static VisualizzaSegnalazioniTecnicoController getInstance() {
        if (instance == null) {
            instance = new VisualizzaSegnalazioniTecnicoController();
        }
        return instance;
    }

    //visualizza le segnalazioni assegnate al tecnico
    public List<Segnalazione> visualizzaSegnalazioniTecnico() throws NessunaSegnalazioneException, NessunaSegnalazioneTecnicoException {
        List<Segnalazione> segnalazioniAll = null;
        SegnalazioneDao segnalazioneDao = DaoFactory.getInstance().getSegnalazioneDao();
        LoginController loginController = LoginController.getInstance();
        String tecnico = loginController.getCurrentUser().getEmail();
        segnalazioniAll = segnalazioneDao.getAllSegnalazioni();
        List<Segnalazione> segnalazioniTecnico = new ArrayList<>();
        //prendi solo quelle assegnate al tecnico
        if (segnalazioniAll.isEmpty()) throw new NessunaSegnalazioneException("Nessuna segnalazione presente");
        for (Segnalazione segnalazione : segnalazioniAll) {
            if (segnalazione.getTecnico().getEmail().equals(tecnico)) {
                segnalazioniTecnico.add(segnalazione);
            }
        }
        if (segnalazioniTecnico.isEmpty()) throw new NessunaSegnalazioneTecnicoException("Nessuna segnalazione assegnata al tecnico");
        return segnalazioniTecnico;

    }
}
