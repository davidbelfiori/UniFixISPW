package org.ing.ispw.unifix.cli;

import org.ing.ispw.unifix.controllerapplicativo.LoginController;
import org.ing.ispw.unifix.utils.Printer;
import org.ing.ispw.unifix.bean.LoginBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginCli {

    boolean quit;
    BufferedReader br;
    LoginController lc;

    public LoginCli() {
        quit = false;
        br = new BufferedReader(new InputStreamReader(System.in));
        lc= LoginController.getInstance();
    }

    public void LoginCli() throws IOException {

        String email = "";
        String password = "";

        while(!quit) {

            Printer.print("******** Login ***********");
            Printer.print("\t1) Enter Username [" + email + "]");
            Printer.print("\t2) Enter Password [" + password + "]");
            Printer.print("\t3) Login");
            Printer.print("\t4) Back");
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
                            DocenteHomeCli docenteView = new DocenteHomeCli();
                            docenteView.docenteHome();
                            break;
                        case "Tecnico":
                            TecnicoHomeCli tecnicoView = new TecnicoHomeCli();
                            tecnicoView.tecnicoHome();
                            break;

                       case "Amministratore di Sistema":
                           Printer.print("amministratore di sistema");
                        default:
                            // Handle other cases or errors
                            break;
                    }
                case "4":
                    return;
                default:
                    break;
            }

    }

    }
}