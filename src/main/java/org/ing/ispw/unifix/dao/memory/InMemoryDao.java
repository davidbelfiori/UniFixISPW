package org.ing.ispw.unifix.dao.memory;

import org.ing.ispw.unifix.dao.Dao;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class InMemoryDao<K, V> implements Dao<K, V>  {

    private final Map<K, V> memory = new HashMap<>();

    protected void store(K key, V value) {
        memory.put(key, value);
    }

    @Override
    public void delete(K id) {
        if (id == null) {
            throw new IllegalArgumentException("L'identificatore non può essere nullo.");
        }
        memory.remove(id);
    }

    @Override
    public boolean exists(K id) {
        if (id == null) {
            throw new IllegalArgumentException("L'identificatore non può essere nullo.");
        }
        return memory.containsKey(id);
    }

    public List<V> loadAll(){
        return new ArrayList<>(memory.values());
    }


    @Override
    public V load(K id) {
        if (id == null) {
            throw new IllegalArgumentException("L'identificatore non può essere nullo.");
        }
        return memory.get(id);
    }

    @Override
    public void store(V entity) {
        if (entity == null) {
            throw new IllegalArgumentException("L'entità non può essere nulla.");
        }
        K key = getKey(entity);
        if (key == null) {
            throw new IllegalArgumentException("La chiave dell'entità non può essere nulla.");
        }
        if (memory.containsKey(key)) {
            throw new IllegalArgumentException("Impossibile memorizzare: entità con ID " + key + " già esistente.");
        }
        store(key, entity);
    }



    @Override
    public final void update(V entity) {
        if (entity == null) {
            throw new IllegalArgumentException("L'entità non può essere nulla.");
        }
        K key = getKey(entity); // Recupera la chiave dell'entità
        if (key == null) {
            throw new IllegalArgumentException("La chiave dell'entità non può essere nulla.");
        }
        if (!memory.containsKey(key)) {
            throw new IllegalArgumentException("Impossibile aggiornare: entità con ID " + key + " non trovata.");
        }
        memory.put(key, entity); // Aggiorna l'entità esistente
    }


    protected abstract K getKey(V value);
}
