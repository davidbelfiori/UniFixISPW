package org.ing.ispw.controller.applicativo;

import org.ing.ispw.unifix.Driver;
import org.ing.ispw.unifix.bean.LoginBean;
import org.ing.ispw.unifix.bean.RegistrazioneBean;
import org.ing.ispw.unifix.controllerapplicativo.LoginController;
import org.ing.ispw.unifix.exception.RuoloNonTrovatoException;
import org.ing.ispw.unifix.exception.UtenteNonTrovatoException;
import org.ing.ispw.unifix.utils.DemoData;
import org.ing.ispw.unifix.utils.UserType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
        LoginBean loginBean = new LoginBean("utente.inesistente@uniroma2.eu", "password123");

        assertThrows(UtenteNonTrovatoException.class, () -> {
            loginController.validate(loginBean);
        });
    }

    @Test
    void testLoginPasswordErrata() {
        LoginBean loginBean = new LoginBean("docente@uniroma2.eu", "password_errata");

        assertThrows(UtenteNonTrovatoException.class, () -> {
            loginController.validate(loginBean);
        });
    }

    @Test
    void testLoginUtenteEsistente() throws UtenteNonTrovatoException {
        LoginBean loginBean = new LoginBean("marco.rizzo@sys.uniroma2.eu", "errata");
        assertEquals(UserType.UNKNOWN,loginController.validate(loginBean));

    }

    @Test
    void testLoginRuoloUtenteSys() throws UtenteNonTrovatoException {
        LoginBean loginBean = new LoginBean("marco.rizzo@sys.uniroma2.eu", "admin");
        assertEquals(UserType.SYSADMIN,loginController.validate(loginBean));
    }

    @Test
    void testLoginRuoloUtenteDocente() throws UtenteNonTrovatoException {
        LoginBean loginBean = new LoginBean("davide.falessi@uniroma2.eu", "admin");
        assertEquals(UserType.DOCENTE,loginController.validate(loginBean));
    }

    @Test
    void testLoginRuoloUtenteTecnico() throws UtenteNonTrovatoException {
        LoginBean loginBean = new LoginBean("giuseppe.rossi@tec.uniroma2.eu", "admin");
        assertEquals(UserType.TECNICO,loginController.validate(loginBean));
    }
    
    // ---- TEST REGISTRAZIONE ---- //

    @Test
    void testRegistrazioneDocenteSuccesso() throws RuoloNonTrovatoException {
        RegistrazioneBean rb = new RegistrazioneBean("mario.rossi@uniroma2.eu", "password");
        assertTrue(loginController.register(rb));
    }

    @Test
    void testRegistrazioneTecnicoSuccesso() throws RuoloNonTrovatoException {
        RegistrazioneBean rb = new RegistrazioneBean("luigi.verdi@tec.uniroma2.eu", "password");
        assertTrue(loginController.register(rb));
    }

    @Test
    void testRegistrazioneSysadminSuccesso() throws RuoloNonTrovatoException {
        RegistrazioneBean rb = new RegistrazioneBean("anna.bianchi@sys.uniroma2.eu", "password");
        assertTrue(loginController.register(rb));
    }

    @Test
    void testRegistrazioneUtenteGiaEsistente() throws RuoloNonTrovatoException {
        RegistrazioneBean rb = new RegistrazioneBean("marco.rizzo@sys.uniroma2.eu", "password");
        assertFalse(loginController.register(rb));
    }

    @Test
    void testRegistrazioneEmailNonValidaNoChiocciola() {
        RegistrazioneBean rb = new RegistrazioneBean("invalidemail", "password");
        assertThrows(IllegalArgumentException.class, () -> loginController.register(rb));
    }

    @Test
    void testRegistrazioneEmailNonValidaFormatoNome() {
        RegistrazioneBean rb = new RegistrazioneBean("mario@uniroma2.eu", "password");
        assertThrows(IllegalArgumentException.class, () -> loginController.register(rb));
    }

    @Test
    void testRegistrazioneDominioSconosciuto() {
        RegistrazioneBean rb = new RegistrazioneBean("mario.rossi@mat.uniroma2.eu", "password");
        assertThrows(RuoloNonTrovatoException.class, () -> loginController.register(rb));
    }

    @Test
    void testRegistrazioneEmailNull() {
        RegistrazioneBean rb = new RegistrazioneBean(null, "password");
        assertThrows(IllegalArgumentException.class, () -> loginController.register(rb));
    }
}
