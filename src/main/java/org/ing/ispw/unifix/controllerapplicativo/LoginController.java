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


    /**
     * Registra un nuovo utente nel sistema. Viene verificato a monte se l'utente è presente nel sistema, se è presente ritorna false, altrimenti crea un nuovo utente e lo salva nel database.
     * Viene creato un utente attraverso l'uso della factory che in base al ruolo (estrapolato da una classe di supporto) crea un oggetto di tipo User con il ruolo corretto.
     * @return true se l'utente è stato registrato con successo, false se l'utente esiste già nel sistema.
     * @throws IllegalArgumentException se i dati forniti non sono validi (ad esempio email o password vuote).
     * @throws RuoloNonTrovatoException se il ruolo dell'utente non è valido o non è stato specificato.
     * @param rb Bean contenente le informazioni di registrazione dell'utente.
     * @throws org.ing.ispw.unifix.exception.PersistenceException se si verifica un errore durante l'accesso ai dati.
     * */

    public boolean register(RegistrazioneBean rb) throws IllegalArgumentException, RuoloNonTrovatoException {
        if (userDao.exists(rb.getEmail())) {
            return false;
        }
        User user = userFactory.createUser(rb.getEmail(), rb.getPassword());
        userDao.store(user);
        return true;
    }

    /**
     * Valida le credenziali di accesso di un utente. Se l'utente esiste e la password è corretta, viene restituito un oggetto UserBean contenente le informazioni dell'utente.
     * @param credentialBean Bean contenente le credenziali dell'utente (email e password).
     * @return UserBean contenente le informazioni dell'utente se le credenziali sono valide.
     * @throws UtenteNonTrovatoException se l'utente non esiste nel sistema.
     * @throws PasswordErrataExecption se la password fornita non corrisponde a quella memorizzata per l'utente.
     * @throws org.ing.ispw.unifix.exception.PersistenceException se si verifica un errore durante l'accesso ai dati.
     */
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
