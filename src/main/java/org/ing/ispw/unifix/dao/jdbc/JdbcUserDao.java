package org.ing.ispw.unifix.dao.jdbc;

import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.exception.SignUpException;
import org.ing.ispw.unifix.model.*;
import org.ing.ispw.unifix.utils.Printer;
import org.ing.ispw.unifix.utils.UserType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JdbcUserDao  implements UserDao {

   

    private static final String EMAIL = "email";
    private static final String NOME = "nome";
    private static final String RUOLO = "ruolo";
    private static final String COGNOME = "cognome";
    private static final String PASSWORD = "password";


    private Connection getConnection() {
        return SingletonConnessione.getInstance();
    }


    @Override
    public User create(String email) {
        return new User(email);
    }



    @Override
    public User load(String id) {
        String query = "SELECT email, password, nome, cognome, ruolo FROM user WHERE email = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String email = rs.getString(EMAIL);
                    String password = rs.getString(PASSWORD);
                    String nome = rs.getString(NOME);
                    String cognome = rs.getString(COGNOME);
                    UserType ruolo = UserType.valueOf(rs.getString(RUOLO));
                    return UserFactory.createUser(email, password, nome, cognome, ruolo, 0);
                }
            }
        }catch (SQLException e) {
            Printer.error("Errore durante il caricamento dell'utente" + e.getMessage());
        }
        return null;
    }

    @Override
    public void store(User entity) {

        if (existsEmail(entity.getEmail())) {
                throw new SignUpException("Email già registrata");
            }
            try (PreparedStatement stmt = getConnection().prepareStatement("INSERT INTO user (email, password, nome, cognome, ruolo) VALUES (?, ?, ?, ?, ?)")) {
                stmt.setString(1, entity.getEmail());
                stmt.setString(2, entity.getPassword());
                stmt.setString(3, entity.getNome());
                stmt.setString(4, entity.getCognome());
                stmt.setString(5, entity.getRuolo().toString());
                stmt.executeUpdate();
                Printer.print("Utente registrato con successo" + entity.getEmail());
            } catch (SQLException e) {
                Printer.error("Errore durante la registrazione dell'utente" + e.getMessage());

            }
    }

    @Override
    public void delete(String id) {
        String query = "DELETE FROM user WHERE email = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Printer.error( "Errore durante l'eliminazione dell'utente"+e.getMessage());
        }
    }

    @Override
    public boolean exists(String identifier) {
        String query = "SELECT COUNT(*) FROM user WHERE  email = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, identifier);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            Printer.error( "Errore durante la verifica dell'esistenza dell'utente"+e.getMessage());
            return false;
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
            Printer.error( "Errore durante il recupero degli utenti"+e.getMessage());
        }
        //se non sono riuscito a recuperali torno una lista vuota
        return Collections.emptyList();
    }

    @Override
    public void update(User entity) {
        String query = "UPDATE user SET password = ?, nome = ?, cognome = ?, ruolo = ? WHERE email = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, entity.getPassword());
            stmt.setString(2, entity.getNome());
            stmt.setString(3, entity.getCognome());
            stmt.setString(4, entity.getRuolo().toString());
            stmt.setString(5, entity.getEmail());
            stmt.executeUpdate();
            Printer.print("Utente aggiornato con successo: " + entity.getEmail());
        } catch (SQLException e) {
            Printer.error("Errore durante l'aggiornamento dell'utente: " + e.getMessage());
        }
    }


    public void update(Tecnico entity) {
        String query = "UPDATE user SET password = ?, nome = ?, cognome = ?, ruolo = ?, numeroSegnalazioni = ? WHERE email = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, entity.getPassword());
            stmt.setString(2, entity.getNome());
            stmt.setString(3, entity.getCognome());
            stmt.setString(4, entity.getRuolo().toString());
            stmt.setInt(5, entity.getNumeroSegnalazioni());
            stmt.setString(6, entity.getEmail());
            stmt.executeUpdate();
            Printer.print("Utente aggiornato con successo: " + entity.getEmail());
        } catch (SQLException e) {
            Printer.error("Errore durante l'aggiornamento dell'utente: " + e.getMessage());
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
                    int numeroSegnalazioni = rs.getInt("numeroSegnalazioni");
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
}
