package csvtools;

/**
 * CsvMerger ist eine Hilfsklasse, die zwei CSV-Dateien zu einer zusammenführt und sicherstellt, dass die Strategienamen eindeutig sind.
 * 
 * Die Klasse liest zwei Eingabe-CSV-Dateien, kombiniert deren Daten und schreibt das Ergebnis in eine Ausgabedatei.
 * Wenn ein Strategiename in beiden Eingabedateien vorkommt, wird der Name aus der zweiten Datei durch Anhängen eines Buchstabens
 * geändert, um die Eindeutigkeit sicherzustellen. Die zusammengeführte Datei behält die Kopfzeile der ersten Eingabedatei bei.
 */
/**
 * CsvMerger ist eine Hilfsklasse, die zwei CSV-Dateien zu einer zusammenführt und sicherstellt, dass die Strategienamen eindeutig sind.
 * 
 * Die Klasse liest zwei Eingabe-CSV-Dateien, kombiniert deren Daten und schreibt das Ergebnis in die zweite Eingabedatei.
 * Wenn ein Strategiename in beiden Eingabedateien vorkommt, wird der Name aus der ersten Datei durch Anhängen eines Buchstabens
 * geändert, um die Eindeutigkeit sicherzustellen.
 */
/**
 * CsvMerger ist eine Hilfsklasse, die zwei CSV-Dateien zu einer zusammenführt und sicherstellt, dass die Strategienamen eindeutig sind.
 * 
 * Die Klasse liest zwei Eingabe-CSV-Dateien, kombiniert deren Daten und schreibt das Ergebnis in die zweite Eingabedatei.
 * Wenn ein Strategiename in beiden Eingabedateien vorkommt, wird der Name aus der ersten Datei durch Anhängen eines Buchstabens
 * geändert, um die Eindeutigkeit sicherzustellen. Falls die zweite Datei nicht existiert, wird sie neu angelegt.
 */
/**
 * CsvMerger ist eine Hilfsklasse, die zwei CSV-Dateien zu einer zusammenführt und sicherstellt, dass die Strategienamen eindeutig sind.
 * 
 * Die Klasse liest zwei Eingabe-CSV-Dateien, kombiniert deren Daten und schreibt das Ergebnis in die zweite Eingabedatei.
 * Wenn ein Strategiename in beiden Eingabedateien vorkommt, wird der Name aus der ersten Datei durch Anhängen eines Buchstabens
 * geändert, um die Eindeutigkeit sicherzustellen. Falls die zweite Datei nicht existiert, wird sie neu angelegt.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CsvMerger {

    public static void mergeCsvFiles(String file1, String file2) throws IOException {
        // HashSet to keep track of all strategy names
        Set<String> strategyNames = new HashSet<>();
        // List to store merged CSV lines
        List<String[]> mergedData = new ArrayList<>();

        // Read the first CSV file and collect all data
        List<String[]> dataFile1 = readCsvFile(file1);

        // Read the second CSV file if it exists, otherwise initialize with an empty list
        List<String[]> dataFile2 = new ArrayList<>();
        File secondFile = new File(file2);
        if (secondFile.exists()) {
            dataFile2 = readCsvFile(file2);
        }

        // Add header from the second file if it exists, otherwise from the first file
        if (!dataFile2.isEmpty()) {
            mergedData.add(dataFile2.get(0));
        } else if (!dataFile1.isEmpty()) {
            mergedData.add(dataFile1.get(0));
        }

        // Add all strategy names from the second file to the set (excluding header)
        for (int i = 1; i < dataFile2.size(); i++) {
            String[] attributes = dataFile2.get(i);
            strategyNames.add(attributes[0]);
        }

        // Process data from the first file (excluding header)
        for (int i = 1; i < dataFile1.size(); i++) {
            String[] attributes = dataFile1.get(i);
            String strategyName = attributes[0];

            // Ensure strategy name is unique if it already exists in the second file
            if (strategyNames.contains(strategyName)) {
                strategyName = makeUnique(strategyName, strategyNames);
            }

            attributes[0] = strategyName;
            strategyNames.add(strategyName);
            mergedData.add(attributes);
        }

        // Add remaining data from the second file (excluding header)
        for (int i = 1; i < dataFile2.size(); i++) {
            mergedData.add(dataFile2.get(i));
        }

        // Write merged data to the second file
        writeOutputFile(file2, mergedData);
    }

    private static List<String[]> readCsvFile(String filename) throws IOException {
        List<String[]> data = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] attributes = line.split(",");
            data.add(attributes);
        }

        reader.close();
        return data;
    }

    private static String makeUnique(String strategyName, Set<String> strategyNames) {
        String originalName = strategyName;
        char suffix = 'a';

        // If strategy name exists, append letters to make it unique
        while (strategyNames.contains(strategyName)) {
            strategyName = originalName + suffix;
            suffix++;
        }

        // Add the unique strategy name to the set
        strategyNames.add(strategyName);

        return strategyName;
    }

    private static void writeOutputFile(String outputFile, List<String[]> mergedData) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

        for (String[] row : mergedData) {
            writer.write(String.join(",", row));
            writer.newLine();
        }

        writer.close();
    }

    public static void main(String[] args) {
        try {
            // Beispielaufruf
            mergeCsvFiles("file1.csv", "file2.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
