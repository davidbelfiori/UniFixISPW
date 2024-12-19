package org.ing.ispw.unifix.controllerapplicativo;


import org.ing.ispw.unifix.bean.LoginBean;
import org.ing.ispw.unifix.bean.RegistrazioneBean;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.model.Docente;
import org.ing.ispw.unifix.model.Sysadmin;
import org.ing.ispw.unifix.model.Tecnico;
import org.ing.ispw.unifix.model.User;



public class LoginController {

    private static LoginController instance;

    private User currentUser;

    private LoginController() {}

    public static LoginController getInstance() {
        if (instance == null) {
            instance = new LoginController();
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
            String ruolo = extractRuolo(rb.getEmail());

            switch (ruolo) {
                case "Docente" -> {
                    Docente docente = new Docente(rb.getEmail());
                    docente.setNome(extractNome(rb.getEmail()));
                    docente.setCognome(extractCognome(rb.getEmail()));
                    docente.setEmail(rb.getEmail());
                    docente.setPassword(rb.getPassword());
                    docente.setRuolo(extractRuolo(rb.getEmail()));
                    userDao.store(docente);
                    return true;
                }
                case "Tecnico" -> {
                    Tecnico tec = new Tecnico(rb.getEmail());
                    tec.setNome(extractNome(rb.getEmail()));
                    tec.setCognome(extractCognome(rb.getEmail()));
                    tec.setEmail(rb.getEmail());
                    tec.setPassword(rb.getPassword());
                    tec.setRuolo(extractRuolo(rb.getEmail()));
                    tec.setNumeroSegnalazioni(0);
                    userDao.store(tec);
                    return true;
                }
                case "Amministratore di Sistema" -> {
                    Sysadmin sysadmin=new Sysadmin(rb.getEmail());
                    sysadmin.setNome(extractNome(rb.getEmail()));
                    sysadmin.setCognome(extractCognome(rb.getEmail()));
                    sysadmin.setEmail(rb.getEmail());
                    sysadmin.setPassword(rb.getPassword());
                    sysadmin.setRuolo(extractRuolo(rb.getEmail()));
                    userDao.store(sysadmin);
                    return true;
                }
                default -> {
                    return false;
                }
            }

        }
        return false;
    }

    public int validate(LoginBean loginBean) {
        UserDao userDao = DaoFactory.getInstance().getUserDao();

        if(userDao.exists(loginBean.getEmail())){
            User user =userDao.load(loginBean.getEmail());
            if(user != null || user.getPassword().equals(loginBean.getPassword())){
                currentUser=user;
                switch (user) {
                    case Docente docente -> {
                        return 1;
                    }
                    case Tecnico tecnico -> {
                        return 2;
                    }
                    case Sysadmin sysadmin -> {
                        return 3;
                    }
                    default -> { return 0;
                    }
                }
            }
        }

        return 0;
    }
}
