package org.ing.ispw.unifix.cli;

import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.controllerapplicativo.InviaSegnalazioneController;
import org.ing.ispw.unifix.model.Aula;
import org.ing.ispw.unifix.utils.Printer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SegnalazioneCli {

    private final BufferedReader br;
    private final InviaSegnalazioneController sc;

    // Stato del form
    private String edificioSelezionato = "";
    private String aulaSelezionata = "";
    private String oggettoSelezionato = "";
    private String descrizioneGuasto = "";
    private List<String> edificiUniciCache = new ArrayList<>();
    private List<String> oggettiAulaCache = new ArrayList<>();

    public SegnalazioneCli() {
        br = new BufferedReader(new InputStreamReader(System.in));
        sc = InviaSegnalazioneController.getInstance();
    }

    public void segnalazioneView() throws IOException {
        boolean quit = false;
        while (!quit) {
            stampaMenu();
            String action = br.readLine();

            switch (action) {
                case null:
                    Printer.print("Input non valido.");
                    break;
                case "1":
                    visualizzaEdifici();
                    break;
                case "2":
                    selezionaEdificio();
                    break;
                case "3":
                    visualizzaAule();
                    break;
                case "4":
                    selezionaAula();
                    break;
                case "5":
                    visualizzaOggetti();
                    break;
                case "6":
                    selezionaOggetto();
                    break;
                case "7":
                    inserisciDescrizione();
                    break;
                case "8":
                    inviaSegnalazione();
                    break;
                case "9":
                    quit = true;
                    break;
                default:
                    Printer.print("Azione non valida. Riprova.");
            }
        }
    }

    private void stampaMenu() {
        Printer.print("\n******* Form per l'invio di una segnalazione *******");
        Printer.print("\t1) Visualizza gli edifici");
        Printer.print("\t2) Seleziona Edificio -> [" + edificioSelezionato + "]");
        Printer.print("\t3) Visualizza aule di [" + edificioSelezionato + "]");
        Printer.print("\t4) Seleziona Aula -> [" + aulaSelezionata + "]");
        Printer.print("\t5) Visualizza oggetti nell'aula [" + aulaSelezionata + "]");
        Printer.print("\t6) Seleziona Oggetto -> [" + oggettoSelezionato + "]");
        Printer.print("\t7) Inserisci una descrizione del problema");
        Printer.print("\t8) Invia Segnalazione");
        Printer.print("\t9) Torna al menu precedente");
        Printer.print(": ");
    }

    private void visualizzaEdifici() {
        // Crea una nuova ArrayList modificabile a partire dalla lista restituita dal controller
        edificiUniciCache = new ArrayList<>(sc.getEdifici());
        if (edificiUniciCache.isEmpty()) {
            Printer.print("Nessun edificio trovato.");
        } else {
            edificiUniciCache.forEach(edificio -> Printer.print("Edificio: " + edificio));
        }
    }

    private void selezionaEdificio() throws IOException {
        Printer.print("Inserisci il nome dell'edificio:");
        String input = br.readLine();
        if (edificiUniciCache.contains(input)) {
            edificioSelezionato = input;
            // Reset delle selezioni dipendenti
            aulaSelezionata = "";
            oggettoSelezionato = "";
        } else {
            Printer.error("Edificio non valido. Visualizza prima gli edifici (opzione 1) e poi selezionalo.");
        }
    }

    private void visualizzaAule() {
        if (edificioSelezionato.isEmpty()) {
            Printer.error("Seleziona prima un edificio.");
            return;
        }
        List<Aula> aule = sc.getAuleByEdificio(edificioSelezionato);
        if (aule.isEmpty()) {
            Printer.print("Nessuna aula trovata per l'edificio " + edificioSelezionato);
        } else {
            aule.forEach(a -> Printer.print("Aula: " + a.getIdAula() + " (Piano: " + a.getPiano() + ")"));
        }
    }

    private void selezionaAula() throws IOException {
        Printer.print("Inserisci l'ID dell'aula:");
        aulaSelezionata = br.readLine();
        // Potrebbe essere utile una validazione qui per assicurarsi che l'aula esista
        oggettoSelezionato = ""; // Reset selezione oggetto
    }

    private void visualizzaOggetti() {
        if (aulaSelezionata.isEmpty()) {
            Printer.error("Seleziona prima un'aula.");
            return;
        }
        oggettiAulaCache= sc.getOggettiAula(aulaSelezionata);
        oggettiAulaCache.forEach(Printer::print);
    }

    private void selezionaOggetto() throws IOException {
        Printer.print("Inserisci l'oggetto:");
        String input = br.readLine();
        if (oggettiAulaCache.contains(input)) {
            oggettoSelezionato = input;
        } else {
            Printer.error("Oggetto non valido. Visualizza prima gli oggetti (opzione 5) e poi selezionalo.");
        }
    }

    private void inserisciDescrizione() throws IOException {
        Printer.print("Descrivi il problema:");
        descrizioneGuasto = br.readLine();
    }

    private void inviaSegnalazione() {
        if (edificioSelezionato.isEmpty() || aulaSelezionata.isEmpty() || oggettoSelezionato.isEmpty() || descrizioneGuasto.isEmpty()) {
            Printer.error("Tutti i campi sono obbligatori prima di inviare la segnalazione.");
            return;
        }

        Printer.print("\n***** Riepilogo della tua segnalazione *****");
        Printer.print("Edificio: " + edificioSelezionato);
        Printer.print("Aula: " + aulaSelezionata);
        Printer.print("Oggetto Guasto: " + oggettoSelezionato);
        Printer.print("Descrizione: " + descrizioneGuasto);
        Printer.print("******************************************");

        SegnalazioneBean bean = new SegnalazioneBean(System.currentTimeMillis(), aulaSelezionata, edificioSelezionato, oggettoSelezionato, descrizioneGuasto);
        if (sc.creaSegnalazione(bean)) {
            Printer.print("Segnalazione inviata con successo!");
            resetForm();
        } else {
            Printer.error("Errore: la segnalazione potrebbe essere duplicata o si è verificato un problema.");
        }
    }

    private void resetForm() {
        edificioSelezionato = "";
        aulaSelezionata = "";
        oggettoSelezionato = "";
        descrizioneGuasto = "";
        edificiUniciCache.clear();
        oggettiAulaCache.clear();
    }
}
