package org.ing.ispw.unifix.controllerapplicativo;

import org.ing.ispw.unifix.bean.AulaBean;
import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.exception.AulaGiaPresenteException;
import org.ing.ispw.unifix.exception.AuleNonTrovateException;
import org.ing.ispw.unifix.exception.CsvInvalidException;
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

    public GestioneAuleController(){
        this.aulaDao = DaoFactory.getInstance().getAulaDao();
    }



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

    private boolean inserisciAulaSeNonEsiste(String edificio, String idAula, int piano, List<String> oggetti) {
        if (aulaDao.exists(new AulaId(idAula,edificio))) {
            return false;
        }
        Aula aula = aulaDao.create(idAula);
        aula.setEdificio(edificio);
        aula.setPiano(piano);
        aula.setOggetti(oggetti);
        aulaDao.store(aula);
        return true;
    }


    public List<AulaBean> visualizzaAule() throws AuleNonTrovateException {
        List<Aula> aule = aulaDao.getAllAule();
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



    public void inserisciAula(AulaBean aulaBean) throws AulaGiaPresenteException {
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
