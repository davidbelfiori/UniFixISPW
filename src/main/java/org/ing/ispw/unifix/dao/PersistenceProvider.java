package org.ing.ispw.unifix.dao;

import org.ing.ispw.unifix.dao.jdbc.PersistenceDaoFactory;
import org.ing.ispw.unifix.dao.json.JsonDaoFactory;
import org.ing.ispw.unifix.dao.memory.InMemoryDaoFactory;

public enum PersistenceProvider {

    IN_MEMORY("in memory", InMemoryDaoFactory.class),
    PERSISTENCE("persistence", PersistenceDaoFactory.class),
    JSON("json", JsonDaoFactory.class);

    private final String name;
    private final Class<? extends DaoFactory> daoFactoryClass;

    PersistenceProvider(String name, Class<? extends DaoFactory> daoFactoryClass) {
        this.name = name;
        this.daoFactoryClass = daoFactoryClass;
    }

    public String getName() {
        return name;
    }

    public Class<? extends DaoFactory> getDaoFactoryClass() {
        return daoFactoryClass;
    }
}
