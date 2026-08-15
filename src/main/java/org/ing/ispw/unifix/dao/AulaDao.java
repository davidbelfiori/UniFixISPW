package org.ing.ispw.unifix.dao;

import org.ing.ispw.unifix.model.Aula;
import org.ing.ispw.unifix.model.AulaId;

import java.util.List;

/**
 * Definisce le operazioni di persistenza e le interrogazioni specifiche per le aule.
 * Ogni aula è identificata dalla coppia composta da codice aula ed edificio.
 */
public interface AulaDao extends Dao<AulaId, Aula> {

    /**
     * Crea un'istanza di aula non ancora persistita.
     *
     * @param idAula codice da assegnare alla nuova aula
     * @return una nuova aula inizializzata con il codice indicato
     * @throws IllegalArgumentException se il costruttore del modello rifiuta il codice fornito
     */
    Aula create(String idAula);

    /**
     * Recupera i nomi distinti degli edifici nei quali sono presenti aule gestite.
     *
     * @return lista degli edifici; vuota se non sono presenti aule
     * @throws org.ing.ispw.unifix.exception.PersistenceException se la sorgente dati non è accessibile
     */
    List<String> getAllEdifici();

    /**
     * Recupera gli oggetti associati a una specifica aula.
     *
     * @param aulaId identificatore composto dell'aula
     * @return lista degli oggetti presenti; vuota se l'aula non ha oggetti
     * @throws IllegalArgumentException se l'identificatore è {@code null}, nei backend che lo validano
     * @throws org.ing.ispw.unifix.exception.PersistenceException se il recupero dalla sorgente dati fallisce
     */
    List<String> getAulaOggetti(AulaId aulaId);

    /**
     * Conta tutte le aule persistite.
     *
     * @return numero complessivo delle aule
     * @throws org.ing.ispw.unifix.exception.PersistenceException se il conteggio non può essere eseguito
     */
    int countAule();

    /**
     * Conta gli edifici distinti ai quali appartengono le aule gestite.
     *
     * @return numero di edifici distinti
     * @throws org.ing.ispw.unifix.exception.PersistenceException se il conteggio non può essere eseguito
     */
    int countEdificiGestiti();
}
