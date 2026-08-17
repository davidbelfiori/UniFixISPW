package org.ing.ispw.unifix.dao.json;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.NotaSegnalazioneDao;
import org.ing.ispw.unifix.exception.EntityAlreadyExistsException;
import org.ing.ispw.unifix.exception.JsonFileException;
import org.ing.ispw.unifix.model.NotaSegnalazione;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.model.Tecnico;
import org.ing.ispw.unifix.model.User;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione del DAO delle note basata su file JSON.
 * Persistendo una nota conserva anche i riferimenti necessari a ricostruirne autore
 * e segnalazione; gli errori I/O sono tradotti in {@code JsonFileException}.
 */
public class JsonNotaSegnalazioneDao extends JsonDao<String, NotaSegnalazione> implements NotaSegnalazioneDao {


    private static final String FILE_NAME = "note_segnalazioni.json";
    private static final String FIELD_TESTO = "testo";
    private static final String FIELD_ID_SEGNALAZIONE = "idSegnalazione";
    private static final String FIELD_TECNICO_EMAIL = "tecnicoEmail";
    private static final String FIELD_DATA_CREAZIONE = "dataCreazione";



    public JsonNotaSegnalazioneDao() {
        // La classe base configura Jackson e associa il DAO al file dedicato alle
        // note, indicando anche il tipo generale degli oggetti persistiti.
        super(FILE_NAME, NotaSegnalazione.class);
    }



    @Override
    protected String getKey(NotaSegnalazione entity) {
        // L'UUID è la chiave univoca usata dai metodi CRUD per distinguere le note.
        return entity.getUuid();
    }

    @Override
    public NotaSegnalazione create(String uuid) {
        // create prepara l'entità in memoria ma non scrive il file: per persisterla
        // il chiamante dovrà invocare successivamente store.
        return new NotaSegnalazione(uuid);
    }


    @Override
    public void store(NotaSegnalazione entity) {
        // Gli input non validi vengono rifiutati prima di leggere il file, evitando
        // operazioni di I/O che non potrebbero produrre un salvataggio valido.
        if (entity == null) {
            throw new IllegalArgumentException(
                    "La nota non può essere nulla"
            );
        }

        String uuid = entity.getUuid();

        if (uuid == null || uuid.isBlank()) {
            throw new IllegalArgumentException(
                    "L'UUID della nota non può essere nullo o vuoto"
            );
        }

        // La strategia read-modify-write carica tutte le note e le relative entità
        // collegate prima di aggiungere il nuovo elemento.
        List<NotaSegnalazione> note = loadAll();

        // La scansione impedisce la presenza di due note con lo stesso UUID;
        // anyMatch termina non appena trova la prima corrispondenza.
        boolean alreadyExists = note.stream()
                .anyMatch(nota -> uuid.equals(nota.getUuid()));

        if (alreadyExists) {
            throw new EntityAlreadyExistsException(
                    "Esiste già una nota con UUID " + uuid
            );
        }

        // La nuova nota viene aggiunta alla lista in memoria, poi saveAll riscrive
        // l'intero array JSON per conservarne la struttura valida.
        note.add(entity);
        saveAll(note);
    }


    @Override
    public List<NotaSegnalazione> loadAll() {
        File file = getFile();

        // L'assenza del file indica che non è stata ancora persistita alcuna nota.
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            // readTree conserva la struttura a nodi perché segnalazione e tecnico
            // sono memorizzati tramite le loro chiavi, non come oggetti annidati.
            ArrayNode arrayNode = (ArrayNode) objectMapper.readTree(file);
            List<NotaSegnalazione> note = new ArrayList<>();

            // Ogni ObjectNode dell'array viene trasformato in una nota completa,
            // risolvendo anche i riferimenti alle altre entità.
            for (var node : arrayNode) {
                NotaSegnalazione nota = deserializeNotaSegnalazione((ObjectNode) node);
                note.add(nota);
            }

            return note;
        } catch (IOException e) {
            // Gli errori tecnici di lettura o parsing vengono tradotti in un errore
            // uniforme del livello di persistenza.
            throw new JsonFileException("Errore durante la lettura del file JSON: " + FILE_NAME + e);
        }
    }


    @Override
    public List<NotaSegnalazione> getAllNotaSegnalazioneById(String idSegnalazione) {
        List<NotaSegnalazione> result = new ArrayList<>();

        // Non esiste un indice per segnalazione: si caricano tutte le note e si
        // selezionano quelle il cui riferimento possiede l'ID richiesto.
        for (NotaSegnalazione nota : loadAll()) {
            // Il controllo su null gestisce note prive di riferimento ed evita una
            // NullPointerException durante l'accesso all'ID della segnalazione.
            if (nota.getSegnalazione() != null &&
                nota.getSegnalazione().getIdSegnalazione().equals(idSegnalazione)) {
                result.add(nota);
            }
        }
        return result;
    }

    @Override
    protected void saveAll(List<NotaSegnalazione> note) {
        try {
            // L'array viene costruito manualmente per decidere esattamente quali
            // dati della nota e quali sole chiavi delle relazioni salvare.
            ArrayNode arrayNode = objectMapper.createArrayNode();

            for (NotaSegnalazione n : note) {
                ObjectNode node = serializeNotaSegnalazione(n);
                arrayNode.add(node);
            }

            // writeValue sostituisce il contenuto del file con lo stato completo
            // della lista; non effettua un append della sola nuova nota.
            objectMapper.writeValue(getFile(), arrayNode);
        } catch (IOException e) {
            throw new JsonFileException("Errore durante la scrittura del file JSON: " + FILE_NAME, e);
        }
    }

    private ObjectNode serializeNotaSegnalazione(NotaSegnalazione n) {
        // ObjectNode rappresenta una singola nota come oggetto JSON { ... }.
        ObjectNode node = objectMapper.createObjectNode();

        // UUID, data e testo sono dati propri della nota e vengono copiati nel nodo;
        // la data è trasformata in millisecondi per avere un valore JSON numerico.
        node.put("uuid", n.getUuid());
        if (n.getDataCreazione() != null) {
            node.put(FIELD_DATA_CREAZIONE, n.getDataCreazione().getTime());
        }
        node.put(FIELD_TESTO, n.getTesto());

        // Delle entità collegate si memorizzano soltanto le chiavi, evitando di
        // duplicare nel file della nota tutti i dati di segnalazione e tecnico.
        if (n.getSegnalazione() != null) {
            node.put(FIELD_ID_SEGNALAZIONE, n.getSegnalazione().getIdSegnalazione());
        }

        if (n.getTecnico() != null) {
            node.put(FIELD_TECNICO_EMAIL, n.getTecnico().getEmail());
        }

        return node;
    }

    private NotaSegnalazione deserializeNotaSegnalazione(ObjectNode node) {
        // L'UUID obbligatorio consente di creare l'entità prima di valorizzare gli
        // altri campi opzionali presenti nel nodo.
        String uuid = node.get("uuid").asText();
        NotaSegnalazione nota = new NotaSegnalazione(uuid);

        // Ogni campo opzionale viene controllato prima della conversione, così un
        // valore assente o null non provoca errori durante la lettura.
        if (node.has(FIELD_DATA_CREAZIONE) && !node.get(FIELD_DATA_CREAZIONE).isNull()) {
            nota.setDataCreazione(new Timestamp(node.get(FIELD_DATA_CREAZIONE).asLong()));
        }
        if (node.has(FIELD_TESTO) && !node.get(FIELD_TESTO).isNull()) {
            nota.setTesto(node.get(FIELD_TESTO).asText());
        }

        // Nel file è presente solo l'ID: SegnalazioneDao viene quindi interrogato
        // per ricostruire l'oggetto completo da assegnare alla nota. Questa scelta
        // mantiene il JSON normalizzato, ma aggiunge ulteriori letture durante loadAll.
        if (node.has(FIELD_ID_SEGNALAZIONE) && !node.get(FIELD_ID_SEGNALAZIONE).isNull()) {
            String idSegnalazione = node.get(FIELD_ID_SEGNALAZIONE).asText();
            Segnalazione segnalazione = DaoFactory.getInstance().getSegnalazioneDao().load(idSegnalazione);
            nota.setSegnalazione(segnalazione);
        }

        // Anche il tecnico viene risolto dalla sola email; instanceof impedisce di
        // assegnare per errore un User che non appartiene al sottotipo Tecnico.
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

