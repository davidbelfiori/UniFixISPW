package org.ing.ispw.unifix.controllergrafico;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.ing.ispw.unifix.bean.CredentialBean;
import org.ing.ispw.unifix.controllerapplicativo.LoginController;
import org.ing.ispw.unifix.exception.UtenteNonTrovatoException;
import org.ing.ispw.unifix.utils.PopUp;
import org.ing.ispw.unifix.utils.Printer;
import org.ing.ispw.unifix.utils.UserType;

import java.io.IOException;


public class ControllerGraficoLogin {


    @FXML
    private Label registrazioneLabel;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;
    PopUp popUp = new PopUp();
    private final LoginController lc;
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
            UserType ruolo=lc.validate(new CredentialBean(email,password));
            switch (ruolo) {
                case UNKNOWN:
                    popUp.showErrorPopup("Errore", "", "Utente non trovato");
                    break;
                case DOCENTE:
                    FXMLLoader fxmlLoaderr=new FXMLLoader(getClass().getResource("/org/ing/ispw/unifix/homeDocente.fxml"));
                    ((Node) mouseEvent.getSource()).getScene().setRoot(fxmlLoaderr.load());
                    break;
                case TECNICO:
                    FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("/org/ing/ispw/unifix/homeTecnico.fxml"));
                    ((Node) mouseEvent.getSource()).getScene().setRoot(fxmlLoader.load());
                    break;
                case SYSADMIN:
                    FXMLLoader fxmlLoaderrr=new FXMLLoader(getClass().getResource("/org/ing/ispw/unifix/homeAdmin.fxml"));
                    ((Node) mouseEvent.getSource()).getScene().setRoot(fxmlLoaderrr.load());
                    break;
                default:
                    break;
            }
        }catch (UtenteNonTrovatoException| IOException e){
            popUp.showErrorPopup("Errore","", e.getMessage());
        }
    }
}
