package org.ing.ispw.unifix.dao.jdbc;

import org.ing.ispw.unifix.dao.*;

/**
 * Factory del backend JDBC. Crea pigramente una sola istanza di ciascun DAO
 * e la riutilizza per tutta la durata della factory.
 */
public class JdbcDaoFactory extends DaoFactory {


    private UserDao userDao;
    private AulaDao aulaDao;
    private SegnalazioneDao segnalazioneDao;
    private NotaSegnalazioneDao notaSegnalazioneDao;

    public UserDao getUserDao() {
        if(userDao == null) {
            userDao = new JdbcUserDao();
        }
        return userDao;
    }

    @Override
    public AulaDao getAulaDao() {
        if(aulaDao == null) {
            aulaDao = new JdbcAulaDao();
        }
        return aulaDao;
    }

    @Override
    public SegnalazioneDao getSegnalazioneDao() {
        if(segnalazioneDao == null) {
            segnalazioneDao = new JdbcSegnalazioneDao();
        }
        return segnalazioneDao;
    }

    @Override
    public NotaSegnalazioneDao getNotaSegnalazioneDao() {
        if(notaSegnalazioneDao == null) {
            notaSegnalazioneDao = new JdbcNotaSegnalazione();
        }
        return notaSegnalazioneDao;
    }
}
