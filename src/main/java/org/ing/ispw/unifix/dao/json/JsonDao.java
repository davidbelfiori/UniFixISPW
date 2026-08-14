package org.ing.ispw.unifix.dao.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.ing.ispw.unifix.dao.Dao;
import org.ing.ispw.unifix.exception.EntityAlreadyExistsException;
import org.ing.ispw.unifix.exception.JsonFileException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class JsonDao<K, V> implements Dao<K, V> {

    private static final String DATA_DIR = "data/json";
    protected final ObjectMapper objectMapper;
    private final String fileName;
    private final Class<V> entityClass;

    protected JsonDao(String fileName, Class<V> entityClass) {
        this.fileName = fileName;
        this.entityClass = entityClass;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        ensureDataDirectoryExists();
    }

    private void ensureDataDirectoryExists() {
        try {
            Path dataPath = Paths.get(DATA_DIR);
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
            }
        } catch (IOException e) {
            throw new JsonFileException("Impossibile creare la directory per i dati JSON"+ e);
        }
    }

    protected File getFile() {
        return new File(DATA_DIR, fileName);
    }

    protected abstract K getKey(V entity);

    @Override
    public V load(K id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "L'identificatore non può essere nullo"
            );
        }

        for (V entity : loadAll()) {
            if (id.equals(getKey(entity))) {
                return entity;
            }
        }

        return null;
    }

    @Override
    public void store(V entity) {
        if (entity == null) {
            throw new IllegalArgumentException("L'entità non può essere nulla");
        }

        K key = getKey(entity);

        if (key == null) {
            throw new IllegalArgumentException("La chiave dell'entità non può essere nulla");
        }

        List<V> entities = loadAll();

        boolean alreadyExists = entities.stream()
                .anyMatch(existing -> key.equals(getKey(existing)));

        if (alreadyExists) {
            throw new EntityAlreadyExistsException(
                    "Esiste già un'entità con chiave " + key
            );
        }

        entities.add(entity);
        saveAll(entities);
    }

    @Override
    public void delete(K id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "L'identificatore non può essere nullo"
            );
        }

        List<V> entities = loadAll();

        boolean removed = entities.removeIf(
                entity -> id.equals(getKey(entity))
        );

        if (removed) {
            saveAll(entities);
        }
    }

    @Override
    public boolean exists(K id) {
        return load(id) != null;
    }

    @Override
    public List<V> loadAll() {
        File file = getFile();
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            CollectionType listType = objectMapper.getTypeFactory()
                    .constructCollectionType(ArrayList.class, entityClass);
            return objectMapper.readValue(file, listType);
        } catch (IOException e) {
            throw new JsonFileException("Errore durante la lettura del file JSON: " + fileName+ e);
        }
    }

    @Override
    public void update(V entity) {
        K key = getKey(entity);
        if (!exists(key)) {
            throw new IllegalArgumentException("Impossibile aggiornare: entità con ID " + key + " non trovata.");
        }
        store(entity);
    }

    protected void saveAll(List<V> entities) {
        try {
            objectMapper.writeValue(getFile(), entities);
        } catch (IOException e) {
            throw new JsonFileException("Errore durante la scrittura del file JSON: " + fileName+ e);
        }
    }
}

