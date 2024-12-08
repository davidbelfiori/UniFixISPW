package org.uniroma2.ing.ispw.unifix.factory;

import org.uniroma2.ing.ispw.unifix.dao.DatabaseConPersistenza;
import org.uniroma2.ing.ispw.unifix.dao.DatabaseInMemoria;
import org.uniroma2.ing.ispw.unifix.dao.DatabaseInterface;

public class DatabaseFactory {

    private DatabaseFactory() {
    }

    public static DatabaseInterface getDatabase(String type) {
        if (type.equalsIgnoreCase("demo")) {
            return new DatabaseInMemoria();
        } else if (type.equalsIgnoreCase("persistente")) {
            try {
                return new DatabaseConPersistenza();
            } catch (Exception e) {
                throw new RuntimeException("Errore nella connessione al database");
            }
        }
        throw new IllegalArgumentException("Tipo di database non valido");
    }
}

