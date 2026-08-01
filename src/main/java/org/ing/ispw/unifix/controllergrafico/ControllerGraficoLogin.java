package org.ing.ispw.unifix.controllergrafico;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.ing.ispw.unifix.bean.CredentialBean;
import org.ing.ispw.unifix.bean.UserBean;
import org.ing.ispw.unifix.controllerapplicativo.LoginController;
import org.ing.ispw.unifix.exception.DbConnException;
import org.ing.ispw.unifix.exception.PasswordErrataExecption;
import org.ing.ispw.unifix.exception.UtenteNonTrovatoException;
import org.ing.ispw.unifix.sessionmanager.SessionManager;
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
    private final LoginController lc;
    public ControllerGraficoLogin() {
        this.lc= new LoginController();
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
            CredentialBean cb = new CredentialBean();
            cb.setEmail(email);
            cb.setPassword(password);
            // se la password o l'utente non esiste verrà sollevata un eccezzione
            UserBean loggedUser = lc.validate(cb);
            SessionManager.getInstance().setCurrentUser(loggedUser);
            switch (loggedUser.getRuolo()) {
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
                    popUp.showErrorPopup("Attenzione", "", "Ruolo non valido");
                    break;
            }
        }catch (IOException | IllegalArgumentException | UtenteNonTrovatoException | PasswordErrataExecption e){
            popUp.showErrorPopup("Errore","", e.getMessage());
        }catch (DbConnException e){
            popUp.showErrorPopup("Errore","", "Errore di connessione al database: " + e.getMessage());
        }
    }
}
