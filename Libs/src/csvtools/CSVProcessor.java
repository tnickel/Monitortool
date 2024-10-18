package csvtools;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CSVProcessor {
    private List<String[]> data;
    private String[] header;

    public CSVProcessor(String filePath) throws IOException, CsvValidationException {
        // Der Code liest eine Attributsmatrix von einer CSV-Datei ein und speichert sie.
        this.data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            this.header = reader.readNext(); // Lese die Kopfzeile der Datei ein
            String[] line;
            while ((line = reader.readNext()) != null) {
                this.data.add(line);
            }
        }
    }

    public void filterAttributes(List<String> attributesToKeep) {
        Set<String> attributesSet = new HashSet<>(attributesToKeep);
        List<Integer> indicesToKeep = getIndicesToKeep(attributesSet);
        String[] newHeader = createNewHeader(indicesToKeep);
        List<String[]> newData = createFilteredData(indicesToKeep, newHeader);
        this.data = newData;
    }

    private List<Integer> getIndicesToKeep(Set<String> attributesSet) {
        List<Integer> indicesToKeep = new ArrayList<>();
        for (int i = 0; i < header.length; i++) {
            if (header[i].equals("Strategy_Name") || header[i].equals("Weka Endtest") || attributesSet.contains(header[i])) {
                indicesToKeep.add(i);
            }
        }
        return indicesToKeep;
    }

    private String[] createNewHeader(List<Integer> indicesToKeep) {
        String[] newHeader = new String[indicesToKeep.size()];
        for (int i = 0; i < indicesToKeep.size(); i++) {
            newHeader[i] = header[indicesToKeep.get(i)];
        }
        return newHeader;
    }

    private List<String[]> createFilteredData(List<Integer> indicesToKeep, String[] newHeader) {
        List<String[]> newData = new ArrayList<>();
        newData.add(newHeader); // Füge die neue Kopfzeile als erste Zeile hinzu

        for (String[] row : this.data) {
            String[] newRow = new String[indicesToKeep.size()];
            for (int i = 0; i < indicesToKeep.size(); i++) {
                newRow[i] = row[indicesToKeep.get(i)];
            }
            newData.add(newRow);
        }
        return newData;
    }

    public void saveToFile(String filePath) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
            writer.writeAll(this.data);
        }
    }

    public static void main(String[] args) {
        try {
            String inputFilePath = "C:\\forex\\Metrikanalyser\\AR\\Q107 EURUSD M5 v1.41 Weka10\\Q107 EURUSD M5 v1.41 weka10_+00000\\exported_for_weka.csv";
            String outputFilePath = "C:\\forex\\Metrikanalyser\\AR\\Q107 EURUSD M5 v1.41 Weka10\\Q107 EURUSD M5 v1.41 weka10_+00000\\exported_for_weka_short.csv";

            CSVProcessor processor = new CSVProcessor(inputFilePath);
            List<String> attributesToKeep = Arrays.asList("Strategy_Name", "Fitness_(IS)1", "Net_profit_(IS)1");
            processor.filterAttributes(attributesToKeep);
            processor.saveToFile(outputFilePath);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }
}