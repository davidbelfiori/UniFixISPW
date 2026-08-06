package org.ing.ispw.controller.applicativo;

import org.ing.ispw.unifix.bean.AulaBean;
import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.bean.UserBean;
import org.ing.ispw.unifix.controllerapplicativo.InviaSegnalazioneController;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.dao.memory.InMemoryDaoFactory;
import org.ing.ispw.unifix.exception.NonCiSonoTecniciException;
import org.ing.ispw.unifix.exception.SegnalazioneGiaEsistenteException;
import org.ing.ispw.unifix.model.Docente;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.model.Tecnico;
import org.ing.ispw.unifix.model.UserFactory;
import org.ing.ispw.unifix.sessionmanager.SessionManager;
import org.ing.ispw.unifix.utils.DemoData;
import org.ing.ispw.unifix.utils.StatoSegnalazione;
import org.ing.ispw.unifix.utils.UserType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InviaSegnalazioneTest {

    private InviaSegnalazioneController controller;
    private SegnalazioneDao segnalazioneDao;
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        InMemoryDaoFactory memoryDaoFactory = new InMemoryDaoFactory();
        DaoFactory.setInstance(memoryDaoFactory);
        DemoData.load();

        userDao = DaoFactory.getInstance().getUserDao();
        segnalazioneDao = DaoFactory.getInstance().getSegnalazioneDao();
        controller = new InviaSegnalazioneController();

        // Imposta un utente docente loggato nella sessione
        UserBean docenteBean = new UserBean();
        docenteBean.setEmail("marco.bianchi@uniroma2.eu");
        docenteBean.setRuolo(UserType.DOCENTE);
        SessionManager.getInstance().setCurrentUser(docenteBean);
    }

    @AfterEach
    void tearDown() {
        SessionManager.getInstance().clearSession();
    }

    @Test
    @DisplayName("Test invio segnalazione con successo")
    void testInviaSegnalazioneSuccesso() throws SegnalazioneGiaEsistenteException, NonCiSonoTecniciException {
        SegnalazioneBean segnalazioneBean = new SegnalazioneBean();
        segnalazioneBean.setDataCreazione(Date.valueOf("2024-06-10"));
        segnalazioneBean.setEdificio("Ingegneria");
        segnalazioneBean.setAula("1");
        segnalazioneBean.setOggettoGuasto("Proiettore");
        segnalazioneBean.setDescrizione("La lampada del proiettore non si accende.");

        boolean result = controller.creaSegnalazione(segnalazioneBean);
        assertTrue(result, "La creazione della segnalazione deve restituire true");

        String chiave = "EdificioIngegneria_Aula1_OggettoGuastoProiettore";
        assertTrue(segnalazioneDao.exists(chiave), "La segnalazione deve essere presente nel DAO");

        Segnalazione segnalazioneCreata = segnalazioneDao.getSegnalazione(chiave);
        assertNotNull(segnalazioneCreata, "L'entità segnalazione salvata non deve essere null");
        assertEquals("Ingegneria", segnalazioneCreata.getEdificio());
        assertEquals("1", segnalazioneCreata.getAula());
        assertEquals("Proiettore", segnalazioneCreata.getOggettoGuasto());
        assertEquals("La lampada del proiettore non si accende.", segnalazioneCreata.getDescrizione());
        assertEquals(Date.valueOf("2024-06-10"), segnalazioneCreata.getDataCreazione());
        assertEquals(StatoSegnalazione.APERTA, segnalazioneCreata.getStato());
        assertNotNull(segnalazioneCreata.getDocente());
        assertEquals("marco.bianchi@uniroma2.eu", segnalazioneCreata.getDocente().getEmail());
        assertNotNull(segnalazioneCreata.getTecnico());
        assertEquals("giuseppe.rossi@tec.uniroma2.eu", segnalazioneCreata.getTecnico().getEmail());
        assertEquals(1, segnalazioneCreata.getTecnico().getNumeroSegnalazioni(), "Il contatore del tecnico deve essere incrementato a 1");
    }

    @Test
    @DisplayName("Test invio segnalazione già esistente lancia SegnalazioneGiaEsistenteException")
    void testInviaSegnalazioneGiaEsistente() throws SegnalazioneGiaEsistenteException, NonCiSonoTecniciException {
        SegnalazioneBean segnalazioneBean = new SegnalazioneBean();
        segnalazioneBean.setDataCreazione(Date.valueOf("2024-06-10"));
        segnalazioneBean.setEdificio("Ingegneria");
        segnalazioneBean.setAula("1");
        segnalazioneBean.setOggettoGuasto("Proiettore");
        segnalazioneBean.setDescrizione("Prima segnalazione");

        // Primo invio valido
        assertTrue(controller.creaSegnalazione(segnalazioneBean));

        // Secondo invio con gli stessi dati identificativi
        assertThrows(SegnalazioneGiaEsistenteException.class, () -> controller.creaSegnalazione(segnalazioneBean));
    }

    @Test
    @DisplayName("Test invio segnalazione senza utente loggato lancia IllegalStateException")
    void testInviaSegnalazioneSenzaUtenteLoggato() {
        SessionManager.getInstance().clearSession();

        SegnalazioneBean segnalazioneBean = new SegnalazioneBean();
        segnalazioneBean.setDataCreazione(Date.valueOf("2024-06-10"));
        segnalazioneBean.setEdificio("Ingegneria");
        segnalazioneBean.setAula("1");
        segnalazioneBean.setOggettoGuasto("Lavagna");
        segnalazioneBean.setDescrizione("Lavagna rotta");

        assertThrows(IllegalStateException.class, () -> controller.creaSegnalazione(segnalazioneBean));
    }

    @Test
    @DisplayName("Test invio segnalazione senza tecnici disponibili lancia NonCiSonoTecniciException")
    void testInviaSegnalazioneSenzaTecniciDisponibili() {
        // Reset factory con DAO vuoto (senza eseguire DemoData.load())
        DaoFactory.setInstance(new InMemoryDaoFactory());
        UserDao emptyUserDao = DaoFactory.getInstance().getUserDao();
        // Registriamo solo il docente segnalatore
        Docente docente = (Docente) UserFactory.createUser("marco.bianchi@uniroma2.eu", "admin", "Marco", "Bianchi", UserType.DOCENTE, 0);
        emptyUserDao.store(docente);

        InviaSegnalazioneController localController = new InviaSegnalazioneController();

        SegnalazioneBean segnalazioneBean = new SegnalazioneBean();
        segnalazioneBean.setDataCreazione(Date.valueOf("2024-06-10"));
        segnalazioneBean.setEdificio("Ingegneria");
        segnalazioneBean.setAula("1");
        segnalazioneBean.setOggettoGuasto("Banchi");
        segnalazioneBean.setDescrizione("Banco danneggiato");

        assertThrows(NonCiSonoTecniciException.class, () -> localController.creaSegnalazione(segnalazioneBean));
    }

    @Test
    @DisplayName("Test assegnazione automatica al tecnico con minor carico di lavoro")
    void testAssegnazioneAlTecnicoConMenoSegnalazioni() throws SegnalazioneGiaEsistenteException, NonCiSonoTecniciException {
        // Aggiungiamo un secondo tecnico con 5 segnalazioni pregresse
        Tecnico tecnicoOccupato = (Tecnico) UserFactory.createUser("luca.verdi@tec.uniroma2.eu", "admin", "Luca", "Verdi", UserType.TECNICO, 5);
        userDao.store(tecnicoOccupato);

        // Giuseppe Rossi (da DemoData) ha 0 segnalazioni
        SegnalazioneBean segnalazioneBean = new SegnalazioneBean();
        segnalazioneBean.setDataCreazione(Date.valueOf("2024-06-10"));
        segnalazioneBean.setEdificio("Ingegneria");
        segnalazioneBean.setAula("2");
        segnalazioneBean.setOggettoGuasto("Impianto Audio");
        segnalazioneBean.setDescrizione("Microfono non funzionante");

        boolean result = controller.creaSegnalazione(segnalazioneBean);
        assertTrue(result);

        String chiave = "EdificioIngegneria_Aula2_OggettoGuastoImpianto Audio";
        Segnalazione segnalazione = segnalazioneDao.getSegnalazione(chiave);
        assertNotNull(segnalazione);
        assertEquals("giuseppe.rossi@tec.uniroma2.eu", segnalazione.getTecnico().getEmail(), "La segnalazione deve essere assegnata al tecnico con meno carichi (0)");
        assertEquals(1, segnalazione.getTecnico().getNumeroSegnalazioni());
    }

    @Test
    @DisplayName("Test getEdifici restituisce gli edifici disponibili")
    void testGetEdifici() {
        List<String> edifici = controller.getEdifici();
        assertNotNull(edifici);
        assertFalse(edifici.isEmpty());
        assertTrue(edifici.contains("Ingegneria"));
        assertTrue(edifici.contains("Economia"));
    }

    @Test
    @DisplayName("Test getAuleByEdificio restituisce le aule dell'edificio specificato")
    void testGetAuleByEdificio() {
        List<AulaBean> auleIngegneria = controller.getAuleByEdificio("Ingegneria");
        assertNotNull(auleIngegneria);
        assertFalse(auleIngegneria.isEmpty());
        for (AulaBean aula : auleIngegneria) {
            assertEquals("Ingegneria", aula.getEdificio());
            assertNotNull(aula.getIdAula());
        }
    }

    @Test
    @DisplayName("Test getOggettiAula restituisce la lista di oggetti presenti nell'aula")
    void testGetOggettiAula() {
        List<String> oggetti = controller.getOggettiAula("Ingegneria", "1");
        assertNotNull(oggetti);
        assertFalse(oggetti.isEmpty());
        assertTrue(oggetti.contains("Proiettore"));
    }
}
