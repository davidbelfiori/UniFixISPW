package org.ing.ispw.unifix.cli;

import org.ing.ispw.unifix.utils.Printer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class StartHomeViewCLI {

    boolean quit;
    BufferedReader br;

    public StartHomeViewCLI() {
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
                        LoginCli loginCli = new LoginCli();
                        loginCli.loginCliHome();
                        break;
                    case "2":
                        RegistrazioneCli rc = new RegistrazioneCli();
                        rc.registrazione();
                        break;
                    case "3":
                        Printer.print("addios");
                        System.exit(0);
                        break;
                    default:
                        Printer.print("scelta non valida");
                        break;
                }


        }
    }

}
