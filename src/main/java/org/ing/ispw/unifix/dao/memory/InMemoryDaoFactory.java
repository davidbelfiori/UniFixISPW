package org.ing.ispw.unifix.dao.memory;

import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.dao.DaoFactory;

public class InMemoryDaoFactory extends DaoFactory {

    public InMemoryUserDao getUserDao() {
        return InMemoryUserDao.getInstance();
    }

    @Override
    public AulaDao getAulaDao() {
        return InMemoryAulaDao.getInstance();
    }

}
