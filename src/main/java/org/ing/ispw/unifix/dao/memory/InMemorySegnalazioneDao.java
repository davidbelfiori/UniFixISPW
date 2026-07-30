package org.ing.ispw.unifix.dao.memory;

import org.ing.ispw.unifix.dao.SegnalazioneDao;

import org.ing.ispw.unifix.model.Segnalazione;
import org.ing.ispw.unifix.utils.StatoSegnalazione;

import java.util.List;

public class InMemorySegnalazioneDao extends InMemoryDao<String, Segnalazione> implements SegnalazioneDao {


   public String getKey(Segnalazione segnalazione){
        return segnalazione.getIdSegnalazione();
   }

   public  Segnalazione create(String idSegnalazione){
        return new Segnalazione(idSegnalazione);
   }

   public List<Segnalazione> getAllSegnalazioni(){
        return loadAll();
   }

   public Segnalazione getSegnalazione(String idSegnalazione){
        return load(idSegnalazione);
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


}
