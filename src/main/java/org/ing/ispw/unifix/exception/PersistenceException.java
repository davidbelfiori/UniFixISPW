package org.ing.ispw.unifix.exception;

/**
 * Categoria base degli errori tecnici verificatisi durante la persistenza.
 *
 * <p>I DAO devono convertire eccezioni infrastrutturali come
 * {@code SQLException} e {@code IOException} in questa eccezione o in una sua
 * sottoclasse, conservando la causa originale. I controller applicativi
 * normalmente la lasciano propagare, mentre la view la intercetta per mostrare
 * un messaggio di errore tecnico senza confonderlo con una violazione delle
 * regole di business.</p>
 */
public class PersistenceException extends UniFixException {
    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
