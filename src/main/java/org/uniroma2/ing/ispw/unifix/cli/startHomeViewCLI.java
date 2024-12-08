package org.uniroma2.ing.ispw.unifix.cli;

import org.uniroma2.ing.ispw.unifix.utils.Printer;

import java.util.Scanner;

public class startHomeViewCLI {

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            Printer.print("1. Registrazione");
            Printer.print("2. Login");
            Printer.print("3. Esci");

            int scelta = scanner.nextInt();
            scanner.nextLine();
            if(scelta == 1){
                Printer.print("hai scelto la registrazione");
                System.exit(0);

            } else if (scelta == 2) {
                Printer.print("hai scelto il login");
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
