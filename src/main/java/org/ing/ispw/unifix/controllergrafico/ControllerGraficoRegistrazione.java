package org.ing.ispw.unifix.controllergrafico;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.ing.ispw.unifix.bean.RegistrazioneBean;
import org.ing.ispw.unifix.controllerapplicativo.LoginController;
import org.ing.ispw.unifix.exception.RuoloNonTrovatoException;
import org.ing.ispw.unifix.utils.PopUp;
import org.ing.ispw.unifix.utils.Printer;



public class ControllerGraficoRegistrazione {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    private final LoginController lc;
    private  static final String ERRORE = "Errore";
    PopUp popUp = new PopUp();
    public ControllerGraficoRegistrazione() {

        lc = new LoginController();
    }

    public void validateRegistrazione() {

        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            popUp.showErrorPopup(ERRORE, "Dati Mancanti", "Inserire tutti i campi");
        } else if (!password.equals(confirmPassword)) {
            popUp.showErrorPopup(ERRORE, "Password non corrispondenti", "Le password non corrispondono");
        } else {
            try {
                RegistrazioneBean bean = new RegistrazioneBean();
                bean.setEmail(email);
                bean.setPassword(password);
                bean.setConfirmPassword(confirmPassword);
                if (lc.register(bean)) {
                    popUp.showSuccessPopup("Successo", "Registrazione avvenuta con successo");
                    clearFields();
                } else {
                    popUp.showErrorPopup(ERRORE, "Registrazione fallita", "Email già registrata");
                }
            } catch (IllegalArgumentException e) {
                popUp.showErrorPopup(ERRORE, "Formato email non valido", e.getMessage());
            } catch (RuoloNonTrovatoException e) {
                popUp.showErrorPopup(ERRORE, "Dominio email non riconosciuto", e.getMessage());
            }
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
