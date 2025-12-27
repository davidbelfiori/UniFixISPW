package org.ing.ispw.unifix.dao.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.NotaSegnalazioneDao;
import org.ing.ispw.unifix.exception.JsonFileException;
import org.ing.ispw.unifix.model.NotaSegnalazione;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.model.Tecnico;
import org.ing.ispw.unifix.model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class JsonNotaSegnalazioneDao implements NotaSegnalazioneDao {

    private static final String DATA_DIR = "data/json";
    private static final String FILE_NAME = "note_segnalazioni.json";
    private static JsonNotaSegnalazioneDao instance;

    private static final String FIELD_TESTO = "testo";
    private static final String FIELD_ID_SEGNALAZIONE = "idSegnalazione";
    private static final String FIELD_TECNICO_EMAIL = "tecnicoEmail";
    private static final String FIELD_DATA_CREAZIONE = "dataCreazione";


    private final ObjectMapper objectMapper;

    private JsonNotaSegnalazioneDao() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        ensureDataDirectoryExists();
    }

    public static synchronized JsonNotaSegnalazioneDao getInstance() {
        if (instance == null) {
            instance = new JsonNotaSegnalazioneDao();
        }
        return instance;
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

    private File getFile() {
        return new File(DATA_DIR, FILE_NAME);
    }

    @Override
    public NotaSegnalazione create(String uuid) {
        return new NotaSegnalazione(uuid);
    }

    @Override
    public NotaSegnalazione load(String id) {
        List<NotaSegnalazione> note = loadAll();
        for (NotaSegnalazione nota : note) {
            if (nota.getUuid().equals(id)) {
                return nota;
            }
        }
        return null;
    }

    @Override
    public void store(NotaSegnalazione entity) {
        List<NotaSegnalazione> note = loadAll();
        String key = entity.getUuid();

        note.removeIf(n -> n.getUuid().equals(key));
        note.add(entity);

        saveAll(note);
    }

    @Override
    public void delete(String id) {
        List<NotaSegnalazione> note = loadAll();
        note.removeIf(n -> n.getUuid().equals(id));
        saveAll(note);
    }

    @Override
    public boolean exists(String id) {
        return load(id) != null;
    }

    @Override
    public List<NotaSegnalazione> loadAll() {
        File file = getFile();
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            ArrayNode arrayNode = (ArrayNode) objectMapper.readTree(file);
            List<NotaSegnalazione> note = new ArrayList<>();

            for (var node : arrayNode) {
                NotaSegnalazione nota = deserializeNotaSegnalazione((ObjectNode) node);
                note.add(nota);
            }

            return note;
        } catch (IOException e) {
            throw new JsonFileException("Errore durante la lettura del file JSON: " + FILE_NAME + e);
        }
    }

    @Override
    public void update(NotaSegnalazione entity) {
        if (!exists(entity.getUuid())) {
            throw new IllegalArgumentException("Impossibile aggiornare: nota con UUID " + entity.getUuid() + " non trovata.");
        }
        store(entity);
    }

    @Override
    public List<NotaSegnalazione> getAllNotaSegnalazioneById(String idSegnalazione) {
        List<NotaSegnalazione> result = new ArrayList<>();
        for (NotaSegnalazione nota : loadAll()) {
            if (nota.getSegnalazione() != null &&
                nota.getSegnalazione().getIdSegnalzione().equals(idSegnalazione)) {
                result.add(nota);
            }
        }
        return result;
    }

    private void saveAll(List<NotaSegnalazione> note) {
        try {
            ArrayNode arrayNode = objectMapper.createArrayNode();

            for (NotaSegnalazione n : note) {
                ObjectNode node = serializeNotaSegnalazione(n);
                arrayNode.add(node);
            }

            objectMapper.writeValue(getFile(), arrayNode);
        } catch (IOException e) {
            throw new JsonFileException("Errore durante la scrittura del file JSON: " + FILE_NAME+ e);
        }
    }

    private ObjectNode serializeNotaSegnalazione(NotaSegnalazione n) {
        ObjectNode node = objectMapper.createObjectNode();

        node.put("uuid", n.getUuid());
        if (n.getDataCreazione() != null) {
            node.put(FIELD_DATA_CREAZIONE, n.getDataCreazione().getTime());
        }
        node.put(FIELD_TESTO, n.getTesto());

        if (n.getSegnalazione() != null) {
            node.put(FIELD_ID_SEGNALAZIONE, n.getSegnalazione().getIdSegnalzione());
        }

        if (n.getTecnico() != null) {
            node.put(FIELD_TECNICO_EMAIL, n.getTecnico().getEmail());
        }

        return node;
    }

    private NotaSegnalazione deserializeNotaSegnalazione(ObjectNode node) {
        String uuid = node.get("uuid").asText();
        NotaSegnalazione nota = new NotaSegnalazione(uuid);

        if (node.has(FIELD_DATA_CREAZIONE) && !node.get(FIELD_DATA_CREAZIONE).isNull()) {
            nota.setDataCreazione(new Timestamp(node.get(FIELD_DATA_CREAZIONE).asLong()));
        }
        if (node.has(FIELD_TESTO) && !node.get(FIELD_TESTO).isNull()) {
            nota.setTesto(node.get(FIELD_TESTO).asText());
        }

        // Carica riferimento alla segnalazione
        if (node.has(FIELD_ID_SEGNALAZIONE) && !node.get(FIELD_ID_SEGNALAZIONE).isNull()) {
            String idSegnalazione = node.get(FIELD_ID_SEGNALAZIONE).asText();
            Segnalazione segnalazione = DaoFactory.getInstance().getSegnalazioneDao().load(idSegnalazione);
            nota.setSegnalazione(segnalazione);
        }

        // Carica riferimento al tecnico
        if (node.has(FIELD_TECNICO_EMAIL) && !node.get(FIELD_TECNICO_EMAIL).isNull()) {
            String tecnicoEmail = node.get(FIELD_TECNICO_EMAIL).asText();
            User user = DaoFactory.getInstance().getUserDao().load(tecnicoEmail);
            if (user instanceof Tecnico tecnico) {
                nota.setTecnico(tecnico);
            }
        }

        return nota;
    }
}

