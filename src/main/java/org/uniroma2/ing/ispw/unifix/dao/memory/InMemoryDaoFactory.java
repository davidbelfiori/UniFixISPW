package org.uniroma2.ing.ispw.unifix.dao.memory;

import org.uniroma2.ing.ispw.unifix.dao.DaoFactory;

public class InMemoryDaoFactory extends DaoFactory {

    public inMemoryUserDao getUserDao() {
        return inMemoryUserDao.getInstance();
    }
}
