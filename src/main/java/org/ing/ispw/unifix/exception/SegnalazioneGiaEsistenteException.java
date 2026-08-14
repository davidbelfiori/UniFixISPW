package org.ing.ispw.unifix.exception;

public class SegnalazioneGiaEsistenteException extends EntityAlreadyExistsException {
    public SegnalazioneGiaEsistenteException(String message) {
        super(message);
    }

    public SegnalazioneGiaEsistenteException(String message, Throwable cause) {
        super(message, cause);
    }
}
