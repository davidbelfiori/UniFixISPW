package org.ing.ispw.unifix.controllergrafico;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.ing.ispw.unifix.Driver;
import org.ing.ispw.unifix.controllerapplicativo.LoginController;
import org.ing.ispw.unifix.controllerapplicativo.TecnicoController;
import org.ing.ispw.unifix.utils.PopUp;

import java.io.IOException;

public class ControllerGraficoHomeTecnico {
    @FXML
    private Label welcome;
    private LoginController lc;
    PopUp popUp = new PopUp();
    private TecnicoController tc;
    public ControllerGraficoHomeTecnico() {

        lc = LoginController.getInstance();
        tc = TecnicoController.getInstance();
    }

    public void initialize() {
        welcome.setText("Ciao " + lc.getCurrentUser().getNome()+" ecco i tuoi interventi");
    }



    @FXML
    protected void logout(MouseEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource("login.fxml"));
        ((Node) event.getSource()).getScene().setRoot(fxmlLoader.load());
    }

}
