package org.ing.ispw.unifix.exception;

public class EntityAlreadyExistsException extends BusinessException {
    public EntityAlreadyExistsException(String message) {
        super(message);
    }

    public EntityAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
