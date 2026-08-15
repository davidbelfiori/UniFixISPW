package org.ing.ispw.unifix.exception;

public class AulaGiaPresenteException extends EntityAlreadyExistsException {
    public AulaGiaPresenteException(String message) {
        super(message);
    }
    public AulaGiaPresenteException(String message, Throwable cause) {
        super(message, cause);
    }
}
