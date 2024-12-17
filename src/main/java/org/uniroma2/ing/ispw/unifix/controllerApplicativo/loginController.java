package org.uniroma2.ing.ispw.unifix.controllerApplicativo;

import javafx.scene.control.TextField;
import org.uniroma2.ing.ispw.unifix.bean.LoginBean;
import org.uniroma2.ing.ispw.unifix.bean.RegistrazioneBean;
import org.uniroma2.ing.ispw.unifix.dao.DaoFactory;
import org.uniroma2.ing.ispw.unifix.dao.UserDao;
import org.uniroma2.ing.ispw.unifix.model.User;

import java.util.regex.Pattern;

public class loginController {

    private static loginController instance;

    private User currentUser;

    private loginController() {}

    public static loginController getInstance() {
        if (instance == null) {
            instance = new loginController();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }


    private String extractNome(String email) {
        String localPart = getLocalPart(email);
        String[] nameParts = localPart.split("\\.");
        if (nameParts.length != 2) {
            throw new IllegalArgumentException("Formato del nome non valido");
        }
        return capitalize(nameParts[0]);
    }

    private String extractCognome(String email) {
        String localPart = getLocalPart(email);
        String[] nameParts = localPart.split("\\.");
        if (nameParts.length != 2) {
            throw new IllegalArgumentException("Formato del cognome non valido");
        }
        return capitalize(nameParts[1]);
    }

    // Estrae il ruolo in base al dominio dell'email
    private String extractRuolo(String email) {
        String dominio = getDomainPart(email);
        return switch (dominio) {
            case "uniroma2.eu" -> "Docente";
            case "tec.uniroma2.eu" -> "Tecnico";
            case "sys.uniroma2.eu" -> "Amministratore di Sistema";
            default -> throw new IllegalArgumentException("Dominio non riconosciuto");
        };
    }

    // Metodo di supporto per ottenere la parte locale dell'email
    private String getLocalPart(String email) {
        validateEmail(email);
        return email.split("@")[0];
    }

    // Metodo di supporto per ottenere la parte di dominio dell'email
    private String getDomainPart(String email) {
        validateEmail(email);
        return email.split("@")[1];
    }

    // Metodo per validare l'email
    private void validateEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email non valida");
        }
    }


    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public  boolean register(RegistrazioneBean rb){

        UserDao userDao = DaoFactory.getInstance().getUserDao();
        if(!userDao.exists(rb.getEmail())){
            User user = userDao.create(rb.getEmail());
            user.setNome(extractNome(rb.getEmail()));
            user.setCognome(extractCognome(rb.getEmail()));
            user.setEmail(rb.getEmail());
            user.setPassword(rb.getPassword());
            user.setRuolo(extractRuolo(rb.getEmail()));
            userDao.store(user);
            return true;
        }
        return false;
    }

    public String validate(LoginBean loginBean) {
        UserDao userDao = DaoFactory.getInstance().getUserDao();

        if(userDao.exists(loginBean.getEmail())){
            User user =userDao.load(loginBean.getEmail());
            if(user != null || user.getPassword().equals(loginBean.getPassword())){
                currentUser=user;
                return user.getRuolo();
            }
        }

        return "";
    }
}
