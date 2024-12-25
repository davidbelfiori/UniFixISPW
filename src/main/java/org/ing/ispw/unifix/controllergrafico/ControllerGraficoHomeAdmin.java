package org.ing.ispw.unifix.controllergrafico;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.ing.ispw.unifix.Driver;
import org.ing.ispw.unifix.controllerapplicativo.GestisciSegnalazioniAdmin;
import org.ing.ispw.unifix.controllerapplicativo.LoginController;
import org.ing.ispw.unifix.utils.PopUp;

import java.io.IOException;

public class ControllerGraficoHomeAdmin {
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

    private LoginController lc;
    PopUp popUp = new PopUp();
    private GestisciSegnalazioniAdmin gs;
    public ControllerGraficoHomeAdmin() {
        lc = LoginController.getInstance();
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

}
