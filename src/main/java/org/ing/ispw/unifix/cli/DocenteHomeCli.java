package org.ing.ispw.unifix.cli;

import org.ing.ispw.unifix.controllerapplicativo.DocenteController;
import org.ing.ispw.unifix.utils.Printer;

import java.io.BufferedReader;
import java.io.IOException;
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

    public void docenteHome() throws IOException {

        while(!quit) {

            Printer.print("Bentornato in unifix Docente");
            Printer.print("\t1) Visualizza le tue segnalazioni");
            Printer.print("\t2 Invia Segnalazione");
            Printer.print("\t3) View Profile Info");
            Printer.print("\t4) Log off");
            Printer.print("\t5) Quit");
            Printer.print(": ");

            String action = br.readLine();

            switch (action){
                case "1":
                    Printer.print("dovresti visualizzare le tue segnalazioni");
                    break;
                case "2":
                    SegnalazioneCli sc= new SegnalazioneCli();
                    sc.segnalazioneView();
                    break;
                case "4":
                    return;
                case "5":
                    quit=true;
                    break;
                default:
                    break;
            }

        }
        System.exit(0);
    }
}
