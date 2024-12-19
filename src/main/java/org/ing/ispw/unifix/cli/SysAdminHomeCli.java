package org.ing.ispw.unifix.cli;

import org.ing.ispw.unifix.controllerapplicativo.SysAdminController;
import org.ing.ispw.unifix.controllerapplicativo.TecnicoController;
import org.ing.ispw.unifix.utils.Printer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SysAdminHomeCli {

    private Boolean quit;
    private final BufferedReader br;
    private SysAdminController sc;
    public SysAdminHomeCli() {
        quit = false;
        br = new BufferedReader(new InputStreamReader(System.in));
        sc = SysAdminController.getInstance();
    }

    public void adminHome() throws IOException {
        while(!quit) {

            Printer.print("Bentornato in unifix admin di sistema");
            Printer.print("\t1) Inserisci aule");
            Printer.print("\t2) Log off");
            Printer.print("\t3) Quit");
            Printer.print(": ");

            String action = br.readLine();

            switch(action) {
                case "1":
                    sc.inserisciAule("src/main/resources/utvAule.csv");
                    return;
                case "3":
                    quit=true;
                    break;
            }
        }


    }
}
