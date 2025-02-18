package org.ing.ispw.unifix.dao.jdbc;


import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.model.Aula;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcAulaDao   implements AulaDao {
    private static JdbcAulaDao instance;
    private Connection  connection;
    public JdbcAulaDao() {
        try {
            this.connection =  SingletonConnessione.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static JdbcAulaDao getInstance(){
        if(instance == null){
            instance = new JdbcAulaDao();
        }
        return instance;
    }


    @Override
    public Aula create(String idAula) {
        return new Aula(idAula);
    }

    @Override
    public Aula load(String id) {
        return null;
    }

    @Override
    public void store(Aula entity) {
//        if (entity == null) {
//            throw new IllegalArgumentException("Aula non valida");
//        }
        String insertAulaQuery = "INSERT INTO aule (IdAula, Edificio, Piano) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE Edificio = VALUES(Edificio), Piano = VALUES(Piano)";

        String insertOggettoQuery = "INSERT INTO oggettiaula (IdAula, Oggetto) VALUES (?, ?)";

        try (PreparedStatement aulaStmt = connection.prepareStatement(insertAulaQuery);
             PreparedStatement oggettoStmt = connection.prepareStatement(insertOggettoQuery)) {

            // Inserimento o aggiornamento dell'aula
            aulaStmt.setString(1, entity.getIdAula());
            aulaStmt.setString(2, entity.getEdificio());
            aulaStmt.setInt(3, entity.getPiano());
            aulaStmt.executeUpdate();

            // Eliminare gli oggetti precedenti per l'aula per evitare duplicati
            try (PreparedStatement deleteStmt = connection.prepareStatement("DELETE FROM oggettiaula WHERE IdAula = ?")) {
                deleteStmt.setString(1, entity.getIdAula());
                deleteStmt.executeUpdate();
            }

            // Inserimento degli oggetti associati all'aula
            List<String> oggetti = entity.getOggetti();
            if (oggetti != null) {
                for (String oggetto : oggetti) {
                    oggettoStmt.setString(1, entity.getIdAula());
                    oggettoStmt.setString(2, oggetto);
                    oggettoStmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore nell'inserimento dell'aula: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String id) {
        //da fare
    }

    @Override
    public boolean exists(String id) {
        String query = "SELECT COUNT(*) FROM aule WHERE IdAula = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id);
            try (var rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il controllo dell'esistenza dell'aula: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Aula> loadAll() {
        return List.of();
    }

    @Override
    public void update(Aula entity) {
        //da fare
    }

    @Override
    public List<Aula> getAllAule() {
        List<Aula> aule = new ArrayList<>();
        Map<String, Aula> aulaMap = new HashMap<>();

        String query = """
                SELECT a.IdAula, a.Edificio, a.Piano, o.Oggetto 
                FROM aule a 
                LEFT JOIN oggettiaula o ON a.IdAula = o.IdAula
                """;

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String idAula = rs.getString("IdAula");
                String edificio = rs.getString("Edificio");
                int piano = rs.getInt("Piano");
                String oggetto = rs.getString("Oggetto");

                Aula aula = aulaMap.get(idAula);
                if (aula == null) {
                    aula = new Aula(idAula,piano, edificio, new ArrayList<>());
                    aulaMap.put(idAula, aula);
                }

                // Aggiunge l'oggetto solo se non è null
                if (oggetto != null) {
                    aula.getOggetti().add(oggetto);
                }
            }

            aule.addAll(aulaMap.values());

        } catch (SQLException e) {
            throw new RuntimeException("Errore nel recupero delle aule: " + e.getMessage(), e);
        }

        return aule;
    }



    @Override
    public List<String> getAllEdifici() {
        return List.of();
    }

}
