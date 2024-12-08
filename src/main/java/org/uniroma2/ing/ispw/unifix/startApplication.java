package org.uniroma2.ing.ispw.unifix;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.uniroma2.ing.ispw.unifix.cli.startHomeViewCLI;
import org.uniroma2.ing.ispw.unifix.dao.DatabaseInterface;
import org.uniroma2.ing.ispw.unifix.factory.DatabaseFactory;
import org.uniroma2.ing.ispw.unifix.utils.Printer;

import java.util.Scanner;

public class startApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("LoginGUI.fxml"));

        primaryStage.setTitle("UniFix");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

    }




    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
       Printer.print("Benvenuto in UniFix!");
        Printer.print("Scegli la modalità: demo o persistente");
        String mode = scanner.nextLine();

        DatabaseInterface db = DatabaseFactory.getDatabase(mode);

        Printer.print("Scegli l'interfaccia: CLI o GUI");
        String interfaceType = scanner.nextLine();

        if (interfaceType.equalsIgnoreCase("CLI")) {
            // Inizializza la CLI
            startHomeViewCLI cli = new startHomeViewCLI();
            cli.start();
            //TODO: non funziona la visualizzazione della pagina
        } else if (interfaceType.equalsIgnoreCase("GUI")) {
           launch(args);
        }
    }


}
