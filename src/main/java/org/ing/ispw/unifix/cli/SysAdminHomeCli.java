package org.ing.ispw.unifix.cli;

import org.ing.ispw.unifix.bean.AulaBean;
import org.ing.ispw.unifix.controllerapplicativo.GestioneAuleController;

import org.ing.ispw.unifix.exception.AulaGiaPresenteException;
import org.ing.ispw.unifix.exception.AuleNonTrovateException;
import org.ing.ispw.unifix.exception.CsvInvalidException;
import org.ing.ispw.unifix.utils.Printer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class SysAdminHomeCli {

    private Boolean quit;
    private final BufferedReader br;
    private final GestioneAuleController sc;
    public SysAdminHomeCli() {
        quit = FALSE;
        br = new BufferedReader(new InputStreamReader(System.in));
        sc = new GestioneAuleController();
    }

    public void adminHome() throws IOException {
        while(Boolean.FALSE.equals(quit)) {

            Printer.print("Bentornato in unifix admin di sistema");
            Printer.print("\t1) Inserisci aule da file CSV");
            Printer.print("\t2) Inserisci aula singola");
            Printer.print("\t3) Visualizza aule inserite");
            Printer.print("\t4) Visualizza Statistiche");
            Printer.print("\t5) Visualizza Segnalazioni");
            Printer.print("\t6) Torna al menu principale");
            Printer.print("\t7) Log off");
            Printer.print("\t8) Quit");
            Printer.print(": ");

            String action = br.readLine();

            switch(action) {
                case "1":
                    try {
                        sc.inserisciAuleFromCsv("src/main/resources/utvAule.csv");
                    } catch (AulaGiaPresenteException | CsvInvalidException e) {
                        Printer.error("Errore durante l'inserimento delle aule: " + e.getMessage());
                    }

                    break;
                case "2":
                    inserisciAulaSingola();
                    break;
                case "3":
                    try {
                        List<AulaBean> aule = sc.visualizzaAule();
                        for (AulaBean aula : aule) {
                            Printer.print("Edificio: " + aula.getEdificio());
                            Printer.print("ID Aula: " + aula.getIdAula());
                            Printer.print("Piano: " + aula.getPiano());
                            Printer.print("Oggetti: " + String.join(", ", aula.getOggetti()));
                            Printer.print("-------------------------");
                        }
                    } catch (AuleNonTrovateException | IllegalArgumentException e) {
                        Printer.error("Errore"+e.getMessage());
                    }
                    break;
                    //CASE LogOut
                case "7":
                    return;
                    //CASE EXIT;
                case "8":
                    quit=TRUE;
                    break;
                default:
                    Printer.error("Si prega di inserire un numero valido\n");
                    return;
            }
        }
        System.exit(0);


    }

    private void inserisciAulaSingola() throws IOException {
        Printer.print("\n--- Inserimento Nuova Aula ---");

        Printer.print("Inserisci ID Aula (es. A1): ");
        String idAula = readLineSafe(br);

        Printer.print("Inserisci Edificio: ");
        String edificio = readLineSafe(br);

        Printer.print("Inserisci Piano (numero): ");
        int piano;
        try {
            piano = Integer.parseInt(readLineSafe(br));
        } catch (NumberFormatException _) {
            Printer.error("Piano non valido, impostato a 0");
            piano = 0;
        }

        List<String> oggetti = new ArrayList<>();
        Printer.print("Inserisci oggetti (invio vuoto per terminare):");

        String riga;
        while ((riga = br.readLine()) != null) {
            String oggetto = riga.trim();

            // Se l'utente ha premuto solo Invio, interrompiamo il ciclo
            if (oggetto.isEmpty()) {
                break;
            }

            oggetti.add(oggetto);
            Printer.print("Oggetto: ");
        }



        try {
            AulaBean aulaBean = new AulaBean();
            aulaBean.setIdAula(idAula);
            aulaBean.setEdificio(edificio);
            aulaBean.setPiano(piano);
            aulaBean.setOggetti(oggetti);
            sc.inserisciAula(aulaBean);
            Printer.print("Aula aggiunta correttamente!");
        } catch (AulaGiaPresenteException _) {
            Printer.error("Errore: Aula già presente");
        } catch (IllegalStateException e) {
            Printer.error("Errore: " + e.getMessage());
        }

    }

    private String readLineSafe(BufferedReader br) throws IOException {
        String line = br.readLine();
        return (line != null) ? line.trim() : "";
    }
}
