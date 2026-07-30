package org.ing.ispw.unifix.cli;

import org.ing.ispw.unifix.bean.UserBean;
import org.ing.ispw.unifix.controllerapplicativo.LoginController;
import org.ing.ispw.unifix.exception.DbConnException;
import org.ing.ispw.unifix.exception.PasswordErrataExecption;
import org.ing.ispw.unifix.exception.UtenteNonTrovatoException;
import org.ing.ispw.unifix.sessionmanager.SessionManager;
import org.ing.ispw.unifix.utils.Printer;
import org.ing.ispw.unifix.bean.CredentialBean;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class LoginCli {

    private boolean quit;
    private final BufferedReader br;
    private final LoginController lc;

    public LoginCli() {
        quit = false;
        br = new BufferedReader(new InputStreamReader(System.in));
        lc= new LoginController();
    }

    public void loginCliHome() throws IOException {

        String email = "";
        String password = "";

        while(!quit) {
            try {
                Printer.print("******** Login ***********");
                Printer.print("\t1) Enter Email [" + email + "]");
                Printer.print("\t2) Enter Password [" + password + "]");
                Printer.print("\t3) Login");
                Printer.print("\t4) Back");
                String action = br.readLine();

                switch (action) {
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
                            CredentialBean cb = new CredentialBean();
                            cb.setEmail(email);
                            cb.setPassword(password);
                            UserBean loggedUser = lc.validate(cb);
                            SessionManager.getInstance().setCurrentUser(loggedUser);
                            switch (loggedUser.getRuolo()) {
                                case DOCENTE:
                                    DocenteHomeCli docenteView = new DocenteHomeCli();
                                    docenteView.docenteHome();
                                    break;
                                case TECNICO:
                                    TecnicoHomeCli tecnicoView = new TecnicoHomeCli();
                                    tecnicoView.tecnicoHome();
                                    break;

                                case SYSADMIN:
                                    SysAdminHomeCli adminView = new SysAdminHomeCli();
                                    adminView.adminHome();
                                    break;
                                default:
                                    Printer.error("L'utente non fa parte del dominio o non ha un ruolo");
                            }
                        } catch (UtenteNonTrovatoException | IllegalArgumentException | PasswordErrataExecption e) {
                            Printer.error("Errore" + e.getMessage());
                            email = "";
                            password = "";
                        }
                        break;
                    case "4":
                        return;
                    default:
                        break;
                }
            }catch (DbConnException e){
                Printer.error("Errore di connessione al database: " + e.getMessage());
            }
            catch (IOException e) {
                Printer.error("Errore di input/output: " + e.getMessage());
            }
    }

    }
}