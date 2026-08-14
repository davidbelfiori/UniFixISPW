package org.ing.ispw.unifix.dao;

import org.ing.ispw.unifix.exception.EntityAlreadyExistsException;
import org.ing.ispw.unifix.exception.EntityNotFoundException;
import org.ing.ispw.unifix.exception.PersistenceException;

import java.util.List;
/**
 * Utilizza i Generici di Java per definire un contratto CRUD comune per tutte le entità.
 *
 * @param <K> Tipo di dato della chiave primaria / identificatore univoco dell'entità (es. String, Long)
 * @param <E> Tipo della classe di dominio (Entità) gestita dal DAO (es. User, Aula, Segnalazione)
 */
public interface Dao <K,E>{


    /**
     * Recupera l'entità associata all'identificatore indicato.
     *
     * @param id identificatore dell'entità da recuperare
     * @return l'entità trovata, oppure {@code null} se non esiste
     * @throws IllegalArgumentException se l'identificatore è {@code null}
     * @throws PersistenceException se si verifica un errore durante
     *                              l'accesso alla persistenza
     */
    E load(K id);

    /**
     * Memorizza una nuova entità.
     *
     * @param entity entità da memorizzare
     * @throws IllegalArgumentException se l'entità o la sua chiave sono
     *                                  {@code null}
     * @throws EntityAlreadyExistsException se esiste già un'entità con
     *                                      la stessa chiave
     * @throws PersistenceException se si verifica un errore durante
     *                              la memorizzazione
     */
    void store(E entity);

    /**
     * Elimina l'entità associata all'identificatore indicato.
     *
     * <p>Se l'entità non esiste, il metodo non produce effetti.</p>
     *
     * @param id identificatore dell'entità da eliminare
     * @throws IllegalArgumentException se l'identificatore è {@code null}
     * @throws PersistenceException se si verifica un errore durante
     *                              l'eliminazione
     */
    void delete(K id);

    /**
     * Verifica se esiste un'entità con l'identificatore indicato.
     *
     * @param id identificatore dell'entità da verificare
     * @return {@code true} se l'entità esiste, altrimenti {@code false}
     * @throws IllegalArgumentException se l'identificatore è {@code null}
     * @throws PersistenceException se non è possibile completare la verifica
     */
    boolean exists(K id);

    /**
     * Recupera tutte le entità disponibili.
     *
     * @return una lista contenente tutte le entità; la lista è vuota se non
     *         sono presenti entità e non è mai {@code null}
     * @throws PersistenceException se si verifica un errore durante
     *                              il recupero
     */
    List<E> loadAll();

    /**
     * Aggiorna un'entità già esistente.
     *
     * @param entity entità contenente i dati aggiornati
     * @throws IllegalArgumentException se l'entità o la sua chiave sono
     *                                  {@code null}
     * @throws EntityNotFoundException se l'entità da aggiornare non esiste
     * @throws PersistenceException se si verifica un errore durante
     *                              l'aggiornamento
     */
    void update(E entity);
}
