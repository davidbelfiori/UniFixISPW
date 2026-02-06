package org.ing.ispw.unifix.cli;

import org.ing.ispw.unifix.bean.InfoTecnicoBean;
import org.ing.ispw.unifix.bean.NotaSegnalazioneBean;
import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.controllerapplicativo.InserisciNotaSegnalazioneController;
import org.ing.ispw.unifix.controllerapplicativo.TecnicoController;
import org.ing.ispw.unifix.controllerapplicativo.VisualizzaSegnalazioniTecnicoController;
import org.ing.ispw.unifix.exception.NessunaSegnalazioneException;
import org.ing.ispw.unifix.exception.NessunaSegnalazioneTecnicoException;
import org.ing.ispw.unifix.utils.Printer;
import org.ing.ispw.unifix.utils.StatoSegnalazione;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.List;

public class TecnicoHomeCli {

    private boolean quit;
    private final BufferedReader br;
    private final TecnicoController tc;
    private final VisualizzaSegnalazioniTecnicoController vstc;
    private  final InserisciNotaSegnalazioneController insc;

    public TecnicoHomeCli() {
        quit = false;
        br = new BufferedReader(new InputStreamReader(System.in));
        tc = TecnicoController.getInstance();
        vstc = new VisualizzaSegnalazioniTecnicoController();
        insc = new InserisciNotaSegnalazioneController();
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
            List<SegnalazioneBean> segnalazioni = vstc.visualizzaSegnalazioniTecnico();
            for (SegnalazioneBean segnalazioneBean: segnalazioni){
                 stampaDettagliSegnalazione(segnalazioneBean);
            }
        } catch (NessunaSegnalazioneException | NessunaSegnalazioneTecnicoException ex) {
            Printer.print(ex.getMessage());
            return;
        }


            Printer.print("\nInserisci l'ID della segnalazione da gestire o '0' per tornare al menu principale:");
            String idSegnalazioneInput = br.readLine();

            if (!"0".equals(idSegnalazioneInput)) {
                selezionaEProcessaSegnalazione(idSegnalazioneInput);
            }

    }

    private void selezionaEProcessaSegnalazione(String idSegnalazioneInput) throws IOException {
        try {
            SegnalazioneBean segnalazione = tc.getSegnalazione(idSegnalazioneInput);
            stampaDettagliSegnalazione(segnalazione);

            Printer.print("\t1)Modifica lo stato della segnalazione");
            Printer.print("\t2)Aggiungi una nota (La segnalazione deve essere IN LAVORAZIONE)");
            String action = br.readLine();
            switch (action){
                case "1": aggiornaStatoSegnalazione(segnalazione); break;
                case "2": aggiungiNotaSegnalazione(segnalazione); break;
                default: Printer.print("Azione non valida.");
            }

        } catch (Exception e) {
            Printer.error(e.getMessage());
        }
    }

    private void aggiungiNotaSegnalazione(SegnalazioneBean segnalazione) {
        String action = "";
        while (!"0".equals(action)) {
            Printer.print("\n--- Gestione Note Segnalazione ---");
            Printer.print("\t1) Visualizza note esistenti");
            Printer.print("\t2) Aggiungi nuova nota");
            Printer.print("\t0) Torna al menù precedente");
            Printer.print(": ");
            try {
                action = br.readLine();
                if (action == null) {
                    action = "0";
                }

                switch (action) {
                    case "1":
                        stampaNoteSegnalazione(segnalazione);
                        break;
                    case "2":
                        aggiungiNuovaNota(segnalazione);
                        break;
                    case "0":
                        break;
                    default:
                        Printer.print("Azione non valida. Riprova.");
                        break;
                }
            } catch (IOException e) {
                Printer.error("Errore di lettura: " + e.getMessage());
            }
        }
    }
            private void aggiornaStatoSegnalazione (SegnalazioneBean segnalazione) throws IOException {
                Printer.print("Seleziona il nuovo stato:");
                Printer.print("\t1) In lavorazione");
                Printer.print("\t2) Chiusa");
                Printer.print(": ");
                String statoInput = br.readLine();

                switch (statoInput) {
                    case null:
                        Printer.print("Input non valido.");
                        return;
                    case "1":
                        tc.inLavorazioneSegnalazione(segnalazione.getIdSegnalazione());
                        break;
                    case "2":
                        tc.chiudiSegnalazione(segnalazione.getIdSegnalazione());
                        break;
                    default:
                        Printer.print("Stato non valido.");
                        return;
                }
                Printer.print("Stato della segnalazione aggiornato con successo.");
            }

            private void stampaDettagliSegnalazione (SegnalazioneBean segnalazione){
                Printer.print("---------------------------------");
                Printer.print("ID Segnalazione: " + segnalazione.getIdSegnalazione());
                Printer.print("Data Creazione: " + segnalazione.getDataCreazione());
                Printer.print("Oggetto Guasto: " + segnalazione.getOggettoGuasto());
                Printer.print("Docente: " + segnalazione.getUser().getNome() + " " + segnalazione.getUser().getCognome());
                Printer.print("Stato: " + segnalazione.getStato());
                Printer.print("Descrizione: " + segnalazione.getDescrizione());
                Printer.print("Aula: " + segnalazione.getAula());
                Printer.print("Edificio: " + segnalazione.getEdificio());
                Printer.print("---------------------------------");
            }

            private void visualizzaInfoProfilo () {
                InfoTecnicoBean info = tc.getTecnicoInformation();
                Printer.print("\n--- I tuoi dati ---");
                Printer.print("Nome: " + info.getNome());
                Printer.print("Cognome: " + info.getCognome());
                Printer.print("Email: " + info.getEmail());
                Printer.print("Password: " + info.getPassword());
                Printer.print("Numero di segnalazioni assegnate: " + info.getNumeroSegnalazioni());
                Printer.print("--------------------");
            }

            private void stampaNoteSegnalazione(SegnalazioneBean segnalazione) {
                List<NotaSegnalazioneBean> note = insc.getNoteForSegnalazione(segnalazione.getIdSegnalazione());
                for (NotaSegnalazioneBean nota : note) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    String dataFormattata = dateFormat.format(nota.getDataCreazione().getTime());
                    Printer.print("--------------------------------");
                    Printer.print(dataFormattata + ": " + nota.getTestoNota());
                }
                Printer.print("--------------------------------");
            }

            private void aggiungiNuovaNota(SegnalazioneBean segnalazione) {
                // Verifica preliminare dello stato della segnalazione
                StatoSegnalazione stato = segnalazione.getStato();

                if (stato != StatoSegnalazione.IN_LAVORAZIONE) {
                    Printer.print("\n╔═══════════════════════════════════════════════════════════╗");
                    Printer.print("║    OPERAZIONE NON CONSENTITA                               ║");
                    Printer.print("╠═══════════════════════════════════════════════════════════╣");
                    Printer.print("║                                                            ║");
                    if (stato == StatoSegnalazione.CHIUSA) {
                        Printer.print("║  La segnalazione è CHIUSA.                                ║");
                        Printer.print("║  Non è possibile aggiungere note a segnalazioni chiuse.  ║");
                    } else {
                        Printer.print("║  La segnalazione è APERTA.                                ║");
                        Printer.print("║  Imposta prima lo stato su 'IN LAVORAZIONE'              ║");
                        Printer.print("║  per poter aggiungere note.                              ║");
                    }
                    Printer.print("║                                                           ║");
                    Printer.print("╚═══════════════════════════════════════════════════════════╝\n");
                    return;
                }

                Printer.print("\n╔════════════════════════════════════════════════════════════╗");
                Printer.print("║             AGGIUNGI NUOVA NOTA                            ║");
                Printer.print("╠════════════════════════════════════════════════════════════╣");
                Printer.print("║  Segnalazione #" + segnalazione.getIdSegnalazione());
                Printer.print("║  Stato: " + segnalazione.getStato());
                Printer.print("╚════════════════════════════════════════════════════════════╝");

                try {
                    Printer.print("\nInserisci il testo della nota (premi INVIO per confermare):");
                    Printer.print(": ");
                    String testoNota = br.readLine();

                    if (testoNota == null || testoNota.trim().isEmpty()) {
                        Printer.print("\n⚠️  Il testo della nota non può essere vuoto. Operazione annullata.");
                        return;
                    }

                    NotaSegnalazioneBean nuovaNota = new NotaSegnalazioneBean(segnalazione.getIdSegnalazione(), testoNota.trim());
                    insc.inserisciNotaSegnalazione(nuovaNota);

                    Printer.print("\n╔════════════════════════════════════════════════════════════╗");
                    Printer.print("║     NOTA AGGIUNTA CON SUCCESSO!                             ║");
                    Printer.print("╚════════════════════════════════════════════════════════════╝\n");

                } catch (IOException e) {
                    Printer.error("Errore durante l'inserimento della nota: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    Printer.error("Dati non validi: " + e.getMessage());
                } catch (Exception e) {
                    Printer.error("Errore imprevisto: " + e.getMessage());
                }
            }

        }