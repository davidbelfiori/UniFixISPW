package org.ing.ispw.unifix.dao;

import org.ing.ispw.unifix.dao.jdbc.JdbcDaoFactory;
import org.ing.ispw.unifix.dao.json.JsonDaoFactory;
import org.ing.ispw.unifix.dao.memory.InMemoryDaoFactory;
import org.ing.ispw.unifix.utils.DemoData;
import org.ing.ispw.unifix.utils.Printer;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Punto di accesso centralizzato alle implementazioni DAO configurate per l'applicazione.
 * La factory è un singleton inizializzato al primo utilizzo in base alla proprietà
 * {@code persistence.type}; in assenza di una configurazione valida usa JDBC.
 */
public abstract class DaoFactory {

    private static DaoFactory instance = null;
    /**
     * Restituisce la factory condivisa, creandola al primo accesso.
     * Per il backend in memoria carica anche i dati dimostrativi.
     *
     * @return factory corrispondente al tipo di persistenza configurato
     * @throws org.ing.ispw.unifix.exception.PersistenceException se l'inizializzazione
     *         del backend o dei dati dimostrativi non riesce
     * @throws org.ing.ispw.unifix.exception.CsvInvalidException se il caricamento dei dati dimostrativi fallisce per CSV malformato
     *
     *<p>Le chiamate successive restituiscono la stessa factory, senza
     *  * rileggere la configurazione. I controller possono quindi ottenere
     *  * il DAO relativo al backend selezionato attraverso chiamate come
     *  * {@code DaoFactory.getInstance().getUserDao()} o
     *  * {@code DaoFactory.getInstance().getAulaDao()}.</p>
     */

    public static synchronized DaoFactory getInstance() {
        if (instance == null) {
            String type = loadPersistenceType();
            Printer.print("Tipo persistenza configurato: " + type + " procedo a creare la factory corrispondente");
            instance = switch (type.toUpperCase().trim()) {
                case "JSON" -> new JsonDaoFactory();
                case "MEMORY", "IN MEMORY" -> {
                    InMemoryDaoFactory memoryDaoFactory = new InMemoryDaoFactory();
                    instance = memoryDaoFactory; //assegno l'istanza
                    DemoData.load(); //carico i dati fantoccio
                    yield memoryDaoFactory;

                }
                case "JDBC", "PERSISTENCE" -> new JdbcDaoFactory();
                default -> {
                    Printer.print("Tipo persistenza non valido o assente, fallback su: JDBC");
                    yield new JdbcDaoFactory();
                }
            };
        }
        return instance;
    }

    /**
     * Sostituisce la factory condivisa, principalmente per test o configurazioni personalizzate.
     * Passando {@code null}, il successivo accesso ricreerà la factory dalla configurazione.
     *
     * @param customInstance factory da utilizzare, oppure {@code null} per azzerarla
     */
    public static synchronized void setInstance(DaoFactory customInstance) {
        DaoFactory.instance = customInstance;
    }

    /**
     * Legge il tipo di persistenza da {@code application.properties}.
     * Qualunque errore di lettura viene deliberatamente assorbito per consentire il fallback a JDBC.
     *
     * @return valore della proprietà {@code persistence.type}, oppure {@code JDBC} come valore predefinito
     */
    private static String loadPersistenceType() {
        Properties props = new Properties();
        try (InputStream is = new FileInputStream("application.properties")) {
            props.load(is);
            return props.getProperty("persistence.type", "JDBC");
        } catch (Exception _) {
            return "JDBC"; // Fallback sicuro
        }
    }
    /** @return DAO degli utenti fornito dal backend selezionato */
    public abstract UserDao getUserDao();

    /** @return DAO delle aule fornito dal backend selezionato */
    public abstract AulaDao getAulaDao();

    /** @return DAO delle segnalazioni fornito dal backend selezionato */
    public abstract SegnalazioneDao getSegnalazioneDao();

    /** @return DAO delle note fornito dal backend selezionato */
    public abstract NotaSegnalazioneDao getNotaSegnalazioneDao();
}
