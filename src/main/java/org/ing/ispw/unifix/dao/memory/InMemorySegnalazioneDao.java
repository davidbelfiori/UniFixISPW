package org.ing.ispw.unifix.dao.memory;

import org.ing.ispw.unifix.dao.SegnalazioneDao;

import org.ing.ispw.unifix.model.Segnalazione;

import java.util.List;

public class InMemorySegnalazioneDao extends InMemoryDao<String, Segnalazione> implements SegnalazioneDao {


   public String getKey(Segnalazione segnalazione){
        return segnalazione.getIdSegnalzione();
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

}
