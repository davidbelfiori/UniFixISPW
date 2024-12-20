package org.ing.ispw.unifix.cli;

import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.controllerapplicativo.InviaSegnalazioneController;
import org.ing.ispw.unifix.model.Aula;
import org.ing.ispw.unifix.utils.Printer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class SegnalazioneCli {

    boolean quit;
    BufferedReader br;
    InviaSegnalazioneController sc;

    public SegnalazioneCli() {
        quit = false;
        br = new BufferedReader(new InputStreamReader(System.in));
        sc= InviaSegnalazioneController.getInstance();

    }

    public void segnalazioneView() throws IOException {
        SegnalazioneBean segnalazioneBean = new SegnalazioneBean(0, null, null, null, null, null, null);
        String edificio="";
        String aula="";
        String oggetto="";
        String descrizione="";
        while (!quit) {

            Printer.print("*******Form per l'invio di una segnalazione******");
            Printer.print("\t1) Visualizza gli edifici");
            Printer.print("\t2) Edificio Selezionato ["+edificio+"]");
            Printer.print("\t3) Visualizza aule di ["+edificio+"]");
            Printer.print("\t4) Aula selezionata ["+aula+"]");
            Printer.print("\t5) Visualizza oggetti presenti in ["+aula+"]");
            Printer.print("\t6) Oggetto Selezionato ["+aula+"]");
            Printer.print("\t7)Inserisci una descrizione del problema (obbligatorio)");
            Printer.print("\t8) Invia Segnalazione");
            Printer.print("\t9)Back");
            Printer.print(": ");

            String action = br.readLine();

            switch (action) {
                case "1":
                    List<String> edificiUnici = sc.getEdifici();
                    for (String edifico : edificiUnici){
                        Printer.print("Edificio: "+edifico);
                    }
                    break;
                case "2":

            }

        }
    }

}
