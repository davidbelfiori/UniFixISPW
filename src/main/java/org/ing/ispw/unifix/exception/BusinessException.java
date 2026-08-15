package org.ing.ispw.unifix.exception;

/**
 * Categoria base delle eccezioni dovute a regole applicative o di dominio.
 *
 * <p>Comprende condizioni previste come entità già esistenti o non trovate,
 * transizioni di stato non consentite e dati non validi per il caso d'uso.
 * La view può intercettare una sottoclasse per mostrare un messaggio specifico
 * oppure questa classe per gestire in modo uniforme gli altri errori di
 * business. Non deve essere usata per rappresentare errori tecnici di accesso
 * al database o ai file.</p>
 */
public abstract class BusinessException extends UniFixException {

    protected BusinessException(String message) {
        super(message);
    }

    protected BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
