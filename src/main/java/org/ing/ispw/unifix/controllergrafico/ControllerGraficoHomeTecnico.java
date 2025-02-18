package org.ing.ispw.unifix.controllergrafico;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.ing.ispw.unifix.Driver;
import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.controllerapplicativo.LoginController;
import org.ing.ispw.unifix.controllerapplicativo.TecnicoController;
import org.ing.ispw.unifix.controllerapplicativo.VisualizzaSegnalazioniTecnicoController;
import org.ing.ispw.unifix.exception.NessunaSegnalazioneException;
import org.ing.ispw.unifix.exception.NessunaSegnalazioneTecnicoException;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.utils.PopUp;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class ControllerGraficoHomeTecnico {
    @FXML
    private Label welcome1;
    @FXML
    private Label testoSegnalazioniTecnico;
    @FXML
    private VBox segnalazioniContainer;
    @FXML
    private Label welcome;
    private LoginController lc;
    private TecnicoController tc;
    PopUp popUp = new PopUp();
    private final VisualizzaSegnalazioniTecnicoController vstc;

    public ControllerGraficoHomeTecnico() {
        tc= TecnicoController.getInstance();
        lc = LoginController.getInstance();
        vstc = VisualizzaSegnalazioniTecnicoController.getInstance();
    }

    public void initialize() {

        welcome1.setText(lc.getCurrentUser().getCognome() +"  "+lc.getCurrentUser().getNome()+"  ecco i tuoi interventi");
        mostraSegnalazioniTecnico();
    }


    public void mostraSegnalazioniTecnico(){
        List<Segnalazione> segnalazioni = null;
        try{
            segnalazioni = vstc.visualizzaSegnalazioniTecnico();
            testoSegnalazioniTecnico.setText("I tuoi interventi:");
            testoSegnalazioniTecnico.setStyle("-fx-text-fill: white");
            for (Segnalazione segnalazione : segnalazioni) {
                segnalazioniContainer.getChildren().add(creaBoxSegnalazione(segnalazione));
            }
        }catch (NessunaSegnalazioneException | NessunaSegnalazioneTecnicoException e){
            testoSegnalazioniTecnico.setText("Non ci sono interventi da visualizzare");
            testoSegnalazioniTecnico.setStyle("-fx-text-fill: white");
        }

    }

    private HBox creaBoxSegnalazione(Segnalazione segnalazione) {
        HBox hbox = new HBox(10);
        hbox.setSpacing(15);
        hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        hbox.setPrefHeight(108);
        hbox.setPrefWidth(1020);
        hbox.setPadding(new Insets(10));
        hbox.setStyle("-fx-background-color: #EEEEEE; -fx-border-color: #CCCCCC; -fx-border-radius: 5; -fx-background-radius: 5;");
        hbox.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Dettagli Segnalazione");
            alert.setHeaderText("Edificio: " + segnalazione.getEdifico() +
                    "\nAula: " + segnalazione.getAula() +
                    "\nOggetto: " + segnalazione.getOggettoGuasto() +
                    "\nDescrizione: " + segnalazione.getDescrizone() +
                    "\nStato: " + segnalazione.getStato() +
                    "\nDocente: " + segnalazione.getDocente().getNome() + " " + segnalazione.getDocente().getCognome());

            // Bottone "Chiudi"
            ButtonType chiudiButton = new ButtonType("Chiudi", ButtonBar.ButtonData.CANCEL_CLOSE);
            // Bottone "Metti in lavorazione"
            ButtonType lavorazioneButton = new ButtonType("lavorazione", ButtonBar.ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(chiudiButton, lavorazioneButton);

            alert.showAndWait().ifPresent(response -> {
              if (response == lavorazioneButton){
                    tc.updateSegnalazione(new SegnalazioneBean(segnalazione.getIdSegnalzione(), "IN LAVORAZIONE"));
                    segnalazioniContainer.getChildren().clear();
                    mostraSegnalazioniTecnico();
              } else{
                  //bug quando selezioni in lavorazione la chiude sempre
                  tc.updateSegnalazione(new SegnalazioneBean(segnalazione.getIdSegnalzione(), "CHIUSA"));
                  segnalazioniContainer.getChildren().clear();
                  mostraSegnalazioniTecnico();
              }
            });
        });

        // Aggiungi le informazioni della segnalazione
        VBox dettagli = getVBox(segnalazione);
        dettagli.setAlignment(javafx.geometry.Pos.CENTER_LEFT);


        // Aggiungi tutto all'HBox
        hbox.getChildren().add(dettagli);
        return hbox;
    }


    @NotNull
    private static VBox getVBox(Segnalazione segnalazione) {
        Label testoLabel = new Label("Edificio: " + segnalazione.getEdifico() +
                "    Aula: " + segnalazione.getAula() +
                "    Oggetto: " + segnalazione.getOggettoGuasto()+
                "    Docente: " + segnalazione.getDocente().getNome() + " " + segnalazione.getDocente().getCognome());
        testoLabel.setStyle("-fx-text-fill: black; -fx-font-size: 20px; -fx-font-weight: bold; -fx-font: Segoe UI");

        // Layout per i dettagli della segnalazione
        VBox dettagli = new VBox(testoLabel);

        dettagli.setSpacing(5);
        return dettagli;
    }

    @FXML
    protected void logout(MouseEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource("login.fxml"));
        ((Node) event.getSource()).getScene().setRoot(fxmlLoader.load());
    }

}
