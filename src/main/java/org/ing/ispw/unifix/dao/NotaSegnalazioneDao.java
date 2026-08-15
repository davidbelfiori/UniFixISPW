package org.ing.ispw.unifix.dao;

import org.ing.ispw.unifix.model.NotaSegnalazione;

import java.util.List;

/**
 * Definisce la persistenza delle note associate alle segnalazioni.
 * Ogni nota è identificata dal proprio UUID.
 */
public interface NotaSegnalazioneDao extends Dao<String, NotaSegnalazione>{

    /**
     * Crea una nota non ancora persistita.
     *
     * @param uuid identificatore univoco da assegnare alla nota
     * @return nuova nota inizializzata con l'UUID indicato
     * @throws IllegalArgumentException se il costruttore del modello rifiuta l'UUID
     */
    NotaSegnalazione create(String uuid);

    /**
     * Recupera tutte le note appartenenti alla segnalazione indicata.
     *
     * @param idSegnalazione identificatore della segnalazione
     * @return note associate, oppure una lista vuota se non ne esistono
     * @throws IllegalArgumentException se l'identificatore è {@code null}, nei backend che lo validano
     * @throws org.ing.ispw.unifix.exception.PersistenceException se la ricerca fallisce
     */
    List<NotaSegnalazione> getAllNotaSegnalazioneById(String idSegnalazione);

    /**
     * Memorizza una nuova nota e i riferimenti alla segnalazione e all'autore.
     *
     * @param nota nota da persistere
     * @throws IllegalArgumentException se la nota, il suo UUID o i riferimenti obbligatori non sono validi
     * @throws org.ing.ispw.unifix.exception.EntityAlreadyExistsException se esiste già una nota con lo stesso UUID
     * @throws org.ing.ispw.unifix.exception.PersistenceException se la scrittura fallisce
     */
    void store(NotaSegnalazione nota);
}
