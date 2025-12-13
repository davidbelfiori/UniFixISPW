package org.ing.ispw.unifix.controllergrafico;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ing.ispw.unifix.Driver;
import org.ing.ispw.unifix.controllerapplicativo.GestisciSegnalazioniAdmin;
import org.ing.ispw.unifix.controllerapplicativo.SysAdminController;
import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.model.Aula;
import org.ing.ispw.unifix.utils.Answer;
import org.ing.ispw.unifix.utils.PopUp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.ing.ispw.unifix.utils.observer.Observer;

import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;


public class ControllerGraficoHomeAdmin implements Observer {
    @FXML
    private AnchorPane inserisciAule;
    @FXML
    private Label auleGestiteLabel;
    @FXML
    private Label edificiGestitiLabel;
    @FXML
    private Label segnalazioniAttiveLabel;

    @FXML
    private Label segnalazioRisolteLabel;

    public void initialize() {
        updateLabels();
    }


    PopUp popUp = new PopUp();
    private GestisciSegnalazioniAdmin gs;
    private SysAdminController sac;
    public ControllerGraficoHomeAdmin() {
        sac = SysAdminController.getInstance();
        sac.attach(this);
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
        sac.detach(this);
        FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource("login.fxml"));
        ((Node) event.getSource()).getScene().setRoot(fxmlLoader.load());
    }

    public  void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleziona un file CSV");
        fileChooser.setFileFilter(new FileNameExtensionFilter("File CSV", "csv"));

        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.getName().toLowerCase().endsWith(".csv")) {
                try {
                    if (sac.inserisciAule(selectedFile.getAbsolutePath())) {
                        popUp.showSuccessPopup(Answer.SUCCESSO.toString(), "Aule inserite correttamente");
                    } else {
                        popUp.showErrorPopup(Answer.ERRORE.toString(), "Nessuna nuova aula inserita", "Le aule potrebbero essere già presenti");
                    }
                } catch (IllegalArgumentException e){
                    popUp.showErrorPopup(Answer.ERRORE.toString(), "File non valido", e.getMessage());
                }
            }else {
                popUp.showErrorPopup(Answer.ERRORE.toString(), "", "Il file selezionato non è un file CSV");
            }

        }


    }

    public void aulePopUp() {
        AulaDao aulaDao = DaoFactory.getInstance().getAulaDao();
        List<Aula> aule = aulaDao.getAllAule();
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setTitle("Elenco Aule");
            stage.initModality(Modality.APPLICATION_MODAL);

            // Creazione della TableView
            TableView<Aula> tableView = new TableView<>();
            tableView.setPrefSize(600, 400);

            // Colonne
            TableColumn<Aula, String> colEdificio = new TableColumn<>("Edificio");
            colEdificio.setCellValueFactory(new PropertyValueFactory<>("edificio"));
            colEdificio.setPrefWidth(150);

            TableColumn<Aula, String> colIdAula = new TableColumn<>("ID Aula");
            colIdAula.setCellValueFactory(new PropertyValueFactory<>("idAula"));
            colIdAula.setPrefWidth(100);

            TableColumn<Aula, Integer> colPiano = new TableColumn<>("Piano");
            colPiano.setCellValueFactory(new PropertyValueFactory<>("piano"));
            colPiano.setPrefWidth(100);

            TableColumn<Aula, String> colOggetti = new TableColumn<>("Oggetti");
            colOggetti.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(
                            String.join(", ", cellData.getValue().getOggetti())));
            colOggetti.setPrefWidth(250);

            // Aggiunta delle colonne alla tabella
            tableView.getColumns().addAll(colEdificio, colIdAula, colPiano, colOggetti);

            // Caricamento dei dati
            ObservableList<Aula> data = FXCollections.observableArrayList(aule);
            tableView.setItems(data);

            // Creazione della scena e visualizzazione
            Scene scene = new Scene(tableView);
            stage.setScene(scene);
            stage.show();
        });

    }

    @Override
    public void update() {
        Platform.runLater(this::updateLabels);
    }

    private void updateLabels() {
        edificiGestitiLabel.setText(getEdificiGestiti());
        auleGestiteLabel.setText(getAuleGestite());
        segnalazioniAttiveLabel.setText(getNumeroSegnalazioniAttive());
        segnalazioRisolteLabel.setText(getNumeroSegnalazioniRisolte());
    }
}
