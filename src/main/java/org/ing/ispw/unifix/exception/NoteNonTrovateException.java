package org.ing.ispw.unifix.exception;

public class NoteNonTrovateException extends PersistenceException {
    public NoteNonTrovateException(String message) {
        super(message);
    }

    public NoteNonTrovateException(String message, Throwable cause) {
        super(message, cause);
    }
}
