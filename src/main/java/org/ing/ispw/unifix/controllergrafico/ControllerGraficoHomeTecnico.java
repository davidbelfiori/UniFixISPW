package org.ing.ispw.unifix.controllergrafico;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.stage.Popup;
import javafx.stage.Window;
import org.ing.ispw.unifix.Driver;
import org.ing.ispw.unifix.bean.InfoTecnicoBean;
import org.ing.ispw.unifix.bean.NotaSegnalazioneBean;
import org.ing.ispw.unifix.bean.SegnalazioneBean;
import org.ing.ispw.unifix.controllerapplicativo.InserisciNotaSegnalazioneController;
import org.ing.ispw.unifix.controllerapplicativo.TecnicoController;
import org.ing.ispw.unifix.controllerapplicativo.VisualizzaSegnalazioniTecnicoController;
import org.ing.ispw.unifix.exception.*;

import org.ing.ispw.unifix.utils.PopUp;
import org.jetbrains.annotations.NotNull;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

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
    private final TecnicoController tc;
    private final InserisciNotaSegnalazioneController isnsc;
    PopUp popUp = new PopUp();
    private final VisualizzaSegnalazioniTecnicoController vstc;

    private static final String POPUPMESSAGGI_1 = "Errore";
    private static final String POPUPMESSAGGI_2 = "Messaggio: ";
    private static final String POPUPMESSAGGI_3 = "Riprova";
    private static final String POPUPMESSAGGI_4 = "Errore Persistenza";


    public ControllerGraficoHomeTecnico() {
        tc= new TecnicoController();
        vstc = new  VisualizzaSegnalazioniTecnicoController();
        isnsc = new InserisciNotaSegnalazioneController();

    }

    public void initialize() {
        try {
            InfoTecnicoBean infoTecnico = tc.getTecnicoInformation();
            welcome1.setText(infoTecnico.getNome() +"  "+infoTecnico.getCognome()+"  ecco i tuoi interventi");
            mostraSegnalazioniTecnico();
        }catch (PersistenceException e){
            popUp.showErrorPopup(POPUPMESSAGGI_1, POPUPMESSAGGI_4, e.getMessage());
        }catch (IllegalStateException _){
            popUp.showErrorPopup(POPUPMESSAGGI_1, "Nessun tecnico loggato", POPUPMESSAGGI_3);
        }catch (IllegalArgumentException e){
            popUp.showErrorPopup(POPUPMESSAGGI_1, "Dati tecnico non validi", e.getMessage());
        }

    }


    public void mostraSegnalazioniTecnico(){
        List<SegnalazioneBean> segnalazioni;
        try{
            segnalazioni = vstc.visualizzaSegnalazioniTecnico();
            testoSegnalazioniTecnico.setText("I tuoi interventi:");
            testoSegnalazioniTecnico.setStyle("-fx-text-fill: white");
            for (SegnalazioneBean segnalazione : segnalazioni) {
                segnalazioniContainer.getChildren().add(creaBoxSegnalazione(segnalazione));
            }
        }catch (NessunaSegnalazioneException | NessunaSegnalazioneTecnicoException _){
            testoSegnalazioniTecnico.setText("Non ci sono interventi da visualizzare");
            testoSegnalazioniTecnico.setStyle("-fx-text-fill: white");
        }
        catch (IllegalStateException _) {
            popUp.showErrorPopup(POPUPMESSAGGI_1, "Nessun tecnico loggato", POPUPMESSAGGI_3);
        }catch (PersistenceException e){
            popUp.showErrorPopup(POPUPMESSAGGI_1, POPUPMESSAGGI_4, e.getMessage());
        }

    }

    private HBox creaBoxSegnalazione(SegnalazioneBean segnalazione) {
        HBox hbox = new HBox(10);
        hbox.setSpacing(15);
        hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        hbox.setPrefHeight(108);
        hbox.setPrefWidth(1020);
        hbox.setPadding(new Insets(10));
        hbox.setStyle("-fx-background-color: #EEEEEE; -fx-border-color: #CCCCCC; -fx-border-radius: 5; -fx-background-radius: 5;");
        hbox.setOnMouseClicked(event -> apriDialogoGestioneSegnalazione(segnalazione));

        VBox dettagli = getVBox(segnalazione);
        dettagli.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        hbox.getChildren().add(dettagli);
        return hbox;
    }

    private void apriDialogoGestioneSegnalazione(SegnalazioneBean segnalazione) {
        Alert alert = getAlert(segnalazione);

        ButtonType lavorazioneButton = new ButtonType("In Lavorazione", ButtonBar.ButtonData.LEFT);
        ButtonType chiudiButton = new ButtonType("Chiudi", ButtonBar.ButtonData.LEFT);
        ButtonType noteButton = new ButtonType("Note", ButtonBar.ButtonData.LEFT);
        ButtonType cancelButton = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(lavorazioneButton, chiudiButton, noteButton, cancelButton);
        applicaStiliPulsantiDialogo(alert, lavorazioneButton, chiudiButton, noteButton, cancelButton);

        alert.showAndWait().ifPresent(response -> gestisciAzioneSegnalazione(response, segnalazione, lavorazioneButton, chiudiButton, noteButton));
    }

    private void applicaStiliPulsantiDialogo(Alert alert, ButtonType lavorazione, ButtonType chiudi, ButtonType note, ButtonType cancel) {
        impostaStilePulsante(alert, lavorazione, "-fx-background-color: #2563eb; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 6px; -fx-padding: 9px 18px; -fx-cursor: hand;");
        impostaStilePulsante(alert, chiudi, "-fx-background-color: #dc2626; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 6px; -fx-padding: 9px 18px; -fx-cursor: hand;");
        impostaStilePulsante(alert, note, "-fx-background-color: #475569; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 6px; -fx-padding: 9px 18px; -fx-cursor: hand;");
        impostaStilePulsante(alert, cancel, "-fx-background-color: #f8fafc; -fx-text-fill: #475569; -fx-border-color: #cbd5e1; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-font-size: 13px; -fx-padding: 9px 18px; -fx-cursor: hand;");
    }

    private void impostaStilePulsante(Alert alert, ButtonType buttonType, String style) {
        Button btn = (Button) alert.getDialogPane().lookupButton(buttonType);
        if (btn != null) {
            ButtonBar.setButtonUniformSize(btn, false);
            btn.setMinWidth(Region.USE_PREF_SIZE);
            btn.setStyle(style);
        }
    }

    private void gestisciAzioneSegnalazione(ButtonType response, SegnalazioneBean segnalazione, ButtonType btnLav, ButtonType btnChiudi, ButtonType btnNote) {
        if (response == btnLav) {
            eseguiInLavorazione(segnalazione.getIdSegnalazione());
        } else if (response == btnChiudi) {
            eseguiChiusura(segnalazione.getIdSegnalazione());
        } else if (response == btnNote) {
            mostraDialogoNote(segnalazione);
        } else if (response == ButtonType.CLOSE) {
            mostraSegnalazioniTecnico();
        }
    }

    private void eseguiInLavorazione(String idSegnalazione) {
        try {
            tc.inLavorazioneSegnalazione(idSegnalazione);
            popUp.showSuccessPopup("Successo", "Segnalazione presa in lavorazione");
        } catch (InvalidStateTransitionException | PersistenceException | SegnalazioneNonTrovataException e) {
            popUp.showErrorPopup(POPUPMESSAGGI_1, "Impossibile eseguire la richiesta", e.getMessage());
        }
        ricaricaSegnalazioni();
    }

    private void eseguiChiusura(String idSegnalazione) {
        boolean conferma = popUp.showConfirmationPopup(
                "Conferma Chiusura",
                "Chiusura Segnalazione",
                "Sei sicuro di voler chiudere definitivamente questa segnalazione?"
        );
        if (conferma) {
            try {
                tc.chiudiSegnalazione(idSegnalazione);
                popUp.showSuccessPopup("Successo", "Segnalazione chiusa correttamente");
            } catch (InvalidStateTransitionException | PersistenceException | SegnalazioneNonTrovataException e) {
                popUp.showErrorPopup(POPUPMESSAGGI_1, "Impossibile eseguire la richiesta", e.getMessage());
            }
            ricaricaSegnalazioni();
        }
    }

    private void ricaricaSegnalazioni() {
        segnalazioniContainer.getChildren().clear();
        mostraSegnalazioniTecnico();
    }

    @NotNull
    private static Alert getAlert(SegnalazioneBean segnalazione) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Dettagli Segnalazione");
        alert.setHeaderText("Gestione Intervento");
        alert.setGraphic(null);

        VBox card = new VBox();
        card.getStyleClass().add("summary-card");

        Label questionLabel = new Label("Cosa vuoi fare con questa segnalazione?");
        questionLabel.getStyleClass().add("summary-title");
        card.getChildren().addAll(questionLabel, new Separator());

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setPadding(new Insets(5, 0, 5, 0));
        grid.setMaxWidth(Double.MAX_VALUE);

        // Mantiene leggibili le etichette anche quando un valore, come la
        // descrizione, occupa più righe. La seconda colonna usa tutto lo
        // spazio disponibile e gestisce il wrapping del testo.
        ColumnConstraints keyColumn = new ColumnConstraints();
        keyColumn.setMinWidth(95);
        keyColumn.setPrefWidth(95);
        keyColumn.setMaxWidth(95);
        keyColumn.setHgrow(Priority.NEVER);

        ColumnConstraints valueColumn = new ColumnConstraints();
        valueColumn.setMinWidth(0);
        valueColumn.setHgrow(Priority.ALWAYS);
        valueColumn.setFillWidth(true);
        grid.getColumnConstraints().addAll(keyColumn, valueColumn);

        grid.add(creaLabelChiave("Edificio:"), 0, 0);
        grid.add(creaLabelValore(segnalazione.getEdificio()), 1, 0);

        grid.add(creaLabelChiave("Aula:"), 0, 1);
        grid.add(creaLabelValore(segnalazione.getAula()), 1, 1);

        grid.add(creaLabelChiave("Oggetto:"), 0, 2);
        grid.add(creaLabelValore(segnalazione.getOggettoGuasto()), 1, 2);

        grid.add(creaLabelChiave("Descrizione:"), 0, 3);
        grid.add(creaLabelValore(segnalazione.getDescrizione()), 1, 3);

        grid.add(creaLabelChiave("Stato:"), 0, 4);
        HBox badgeContainer = new HBox(creaBadgeStato(String.valueOf(segnalazione.getStato())));
        badgeContainer.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        grid.add(badgeContainer, 1, 4);

        if (segnalazione.getUser() != null) {
            grid.add(creaLabelChiave("Docente:"), 0, 5);
            grid.add(creaLabelValore(segnalazione.getUser().getNome() + " " + segnalazione.getUser().getCognome()), 1, 5);
        }

        card.getChildren().add(grid);
        alert.getDialogPane().setContent(card);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setMinHeight(Region.USE_PREF_SIZE);
        dialogPane.setMinWidth(640);
        dialogPane.setPrefWidth(660);
        try {
            String css = Objects.requireNonNull(ControllerGraficoHomeTecnico.class.getResource("/org/ing/ispw/unifix/dialog.css")).toExternalForm();
            dialogPane.getStylesheets().add(css);
        } catch (Exception _) {
            // Fallback
        }

        return alert;
    }

    private static Label creaLabelChiave(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("summary-key");
        label.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        return label;
    }

    private static Label creaLabelValore(String text) {
        Label label = new Label(text != null ? text : "-");
        label.getStyleClass().add("summary-val");
        label.setWrapText(true);
        label.setMinWidth(0);
        label.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(label, Priority.ALWAYS);
        label.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        return label;
    }

    private static Label creaBadgeStato(String stato) {
        Label badge = new Label(stato);
        badge.getStyleClass().add("badge-status");
        badge.setAlignment(javafx.geometry.Pos.CENTER);
        if ("APERTA".equalsIgnoreCase(stato)) {
            badge.getStyleClass().add("badge-aperta");
        } else if ("IN_LAVORAZIONE".equalsIgnoreCase(stato) || "IN LAVORAZIONE".equalsIgnoreCase(stato)) {
            badge.getStyleClass().add("badge-in-lavorazione");
        } else if ("CHIUSA".equalsIgnoreCase(stato)) {
            badge.getStyleClass().add("badge-chiusa");
        }
        return badge;
    }


    @NotNull
    private static VBox getVBox(SegnalazioneBean segnalazione) {
        Label testoLabel = new Label("Edificio: " + segnalazione.getEdificio() +
                "    Aula: " + segnalazione.getAula() +
                "    Oggetto: " + segnalazione.getOggettoGuasto()+
                "    Docente: " + segnalazione.getUser().getNome() + " " + segnalazione.getUser().getCognome());
        testoLabel.setStyle("-fx-text-fill: black; -fx-font-size: 20px; -fx-font-weight: bold; -fx-font: Segoe UI");

        // Layout per i dettagli della segnalazione
        VBox dettagli = new VBox(testoLabel);

        dettagli.setSpacing(5);
        return dettagli;
    }



    private void mostraDialogoNote(SegnalazioneBean segnalazione) {
        Dialog<String> dialog = creaDialogoNote(segnalazione);
        TextArea nuovaNotaArea = new TextArea();

        VBox content = creaContenutoDialogo(segnalazione, nuovaNotaArea);
        dialog.getDialogPane().setContent(content);

        configuraBottoniDialogo(dialog, nuovaNotaArea);

        dialog.showAndWait().ifPresent(nuovaNota -> salvaNuovaNota(segnalazione, nuovaNota));
    }

    private Dialog<String> creaDialogoNote(SegnalazioneBean segnalazione) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Note Segnalazione");
        dialog.setHeaderText("Gestione note per: " + segnalazione.getOggettoGuasto() +
                           "  in  " + segnalazione.getEdificio() +
                           "  aula  " + segnalazione.getAula());

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setMinHeight(Region.USE_PREF_SIZE);
        dialogPane.setMinWidth(480);
        try {
            String css = Objects.requireNonNull(ControllerGraficoHomeTecnico.class.getResource("/org/ing/ispw/unifix/dialog.css")).toExternalForm();
            dialogPane.getStylesheets().add(css);
        } catch (Exception _) {
            // Fallback
        }
        return dialog;
    }

    private VBox creaContenutoDialogo(SegnalazioneBean segnalazione, TextArea nuovaNotaArea) {
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        // Sezione note esistenti
        Label labelNoteEsistenti = new Label("Note esistenti:");
        TextArea noteEsistentiArea = creaNoteEsistentiArea(segnalazione);

        // Sezione nuova nota
        Label labelNuovaNota = new Label("Aggiungi nuova nota:");
        configuraNuovaNotaArea(nuovaNotaArea);

        content.getChildren().addAll(labelNoteEsistenti, noteEsistentiArea, labelNuovaNota, nuovaNotaArea);
        return content;
    }

    private TextArea creaNoteEsistentiArea(SegnalazioneBean segnalazione) {
        TextArea noteEsistentiArea = new TextArea();
        noteEsistentiArea.setEditable(false);
        noteEsistentiArea.setPrefRowCount(5);
        noteEsistentiArea.setWrapText(true);
        noteEsistentiArea.setText(formattaNoteEsistenti(segnalazione));
        return noteEsistentiArea;
    }

    private String formattaNoteEsistenti(SegnalazioneBean segnalazione) {
        try {
            List<NotaSegnalazioneBean> noteAttuali = isnsc.getNoteForSegnalazione(segnalazione.getIdSegnalazione());

            if (noteAttuali.isEmpty()) {
                return "Non ci sono note presenti.";
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            StringBuilder noteTesto = new StringBuilder();
            for (NotaSegnalazioneBean nota : noteAttuali) {
                noteTesto.append(dateFormat.format(nota.getDataCreazione().getTime()))
                        .append(": ").append(nota.getTestoNota()).append("\n");
            }
            return noteTesto.toString();
        } catch (PersistenceException e) {
            return "Errore nel recupero delle note: " + e.getMessage();
        }
    }

    private void configuraNuovaNotaArea(TextArea nuovaNotaArea) {
        nuovaNotaArea.setPromptText("Scrivi qui la nuova nota...");
        nuovaNotaArea.setPrefRowCount(3);
        nuovaNotaArea.setWrapText(true);
    }

    private void configuraBottoniDialogo(Dialog<String> dialog, TextArea nuovaNotaArea) {
        ButtonType salvaButton = new ButtonType("Salva", ButtonBar.ButtonData.OK_DONE);
        ButtonType annullaButton = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(salvaButton, annullaButton);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == salvaButton) {
                return nuovaNotaArea.getText();
            }
            return null;
        });
    }


    private void salvaNuovaNota(SegnalazioneBean segnalazione, String nuovaNota) {
        if (nuovaNota == null || nuovaNota.trim().isEmpty()) {
            popUp.showErrorPopup(POPUPMESSAGGI_1, "Nota non valida", "Il contenuto della nota è vuoto. Inserisci del testo prima di salvare.");
            return;
        }
        try {
            NotaSegnalazioneBean notaSegnalazioneBean = new NotaSegnalazioneBean();
            notaSegnalazioneBean.setIdSegnalazione(segnalazione.getIdSegnalazione());
            notaSegnalazioneBean.setTestoNota(nuovaNota.trim());
            isnsc.inserisciNotaSegnalazione(notaSegnalazioneBean);
        } catch (SegnalazioneNonTrovataException e) {
            popUp.showErrorPopup(POPUPMESSAGGI_1, "Segnalazione non trovata", POPUPMESSAGGI_2 + e.getMessage());
        } catch (TecnicoNonAssegnatoException e) {
            popUp.showErrorPopup(POPUPMESSAGGI_1, "Tecnico non assegnato", POPUPMESSAGGI_2 + e.getMessage());
        } catch (IllegalArgumentException e) {
            popUp.showErrorPopup(POPUPMESSAGGI_1, "Dati non validi", POPUPMESSAGGI_2 + e.getMessage());
        }catch (NotaStatoSegnalazioneLavorazioneException e){
            popUp.showErrorPopup(POPUPMESSAGGI_1,"Attenzione",POPUPMESSAGGI_2+e.getMessage());
        }catch (PersistenceException e){
            popUp.showErrorPopup(POPUPMESSAGGI_1, POPUPMESSAGGI_4, POPUPMESSAGGI_2+e.getMessage());
        }
    }


    @FXML
    void mostraInfoTecnico(MouseEvent event) {
        try {
            // 1. Recupera i dati dal controller applicativo
            InfoTecnicoBean infoTecnico = tc.getTecnicoInformation();

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
        }catch (IllegalArgumentException | IllegalStateException e){
            popUp.showErrorPopup(POPUPMESSAGGI_1,"Si è verificato un errore", e.getMessage());
        }

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
