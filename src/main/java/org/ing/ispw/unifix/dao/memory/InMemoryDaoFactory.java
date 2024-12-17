package org.ing.ispw.unifix.dao.memory;

import org.ing.ispw.unifix.dao.DaoFactory;

public class InMemoryDaoFactory extends DaoFactory {

    public InMemoryUserDao getUserDao() {
        return InMemoryUserDao.getInstance();
    }
}
