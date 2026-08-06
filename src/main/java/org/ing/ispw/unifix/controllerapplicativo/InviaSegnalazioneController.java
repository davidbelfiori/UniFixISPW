package org.ing.ispw.unifix.controllerapplicativo;

import org.ing.ispw.unifix.bean.AulaBean;
import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.bean.UserBean;
import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.exception.NonCiSonoTecniciException;
import org.ing.ispw.unifix.exception.SegnalazioneGiaEsistenteException;
import org.ing.ispw.unifix.model.*;
import org.ing.ispw.unifix.sessionmanager.SessionManager;
import org.ing.ispw.unifix.utils.observer.EmailNotificationService;


import java.util.ArrayList;
import java.util.List;


public class InviaSegnalazioneController {


    private final SegnalazioneDao segnalazioneDao;
    private final UserDao userDao;
    private final AulaDao aulaDao;

    public InviaSegnalazioneController() {
        this.userDao = DaoFactory.getInstance().getUserDao();
        this.aulaDao = DaoFactory.getInstance().getAulaDao();
        this.segnalazioneDao = DaoFactory.getInstance().getSegnalazioneDao();
    }

    public List<String> getEdifici(){
        return aulaDao.getAllEdifici();
    }

    public List<AulaBean> getAuleByEdificio(String edificio){
        // 1. Recupero la lista delle entità di Dominio dal DAO
        List<Aula> aule = aulaDao.getAllAule();

        // 2. Creo una nuova lista vuota che conterrà i Bean per la View
        List<AulaBean> auleBeanList = new ArrayList<>();
        // 3. Scorro tutte le aule trovate
        for (Aula aula : aule) {
            // Controllo se l'edificio dell'aula corrisponde a quello cercato
            if (aula.getEdificio() != null && aula.getEdificio().equals(edificio)) {

                // Creo un nuovo oggetto AulaBean
                AulaBean bean = new AulaBean();
                bean.setIdAula(aula.getIdAula());
                bean.setEdificio(aula.getEdificio());
                bean.setPiano(aula.getPiano());
                bean.setOggetti(aula.getOggetti());

                // Aggiungo il Bean convertito alla lista
                auleBeanList.add(bean);
            }
        }
        // 4. Restituisco la lista dei Bean pronti per la View
        return auleBeanList;
    }

    public List<String> getOggettiAula(String edificio, String idAula) {
        return aulaDao.getAulaOggetti(edificio, idAula);
    }

    private Tecnico getTecnicoConMenoSegnalazioni() throws NonCiSonoTecniciException {
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
        UserBean loggetUser = SessionManager.getInstance().getCurrentUser();
        if (loggetUser == null) {
            throw new IllegalStateException("Nessun utente loggato");
        }
        User docenteSegnalatore = userDao.load(loggetUser.getEmail());

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
        segnalazione.setIdSegnalazione(chiave);
        segnalazione.setDescrizione(sb.getDescrizione());
        segnalazioneDao.store(segnalazione);

        // Dopo aver salvato la segnalazione, aggiorno il numero di segnalazioni del tecnico
        tecnicoAssegnato.incrementaSegnalazioni();
        userDao.update(tecnicoAssegnato);

        segnalazione.attach(new EmailNotificationService());
        segnalazione.notifyObservers(segnalazione);

        return true;


    }
}
