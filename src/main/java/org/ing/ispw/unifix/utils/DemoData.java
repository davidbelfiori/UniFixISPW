package org.ing.ispw.unifix.utils;

import org.ing.ispw.unifix.controllerapplicativo.GestioneAuleController;
import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.model.Docente;
import org.ing.ispw.unifix.model.Sysadmin;
import org.ing.ispw.unifix.model.Tecnico;

public class DemoData {

    private DemoData() {
        throw new IllegalStateException("Utility class");
    }

    public static void load() {
        UserDao userDao = DaoFactory.getInstance().getUserDao();
        String action = "admin";
        // Crea utenti se non esistono
        if (!userDao.exists("marco.rizzo@sys.uniroma2.eu")) {
            Sysadmin sysadmin = new Sysadmin("marco.rizzo@sys.uniroma2.eu", action, "Marco", "Rizzo", UserType.SYSADMIN);
            userDao.store(sysadmin);
            Printer.print("Creato utente demo: marco.rizzo@sys.uniroma2.eu (password: admin)");
        }

        if (!userDao.exists("davide.falessi@uniroma2.eu")) {
            Docente docente = new Docente("davide.falessi@uniroma2.eu", action, "Davide", "Falessi", UserType.DOCENTE);
            userDao.store(docente);
            Printer.print("Creato utente demo: davide.falessi@uniroma2.eu (password: admin)");
        }

        if (!userDao.exists("giuseppe.rossi@tec.uniroma2.eu")) {
            Tecnico tec = new Tecnico("giuseppe.rossi@tec.uniroma2.eu", action, "Giuseppe", "Rossi", UserType.TECNICO, 0);
            userDao.store(tec);
            Printer.print("Creato utente demo: giuseppe.rossi@tec.uniroma2.eu (password: admin)");
        }

        GestioneAuleController sc = new GestioneAuleController();

        sc.inserisciAuleFromCsv("src/main/resources/utvAule.csv");

    }
}
