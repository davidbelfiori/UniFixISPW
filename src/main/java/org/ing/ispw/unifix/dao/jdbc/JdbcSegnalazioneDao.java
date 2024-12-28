package org.ing.ispw.unifix.dao.jdbc;


import org.ing.ispw.unifix.dao.SegnalazioneDao;
import org.ing.ispw.unifix.model.Segnalazione;

import java.util.List;

public class JdbcSegnalazioneDao extends PersitenceDao<String , Segnalazione>  implements SegnalazioneDao {

    //rendila singelton e aggiungi il metodo getInstance
    private static JdbcSegnalazioneDao instance;

    public static JdbcSegnalazioneDao getInstance(){
        if(instance == null){
            instance = new JdbcSegnalazioneDao();
        }
        return instance;
    }

    @Override
    public List<Segnalazione> getAllSegnalazioni() {
        return List.of();
    }

    @Override
    public Segnalazione getSegnalazione(String idSegnalazione) {
        return null;
    }
}
