package org.ing.ispw.unifix.controllerapplicativo;


import org.ing.ispw.unifix.bean.CredentialBean;
import org.ing.ispw.unifix.bean.RegistrazioneBean;
import org.ing.ispw.unifix.bean.UserBean;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.exception.PasswordErrataExecption;
import org.ing.ispw.unifix.exception.RuoloNonTrovatoException;
import org.ing.ispw.unifix.exception.UtenteNonTrovatoException;
import org.ing.ispw.unifix.model.User;
import org.ing.ispw.unifix.model.UserFactory;
import org.ing.ispw.unifix.utils.EmailParserService;
import org.ing.ispw.unifix.utils.UserType;


public class LoginController {



    private final UserDao userDao;
    private final EmailParserService emailParserService;
    private final UserFactory userFactory;

    public LoginController() {
        this.userDao = DaoFactory.getInstance().getUserDao();
        this.emailParserService = new EmailParserService();
        this.userFactory = new UserFactory(emailParserService);
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

    // NEL LoginController.java
    public UserBean validate(CredentialBean credentialBean) throws UtenteNonTrovatoException , PasswordErrataExecption {
        if (userDao.exists(credentialBean.getEmail())) {
            User user = userDao.load(credentialBean.getEmail());
            if (user != null && user.getPassword().equals(credentialBean.getPassword())) {
                UserBean userBean = new UserBean();
                userBean.setEmail(user.getEmail());
                userBean.setRuolo(user.getRuolo());

                return userBean;
            }else {
                throw  new PasswordErrataExecption("Email o password errata");
            }
        } else {
            throw new UtenteNonTrovatoException("L'utente inserito non esiste");
        }
    }
}
