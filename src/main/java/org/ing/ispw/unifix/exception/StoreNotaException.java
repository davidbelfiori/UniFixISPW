package org.ing.ispw.unifix.exception;

public class StoreNotaException extends PersistenceException {
    public StoreNotaException(String message) {
        super(message);
    }

    public StoreNotaException(String message, Throwable cause) {
        super(message, cause);
    }
}
