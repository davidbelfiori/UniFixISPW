package org.ing.ispw.unifix.dao.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.ing.ispw.unifix.dao.Dao;
import org.ing.ispw.unifix.exception.EntityAlreadyExistsException;
import org.ing.ispw.unifix.exception.EntityNotFoundException;
import org.ing.ispw.unifix.exception.JsonFileException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione CRUD generica che serializza una collezione di entità in un file JSON.
 * Ogni modifica riscrive l'intera collezione; gli errori di filesystem o parsing vengono
 * tradotti in {@code JsonFileException}.
 *
 * @param <K> tipo della chiave dell'entità
 * @param <E> tipo dell'entità serializzata
 */
public abstract class JsonDao<K, E> implements Dao<K, E> {

    private static final String DATA_DIR = "data/json";
    protected final ObjectMapper objectMapper;
    private final String fileName;
    private final Class<E> entityClass;

    /**
     * Configura il file e il tipo usato da Jackson per deserializzare le entità.
     *
     * @param fileName nome del file nella directory dei dati JSON
     * @param entityClass classe concreta da serializzare e deserializzare
     * @throws org.ing.ispw.unifix.exception.JsonFileException se la directory dei dati non può essere creata
     */
    protected JsonDao(String fileName, Class<E> entityClass) {
        // Ogni DAO comunica alla classe base quale file usare e quale tipo concreto
        // ricostruire: Jackson perde infatti l'informazione sul generico E a runtime.
        this.fileName = fileName;
        this.entityClass = entityClass;

        // Un Object Mapper per DAO centralizza conversione Java <-> JSON;
        // INDENT_OUTPUT rende il file leggibile anche manualmente.
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // La directory viene preparata subito, così le successive scritture non
        // falliscono soltanto perché data/json non è ancora presente.
        ensureDataDirectoryExists();
    }


    private void ensureDataDirectoryExists() {
        try {
            Path dataPath = Paths.get(DATA_DIR);

            // createDirectories crea anche eventuali directory intermedie ed è
            // eseguito solo quando il percorso non esiste già.
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
            }
        } catch (IOException e) {
            throw new JsonFileException("Impossibile creare la directory per i dati JSON"+ e);
        }
    }

    /**
     * Restituisce il file associato a questo DAO.
     *  Impostato la visubilità a protected in quanto è un operazione untilizzata sono dalle sottoclassi
     * @return file JSON collocato nella directory dati
     */
    protected File getFile() {
        // Il percorso viene composto in un solo punto per evitare che ogni DAO
        // concreto replichi la conoscenza della directory data/json.
        return new File(DATA_DIR, fileName);
    }

    /**
     * Estrae la chiave identificativa da un'entità.
     *
     * @param entity entità da esaminare
     * @return chiave usata per confronti e operazioni CRUD
     */
    protected abstract K getKey(E entity);

    @Override
    public E load(K id) {
        // Un identificatore nullo non può essere confrontato in modo significativo
        // e spesso indica un errore del chiamante.
        if (id == null) {
            throw new IllegalArgumentException("L'identificatore non può essere nullo");
        }

        // Il backend usa un unico array JSON: per trovare un elemento deve quindi
        // caricare la collezione e fare una ricerca lineare sulla chiave.
        for (E entity : loadAll()) {
            if (id.equals(getKey(entity))) {
                // Il primo elemento con la chiave richiesta è quello persistito;
                // store impedisce normalmente la presenza di duplicati.
                return entity;
            }
        }

        // Il contratto del DAO rappresenta l'assenza con null anziché sollevare
        // EntityNotFoundException durante una semplice lettura.
        return null;
    }

    @Override
    public void store(E entity) {
        // Si valida prima di leggere il file per evitare I/O inutile quando
        // l'oggetto ricevuto non può essere persistito.
        if (entity == null) {
            throw new IllegalArgumentException("L'entità non può essere nulla");
        }

        // La classe concreta decide come estrarre la chiave (per esempio email,
        // UUID oppure la chiave composta AulaId).
        K key = getKey(entity);

        if (key == null) {
            throw new IllegalArgumentException("La chiave dell'entità non può essere nulla");
        }

        // Per mantenere un array JSON valido si applica una strategia
        // read-modify-write: si carica lo stato corrente, lo si modifica e lo si
        // riscrive. È semplice, ma il costo cresce con il numero di elementi.
        List<E> entities = loadAll();

        // La ricerca impedisce di salvare due entità con la stessa chiave.
        // anyMatch termina appena trova la prima corrispondenza.
        boolean alreadyExists = entities.stream()
                .anyMatch(existing -> key.equals(getKey(existing)));

        if (alreadyExists) {
            throw new EntityAlreadyExistsException(
                    "Esiste già un'entità con chiave " + key
            );
        }

        // L'aggiunta avviene sulla lista in memoria; il file viene aggiornato
        // soltanto dalla successiva chiamata a saveAll.
        entities.add(entity);

        // Un array JSON non consente un append sicuro dopo la parentesi ']':
        // per questo viene serializzata nuovamente l'intera collezione.
        saveAll(entities);
    }

    @Override
    public void delete(K id) {
        // La validazione evita che removeIf debba gestire un confronto con null.
        if (id == null) {
            throw new IllegalArgumentException("L'identificatore non può essere nullo");

        }

        // Anche la cancellazione parte dalla rappresentazione completa in memoria,
        // perché gli elementi sono contenuti nello stesso array JSON.
        List<E> entities = loadAll();

        // removeIf elimina l'eventuale elemento con la chiave richiesta e restituisce
        // true solo se la lista è stata realmente modificata.
        boolean removed = entities.removeIf(
                entity -> id.equals(getKey(entity))
        );

        // Se la chiave non esiste non si riscrive il file, evitando un'operazione
        // di I/O che non cambierebbe lo stato persistito.
        if (removed) {
            saveAll(entities);
        }
    }

    @Override
    public boolean exists(K id) {
        // Si riusa la stessa semantica di load: un risultato diverso da null indica
        // che nel file è presente un'entità con quella chiave.
        return load(id) != null;
    }

    @Override
    public List<E> loadAll() {
        File file = getFile();

        // Un file non ancora creato rappresenta un archivio vuoto, non un errore.
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            // A runtime List<V> non conserva il tipo V (type erasure). CollectionType
            // comunica a Jackson che deve costruire una lista di entityClass.
            CollectionType listType = objectMapper.getTypeFactory()
                    .constructCollectionType(ArrayList.class, entityClass);

            // readValue legge l'intero array JSON e crea in memoria tutte le entità.
            return objectMapper.readValue(file, listType);
        } catch (IOException e) {
            // Errori di lettura e JSON malformato vengono tradotti nell'eccezione
            // di persistenza usata dal resto dell'applicazione.
            throw new JsonFileException("Errore durante la lettura del file JSON: " + fileName+ e);
        }
    }

    @Override
    public void update(E entity) {
        // Come in store, si controlla l'input prima di accedere al filesystem.
        if (entity == null) {
            throw new IllegalArgumentException(
                    "L'entità non può essere nulla"
            );
        }

        K key = getKey(entity);

        if (key == null) {
            throw new IllegalArgumentException(
                    "La chiave dell'entità non può essere nulla"
            );
        }

        // Poiché il file contiene un array unico, l'aggiornamento viene effettuato
        // sostituendo l'elemento nella copia in memoria.
        List<E> entities = loadAll();

        for (int i = 0; i < entities.size(); i++) {
            if (key.equals(getKey(entities.get(i)))) {
                // Si conserva la posizione dell'elemento nel file, sostituendo solo
                // il valore associato alla stessa chiave.
                entities.set(i, entity);

                // La modifica diventa persistente riscrivendo l'intero array JSON.
                saveAll(entities);
                return;
            }
        }

        // A differenza di delete, update segnala l'assenza perché non avrebbe senso
        // dichiarare riuscito un aggiornamento che non ha modificato nulla.
        throw new EntityNotFoundException(
                "Nessuna entità trovata con chiave " + key
        );
    }

    /**
     * Riscrive nel file l'intera collezione fornita.
     * Impostata la visubilità a protected in quanto è un operazione untilizzata sono dalle sottoclassi
     * @param entities entità da serializzare
     * @throws org.ing.ispw.unifix.exception.JsonFileException se la scrittura del file fallisce
     */
    protected void saveAll(List<E> entities) {
        try {
            // Jackson converte la lista in un array JSON e sostituisce il contenuto
            // del file. Questa scelta privilegia semplicità e leggibilità rispetto
            // alle prestazioni su collezioni molto grandi.
            objectMapper.writeValue(getFile(), entities);
        } catch (IOException e) {
            // Il chiamante vede un errore uniforme di persistenza e non deve conoscere
            // le eccezioni specifiche del filesystem o di Jackson.
            throw new JsonFileException("Errore durante la scrittura del file JSON: " + fileName+ e);
        }
    }
}

