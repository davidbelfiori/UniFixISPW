package org.ing.ispw.controller.applicativo;

import org.ing.ispw.unifix.bean.AulaBean;
import org.ing.ispw.unifix.controllerapplicativo.GestioneAuleController;
import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.memory.InMemoryDaoFactory;
import org.ing.ispw.unifix.exception.AulaGiaPresenteException;
import org.ing.ispw.unifix.exception.AuleNonTrovateException;
import org.ing.ispw.unifix.model.AulaId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class TestGestioneAuleController {

    private GestioneAuleController controller;
    private AulaDao aulaDao;
    private File tempCsvFile;

    @BeforeEach
    void setUp() {
        DaoFactory.setInstance(new InMemoryDaoFactory());
        aulaDao = DaoFactory.getInstance().getAulaDao();
        controller = new GestioneAuleController();
    }

    @Test
    @DisplayName("Inserisce un'aula e la rende disponibile nel DAO")
    void testInserisciAulaSuccesso() throws AulaGiaPresenteException {
        AulaBean aula = creaAula("A1", "Ingegneria", 1,
                List.of("Proiettore", "Lavagna"));

        controller.inserisciAula(aula);

        assertTrue(aulaDao.exists(new AulaId("A1", "Ingegneria")));
        assertEquals(1, aulaDao.countAule());
        assertEquals(List.of("Proiettore", "Lavagna"),
                aulaDao.getAulaOggetti(new AulaId("A1", "Ingegneria")));
    }

    @Test
    @DisplayName("Rifiuta un'aula duplicata nello stesso edificio")
    void testInserisciAulaDuplicata() throws AulaGiaPresenteException {
        controller.inserisciAula(creaAula("A1", "Ingegneria", 0,
                List.of("Proiettore")));

        AulaBean duplicata = creaAula("a1", " ingegneria ", 2,
                List.of("Lavagna"));

        assertThrows(AulaGiaPresenteException.class,
                () -> controller.inserisciAula(duplicata));
        assertEquals(1, aulaDao.countAule());
    }

    @Test
    @DisplayName("Consente lo stesso identificativo in edifici diversi")
    void testInserisciStessoIdInEdificiDiversi() throws AulaGiaPresenteException {
        controller.inserisciAula(creaAula("A1", "Ingegneria", 0,
                List.of("Proiettore")));
        controller.inserisciAula(creaAula("A1", "Economia", 1,
                List.of("Lavagna")));

        assertEquals(2, aulaDao.countAule());
        assertEquals(2, aulaDao.countEdificiGestiti());
    }

    @Test
    @DisplayName("Visualizza le aule convertite in bean")
    void testVisualizzaAule() throws AulaGiaPresenteException, AuleNonTrovateException {
        controller.inserisciAula(creaAula("T2", "Economia", -1,
                List.of("Monitor", "Prese")));

        List<AulaBean> risultato = controller.visualizzaAule();

        assertEquals(1, risultato.size());
        AulaBean aula = risultato.getFirst();
        assertAll(
                () -> assertEquals("T2", aula.getIdAula()),
                () -> assertEquals("Economia", aula.getEdificio()),
                () -> assertEquals(-1, aula.getPiano()),
                () -> assertEquals(List.of("Monitor", "Prese"), aula.getOggetti())
        );
    }


    @Test
    @DisplayName("Importa da CSV le righe valide ignorando i duplicati")
    void testInserisciAuleFromCsv() throws IOException {
       tempCsvFile =  createTempCsvFile( """
                Edificio,IdAula,Piano,Oggetti
                Ingegneria,A1,0,Proiettore;Lavagna
                Ingegneria,A1,0,Proiettore;Lavagna
                Economia,T1,-1,Monitor;Prese
                """);

        boolean inserite = controller.inserisciAuleFromCsv(tempCsvFile.getAbsolutePath());

        assertTrue(inserite);
        assertEquals(2, aulaDao.countAule());
        assertTrue(aulaDao.exists(new AulaId("A1", "Ingegneria")));
        assertTrue(aulaDao.exists(new AulaId("T1", "Economia")));
    }

    @Test
    @DisplayName("Importa le nuove aule ignorando quelle già presenti nel sistema")
    void testInserisciAuleFromCsvConAulaGiaPresente() throws IOException, AulaGiaPresenteException {
        controller.inserisciAula(creaAula("A1", "Ingegneria", 0,
                List.of("Proiettore")));
        tempCsvFile = createTempCsvFile("""
                Edificio,IdAula,Piano,Oggetti
                Ingegneria,A1,0,Proiettore
                Ingegneria,A2,1,Lavagna
                """);

        boolean inserite = controller.inserisciAuleFromCsv(tempCsvFile.getAbsolutePath());

        assertTrue(inserite);
        assertEquals(2, aulaDao.countAule());
        assertTrue(aulaDao.exists(new AulaId("A1", "Ingegneria")));
        assertTrue(aulaDao.exists(new AulaId("A2", "Ingegneria")));
    }


    private static AulaBean creaAula(String id, String edificio, int piano,
                                     List<String> oggetti) {
        AulaBean aula = new AulaBean();
        aula.setIdAula(id);
        aula.setEdificio(edificio);
        aula.setPiano(piano);
        aula.setOggetti(oggetti);
        return aula;
    }

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
