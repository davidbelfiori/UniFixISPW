package org.ing.ispw.unifix.controllerapplicativo;

import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.dao.SegnalazioneDao;

public class DashboardKpiController {

    // Dichiarazione delle dipendenze come attributi
    private final SegnalazioneDao segnalazioneDao;
    private final AulaDao aulaDao;

    // Inizializzazione unica nel costruttore
    public DashboardKpiController() {
        DaoFactory factory = DaoFactory.getInstance();
        this.segnalazioneDao = factory.getSegnalazioneDao();
        this.aulaDao = factory.getAulaDao();
    }

    /**
     * Per visualizzare il numero di segnalazioni attive
     * @return  String con il numero delle segnalazioni attive
     */
    public String visualizzaSegnalazioniAttiveAdmin() {
        int numero = segnalazioneDao.countSegnalazioniAttive();
        return String.valueOf(numero);
    }

    /**
     * Per visualizzare il numero di segnalazioni risolte
     * @return  String con il numero delle segnalazioni risolte
     */
    public String  visualizzaSegnalazioniRisolteAdmin()  {
       int numero = segnalazioneDao.countSegnalazioniRisolte();
       return String.valueOf(numero);
    }

    /**
     * Per visualizzare il numero di edifici gestiti dall'applicativo
     * @return  String con il numero degli edifici gestiti
     * */
    public String visualizzaEdificiGestiti (){
        return String.valueOf(aulaDao.countEdificiGestiti());
    }

    /**
     * Per visualizzare il numero di aule presenti nel sistema
     * @return String con il numero delle aule
     * */
    public String visualizzaNumeroaule(){
        return String.valueOf(aulaDao.countAule());
    }
}
