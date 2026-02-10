package org.ing.ispw.controller.applicativo;

import org.ing.ispw.unifix.Driver;
import org.ing.ispw.unifix.bean.AulaBean;
import org.ing.ispw.unifix.controllerapplicativo.GestioneAuleController;
import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.exception.AulaGiaPresenteException;
import org.ing.ispw.unifix.exception.DatiAulaNonValidiException;
import org.ing.ispw.unifix.model.Aula;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
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
            tempCsvFile.delete();
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
        existingAula.setOggetti(Arrays.asList("Oggetto1"));
        aulaDao.store(existingAula);

        // Prova a inserire da CSV con la stessa aula
        tempCsvFile = createTempCsvFile(
                """
                            Edificio,IdAula,Piano,Oggetti
                            EdificioNuovo,A101,1,Proiettore;Lavagna
                            Edificio2,B202,2,Computer;Sedia
                        """
        );

        boolean result = controller.inserisciAuleFromCsv(tempCsvFile.getAbsolutePath());

        assertTrue(result); // B202 è stata inserita

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
        existingAula.setOggetti(Arrays.asList("Oggetto1"));
        aulaDao.store(existingAula);

        tempCsvFile = createTempCsvFile(
                """
                        Edificio,IdAula,Piano,Oggetti
                        EdificioNuovo,A101,1,Proiettore;Lavagna
                        """
);

    boolean result = controller.inserisciAuleFromCsv(tempCsvFile.getAbsolutePath());

    assertFalse(result);
}



// ==================== TEST INSERISCI SINGOLA AULA ====================

        @Test
        @DisplayName("inserisciAula - Inserimento aula singola con successo")
        void testInserisciAulaSingola() throws DatiAulaNonValidiException, AulaGiaPresenteException {
            AulaBean aulaBean = new AulaBean("C303", "Edificio3", 3, Arrays.asList("Monitor", "Webcam"));

            controller.inserisciAula(aulaBean);

            assertTrue(aulaDao.exists("C303"));
            Aula aulaStored = aulaDao.load("C303");
            assertEquals("Edificio3", aulaStored.getEdificio());
            assertEquals(3, aulaStored.getPiano());
            assertEquals(Arrays.asList("Monitor", "Webcam"), aulaStored.getOggetti());
            }

            @Test
            @DisplayName("inserisciAula - Aula già esistente lancia eccezione")
            void testInserisciAulaThrowsExceptionWhenExists() throws DatiAulaNonValidiException {
            // Prima inserisci l'aula
            Aula existingAula = aulaDao.create("C303");
            existingAula.setEdificio("Edificio3");
            existingAula.setPiano(3);
            existingAula.setOggetti(List.of("Oggetto1"));
            aulaDao.store(existingAula);

            AulaBean aulaBean = new AulaBean("C303", "EdificioNuovo", 1, Arrays.asList("Monitor", "Webcam"));

            assertThrows(AulaGiaPresenteException.class, () -> {
                controller.inserisciAula(aulaBean);
            });
        }

// ==================== TEST AULABEAN VALIDAZIONE ====================
/*
*  Test per verificare che il costruttore di AulaBean lanci eccezioni
*  quando i dati forniti non sono validi.
*/

    //Test eccezione per idAula null, vuoto, edificio vuoto, piano negativo, oggetti vuoti
    @ParameterizedTest
    @CsvSource({
            "'', Edificio1, 1, 'Proiettore;Lavagna'",
            "A101, '', 1, 'Proiettore;Lavagna'",
            "A101, Edificio1, -1, 'Proiettore;Lavagna'",
            "A101, Edificio1, 1, ';'",
    })
    void testAulaBeanException(String idAula, String edificio, int piano, String oggettiStr) {
        assertThrows(DatiAulaNonValidiException.class, () -> {
            new AulaBean(idAula,edificio, piano, Arrays.asList(oggettiStr.split(";")));
        });
    }



    @Test
    @DisplayName("AulaBean - Creazione valida con dati corretti")
    void testAulaBeanValidCreation() throws DatiAulaNonValidiException {
    AulaBean bean = new AulaBean("A101", "Edificio1", 2, Arrays.asList("Proiettore", "Lavagna"));

    assertEquals("A101", bean.getIdAula());
    assertEquals("Edificio1", bean.getEdificio());
    assertEquals(2, bean.getPiano());
    assertEquals(Arrays.asList("Proiettore", "Lavagna"), bean.getOggetti());
    }

    @Test
    @DisplayName("AulaBean - Piano zero è valido")
    void testAulaBeanZeroFloorIsValid() throws DatiAulaNonValidiException {
    AulaBean bean = new AulaBean("A101", "Edificio1", 0, Arrays.asList("Oggetto"));

    assertEquals(0, bean.getPiano());
    }


    // ==================== METODI DI UTILITÀ ====================

    // Crea un file CSV temporaneo con il contenuto specificato e lo ritorna, il file viene eliminato alla chiusura del test
    private File createTempCsvFile(String content) throws IOException {
        File tempFile = Files.createTempFile("test_aule", ".csv").toFile();
        tempFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(content);
        }
        return tempFile;
    }

}
