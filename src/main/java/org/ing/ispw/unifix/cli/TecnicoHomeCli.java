package org.ing.ispw.unifix.cli;

import org.ing.ispw.unifix.controllerapplicativo.TecnicoController;
import org.ing.ispw.unifix.utils.Printer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TecnicoHomeCli {

   private Boolean quit;
   private final BufferedReader br;
   private TecnicoController tc;
    public TecnicoHomeCli() {
        quit = false;
        br = new BufferedReader(new InputStreamReader(System.in));
        tc = TecnicoController.getInstance();
    }

    public void tecnicoHome() throws IOException {
        while(!quit) {

            Printer.print("Bentornato in unifix tecnico");
            Printer.print("\t1) Visualizza le tue segnalazioni");
            Printer.print("\t2) Invia nuova segnalazione");
            Printer.print("\t3) View Profile Info");
            Printer.print("\t4) Log off");
            Printer.print("\t5) Quit");
            Printer.print(": ");

            String action = br.readLine();

            switch(action) {
                case "5":
                    quit=true;
                    break;

                default: return;
            }
        }


    }

}

