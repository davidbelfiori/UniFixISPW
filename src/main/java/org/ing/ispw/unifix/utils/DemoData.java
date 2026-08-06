package org.ing.ispw.unifix.utils;

import org.ing.ispw.unifix.controllerapplicativo.GestioneAuleController;
import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.model.*;

public class DemoData {

    private DemoData() {

    }

    public static void load() {
        UserDao userDao = DaoFactory.getInstance().getUserDao();
        String action = "admin";
        // Crea utenti se non esistono
        if (!userDao.exists("marco.rizzo@sys.uniroma2.eu")) {
            User sysadmin = UserFactory.createUser("marco.rizzo@sys.uniroma2.eu", action, "Marco", "Rizzo", UserType.SYSADMIN, 0);
            userDao.store(sysadmin);
            Printer.print("Creato utente demo: marco.rizzo@sys.uniroma2.eu (password: admin)");
        }

        if (!userDao.exists("marco.bianchi@uniroma2.eu")) {
            User docente = UserFactory.createUser("marco.bianchi@uniroma2.eu", action, "Marco", "Bianchi", UserType.DOCENTE, 0);
            userDao.store(docente);
            Printer.print("Creato utente demo: marco.bianchi@uniroma2.eu (password: admin)");
        }

        if (!userDao.exists("giuseppe.rossi@tec.uniroma2.eu")) {
           User tecnico = UserFactory.createUser("giuseppe.rossi@tec.uniroma2.eu", action, "Giuseppe", "Rossi", UserType.TECNICO, 0);
            userDao.store(tecnico);
            Printer.print("Creato utente demo: giuseppe.rossi@tec.uniroma2.eu (password: admin)");
        }

        GestioneAuleController sc = new GestioneAuleController();

        sc.inserisciAuleFromCsv("src/main/resources/utvAule.csv");

    }
}
