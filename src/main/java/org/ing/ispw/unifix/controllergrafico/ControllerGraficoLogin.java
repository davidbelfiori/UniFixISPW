package org.ing.ispw.unifix.controllergrafico;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerGraficoLogin {

    @FXML
    private Label handleToRegistrazione;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;


    public void handleToRegistrazione(MouseEvent mouseEvent){
        try {
            // Carica la scena di registrazione
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signUpGui.fxml"));
            Parent root = loader.load();

            // Ottieni la finestra corrente e imposta la nuova scena
            Stage stage = (Stage) ((javafx.scene.Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


//    public void validateLogin(MouseEvent mouseEvent) {
//
//
//
//
//    }


}
