package org.ing.ispw.unifix.dao.memory;

import org.ing.ispw.unifix.dao.*;

public class InMemoryDaoFactory extends DaoFactory {

    public UserDao getUserDao() {
        return InMemoryUserDao.getInstance();
    }

    @Override
    public AulaDao getAulaDao() {
        return InMemoryAulaDao.getInstance();
    }

    @Override
    public SegnalazioneDao getSegnalazioneDao() {
        return InMemorySegnalazioneDao.getInstance();
    }

    @Override
    public NotaSegnalazioneDao getNotaSegnalazioneDao() {
        return InMemoryNotaSegnalazioneDao.getInstance();
    }

}
