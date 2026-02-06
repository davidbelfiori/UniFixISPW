package org.ing.ispw.unifix.cli;

import org.ing.ispw.unifix.controllerapplicativo.LoginController;
import org.ing.ispw.unifix.exception.UtenteNonTrovatoException;
import org.ing.ispw.unifix.utils.Printer;
import org.ing.ispw.unifix.bean.CredentialBean;
import org.ing.ispw.unifix.utils.UserType;

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

    public void loginCliHome() throws IOException {

        String email = "";
        String password = "";

        while(!quit) {

            Printer.print("******** Login ***********");
            Printer.print("\t1) Enter Email [" + email + "]");
            Printer.print("\t2) Enter Password [" + password + "]");
            Printer.print("\t3) Login");
            Printer.print("\t4) Back");
            String action = br.readLine();

            switch(action) {
                case "1":
                    Printer.print("Enter Email");
                    Printer.print("\t: ");
                    email = br.readLine();
                    break;
                case "2":
                    Printer.print("Enter Password");
                    Printer.print("\t: ");
                    password = br.readLine();
                    break;
                case "3":

                    try {
                        UserType ruolo=lc.validate(new CredentialBean(email,password));
                        switch (ruolo) {
                            case DOCENTE:
                                DocenteHomeCli docenteView = new DocenteHomeCli();
                                docenteView.docenteHome();
                                break;
                            case TECNICO:
                                TecnicoHomeCli tecnicoView = new TecnicoHomeCli();
                                tecnicoView.tecnicoHome();
                                break;

                            case SYSADMIN:
                                SysAdminHomeCli adminView= new SysAdminHomeCli();
                                adminView.adminHome();
                                break;
                            default: Printer.error("L'utente non fa parte del dominio o non ha un ruolo");
                        }
                    }catch (UtenteNonTrovatoException e){
                        Printer.error("Errore"+e.getMessage());
                        email="";
                        password="";
                    }
                    break;
                case "4":
                    return;
                default:
                    break;
            }

    }

    }
}