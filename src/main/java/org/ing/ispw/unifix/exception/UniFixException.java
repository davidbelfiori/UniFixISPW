package org.ing.ispw.unifix.exception;

/**
 * Radice comune di tutte le eccezioni personalizzate di UniFix.
 *
 * <p>Estende {@link RuntimeException} per consentire la propagazione degli
 * errori tra DAO, controller applicativi e view senza obbligare ogni metodo a
 * dichiararli nella clausola {@code throws}. Le view dovrebbero normalmente
 * intercettare una categoria più specifica, come {@link BusinessException} o
 * {@link PersistenceException}; questa classe può essere usata soltanto come
 * fallback generale al confine dell'applicazione.</p>
 */
public abstract class UniFixException extends RuntimeException {

    protected UniFixException(String message) {
        super(message);
    }

    protected UniFixException(String message, Throwable cause) {
        super(message, cause);
    }
}
