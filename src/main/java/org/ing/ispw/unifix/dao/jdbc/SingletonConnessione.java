package org.ing.ispw.unifix.dao.jdbc;

import org.ing.ispw.unifix.exception.ErroreLetturaPasswordException;
import org.ing.ispw.unifix.utils.Printer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SingletonConnessione {
    private static Connection connection;
    private  static final String URL ="jdbc:mariadb://localhost:3306/unifix";
    private static final String USERNAME="root";


    private SingletonConnessione() throws SQLException, IOException {
        collegatiAlDB();
    }

    private static void collegatiAlDB() throws IOException, SQLException {
        Properties properties=new Properties();

            try (InputStream is = new FileInputStream("application.properties")) {
                properties.load(is);
            } catch (IOException _) {
                Printer.error("impossibile leggere il file application.properties");
        }
        connection= DriverManager.getConnection(URL,USERNAME,(String)properties.get("password"));
    }

    public static Connection getInstance() throws SQLException, ErroreLetturaPasswordException {
        try {
            if (connection == null) {
                new SingletonConnessione();
            }
        }catch (SQLException _){
            throw new SQLException("impossibile connettersi al database\nriprova più tardi");
        } catch (IOException _) {
            throw new ErroreLetturaPasswordException ("impossibile estrarre la password\ndi connessione al db");
        }
        return connection;
    }

    public static void closeConnection()  {
        if (connection != null) {
            try {
                connection.close();
            }catch (SQLException _){
                //ho provato tutti i casi possibili e non viene mai lanciata un eccezione del tipo sql exception
                //da questo metodo
                System.exit(-2);
            }
        }
    }

}
