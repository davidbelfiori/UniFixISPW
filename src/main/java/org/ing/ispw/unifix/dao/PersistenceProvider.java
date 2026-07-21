package org.ing.ispw.unifix.dao;


public enum PersistenceProvider {

    IN_MEMORY("in memory"),
    PERSISTENCE("persistence"),
    JSON("json");

    private final String name;

    PersistenceProvider(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

}
