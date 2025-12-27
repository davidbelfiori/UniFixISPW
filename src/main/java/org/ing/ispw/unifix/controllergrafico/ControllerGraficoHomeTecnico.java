package org.ing.ispw.unifix.controllergrafico;

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
import org.ing.ispw.unifix.bean.InfoTecnicoBean;
import org.ing.ispw.unifix.bean.NotaSegnalazioneBean;
import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.controllerapplicativo.InserisciNotaSegnalazioneController;
import org.ing.ispw.unifix.controllerapplicativo.LoginController;
import org.ing.ispw.unifix.controllerapplicativo.TecnicoController;
import org.ing.ispw.unifix.controllerapplicativo.VisualizzaSegnalazioniTecnicoController;
import org.ing.ispw.unifix.exception.NessunaSegnalazioneException;
import org.ing.ispw.unifix.exception.NessunaSegnalazioneTecnicoException;
import org.ing.ispw.unifix.exception.StoreNotaException;
import org.ing.ispw.unifix.model.NotaSegnalazione;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.utils.PopUp;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

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
    private InserisciNotaSegnalazioneController isnsc;
    PopUp popUp = new PopUp();
    private  VisualizzaSegnalazioniTecnicoController vstc;

    private static final String ACTION_1 = "IN LAVORAZIONE";

    public ControllerGraficoHomeTecnico() {
        tc= TecnicoController.getInstance();
        lc = LoginController.getInstance();
        vstc = new  VisualizzaSegnalazioniTecnicoController();
        isnsc = new InserisciNotaSegnalazioneController();

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
        }catch (NessunaSegnalazioneException | NessunaSegnalazioneTecnicoException _){
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
            alert.setHeaderText("Edificio: " + segnalazione.getEdificio() +
                    "\nAula: " + segnalazione.getAula() +
                    "\nOggetto: " + segnalazione.getOggettoGuasto() +
                    "\nDescrizione: " + segnalazione.getDescrizione() +
                    "\nStato: " + segnalazione.getStato() +
                    "\nDocente: " + segnalazione.getDocente().getNome() + " " + segnalazione.getDocente().getCognome()+ "\n" +
                    "Cosa vuoi fare con questa segnalazione? Chiuderla o mettere in lavorazione?");

            // Bottone "Chiudi segnalazione"
            ButtonType chiudiButton = new ButtonType("Chiudi", ButtonBar.ButtonData.OK_DONE);
            // Bottone "Metti in lavorazione"
            ButtonType lavorazioneButton = new ButtonType("lavorazione", ButtonBar.ButtonData.OK_DONE);
            // Bottone Note
            ButtonType noteButton = new  ButtonType("Note", ButtonBar.ButtonData.OK_DONE);

            ButtonType cancelButton = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(chiudiButton, lavorazioneButton,noteButton,cancelButton);

            alert.showAndWait().ifPresent(response -> {
                if (response == lavorazioneButton){
                    tc.updateSegnalazione(new SegnalazioneBean(segnalazione.getIdSegnalzione(), ACTION_1));
                    segnalazioniContainer.getChildren().clear();
                    mostraSegnalazioniTecnico();
                } else if (response == chiudiButton){
                    tc.updateSegnalazione(new SegnalazioneBean(segnalazione.getIdSegnalzione(), "CHIUSA"));
                    segnalazioniContainer.getChildren().clear();
                    mostraSegnalazioniTecnico();
                } else if (response == noteButton){
                    if (Objects.equals(segnalazione.getStato(), ACTION_1)){mostraDialogoNote(segnalazione);}
                    else {
                        popUp.showErrorPopup("Attenzione!","Riprova più tardi","Per aggiungere una nota , l'intervento deve essere in lavorazione");
                    }
                } else if (response == ButtonType.CLOSE){
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
        Label testoLabel = new Label("Edificio: " + segnalazione.getEdificio() +
                "    Aula: " + segnalazione.getAula() +
                "    Oggetto: " + segnalazione.getOggettoGuasto()+
                "    Docente: " + segnalazione.getDocente().getNome() + " " + segnalazione.getDocente().getCognome());
        testoLabel.setStyle("-fx-text-fill: black; -fx-font-size: 20px; -fx-font-weight: bold; -fx-font: Segoe UI");

        // Layout per i dettagli della segnalazione
        VBox dettagli = new VBox(testoLabel);

        dettagli.setSpacing(5);
        return dettagli;
    }



    private void mostraDialogoNote(Segnalazione segnalazione) {
        javafx.scene.control.Dialog<String> dialog = new javafx.scene.control.Dialog<>();
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
        List<NotaSegnalazione> noteAttuali = isnsc.getNoteForSegnalazione(segnalazione.getIdSegnalzione());
        StringBuilder noteTesto = new StringBuilder();
        if (noteAttuali.isEmpty()) {
            noteTesto.append("Non ci sono note presenti.");
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            for (NotaSegnalazione nota : noteAttuali) {
                noteTesto.append(dateFormat.format(nota.getDataCreazione().getTime()))
                        .append(": ").append(nota.getTesto()).append("\n");
            }
        }
        noteEsistentiArea.setText(noteTesto.toString());

        // Area per aggiungere nuova nota
        Label labelNuovaNota = new Label("Aggiungi nuova nota:");
        javafx.scene.control.TextArea nuovaNotaArea = new javafx.scene.control.TextArea();
        nuovaNotaArea.setPromptText("Scrivi qui la nuova nota...");
        nuovaNotaArea.setPrefRowCount(3);
        nuovaNotaArea.setWrapText(true);

        content.getChildren().addAll(labelNoteEsistenti, noteEsistentiArea, labelNuovaNota, nuovaNotaArea);
        dialog.getDialogPane().setContent(content);

        // Bottoni
        ButtonType salvaButton = new ButtonType("Salva", ButtonBar.ButtonData.OK_DONE);
        ButtonType annullaButton = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(salvaButton, annullaButton);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == salvaButton) {
                if (Objects.equals(segnalazione.getStato(), ACTION_1)) {
                    return nuovaNotaArea.getText();
                }else {
                    popUp.showErrorPopup("Attenzione!","Operazione non consentita","Per aggiungere una nota ,\n l'intervento deve essere in lavorazione");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(nuovaNota -> {
            if (!nuovaNota.trim().isEmpty()) {
                // Salva la nuova nota tramite il controller
                try {
                    isnsc.inserisciNotaSegnalazione(new NotaSegnalazioneBean(segnalazione.getIdSegnalzione(), nuovaNota));
                }catch (StoreNotaException e){
                    popUp.showErrorPopup("Errore","Si è verificato un errore","Messaggio"+e.getMessage());
                }
            }
        });
    }


    @FXML
    void mostraInfoTecnico(MouseEvent event) {
        // 1. Recupera i dati dal controller applicativo
        InfoTecnicoBean infoTecnico = TecnicoController.getInstance().getTecnicoInformation();

        if (infoTecnico == null) {popUp.showErrorPopup("Errore","Si è verificato un errore","Riprova");
            return;}

        // 2. Crea il layout della Card
        VBox card = getVBox();

        // 3. Popola la card con i dati
        Label lbNome = new Label(infoTecnico.getNome() + " " + infoTecnico.getCognome());
        lbNome.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #333;");

        Label lbEmail = new Label(infoTecnico.getEmail());
        lbEmail.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");

        Label lbRuolo = new Label(infoTecnico.getRuolo().toString());
        lbRuolo.setStyle("-fx-text-fill: #0056b4; -fx-font-weight: bold;");

        // Aggiungi tutto al contenitore
        card.getChildren().addAll(lbNome, lbEmail, new Separator(), lbRuolo);

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

    @NotNull
    private static VBox getVBox() {
        VBox card = new VBox(12); // 10px di spazio verticale tra gli elementi
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
        return card;
    }

    @FXML
    protected void logout(MouseEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource("login.fxml"));
        ((Node) event.getSource()).getScene().setRoot(fxmlLoader.load());
    }

}
