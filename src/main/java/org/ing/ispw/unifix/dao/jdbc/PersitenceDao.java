package org.ing.ispw.unifix.dao.jdbc;

import org.ing.ispw.unifix.dao.Dao;

import java.util.List;

public abstract class PersitenceDao <K, V> implements Dao<K, V> {

    @Override
    public V create(K id) {
        return null;
    }

    @Override
    public V load(K id) {
        return null;
    }

    @Override
    public void store(V entity) {

    }

    @Override
    public void delete(K id) {

    }

    @Override
    public boolean exists(K id) {
        return false;
    }

    @Override
    public List<V> loadAll() {
        return List.of();
    }

    @Override
    public void update(V entity) {

    }
}
