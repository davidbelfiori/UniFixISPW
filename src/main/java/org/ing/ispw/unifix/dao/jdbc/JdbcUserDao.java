package org.ing.ispw.unifix.dao.jdbc;

import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.exception.DbConnException;
import org.ing.ispw.unifix.exception.SignUpException;
import org.ing.ispw.unifix.model.Docente;
import org.ing.ispw.unifix.model.Sysadmin;
import org.ing.ispw.unifix.model.Tecnico;
import org.ing.ispw.unifix.model.User;
import org.ing.ispw.unifix.utils.Printer;
import org.ing.ispw.unifix.utils.UserType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserDao  implements UserDao {

    private static JdbcUserDao instance;
    private Connection connection;

    private static final String ACTION_1 = "email";

    public JdbcUserDao() {
        try {
            this.connection =  SingletonConnessione.getInstance();
        } catch (SQLException e) {
            throw new DbConnException("Impossibile connettersi al database:"+e.getMessage());
        }
    }

    public static JdbcUserDao getInstance(){
        if(instance == null){
            instance = new JdbcUserDao();
        }
        return instance;
    }

    @Override
    public User create(String email) {

        return null;
    }



    @Override
    public User load(String id) {
        String query = "SELECT email, password, nome, cognome, ruolo FROM user WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String ruolo = rs.getString("ruolo");
                    User user;
                    switch (ruolo){
                        case "DOCENTE":
                            user = new Docente(rs.getString(ACTION_1));
                            user.setRuolo(UserType.DOCENTE);
                            break;
                        case "TECNICO":
                            user = new Tecnico(rs.getString(ACTION_1));
                            user.setRuolo(UserType.TECNICO);
                            break;
                        case "SYSADMIN":
                            user = new Sysadmin(rs.getString(ACTION_1));
                            user.setRuolo(UserType.SYSADMIN);
                            break;
                        default:
                            user = new User(rs.getString(ACTION_1));
                            user.setRuolo(UserType.UNKNOWN);
                            break;
                    }
                    user.setPassword(rs.getString("password"));
                    user.setNome(rs.getString("nome"));
                    user.setCognome(rs.getString("cognome"));
                    return user;
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
            try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO user (email, password, nome, cognome, ruolo) VALUES (?, ?, ?, ?, ?)")) {
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
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Printer.error( "Errore durante l'eliminazione dell'utente"+e.getMessage());
        }
    }

    @Override
    public boolean exists(String identifier) {
        String query = "SELECT COUNT(*) FROM user WHERE  email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
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
        return List.of();
    }

    @Override
    public void update(User entity) {
        String query = "UPDATE user SET password = ?, nome = ?, cognome = ?, ruolo = ? WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
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
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
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
        try (PreparedStatement statement = connection.prepareStatement("SELECT email, password, nome, cognome, numeroSegnalazioni FROM user WHERE ruolo = 'TECNICO'")){

            try (ResultSet rs = statement.executeQuery()) {
                List<Tecnico> tecnici = new ArrayList<>();
                while (rs.next()) {
                    String email = rs.getString(ACTION_1);
                    String password = rs.getString("password");
                    String nome = rs.getString("nome");
                    String cognome = rs.getString("cognome");
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
        try (PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM user WHERE email = ?")) {
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
