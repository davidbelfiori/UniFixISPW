package org.ing.ispw.unifix;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.ing.ispw.unifix.cli.StartHomeViewCLI;
import org.ing.ispw.unifix.utils.Printer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Scanner;

public class Driver extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader=new FXMLLoader(Driver.class.getResource("login.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        primaryStage.setTitle("unifix");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Photo/logo2.png"))));

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Printer.print("Benvenuto in UniFix!");

        /*
        Per rispettare il principio delle Protected Variations e il disaccoppiamento dei dettagli di deployment,
        la scelta del meccanismo di persistenza è stata esternalizzata nel file di configurazione
        application.properties. In questo modo, l'applicazione può essere migrata da un database MariaDB
         a un file system JSON o a una memoria RAM semplicemente modificando il file di properties,
         senza modificare né ricompilare il codice sorgente. La DaoFactory agisce come Singleton
         polimorfico auto-configurante,
        */
        Printer.print("Scegli l'interfaccia: CLI o GUI");
        String interfaceType = scanner.nextLine();

        while (!interfaceType.equalsIgnoreCase("CLI") && !interfaceType.equalsIgnoreCase("GUI")) {
            Printer.print("Scelta interfaccia non valida. Scegli: CLI o GUI");
            interfaceType = scanner.nextLine();
        }

        if (interfaceType.equalsIgnoreCase("CLI")) {
            // Inizializza la CLI
            StartHomeViewCLI cli = new StartHomeViewCLI();
            cli.start();

        } else if (interfaceType.equalsIgnoreCase("GUI")) {
           launch(args);
            System.exit(0);
        } else {
            Printer.print("Scelta interfaccia non valida. Uscita.");
        }
    }

    public void logout(Stage stage){
        //metodo che si attiva se con l'interfaccia grafica clicco sulla "x" di uscita, avverte l'utente (grazie ad
        //una finestra) che sta uscendo dal sistema e gli fa decidere se uscire oppure no
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("uscita");
        alert.setContentText("vuoi davvero uscire ? ");
        alert.setHeaderText("stai uscendo ");

        // The result of showAndWait() is an Optional<ButtonType>.
        // We should check if a value is present before calling get().
        if (alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent()) {
            //usciamo dall'applicazione
            stage.close();
        }
    }
}
