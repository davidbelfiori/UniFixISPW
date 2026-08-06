package org.ing.ispw.unifix.dao.jdbc;

import org.ing.ispw.unifix.exception.DbConnException;
import org.ing.ispw.unifix.utils.Printer;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SingletonConnessione {

    private static Connection connection;

    private static final String URL = "jdbc:mariadb://localhost:3306/unifix";
    private static final String USERNAME = "root";

    // Costruttore privato vuoto per impedire l'istanziamento dall'esterno
    private SingletonConnessione() {}

    // Metodo thread-safe per ottenere la connessione unica
    public static synchronized Connection getInstance() throws DbConnException {
        try {
            // Controlla sia se la connessione è null, sia se è stata chiusa/interrotta
            if (connection == null || connection.isClosed()) {
                String password = loadDatabasePassword();
                connection = DriverManager.getConnection(URL, USERNAME, password);
            }
        } catch (RuntimeException | SQLException e) {
            throw new DbConnException("Impossibile connettersi al database: " + e.getMessage());
        }
        return connection;
    }

    // Metodo privato estratto per isolare la logica di lettura del file
    //un metodo statico non possiede un riferimento this ad un'istanza e non
    // può invocare metodi di istanza non statici senza istanziare la classe.
    private static String loadDatabasePassword() {
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream("application.properties")) {
            properties.load(is);
        } catch (java.io.IOException _) { // Sostituito "Exception" con la più specifica "IOException"
            Printer.error("Impossibile leggere il file application.properties, utilizzo password di default");
        }
        return properties.getProperty("password", "");
    }


    // Chiusura sicura della connessione alla chiusura dell'applicazione
    /*Rendere closeConnection() static consente a qualunque componente
    (es. alla chiusura dell'app o in fase di shutdown hook)
     di invocare SingletonConnessione.closeConnection() senza bisogno di avere un'istanza della classe.*/
    public static synchronized void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                Printer.error("Errore durante la chiusura della connessione: " + e.getMessage());
            } finally {
                connection = null;
            }
        }
    }
}