package org.ing.ispw.unifix.dao;

import org.ing.ispw.unifix.model.Tecnico;
import org.ing.ispw.unifix.model.User;

import java.util.List;

/**
 * Definisce le operazioni di persistenza per gli utenti, identificati dall'indirizzo email.
 */
public interface UserDao extends Dao<String, User> {

    /**
     * Recupera tutti e soli gli utenti aventi ruolo di tecnico.
     *
     * @return lista dei tecnici; vuota se non ne esistono
     * @throws org.ing.ispw.unifix.exception.PersistenceException se la lettura dei dati fallisce
     */
    List<Tecnico> getAllTecnici();

    /**
     * Aggiorna i dati e il carico di lavoro di un tecnico già persistito.
     *
     * @param tecnico tecnico contenente i valori aggiornati
     * @throws IllegalArgumentException se il tecnico o la sua email non sono validi
     * @throws org.ing.ispw.unifix.exception.EntityNotFoundException se il tecnico non esiste
     * @throws org.ing.ispw.unifix.exception.PersistenceException se l'aggiornamento fallisce
     */
    void update(Tecnico tecnico);
}
