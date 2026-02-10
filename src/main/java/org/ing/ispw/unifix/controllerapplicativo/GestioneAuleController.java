package org.ing.ispw.unifix.controllerapplicativo;

import org.ing.ispw.unifix.bean.AulaBean;
import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.exception.AulaGiaPresenteException;
import org.ing.ispw.unifix.exception.AuleNonTrovateException;
import org.ing.ispw.unifix.exception.CsvInvalidException;
import org.ing.ispw.unifix.exception.DatiAulaNonValidiException;
import org.ing.ispw.unifix.model.Aula;
import org.ing.ispw.unifix.utils.CSVParserService;
import org.ing.ispw.unifix.utils.Printer;
import org.ing.ispw.unifix.utils.observer.Observer;
import org.ing.ispw.unifix.utils.observer.Subject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class GestioneAuleController {


    private final Subject subject = new Subject();

    public void attach(Observer observer) {
        subject.attach(observer);
    }

    public void detach(Observer observer) {
        subject.detach(observer);
    }



    public boolean inserisciAuleFromCsv(String filePath) throws CsvInvalidException {

            boolean auleInserite = false;
           CSVParserService.validateCSV(filePath);

            AulaDao aulaDao = DaoFactory.getInstance().getAulaDao();
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                boolean firstLine = true;

                while ((line = br.readLine()) != null) {
                    // Salta la prima riga (header)
                    if (firstLine) {
                        firstLine = false;
                        continue;
                    }
                    String[] fields = line.split(",");

                    // Parsing dei campi
                    String edificio = fields[0].trim();
                    String idAula = fields[1].trim();
                    int piano = Integer.parseInt(fields[2].trim());
                    List<String> oggetti = Arrays.asList(fields[3].trim().split(";"));

                    //controlla se l'aula esiste già
                    if (aulaDao.exists(idAula)) {
                        throw new AulaGiaPresenteException("L'aula con ID " + idAula + " è già presente nel sistema.");
                    }else {
                        Aula aula = aulaDao.create(idAula);
                        aula.setEdificio(edificio);
                        aula.setPiano(piano);
                        aula.setOggetti(oggetti);
                        aulaDao.store(aula);
                        auleInserite = true;
                    }

                }
            } catch (IOException e) {
                Printer.error(e.getMessage());
                return false; // Ritorna false in caso di errore di I/O
            }
            catch (NumberFormatException e) {
                throw  new CsvInvalidException(e.getMessage());
            }

            if(auleInserite){
                subject.notifyObservers();
            }
            return auleInserite;
        }


    public List<AulaBean> visualizzaAule() throws AuleNonTrovateException, DatiAulaNonValidiException {
        AulaDao aulaDao = DaoFactory.getInstance().getAulaDao();
        List<Aula> aule = aulaDao.getAllAule();
        List<AulaBean> auleToBean = new ArrayList<>();
        if (aule.isEmpty()) {
            throw new AuleNonTrovateException("Non sono state trovate aule");
        }else {
            for (Aula aula : aule) {
                AulaBean aulaBean =  new AulaBean(
                        aula.getIdAula(),
                        aula.getEdificio(),
                        aula.getPiano(),
                        aula.getOggetti()
                );
                auleToBean.add(aulaBean);
            }
        }


        //converti le aule in bean per la view (paradigma MVC)
        return auleToBean;
    }



    public void inserisciAula(AulaBean aulaBean) throws AulaGiaPresenteException {
        AulaDao aulaDao = DaoFactory.getInstance().getAulaDao();
        if (!aulaDao.exists(aulaBean.getIdAula())) {
            Aula aula = aulaDao.create(aulaBean.getIdAula());
            aula.setEdificio(aulaBean.getEdificio());
            aula.setPiano(aulaBean.getPiano());
            aula.setOggetti(aulaBean.getOggetti());
            aulaDao.store(aula);
            subject.notifyObservers();
        } else {
            throw new AulaGiaPresenteException("Aula già presente");
        }
    }
}
