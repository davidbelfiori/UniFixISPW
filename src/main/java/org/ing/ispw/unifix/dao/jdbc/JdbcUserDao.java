package org.ing.ispw.unifix.dao.jdbc;

import org.ing.ispw.unifix.dao.UserDao;
import org.ing.ispw.unifix.exception.SignUpException;
import org.ing.ispw.unifix.model.Tecnico;
import org.ing.ispw.unifix.model.User;
import org.ing.ispw.unifix.utils.Printer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcUserDao  implements UserDao {

    private static JdbcUserDao instance;
    private Connection connection;

    public JdbcUserDao() {
        try {
            this.connection =  SingletonConnessione.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
                    User user = new User(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setNome(rs.getString("nome"));
                    user.setCognome(rs.getString("cognome"));
                    user.setRuolo(rs.getString("ruolo"));
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
        try{
            if (existsEmail(entity.getEmail())) {
                throw new SignUpException("Email già registrata");
            }
            try {
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO user (email, password, nome, cognome, ruolo) VALUES (?, ?, ?, ?, ?)");
                stmt.setString(1, entity.getEmail());
                stmt.setString(2, entity.getPassword());
                stmt.setString(3, entity.getNome());
                stmt.setString(4, entity.getCognome());
                stmt.setString(5, entity.getRuolo());
                stmt.executeUpdate();
                Printer.print( "Utente registrato con successo"+entity.getEmail());
            } catch (SQLException e) {
                Printer.error("Errore durante la registrazione dell'utente"+e.getMessage());
            }finally {
                connection.close();
            }
        }catch (Exception e){
            throw new RuntimeException(e);
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
        //da fare se necessario
    }

    @Override
    public List<Tecnico> getAllTecnici() {
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
