package Klassifikator;

import java.util.ArrayList;
import java.util.List;

public class StabilityFromStringCalculator {

    // Statische Funktion zur Berechnung der Stabilität (Standardabweichung) aus einem String
    public static double calculateStabilityFromString(String explainString) {
        List<Double> values = convertStringToDoubles(explainString);

        double mean = calculateMean(values);
        double varianceSum = 0.0;

        // Berechne die Varianz
        for (double value : values) {
            varianceSum += Math.pow(value - mean, 2);
        }

        double variance = varianceSum / values.size();
        return Math.sqrt(variance);  // Standardabweichung als Stabilität
    }

    // Statische Funktion, die den String in eine Liste von Double-Werten umwandelt
    private static List<Double> convertStringToDoubles(String explainString) {
        String[] parts = explainString.trim().split("\\s+");  // String aufteilen nach Leerzeichen
        List<Double> values = new ArrayList<>();

        for (String part : parts) {
            values.add(Double.parseDouble(part));  // Umwandlung von String zu Double
        }

        return values;
    }

    // Statische Funktion zur Berechnung des Mittelwerts
    private static double calculateMean(List<Double> values) {
        double sum = 0.0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.size();
    }

    public static void main(String[] args) {
        // Beispiel: Ein String von Double-Werten
        String explainString = "0.03 -0.03 0.02 -0.02";

        // Berechne die Stabilität basierend auf dem String
        double stability = calculateStabilityFromString(explainString);

        // Ausgabe der Stabilität
        System.out.println("Stabilität: " + stability);
    }
}
