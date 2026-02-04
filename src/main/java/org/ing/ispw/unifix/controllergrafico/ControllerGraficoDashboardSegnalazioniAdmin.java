package org.ing.ispw.unifix.controllergrafico;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.ing.ispw.unifix.Driver;
import org.ing.ispw.unifix.bean.NotaSegnalazioneBean;
import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.controllerapplicativo.DashboardKpiController;
import org.ing.ispw.unifix.controllerapplicativo.GestisciSegnalazioniAdminController;
import org.ing.ispw.unifix.controllerapplicativo.InserisciNotaSegnalazioneController;
import org.ing.ispw.unifix.utils.PopUp;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ControllerGraficoDashboardSegnalazioniAdmin {

    @FXML
    private VBox segnalazioniContainer;
    @FXML
    private AnchorPane inserisciSegnalazioni;
    @FXML
    private Label segnalazioniAttiveLabel;

    @FXML
    private Label segnalazioRisolteLabel;

    public void initialize() {
        updateLabels();
        mostraSegnalazioni();
    }

    private final GestisciSegnalazioniAdminController gs;
    private final InserisciNotaSegnalazioneController isnsc;
    private final DashboardKpiController dkc;

    PopUp popUp = new PopUp();

    public ControllerGraficoDashboardSegnalazioniAdmin() {
        gs = new GestisciSegnalazioniAdminController();
        isnsc = new InserisciNotaSegnalazioneController();
        dkc = new DashboardKpiController();
    }

    private String getNumeroSegnalazioniRisolte() {
        return dkc.visualizzaSegnalazioniRisolteAdmin();
    }

    private String getNumeroSegnalazioniAttive() {
        return dkc.visualizzaSegnalazioniAttiveAdmin();
    }

    @FXML
    protected void logout(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource("login.fxml"));
        ((Node) event.getSource()).getScene().setRoot(fxmlLoader.load());
    }

    @FXML
    void goToGestioneAule(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource("GestioneAule.fxml"));
        ((Node) event.getSource()).getScene().setRoot(fxmlLoader.load());
    }


    private void updateLabels() {
        segnalazioniAttiveLabel.setText(getNumeroSegnalazioniAttive());
        segnalazioRisolteLabel.setText(getNumeroSegnalazioniRisolte());
    }

    public void goToHomeAdmin(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource("homeAdmin.fxml"));
        ((Node) mouseEvent.getSource()).getScene().setRoot(fxmlLoader.load());
    }

    public void mostraSegnalazioni(){
        List<SegnalazioneBean> segnalazioneList = gs.getAllSegnalazioni();
        for (SegnalazioneBean segnalazione : segnalazioneList) {
            segnalazioniContainer.getChildren().add(creaBoxSegnalazione(segnalazione));
        }

    }


    private HBox creaBoxSegnalazione(SegnalazioneBean segnalazione) {
        HBox hbox = new HBox(10);
        hbox.setSpacing(15);
        hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);


        hbox.setPrefHeight(108);
        hbox.setPrefWidth(700);
        hbox.setPadding(new Insets(10));
        hbox.setStyle("-fx-background-color: #EEEEEE; -fx-border-color: #CCCCCC; -fx-border-radius: 5; -fx-background-radius: 5;");

        hbox.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Dettagli Segnalazione");
            alert.setHeaderText("Edificio: " + segnalazione.getEdificio() +
                    "\nAula: " + segnalazione.getAula() +
                    "\nOggetto: " + segnalazione.getOggettoGuasto() +
                    "\nDescrizione: " + segnalazione.getDescrizione() +
                    "\nStato: " + segnalazione.getStato() +
                    "\nDocente: " + segnalazione.getUser().getNome() + " " + segnalazione.getUser().getCognome()+
                    "\nTecnico: " + segnalazione.getTecnico().getNome() + " " + segnalazione.getTecnico().getCognome()
                    );

            // Bottone Note
            ButtonType noteButton = new  ButtonType("Note", ButtonBar.ButtonData.OK_DONE);


            alert.getButtonTypes().setAll(noteButton);

            alert.showAndWait().ifPresent(response -> {
              if (response == noteButton){mostraNote(segnalazione);}
            });
        });

        // Aggiungi le informazioni della segnalazione
        VBox dettagli = getVBox(segnalazione);

        // Aggiungi tutto all'HBox
        dettagli.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        hbox.getChildren().add(dettagli);
        return hbox;
    }


    private void mostraNote(SegnalazioneBean segnalazione) {
        Dialog<String> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Note Segnalazione");
        dialog.setHeaderText("Gestione note per: " + segnalazione.getOggettoGuasto() + "  in  " + segnalazione.getEdificio() + "  aula  " + segnalazione.getAula());

        // Layout del dialogo
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        // Area per visualizzare le note esistenti
        Label labelNoteEsistenti = new Label("Note esistenti:");
        javafx.scene.control.TextArea noteEsistentiArea = new javafx.scene.control.TextArea();
        noteEsistentiArea.setEditable(false);
        noteEsistentiArea.setPrefRowCount(5);
        noteEsistentiArea.setWrapText(true);

        // Carica le note esistenti (adatta al tuo model)
        List<NotaSegnalazioneBean> noteAttuali = isnsc.getNoteForSegnalazione(segnalazione.getIdSegnalazione());
        StringBuilder noteTesto = new StringBuilder();
        if (noteAttuali.isEmpty()) {
            noteTesto.append("Non ci sono note presenti.");
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            for (NotaSegnalazioneBean nota : noteAttuali) {
                noteTesto.append(dateFormat.format(nota.getDataCreazione().getTime()))
                        .append(": ").append(nota.getTestoNota()).append("\n");
            }
        }
        noteEsistentiArea.setText(noteTesto.toString());
        ButtonType closeButton = new ButtonType("Ok", ButtonBar.ButtonData.CANCEL_CLOSE);


        content.getChildren().addAll(labelNoteEsistenti, noteEsistentiArea);
        dialog.getDialogPane().getButtonTypes().addAll(closeButton);
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait();
        // Aggiungi un bottone "Chiudi" per chiudere il dialogo


        // Imposta l'azione per il bottone "Chiudi"
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == closeButton) {
                return "Closed"; // Puoi restituire qualsiasi valore per indicare la chiusura
            }
            return null;
        });

    }


    @NotNull
    private static VBox getVBox(SegnalazioneBean segnalazione) {
        Label testoLabel = new Label("Edificio: " + segnalazione.getEdificio() +
                "       Aula: " + segnalazione.getAula()+ "  Stato: " + segnalazione.getStato());
        testoLabel.setStyle("-fx-text-fill: black; -fx-font-size: 18px; -fx-font-weight: bold; -fx-font: Segoe UI");

        // Layout per i dettagli della segnalazione
        VBox dettagli = new VBox(testoLabel);
        dettagli.setSpacing(5);
        return dettagli;
    }
}
