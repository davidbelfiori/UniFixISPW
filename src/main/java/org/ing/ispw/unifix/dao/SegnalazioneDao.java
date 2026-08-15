package org.ing.ispw.unifix.dao;

import org.ing.ispw.unifix.model.Segnalazione;

import java.util.List;

/**
 * Definisce le operazioni di persistenza e ricerca per le segnalazioni.
 * Le segnalazioni sono identificate dal relativo codice univoco.
 */
public interface SegnalazioneDao extends Dao<String, Segnalazione> {

    /**
     * Crea una segnalazione non ancora persistita.
     *
     * @param idSegnalazione identificatore da assegnare
     * @return nuova segnalazione inizializzata con l'identificatore fornito
     * @throws IllegalArgumentException se il costruttore del modello rifiuta l'identificatore
     */
    Segnalazione create(String idSegnalazione);

    /**
     * Recupera le segnalazioni aperte dal docente indicato.
     *
     * @param docenteEmail email del docente usata come criterio di ricerca
     * @return segnalazioni associate al docente; lista vuota in assenza di corrispondenze
     * @throws IllegalArgumentException se l'email è {@code null}, nei backend che la validano
     * @throws org.ing.ispw.unifix.exception.PersistenceException se la ricerca fallisce
     */
    List<Segnalazione> getSegnalazioniByDocente(String docenteEmail);

    /**
     * Recupera le segnalazioni assegnate al tecnico indicato.
     *
     * @param tecnicoEmail email del tecnico usata come criterio di ricerca
     * @return segnalazioni associate al tecnico; lista vuota in assenza di corrispondenze
     * @throws IllegalArgumentException se l'email è {@code null}, nei backend che la validano
     * @throws org.ing.ispw.unifix.exception.PersistenceException se la ricerca fallisce
     */
    List<Segnalazione> getSegnalazioniByTecnico(String tecnicoEmail);

    /**
     * Conta le segnalazioni aperte o in lavorazione.
     *
     * @return numero di segnalazioni attive
     * @throws org.ing.ispw.unifix.exception.PersistenceException se il conteggio fallisce
     */
    int countSegnalazioniAttive();

    /**
     * Conta le segnalazioni chiuse.
     *
     * @return numero di segnalazioni risolte
     * @throws org.ing.ispw.unifix.exception.PersistenceException se il conteggio fallisce
     */
    int countSegnalazioniRisolte();
}
