package org.ing.ispw.unifix.exception;

public class DbConnException extends PersistenceException {
    public DbConnException(String message) {
        super(message);
    }

    public DbConnException(String message, Throwable cause) {
        super(message, cause);
    }
}

