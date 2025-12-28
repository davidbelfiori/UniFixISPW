package org.ing.ispw.unifix.dao.jdbc;

import org.ing.ispw.unifix.dao.*;

public class PersistenceDaoFactory extends DaoFactory {

    public UserDao getUserDao() {
        return JdbcUserDao.getInstance();
    }

    @Override
    public AulaDao getAulaDao() {
        return new JdbcAulaDao();
    }

    @Override
    public SegnalazioneDao getSegnalazioneDao() {
        return new JdbcSegnalazioneDao();
    }

    @Override
    public NotaSegnalazioneDao getNotaSegnalazioneDao() {
        return new JdbcNotaSegnalazione();
    }
}
