package org.ing.ispw.unifix.controllergrafico;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.ing.ispw.unifix.bean.RegistrazioneBean;
import org.ing.ispw.unifix.controllerapplicativo.LoginController;
import org.ing.ispw.unifix.utils.PopUp;
import org.ing.ispw.unifix.utils.Printer;



public class ControllerGraficoRegistrazione {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    private  LoginController lc;
    PopUp popUp = new PopUp();
    public ControllerGraficoRegistrazione() {

        lc = LoginController.getInstance();
    }

    public void validateRegistrazione(MouseEvent mouseEvent) {

        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            popUp.showErrorPopup("Errore", "Dati Mancanti", "Inserire tutti i campi");
        } else if (!password.equals(confirmPassword)) {
            popUp.showErrorPopup("Errore", "Password non corrispondenti", "Le password non corrispondono");
        }
        else if ( lc.register(new RegistrazioneBean(email, password))) {
            popUp.showSuccessPopup("Successo", "Registrazione avvenuta con successo");
            clearFields();
        } else {
            popUp.showErrorPopup("Errore", "Registrazione fallita", "Email già registrata");
        }


    }

    private void clearFields() {
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }
    public void handleGoToLogin(MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/org/ing/ispw/unifix/login.fxml"));
            ((Node) mouseEvent.getSource()).getScene().setRoot(fxmlLoader.load());
        } catch (Exception e) {
            Printer.error(e.getMessage());
        }
    }
}
