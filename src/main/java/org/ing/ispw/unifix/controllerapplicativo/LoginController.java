package org.ing.ispw.unifix.controllerapplicativo;


import org.ing.ispw.unifix.bean.CredentialBean;
import org.ing.ispw.unifix.bean.RegistrazioneBean;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.exception.RuoloNonTrovatoException;
import org.ing.ispw.unifix.exception.UtenteNonTrovatoException;
import org.ing.ispw.unifix.model.User;
import org.ing.ispw.unifix.model.UserFactory;
import org.ing.ispw.unifix.utils.EmailParserService;
import org.ing.ispw.unifix.utils.UserType;


public class LoginController {

    private static LoginController instance;

    private User currentUser;

    private final UserDao userDao;
    private final EmailParserService emailParserService;
    private final UserFactory userFactory;

    private LoginController() {
        this.userDao = DaoFactory.getInstance().getUserDao();
        this.emailParserService = new EmailParserService();
        this.userFactory = new UserFactory(emailParserService);
    }

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


    public  boolean register(RegistrazioneBean rb) throws IllegalArgumentException,RuoloNonTrovatoException{
        if(userDao.exists(rb.getEmail())){
            return false;
        }

        UserType ruolo = emailParserService.extractRuolo(rb.getEmail());
        if(ruolo == UserType.UNKNOWN) {
            return false;
        }

        User user = userFactory.createUser(rb.getEmail(), rb.getPassword(), ruolo);
        userDao.store(user);
        return true;
    }

    public UserType validate(CredentialBean credentialBean) throws UtenteNonTrovatoException{
        if(userDao.exists(credentialBean.getEmail())){
            User user =userDao.load(credentialBean.getEmail());
            if(user != null && user.getPassword().equals(credentialBean.getPassword())){
                currentUser=user;
                return user.getRuolo();
            }
        }else {
            throw new UtenteNonTrovatoException("L'utente inserito non esiste");
        }

        return UserType.UNKNOWN;
    }
}
