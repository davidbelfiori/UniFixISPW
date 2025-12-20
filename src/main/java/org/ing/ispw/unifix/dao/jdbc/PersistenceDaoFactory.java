package org.ing.ispw.unifix.dao.jdbc;

import org.ing.ispw.unifix.dao.*;

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

    @Override
    public NotaSegnalazioneDao getNotaSegnalazioneDao() {
        return JdbcNotaSegnalazione.getInstance();
    }
}
