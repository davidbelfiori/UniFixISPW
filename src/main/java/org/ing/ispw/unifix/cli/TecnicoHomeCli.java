package org.ing.ispw.unifix.cli;

import org.ing.ispw.unifix.bean.InfoTecnicoBean;
import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.controllerapplicativo.TecnicoController;
import org.ing.ispw.unifix.controllerapplicativo.VisualizzaSegnalazioniTecnicoController;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.utils.Printer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class TecnicoHomeCli {

   private boolean quit;
   private final BufferedReader br;
   private TecnicoController tc;
   private VisualizzaSegnalazioniTecnicoController vstc;
    public TecnicoHomeCli() {
        quit = false;
        br = new BufferedReader(new InputStreamReader(System.in));
        tc = TecnicoController.getInstance();
        vstc = VisualizzaSegnalazioniTecnicoController.getInstance();
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
                    List<Segnalazione> segnalazioniTecnico = null;
                    try {
                        segnalazioniTecnico = vstc.visualizzaSegnalazioniTecnico();
                        for (Segnalazione segnalazione : segnalazioniTecnico) {
                            Printer.print("ID Segnalazione: " + segnalazione.getIdSegnalzione());
                            Printer.print("Data Creazione: " + segnalazione.getDataCreazione());
                            Printer.print("Oggetto Guasto: " + segnalazione.getOggettoGuasto());
                            Printer.print("Docente: " + segnalazione.getDocente().getNome() + " " + segnalazione.getDocente().getCognome());
                            Printer.print("Stato: " + segnalazione.getStato());
                            Printer.print("Descrizione: " + segnalazione.getDescrizone());
                            Printer.print("Aula: " + segnalazione.getAula());
                            Printer.print("Edificio: " + segnalazione.getEdifico());
                        }
                        Printer.print("Inserisci l'ID della segnalazione da gestire o '0' per tornare al menu principale:");
                        String idSegnalazioneInput = br.readLine();
                        if (!idSegnalazioneInput.equals("0")) {
                            try {
                                Segnalazione segnalazione = tc.getSegnalazione(idSegnalazioneInput);
                                Printer.print("ID Segnalazione: " + segnalazione.getIdSegnalzione());
                                Printer.print("Data Creazione: " + segnalazione.getDataCreazione());
                                Printer.print("Oggetto Guasto: " + segnalazione.getOggettoGuasto());
                                Printer.print("Docente: " + segnalazione.getDocente().getNome() + " " + segnalazione.getDocente().getCognome());
                                Printer.print("Stato: " + segnalazione.getStato());
                                Printer.print("Descrizione: " + segnalazione.getDescrizone());
                                Printer.print("Aula: " + segnalazione.getAula());
                                Printer.print("Edificio: " + segnalazione.getEdifico());
                                Printer.print("Vuoi modificare lo stato della segnalazione? (y/n)");
                                String action2 = br.readLine();
                                if (action2.equals("y")) {
                                    Printer.print("Seleziona lo stato della segnalazione:");
                                    Printer.print("\t1) In lavorazione");
                                    Printer.print("\t2) Chiusa");
                                    Printer.print(": ");
                                    String stato = br.readLine();
                                    switch (stato) {
                                        case "1":
                                            tc.updateSegnalazione(new SegnalazioneBean(segnalazione.getIdSegnalzione(), "In lavorazione"));
                                            break;
                                        case "2":
                                            tc.updateSegnalazione(new SegnalazioneBean(segnalazione.getIdSegnalzione(), "Chiusa"));
                                            break;
                                        default:
                                            Printer.print("Stato non valido");
                                            break;
                                    }
                                }
                            } catch (Exception e) {
                                Printer.error(e.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        Printer.error(e.getMessage());
                    }
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

