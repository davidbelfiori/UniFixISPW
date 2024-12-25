package org.ing.ispw.unifix.controllergrafico;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.ing.ispw.unifix.Driver;
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
    PopUp popUp = new PopUp();
    private TecnicoController tc;
    private final VisualizzaSegnalazioniTecnicoController vstc;
    public ControllerGraficoHomeTecnico() {

        lc = LoginController.getInstance();
        tc = TecnicoController.getInstance();
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
            // Mostra i dettagli della segnalazione
            popUp.showSuccessPopup("Dettagli segnalazione","Edificio"+segnalazione.getEdifico()+"\nAula: "+segnalazione.getAula()+"\nOggetto: "+segnalazione.getOggettoGuasto()+"\nDescrizione: "+segnalazione.getDescrizone()+"\nStato: "+segnalazione.getStato()+"\nDocente Segnalatore: "+segnalazione.getDocente().getNome()+" "+segnalazione.getDocente().getCognome());
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
