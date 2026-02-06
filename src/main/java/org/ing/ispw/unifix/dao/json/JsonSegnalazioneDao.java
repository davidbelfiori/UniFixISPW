package org.ing.ispw.unifix.dao.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.exception.JsonFileException;
import org.ing.ispw.unifix.model.Docente;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.model.Tecnico;
import org.ing.ispw.unifix.model.User;
import org.ing.ispw.unifix.utils.StatoSegnalazione;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class JsonSegnalazioneDao implements SegnalazioneDao {

    private static final String DATA_DIR = "data/json";
    private static final String FILE_NAME = "segnalazioni.json";
    private static final String FIELD_ID_SEGNALAZIONE = "idSegnalazione";
    private static final String FIELD_DATA_CREAZIONE = "dataCreazione";
    private static final String FIELD_OGGETTO_GUASTO = "oggettoGuasto";
    private static final String FIELD_STATO = "stato";
    private static final String FIELD_DESCRIZIONE = "descrizione";
    private static final String FIELD_AULA = "aula";
    private static final String FIELD_EDIFICIO = "edificio";
    private static final String FIELD_DOCENTE_EMAIL = "docenteEmail";
    private static final String FIELD_TECNICO_EMAIL = "tecnicoEmail";

    private final ObjectMapper objectMapper;

    public JsonSegnalazioneDao() {
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

    private File getFile() {
        return new File(DATA_DIR, FILE_NAME);
    }

    @Override
    public Segnalazione create(String idSegnalazione) {
        return new Segnalazione(idSegnalazione);
    }

    @Override
    public Segnalazione load(String id) {
        List<Segnalazione> segnalazioni = loadAll();
        for (Segnalazione s : segnalazioni) {
            if (s.getIdSegnalazione().equals(id)) {
                return s;
            }
        }
        return null;
    }

    @Override
    public void store(Segnalazione entity) {
        List<Segnalazione> segnalazioni = loadAll();
        String key = entity.getIdSegnalazione();

        segnalazioni.removeIf(s -> s.getIdSegnalazione().equals(key));
        segnalazioni.add(entity);

        saveAll(segnalazioni);
    }

    @Override
    public void delete(String id) {
        List<Segnalazione> segnalazioni = loadAll();
        segnalazioni.removeIf(s -> s.getIdSegnalazione().equals(id));
        saveAll(segnalazioni);
    }

    @Override
    public boolean exists(String id) {
        return load(id) != null;
    }

    @Override
    public List<Segnalazione> loadAll() {
        File file = getFile();
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            ArrayNode arrayNode = (ArrayNode) objectMapper.readTree(file);
            List<Segnalazione> segnalazioni = new ArrayList<>();

            for (var node : arrayNode) {
                Segnalazione segnalazione = deserializeSegnalazione((ObjectNode) node);
                segnalazioni.add(segnalazione);
            }

            return segnalazioni;
        } catch (IOException e) {
            throw new JsonFileException("Errore durante la lettura del file JSON: " + FILE_NAME+ e);
        }
    }

    @Override
    public void update(Segnalazione entity) {
        if (!exists(entity.getIdSegnalazione())) {
            throw new IllegalArgumentException("Impossibile aggiornare: segnalazione con ID " + entity.getIdSegnalazione() + " non trovata.");
        }
        store(entity);
    }

    @Override
    public List<Segnalazione> getAllSegnalazioni() {
        return loadAll();
    }

    @Override
    public Segnalazione getSegnalazione(String idSegnalazione) {
        return load(idSegnalazione);
    }

    private void saveAll(List<Segnalazione> segnalazioni) {
        try {
            ArrayNode arrayNode = objectMapper.createArrayNode();

            for (Segnalazione s : segnalazioni) {
                ObjectNode node = serializeSegnalazione(s);
                arrayNode.add(node);
            }

            objectMapper.writeValue(getFile(), arrayNode);
        } catch (IOException e) {
            throw new JsonFileException("Errore durante la scrittura del file JSON: " + FILE_NAME +e);
        }
    }

    private ObjectNode serializeSegnalazione(Segnalazione s) {
        ObjectNode node = objectMapper.createObjectNode();

        node.put(FIELD_ID_SEGNALAZIONE, s.getIdSegnalazione());
        if (s.getDataCreazione() != null) {
            node.put(FIELD_DATA_CREAZIONE, s.getDataCreazione().getTime());
        }
        node.put(FIELD_OGGETTO_GUASTO, s.getOggettoGuasto());
        node.put(FIELD_STATO, s.getStato().name());
        node.put(FIELD_DESCRIZIONE, s.getDescrizione());
        node.put(FIELD_AULA, s.getAula());
        node.put(FIELD_EDIFICIO, s.getEdificio());

        if (s.getDocente() != null) {
            node.put(FIELD_DOCENTE_EMAIL, s.getDocente().getEmail());
        }

        if (s.getTecnico() != null) {
            node.put(FIELD_TECNICO_EMAIL, s.getTecnico().getEmail());
        }

        return node;
    }

    private Segnalazione deserializeSegnalazione(ObjectNode node) {
        String id = node.get(FIELD_ID_SEGNALAZIONE).asText();
        Segnalazione segnalazione = new Segnalazione(id);

        deserializeBasicFields(node, segnalazione);
        deserializeUserReferences(node, segnalazione);

        return segnalazione;
    }

    private void deserializeBasicFields(ObjectNode node, Segnalazione segnalazione) {
        if (hasValidField(node, FIELD_DATA_CREAZIONE)) {
            segnalazione.setDataCreazione(new Date(node.get(FIELD_DATA_CREAZIONE).asLong()));
        }
        if (hasValidField(node, FIELD_OGGETTO_GUASTO)) {
            segnalazione.setOggettoGuasto(node.get(FIELD_OGGETTO_GUASTO).asText());
        }
        if (hasValidField(node, FIELD_STATO)) {
            segnalazione.setStato(StatoSegnalazione.fromString(node.get(FIELD_STATO).asText()));
        }
        if (hasValidField(node, FIELD_DESCRIZIONE)) {
            segnalazione.setDescrizione(node.get(FIELD_DESCRIZIONE).asText());
        }
        if (hasValidField(node, FIELD_AULA)) {
            segnalazione.setAula(node.get(FIELD_AULA).asText());
        }
        if (hasValidField(node, FIELD_EDIFICIO)) {
            segnalazione.setEdificio(node.get(FIELD_EDIFICIO).asText());
        }
    }

    private void deserializeUserReferences(ObjectNode node, Segnalazione segnalazione) {
        if (hasValidField(node, FIELD_DOCENTE_EMAIL)) {
            String docenteEmail = node.get(FIELD_DOCENTE_EMAIL).asText();
            User user = DaoFactory.getInstance().getUserDao().load(docenteEmail);
            if (user instanceof Docente docente) {
                segnalazione.setDocente(docente);
            }
        }

        if (hasValidField(node, FIELD_TECNICO_EMAIL)) {
            String tecnicoEmail = node.get(FIELD_TECNICO_EMAIL).asText();
            User user = DaoFactory.getInstance().getUserDao().load(tecnicoEmail);
            if (user instanceof Tecnico tecnico) {
                segnalazione.setTecnico(tecnico);
            }
        }
    }

    private boolean hasValidField(ObjectNode node, String fieldName) {
        return node.has(fieldName) && !node.get(fieldName).isNull();
    }
}

