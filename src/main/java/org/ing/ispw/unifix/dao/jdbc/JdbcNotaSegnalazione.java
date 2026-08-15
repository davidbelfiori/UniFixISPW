package org.ing.ispw.unifix.dao.jdbc;

import org.ing.ispw.unifix.dao.NotaSegnalazioneDao;
import org.ing.ispw.unifix.exception.*;
import org.ing.ispw.unifix.model.NotaSegnalazione;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.model.Tecnico;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcNotaSegnalazione  implements NotaSegnalazioneDao {

    private Connection getConnection() { return SingletonConnessione.getInstance(); }

    @Override
    public NotaSegnalazione create(String uuid) {
        return new NotaSegnalazione(uuid);
    }

    @Override
    public NotaSegnalazione load(String id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "L'UUID della nota non può essere nullo"
            );
        }

        String query = """
            SELECT
                uuid,
                idsegnalazione AS id,
                datacreazione,
                tecnico AS tec,
                nota
            FROM nota_segnalazione
            WHERE UUID = ?
            """;

        try (PreparedStatement ps =
                     getConnection().prepareStatement(query)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                Segnalazione segnalazione =
                        new Segnalazione(rs.getString("id"));

                Tecnico tecnico =
                        new Tecnico(rs.getString("tec"));

                return new NotaSegnalazione(
                        rs.getString("uuid"),
                        segnalazione,
                        rs.getTimestamp("datacreazione"),
                        tecnico,
                        rs.getString("nota")
                );
            }

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante il caricamento della nota " + id,
                    e
            );
        } catch (IllegalArgumentException e) {
            throw new PersistenceException(
                    "I dati persistiti della nota " + id + " non sono validi",
                    e
            );
        }
    }

    @Override
    public List<NotaSegnalazione> getAllNotaSegnalazioneById(String idSegnalazione) {
        List<NotaSegnalazione> note = new ArrayList<>();
        String query = "SELECT uuid, idsegnalazione, datacreazione as dcreate, tecnico, nota FROM nota_segnalazione WHERE idSegnalazione = ?";
        try {
            try (PreparedStatement ps = getConnection().prepareStatement(query)) {
                ps.setString(1, idSegnalazione);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Segnalazione segnalazione = new Segnalazione(rs.getString("idSegnalazione"));
                        Tecnico tecnico = new Tecnico(rs.getString("tecnico"));
                        note.add(new NotaSegnalazione(
                                rs.getString("UUID"),
                                segnalazione,
                                rs.getTimestamp("dcreate"),
                                tecnico,
                                rs.getString("Nota")
                        ));
                    }
                }
            }
        } catch (SQLException  _) {
            throw new NoteNonTrovateException("Immpossibile trovare la segnalazione con id:"+idSegnalazione);
        }
        return note;
    }

    @Override
    public void store(NotaSegnalazione nota) {
        if (nota == null) {
            throw new IllegalArgumentException(
                    "La nota non può essere nulla"
            );
        }

        String uuid = nota.getUuid();

        if (uuid == null || uuid.isBlank()) {
            throw new IllegalArgumentException(
                    "L'UUID della nota non può essere nullo o vuoto"
            );
        }

        String query = """
            INSERT INTO nota_segnalazione (
                UUID,
                idSegnalazione,
                dataCreazione,
                tecnico,
                Nota
            )
            VALUES (?, ?, ?, ?, ?)
            """;

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, uuid);
            ps.setString(
                    2,
                    nota.getSegnalazione().getIdSegnalazione()
            );
            ps.setTimestamp(3, nota.getDataCreazione());
            ps.setString(4, nota.getTecnico().getEmail());
            ps.setString(5, nota.getTesto());

            ps.executeUpdate();
        } catch (SQLException e) {
            if (isDuplicateKey(e)) {
                throw new EntityAlreadyExistsException(
                        "Esiste già una nota con UUID " + uuid,
                        e
                );
            }

            throw new StoreNotaException(
                    "Impossibile salvare la nota con UUID " + uuid,
                    e
            );
        }
    }

    @Override
    public void delete(String id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "L'UUID della nota non può essere nullo"
            );
        }

        String query =
                "DELETE FROM nota_segnalazione WHERE UUID = ?";

        try (PreparedStatement ps =
                     getConnection().prepareStatement(query)) {

            ps.setString(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante l'eliminazione della nota " + id,
                    e
            );
        }
    }

    @Override
    public boolean exists(String id) {
        if (id == null) {
            throw new IllegalArgumentException("L'UUID della nota non può essere nullo");
        }

        String query = """
            SELECT 1
            FROM nota_segnalazione
            WHERE UUID = ?
            LIMIT 1
            """;

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante la verifica della nota " + id,
                    e
            );
        }
    }

    @Override
    public List<NotaSegnalazione> loadAll() {
        List<NotaSegnalazione> note = new ArrayList<>();
        String query = "SELECT uuid, idsegnalazione, datacreazione, tecnico, nota, email, nome, cognome FROM nota_segnalazione join unifix.user u on u.email = nota_segnalazione.tecnico";
        try {
            try (PreparedStatement ps = getConnection().prepareStatement(query)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Segnalazione segnalazione = new Segnalazione(rs.getString("idSegnalazione"));
                        Tecnico tecnico = new Tecnico(rs.getString("tecnico"), rs.getString("nome"), rs.getString("cognome"));
                        note.add(new NotaSegnalazione(
                                rs.getString("UUID"),
                                segnalazione,
                                rs.getTimestamp("dataCreazione"),
                                tecnico,
                                rs.getString("Nota")
                        ));
                    }
                }
            }
        } catch (SQLException | ErroreLetturaPasswordException e) {
            throw new NoteNonTrovateException("impossibile trovare tutte le note"+e.getMessage());
        }
        return note;
    }

    @Override
    public void update(NotaSegnalazione entity) {
        if (entity == null) {
            throw new IllegalArgumentException(
                    "La nota non può essere nulla"
            );
        }

        String uuid = entity.getUuid();

        if (uuid == null) {
            throw new IllegalArgumentException(
                    "L'UUID della nota non può essere nullo"
            );
        }

        String query = """
            UPDATE nota_segnalazione
            SET idSegnalazione = ?,
                dataCreazione = ?,
                tecnico = ?,
                Nota = ?
            WHERE UUID = ?
            """;

        try (PreparedStatement ps =
                     getConnection().prepareStatement(query)) {

            ps.setString(
                    1,
                    entity.getSegnalazione().getIdSegnalazione()
            );
            ps.setTimestamp(2, entity.getDataCreazione());
            ps.setString(3, entity.getTecnico().getEmail());
            ps.setString(4, entity.getTesto());
            ps.setString(5, uuid);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0 && !exists(uuid)) {
                throw new EntityNotFoundException(
                        "Nessuna nota trovata con UUID " + uuid
                );
            }

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante l'aggiornamento della nota " + uuid,
                    e
            );
        }
    }


    private static boolean isDuplicateKey(SQLException exception) {
        return exception.getErrorCode() == 1062;
    }

}
