package org.ing.ispw.unifix.controllerapplicativo;

import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.exception.NonCiSonoTecniciException;
import org.ing.ispw.unifix.exception.SegnalazioneGiaEsistenteException;
import org.ing.ispw.unifix.model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InviaSegnalazioneController {


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

    public List<String> getOggettiAula(String idAula){
        AulaDao aulaDao = DaoFactory.getInstance().getAulaDao();
        return  aulaDao.getAulaOggetti(idAula);

    }

    public Tecnico getTecnicoConMenoSegnalazioni() throws NonCiSonoTecniciException {
        UserDao userDao = DaoFactory.getInstance().getUserDao();
        List<Tecnico> tecnici = userDao.getAllTecnici();
        if (tecnici.isEmpty()) throw new NonCiSonoTecniciException("Non ci sono tecnici disponibili");
        //prendi quello con meno segnalazioni

        Tecnico tecnicoScelto = tecnici.stream()
                .min((t1, t2) -> Integer.compare(t1.getNumeroSegnalazioni(), t2.getNumeroSegnalazioni()))
                .orElse(null);
        if (tecnicoScelto == null) throw new NonCiSonoTecniciException("Non ci sono tecnici disponibili");
        return tecnicoScelto;
    }

    public  boolean creaSegnalazione(SegnalazioneBean sb) throws SegnalazioneGiaEsistenteException, NonCiSonoTecniciException {
        SegnalazioneDao segnalazioneDao = DaoFactory.getInstance().getSegnalazioneDao();
        UserDao userDao = DaoFactory.getInstance().getUserDao();
        User docenteSegnalatore = LoginController.getInstance().getCurrentUser();

        String chiave = "Edificio"+sb.getEdificio()+"_Aula"+sb.getAula()+"_OggettoGuasto"+sb.getOggettoGuasto();

        if (segnalazioneDao.exists(chiave)) throw new SegnalazioneGiaEsistenteException("Segnalazione già esistente");

        Tecnico tecnicoAssegnato = getTecnicoConMenoSegnalazioni();

        Segnalazione segnalazione = segnalazioneDao.create(chiave);
        segnalazione.setAula(sb.getAula());
        segnalazione.setDataCreazione(sb.getDataCreazione());
        segnalazione.setEdificio(sb.getEdificio());
        segnalazione.setDocente((Docente) docenteSegnalatore);
        segnalazione.setOggettoGuasto(sb.getOggettoGuasto());
        segnalazione.setTecnico(tecnicoAssegnato);
        segnalazione.setStato("APERTA");
        segnalazione.setIdSegnalzione(chiave);
        segnalazione.setDescrizione(sb.getDescrizione());
        segnalazioneDao.store(segnalazione);

        // Dopo aver salvato la segnalazione, aggiorno il numero di segnalazioni del tecnico
        tecnicoAssegnato.incrementaSegnalazioni();
        userDao.update(tecnicoAssegnato);

        return true;


    }
}
