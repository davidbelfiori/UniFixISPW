package org.ing.ispw.unifix.cli;

import org.ing.ispw.unifix.bean.RegistrazioneBean;
import org.ing.ispw.unifix.controllerapplicativo.LoginController;
import org.ing.ispw.unifix.exception.RuoloNonTrovatoException;
import org.ing.ispw.unifix.utils.Printer;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RegistrazioneCli {

    boolean quit;
    BufferedReader br;
    LoginController lc;

    public RegistrazioneCli() {
        quit = false;
        br = new BufferedReader(new InputStreamReader(System.in));
        lc = LoginController.getInstance();
    }

    public void registrazione() throws IOException {

        String email = "";
        String password = "";
        String confirmPassword= "";

        while (!quit) {

            Printer.print("Register");
            Printer.print("\t1) Enter Email [" + email + "]");
            Printer.print("\t2) Enter Password [" + password + "]");
            Printer.print("\t3) Enter Password [" + confirmPassword + "]");
            Printer.print("\t4) Resgisrtami");
            Printer.print("\t5) Back");
            Printer.print(": ");

            String action = br.readLine();
            switch (action){
                case "1":
                    Printer.print("Enter email");
                    Printer.print("\t: ");
                    email = br.readLine();
                    break;
                case "2":
                    Printer.print("Enter Password");
                    Printer.print("\t: ");
                    password = br.readLine();
                    break;
                case "3":
                    Printer.print("Enter Confirm Password");
                    Printer.print("\t: ");
                    confirmPassword = br.readLine();
                    break;
                case "4":

                        try {
                            if (lc.register(new RegistrazioneBean(email,password))&& password.equals(confirmPassword)){
                                Printer.print("Registration Successful");
                                return;
                            }else{ Printer.print("Registration unsuccessful");}
                        }catch (IllegalArgumentException | RuoloNonTrovatoException e){
                            Printer.error("Errore"+e.getMessage());
                            email="";
                            password="";
                            confirmPassword="";
                        }
                    break;
                case "5":
                    return;
                default:
                    break;

            }

        }

    }

}