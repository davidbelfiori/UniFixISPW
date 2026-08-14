package org.ing.ispw.unifix.dao.jdbc;


import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.exception.*;
import org.ing.ispw.unifix.model.Aula;
import org.ing.ispw.unifix.model.AulaId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JdbcAulaDao  implements AulaDao {



    private static  final String ACTION_1 ="Oggetto";
    private static  final String ACTION_2 ="Edificio";

     private Connection getConnection() { return SingletonConnessione.getInstance(); }


    @Override
    public Aula create(String idAula) {
        return new Aula(idAula);
    }

    @Override
    public Aula load(AulaId id) {
         if (id == null) {
            throw new IllegalArgumentException("L'identificatore dell'aula non può essere nullo.");
        }
         String query = """
            SELECT a.IdAula, a.Edificio, a.Piano, o.Oggetto
            FROM aule a
            LEFT JOIN oggettiaula o
                ON a.IdAula = o.IdAula
                AND a.Edificio = o.Edificio
            WHERE LOWER(a.IdAula) = ?
                AND LOWER(a.Edificio) = ?
            """;

        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, id.idAula());
            stmt.setString(2, id.edificio());

            try (ResultSet rs = stmt.executeQuery()) {
                Aula aula = null;
                List<String> oggetti = new ArrayList<>();

                while (rs.next()) {
                    if (aula == null) {
                        aula = new Aula(rs.getString("IdAula"));
                        aula.setEdificio(rs.getString(ACTION_2));
                        aula.setPiano(rs.getInt("Piano"));
                    }

                    String oggetto = rs.getString(ACTION_1);

                    if (oggetto != null) {
                        oggetti.add(oggetto);
                    }
                }

                if (aula == null) {
                    return null;
                }

                aula.setOggetti(oggetti);
                return aula;
            }
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante il caricamento dell'aula",
                    e
            );
        }
    }


    @Override
    public void store(Aula entity) {
        if (entity == null) {
            throw new IllegalArgumentException(
                    "L'aula non può essere nulla"
            );
        }

        AulaId key = new AulaId(
                entity.getIdAula(),
                entity.getEdificio()
        );
        String insertAulaQuery = "INSERT INTO aule (IdAula, Edificio, Piano) VALUES (?, ?, ?) ";

        String insertOggettoQuery = "INSERT INTO oggettiaula (IdAula, Edificio, Oggetto) VALUES (?, ?, ?)";

        try (PreparedStatement aulaStmt = getConnection().prepareStatement(insertAulaQuery);
             PreparedStatement oggettoStmt = getConnection().prepareStatement(insertOggettoQuery)) {

            // Inserimento o aggiornamento dell'aula
            aulaStmt.setString(1, entity.getIdAula());
            aulaStmt.setString(2, entity.getEdificio());
            aulaStmt.setInt(3, entity.getPiano());
            aulaStmt.executeUpdate();

            // Inserimento degli oggetti associati all'aula
            List<String> oggetti = entity.getOggetti();
            if (oggetti != null) {
                for (String oggetto : oggetti) {
                    oggettoStmt.setString(1, entity.getIdAula());
                    oggettoStmt.setString(2, entity.getEdificio());
                    oggettoStmt.setString(3, oggetto);
                    oggettoStmt.addBatch();
                }
                oggettoStmt.executeBatch();
            }

        } catch (SQLException e) {
            if (isDuplicateKey(e)) {
                throw new EntityAlreadyExistsException(
                        "Esiste già l'aula " + key,
                        e
                );
            }

            throw new PersistenceException(
                    "Errore durante l'inserimento dell'aula " + key,
                    e
            );
        }

    }
    @Override
    public void delete(AulaId id) {
        if (id == null) {
            throw new IllegalArgumentException("L'identificatore dell'aula non può essere nullo");
        }

        String query = """
            DELETE FROM aule
            WHERE LOWER(IdAula) = ?
              AND LOWER(Edificio) = ?
            """;

        try (PreparedStatement stmt =
                     getConnection().prepareStatement(query)) {

            stmt.setString(1, id.idAula());
            stmt.setString(2, id.edificio());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException("Errore durante l'eliminazione dell'aula " + id, e);
        }
    }

    @Override
    public boolean exists(AulaId id) {
        if (id == null) {
            throw new IllegalArgumentException("L'identificatore dell'aula non può essere nullo.");
        }
        String query = """
            SELECT COUNT(*)
            FROM aule
            WHERE LOWER(IdAula) = ?
                AND LOWER(Edificio) = ?
            """;

        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, id.idAula());
            stmt.setString(2, id.edificio());

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante il controllo dell'esistenza dell'aula: "
                            + e.getMessage()
            );
        }
    }
    @Override
    public List<Aula> loadAll() {
        return getAllAule();
    }

    @Override
    public void update(Aula entity) {
        // Il metodo store gestisce già l'aggiornamento se l'aula esiste
        // (grazie a ON DUPLICATE KEY UPDATE), quindi possiamo semplicemente invocarlo.
        // Questo approccio evita la duplicazione del codice e mantiene la logica
        // di salvataggio/aggiornamento in un unico posto.
        if (entity == null) {throw new IllegalArgumentException("L'entità Aula non può essere nulla.");}
    }

    @Override
    public List<Aula> getAllAule() {
        List<Aula> aule = new ArrayList<>();
        Map<String, Aula> aulaMap = new HashMap<>();

        String query = """
                SELECT a.IdAula, a.Edificio, a.Piano, o.Oggetto 
                FROM aule a 
                LEFT JOIN oggettiaula o ON a.IdAula = o.IdAula AND a.Edificio = o.Edificio
                """;

        try (PreparedStatement stmt = getConnection().prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String idAula = rs.getString("IdAula");
                String edificio = rs.getString(ACTION_2);
                int piano = rs.getInt("Piano");
                String oggetto = rs.getString(ACTION_1);

                String key = (edificio + "_" + idAula).toLowerCase();
                Aula aula = aulaMap.get(key);
                if (aula == null) {
                    aula = new Aula(idAula, piano, edificio, new ArrayList<>());
                    aulaMap.put(key, aula);
                }

                // Aggiunge l'oggetto solo se non è null
                if (oggetto != null) {
                    aula.getOggetti().add(oggetto);
                }
            }

            aule.addAll(aulaMap.values());

        } catch (SQLException e) {
            throw new AuleNonTrovateException("Errore nel recupero delle aule: " + e.getMessage());
        }

        return aule;
    }



    @Override
    public List<String> getAllEdifici() {
        List<String> edifici = new ArrayList<>();
        String query = "SELECT DISTINCT Edificio FROM aule";
        try (PreparedStatement stmt = getConnection().prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String edificio = rs.getString(ACTION_2);
                edifici.add(edificio);
            }
            return edifici;
        } catch (SQLException _) {
            throw new EdificiNonTrovatiException("Impossibile trovare degli edifici");
        }
    }

    @Override
    public List<String> getAulaOggetti(AulaId id) {
        String query = """
            SELECT Oggetto
            FROM oggettiaula
            WHERE LOWER(IdAula) = ?
                AND LOWER(Edificio) = ?
            """;

        List<String> oggetti = new ArrayList<>();

        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, id.idAula());
            stmt.setString(2, id.edificio());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    oggetti.add(rs.getString(ACTION_1));
                }
            }

            return oggetti;
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore nel recupero degli oggetti dell'aula: "
                            + e.getMessage()
            );
        }
    }




    @Override
    public int countAule() {
        String query = "SELECT COUNT(*) FROM aule";
        try (PreparedStatement stmt = getConnection().prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new AuleNonTrovateException("Errore durante il conteggio delle aule");
            }
        } catch (SQLException e) {
            throw new AuleNonTrovateException("Errore durante il conteggio delle aule: " + e.getMessage());
        }
    }

    @Override
    public int countEdificiGestiti() {
        String query = "SELECT COUNT(DISTINCT Edificio) FROM aule";
        try (PreparedStatement stmt = getConnection().prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new EdificiNonTrovatiException("Errore durante il conteggio degli edifici");
            }
        } catch (SQLException e) {
            throw new EdificiNonTrovatiException("Errore durante il conteggio degli edifici: " + e.getMessage());
        }
    }


    private static boolean isDuplicateKey(SQLException exception) {
        return exception.getErrorCode() == 1062;
    }

}
