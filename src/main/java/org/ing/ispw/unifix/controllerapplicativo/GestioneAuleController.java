package org.ing.ispw.unifix.controllerapplicativo;

import org.ing.ispw.unifix.bean.AulaBean;
import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.exception.AulaGiaPresenteException;
import org.ing.ispw.unifix.exception.AuleNonTrovateException;
import org.ing.ispw.unifix.exception.CsvInvalidException;
import org.ing.ispw.unifix.exception.PersistenceException;
import org.ing.ispw.unifix.model.Aula;
import org.ing.ispw.unifix.model.AulaId;
import org.ing.ispw.unifix.utils.CSVParserService;
import org.ing.ispw.unifix.utils.Printer;
import org.ing.ispw.unifix.utils.observer.Subject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class GestioneAuleController extends Subject{

    private final AulaDao aulaDao;



    /*
    * Istanziazione di aula dao , la corretta istaziazione rispetto al metodo di persistenza è fornita dalla Factory
    * */
    public GestioneAuleController(){
        this.aulaDao = DaoFactory.getInstance().getAulaDao();
    }



    /**
     * Inserisce aule da un file CSV. Il file deve avere intestazione e le colonne devono essere:
     * Edificio, IdAula, Piano, Oggetti (separati da punto e virgola).
     * @param filePath percorso del file CSV
     * @return true se almeno un'aula è stata inserita, false altrimenti
     * @throws CsvInvalidException se il file CSV non è valido
     */
    public boolean inserisciAuleFromCsv(String filePath) throws CsvInvalidException {
        boolean auleInserite = false;
        CSVParserService.validateCSV(filePath);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String header = br.readLine(); // Salta l'intestazione CSV
            if (header == null) {
                return false;
            }
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                if (processaRigaCsv(line)) {
                    auleInserite = true;
                }
            }
        } catch (IOException e) {
            Printer.error(e.getMessage());
            return false;
        } catch (NumberFormatException e) {
            throw new CsvInvalidException("Errore nel formato del piano nel file CSV: " + e.getMessage());
        }
        if (auleInserite) {
            notifyObservers();
        }
        return auleInserite;
    }



    /**
     * Processa una riga del file CSV e inserisce l'aula se non esiste.
     * @param line riga del file CSV
     * @return true se l'aula è stata inserita, false altrimenti
     */
    private boolean processaRigaCsv(String line) {
        String[] fields = line.split(",");
        if (fields.length < 4) {
            return false;
        }

        String edificio = fields[0].trim();
        String idAula = fields[1].trim();
        int piano = Integer.parseInt(fields[2].trim());
        List<String> oggetti = estraiOggetti(fields[3]);

        return inserisciAulaSeNonEsiste(edificio, idAula, piano, oggetti);
    }

    private List<String> estraiOggetti(String oggettiRaw) {
        List<String> oggetti = new ArrayList<>();
        for (String obj : oggettiRaw.split(";")) {
            String objPulito = obj.trim();
            if (!objPulito.isEmpty()) {
                oggetti.add(objPulito);
            }
        }
        return oggetti;
    }

    /**
     * Inserisce un'aula se non esiste già.
     * @param edificio edificio dell'aula
     * @param idAula identificatore dell'aula
     * @param piano piano dell'aula
     * @param oggetti lista degli oggetti nell'aula
     * @return true se l'aula è stata inserita, false altrimenti
     */
    private boolean inserisciAulaSeNonEsiste(String edificio, String idAula, int piano, List<String> oggetti) {
        AulaId aulaId = new AulaId(idAula, edificio);
        if (aulaDao.exists(aulaId)) {
            return false;
        }

        Aula aula = aulaDao.create(idAula);
        aula.setEdificio(edificio);
        aula.setPiano(piano);
        aula.setOggetti(oggetti);

        try {
            aulaDao.store(aula);
            return true;
        } catch (AulaGiaPresenteException _) {
            // Il database può rilevare un duplicato anche dopo il controllo exists
            // (ad esempio per regole di confronto o inserimenti concorrenti).
            return false;
        }
    }


    /**
    * Ritorna una lista di aule presenti nel sistema, se non ci sono aule lancia un'eccezione
    * @return List<AulaBean> lista di aule presenti nel sistema
    * @throws AuleNonTrovateException se non ci sono aule nel sistema , la view catturerà l'errore mostrerà a video che non sono presenti aule
    * @throws IllegalArgumentException se l'input non è valido
     * @throws PersistenceException se c'è un errore di persistenza dei dati
    * */
    public List<AulaBean> visualizzaAule()  {
        List<Aula> aule = aulaDao.loadAll();
        List<AulaBean> auleToBean = new ArrayList<>();
        if (aule.isEmpty()) {
            throw new AuleNonTrovateException("Non sono state trovate aule");
        }else {
            for (Aula aula : aule) {
                AulaBean aulaBean =  new AulaBean();
                aulaBean.setIdAula(aula.getIdAula());
                aulaBean.setEdificio(aula.getEdificio());
                aulaBean.setPiano(aula.getPiano());
                aulaBean.setOggetti(aula.getOggetti());
                auleToBean.add(aulaBean);
            }
        }
        //converti le aule in bean per la view (paradigma MVC)
        return auleToBean;
    }



    /**
     * Inserisce un'aula se non esiste già. se l'aula inserita esiste gia , quindi la coppia idAula e edificio , viene lanciata un eccezzione
     * @param aulaBean bean contenente le informazioni dell'aula
     * @throws IllegalArgumentException se l'input non è valido
     * @throws PersistenceException se c'è un errore di persistenza dei dati
     * @throws AulaGiaPresenteException se l'aula è già presente, quindi la coppia idAula e edificio è gia presente nella persistenza
     */
    public void inserisciAula(AulaBean aulaBean){
        // Controllo basato su Edificio + IdAula
        if (!aulaDao.exists(new AulaId(aulaBean.getIdAula(), aulaBean.getEdificio()))) {
            Aula aula = aulaDao.create(aulaBean.getIdAula());
            aula.setEdificio(aulaBean.getEdificio());
            aula.setPiano(aulaBean.getPiano());
            aula.setOggetti(aulaBean.getOggetti());
            aulaDao.store(aula);
            notifyObservers();
        } else {
            throw new AulaGiaPresenteException("L'aula " + aulaBean.getIdAula() + " è già presente nell'edificio " + aulaBean.getEdificio());
        }
    }
}
