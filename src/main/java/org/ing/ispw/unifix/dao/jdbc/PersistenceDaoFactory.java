package org.ing.ispw.unifix.dao.jdbc;

import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.dao.UserDao;

public class PersistenceDaoFactory extends DaoFactory {

    public UserDao getUserDao() {
        return JdbcUserDao.getInstance();
    }

    @Override
    public AulaDao getAulaDao() {
        return JdbcAulaDao.getInstance();
    }

    @Override
    public SegnalazioneDao getSegnalazioneDao() {
        return JdbcSegnalazioneDao.getInstance();
    }
}
