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
        // La classe base gestisce il percorso e l'ObjectMapper; User.class indica
        // il tipo generale contenuto nell'archivio users.json.
        super(FILE_NAME, User.class);
    }

    @Override
    protected String getKey(User user) {
        // L'email identifica univocamente ogni utente nelle operazioni CRUD comuni.
        return user.getEmail();
    }



    @Override
    public User load(String id) {
        // Un'email nulla non identifica alcun utente e viene trattata come errore
        // del chiamante prima di effettuare accessi al file.
        if (id == null) {
            throw new IllegalArgumentException(
                    "L'email dell'utente non può essere nulla"
            );
        }

        // users.json è un array privo di indice: loadAll ricostruisce gli utenti e
        // questa scansione lineare cerca quello con l'email richiesta.
        for (User user : loadAll()) {
            if (id.equals(user.getEmail())) {
                return user;
            }
        }

        // Il contratto di load rappresenta con null un utente non presente.
        return null;
    }

    @Override
    public void store(User entity) {
        // La validazione anticipata evita di leggere users.json quando l'oggetto
        // ricevuto non può comunque essere persistito.
        if (entity == null) {
            throw new IllegalArgumentException("L'utente non può essere nullo");
        }

        String email = entity.getEmail();

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "L'email dell'utente non può essere nulla o vuota"
            );
        }

        // Si usa read-modify-write: l'intero array viene caricato per controllare
        // l'unicità e aggiungere il nuovo utente alla lista in memoria.
        List<User> users = loadAll();

        // anyMatch termina alla prima email uguale e impedisce chiavi duplicate.
        boolean alreadyExists = users.stream()
                .anyMatch(user -> email.equals(user.getEmail()));

        if (alreadyExists) {
            throw new EntityAlreadyExistsException(
                    "Esiste già un utente con email " + email
            );
        }

        // L'aggiunta modifica soltanto la lista in memoria; saveAll rende persistente
        // lo stato riscrivendo l'array JSON completo.
        users.add(entity);
        saveAll(users);
    }


    @Override
    public List<User> loadAll() {
        File file = getFile();

        // Se users.json non è stato ancora creato, l'archivio è considerato vuoto.
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            // readTree mantiene la struttura a nodi: serve perché ogni elemento può
            // rappresentare un sottotipo diverso di User.
            ArrayNode arrayNode = (ArrayNode) objectMapper.readTree(file);
            List<User> users = new ArrayList<>();

            // Ogni ObjectNode, cioè ogni oggetto { ... } dell'array, viene convertito
            // nel corretto oggetto di dominio e inserito nella lista risultante.
            for (var node : arrayNode) {
                User user = deserializeUser((ObjectNode) node);
                users.add(user);
            }

            return users;
        } catch (IOException e) {
            // Errori del filesystem e JSON malformato vengono esposti al resto
            // dell'applicazione come errori uniformi di persistenza.
            throw new JsonFileException("Errore durante la lettura del file JSON: " + FILE_NAME+ e);
        }
    }



    @Override
    public List<Tecnico> getAllTecnici() {
        // loadAll restituisce sottotipi diversi: prima si filtrano le sole istanze
        // Tecnico, poi si esegue un cast reso sicuro dal filtro precedente.
        return loadAll().stream()
                .filter(Tecnico.class::isInstance)
                .map(Tecnico.class::cast)
                .toList();
    }

    @Override
    public void update(Tecnico tecnico) {
        // Tecnico è un User: il cast permette di riusare update(User) ereditato
        // dalla classe base senza duplicare l'algoritmo di aggiornamento.
        update((User) tecnico);
    }

    @Override
    protected void saveAll(List<User> users) {
        try {
            // L'array viene costruito manualmente perché deve conservare anche le
            // informazioni necessarie a distinguere Docente, Tecnico e Sysadmin.
            ArrayNode arrayNode = objectMapper.createArrayNode();

            for (User user : users) {
                ObjectNode node = serializeUser(user);
                arrayNode.add(node);
            }

            /*write Value sostituisce users.json con l'intera collezione aggiornata;
             non aggiunge soltanto l'ultimo elemento in coda al file.*/
            objectMapper.writeValue(getFile(), arrayNode);
        } catch (IOException e) {
            throw new JsonFileException("Errore durante la scrittura del file JSON: " + FILE_NAME + e);
        }
    }

    private ObjectNode serializeUser(User user) {
        // ObjectNode rappresenta un singolo oggetto JSON delimitato da { e }.
        ObjectNode node = objectMapper.createObjectNode();

        // Il tipo concreto viene salvato perché l'informazione sul sottotipo non
        // deve andare persa quando tutti gli elementi sono trattati come User.
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

        // Questi campi sono comuni a tutti i sottotipi e vengono quindi sempre
        // trasferiti dall'oggetto Java al nodo JSON.
        node.put(FIELD_TYPE, type);
        node.put(FIELD_EMAIL, user.getEmail());
        node.put(FIELD_PASSWORD, user.getPassword());
        node.put(FIELD_NOME, user.getNome());
        node.put(FIELD_COGNOME, user.getCognome());

        if (user.getRuolo() != null) {
            node.put(FIELD_RUOLO, user.getRuolo().name());
        }

        // numeroSegnalazioni appartiene soltanto a Tecnico e viene scritto solo
        // quando l'oggetto possiede effettivamente quel dato.
        if (user instanceof Tecnico tecnico) {
            node.put(FIELD_NUMERO_SEGNALAZIONI, tecnico.getNumeroSegnalazioni());
        }

        return node;
    }

    private User deserializeUser(ObjectNode node) {
        // Si estraggono prima i campi comuni; l'helper restituisce null quando una
        // proprietà è assente o contiene esplicitamente null.
        String email = getStringField(node, FIELD_EMAIL);
        String password = getStringField(node, FIELD_PASSWORD);
        String nome = getStringField(node, FIELD_NOME);
        String cognome = getStringField(node, FIELD_COGNOME);

        // Il ruolo consente alla UserFactory di scegliere il sottotipo da creare.
        UserType ruolo = null;
        if (node.has(FIELD_RUOLO) && !node.get(FIELD_RUOLO).isNull()) {
            ruolo = UserType.valueOf(node.get(FIELD_RUOLO).asText());
        }
        // I tipi diversi da Tecnico non hanno questo campo e mantengono il valore 0.
        int numeroSegnalazioni = 0;
        if (node.has(FIELD_NUMERO_SEGNALAZIONI)) {
            numeroSegnalazioni = node.get(FIELD_NUMERO_SEGNALAZIONI).asInt();
        }
        // La factory centralizza la costruzione di Docente, Tecnico, Sysadmin o User,
        // evitando di replicare nel DAO la logica di scelta della classe concreta.
        return UserFactory.createUser(email, password, nome, cognome, ruolo, numeroSegnalazioni);
    }

    private String getStringField(ObjectNode node, String field) {
        // Il doppio controllo evita NullPointerException quando il campo non esiste
        // oppure nel JSON è presente con valore null.
        if (node.has(field) && !node.get(field).isNull()) {
            return node.get(field).asText();
        }
        return null;
    }
}

