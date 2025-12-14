package org.ing.ispw.unifix.controllerapplicativo;

import org.ing.ispw.unifix.bean.InfoDocenteBean;
import org.ing.ispw.unifix.model.User;


public class DocenteController {

    private static DocenteController instance;

    public static DocenteController getInstance() {
        if(instance == null) {
            instance = new DocenteController();
        }
        return instance;
    }

    private DocenteController() {
    }

    public InfoDocenteBean getDocenteInformation() {
        User currentUser = LoginController.getInstance().getCurrentUser();
        if (currentUser != null) {
            return new InfoDocenteBean(currentUser.getNome(), currentUser.getCognome(), currentUser.getEmail());
        }
        return null; // O lanciare un'eccezione se l'utente non dovrebbe mai essere null qui
    }
}
