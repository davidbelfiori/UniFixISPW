package org.uniroma2.ing.ispw.unifix.controllerApplicativo;

public class tecnicoController {

    private static tecnicoController instance;

    public static tecnicoController getInstance() {
        if(instance == null) {
            instance = new tecnicoController();
        }
        return instance;
    }

    public tecnicoController() {
    }

}
