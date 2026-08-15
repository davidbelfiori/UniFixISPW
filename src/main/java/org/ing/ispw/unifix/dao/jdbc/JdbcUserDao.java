package org.ing.ispw.unifix.dao.jdbc;

import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.exception.EntityAlreadyExistsException;
import org.ing.ispw.unifix.exception.EntityNotFoundException;
import org.ing.ispw.unifix.exception.PersistenceException;
import org.ing.ispw.unifix.model.*;
import org.ing.ispw.unifix.utils.Printer;
import org.ing.ispw.unifix.utils.UserType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserDao  implements UserDao {

   

    private static final String EMAIL = "email";
    private static final String NOME = "nome";
    private static final String RUOLO = "ruolo";
    private static final String COGNOME = "cognome";
    private static final String PASSWORD = "password";
    private static final String NUMERO_SEGNALAZIONI = "numeroSegnalazioni";

    private Connection getConnection() {
        return SingletonConnessione.getInstance();
    }





    @Override
    public User load(String id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "L'email dell'utente non può essere nulla"
            );
        }

        String query = """
            SELECT email, password, nome, cognome, ruolo , numeroSegnalazioni
            FROM user
            WHERE email = ?
            """;

        try (PreparedStatement stmt =
                     getConnection().prepareStatement(query)) {

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                String email = rs.getString(EMAIL);
                String password = rs.getString(PASSWORD);
                String nome = rs.getString(NOME);
                String cognome = rs.getString(COGNOME);
                int numeroSegnalazioni = rs.getInt(NUMERO_SEGNALAZIONI);
                UserType ruolo = UserType.valueOf(rs.getString(RUOLO));

                return UserFactory.createUser(
                        email,
                        password,
                        nome,
                        cognome,
                        ruolo,
                        numeroSegnalazioni
                );
            }

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante il caricamento dell'utente " + id,
                    e
            );
        } catch (IllegalArgumentException e) {
            throw new PersistenceException(
                    "I dati persistiti dell'utente " + id + " non sono validi",
                    e
            );
        }
    }

    @Override
    public void store(User entity) {
        if (entity == null) {
            throw new IllegalArgumentException(
                    "L'utente non può essere nullo"
            );
        }

        String email = entity.getEmail();

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "L'email dell'utente non può essere nulla o vuota"
            );
        }

        String query = """
            INSERT INTO user (email, password, nome, cognome, ruolo)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, entity.getPassword());
            stmt.setString(3, entity.getNome());
            stmt.setString(4, entity.getCognome());
            stmt.setString(5, entity.getRuolo().toString());

            stmt.executeUpdate();
        } catch (SQLException e) {
            if (isDuplicateKey(e)) {
                throw new EntityAlreadyExistsException(
                        "Esiste già un utente con email " + email,
                        e
                );
            }

            throw new PersistenceException(
                    "Errore durante la registrazione dell'utente " + email,
                    e
            );
        }
    }

    @Override
    public void delete(String id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "L'email dell'utente non può essere nulla"
            );
        }

        String query = "DELETE FROM user WHERE email = ?";

        try (PreparedStatement stmt =
                     getConnection().prepareStatement(query)) {

            stmt.setString(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante l'eliminazione dell'utente " + id,
                    e
            );
        }
    }

    @Override
    public boolean exists(String id) {
        if (id == null) {
            throw new IllegalArgumentException("L'email dell'utente non può essere nulla");
        }

        String query = """
            SELECT 1
            FROM user
            WHERE email = ?
            LIMIT 1
            """;

        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante la verifica dell'utente " + id,
                    e
            );
        }
    }

    @Override
    public List<User> loadAll() {
        try (PreparedStatement statement = getConnection().prepareStatement("SELECT email, password, nome, cognome, ruolo FROM user ")){

            try (ResultSet rs = statement.executeQuery()) {
                List<User> user = new ArrayList<>();
                while (rs.next()) {
                    String email = rs.getString(EMAIL);
                    String password = rs.getString(PASSWORD);
                    String nome = rs.getString(NOME);
                    String cognome = rs.getString(COGNOME);
                    UserType ruolo = UserType.valueOf(rs.getString(RUOLO));
                    user.add(UserFactory.createUser(email, password, nome, cognome, ruolo, 0));
                }
                return user;
            }

        }catch (SQLException e) {
           throw new PersistenceException( "Errore durante il recupero degli utenti"+e.getMessage());
        }
    }

    @Override
    public void update(User entity) {
        if (entity == null) {
            throw new IllegalArgumentException(
                    "L'utente non può essere nullo"
            );
        }

        String email = entity.getEmail();

        if (email == null) {
            throw new IllegalArgumentException("L'email dell'utente non può essere nulla");
        }

        String query = """
            UPDATE user
            SET password = ?, nome = ?, cognome = ?, ruolo = ?
            WHERE email = ?
            """;

        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, entity.getPassword());
            stmt.setString(2, entity.getNome());
            stmt.setString(3, entity.getCognome());
            stmt.setString(4, entity.getRuolo().toString());
            stmt.setString(5, email);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0 && !exists(email)) {
                throw new EntityNotFoundException(
                        "Nessun utente trovato con email " + email
                );
            }

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante l'aggiornamento dell'utente " + email,
                    e
            );
        }
    }


    @Override
    public void update(Tecnico entity) {
        if (entity == null) {
            throw new IllegalArgumentException(
                    "Il tecnico non può essere nullo"
            );
        }

        String email = entity.getEmail();

        if (email == null) {
            throw new IllegalArgumentException(
                    "L'email del tecnico non può essere nulla"
            );
        }

        String query = """
            UPDATE user
            SET password = ?,
                nome = ?,
                cognome = ?,
                ruolo = ?,
                numeroSegnalazioni = ?
            WHERE email = ?
            """;

        try (PreparedStatement stmt =
                     getConnection().prepareStatement(query)) {

            stmt.setString(1, entity.getPassword());
            stmt.setString(2, entity.getNome());
            stmt.setString(3, entity.getCognome());
            stmt.setString(4, entity.getRuolo().toString());
            stmt.setInt(5, entity.getNumeroSegnalazioni());
            stmt.setString(6, email);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0 && !exists(email)) {
                throw new EntityNotFoundException(
                        "Nessun tecnico trovato con email " + email
                );
            }

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante l'aggiornamento del tecnico " + email,
                    e
            );
        }
    }

    @Override
    public List<Tecnico> getAllTecnici() {
        try (PreparedStatement statement = getConnection().prepareStatement("SELECT email, password, nome, cognome, numeroSegnalazioni FROM user WHERE ruolo = 'TECNICO'")){

            try (ResultSet rs = statement.executeQuery()) {
                List<Tecnico> tecnici = new ArrayList<>();
                while (rs.next()) {
                    String email = rs.getString(EMAIL);
                    String password = rs.getString(PASSWORD);
                    String nome = rs.getString(NOME);
                    String cognome = rs.getString(COGNOME);
                    int numeroSegnalazioni = rs.getInt(NUMERO_SEGNALAZIONI);
                    tecnici.add(new Tecnico(email, password, nome, cognome, UserType.TECNICO, numeroSegnalazioni));
                }
                return tecnici;
            }

        }catch (SQLException e) {
            Printer.error( "Errore durante la verifica dell'esistenza dell'utente"+e.getMessage());
        }
        return List.of();
    }

    public boolean existsEmail(String email) {
        try (PreparedStatement stmt = getConnection().prepareStatement("SELECT COUNT(*) FROM user WHERE email = ?")) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            Printer.error( "Errore durante la verifica dell'email"+e.getMessage());
        }
        return false; // Return false if there's an exception during the check
    }

    private static boolean isDuplicateKey(SQLException exception) {
        return exception.getErrorCode() == 1062;
    }

}
