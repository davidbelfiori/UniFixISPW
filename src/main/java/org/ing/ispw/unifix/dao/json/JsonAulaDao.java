package org.ing.ispw.unifix.dao.json;

import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.model.Aula;
import org.ing.ispw.unifix.model.AulaId;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione del DAO delle aule basata sul supporto JSON generico.
 * Usa la coppia codice aula-edificio come chiave e calcola edifici e conteggi
 * a partire dal contenuto completo del file.
 */
public class JsonAulaDao
        extends JsonDao<AulaId, Aula>
        implements AulaDao {

    public JsonAulaDao() {
        // La classe base userà aule.json come archivio e Aula.class per
        // ricostruire gli oggetti durante la deserializzazione.
        super("aule.json", Aula.class);
    }

    @Override
    public Aula create(String idAula) {
        // create costruisce l'oggetto di dominio ma non lo salva: la persistenza
        // avverrà soltanto quando il chiamante invocherà store.
        return new Aula(idAula);
    }

    @Override
    protected AulaId getKey(Aula aula) {
        // Il solo codice dell'aula non basta a identificarla: la chiave composta
        // include anche l'edificio per distinguere aule omonime in sedi diverse.
        return new AulaId(
                aula.getIdAula(),
                aula.getEdificio()
        );
    }


    @Override
    public List<Aula> getAuleByEdificio(String edificio) {
        if(edificio.trim().isEmpty()) {
            throw new IllegalArgumentException("L'edificio non può essere vuoto");
        }
        List<Aula> aule = new ArrayList<>();

        for (Aula aula : loadAll()) {
            if (edificio.equals(aula.getEdificio())) {
                aule.add(aula);
            }
        }

        return aule;
    }

    @Override
    public List<String> getAllEdifici() {
        List<String> edifici = new ArrayList<>();

        // loadAll deserializza tutte le aule perché nel file JSON non esiste un
        // indice separato degli edifici.
        for (Aula aula : loadAll()) {
            String edificio = aula.getEdificio();

            // Si ignorano valori null e duplicati, così ogni edificio compare una
            // sola volta nel risultato restituito al chiamante.
            if (edificio != null && !edifici.contains(edificio)) {
                edifici.add(edificio);
            }
        }

        return edifici;
    }

    @Override
    public List<String> getAulaOggetti(AulaId id) {
        // load usa la chiave composta per recuperare la singola aula dal file.
        Aula aula = load(id);

        // Una lista vuota semplifica il chiamante: non deve distinguere tra aula
        // assente, collezione non inizializzata e aula senza oggetti.
        if (aula == null || aula.getOggetti() == null) {
            return new ArrayList<>();
        }

        // La copia difensiva impedisce al chiamante di modificare direttamente la
        // collezione interna dell'entità senza passare da update.
        return new ArrayList<>(aula.getOggetti());
    }

    @Override
    public int countAule() {
        // Poiché ogni elemento del file rappresenta un'aula, la dimensione della
        // lista deserializzata coincide con il numero totale delle aule.
        return loadAll().size();
    }

    @Override
    public int countEdificiGestiti() {
        // Si riusa getAllEdifici per contare soltanto gli edifici distinti ed
        // evitare di duplicare la logica di esclusione dei null e dei duplicati.
        return getAllEdifici().size();
    }
}
