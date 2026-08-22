package org.ing.ispw.unifix.dao.memory;

import org.ing.ispw.unifix.dao.SegnalazioneDao;

import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.utils.StatoSegnalazione;

import java.util.List;

/**
 * DAO in memoria delle segnalazioni, indicizzate per identificatore.
 * Le ricerche per utente e i conteggi vengono effettuati filtrando le entità memorizzate.
 */
public class InMemorySegnalazioneDao extends InMemoryDao<String, Segnalazione> implements SegnalazioneDao {


   public String getKey(Segnalazione segnalazione){
        return segnalazione.getIdSegnalazione();
   }

   public  Segnalazione create(String idSegnalazione){
        return new Segnalazione(idSegnalazione);
   }

   public List<Segnalazione> getSegnalazioniByDocente(String docenteEmail) {
        List<Segnalazione> result = new java.util.ArrayList<>();
        for (Segnalazione s : loadAll()) {
            if (s.getDocente() != null && s.getDocente().getEmail().equals(docenteEmail)) {
                result.add(s);
            }
        }
        return result;
   }
    public List<Segnalazione> getSegnalazioniByTecnico(String tecnicoMail) {
        List<Segnalazione> result = new java.util.ArrayList<>();
        for (Segnalazione s : loadAll()) {
            if (s.getTecnico() != null && s.getTecnico().getEmail().equals(tecnicoMail)) {
                result.add(s);
            }
        }
        return result;
    }

    @Override
    public int countSegnalazioniAttive() {
        List<Segnalazione> allSegnalazioni = loadAll();
        int count = 0;
        for (Segnalazione s : allSegnalazioni) {
            if (s.getStato() == StatoSegnalazione.APERTA || s.getStato() == StatoSegnalazione.IN_LAVORAZIONE) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int countSegnalazioniRisolte() {
        List<Segnalazione> allSegnalazioni = loadAll();
        int count = 0;
        for (Segnalazione s : allSegnalazioni) {
            if (s.getStato() == StatoSegnalazione.CHIUSA) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean exists(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'identificatore non può essere nullo.");}
            Segnalazione segnalazione;
            segnalazione = load(id);
            if (segnalazione.getStato() == StatoSegnalazione.CHIUSA) {
                return false;
            }
            return super.exists(id);

    }
}
