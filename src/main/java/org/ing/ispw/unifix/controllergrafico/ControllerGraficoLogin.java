package org.ing.ispw.unifix.controllergrafico;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.ing.ispw.unifix.bean.LoginBean;
import org.ing.ispw.unifix.controllerapplicativo.LoginController;
import org.ing.ispw.unifix.exception.UtenteNonTrovatoException;
import org.ing.ispw.unifix.utils.PopUp;
import org.ing.ispw.unifix.utils.Printer;

import java.io.IOException;


public class ControllerGraficoLogin {


    @FXML
    private Label registrazioneLabel;


    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;
    PopUp popUp = new PopUp();
    private LoginController lc;
    public ControllerGraficoLogin() {
        lc= LoginController.getInstance();
    }

    public void handleToRegistrazione(javafx.scene.input.MouseEvent mouseEvent){
        try {
           FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/org/ing/ispw/unifix/SignUP.fxml"));
            ((Node) mouseEvent.getSource()).getScene().setRoot(fxmlLoader.load());
        } catch (Exception e) {
            Printer.error(e.getMessage());
        }

    }


    public void validateLogin(MouseEvent mouseEvent) {
        String email = emailField.getText();
        String password = passwordField.getText();
        try {
            int val=lc.validate(new LoginBean(email,password));
            switch (val) {
                case 1:
                    FXMLLoader fxmlLoaderr=new FXMLLoader(getClass().getResource("/org/ing/ispw/unifix/homeDocente.fxml"));
                    ((Node) mouseEvent.getSource()).getScene().setRoot(fxmlLoaderr.load());
                    break;
                case 2:
                    FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/org/ing/ispw/unifix/homeTecnico.fxml"));
                    ((Node) mouseEvent.getSource()).getScene().setRoot(fxmlLoader.load());
                    break;
                case 3:
                    popUp.showSuccessPopup("Successo", "Login effettuato con successo ciao amministratore"+lc.getCurrentUser().getNome()+" "+lc.getCurrentUser().getCognome());
                    break;
                default:
                    break;
            }
        }catch (UtenteNonTrovatoException| IOException e){
            popUp.showErrorPopup("Errore","", e.getMessage());
        }
    }
}
