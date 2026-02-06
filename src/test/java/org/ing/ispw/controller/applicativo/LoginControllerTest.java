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
        CredentialBean credentialBean = new CredentialBean("utente.inesistente@uniroma2.eu", "password123");

        assertThrows(UtenteNonTrovatoException.class, () -> {
            loginController.validate(credentialBean);
        });
    }

    @Test
    void testLoginPasswordErrata() {
        CredentialBean credentialBean = new CredentialBean("docente@uniroma2.eu", "password_errata");

        assertThrows(UtenteNonTrovatoException.class, () -> {
            loginController.validate(credentialBean);
        });
    }

    @Test
    void testLoginUtenteEsistente() throws UtenteNonTrovatoException {
        CredentialBean credentialBean = new CredentialBean("marco.rizzo@sys.uniroma2.eu", "errata");
        assertEquals(UserType.UNKNOWN,loginController.validate(credentialBean));

    }

    @Test
    void testLoginRuoloUtenteSys() throws UtenteNonTrovatoException {
        CredentialBean credentialBean = new CredentialBean("marco.rizzo@sys.uniroma2.eu", "admin");
        assertEquals(UserType.SYSADMIN,loginController.validate(credentialBean));
    }

    @Test
    void testLoginRuoloUtenteDocente() throws UtenteNonTrovatoException {
        CredentialBean credentialBean = new CredentialBean("davide.falessi@uniroma2.eu", "admin");
        assertEquals(UserType.DOCENTE,loginController.validate(credentialBean));
    }

    @Test
    void testLoginRuoloUtenteTecnico() throws UtenteNonTrovatoException {
        CredentialBean credentialBean = new CredentialBean("giuseppe.rossi@tec.uniroma2.eu", "admin");
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
        RegistrazioneBean rb = new RegistrazioneBean(email, password);
        assertTrue(loginController.register(rb));
    }

    @Test
    void testRegistrazioneUtenteGiaEsistente() throws RuoloNonTrovatoException {
        RegistrazioneBean rb = new RegistrazioneBean("marco.rizzo@sys.uniroma2.eu", "password");
        assertFalse(loginController.register(rb));
    }


    // ---- TEST REGISTRAZIONE EMAIL NON VALIDA (Mancanza chiocciola, formato nome errato , mail null)---- //
     @ParameterizedTest(name = "Registrazione {0} con email {1}")
     @CsvSource({
             "Docente, mario.rossiuniroma2.eu, password",
             "Tecnico, luigi@tec.uniroma2.eu, password",
             "Docente, , password"
     })
    void testRegistrazioneEmailNonValidaNo(String ruolo, String email, String password) {
        RegistrazioneBean rb = new RegistrazioneBean(email, password);
        assertThrows(IllegalArgumentException.class , () -> loginController.register(rb));

    }

    @Test
    void testRegistrazioneDominioSconosciuto() {
        RegistrazioneBean rb = new RegistrazioneBean("mario.rossi@mat.uniroma2.eu", "password");
        assertThrows(RuoloNonTrovatoException.class, () -> loginController.register(rb));
    }

}
