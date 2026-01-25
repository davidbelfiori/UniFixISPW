package org.ing.ispw.unifix.controllergrafico;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.ing.ispw.unifix.Driver;
import org.ing.ispw.unifix.bean.AulaBean;
import org.ing.ispw.unifix.controllerapplicativo.SysAdminController;
import org.ing.ispw.unifix.exception.AulaGiaPresenteException;
import org.ing.ispw.unifix.exception.DatiAulaNonValidiException;
import org.ing.ispw.unifix.utils.PopUp;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class ControllerGraficoGestioneAule {


    @FXML
    private VBox aulaContainer;


    @FXML
    private TextField newAulaName;

    private static final String POPUPMESSAGGI_1 = "Errore";

    PopUp popUp = new PopUp();


    SysAdminController sysAdminController;
    public ControllerGraficoGestioneAule(){
        sysAdminController = new SysAdminController();
    }


    @FXML
    public void initialize() {
       mostraAule();
    }

    public void mostraAule () {

        try {
            List<AulaBean> aule = sysAdminController.visualizzaAule();
            aulaContainer.getChildren().clear();

            // Ordina le aule per edificio
            aule.sort(Comparator.comparing(AulaBean::getEdificio));

            for (AulaBean a : aule) {
                aulaContainer.getChildren().add(creaBoxAula(a));
            }
        }catch (DatiAulaNonValidiException e){
            popUp.showErrorPopup(POPUPMESSAGGI_1, "Dati non validi", e.getMessage());
        }

    }


    private HBox creaBoxAula(AulaBean aula) {
        HBox hbox = new HBox(10);
        hbox.setSpacing(15);
        hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);


        hbox.setPrefHeight(108);
        hbox.setPrefWidth(700);
        hbox.setPadding(new Insets(10));
        hbox.setStyle("-fx-background-color: #EEEEEE; -fx-border-color: #CCCCCC; -fx-border-radius: 5; -fx-background-radius: 5;");
        hbox.setOnMouseClicked(event ->
                // Mostra i dettagli della segnalazione
                popUp.showSuccessPopup("Dettagli","Edificio: "+aula.getEdificio()+"\nAula: "+aula.getIdAula()+"\nPiano: "+aula.getPiano()+"\nOggetti: "+aula.getOggetti().toString()) );

        // Aggiungi le informazioni della segnalazione
        VBox dettagli = getVBox(aula);

        // Aggiungi tutto all'HBox
        dettagli.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        hbox.getChildren().add(dettagli);
        return hbox;
    }
    @NotNull
    private static VBox getVBox(AulaBean aula) {
        Label testoLabel = new Label("Edificio: " + aula.getEdificio() +
                "    Aula: " + aula.getIdAula()+
                "    Piano: " + aula.getPiano());
        testoLabel.setStyle("-fx-text-fill: black; -fx-font-size: 18px; -fx-font-weight: bold; -fx-font: Segoe UI");

        // Layout per i dettagli della segnalazione
        VBox dettagli = new VBox(testoLabel);
        dettagli.setSpacing(5);
        return dettagli;
    }

    public void aggiungiAula() {
        Dialog<AulaBean> dialog = creaDialogAula();
        ButtonType aggiungiButtonType = new ButtonType("Aggiungi", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(aggiungiButtonType, ButtonType.CANCEL);

        TextField idAula = new TextField();
        idAula.setPromptText("ID Aula (es. A1)");
        TextField edificio = new TextField();
        edificio.setPromptText("Edificio");
        TextField piano = new TextField();
        piano.setPromptText("Piano (numero)");
        VBox oggettiContainer = creaOggettiContainer();

        GridPane grid = creaGridLayout(idAula, edificio, piano, oggettiContainer, dialog);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton ->
            convertiRisultatoDialog(dialogButton, aggiungiButtonType, idAula, edificio, piano, oggettiContainer));

        dialog.showAndWait().ifPresent(this::processaAulaAggiunta);
    }

    private Dialog<AulaBean> creaDialogAula() {
        Dialog<AulaBean> dialog = new Dialog<>();
        dialog.setTitle("Aggiungi Nuova Aula");
        dialog.setHeaderText("Inserisci i dettagli della nuova aula");
        return dialog;
    }

    private VBox creaOggettiContainer() {
        VBox oggettiContainer = new VBox(5);
        TextField oggetto1 = new TextField();
        oggetto1.setPromptText("Oggetto");
        oggettiContainer.getChildren().add(oggetto1);
        return oggettiContainer;
    }

    private GridPane creaGridLayout(TextField idAula, TextField edificio, TextField piano,
                                     VBox oggettiContainer, Dialog<AulaBean> dialog) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        Button btnAddOggetto = new Button("+");
        btnAddOggetto.setOnAction(e -> aggiungiCampoOggetto(oggettiContainer, dialog));

        grid.add(new Label("ID Aula:"), 0, 0);
        grid.add(idAula, 1, 0);
        grid.add(new Label("Edificio:"), 0, 1);
        grid.add(edificio, 1, 1);
        grid.add(new Label("Piano:"), 0, 2);
        grid.add(piano, 1, 2);
        grid.add(new Label("Oggetti:"), 0, 3);
        grid.add(oggettiContainer, 1, 3);
        grid.add(btnAddOggetto, 2, 3);

        return grid;
    }

    private void aggiungiCampoOggetto(VBox oggettiContainer, Dialog<AulaBean> dialog) {
        TextField nuovoOggetto = new TextField();
        nuovoOggetto.setPromptText("Oggetto");
        oggettiContainer.getChildren().add(nuovoOggetto);
        dialog.getDialogPane().getScene().getWindow().sizeToScene();
    }

    private AulaBean convertiRisultatoDialog(ButtonType dialogButton, ButtonType aggiungiButtonType,
                                              TextField idAula, TextField edificio, TextField piano,
                                              VBox oggettiContainer) {
        if (dialogButton != aggiungiButtonType) {
            return null;
        }
        List<String> oggetti = raccogliOggetti(oggettiContainer);
        return creaAulaBean(idAula.getText(), edificio.getText(), piano.getText(), oggetti);
    }

    private List<String> raccogliOggetti(VBox oggettiContainer) {
        List<String> oggetti = new ArrayList<>();
        for (Node node : oggettiContainer.getChildren()) {
            if (node instanceof TextField textfield && !textfield.getText().isEmpty()) {
                oggetti.add(textfield.getText());
            }
        }
        return oggetti;
    }

    private AulaBean creaAulaBean(String idAula, String edificio, String pianoText, List<String> oggetti) {
        try {
            return new AulaBean(idAula, edificio, Integer.parseInt(pianoText), oggetti);
        } catch (DatiAulaNonValidiException e) {
            popUp.showErrorPopup(POPUPMESSAGGI_1, "Dati non validi", e.getMessage());
            return null;
        } catch (NumberFormatException _) {
            popUp.showErrorPopup(POPUPMESSAGGI_1, "Piano non valido", "Inserisci un numero valido per il piano");
            return null;
        }
    }

    private void processaAulaAggiunta(AulaBean aulaBean) {
        try {
            sysAdminController.inserisciAula(aulaBean);
            mostraAule();
            popUp.showSuccessPopup("Successo", "Aula aggiunta correttamente!");
        } catch (AulaGiaPresenteException _) {
            popUp.showErrorPopup(POPUPMESSAGGI_1, "Aula già presente", "");
        }
    }
    
    
    @FXML
    protected void logout(MouseEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource("login.fxml"));
        ((Node) event.getSource()).getScene().setRoot(fxmlLoader.load());
    }

    public void goToHomeAdmin(MouseEvent mouseEvent) throws  IOException{
        FXMLLoader fxmlLoaderrr=new FXMLLoader(getClass().getResource("/org/ing/ispw/unifix/homeAdmin.fxml"));
        ((Node) mouseEvent.getSource()).getScene().setRoot(fxmlLoaderrr.load());
    }

    public void goToSegnalazioni(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource("SegnalazioniAdmin.fxml"));
        ((Node) mouseEvent.getSource()).getScene().setRoot(fxmlLoader.load());
    }


}
