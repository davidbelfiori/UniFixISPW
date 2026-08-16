package org.ing.ispw.unifix.dao.json;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.exception.EntityAlreadyExistsException;
import org.ing.ispw.unifix.exception.JsonFileException;
import org.ing.ispw.unifix.model.*;
import org.ing.ispw.unifix.utils.UserType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione del DAO utenti basata sul file {@code data/json/users.json}.
 * Conserva nel JSON il tipo concreto dell'utente e segnala problemi di lettura,
 * scrittura o creazione della directory tramite {@code JsonFileException}.
 */
public class JsonUserDao extends JsonDao<String, User> implements UserDao {


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


    public JsonUserDao() {
        super(FILE_NAME, User.class);
    }

    @Override
    protected String getKey(User user) {
        return user.getEmail();
    }



    @Override
    public User load(String id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "L'email dell'utente non può essere nulla"
            );
        }

        for (User user : loadAll()) {
            if (id.equals(user.getEmail())) {
                return user;
            }
        }

        return null;
    }

    @Override
    public void store(User entity) {
        if (entity == null) {
            throw new IllegalArgumentException("L'utente non può essere nullo");
        }

        String email = entity.getEmail();

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "L'email dell'utente non può essere nulla o vuota"
            );
        }

        List<User> users = loadAll();

        boolean alreadyExists = users.stream()
                .anyMatch(user -> email.equals(user.getEmail()));

        if (alreadyExists) {
            throw new EntityAlreadyExistsException(
                    "Esiste già un utente con email " + email
            );
        }

        users.add(entity);
        saveAll(users);
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

    @Override
    public void saveAll(List<User> users) {
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
        String email = getStringField(node, FIELD_EMAIL);
        String password = getStringField(node, FIELD_PASSWORD);
        String nome = getStringField(node, FIELD_NOME);
        String cognome = getStringField(node, FIELD_COGNOME);

        UserType ruolo = null;
        if (node.has(FIELD_RUOLO) && !node.get(FIELD_RUOLO).isNull()) {
            ruolo = UserType.valueOf(node.get(FIELD_RUOLO).asText());
        }
        int numeroSegnalazioni = 0;
        if (node.has(FIELD_NUMERO_SEGNALAZIONI)) {
            numeroSegnalazioni = node.get(FIELD_NUMERO_SEGNALAZIONI).asInt();
        }
        return UserFactory.createUser(email, password, nome, cognome, ruolo, numeroSegnalazioni);
    }

    private String getStringField(ObjectNode node, String field) {
        if (node.has(field) && !node.get(field).isNull()) {
            return node.get(field).asText();
        }
        return null;
    }
}

