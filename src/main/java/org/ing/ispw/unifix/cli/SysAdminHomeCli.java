package org.ing.ispw.unifix.cli;

import org.ing.ispw.unifix.bean.AulaBean;
import org.ing.ispw.unifix.controllerapplicativo.GestioneAuleController;

import org.ing.ispw.unifix.exception.AulaGiaPresenteException;
import org.ing.ispw.unifix.exception.AuleNonTrovateException;
import org.ing.ispw.unifix.exception.DatiAulaNonValidiException;
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
            Printer.print("\t4) Log off");
            Printer.print("\t5) Quit");
            Printer.print(": ");

            String action = br.readLine();

            switch(action) {
                case "1":
                    sc.inserisciAule("src/main/resources/utvAule.csv");
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
                    } catch (AuleNonTrovateException | DatiAulaNonValidiException e) {
                        Printer.error("Errore"+e.getMessage());
                    }
                    break;
                case "4":
                    return;
                case "5":
                    quit=TRUE;
                    break;
                default:
                    return;
            }
        }
        System.exit(0);


    }

    private void inserisciAulaSingola() throws IOException {
        Printer.print("\n--- Inserimento Nuova Aula ---");

        Printer.print("Inserisci ID Aula (es. A1): ");
        String idAula = br.readLine().trim();

        Printer.print("Inserisci Edificio: ");
        String edificio = br.readLine().trim();

        Printer.print("Inserisci Piano (numero): ");
        int piano;
        try {
            piano = Integer.parseInt(br.readLine().trim());
        } catch (NumberFormatException _) {
            Printer.error("Piano non valido, impostato a 0");
            piano = 0;
        }

        List<String> oggetti = new ArrayList<>();
        Printer.print("Inserisci oggetti (invio vuoto per terminare):");
        String oggetto;
        do {
            Printer.print("Oggetto: ");
            oggetto = br.readLine().trim();
            if (!oggetto.isEmpty()) {
                oggetti.add(oggetto);
            }
        } while (!oggetto.isEmpty());



        try {
            AulaBean aulaBean = new AulaBean(idAula, edificio, piano, oggetti);
            sc.inserisciAula(aulaBean);
            Printer.print("Aula aggiunta correttamente!");
        } catch (AulaGiaPresenteException _) {
            Printer.error("Errore: Aula già presente");
        }
        catch (DatiAulaNonValidiException e) {
            Printer.error("Errore: " + e.getMessage());
        }

    }
}
