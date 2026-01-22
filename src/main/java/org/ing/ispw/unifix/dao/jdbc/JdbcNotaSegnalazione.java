package org.ing.ispw.unifix.dao.jdbc;

import org.ing.ispw.unifix.dao.NotaSegnalazioneDao;
import org.ing.ispw.unifix.exception.DbConnException;
import org.ing.ispw.unifix.exception.ErroreLetturaPasswordException;
import org.ing.ispw.unifix.exception.NoteNonTrovateException;
import org.ing.ispw.unifix.exception.StoreNotaException;
import org.ing.ispw.unifix.model.NotaSegnalazione;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.model.Tecnico;
import org.ing.ispw.unifix.utils.Printer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcNotaSegnalazione  implements NotaSegnalazioneDao {

    private final Connection connection;


    public JdbcNotaSegnalazione() {
        try {
            this.connection =  SingletonConnessione.getInstance();
        } catch (SQLException _) {
            throw new DbConnException("Impossibile connettersi al database");
        }
    }

    @Override
    public NotaSegnalazione create(String uuid) {
        return new NotaSegnalazione(uuid);
    }

    @Override
    public NotaSegnalazione load(String id) {
        String query = "SELECT uuid, idsegnalazione as id, datacreazione, tecnico as tec, nota FROM nota_segnalazione WHERE UUID = ?";
        try {
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Segnalazione segnalazione = new Segnalazione(rs.getString("id"));
                        Tecnico tecnico = new Tecnico(rs.getString("tec"));
                        return new NotaSegnalazione(
                                rs.getString("UUID"),
                                segnalazione,
                                rs.getTimestamp("dataCreazione"),
                                tecnico,
                                rs.getString("Nota")
                        );
                    }
                }
            }
        } catch (SQLException _) {
            throw new NoteNonTrovateException("Immpossibile trovare la nota con UUID"+id);
        }
        return null;
    }

    @Override
    public List<NotaSegnalazione> getAllNotaSegnalazioneById(String idSegnalazione) {
        List<NotaSegnalazione> note = new ArrayList<>();
        String query = "SELECT uuid, idsegnalazione, datacreazione as dcreate, tecnico, nota FROM nota_segnalazione WHERE idSegnalazione = ?";
        try {
            try (PreparedStatement ps = connection.prepareStatement(query)) {
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
        String query = "INSERT INTO nota_segnalazione (UUID, idSegnalazione, dataCreazione, tecnico, Nota) VALUES (?, ?, ?, ?, ?)";
        try {
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, nota.getUuid());
                ps.setString(2, nota.getSegnalazione().getIdSegnalzione());
                ps.setTimestamp(3, nota.getDataCreazione());
                ps.setString(4, nota.getTecnico().getEmail());
                ps.setString(5, nota.getTesto());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
           throw new StoreNotaException("impossibile salvare la nota:"+e.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        String query = "DELETE FROM nota_segnalazione WHERE UUID = ?";
        try {
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, id);
                ps.executeUpdate();
            }
        } catch (SQLException _) {
            Printer.error("impossibile eliminare la nota");
        }
    }

    @Override
    public boolean exists(String id) {
        String query = "SELECT UUID FROM nota_segnalazione WHERE UUID = ?";
        try {
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException | ErroreLetturaPasswordException _) {
            throw new NoteNonTrovateException("Non è stato possibile trovare la nota con UUID:"+id);
        }
    }

    @Override
    public List<NotaSegnalazione> loadAll() {
        List<NotaSegnalazione> note = new ArrayList<>();
        String query = "SELECT uuid, idsegnalazione, datacreazione, tecnico, nota, email, nome, cognome FROM nota_segnalazione join unifix.user u on u.email = nota_segnalazione.tecnico";
        try {
            try (PreparedStatement ps = connection.prepareStatement(query)) {
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
        String query = "UPDATE nota_segnalazione SET idSegnalazione = ?, dataCreazione = ?, tecnico = ?, Nota = ? WHERE UUID = ?";
        try {
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, entity.getSegnalazione().getIdSegnalzione());
                ps.setTimestamp(2, entity.getDataCreazione());
                ps.setString(3, entity.getTecnico().getEmail());
                ps.setString(4, entity.getTesto());
                ps.setString(5, entity.getUuid());
                ps.executeUpdate();
            }
        } catch (SQLException | ErroreLetturaPasswordException e) {
            throw new NoteNonTrovateException("impossibile aggiornare la nota"+e.getMessage());
        }
    }
}
