package org.ing.ispw.unifix.cli;

import org.ing.ispw.unifix.bean.InfoDocenteBean;
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

    private boolean quit;
    private final BufferedReader br;
    private final VisualizzaSegnalazioniDocenteController vsdc;
    private final DocenteController dc;

    public DocenteHomeCli() {
        quit = false;
        br = new BufferedReader(new InputStreamReader(System.in));
        vsdc = new VisualizzaSegnalazioniDocenteController();
        dc = new DocenteController();
    }

    public void docenteHome() throws IOException {

        while (!quit) {
            stampaMenu();
            String action = br.readLine();

            switch (action) {
                case "1":
                    visualizzaSegnalazioni();
                    break;
                case "2":
                    inviaSegnalazione();
                    break;
                case "3":
                    visualizzaInfoProfilo();
                    break;
                case "4": // Log off
                    return;
                case "5": // Quit
                    quit = true;
                    break;
                default:
                    Printer.print("Azione non valida. Riprova.");
                    break;
            }
        }
        System.exit(0);
    }

    private void stampaMenu() {
        Printer.print("\nBentornato in UniFix Docente");
        Printer.print("\t1) Visualizza le tue segnalazioni");
        Printer.print("\t2) Invia Segnalazione");
        Printer.print("\t3) Visualizza informazioni profilo");
        Printer.print("\t4) Log off");
        Printer.print("\t5) Esci");
        Printer.print(": ");
    }

    private void visualizzaSegnalazioni() {
        try {
            List<Segnalazione> segnalazioniDocente = vsdc.visualizzaSegnalazioniDocente();
            if (segnalazioniDocente.isEmpty()){
                Printer.print("Nessuna segnalazione trovata.");
                return;
            }
            for (Segnalazione segnalazione : segnalazioniDocente) {
                Printer.print(segnalazione.toString());
            }
        } catch (NessunSegnalazioneDocenteException | NessunaSegnalazioneException e) {
            Printer.error(e.getMessage());
        }
    }

    private void inviaSegnalazione() throws IOException {
        SegnalazioneCli sc = new SegnalazioneCli();
        sc.segnalazioneView();
    }

    private void visualizzaInfoProfilo() {
        InfoDocenteBean info = dc.getDocenteInformation();
        if (info != null) {
            Printer.print("\n--- I tuoi dati ---");
            Printer.print("Nome: " + info.getNome());
            Printer.print("Cognome: " + info.getCognome());
            Printer.print("Email: " + info.getEmail());
            Printer.print("--------------------");
        } else {
            Printer.error("Impossibile recuperare le informazioni dell'utente.");
        }
    }
}
