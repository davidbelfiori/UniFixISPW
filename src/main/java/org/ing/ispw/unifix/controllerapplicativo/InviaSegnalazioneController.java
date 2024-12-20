package org.ing.ispw.unifix.controllerapplicativo;

import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.model.Aula;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InviaSegnalazioneController {


    private static InviaSegnalazioneController instance;

    private User currentUser;

    private InviaSegnalazioneController() {}

    public static InviaSegnalazioneController getInstance() {
        if (instance == null) {
            instance = new InviaSegnalazioneController();
        }
        return instance;
    }


    public List<String> getEdifici(){
        AulaDao aulaDao = DaoFactory.getInstance().getAulaDao();
        List<Aula> aule = aulaDao.getAllAule();

        // Usa un Set per eliminare i duplicati
        Set<String> edificiUnici = new HashSet<>();
        for (Aula aula : aule) {
            edificiUnici.add(aula.getEdificio());
        }

        // Imposta gli edifici unici nel SegnalazioneBean
       return new ArrayList<>(edificiUnici);
    }


}
