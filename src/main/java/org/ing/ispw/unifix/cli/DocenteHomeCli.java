package org.ing.ispw.unifix.cli;

import org.ing.ispw.unifix.controllerapplicativo.DocenteController;
import org.ing.ispw.unifix.utils.Printer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DocenteHomeCli {

    Boolean quit;
    BufferedReader br;
    DocenteController dc;
    public DocenteHomeCli() {
        quit = false;
        br = new BufferedReader(new InputStreamReader(System.in));
        dc = DocenteController.getInstance();
    }

    public void docenteHome(){

        Printer.print("ciao");
    }
}
