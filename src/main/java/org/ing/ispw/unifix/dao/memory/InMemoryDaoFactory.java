package org.ing.ispw.unifix.dao.memory;

import org.ing.ispw.unifix.dao.*;

/**
 * Factory del backend volatile in memoria. Crea pigramente un DAO per ogni tipo
 * di entità e ne mantiene l'istanza, così che i dati restino condivisi.
 */
public class InMemoryDaoFactory extends DaoFactory {

    private UserDao userDao;
    private AulaDao aulaDao;
    private SegnalazioneDao segnalazioneDao;
    private NotaSegnalazioneDao notaSegnalazioneDao;

    @Override
    public UserDao getUserDao() {
        if (userDao == null) {
            userDao = new InMemoryUserDao();
        }
        return userDao;
    }

    @Override
    public AulaDao getAulaDao() {
        if (aulaDao == null) {
            aulaDao = new InMemoryAulaDao();
        }
        return aulaDao;
    }

    @Override
    public SegnalazioneDao getSegnalazioneDao() {
        if (segnalazioneDao == null) {
            segnalazioneDao = new InMemorySegnalazioneDao();
        }
        return segnalazioneDao;
    }

    @Override
    public NotaSegnalazioneDao getNotaSegnalazioneDao() {
        if (notaSegnalazioneDao == null) {
            notaSegnalazioneDao = new InMemoryNotaSegnalazioneDao();
        }
        return notaSegnalazioneDao;
    }

}
