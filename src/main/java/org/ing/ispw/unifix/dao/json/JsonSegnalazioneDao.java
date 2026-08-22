package org.ing.ispw.unifix.dao.json;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.exception.EntityAlreadyExistsException;
import org.ing.ispw.unifix.exception.JsonFileException;
import org.ing.ispw.unifix.model.Docente;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.model.Tecnico;
import org.ing.ispw.unifix.model.User;
import org.ing.ispw.unifix.utils.StatoSegnalazione;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione del DAO segnalazioni basata su file JSON.
 * Serializza i dati della segnalazione e gli identificatori delle entità collegate;
 * gli errori di accesso al file sono esposti come {@code JsonFileException}.
 */
public class JsonSegnalazioneDao extends JsonDao<String, Segnalazione> implements SegnalazioneDao {


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



    public JsonSegnalazioneDao() {
        // La classe base prepara data/json, configura Jackson e associa questo DAO
        // al file segnalazioni.json.
        super(FILE_NAME, Segnalazione.class);

    }

    @Override
    protected String getKey(Segnalazione segnalazione) {
        // L'identificatore della segnalazione è la chiave usata dai metodi CRUD
        // ereditati per confrontare e sostituire gli elementi.
        return segnalazione.getIdSegnalazione();
    }

    @Override
    public Segnalazione create(String idSegnalazione) {
        // create costruisce soltanto l'entità; il file verrà modificato solo dopo
        // una successiva chiamata esplicita a store.
        return new Segnalazione(idSegnalazione);
    }

    @Override
    public Segnalazione load(String id) {
        // La validazione evita una scansione completa del file per una chiave nulla.
        if (id == null) {
            throw new IllegalArgumentException(
                    "L'ID della segnalazione non può essere nullo"
            );
        }

        // Poiché il JSON non dispone di un indice, tutte le segnalazioni vengono
        // ricostruite e poi cercate linearmente tramite il loro ID.
        for (Segnalazione segnalazione : loadAll()) {
            if (id.equals(segnalazione.getIdSegnalazione())) {
                return segnalazione;
            }
        }

        // L'assenza viene rappresentata con null secondo il contratto di Dao.load.
        return null;
    }

    @Override
    public void store(Segnalazione entity) {
        // Si rifiutano input invalidi prima di eseguire letture o scritture su disco.
        if (entity == null) {
            throw new IllegalArgumentException(
                    "La segnalazione non può essere nulla"
            );
        }

        String id = entity.getIdSegnalazione();

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "L'ID della segnalazione non può essere nullo o vuoto"
            );
        }

        // Strategia read-modify-write: loadAll deserializza l'intero array, comprese
        // le relazioni con gli utenti, prima di aggiungere il nuovo elemento.
        List<Segnalazione> segnalazioni = loadAll();

        // Il controllo lineare impedisce due segnalazioni con lo stesso ID;
        // anyMatch interrompe la scansione appena trova una corrispondenza.
        boolean alreadyExists = segnalazioni.stream()
                .anyMatch(segnalazione ->
                        id.equals(segnalazione.getIdSegnalazione())
                );

        if (alreadyExists) {
            throw new EntityAlreadyExistsException(
                    "Esiste già una segnalazione con ID " + id
            );
        }

        // La lista viene modificata in memoria e saveAll riscrive l'intero array:
        // un singolo array JSON non supporta un append diretto e sicuro dopo ']'.
        segnalazioni.add(entity);
        saveAll(segnalazioni);
    }




    @Override
    public List<Segnalazione> loadAll() {
        File file = getFile();

        // Un file non ancora presente equivale a un archivio senza segnalazioni.
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            // readTree mantiene l'array come insieme di nodi, necessario perché i
            // riferimenti a docente e tecnico sono memorizzati come semplici email.
            ArrayNode arrayNode = (ArrayNode) objectMapper.readTree(file);
            List<Segnalazione> segnalazioni = new ArrayList<>();

            // Ogni elemento { ... } viene ricostruito insieme ai riferimenti agli
            // utenti prima di essere restituito come oggetto di dominio completo.
            for (var node : arrayNode) {
                Segnalazione segnalazione = deserializeSegnalazione((ObjectNode) node);
                segnalazioni.add(segnalazione);
            }

            return segnalazioni;
        } catch (IOException e) {
            // Letture fallite e JSON non valido sono tradotti nell'eccezione del
            // livello di persistenza, nascondendo i dettagli di Jackson.
            throw new JsonFileException("Errore durante la lettura del file JSON: " + FILE_NAME+ e);
        }
    }



    @Override
    public List<Segnalazione> getSegnalazioniByDocente(String docenteEmail) {
        // Non essendoci indici nel file, si parte dall'intera collezione già
        // deserializzata e si costruisce una nuova lista con le sole corrispondenze.
        List<Segnalazione> allSegnalazioni = loadAll();
        List<Segnalazione> result = new ArrayList<>();
        for (Segnalazione s : allSegnalazioni) {
            // Il controllo su null gestisce le segnalazioni non associate ad alcun
            // docente ed evita di dereferenziare un riferimento assente.
            if (s.getDocente() != null && s.getDocente().getEmail().equals(docenteEmail)) {
                result.add(s);
            }
        }
        return result;
    }

    @Override
    public List<Segnalazione> getSegnalazioniByTecnico(String tecnicoEmail) {
        // Anche questa ricerca è lineare perché l'archivio JSON non mantiene un
        // indice separato per l'email del tecnico.
        List<Segnalazione> allSegnalazioni = loadAll();
        List<Segnalazione> result = new ArrayList<>();
        for (Segnalazione s : allSegnalazioni) {
            if(s.getTecnico() != null && s.getTecnico().getEmail().equals(tecnicoEmail)) {
                result.add(s);
            }
        }
        return result;
    }

    @Override
    public int countSegnalazioniAttive() {
        // Si carica lo stato corrente e si considerano attive sia le segnalazioni
        // appena aperte sia quelle già prese in lavorazione.
        List<Segnalazione> allSegnalazioni = loadAll();
        int count = 0;
        for (Segnalazione s : allSegnalazioni) {
            if (s.getStato() == StatoSegnalazione.APERTA || s.getStato() == StatoSegnalazione.IN_LAVORAZIONE) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int countSegnalazioniRisolte() {
        // Il conteggio include soltanto lo stato finale CHIUSA.
        List<Segnalazione> allSegnalazioni = loadAll();
        int count = 0;
        for (Segnalazione s : allSegnalazioni) {
            if (s.getStato() == StatoSegnalazione.CHIUSA) {
                count++;
            }
        }
        return count;
    }

    @Override
    protected void saveAll(List<Segnalazione> segnalazioni) {
        try {
            // Si costruisce manualmente l'array per salvare le relazioni tramite
            // identificatori (email) anziché serializzare interi oggetti User.
            ArrayNode arrayNode = objectMapper.createArrayNode();

            for (Segnalazione s : segnalazioni) {
                ObjectNode node = serializeSegnalazione(s);
                arrayNode.add(node);
            }

            // Il file viene sostituito con l'intero array aggiornato; questa scelta
            // è semplice ma ha un costo proporzionale al numero di segnalazioni.
            objectMapper.writeValue(getFile(), arrayNode);
        } catch (IOException e) {
            throw new JsonFileException("Errore durante la scrittura del file JSON: " + FILE_NAME +e);
        }
    }

    private ObjectNode serializeSegnalazione(Segnalazione s) {
        // ObjectNode rappresenta una singola segnalazione come oggetto JSON { ... }.
        ObjectNode node = objectMapper.createObjectNode();

        // I valori semplici vengono copiati direttamente; date ed enum richiedono
        // una rappresentazione stabile, rispettivamente timestamp e nome testuale.
        node.put(FIELD_ID_SEGNALAZIONE, s.getIdSegnalazione());
        if (s.getDataCreazione() != null) {
            node.put(FIELD_DATA_CREAZIONE, s.getDataCreazione().getTime());
        }
        node.put(FIELD_OGGETTO_GUASTO, s.getOggettoGuasto());
        if (s.getStato() != null) {
            node.put(FIELD_STATO, s.getStato().name());
        }
        node.put(FIELD_DESCRIZIONE, s.getDescrizione());
        node.put(FIELD_AULA, s.getAula());
        node.put(FIELD_EDIFICIO, s.getEdificio());

        // Delle entità collegate si salva soltanto la chiave. Questo evita di
        // duplicare nel file i dati completi di docente e tecnico.
        if (s.getDocente() != null) {
            node.put(FIELD_DOCENTE_EMAIL, s.getDocente().getEmail());
        }

        if (s.getTecnico() != null) {
            node.put(FIELD_TECNICO_EMAIL, s.getTecnico().getEmail());
        }

        return node;
    }

    private Segnalazione deserializeSegnalazione(ObjectNode node) {
        // L'ID obbligatorio permette di creare subito l'oggetto di dominio che
        // verrà poi completato con campi semplici e riferimenti esterni.
        String id = node.get(FIELD_ID_SEGNALAZIONE).asText();
        Segnalazione segnalazione = new Segnalazione(id);

        // La ricostruzione è separata in due fasi per distinguere i valori contenuti
        // nel nodo dalle relazioni che richiedono l'accesso ad altri DAO.
        deserializeBasicFields(node, segnalazione);
        deserializeUserReferences(node, segnalazione);

        return segnalazione;
    }

    private void deserializeBasicFields(ObjectNode node, Segnalazione segnalazione) {
        // Ogni proprietà è opzionale: hasValidField evita accessi a nodi assenti
        // o null prima di applicare la conversione nel corrispondente tipo Java.
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
            // Nel JSON è memorizzata soltanto l'email: il DAO utenti ricostruisce
            // l'oggetto completo e il controllo instanceof verifica il ruolo atteso.
            // Questa chiamata rilegge users.json per ogni riferimento e può quindi
            // diventare costosa quando vengono caricate molte segnalazioni.
            String docenteEmail = node.get(FIELD_DOCENTE_EMAIL).asText();
            User user = DaoFactory.getInstance().getUserDao().load(docenteEmail);
            if (user instanceof Docente docente) {
                segnalazione.setDocente(docente);
            }
        }

        if (hasValidField(node, FIELD_TECNICO_EMAIL)) {
            // Come per il docente, la chiave esterna viene risolta tramite UserDao
            // e assegnata solo se identifica realmente un Tecnico.
            String tecnicoEmail = node.get(FIELD_TECNICO_EMAIL).asText();
            User user = DaoFactory.getInstance().getUserDao().load(tecnicoEmail);
            if (user instanceof Tecnico tecnico) {
                segnalazione.setTecnico(tecnico);
            }
        }
    }



    @Override
    public boolean exists(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'identificatore non può essere nullo.");}
        Segnalazione segnalazione;
        segnalazione = load(id);
        if (segnalazione.getStato() == StatoSegnalazione.CHIUSA) {
            return false;
        }
        return super.exists(id);

    }

    private boolean hasValidField(ObjectNode node, String fieldName) {
        // Un campo è utilizzabile soltanto se esiste e non contiene il valore JSON
        // null; centralizzare il controllo evita condizioni duplicate.
        return node.has(fieldName) && !node.get(fieldName).isNull();
    }
}

