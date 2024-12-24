package org.ing.ispw.unifix.controllergrafico;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.ing.ispw.unifix.Driver;
import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.controllerapplicativo.*;
import org.ing.ispw.unifix.exception.NessunSegnalazioneDocenteException;
import org.ing.ispw.unifix.exception.NessunaSegnalazioneException;
import org.ing.ispw.unifix.exception.NonCiSonoTecniciException;
import org.ing.ispw.unifix.exception.SegnalazioneGiaEsistenteException;
import org.ing.ispw.unifix.model.Aula;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.utils.PopUp;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class ControllerGraficoHomeDocente {

    @FXML
    private VBox segnalazioniContainer;
    @FXML
    private ComboBox<String> edificioComboBox;
    @FXML
    private ComboBox<String> aulaComboBox;
    @FXML
    private ComboBox<String> oggettoComboBox;
    @FXML
    private TextArea descrizioneTextArea;
    @FXML
    private Label testoSegnalazioni;
    @FXML
    private Label welcome;
    private LoginController lc;
    PopUp popUp = new PopUp();
    private DocenteController dc;
    InviaSegnalazioneController sc;
    SysAdminController sysAdminController;
    VisualizzaSegnalazioniDocenteController vsdc;
    public ControllerGraficoHomeDocente() {
        lc = LoginController.getInstance();
        dc = DocenteController.getInstance();
        sc=InviaSegnalazioneController.getInstance();
        sysAdminController = SysAdminController.getInstance();
        vsdc = VisualizzaSegnalazioniDocenteController.getInstance();
        sysAdminController.inserisciAule("src/main/resources/utvAule.csv");
    }

    public void initialize() {
        welcome.setText(lc.getCurrentUser().getCognome() +"  "+lc.getCurrentUser().getNome());
        List<String> edifici=sc.getEdifici();
        edificioComboBox.setItems(FXCollections.observableList(edifici));
        edificioComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) { // Verifica che ci sia un valore selezionato
                aggiornaAule(newValue); // Passa l'edificio selezionato al metodo
            }
        });
       aulaComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, aulaScelta) -> {
            if (aulaScelta != null) { // Verifica che ci sia un valore selezionato
                aggiornaOggetti(aulaScelta); // Passa l'edificio selezionato al metodo
            }
        });

       mostraSegnalazioni();
    }

    public void mostraSegnalazioni() {
        List<Segnalazione> segnalazioniDocente = null;
        try {
            segnalazioniDocente = vsdc.visualizzaSegnalazioniDocente();

            segnalazioniContainer.getChildren().clear();
            for (Segnalazione segnalazione : segnalazioniDocente) {
                segnalazioniContainer.getChildren().add(creaBoxSegnalazione(segnalazione));
            }
        }
        catch(NessunaSegnalazioneException | NessunSegnalazioneDocenteException e){
            testoSegnalazioni.setText("Non ci sono segnalazioni");
            testoSegnalazioni.setStyle("-fx-text-fill: white;");
        }

    }

    private HBox creaBoxSegnalazione(Segnalazione segnalazione) {
        HBox hbox = new HBox(10);
        hbox.setSpacing(10);

        hbox.setPrefHeight(108);
        hbox.setPrefWidth(700);
        hbox.setPadding(new Insets(10));
        hbox.setStyle("-fx-background-color: #EEEEEE; -fx-border-color: #CCCCCC; -fx-border-radius: 5; -fx-background-radius: 5;");
        hbox.setOnMouseClicked(event -> {
            // Mostra i dettagli della segnalazione
            popUp.showSuccessPopup("Dettagli segnalazione","Aula: "+segnalazione.getAula()+"\nOggetto: "+segnalazione.getOggettoGuasto()+"\nDescrizione: "+segnalazione.getDescrizone()+"\nStato: "+segnalazione.getStato()+"\nTecnico: "+segnalazione.getTecnico().getNome()+" "+segnalazione.getTecnico().getCognome());
        });

        // Aggiungi le informazioni della segnalazione
        VBox dettagli = getVBox(segnalazione);

        // Aggiungi tutto all'HBox
        hbox.getChildren().add(dettagli);
        return hbox;
    }

    @NotNull
    private static VBox getVBox(Segnalazione segnalazione) {
        Label aulaLabel = new Label("Aula: " + segnalazione.getAula());
        aulaLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");

        Label oggettoLabel = new Label("Oggetto: " + segnalazione.getOggettoGuasto());
        oggettoLabel.setStyle("-fx-text-fill: #000000;");

        Label statoLabel = new Label("Stato: " + segnalazione.getStato());
        statoLabel.setStyle("-fx-text-fill: #000000;");

        // Layout per i dettagli della segnalazione
        VBox dettagli = new VBox(aulaLabel, oggettoLabel, statoLabel);
        dettagli.setSpacing(5);
        return dettagli;
    }

    @FXML
    private void aggiornaAule(String edificioSelezionato) {
        List<Aula> auleEdificioSelezionato = sc.getAuleByEdificio(edificioSelezionato);
        List<String> nomiAule = auleEdificioSelezionato.stream()
                .map(Aula::getIdAula) // Supponendo che il metodo getNome() restituisca il nome dell'aula
                .toList();
        aulaComboBox.setItems(FXCollections.observableList(nomiAule));
    }
    @FXML
    private void aggiornaOggetti(String aulaScelta) {
        Aula oggettiAulaSelezionata = sc.getOggettiAula(aulaScelta);
        List<String> oggettiAula=  oggettiAulaSelezionata.getOggetti();
        oggettoComboBox.setItems(FXCollections.observableList(oggettiAula));
    }

    @FXML
    protected void logout(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource("login.fxml"));
        ((Node) event.getSource()).getScene().setRoot(fxmlLoader.load());
    }

    public void inviaSegnalazione(MouseEvent mouseEvent) {
        String edificio = edificioComboBox.getValue();
        String aula = aulaComboBox.getValue();
        String oggetto = oggettoComboBox.getValue();
        String descrizione = descrizioneTextArea.getText();

        if (edificio == null || aula == null || oggetto == null || descrizione.isEmpty()) {
            popUp.showErrorPopup("Errore", "Compilare tutti i campi", "Riprova");
        } else {
            try {
                sc.creaSegnalazione(new SegnalazioneBean(System.currentTimeMillis(), aula, edificio, oggetto, descrizione));
                popUp.showSuccessPopup("Successo", "Segnalazione inviata");
                mostraSegnalazioni();
            }catch (NonCiSonoTecniciException | SegnalazioneGiaEsistenteException e) {
                popUp.showErrorPopup("Errore", e.getMessage(), "Riprova");
            }
        }
    }


}
