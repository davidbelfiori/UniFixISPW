package org.ing.ispw.unifix;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ing.ispw.unifix.cli.StartHomeViewCLI;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.PersistenceProvider;
import org.ing.ispw.unifix.utils.Printer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class Driver extends Application {



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

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = new FXMLLoader(this.getClass().getResource("LoginGUI.fxml")).load();

        primaryStage.setTitle("UniFix");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

    }






    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
       Printer.print("Benvenuto in UniFix!");
       setPersistenceProvider("in memory");

        Printer.print("Scegli l'interfaccia: CLI o GUI");
        String interfaceType = scanner.nextLine();

        if (interfaceType.equalsIgnoreCase("CLI")) {
            // Inizializza la CLI
            StartHomeViewCLI cli = new StartHomeViewCLI();
            cli.start();
            
        } else if (interfaceType.equalsIgnoreCase("GUI")) {
           launch(args);
        }
    }


}
