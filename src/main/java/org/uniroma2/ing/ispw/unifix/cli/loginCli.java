package org.uniroma2.ing.ispw.unifix.cli;

import org.uniroma2.ing.ispw.unifix.utils.Printer;
import org.uniroma2.ing.ispw.unifix.bean.LoginBean;

import java.util.Scanner;

public class loginCli {

    public void start() {
        Scanner scanner = new Scanner(System.in);

        Printer.print("Inserisci la tua:");
        String email = scanner.nextLine();

        Printer.print("Inserisci la tua password:");
        String password = scanner.nextLine();

        LoginBean loginBean=new LoginBean(email,password);

        //loginController lc= new loginController();

        //lc.
        // Pass the loginBean to the next step (e.g., authentication process)
        // authenticate(loginBean);
    }
}