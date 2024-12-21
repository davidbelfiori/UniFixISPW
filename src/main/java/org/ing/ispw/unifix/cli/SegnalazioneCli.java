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

    boolean quit;
    BufferedReader br;
    InviaSegnalazioneController sc;

    public SegnalazioneCli() {
        quit = false;
        br = new BufferedReader(new InputStreamReader(System.in));
        sc= InviaSegnalazioneController.getInstance();

    }

    public void segnalazioneView() throws IOException {
        String edificioSelezionato="";
        String aulaSelezionata="";
        String oggettoSelezionato="";
        String descrizioneGuasto="";
        List<String> edificiUnici = new ArrayList<>();
        List<Aula> auleEdificioSelezionato;
        List <String> oggettiAula = new ArrayList<>();
        while (!quit) {

            Printer.print("*******Form per l'invio di una segnalazione******");
            Printer.print("\t1) Visualizza gli edifici");
            Printer.print("\t2) Edificio Selezionato ["+edificioSelezionato+"]");
            Printer.print("\t3) Visualizza aule di ["+edificioSelezionato+"]");
            Printer.print("\t4) Aula selezionata ["+aulaSelezionata+"]");
            Printer.print("\t5) Visualizza oggetti presenti in ["+aulaSelezionata+"]");
            Printer.print("\t6) Oggetto Selezionato ["+oggettoSelezionato+"]");
            Printer.print("\t7)Inserisci una descrizione del problema (obbligatorio)");
            Printer.print("\t8) Invia Segnalazione");
            Printer.print("\t9)Back");
            Printer.print(": ");

            String action = br.readLine();

            switch (action) {
                case "1":
                    edificiUnici = sc.getEdifici();
                    for (String edifico : edificiUnici){
                        Printer.print("Edificio: "+edifico);
                    }
                    break;
                case "2":
                    Printer.print("Edificio:");
                    edificioSelezionato = br.readLine();
                    if (!edificiUnici.contains(edificioSelezionato)){
                        Printer.error("devi prima visualizzare gli edifici o l'edificio immesso non è esistente");
                        edificioSelezionato="";
                    }
                    break;
                case "3":
                    auleEdificioSelezionato = sc.getAuleByEdificio(edificioSelezionato);
                    for (Aula a: auleEdificioSelezionato){
                        Printer.print("Edificio"+ a.getEdificio());
                        Printer.print("Aula:"+a.getIdAula());
                        Printer.print("Piano"+a.getPiano());
                    }
                    break;
                case "4":
                    Printer.print("Aula:");
                    aulaSelezionata = br.readLine();
                    break;
                case "5":
                    Aula oggetti = sc.getOggettiAula(aulaSelezionata);
                    oggettiAula=  oggetti.getOggetti();
                    Printer.print("Nell'aula "+aulaSelezionata+" ci sono i seguenti oggetti");
                    for (String o:oggettiAula){
                        Printer.print(o);
                    }
                    break;
                case "6":
                    Printer.print("Oggetto:");
                    oggettoSelezionato = br.readLine();
                    if (!oggettiAula.contains(oggettoSelezionato)) {
                        Printer.error("l'oggetto selezionato non è presente oppure controlla come lo hai scritto");
                        oggettoSelezionato="";
                        break;
                    }
                    break;
                case "7":
                    Printer.print("Descrivi problema:");
                    descrizioneGuasto = br.readLine();
                    break;
                case "8":
                    Printer.print("*****La tua segnalazione*****");
                    Printer.print("Edificio: "+edificioSelezionato+"\n Aula: "+aulaSelezionata+"\n Oggetto Gusto: "+oggettoSelezionato+"\n Descrizione Gasto: "+ descrizioneGuasto);
                    if(sc.creaSegnalazione(new SegnalazioneBean(System.currentTimeMillis(),aulaSelezionata,edificioSelezionato,oggettoSelezionato,descrizioneGuasto))) {
                        Printer.print("Segnalazione inviata con successo");
                    }else {Printer.print("Annuncio gia esistente");
                        edificioSelezionato="";
                         aulaSelezionata="";
                         oggettoSelezionato="";
                         descrizioneGuasto="";}
                    break;
                default:
                    return;
            }

        }
    }

}
