package org.ing.ispw.unifix.utils;

import org.ing.ispw.unifix.exception.CsvInvalidException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVParserService {

    private CSVParserService(){}

    public static void validateCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String header = br.readLine();
            if (header == null || !header.equals("Edificio,IdAula,Piano,Oggetti")) {
                return;
            }
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length != 4) {
                    return;
                }

                Integer.parseInt(fields[2].trim());

                if (!fields[3].contains(";") && !fields[3].trim().isEmpty()) {
                    return;
                }
            }
        } catch (IOException | NumberFormatException e) {
            throw new CsvInvalidException("Errore durante la lettura del file CSV: " + e.getMessage());
        }



    }
}
