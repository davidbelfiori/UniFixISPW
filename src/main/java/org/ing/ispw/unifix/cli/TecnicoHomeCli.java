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
    private final TecnicoController tc;
    private final VisualizzaSegnalazioniTecnicoController vstc;

    public TecnicoHomeCli() {
        quit = false;
        br = new BufferedReader(new InputStreamReader(System.in));
        tc = TecnicoController.getInstance();
        vstc = VisualizzaSegnalazioniTecnicoController.getInstance();
    }

    public void tecnicoHome() throws IOException {
        while (!quit) {
            stampaMenuPrincipale();
            String action = br.readLine();

            switch (action) {
                case "1":
                    gestisciSegnalazioni();
                    break;
                case "2":
                    visualizzaInfoProfilo();
                    break;
                case "3": // Log off
                    return;
                case "4": // Quit
                    quit = true;
                    break;
                default:
                    Printer.print("Azione non valida. Riprova.");
            }
        }
        System.exit(0);
    }

    private void stampaMenuPrincipale() {
        Printer.print("\nBentornato in UniFix Tecnico");
        Printer.print("\t1) Visualizza le tue segnalazioni");
        Printer.print("\t2) Visualizza informazioni profilo");
        Printer.print("\t3) Log off");
        Printer.print("\t4) Esci");
        Printer.print(": ");
    }

    private void gestisciSegnalazioni() throws IOException {
        try {
            List<Segnalazione> segnalazioni = vstc.visualizzaSegnalazioniTecnico();
            if (segnalazioni.isEmpty()) {
                Printer.print("Nessuna segnalazione da visualizzare.");
                return;
            }

            segnalazioni.forEach(this::stampaDettagliSegnalazione);

            Printer.print("\nInserisci l'ID della segnalazione da gestire o '0' per tornare al menu principale:");
            String idSegnalazioneInput = br.readLine();

            if (!"0".equals(idSegnalazioneInput)) {
                selezionaEProcessaSegnalazione(idSegnalazioneInput);
            }
        } catch (Exception e) {
            Printer.error(e.getMessage());
        }
    }

    private void selezionaEProcessaSegnalazione(String idSegnalazioneInput) throws IOException {
        try {
            Segnalazione segnalazione = tc.getSegnalazione(idSegnalazioneInput);
            stampaDettagliSegnalazione(segnalazione);

            Printer.print("Vuoi modificare lo stato della segnalazione? (y/n)");
            String action = br.readLine();

            if ("y".equalsIgnoreCase(action)) {
                aggiornaStatoSegnalazione(segnalazione);
            }
        } catch (Exception e) {
            Printer.error(e.getMessage());
        }
    }

    private void aggiornaStatoSegnalazione(Segnalazione segnalazione) throws IOException {
        Printer.print("Seleziona il nuovo stato:");
        Printer.print("\t1) In lavorazione");
        Printer.print("\t2) Chiusa");
        Printer.print(": ");
        String statoInput = br.readLine();
        String nuovoStato;

        switch (statoInput) {
            case null:
                Printer.print("Input non valido.");
                return;
            case "1":
                nuovoStato = "In lavorazione";
                break;
            case "2":
                nuovoStato = "Chiusa";
                break;
            default:
                Printer.print("Stato non valido.");
                return;
        }
        tc.updateSegnalazione(new SegnalazioneBean(segnalazione.getIdSegnalzione(), nuovoStato));
        Printer.print("Stato della segnalazione aggiornato con successo.");
    }

    private void stampaDettagliSegnalazione(Segnalazione segnalazione) {
        Printer.print("---------------------------------");
        Printer.print("ID Segnalazione: " + segnalazione.getIdSegnalzione());
        Printer.print("Data Creazione: " + segnalazione.getDataCreazione());
        Printer.print("Oggetto Guasto: " + segnalazione.getOggettoGuasto());
        Printer.print("Docente: " + segnalazione.getDocente().getNome() + " " + segnalazione.getDocente().getCognome());
        Printer.print("Stato: " + segnalazione.getStato());
        Printer.print("Descrizione: " + segnalazione.getDescrizone());
        Printer.print("Aula: " + segnalazione.getAula());
        Printer.print("Edificio: " + segnalazione.getEdifico());
        Printer.print("---------------------------------");
    }

    private void visualizzaInfoProfilo() {
        InfoTecnicoBean info = tc.getTecnicoInformation();
        Printer.print("\n--- I tuoi dati ---");
        Printer.print("Nome: " + info.getNome());
        Printer.print("Cognome: " + info.getCognome());
        Printer.print("Email: " + info.getEmail());
        Printer.print("Password: " + info.getPassword());
        Printer.print("Numero di segnalazioni assegnate: " + info.getNumeroSegnalazioni());
        Printer.print("--------------------");
    }
}
