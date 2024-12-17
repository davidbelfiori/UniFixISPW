package org.ing.ispw.unifix.controllerApplicativo;

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

}
