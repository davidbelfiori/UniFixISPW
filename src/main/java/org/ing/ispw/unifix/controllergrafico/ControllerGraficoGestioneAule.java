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
import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.exception.AulaGiaPresenteException;
import org.ing.ispw.unifix.model.Aula;
import org.ing.ispw.unifix.utils.PopUp;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class ControllerGraficoGestioneAule {


    @FXML
    private VBox aulaContainer;


    @FXML
    private TextField newAulaName;


    PopUp popUp = new PopUp();

    AulaDao aulaDao ;
    SysAdminController sysAdminController;
    public ControllerGraficoGestioneAule(){
        aulaDao = DaoFactory.getInstance().getAulaDao();
        sysAdminController = new SysAdminController();
    }


    @FXML
    public void initialize() {
       mostraAule();
    }

    public void mostraAule () {

        List<Aula> aule = aulaDao.getAllAule();
        aulaContainer.getChildren().clear();

        // Ordina le aule per edificio
        aule.sort(Comparator.comparing(Aula::getEdificio));

        for (Aula a : aule) {
            aulaContainer.getChildren().add(creaBoxAula(a));
        }
    }


    private HBox creaBoxAula(Aula aula) {
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
    private static VBox getVBox(Aula aula) {
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
        // Creazione del Dialog
        Dialog<AulaBean> dialog = new Dialog<>();
        dialog.setTitle("Aggiungi Nuova Aula");
        dialog.setHeaderText("Inserisci i dettagli della nuova aula");

        // Configurazione dei bottoni
        ButtonType loginButtonType = new ButtonType("Aggiungi", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Creazione del layout per i campi di input
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField idAula = new TextField();
        idAula.setPromptText("ID Aula (es. A1)");
        TextField edificio = new TextField();
        edificio.setPromptText("Edificio");
        TextField piano = new TextField();
        piano.setPromptText("Piano (numero)");

        // Container per gli oggetti dinamici
        VBox oggettiContainer = new VBox(5);
        TextField oggetto1 = new TextField();
        oggetto1.setPromptText("Oggetto");
        oggettiContainer.getChildren().add(oggetto1);

        Button btnAddOggetto = new Button("+");
        btnAddOggetto.setOnAction(e -> {
            TextField nuovoOggetto = new TextField();
            nuovoOggetto.setPromptText("Oggetto");
            oggettiContainer.getChildren().add(nuovoOggetto);
            dialog.getDialogPane().getScene().getWindow().sizeToScene();
        });

        grid.add(new Label("ID Aula:"), 0, 0);
        grid.add(idAula, 1, 0);
        grid.add(new Label("Edificio:"), 0, 1);
        grid.add(edificio, 1, 1);
        grid.add(new Label("Piano:"), 0, 2);
        grid.add(piano, 1, 2);
        grid.add(new Label("Oggetti:"), 0, 3);
        grid.add(oggettiContainer, 1, 3);
        grid.add(btnAddOggetto, 2, 3);

        dialog.getDialogPane().setContent(grid);

        // Conversione del risultato in un oggetto Aula
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                AulaBean newAula = new AulaBean();
                newAula.setIdAula(idAula.getText());
                newAula.setEdificio(edificio.getText());
                try {
                    newAula.setPiano(Integer.parseInt(piano.getText()));
                } catch (NumberFormatException _) {
                    newAula.setPiano(0); // Valore di default in caso di errore
                }
                List<String> oggetti = new ArrayList<>();
                for (Node node : oggettiContainer.getChildren()) {
                    if (node instanceof TextField textfield && !textfield.getText().isEmpty()) {
                        oggetti.add(textfield.getText());
                    }
                }
                newAula.setOggetti(oggetti);
                return newAula;
            }
            return null;
        });

        Optional<AulaBean> result = dialog.showAndWait();

        result.ifPresent(aula -> {
            try {
                sysAdminController.inserisciAula(result);
                mostraAule();
                popUp.showSuccessPopup("Successo", "Aula aggiunta correttamente!");
            }catch (AulaGiaPresenteException _){
                popUp.showErrorPopup("Errore", "Aula già presente","");
                idAula.setText("");
                edificio.setText("");
                piano.setText("");
                oggettiContainer.getChildren().setAll(new TextField()); // Reset objects
            }


        });
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
}
