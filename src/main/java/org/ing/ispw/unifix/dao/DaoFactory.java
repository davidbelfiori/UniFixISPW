package org.ing.ispw.unifix.dao;

import org.ing.ispw.unifix.dao.jdbc.PersistenceDaoFactory;
import org.ing.ispw.unifix.dao.json.JsonDaoFactory;
import org.ing.ispw.unifix.dao.memory.InMemoryDaoFactory;
import org.ing.ispw.unifix.utils.DemoData;
import org.ing.ispw.unifix.utils.Printer;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public abstract class DaoFactory {

    private static DaoFactory instance = null;
    // Singleton Lazy Initialization guidato da configurazione esterna
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
                case "JDBC", "PERSISTENCE" -> new PersistenceDaoFactory();
                default -> {
                    Printer.print("Tipo persistenza non valido o assente, fallback su: JDBC");
                    yield new PersistenceDaoFactory();
                }
            };
        }
        return instance;
    }

    public static synchronized void setInstance(DaoFactory customInstance) {
        DaoFactory.instance = customInstance;
    }

    // Metodo privato helper per leggere il file application.properties
    private static String loadPersistenceType() {
        Properties props = new Properties();
        try (InputStream is = new FileInputStream("application.properties")) {
            props.load(is);
            return props.getProperty("persistence.type", "JDBC");
        } catch (Exception _) {
            return "JDBC"; // Fallback sicuro
        }
    }


    public abstract UserDao getUserDao();
    public abstract AulaDao getAulaDao();
    public abstract SegnalazioneDao getSegnalazioneDao();
    public abstract NotaSegnalazioneDao getNotaSegnalazioneDao();
}