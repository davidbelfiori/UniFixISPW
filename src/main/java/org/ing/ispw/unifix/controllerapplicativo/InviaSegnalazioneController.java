package org.ing.ispw.unifix.controllerapplicativo;

import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.exception.NonCiSonoTecniciException;
import org.ing.ispw.unifix.exception.SegnalazioneGiaEsistenteException;
import org.ing.ispw.unifix.model.Aula;
import org.ing.ispw.unifix.model.Docente;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.model.Tecnico;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InviaSegnalazioneController {


    private static InviaSegnalazioneController instance;



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

    public List<Aula> getAuleByEdificio(String edificio){
        AulaDao aulaDao = DaoFactory.getInstance().getAulaDao();
        List<Aula> aule = aulaDao.getAllAule();

        return aule.stream()
                .filter(aula -> aula.getEdificio().equals(edificio)).toList();
    }

    public Aula getOggettiAula(String idAula){
        AulaDao aulaDao = DaoFactory.getInstance().getAulaDao();
        return  aulaDao.load(idAula);

    }

    public Tecnico getTecnicoConMenoSegnalazioni() throws NonCiSonoTecniciException {
        UserDao userDao = DaoFactory.getInstance().getUserDao();
        List<Tecnico> tecnici = userDao.getAllTecnici();
        if (tecnici.isEmpty()) throw new NonCiSonoTecniciException("Non ci sono tecnici disponibili");
        //prendi quello con meno segnalazioni e aggiorna il numero di segnalazioni

        Tecnico tecnicoSelto= tecnici.stream()
                .min((t1, t2) -> Integer.compare(t1.getNumeroSegnalazioni(), t2.getNumeroSegnalazioni()))
                .orElse(null);
        tecnicoSelto.incrementaSegnalazioni();
        userDao.update(tecnicoSelto);
        return tecnicoSelto;
    }

    public  boolean creaSegnalazione(SegnalazioneBean sb) throws SegnalazioneGiaEsistenteException {
        SegnalazioneDao segnalazioneDao=DaoFactory.getInstance().getSegnalazioneDao();
        Docente docenteSegnalatore =(Docente) LoginController.getInstance().getCurrentUser();

        String chiave = "Edificio"+sb.getEdificio()+"_Aula"+sb.getAula()+"_OggettoGuasto"+sb.getOggettoGuasto();

        if (segnalazioneDao.exists(chiave)) throw new SegnalazioneGiaEsistenteException("Segnalazione già esistente");

        Segnalazione segnalazione=segnalazioneDao.create(chiave);
        segnalazione.setAula(sb.getAula());
        segnalazione.setDataCreazione(sb.getDataCreazione());
        segnalazione.setEdifico(sb.getEdificio());
        segnalazione.setDocente(docenteSegnalatore);
        segnalazione.setOggettoGuasto(sb.getOggettoGuasto());
        segnalazione.setTecnico(getTecnicoConMenoSegnalazioni()); // per ora è null ma va implementata la logica in cui viene preso quello con meno segnalazioni
        segnalazione.setStato("APERTA");
        segnalazione.setIdSegnalzione(chiave);
        segnalazione.setDescrizone(sb.getDescrizone());
        segnalazioneDao.store(segnalazione);

        return true;


    }
}
