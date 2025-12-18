package org.ing.ispw.unifix.cli;

import org.ing.ispw.unifix.controllerapplicativo.SysAdminController;

import org.ing.ispw.unifix.exception.AuleNonTrovateException;
import org.ing.ispw.unifix.utils.Printer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class SysAdminHomeCli {

    private Boolean quit;
    private final BufferedReader br;
    private SysAdminController sc;
    public SysAdminHomeCli() {
        quit = FALSE;
        br = new BufferedReader(new InputStreamReader(System.in));
        sc = new SysAdminController();
    }

    public void adminHome() throws IOException {
        while(Boolean.FALSE.equals(quit)) {

            Printer.print("Bentornato in unifix admin di sistema");
            Printer.print("\t1) Inserisci aule");
            Printer.print("\t2) Visualizza aule inserite");
            Printer.print("\t3) Log off");
            Printer.print("\t4) Quit");
            Printer.print(": ");

            String action = br.readLine();

            switch(action) {
                case "1":
                    sc.inserisciAule("src/main/resources/utvAule.csv");
                    break;
                case "2":
                    try {
                        sc.visualizzaAule();
                    } catch (AuleNonTrovateException e) {
                        Printer.error("Errore"+e.getMessage());
                    }
                    break;
                case "3":
                    return;
                case "4":
                    quit=TRUE;
                    break;
                default:
                    return;
            }
        }
        System.exit(0);


    }
}
