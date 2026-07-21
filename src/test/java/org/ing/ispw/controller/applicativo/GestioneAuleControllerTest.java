package org.ing.ispw.controller.applicativo;

import org.ing.ispw.unifix.Driver;
import org.ing.ispw.unifix.bean.AulaBean;
import org.ing.ispw.unifix.controllerapplicativo.GestioneAuleController;
import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.exception.AulaGiaPresenteException;
import org.ing.ispw.unifix.model.Aula;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GestioneAuleControllerTest {

    private GestioneAuleController controller;
    private AulaDao aulaDao;
    private File tempCsvFile;

    @BeforeEach
    void setUp() {
        // Configura il DaoFactory con InMemoryDaoFactory per i test
        Driver.setPersistenceProvider("in memory");
        controller = new GestioneAuleController();
        aulaDao = DaoFactory.getInstance().getAulaDao();
    }

    @AfterEach
    void tearDown() {
        // Pulizia del file temporaneo se esiste
        if (tempCsvFile != null && tempCsvFile.exists()) {
            assertTrue(tempCsvFile.delete());
        }
    }

    // ==================== TEST INSERISCI AULE DA CSV ====================

    @Test
    @DisplayName("inserisciAule - Inserimento aule da CSV valido")
    void testInserisciAuleFromCsvWithValidCsv() throws IOException {
        tempCsvFile = createTempCsvFile(
                """
                        Edificio,IdAula,Piano,Oggetti
                        Edificio1,A101,1,Proiettore;Lavagna
                        Edificio2,B202,2,Computer;Sedia
                        
                        """
        );

        boolean result = controller.inserisciAuleFromCsv(tempCsvFile.getAbsolutePath());

        assertTrue(result);
        assertTrue(aulaDao.exists("A101"));
        assertTrue(aulaDao.exists("B202"));

        Aula aulaA101 = aulaDao.load("A101");
        assertEquals("Edificio1", aulaA101.getEdificio());
        assertEquals(1, aulaA101.getPiano());
        assertEquals(Arrays.asList("Proiettore", "Lavagna"), aulaA101.getOggetti());
    }


    @Test
    @DisplayName("inserisciAule - Aula già esistente viene saltata")
    void testInserisciAuleFromCsvSkipsExistingAula() throws IOException {
        // Prima inserisci un'aula
        Aula existingAula = aulaDao.create("A101");
        existingAula.setEdificio("EdificioOriginale");
        existingAula.setPiano(0);
        existingAula.setOggetti(List.of("Oggetto1"));
        aulaDao.store(existingAula);

        // Prova a inserire da CSV con la stessa aula
        tempCsvFile = createTempCsvFile(
                """
                            Edificio,IdAula,Piano,Oggetti
                            EdificioNuovo,A101,1,Proiettore;Lavagna
                            Edificio2,B202,2,Computer;Sedia
                        """
        );
        String filePath = tempCsvFile.getAbsolutePath();

        assertThrows(AulaGiaPresenteException.class, () -> controller.inserisciAuleFromCsv(filePath));
        // B202 è stata inserita

        // Verifica che A101 mantenga i dati originali
        Aula aulaA101 = aulaDao.load("A101");
        assertEquals("EdificioOriginale", aulaA101.getEdificio());
        assertEquals(0, aulaA101.getPiano());
    }

    @Test
    @DisplayName("inserisciAule - Nessuna aula inserita se tutte esistono")
    void testInserisciAuleFromCsvReturnsFalseWhenAllExist() throws IOException {
        // Inserisci l'aula prima
        Aula existingAula = aulaDao.create("A101");
        existingAula.setEdificio("EdificioOriginale");
        existingAula.setPiano(0);
        existingAula.setOggetti(List.of("Oggetto1"));
        aulaDao.store(existingAula);

        tempCsvFile = createTempCsvFile(
                """
                        Edificio,IdAula,Piano,Oggetti
                        EdificioNuovo,A101,1,Proiettore;Lavagna
                        """
);

        String filePath = tempCsvFile.getAbsolutePath();

        assertThrows(AulaGiaPresenteException.class, () -> controller.inserisciAuleFromCsv(filePath));

}



// ==================== TEST INSERISCI SINGOLA AULA ====================

        @Test
        @DisplayName("inserisciAula - Inserimento aula singola con successo")
        void testInserisciAulaSingola() throws IllegalStateException, AulaGiaPresenteException {
            AulaBean aulaBean = new AulaBean();
            aulaBean.setIdAula("C303");
            aulaBean.setEdificio("Edificio3");
            aulaBean.setPiano(3);
            aulaBean.setOggetti(Arrays.asList("Monitor", "Webcam"));

            controller.inserisciAula(aulaBean);

            assertTrue(aulaDao.exists("C303"));
            Aula aulaStored = aulaDao.load("C303");
            assertEquals("Edificio3", aulaStored.getEdificio());
            assertEquals(3, aulaStored.getPiano());
            assertEquals(Arrays.asList("Monitor", "Webcam"), aulaStored.getOggetti());
            }

            @Test
            @DisplayName("inserisciAula - Aula già esistente lancia eccezione")
            void testInserisciAulaThrowsExceptionWhenExists() throws IllegalStateException {
            // Prima inserisci l'aula
            Aula existingAula = aulaDao.create("C303");
            existingAula.setEdificio("Edificio3");
            existingAula.setPiano(3);
            existingAula.setOggetti(List.of("Oggetto1"));
            aulaDao.store(existingAula);

            AulaBean aulaBean = new AulaBean();
            aulaBean.setIdAula("C303");
            aulaBean.setEdificio("EdificioNuovo");
            aulaBean.setPiano(1);
            aulaBean.setOggetti(Arrays.asList("Monitor", "Webcam"));

                assertThrows(AulaGiaPresenteException.class, () -> controller.inserisciAula(aulaBean));
        }


        /* AULA BEAN TEST */

    // --- TEST ID AULA ---

    @Test
    void testSetIdAulaValido() {
        AulaBean aulaBean = new AulaBean();
        assertDoesNotThrow(() -> aulaBean.setIdAula("A1"));
        assertEquals("A1", aulaBean.getIdAula());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", " \t \n "})
    void testSetIdAulaVuotoOLimitiLanciaEccezione(String idInvalido) {
        AulaBean aulaBean = new AulaBean();
        assertThrows(IllegalArgumentException.class, () -> aulaBean.setIdAula(idInvalido));
    }

    @Test
    void testSetIdAulaNullLanciaEccezione() {
        AulaBean aulaBean = new AulaBean();
        assertThrows(IllegalArgumentException.class, () -> aulaBean.setIdAula(null));
    }

    // --- TEST PIANO ---

    @Test
    void testSetPianoValido() {
        AulaBean aulaBean = new AulaBean();
        aulaBean.setPiano(2);
        assertEquals(2, aulaBean.getPiano());
    }

    @Test
    void testSetPianoValoriLimiteConsentiti() {
        AulaBean aulaBean = new AulaBean();
        assertDoesNotThrow(() -> aulaBean.setPiano(-5));
        assertEquals(-5, aulaBean.getPiano());

        assertDoesNotThrow(() -> aulaBean.setPiano(100));
        assertEquals(100, aulaBean.getPiano());
    }

    @Test
    void testSetPianoTroppoBassoLanciaEccezione() {
        AulaBean aulaBean = new AulaBean();
        assertThrows(IllegalArgumentException.class, () -> aulaBean.setPiano(-6));
    }

    @Test
    void testSetPianoTroppoAltoLanciaEccezione() {
        AulaBean aulaBean = new AulaBean();
        assertThrows(IllegalArgumentException.class, () -> aulaBean.setPiano(101));
    }

    // --- TEST EDIFICIO ---

    @Test
    void testSetEdificioValido() {
        AulaBean aulaBean = new AulaBean();
        aulaBean.setEdificio("Edificio Didattica");
        assertEquals("Edificio Didattica", aulaBean.getEdificio());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void testSetEdificioVuotoLanciaEccezione(String edificioInvalido) {
        AulaBean aulaBean = new AulaBean();
        assertThrows(IllegalArgumentException.class, () -> aulaBean.setEdificio(edificioInvalido));
    }

    @Test
    void testSetEdificioNullLanciaEccezione() {
        AulaBean aulaBean = new AulaBean();
        assertThrows(IllegalArgumentException.class, () -> aulaBean.setEdificio(null));
    }

    // --- TEST OGGETTI ---

    @Test
    void testSetOggettiValidi() {
        List<String> oggetti = List.of("Proiettore", "Cattedra");
        AulaBean aulaBean = new AulaBean();
        aulaBean.setOggetti(oggetti);
        assertEquals(2, aulaBean.getOggetti().size());
        assertTrue(aulaBean.getOggetti().contains("Proiettore"));
    }

    @Test
    void testSetOggettiNullLanciaEccezione() {
        AulaBean aulaBean = new AulaBean();
        assertThrows(IllegalArgumentException.class, () -> aulaBean.setOggetti(null));
    }

    @Test
    void testSetOggettiListaVuotaLanciaEccezione() {
        List<String> listaVuota = new ArrayList<>();
        AulaBean aulaBean = new AulaBean();
        assertThrows(IllegalArgumentException.class, () -> aulaBean.setOggetti(listaVuota));
    }



    // ==================== METODI DI UTILITÀ ====================

    // Crea un file CSV temporaneo con il contenuto specificato e lo ritorna, il file viene eliminato alla chiusura del test
    private File createTempCsvFile(String content) throws IOException {
        File tempFile = Files.createTempFile("test_aule", ".csv").toFile();
        tempFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(content.trim());
        }
        return tempFile;
    }

}
