package org.ing.ispw.unifix.controllergrafico;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.ing.ispw.unifix.Driver;
import org.ing.ispw.unifix.controllerapplicativo.DocenteController;
import org.ing.ispw.unifix.controllerapplicativo.LoginController;
import org.ing.ispw.unifix.utils.PopUp;

import java.io.IOException;

public class ControllerGraficoHomeDocente {
    @FXML
    private Label welcome;
    private LoginController lc;
    PopUp popUp = new PopUp();
    private DocenteController dc;
    public ControllerGraficoHomeDocente() {

        lc = LoginController.getInstance();
        dc = DocenteController.getInstance();
    }

    public void initialize() {
        welcome.setText("Ciao Docente" +lc.getCurrentUser().getCognome() +lc.getCurrentUser().getNome());
    }



    @FXML
    protected void logout(MouseEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource("login.fxml"));
        ((Node) event.getSource()).getScene().setRoot(fxmlLoader.load());
    }
}
