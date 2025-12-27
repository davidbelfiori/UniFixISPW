package org.ing.ispw.unifix.dao.json;

import org.ing.ispw.unifix.dao.*;

public class JsonDaoFactory extends DaoFactory {

    @Override
    public UserDao getUserDao() {
        return JsonUserDao.getInstance();
    }

    @Override
    public AulaDao getAulaDao() {
        return JsonAulaDao.getInstance();
    }

    @Override
    public SegnalazioneDao getSegnalazioneDao() {
        return JsonSegnalazioneDao.getInstance();
    }

    @Override
    public NotaSegnalazioneDao getNotaSegnalazioneDao() {
        return JsonNotaSegnalazioneDao.getInstance();
    }
}

