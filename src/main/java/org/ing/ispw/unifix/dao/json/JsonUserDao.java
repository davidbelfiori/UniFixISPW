package org.ing.ispw.unifix.dao.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.exception.JsonFileException;
import org.ing.ispw.unifix.model.Docente;
import org.ing.ispw.unifix.model.Sysadmin;
import org.ing.ispw.unifix.model.Tecnico;
import org.ing.ispw.unifix.model.User;
import org.ing.ispw.unifix.utils.UserType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JsonUserDao implements UserDao {

    private static final String DATA_DIR = "data/json";
    private static final String FILE_NAME = "users.json";
    private static final String FIELD_TYPE = "_type";
    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_PASSWORD = "password";
    private static final String FIELD_NOME = "nome";
    private static final String FIELD_COGNOME = "cognome";
    private static final String FIELD_RUOLO = "ruolo";
    private static final String FIELD_NUMERO_SEGNALAZIONI = "numeroSegnalazioni";

    private static final String TYPE_USER = "User";
    private static final String TYPE_DOCENTE = "Docente";
    private static final String TYPE_TECNICO = "Tecnico";
    private static final String TYPE_SYSADMIN = "Sysadmin";

    private final ObjectMapper objectMapper;

    public JsonUserDao() {
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
            throw new JsonFileException("Impossibile creare la directory per i dati JSON" + e);
        }
    }

    private File getFile() {
        return new File(DATA_DIR, FILE_NAME);
    }

    @Override
    public User create(String email) {
        return new User(email);
    }

    @Override
    public User load(String id) {
        List<User> users = loadAll();
        for (User user : users) {
            if (user.getEmail().equals(id)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void store(User entity) {
        List<User> users = loadAll();
        String key = entity.getEmail();

        users.removeIf(u -> u.getEmail().equals(key));
        users.add(entity);

        saveAll(users);
    }

    @Override
    public void delete(String id) {
        List<User> users = loadAll();
        users.removeIf(u -> u.getEmail().equals(id));
        saveAll(users);
    }

    @Override
    public boolean exists(String id) {
        return load(id) != null;
    }

    @Override
    public List<User> loadAll() {
        File file = getFile();
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            ArrayNode arrayNode = (ArrayNode) objectMapper.readTree(file);
            List<User> users = new ArrayList<>();

            for (var node : arrayNode) {
                User user = deserializeUser((ObjectNode) node);
                users.add(user);
            }

            return users;
        } catch (IOException e) {
            throw new JsonFileException("Errore durante la lettura del file JSON: " + FILE_NAME+ e);
        }
    }

    @Override
    public void update(User entity) {
        if (!exists(entity.getEmail())) {
            throw new IllegalArgumentException("Impossibile aggiornare: utente con email " + entity.getEmail() + " non trovato.");
        }
        store(entity);
    }

    @Override
    public List<Tecnico> getAllTecnici() {
        return loadAll().stream()
                .filter(Tecnico.class::isInstance)
                .map(Tecnico.class::cast)
                .toList();
    }

    @Override
    public void update(Tecnico tecnico) {
        update((User) tecnico);
    }

    private void saveAll(List<User> users) {
        try {
            ArrayNode arrayNode = objectMapper.createArrayNode();

            for (User user : users) {
                ObjectNode node = serializeUser(user);
                arrayNode.add(node);
            }

            objectMapper.writeValue(getFile(), arrayNode);
        } catch (IOException e) {
            throw new JsonFileException("Errore durante la scrittura del file JSON: " + FILE_NAME + e);
        }
    }

    private ObjectNode serializeUser(User user) {
        ObjectNode node = objectMapper.createObjectNode();

        // Determina il tipo
        String type;
        if (user instanceof Tecnico) {
            type = TYPE_TECNICO;
        } else if (user instanceof Docente) {
            type = TYPE_DOCENTE;
        } else if (user instanceof Sysadmin) {
            type = TYPE_SYSADMIN;
        } else {
            type = TYPE_USER;
        }

        node.put(FIELD_TYPE, type);
        node.put(FIELD_EMAIL, user.getEmail());
        node.put(FIELD_PASSWORD, user.getPassword());
        node.put(FIELD_NOME, user.getNome());
        node.put(FIELD_COGNOME, user.getCognome());

        if (user.getRuolo() != null) {
            node.put(FIELD_RUOLO, user.getRuolo().name());
        }

        // Campi specifici per Tecnico
        if (user instanceof Tecnico tecnico) {
            node.put(FIELD_NUMERO_SEGNALAZIONI, tecnico.getNumeroSegnalazioni());
        }

        return node;
    }

    private User deserializeUser(ObjectNode node) {
        String type = node.has(FIELD_TYPE) ? node.get(FIELD_TYPE).asText() : TYPE_USER;
        String email = getStringField(node, FIELD_EMAIL);
        String password = getStringField(node, FIELD_PASSWORD);
        String nome = getStringField(node, FIELD_NOME);
        String cognome = getStringField(node, FIELD_COGNOME);
        UserType ruolo = null;

        if (node.has(FIELD_RUOLO) && !node.get(FIELD_RUOLO).isNull()) {
            ruolo = UserType.valueOf(node.get(FIELD_RUOLO).asText());
        }

        User user;
        switch (type) {
            case TYPE_TECNICO:
                Tecnico tecnico = new Tecnico(email, password, nome, cognome, ruolo, 0);
                if (node.has(FIELD_NUMERO_SEGNALAZIONI)) {
                    tecnico.setNumeroSegnalazioni(node.get(FIELD_NUMERO_SEGNALAZIONI).asInt());
                }
                user = tecnico;
                break;
            case TYPE_DOCENTE:
                user = new Docente(email, password, nome, cognome, ruolo);
                break;
            case TYPE_SYSADMIN:
                user = new Sysadmin(email, password, nome, cognome, ruolo);
                break;
            default:
                user = new User(email, password, nome, cognome, ruolo);
                break;
        }

        return user;
    }

    private String getStringField(ObjectNode node, String field) {
        if (node.has(field) && !node.get(field).isNull()) {
            return node.get(field).asText();
        }
        return null;
    }
}

