package org.ing.ispw.unifix.controllergrafico;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.ing.ispw.unifix.Driver;
import org.ing.ispw.unifix.controllerapplicativo.GestisciSegnalazioniAdminController;
import org.ing.ispw.unifix.controllerapplicativo.SysAdminController;
import org.ing.ispw.unifix.utils.Answer;
import org.ing.ispw.unifix.utils.PopUp;
import org.ing.ispw.unifix.utils.observer.Observer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;


public class ControllerGraficoHomeAdmin implements Observer {
    @FXML
    private AnchorPane inserisciAule;
    @FXML
    private Label auleGestiteLabel;
    @FXML
    private Label edificiGestitiLabel;
    @FXML
    private Label segnalazioniAttiveLabel;

    @FXML
    private Label segnalazioRisolteLabel;

    public void initialize() {
        updateLabels();
    }


    PopUp popUp = new PopUp();
    private final GestisciSegnalazioniAdminController gs;
    private final SysAdminController sac;
    public ControllerGraficoHomeAdmin() {
        sac = new  SysAdminController();
        sac.attach(this);
        gs = new GestisciSegnalazioniAdminController();
    }

    private String getAuleGestite() {
        return gs.visualizzaNumeroaule();
    }

    private String getNumeroSegnalazioniRisolte() {
        return gs.visualizzaSegnalazioniRisolteAdmin();
    }

    private String getNumeroSegnalazioniAttive() {
        return gs.visualizzaSegnalazioniAttiveAdmin();
    }

    private String getEdificiGestiti() {
        return gs.visualizzaEdificiGestiti();
    }

    @FXML
    protected void logout(MouseEvent event) throws IOException {
        sac.detach(this);
        FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource("login.fxml"));
        ((Node) event.getSource()).getScene().setRoot(fxmlLoader.load());
    }

    public  void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleziona un file CSV");
        fileChooser.setFileFilter(new FileNameExtensionFilter("File CSV", "csv"));

        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.getName().toLowerCase().endsWith(".csv")) {
                try {
                    if (sac.inserisciAule(selectedFile.getAbsolutePath())) {
                        popUp.showSuccessPopup(Answer.SUCCESSO.toString(), "Aule inserite correttamente");
                    } else {
                        popUp.showErrorPopup(Answer.ERRORE.toString(), "Nessuna nuova aula inserita", "Le aule potrebbero essere già presenti");
                    }
                } catch (IllegalArgumentException e){
                    popUp.showErrorPopup(Answer.ERRORE.toString(), "File non valido", e.getMessage());
                }
            }else {
                popUp.showErrorPopup(Answer.ERRORE.toString(), "", "Il file selezionato non è un file CSV");
            }

        }


    }



    @FXML
    void goToGestioneAule(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource("GestioneAule.fxml"));
        ((Node) event.getSource()).getScene().setRoot(fxmlLoader.load());
    }

    @Override
    public void update() {
        Platform.runLater(this::updateLabels);
    }

    private void updateLabels() {
        edificiGestitiLabel.setText(getEdificiGestiti());
        auleGestiteLabel.setText(getAuleGestite());
        segnalazioniAttiveLabel.setText(getNumeroSegnalazioniAttive());
        segnalazioRisolteLabel.setText(getNumeroSegnalazioniRisolte());
    }

    public void goToGestioneSegnalazioni(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource("SegnalazioniAdmin.fxml"));
        ((Node) mouseEvent.getSource()).getScene().setRoot(fxmlLoader.load());
    }
}
