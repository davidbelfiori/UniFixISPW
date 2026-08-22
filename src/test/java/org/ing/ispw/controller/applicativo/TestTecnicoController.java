package org.ing.ispw.controller.applicativo;

import org.ing.ispw.unifix.bean.UserBean;
import org.ing.ispw.unifix.controllerapplicativo.InviaSegnalazioneController;
import org.ing.ispw.unifix.controllerapplicativo.TecnicoController;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.dao.memory.InMemoryDaoFactory;
import org.ing.ispw.unifix.sessionmanager.SessionManager;
import org.ing.ispw.unifix.utils.DemoData;
import org.ing.ispw.unifix.utils.UserType;
import org.junit.jupiter.api.BeforeEach;

public class TestTecnicoController {


    private InviaSegnalazioneController controller;
    private SegnalazioneDao segnalazioneDao;
    private UserDao userDao;
    private TecnicoController tecnicoController;

    @BeforeEach
    void setUp() {
        InMemoryDaoFactory memoryDaoFactory = new InMemoryDaoFactory();
        DaoFactory.setInstance(memoryDaoFactory);
        DemoData.load();

        userDao = DaoFactory.getInstance().getUserDao();
        segnalazioneDao = DaoFactory.getInstance().getSegnalazioneDao();
        controller = new InviaSegnalazioneController();
        tecnicoController = new TecnicoController();

        // Imposta un utente docente loggato nella sessione
        UserBean docenteBean = new UserBean();
        docenteBean.setEmail("marco.bianchi@uniroma2.eu");
        docenteBean.setRuolo(UserType.DOCENTE);
        SessionManager.getInstance().setCurrentUser(docenteBean);
    }

}
