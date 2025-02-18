package org.ing.ispw.unifix;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.ing.ispw.unifix.cli.StartHomeViewCLI;

import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.PersistenceProvider;
import org.ing.ispw.unifix.utils.Printer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class Driver extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader=new FXMLLoader(Driver.class.getResource("login.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        primaryStage.setTitle("unifix");

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static void setPersistenceProvider(String provider) {
        for (PersistenceProvider p : PersistenceProvider.values()) {
            if (p.getName().equals(provider)) {
                try {
                    DaoFactory.setInstance(p.getDaoFactoryClass().getConstructor().newInstance());
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException
                         | IllegalAccessException e) {
                    throw new IllegalStateException("Invalid Provider");
                }
                return;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
       Printer.print("Benvenuto in UniFix!");
       setPersistenceProvider("in memory");
        //setPersistenceProvider("persistence");



        Printer.print("Scegli l'interfaccia: CLI o GUI");
        String interfaceType = scanner.nextLine();

        if (interfaceType.equalsIgnoreCase("CLI")) {
            // Inizializza la CLI
            StartHomeViewCLI cli = new StartHomeViewCLI();
            cli.start();
            
        } else if (interfaceType.equalsIgnoreCase("GUI")) {
           launch();
            System.exit(0);
        }
    }

    public void logout(Stage stage){
        //metodo che si attiva se con l'interfaccia grafica clicco sulla "x" di uscita, avverte l'utente (grazie ad
        //una finestra) che sta uscendo dal sistema e gli fa decidere se uscire oppure no
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("uscita");
        alert.setContentText("vuoi davvero uscire ? ");
        alert.setHeaderText("stai uscendo ");
        if(alert.showAndWait().get()== ButtonType.OK){
            //usciamo dall'applicazione
            stage.close();
        }
    }

}
