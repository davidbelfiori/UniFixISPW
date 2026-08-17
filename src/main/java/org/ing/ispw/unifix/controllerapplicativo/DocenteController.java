package org.ing.ispw.unifix.controllerapplicativo;

import org.ing.ispw.unifix.bean.InfoDocenteBean;
import org.ing.ispw.unifix.bean.UserBean;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.model.User;
import org.ing.ispw.unifix.sessionmanager.SessionManager;


public class DocenteController {

    private final UserDao userDao;

    public DocenteController() {
        this.userDao = DaoFactory.getInstance().getUserDao();
    }

    /**
        Reupero informazione del docente
        @return InfoDocenteBean con le sue informazioni
        @throws IllegalStateException se non c'è un docente loggato
        @throws IllegalArgumentException se i dati inseriti nella bean sono errati
    * */

    public InfoDocenteBean getDocenteInformation() {
        // Recupero le informazioni dell'utente loggato dalla sessione e verifico che non sia null
        UserBean loggedUser = SessionManager.getInstance().getCurrentUser();
        if (loggedUser == null) {
            throw new IllegalStateException("Nessun docente loggato");
        }
        User currentUser = userDao.load(loggedUser.getEmail());
        if (currentUser != null) {
            InfoDocenteBean infoDocente = new InfoDocenteBean();
            infoDocente.setEmail(currentUser.getEmail());
            infoDocente.setNome(currentUser.getNome());
            infoDocente.setCognome(currentUser.getCognome());
            return infoDocente;
        }
        throw new IllegalStateException("Nessun docente loggato"); // O lanciare un'eccezione se l'utente non dovrebbe mai essere null qui
    }
}
