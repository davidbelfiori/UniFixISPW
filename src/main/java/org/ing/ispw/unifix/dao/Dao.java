package org.ing.ispw.unifix.dao;

import java.util.List;

public interface Dao <K,E>{

    E load(K id);
    void store(E entity);
    void delete(K id); // id è la chiave dell'hash map , na nostra pk
    boolean exists(K id);
    List<E>  loadAll();
    void update(E entity);
}
