package org.ing.ispw.unifix.dao.memory;

import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.dao.UserDao;

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

}
