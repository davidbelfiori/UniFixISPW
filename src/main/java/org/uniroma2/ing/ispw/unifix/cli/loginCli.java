package org.uniroma2.ing.ispw.unifix.cli;

import org.uniroma2.ing.ispw.unifix.controllerApplicativo.loginController;
import org.uniroma2.ing.ispw.unifix.utils.Printer;
import org.uniroma2.ing.ispw.unifix.bean.LoginBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class loginCli {

    boolean quit;
    BufferedReader br;
    loginController lc;

    public loginCli() {
        quit = false;
        br = new BufferedReader(new InputStreamReader(System.in));
        lc=loginController.getInstance();
    }

    public void LoginCli() throws IOException {

        String email = "";
        String password = "";

        while(!quit) {

            Printer.print("******** Login ***********");
            Printer.print("\t1) Enter Username [" + email + "]");
            Printer.print("\t2) Enter Password [" + password + "]");
            Printer.print("\t3) Login");
            String action = br.readLine();

            switch(action) {
                case "1":
                    Printer.print("Enter Username");
                    Printer.print("\t: ");
                    email = br.readLine();
                    break;
                case "2":
                    Printer.print("Enter Password");
                    Printer.print("\t: ");
                    password = br.readLine();
                    break;
                case "3":
                    String val=lc.validate(new LoginBean(email,password));
                   switch (val) {
                        case "Docente":
                            docenteHomeCli docenteView = new docenteHomeCli();
                            docenteView.docenteHome();
                            break;
                        case "Tecnico":
                            tecnicoHomeCli tecnicoView = new tecnicoHomeCli();
                            tecnicoView.tecnicoHome();
                            break;

                       case "Amministratore di Sistema":
                           Printer.print("amministratore di sistema");
                        default:
                            // Handle other cases or errors
                            break;
                    }
            }

    }

    }
}