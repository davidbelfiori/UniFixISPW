package org.ing.ispw.controller.applicativo;

import org.ing.ispw.unifix.Driver;
import org.ing.ispw.unifix.bean.CredentialBean;
import org.ing.ispw.unifix.bean.RegistrazioneBean;
import org.ing.ispw.unifix.controllerapplicativo.LoginController;
import org.ing.ispw.unifix.exception.RuoloNonTrovatoException;
import org.ing.ispw.unifix.exception.UtenteNonTrovatoException;
import org.ing.ispw.unifix.utils.DemoData;
import org.ing.ispw.unifix.utils.UserType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

 class LoginControllerTest {

    private static LoginController loginController;

    @BeforeAll
    static void setUp() {
        Driver.setPersistenceProvider("in memory");
        DemoData.load();
        loginController = LoginController.getInstance();
    }

    // ---- TEST LOGIN ---- //

    @Test
    void testLoginUtenteNonEsistente() {

        CredentialBean credentialBean = new CredentialBean();
        credentialBean.setEmail("docente@uniroma2.eu");
        credentialBean.setPassword("password_errata");
        assertThrows(UtenteNonTrovatoException.class, () -> {
            loginController.validate(credentialBean);
        });
    }

    @Test
    void testLoginPasswordErrata() {
        CredentialBean credentialBean = new CredentialBean();
        credentialBean.setEmail("davide.falessi@uniroma2.eu");
        credentialBean.setPassword("password_errata");

        assertThrows(UtenteNonTrovatoException.class, () -> {
            loginController.validate(credentialBean);
        });
    }

    @Test
    void testLoginUtenteEsistente() throws UtenteNonTrovatoException {
        CredentialBean credentialBean = new CredentialBean();
        credentialBean.setEmail("marco.rizzo@sys.uniroma2.eu");
        credentialBean.setPassword("errata");

        assertEquals(UserType.UNKNOWN,loginController.validate(credentialBean));

    }

    @Test
    void testLoginRuoloUtenteSys() throws UtenteNonTrovatoException {
        CredentialBean credentialBean = new CredentialBean();
        credentialBean.setEmail("marco.rizzo@sys.uniroma2.eu");
        credentialBean.setPassword("admin");
        assertEquals(UserType.SYSADMIN,loginController.validate(credentialBean));
    }

    @Test
    void testLoginRuoloUtenteDocente() throws UtenteNonTrovatoException {
        CredentialBean credentialBean = new CredentialBean();
        credentialBean.setEmail("davide.falessi@uniroma2.eu");
        credentialBean.setPassword("admin");
        assertEquals(UserType.DOCENTE,loginController.validate(credentialBean));
    }

    @Test
    void testLoginRuoloUtenteTecnico() throws UtenteNonTrovatoException {
        CredentialBean credentialBean = new CredentialBean();
        credentialBean.setEmail("giuseppe.rossi@tec.uniroma2.eu");
        credentialBean.setPassword("admin");
        assertEquals(UserType.TECNICO,loginController.validate(credentialBean));
    }
    
    // ---- TEST REGISTRAZIONE ---- //

    @ParameterizedTest(name = "Registrazione {0} con email {1}")
    @CsvSource({
            "Docente, mario.rossi@uniroma2.eu, password",
            "Tecnico, luigi.verdi@tec.uniroma2.eu, password",
            "Sysadmin, anna.bianchi@sys.uniroma2.eu, password"
    })
    void testRegistrazioneSuccesso(String ruolo, String email, String password) throws RuoloNonTrovatoException {
        RegistrazioneBean rb = new RegistrazioneBean();
        rb.setEmail(email);
        rb.setPassword(password);
        assertTrue(loginController.register(rb));
    }

    @Test
    void testRegistrazioneUtenteGiaEsistente() throws RuoloNonTrovatoException {
        RegistrazioneBean rb = new RegistrazioneBean();
        rb.setEmail("marco.rizzo@sys.uniroma2.eu");
        rb.setPassword("password");
        assertFalse(loginController.register(rb));
    }


    // ---- TEST REGISTRAZIONE EMAIL NON VALIDA (Mancanza chiocciola, formato nome errato , mail null)---- //
    @ParameterizedTest(name = "Registrazione {0} con email non valida {1}")
    @CsvSource({
            "Docente, mario.rossiuniroma2.eu, password", // Manca la @
            "Tecnico, luigi@tec.uniroma2.eu, password"   // Manca il cognome
    })
    void testRegistrazioneEmailNonValidaController(String ruolo, String email, String password) {
        RegistrazioneBean rb = new RegistrazioneBean();

        // Popoliamo il Bean (i dati sono stringhe non vuote, quindi i setter passano)
        rb.setEmail(email);
        rb.setPassword(password);

        // Il controller fallirà estraendo nome/cognome/ruolo tramite EmailParserService
        assertThrows(IllegalArgumentException.class, () -> loginController.register(rb));
    }

    @Test
    void testConfirmPassword() {
        RegistrazioneBean rb = new RegistrazioneBean();
        rb.setEmail("mario.rossi@uniroma2.eu");
        rb.setPassword("password");
        rb.setConfirmPassword("password");
        assertTrue(loginController.register(rb));
    }

     @Test
     void testErrorConfirmPassword() {
         RegistrazioneBean rb = new RegistrazioneBean();
         rb.setEmail("mario.rossi@uniroma2.eu");
         rb.setPassword("password");
         assertThrows(IllegalArgumentException.class, () -> rb.setConfirmPassword("password_errata"));
     }

    @Test
    void testRegistrazioneDominioSconosciuto() {
        RegistrazioneBean rb = new RegistrazioneBean();
        rb.setEmail("mario.rossi@mat.uniroma2.eu");
        rb.setPassword("password");
        assertThrows(RuoloNonTrovatoException.class, () -> loginController.register(rb));
    }

}
