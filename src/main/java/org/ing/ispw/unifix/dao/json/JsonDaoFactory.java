package org.ing.ispw.unifix.dao.json;

import org.ing.ispw.unifix.dao.*;

public class JsonDaoFactory extends DaoFactory {

    private UserDao userDao;
    private AulaDao aulaDao;
    private SegnalazioneDao segnalazioneDao;
    private NotaSegnalazioneDao notaSegnalazioneDao;

    @Override
    public UserDao getUserDao() {
        if (userDao == null) {
            userDao = new JsonUserDao();
        }
        return userDao;
    }

    @Override
    public AulaDao getAulaDao() {
        if (aulaDao == null) {
            aulaDao = new JsonAulaDao();
        }
        return aulaDao;
    }

    @Override
    public SegnalazioneDao getSegnalazioneDao() {
        if (segnalazioneDao == null) {
            segnalazioneDao = new JsonSegnalazioneDao();
        }
        return segnalazioneDao;
    }

    @Override
    public NotaSegnalazioneDao getNotaSegnalazioneDao() {
        if (notaSegnalazioneDao == null) {
            notaSegnalazioneDao = new JsonNotaSegnalazioneDao();
        }
        return notaSegnalazioneDao;
    }
}

