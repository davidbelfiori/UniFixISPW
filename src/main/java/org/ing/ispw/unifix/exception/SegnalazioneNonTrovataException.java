package org.ing.ispw.unifix.exception;

/**
 * Eccezione lanciata quando una segnalazione richiesta non viene trovata nel sistema.
 */
public class SegnalazioneNonTrovataException extends EntityNotFoundException {
    public SegnalazioneNonTrovataException(String message) {
        super(message);
    }

    public SegnalazioneNonTrovataException(String message, Throwable cause) {
        super(message, cause);
    }
}

