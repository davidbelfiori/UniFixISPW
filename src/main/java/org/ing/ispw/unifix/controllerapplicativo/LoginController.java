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

public class LoginController {


    private final UserDao userDao;
    private final UserFactory userFactory;

    public LoginController() {
        this.userDao = DaoFactory.getInstance().getUserDao();
        this.userFactory = new UserFactory();
    }


    public boolean register(RegistrazioneBean rb) throws IllegalArgumentException, RuoloNonTrovatoException {
        if (userDao.exists(rb.getEmail())) {
            return false;
        }
        User user = userFactory.createUser(rb.getEmail(), rb.getPassword());
        userDao.store(user);
        return true;
    }

    // NEL LoginController.java
    public UserBean validate(CredentialBean credentialBean) {
        User user = userDao.load(credentialBean.getEmail());

        if (user == null) {
            throw new UtenteNonTrovatoException(
                    "L'utente inserito non esiste"
            );
        }

        if (!user.getPassword().equals(credentialBean.getPassword())) {
            throw new PasswordErrataExecption("Email o password errata");
        }

        UserBean userBean = new UserBean();
        userBean.setEmail(user.getEmail());
        userBean.setRuolo(user.getRuolo());

        return userBean;
    }
}
