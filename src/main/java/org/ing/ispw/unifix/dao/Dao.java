package org.ing.ispw.unifix.dao;

import java.util.List;
/**
 * Utilizza i Generici di Java per definire un contratto CRUD comune per tutte le entità.
 *
 * @param <K> Tipo di dato della chiave primaria / identificatore univoco dell'entità (es. String, Long)
 * @param <E> Tipo della classe di dominio (Entità) gestita dal DAO (es. User, Aula, Segnalazione)
 */
public interface Dao <K,E>{

    E load(K id);
    void store(E entity);
    void delete(K id); 
    boolean exists(K id);
    List<E>  loadAll();
    void update(E entity);
}
