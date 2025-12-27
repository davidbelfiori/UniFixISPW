package org.ing.ispw.unifix.exception;

/**
 * Eccezione lanciata quando una segnalazione non ha un tecnico assegnato.
 */
public class TecnicoNonAssegnatoException extends RuntimeException {
    public TecnicoNonAssegnatoException(String message) {
        super(message);
    }
}

