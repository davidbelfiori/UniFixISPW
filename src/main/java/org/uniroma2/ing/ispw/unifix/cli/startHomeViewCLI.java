package org.uniroma2.ing.ispw.unifix.cli;

import org.uniroma2.ing.ispw.unifix.utils.Printer;

import java.io.IOException;
import java.util.Scanner;

public class startHomeViewCLI {

    public void start() throws IOException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            Printer.print("1. Registrazione");
            Printer.print("2. Login");
            Printer.print("3. Esci");

            int scelta = scanner.nextInt();
            scanner.nextLine();
            if(scelta == 1){
                registrazioneCli rc=new registrazioneCli();
                rc.registrazione();
                System.exit(0);

            } else if (scelta == 2) {
                loginCli loginCli=new loginCli();
                loginCli.start();
                System.exit(0);

            } else if (scelta == 3) {
                Printer.print("addios");
                System.exit(0);

            }else {
                Printer.print("scelta non valida");
            }
        }
    }

}
