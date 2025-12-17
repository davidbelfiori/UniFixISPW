package org.ing.ispw.unifix.dao.jdbc;


import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.exception.DbConnException;
import org.ing.ispw.unifix.exception.NessunaSegnalazioneException;
import org.ing.ispw.unifix.exception.SegnalazioneGiaEsistenteException;
import org.ing.ispw.unifix.exception.UpdateSegnalazioneException;
import org.ing.ispw.unifix.model.Docente;
import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.model.Tecnico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcSegnalazioneDao extends PersitenceDao<String , Segnalazione>  implements SegnalazioneDao {

    //rendila singelton e aggiungi il metodo getInstance
    private static JdbcSegnalazioneDao instance;
    private Connection connection;

    public static JdbcSegnalazioneDao getInstance(){
        if(instance == null){
            instance = new JdbcSegnalazioneDao();
        }
        return instance;
    }

    public JdbcSegnalazioneDao(){
        try {
            this.connection =  SingletonConnessione.getInstance();
        } catch (SQLException _) {
            throw new DbConnException("Impossibile connettersi al database");
        }
    }

    @Override
    public Segnalazione create(String idSegnalazione) {
        return new Segnalazione(idSegnalazione);
    }

    @Override
    public void store(Segnalazione entity) {

        String query = "INSERT INTO segnalazione (IdSegnalazione, dataCreazione,oggettoGuasto,docente,stato,descrizione,aula,edificio,tecnico) values (?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setString(1,entity.getIdSegnalzione());
            stmt.setLong(2,entity.getDataCreazione());
            stmt.setString(3,entity.getOggettoGuasto());
            stmt.setString(4,entity.getDocente().getEmail());
            stmt.setString(5,entity.getStato());
            stmt.setString(6,entity.getDescrizione());
            stmt.setString(7,entity.getAula());
            stmt.setString(8,entity.getEdificio());
            stmt.setString(9,entity.getTecnico().getEmail());
            stmt.executeUpdate();
        }
        catch (SQLException _) {
            throw new SegnalazioneGiaEsistenteException("Errore durante l'inserimento della segnalazione");
        }
    }

    @Override
    public boolean exists(String id) {
        String query = "SELECT COUNT(*) FROM segnalazione WHERE IdSegnalazione = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setString(1,id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException _) {
            throw new SegnalazioneGiaEsistenteException("La segnalazione Esiste Gia");
        }
    }

    @Override
    public void update(Segnalazione entity) {
        String query = "UPDATE segnalazione SET dataCreazione = ?, oggettoGuasto = ?, docente = ?, stato = ?, descrizione = ?, aula = ?, edificio = ?, tecnico = ? WHERE IdSegnalazione = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, entity.getDataCreazione());
            stmt.setString(2, entity.getOggettoGuasto());
            stmt.setString(3, entity.getDocente().getEmail());
            stmt.setString(4, entity.getStato());
            stmt.setString(5, entity.getDescrizione());
            stmt.setString(6, entity.getAula());
            stmt.setString(7, entity.getEdificio());
            // Handle the case where tecnico might be null (e.g., if it's an optional field)
            if (entity.getTecnico() != null) {
                stmt.setString(8, entity.getTecnico().getEmail());
            } else {
                stmt.setNull(8, java.sql.Types.VARCHAR);
            }
            stmt.setString(9, entity.getIdSegnalzione());
            stmt.executeUpdate();
        } catch (SQLException _) {
            throw new UpdateSegnalazioneException("Errore durante l'aggiornamento della segnalazione");
        }
    }


    @Override
    public List<Segnalazione> getAllSegnalazioni() {
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
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Segnalazione segnalazione = new Segnalazione(rs.getString("IdSegnalazione"));
                segnalazione.setDataCreazione(rs.getLong("dataCreazione"));
                segnalazione.setOggettoGuasto(rs.getString("oggettoGuasto"));
                segnalazione.setDocente(new Docente(rs.getString("email_docente"),rs.getString("nome_docente"),rs.getString("cognome_docente")));
                segnalazione.setStato(rs.getString("stato"));
                segnalazione.setDescrizione(rs.getString("descrizione"));
                segnalazione.setAula(rs.getString("aula"));
                segnalazione.setEdificio(rs.getString("edificio"));
                segnalazione.setTecnico(new Tecnico(rs.getString("email_tecnico"),rs.getString("nome_tecnico"),rs.getString("cognome_tecnico")));
                segnalazioni.add(segnalazione);
            }
        }catch (SQLException _){
            throw new NessunaSegnalazioneException("Nessuna segnalazione trovata");
        }
        return segnalazioni;
    }

    @Override
    public Segnalazione getSegnalazione(String idSegnalazione) {
        Segnalazione segnalazione = null;
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
                where idSegnalazione = ?;
""";
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setString(1,idSegnalazione);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) { // Check if there's a result before trying to read it
                segnalazione = new Segnalazione(rs.getString("IdSegnalazione"));
                segnalazione.setDataCreazione(rs.getLong("dataCreazione"));
                segnalazione.setOggettoGuasto(rs.getString("oggettoGuasto"));
                segnalazione.setDocente(new Docente(rs.getString("email_docente"),rs.getString("nome_docente"),rs.getString("cognome_docente")));
                segnalazione.setStato(rs.getString("stato"));
                segnalazione.setDescrizione(rs.getString("descrizione"));
                segnalazione.setAula(rs.getString("aula"));
                segnalazione.setEdificio(rs.getString("edificio"));
                segnalazione.setTecnico(new Tecnico(rs.getString("email_tecnico"),rs.getString("nome_tecnico"),rs.getString("cognome_tecnico")));
            }else {
                throw new NessunaSegnalazioneException("Segnalazione non trovata");
            }

        }catch (SQLException _){
            throw new NessunaSegnalazioneException("Nessuna segnalazione trovata");
        }
        return segnalazione;
    }

}
