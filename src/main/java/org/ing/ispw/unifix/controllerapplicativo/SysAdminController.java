package org.ing.ispw.unifix.controllerapplicativo;

import org.ing.ispw.unifix.dao.AulaDao;
import org.ing.ispw.unifix.dao.DaoFactory;
import org.ing.ispw.unifix.exception.AuleNonTrovateException;
import org.ing.ispw.unifix.model.Aula;
import org.ing.ispw.unifix.utils.Printer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SysAdminController {

    private static SysAdminController instance;

    public static SysAdminController getInstance() {
        if(instance == null) {
            instance = new SysAdminController();
        }
        return instance;
    }

    private SysAdminController() {
    }

    public void inserisciAule(String filePath){

        AulaDao aulaDao= DaoFactory.getInstance().getAulaDao();
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

                Aula aula =aulaDao.create(idAula);
                aula.setEdificio(edificio);
                aula.setPiano(piano);
                aula.setOggetti(oggetti);
                aulaDao.store(aula);

            }
        }catch (IOException e){
           Printer.error(e.getMessage());
        }
    }

    public void visualizzaAule() throws AuleNonTrovateException{
        AulaDao aulaDao = DaoFactory.getInstance().getAulaDao();
        List<Aula> aule = aulaDao.getAllAule();
        if (!aule.isEmpty()) {
            for (Aula aula : aule) {
                Printer.print("Edificio: " + aula.getEdificio());
                Printer.print("ID Aula: " + aula.getIdAula());
                Printer.print("Piano: " + aula.getPiano());
                Printer.print("Oggetti: " + String.join(", ", aula.getOggetti()));
                Printer.print("-------------------------");
            }
        }else {
            throw new AuleNonTrovateException("Non sono state trovate aule");
        }
    }
    }

