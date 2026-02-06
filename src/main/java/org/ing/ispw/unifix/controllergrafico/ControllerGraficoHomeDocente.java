package org.ing.ispw.unifix.controllergrafico;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Window;
import org.ing.ispw.unifix.Driver;
import org.ing.ispw.unifix.bean.InfoDocenteBean;
import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.controllerapplicativo.*;
import org.ing.ispw.unifix.exception.NessunSegnalazioneDocenteException;
import org.ing.ispw.unifix.exception.NessunaSegnalazioneException;
import org.ing.ispw.unifix.exception.NonCiSonoTecniciException;
import org.ing.ispw.unifix.exception.SegnalazioneGiaEsistenteException;
import org.ing.ispw.unifix.model.Aula;
import org.ing.ispw.unifix.utils.PopUp;
import org.ing.ispw.unifix.utils.Printer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class ControllerGraficoHomeDocente {

    private static final String ACTION_1= "Errore";

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
    PopUp popUp = new PopUp();

    private final  InviaSegnalazioneController sc;
    private final VisualizzaSegnalazioniDocenteController vsdc;
    private final  DocenteController docenteController;



    public ControllerGraficoHomeDocente() {
        lc = LoginController.getInstance();
        sc=new InviaSegnalazioneController();
        vsdc = new VisualizzaSegnalazioniDocenteController();
        docenteController = new DocenteController();
    }

    public void initialize() {
        InfoDocenteBean infoDocente = docenteController.getDocenteInformation();
        welcome.setText(infoDocente.getCognome() +"  "+ infoDocente.getNome());
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
        List<SegnalazioneBean> segnalazioniDocente = null;
        try {
            segnalazioniDocente = vsdc.visualizzaSegnalazioniDocente();
            testoSegnalazioni.setText("Le tue segnalazioni inviate:");
            testoSegnalazioni.setStyle("-fx-text-fill: white;");
            segnalazioniContainer.getChildren().clear();
            for (SegnalazioneBean segnalazione : segnalazioniDocente) {
                segnalazioniContainer.getChildren().add(creaBoxSegnalazione(segnalazione));
            }
        }
        catch(NessunaSegnalazioneException | NessunSegnalazioneDocenteException _){
            testoSegnalazioni.setText("Non ci sono segnalazioni");
            testoSegnalazioni.setStyle("-fx-text-fill: white;");
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
        hbox.setOnMouseClicked(event ->
            // Mostra i dettagli della segnalazione
            popUp.showSuccessPopup("Dettagli segnalazione","Aula: "+segnalazione.getAula()+"\nOggetto: "+segnalazione.getOggettoGuasto()+"\nDescrizione: "+segnalazione.getDescrizione()+"\nStato: "+segnalazione.getStato()+"\nTecnico: "+segnalazione.getTecnico().getNome()+" "+segnalazione.getTecnico().getCognome()));

        // Aggiungi le informazioni della segnalazione
        VBox dettagli = getVBox(segnalazione);

        // Aggiungi tutto all'HBox
        dettagli.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        hbox.getChildren().add(dettagli);
        return hbox;
    }
    @NotNull
    private static VBox getVBox(SegnalazioneBean segnalazione) {
        Label testoLabel = new Label("Edificio: " + segnalazione.getEdificio() +
                "    Aula: " + segnalazione.getAula() +
                "    Oggetto: " + segnalazione.getOggettoGuasto() +
                "    Stato: " + segnalazione.getStato());
        testoLabel.setStyle("-fx-text-fill: black; -fx-font-size: 15px; -fx-font-weight: bold; -fx-font: Segoe UI");

        // Layout per i dettagli della segnalazione
        VBox dettagli = new VBox(testoLabel);
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
        List<String> oggettiAulaSelezionata = sc.getOggettiAula(aulaScelta);
        if (oggettiAulaSelezionata == null){
            Printer.error("Oggetti selezionata non trovata");
        }else {
            oggettoComboBox.setItems(FXCollections.observableList(oggettiAulaSelezionata));
        }

    }

    @FXML
    protected void logout(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource("login.fxml"));
        ((Node) event.getSource()).getScene().setRoot(fxmlLoader.load());
    }

    public void inviaSegnalazione() {
        String edificio = edificioComboBox.getValue();
        String aula = aulaComboBox.getValue();
        String oggetto = oggettoComboBox.getValue();
        String descrizione = descrizioneTextArea.getText();

        if (edificio == null || aula == null || oggetto == null || descrizione.isEmpty()) {
            popUp.showErrorPopup("Si è verificato errore", "Compilare tutti i campi", "Riprova");
            return;
        }

        // Mostra il popup di conferma
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Segnalazione");
        alert.setHeaderText("Sei sicuro di voler inviare questa segnalazione?");
        alert.setContentText("Edificio: " + edificio + "\n" +
                "Aula: " + aula + "\n" +
                "Oggetto: " + oggetto + "\n" +
                "Descrizione: " + descrizione);

        ButtonType buttonConferma = new ButtonType("Conferma");
        ButtonType buttonAnnulla = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonConferma, buttonAnnulla);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonConferma){
        try {
                sc.creaSegnalazione(new SegnalazioneBean(new Date(System.currentTimeMillis()), aula, edificio, oggetto, descrizione));

                popUp.showSuccessPopup("Successo", "Segnalazione inviata");
                mostraSegnalazioni();
            }catch (NonCiSonoTecniciException _) {
                popUp.showErrorPopup(ACTION_1, "Errore tecnici non trovati", "Al momento non ci sono tecnici inseriti nel sistema");
            }
        catch (SegnalazioneGiaEsistenteException _){
            popUp.showErrorPopup(ACTION_1, "Segnalazione già inviata", "La segnalazione per l'oggetto selezionato è gia stata inviata");
        }
        }else {
            popUp.showErrorPopup(ACTION_1, "Segnalazione non inviata", "L'invio della segnalazione è stato annullato");
        }
    }

    @FXML
    void mostraInfoDocente(MouseEvent event) {
        // 1. Recupera i dati dal controller applicativo
        InfoDocenteBean info = docenteController.getDocenteInformation();

        if (info == null) return;

        // 2. Crea il layout della Card
        VBox card = new VBox(10); // 10px di spazio verticale tra gli elementi
        card.setPadding(new Insets(15));
        card.setPrefWidth(250);

        // Stile "Card" (Sfondo bianco, ombra, bordi arrotondati)
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #cccccc;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);"
        );

        // 3. Popola la card con i dati
        Label lblNome = new Label(info.getNome() + " " + info.getCognome());
        lblNome.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #333;");

        Label lblEmail = new Label(info.getEmail());
        lblEmail.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");

        Label lblRuolo = new Label("Ruolo: Docente");
        lblRuolo.setStyle("-fx-text-fill: #0056b3; -fx-font-weight: bold;");

        // Aggiungi tutto al contenitore
        card.getChildren().addAll(lblNome, lblEmail, new Separator(), lblRuolo);

        // 4. Crea il Popup e mostralo
        Popup popup = new Popup();
        popup.getContent().add(card);
        popup.setAutoHide(true); // Si chiude se clicchi fuori

        // Posiziona il popup sotto l'icona cliccata
        Node source = (Node) event.getSource();
        Window stage = source.getScene().getWindow();

        // Calcolo posizione: x leggermente spostato a sinistra per allinearlo, y sotto l'icona
        double anchorX = event.getScreenX() - 200;
        double anchorY = event.getScreenY() + 20;

        popup.show(stage, anchorX, anchorY);
    }



}
