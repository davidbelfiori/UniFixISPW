package org.ing.ispw.unifix.controllerapplicativo;


import org.ing.ispw.unifix.bean.InfoTecnicoBean;
import org.ing.ispw.unifix.model.Tecnico;

public class TecnicoController {

    private static TecnicoController instance;

    public static TecnicoController getInstance() {
        if(instance == null) {
            instance = new TecnicoController();
        }
        return instance;
    }

    private TecnicoController() {
    }

    public InfoTecnicoBean getTecnicoInformation(){

        Tecnico currentUser = (Tecnico) LoginController.getInstance().getCurrentUser();
        return new InfoTecnicoBean(currentUser.getNome(), currentUser.getCognome(), currentUser.getEmail(), currentUser.getPassword(),currentUser.getRuolo(), currentUser.getNumeroSegnalazioni());

    }


}
