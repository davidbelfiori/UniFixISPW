package org.ing.ispw.unifix.cli;

import org.ing.ispw.unifix.controllerapplicativo.DocenteController;
import org.ing.ispw.unifix.controllerapplicativo.VisualizzaSegnalazioniDocenteController;
import org.ing.ispw.unifix.exception.NessunSegnalazioneDocenteException;
import org.ing.ispw.unifix.exception.NessunaSegnalazioneException;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.utils.Printer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class DocenteHomeCli {

    Boolean quit;
    BufferedReader br;
    DocenteController dc;
    VisualizzaSegnalazioniDocenteController vsdc;
    public DocenteHomeCli() {
        quit = false;
        br = new BufferedReader(new InputStreamReader(System.in));
        dc = DocenteController.getInstance();
        vsdc = VisualizzaSegnalazioniDocenteController.getInstance();
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
                    List<Segnalazione> segnalazioniDocente = null;
                    try {
                       segnalazioniDocente= vsdc.visualizzaSegnalazioniDocente();
                    } catch (NessunSegnalazioneDocenteException | NessunaSegnalazioneException e) {
                        Printer.error(e.getMessage());
                    }
                    for (Segnalazione segnalazione : segnalazioniDocente) {
                        Printer.print(segnalazione.toString());
                    }
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
