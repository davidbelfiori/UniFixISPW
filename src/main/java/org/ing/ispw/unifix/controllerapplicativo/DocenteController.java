package org.ing.ispw.unifix.controllerapplicativo;

import org.ing.ispw.unifix.bean.InfoDocenteBean;
import org.ing.ispw.unifix.model.User;


public class DocenteController {


    public InfoDocenteBean getDocenteInformation() {
        User currentUser = LoginController.getInstance().getCurrentUser();
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
