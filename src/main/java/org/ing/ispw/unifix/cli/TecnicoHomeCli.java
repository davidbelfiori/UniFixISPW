package org.ing.ispw.unifix.cli;

import org.ing.ispw.unifix.bean.InfoTecnicoBean;
import org.ing.ispw.unifix.controllerapplicativo.TecnicoController;
import org.ing.ispw.unifix.model.Tecnico;
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
            Printer.print("\t2) View Profile Info");
            Printer.print("\t3) Log off");
            Printer.print("\t4) Quit");
            Printer.print(": ");

            String action = br.readLine();

            switch(action) {
                case "1":
                    break;
                case "2":
                    InfoTecnicoBean info=tc.getTecnicoInformation();
                    Printer.print("Ecco i tuoi dati");
                    Printer.print("Nome:"+info.getNome());
                    Printer.print("Cognome:"+info.getCognome());
                    Printer.print("Email:"+info.getEmail());
                    Printer.print("Password:"+info.getPassword());
                    Printer.print("Numero di segnalazioni assegnate:"+info.getNumeroSegnalazioni());
                    break;
                case "3":
                    return;
                case "4":
                    quit=true;
                    break;

                default: return;
            }
        }
        System.exit(0);


    }

}

