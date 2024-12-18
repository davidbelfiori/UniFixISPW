package org.ing.ispw.unifix.controllerapplicativo;

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
}
