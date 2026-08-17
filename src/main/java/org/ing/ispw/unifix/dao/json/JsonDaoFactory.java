package org.ing.ispw.unifix.dao.json;

import org.ing.ispw.unifix.dao.*;

/**
 * Factory del backend JSON. Inizializza pigramente un'unica istanza per ogni DAO
 * e la riutilizza nelle richieste successive.
 */
public class JsonDaoFactory extends DaoFactory {

    private UserDao userDao;
    private AulaDao aulaDao;
    private SegnalazioneDao segnalazioneDao;
    private NotaSegnalazioneDao notaSegnalazioneDao;

    @Override
    public UserDao getUserDao() {
        // Lazy initialization: il DAO viene creato soltanto al primo utilizzo,
        // evitando di inizializzare componenti che potrebbero non servire.
        if (userDao == null) {
            userDao = new JsonUserDao();
        }

        // Le chiamate successive riutilizzano la stessa istanza; questo evita
        // configurazioni ripetute dell'ObjectMapper e della directory dati.
        return userDao;
    }

    @Override
    public AulaDao getAulaDao() {
        // Ogni tipo di entità ha una propria istanza DAO, creata pigramente.
        if (aulaDao == null) {
            aulaDao = new JsonAulaDao();
        }
        return aulaDao;
    }

    @Override
    public SegnalazioneDao getSegnalazioneDao() {
        // La factory restituisce l'interfaccia SegnalazioneDao: i controller non
        // devono conoscere la classe concreta che usa la persistenza JSON.
        if (segnalazioneDao == null) {
            segnalazioneDao = new JsonSegnalazioneDao();
        }
        return segnalazioneDao;
    }

    @Override
    public NotaSegnalazioneDao getNotaSegnalazioneDao() {
        // Anche il DAO delle note viene conservato dopo la prima creazione. Viene
        // memorizzata l'istanza, non il contenuto dei file JSON.
        if (notaSegnalazioneDao == null) {
            notaSegnalazioneDao = new JsonNotaSegnalazioneDao();
        }
        return notaSegnalazioneDao;
    }
}

