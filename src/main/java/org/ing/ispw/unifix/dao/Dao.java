package org.ing.ispw.unifix.dao;

public interface Dao <K,E>{

    E create(K id);
    E load(K id);
    void store(E entity);
    void delete(K id); // id è la chiave dell'hash map , na nostra pk
    boolean exists(K id);

}
