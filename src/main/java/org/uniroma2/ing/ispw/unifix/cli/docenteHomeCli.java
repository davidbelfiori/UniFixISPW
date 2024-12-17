package org.uniroma2.ing.ispw.unifix.cli;

import org.uniroma2.ing.ispw.unifix.controllerApplicativo.docenteController;
import org.uniroma2.ing.ispw.unifix.utils.Printer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class docenteHomeCli {

    Boolean quit;
    BufferedReader br;
    docenteController dc;
    public docenteHomeCli() {
        quit = false;
        br = new BufferedReader(new InputStreamReader(System.in));
        dc = docenteController.getInstance();
    }

    public void docenteHome(){

        Printer.print("ciao");
    }
}
