package org.uniroma2.ing.ispw.unifix.controllerApplicativo;

public class docenteController {

    private static docenteController instance;

    public static docenteController getInstance() {
        if(instance == null) {
            instance = new docenteController();
        }
        return instance;
    }

    public docenteController() {
    }
}
