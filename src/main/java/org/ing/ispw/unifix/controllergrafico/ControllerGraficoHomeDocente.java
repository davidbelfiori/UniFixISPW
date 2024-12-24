package org.ing.ispw.unifix.controllergrafico;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import org.ing.ispw.unifix.Driver;
import org.ing.ispw.unifix.controllerapplicativo.DocenteController;
import org.ing.ispw.unifix.controllerapplicativo.InviaSegnalazioneController;
import org.ing.ispw.unifix.controllerapplicativo.LoginController;
import org.ing.ispw.unifix.controllerapplicativo.SysAdminController;
import org.ing.ispw.unifix.model.Aula;
import org.ing.ispw.unifix.utils.PopUp;

import java.io.IOException;
import java.util.List;

public class ControllerGraficoHomeDocente {


    @FXML
    private ComboBox<String> edificioComboBox;
    @FXML
    private ComboBox<String> aulaComboBox;
    @FXML
    private ComboBox<String> oggettoComboBox;
    @FXML
    private TextArea descrizioneTextArea;
    @FXML
    private Label testoSegnalazioni;
    @FXML
    private Label welcome;
    private LoginController lc;
    PopUp popUp = new PopUp();
    private DocenteController dc;
    InviaSegnalazioneController sc;
    SysAdminController sysAdminController;
    public ControllerGraficoHomeDocente() {
        lc = LoginController.getInstance();
        dc = DocenteController.getInstance();
        sc=InviaSegnalazioneController.getInstance();
        sysAdminController = SysAdminController.getInstance();
        sysAdminController.inserisciAule("src/main/resources/utvAule.csv");
    }

    public void initialize() {
        welcome.setText(lc.getCurrentUser().getCognome() +"  "+lc.getCurrentUser().getNome());
        List<String> edifici=sc.getEdifici();
        edificioComboBox.setItems(FXCollections.observableList(edifici));
        edificioComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) { // Verifica che ci sia un valore selezionato
                aggiornaAule(newValue); // Passa l'edificio selezionato al metodo
            }
        });
       aulaComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, aulaScelta) -> {
            if (aulaScelta != null) { // Verifica che ci sia un valore selezionato
                aggiornaOggetti(aulaScelta); // Passa l'edificio selezionato al metodo
            }
        });

    }


    @FXML
    private void aggiornaAule(String edificioSelezionato) {
        List<Aula> auleEdificioSelezionato = sc.getAuleByEdificio(edificioSelezionato);
        List<String> nomiAule = auleEdificioSelezionato.stream()
                .map(Aula::getIdAula) // Supponendo che il metodo getNome() restituisca il nome dell'aula
                .toList();
        aulaComboBox.setItems(FXCollections.observableList(nomiAule));
    }
    @FXML
    private void aggiornaOggetti(String aulaScelta) {
        Aula oggettiAulaSelezionata = sc.getOggettiAula(aulaScelta);
        List<String> oggettiAula=  oggettiAulaSelezionata.getOggetti();
        oggettoComboBox.setItems(FXCollections.observableList(oggettiAula));
    }

    @FXML
    protected void logout(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource("login.fxml"));
        ((Node) event.getSource()).getScene().setRoot(fxmlLoader.load());
    }

    public void inviaSegnalazione(MouseEvent mouseEvent) {
        //da implementare
    }
}
