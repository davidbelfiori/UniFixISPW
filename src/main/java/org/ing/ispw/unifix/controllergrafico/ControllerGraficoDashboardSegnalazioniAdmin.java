package org.ing.ispw.unifix.controllergrafico;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.ing.ispw.unifix.Driver;
import org.ing.ispw.unifix.controllerapplicativo.GestisciSegnalazioniAdmin;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.utils.PopUp;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
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

    private final GestisciSegnalazioniAdmin gs;

    PopUp popUp = new PopUp();

    public ControllerGraficoDashboardSegnalazioniAdmin() {
        gs = new GestisciSegnalazioniAdmin();
    }

    private String getNumeroSegnalazioniRisolte() {
        return gs.visualizzaSegnalazioniRisolteAdmin();
    }

    private String getNumeroSegnalazioniAttive() {
        return gs.visualizzaSegnalazioniAttiveAdmin();
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
        List<Segnalazione> segnalazioneList = gs.getAllSegnalazioni();
        for (Segnalazione segnalazione : segnalazioneList) {
            segnalazioniContainer.getChildren().add(creaBoxSegnalazione(segnalazione));
        }

    }


    private HBox creaBoxSegnalazione(Segnalazione segnalazione) {
        HBox hbox = new HBox(10);
        hbox.setSpacing(15);
        hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);


        hbox.setPrefHeight(108);
        hbox.setPrefWidth(700);
        hbox.setPadding(new Insets(10));
        hbox.setStyle("-fx-background-color: #EEEEEE; -fx-border-color: #CCCCCC; -fx-border-radius: 5; -fx-background-radius: 5;");
        hbox.setOnMouseClicked(event ->
                // Mostra i dettagli della segnalazione
                popUp.showSuccessPopup("Dettagli",segnalazione.toString()));
        // Aggiungi le informazioni della segnalazione
        VBox dettagli = getVBox(segnalazione);

        // Aggiungi tutto all'HBox
        dettagli.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        hbox.getChildren().add(dettagli);
        return hbox;
    }
    @NotNull
    private static VBox getVBox(Segnalazione segnalazione) {
        Label testoLabel = new Label("Edificio: " + segnalazione.getEdificio() +
                "       Aula: " + segnalazione.getAula()+
                "       Tecnico: " + segnalazione.getTecnico().getNome()+ "  " +segnalazione.getTecnico().getCognome());
        testoLabel.setStyle("-fx-text-fill: black; -fx-font-size: 18px; -fx-font-weight: bold; -fx-font: Segoe UI");

        // Layout per i dettagli della segnalazione
        VBox dettagli = new VBox(testoLabel);
        dettagli.setSpacing(5);
        return dettagli;
    }
}
