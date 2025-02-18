package org.ing.ispw.unifix.controllergrafico;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.ing.ispw.unifix.Driver;
import org.ing.ispw.unifix.controllerapplicativo.GestisciSegnalazioniAdmin;
import org.ing.ispw.unifix.controllerapplicativo.SysAdminController;
import org.ing.ispw.unifix.utils.PopUp;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;

public class ControllerGraficoHomeAdmin {
    public AnchorPane InserisciAule;
    @FXML
    private Label auleGestiteLabel;
    @FXML
    private Label edificiGestitiLabel;
    @FXML
    private Label segnalazioniAttiveLabel;

    @FXML
    private Label segnalazioRisolteLabel;

    public void initialize() {
        edificiGestitiLabel.setText(getEdificiGestiti());
        auleGestiteLabel.setText(getAuleGestite());
        segnalazioniAttiveLabel.setText(getNumeroSegnalazioniAttive());
        segnalazioRisolteLabel.setText(getNumeroSegnalazioniRisolte());
    }


    PopUp popUp = new PopUp();
    private GestisciSegnalazioniAdmin gs;
    private SysAdminController sac;
    public ControllerGraficoHomeAdmin() {
        sac = SysAdminController.getInstance();
        gs = GestisciSegnalazioniAdmin.getInstance();
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
        FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource("login.fxml"));
        ((Node) event.getSource()).getScene().setRoot(fxmlLoader.load());
    }

    public  void ChooseFile(MouseEvent mouseEvent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleziona un file CSV");
        fileChooser.setFileFilter(new FileNameExtensionFilter("File CSV", "csv"));

        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.getName().toLowerCase().endsWith(".csv")) {
                sac.inserisciAule(selectedFile.getAbsolutePath());
            }else {
                popUp.showErrorPopup("Errore", "", "Il file selezionato non è un file CSV");
            }

        }


    }
}
