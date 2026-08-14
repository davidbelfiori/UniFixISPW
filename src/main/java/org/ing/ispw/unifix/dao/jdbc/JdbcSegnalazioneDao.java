package org.ing.ispw.unifix.dao.jdbc;


import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.exception.NessunaSegnalazioneException;
import org.ing.ispw.unifix.exception.PersistenceException;
import org.ing.ispw.unifix.exception.SegnalazioneGiaEsistenteException;
import org.ing.ispw.unifix.exception.UpdateSegnalazioneException;
import org.ing.ispw.unifix.model.Docente;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.model.Tecnico;
import org.ing.ispw.unifix.utils.StatoSegnalazione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcSegnalazioneDao  implements SegnalazioneDao {


    private static final String IDSEGNALAZIONE = "IdSegnalazione";
    private static final String DATA_CREAZIONE = "dataCreazione";
    private static final String OGGETTO_GUASTO = "oggettoGuasto";
    private static final String DOCENTEMAIL  = "email_docente";
    private static final String STATO = "stato";
    private static final String DESCRIZIONE = "descrizione";
    private static final String AULA = "aula";
    private static final String EDIFICIO = "edificio";
    private static final String TECNICOMAIL = "email_tecnico";
    private static final String TECNICONOME = "nome_tecnico";
    private static final String TECNINCOCOGNOME = "cognome_tecnico";
    private static final String DOCENTENOME = "nome_docente";
    private static final String DOCENTECOGNOME = "cognome_docente";



    private Connection getConnection() { return SingletonConnessione.getInstance(); }


    @Override
    public Segnalazione create(String idSegnalazione) {
        return new Segnalazione(idSegnalazione);
    }

    @Override
    public Segnalazione load(String id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "L'ID della segnalazione non può essere nullo"
            );
        }

        String query = """
            SELECT
                s.*,
                d.nome AS nome_docente,
                d.cognome AS cognome_docente,
                d.email AS email_docente,
                t.nome AS nome_tecnico,
                t.cognome AS cognome_tecnico,
                t.email AS email_tecnico
            FROM segnalazione s
            JOIN user d ON d.email = s.docente
            LEFT JOIN user t ON t.email = s.tecnico
            WHERE s.idSegnalazione = ?
            """;

        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {return null;}

                Segnalazione segnalazione = new Segnalazione(rs.getString(IDSEGNALAZIONE));

                segnalazione.setDataCreazione(rs.getDate(DATA_CREAZIONE));

                segnalazione.setOggettoGuasto(rs.getString(OGGETTO_GUASTO));
                segnalazione.setDocente(new Docente(rs.getString(DOCENTEMAIL), rs.getString(DOCENTENOME), rs.getString(DOCENTECOGNOME)));
                segnalazione.setStato(StatoSegnalazione.fromString(rs.getString(STATO)));
                segnalazione.setDescrizione(rs.getString(DESCRIZIONE));
                segnalazione.setAula(rs.getString(AULA));
                segnalazione.setEdificio(rs.getString(EDIFICIO));
                String tecnicoEmail = rs.getString(TECNICOMAIL);

                if (tecnicoEmail != null) {
                    segnalazione.setTecnico(new Tecnico(tecnicoEmail, rs.getString(TECNICONOME), rs.getString(TECNINCOCOGNOME)));
                }

                return segnalazione;
            }

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante il caricamento della segnalazione " + id,
                    e
            );
        } catch (IllegalArgumentException e) {
            throw new PersistenceException(
                    "I dati persistiti della segnalazione "
                            + id + " non sono validi",
                    e
            );
        }
    }

    @Override
    public void store(Segnalazione entity) {
        if (entity == null) {
            throw new IllegalArgumentException(
                    "La segnalazione non può essere nulla"
            );
        }

        String id = entity.getIdSegnalazione();

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException(
                    "L'ID della segnalazione non può essere nullo o vuoto"
            );
        }

        String query = """
            INSERT INTO segnalazione (
                IdSegnalazione,
                dataCreazione,
                oggettoGuasto,
                docente,
                stato,
                descrizione,
                aula,
                edificio,
                tecnico
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.setDate(2, entity.getDataCreazione());
            stmt.setString(3, entity.getOggettoGuasto());
            stmt.setString(4, entity.getDocente().getEmail());
            stmt.setString(5, entity.getStato().toString());
            stmt.setString(6, entity.getDescrizione());
            stmt.setString(7, entity.getAula());
            stmt.setString(8, entity.getEdificio());

            if (entity.getTecnico() != null) {
                stmt.setString(9, entity.getTecnico().getEmail());
            } else {
                stmt.setNull(9, java.sql.Types.VARCHAR);
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            if (isDuplicateKey(e)) {
                throw new SegnalazioneGiaEsistenteException(
                        "Esiste già una segnalazione con ID " + id,
                        e
                );
            }

            throw new PersistenceException(
                    "Errore durante l'inserimento della segnalazione " + id,
                    e
            );
        }
    }

    @Override
    public void delete(String id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "L'ID della segnalazione non può essere nullo"
            );
        }

        String query =
                "DELETE FROM segnalazione WHERE IdSegnalazione = ?";

        try (PreparedStatement stmt =
                     getConnection().prepareStatement(query)) {

            stmt.setString(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante l'eliminazione della segnalazione " + id,
                    e
            );
        }
    }

    @Override
    public boolean exists(String id) {
        String query = "SELECT COUNT(*) FROM segnalazione WHERE IdSegnalazione = ? and stato <> 'CHIUSA' ";

        try (PreparedStatement stmt = getConnection() .prepareStatement(query)){
            stmt.setString(1,id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException _) {
            throw new SegnalazioneGiaEsistenteException("La segnalazione Esiste Gia");
        }
    }

    @Override
    public List<Segnalazione> loadAll() {
        List<Segnalazione> segnalazioni = new ArrayList<>();
        String query = """

                SELECT
                    s.*,
                    d.nome AS nome_docente,
                    d.cognome AS cognome_docente,
                    d.email AS email_docente,
                    t.nome AS nome_tecnico,
                    t.cognome AS cognome_tecnico,
                    t.email AS email_tecnico
                FROM
                    segnalazione s
                        JOIN
                    user d ON d.email= s.docente
                        LEFT JOIN
                    user t ON t.email = s.tecnico;
""";
        try (PreparedStatement stmt = getConnection() .prepareStatement(query)){
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Segnalazione segnalazione = new Segnalazione(rs.getString(IDSEGNALAZIONE));
                segnalazione.setDataCreazione(rs.getDate(DATA_CREAZIONE));
                segnalazione.setOggettoGuasto(rs.getString(OGGETTO_GUASTO));
                segnalazione.setDocente(new Docente(rs.getString(DOCENTEMAIL),rs.getString(DOCENTENOME),rs.getString(DOCENTECOGNOME)));
                segnalazione.setStato(StatoSegnalazione.fromString(rs.getString(STATO)));
                segnalazione.setDescrizione(rs.getString(DESCRIZIONE));
                segnalazione.setAula(rs.getString(AULA));
                segnalazione.setEdificio(rs.getString(EDIFICIO));
                segnalazione.setTecnico(new Tecnico(rs.getString(TECNICOMAIL),rs.getString(TECNICONOME),rs.getString(TECNINCOCOGNOME)));
                segnalazioni.add(segnalazione);
            }
        }catch (SQLException _){
            throw new NessunaSegnalazioneException("Nessuna segnalazione trovata");
        }
        return segnalazioni;
    }

    @Override
    public void update(Segnalazione entity) {
        String query = "UPDATE segnalazione SET dataCreazione = ?, oggettoGuasto = ?, docente = ?, stato = ?, descrizione = ?, aula = ?, edificio = ?, tecnico = ? WHERE IdSegnalazione = ?";
        try (PreparedStatement stmt = getConnection() .prepareStatement(query)) {
            stmt.setDate(1, entity.getDataCreazione());
            stmt.setString(2, entity.getOggettoGuasto());
            stmt.setString(3, entity.getDocente().getEmail());
            stmt.setString(4, entity.getStato().toString());
            stmt.setString(5, entity.getDescrizione());
            stmt.setString(6, entity.getAula());
            stmt.setString(7, entity.getEdificio());
            // Handle the case where tecnico might be null (e.g., if it's an optional field)
            if (entity.getTecnico() != null) {
                stmt.setString(8, entity.getTecnico().getEmail());
            } else {
                stmt.setNull(8, java.sql.Types.VARCHAR);
            }
            stmt.setString(9, entity.getIdSegnalazione());
            stmt.executeUpdate();
        } catch (SQLException _) {
            throw new UpdateSegnalazioneException("Errore durante l'aggiornamento della segnalazione");
        }
    }




    @Override
    public List<Segnalazione> getSegnalazioniByDocente(String docenteEmail) {
        List<Segnalazione> segnalazioni = new ArrayList<>();
        String query = """

                SELECT
                    s.*,
                    d.nome AS nome_docente,
                    d.cognome AS cognome_docente,
                    d.email AS email_docente,
                    t.nome AS nome_tecnico,
                    t.cognome AS cognome_tecnico,
                    t.email AS email_tecnico
                FROM
                    segnalazione s
                        JOIN
                    user d ON d.email= s.docente
                        LEFT JOIN
                    user t ON t.email = s.tecnico
                WHERE s.docente = ?;
""";
        try (PreparedStatement stmt = getConnection() .prepareStatement(query)){
            stmt.setString(1, docenteEmail);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Segnalazione segnalazione = new Segnalazione(rs.getString(IDSEGNALAZIONE));
                segnalazione.setDataCreazione(rs.getDate(DATA_CREAZIONE));
                segnalazione.setOggettoGuasto(rs.getString(OGGETTO_GUASTO));
                segnalazione.setDocente(new Docente(rs.getString(DOCENTEMAIL),rs.getString(DOCENTENOME),rs.getString(DOCENTECOGNOME)));
                segnalazione.setStato(StatoSegnalazione.valueOf(rs.getString(STATO)));
                segnalazione.setDescrizione(rs.getString(DESCRIZIONE));
                segnalazione.setAula(rs.getString(AULA));
                segnalazione.setEdificio(rs.getString(EDIFICIO));
                segnalazione.setTecnico(new Tecnico(rs.getString(TECNICOMAIL),rs.getString(TECNICONOME),rs.getString(TECNINCOCOGNOME)));
                segnalazioni.add(segnalazione);
            }
        }catch (SQLException _){
            throw new NessunaSegnalazioneException("Nessuna segnalazione trovata");
        }
        return segnalazioni;
    }

    @Override
    public List<Segnalazione> getSegnalazioniByTecnico(String tecnicoEmail) {
        List<Segnalazione> segnalazioni = new ArrayList<>();
        String query = """

                SELECT
                    s.*,
                    d.nome AS nome_docente,
                    d.cognome AS cognome_docente,
                    d.email AS email_docente,
                    t.nome AS nome_tecnico,
                    t.cognome AS cognome_tecnico,
                    t.email AS email_tecnico
                FROM
                    segnalazione s
                        JOIN
                    user d ON d.email= s.docente
                        LEFT JOIN
                    user t ON t.email = s.tecnico
                WHERE s.tecnico = ?;
""";
        try (PreparedStatement stmt = getConnection() .prepareStatement(query)){
            stmt.setString(1, tecnicoEmail);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Segnalazione segnalazione = new Segnalazione(rs.getString(IDSEGNALAZIONE));
                segnalazione.setDataCreazione(rs.getDate(DATA_CREAZIONE));
                segnalazione.setOggettoGuasto(rs.getString(OGGETTO_GUASTO));
                segnalazione.setDocente(new Docente(rs.getString(DOCENTEMAIL),rs.getString(DOCENTENOME),rs.getString(DOCENTECOGNOME)));
                segnalazione.setStato(StatoSegnalazione.fromString(rs.getString(STATO)));
                segnalazione.setDescrizione(rs.getString(DESCRIZIONE));
                segnalazione.setAula(rs.getString(AULA));
                segnalazione.setEdificio(rs.getString(EDIFICIO));
                segnalazione.setTecnico(new Tecnico(rs.getString(TECNICOMAIL),rs.getString(TECNICONOME),rs.getString(TECNINCOCOGNOME)));
                segnalazioni.add(segnalazione);
            }
        }catch (SQLException _){
            throw new NessunaSegnalazioneException("Nessuna segnalazione trovata");
        }
        return segnalazioni;
    }

    @Override
    public int countSegnalazioniAttive() {
        String query = "SELECT count(*) as numero from segnalazione where stato = 'APERTA' or stato = 'IN_LAVORAZIONE' ";
        try(PreparedStatement stmt = getConnection() .prepareStatement(query)){
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt("numero");
        }catch (SQLException _){
            throw new NessunaSegnalazioneException("Nessuna segnalazione trovata");
        }
    }

    @Override
    public int countSegnalazioniRisolte() {
        String query = "SELECT count(*) as numero from segnalazione where stato = 'CHIUSA' ";
        try(PreparedStatement stmt = getConnection() .prepareStatement(query)){
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt("numero");
        }catch (SQLException _){
            throw new NessunaSegnalazioneException("Nessuna segnalazione trovata");
        }
    }
    private static boolean isDuplicateKey(SQLException exception) {
        return exception.getErrorCode() == 1062;
    }
    }
