package org.uniroma2.ing.ispw.unifix.cli;

import org.uniroma2.ing.ispw.unifix.utils.Printer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class startHomeViewCLI {

    boolean quit;
    BufferedReader br;

    public startHomeViewCLI() {
        quit=false;
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    public void start() throws IOException {

        while (!quit) {
            Printer.print("\t1) Login");
            Printer.print("\t2) Register");
            Printer.print("\t3) Quit");

                String action= br.readLine();

                switch (action) {
                    case "1":
                        loginCli loginCli = new loginCli();
                        loginCli.LoginCli();
                        break;
                    case "2":
                        registrazioneCli rc = new registrazioneCli();
                        rc.registrazione();
                        break;
                    case "3":
                        Printer.print("addios");
                        quit=true;
                        break;
                    default:
                        Printer.print("scelta non valida");
                        break;
                }


        }
    }

}
