package WekaTools;

import java.io.File;
import java.util.ArrayList;

import weka.classifiers.misc.InputMappedClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;

public class WekaModelTesterSimple {

    public static void main(String[] args) throws Exception {
        // Pfade zu den Dateien
        String modelPath = "C:/forex/Metrikanalyser/AR/Q105 EURUSD H1 v1.41 org 11-15/Q105 EURUSD H1 v1.41 org 11-15_+00000/randomForest.model";
        String dataPath = "C:/forex/Metrikanalyser/AR/Q105 EURUSD H1 v1.41 org 11-15/Q105 EURUSD H1 v1.41 org 11-15_--00336/exported_for_weka.csv";

        // Lade das Modell
        //System.out.println("Loading model from: " + modelPath);
        RandomForest forest = (RandomForest) SerializationHelper.read(modelPath);

        // Lade die neuen Daten von der CSV-Datei
        //System.out.println("Loading new data from: " + dataPath);
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(dataPath));
        Instances newData = loader.getDataSet();

      
        
        
        
        // Debug-Ausgabe für neue Daten
        System.out.println("New data loaded: " + newData.numInstances() + " instances, " + newData.numAttributes() + " attributes.");

        // Setze die Klassenattributsindex (letztes Attribut)
        if (newData.classIndex() == -1) {
            newData.setClassIndex(newData.numAttributes() - 1);
        }

        // Ersetze fehlende Werte
        newData = replaceMissingValues(newData);

        // Normalisiere die neuen Daten
        newData = normalizeData(newData);

        // Debug-Ausgabe für Klassenattribut
        System.out.println("Class attribute for new data set to index: " + newData.classIndex());
        System.out.println("Class attribute name: " + newData.classAttribute().name());

        // Debug-Ausgabe für die Struktur der Daten
        System.out.println("New data structure:");
        for (int i = 0; i < newData.numAttributes(); i++) {
            System.out.println(newData.attribute(i).name());
        }

        // Überprüfe, ob die Klassen übereinstimmen
      /*  System.out.println("Checking class values consistency...");
        Instances trainingHeader = (Instances) SerializationHelper.read(modelPath + ".header");
        if (!newData.equalHeaders(trainingHeader)) {
            throw new IllegalArgumentException("The structure of the test data does not match the training data.");
        }
        */
        
        
        
        // Debug-Ausgabe für Klassenattribut
        System.out.println("Class attribute for new data set to index: " + newData.classIndex());
        System.out.println("Class attribute name: " + newData.classAttribute().name());

        // Erstelle InputMappedClassifier und mappe die Daten
        InputMappedClassifier inputMappedClassifier = new InputMappedClassifier();
        inputMappedClassifier.setClassifier(forest);
        inputMappedClassifier.setModelHeader(newData);  // Verwende das Header von newData

        // Variables to calculate metrics
        ArrayList<Double> actualValues = new ArrayList<>();
        ArrayList<Double> predictedValues = new ArrayList<>();

        // Klassifiziere die neuen Daten
        for (int i = 0; i < newData.numInstances(); i++) {
            double actualValue = newData.instance(i).classValue();
            double predictedValue;
            try {
                predictedValue = inputMappedClassifier.classifyInstance(newData.instance(i));
            } catch (Exception e) {
                System.out.println("Error classifying instance " + i + ": " + e.getMessage());
                continue;
            }
            actualValues.add(actualValue);
            predictedValues.add(predictedValue);
            //System.out.println("Instance " + i + ": Actual class=" + actualValue + ", Predicted class=" + predictedValue);
        }

        // Calculate and print correlation
        double correlation = calculateCorrelation(actualValues, predictedValues);
        System.out.println("Correlation coefficient: " + correlation);
    }

    // Methode zur Normalisierung der Daten
    public static Instances normalizeData(Instances data) throws Exception {
        Normalize normalize = new Normalize();
        normalize.setInputFormat(data);
        return Filter.useFilter(data, normalize);
    }

    // Methode zum Ersetzen fehlender Werte
    public static Instances replaceMissingValues(Instances data) throws Exception {
        ReplaceMissingValues replaceMissingValues = new ReplaceMissingValues();
        replaceMissingValues.setInputFormat(data);
        return Filter.useFilter(data, replaceMissingValues);
    }

    // Methode zur Berechnung der Korrelation
    public static double calculateCorrelation(ArrayList<Double> actualValues, ArrayList<Double> predictedValues) {
        double meanActual = mean(actualValues);
        double meanPredicted = mean(predictedValues);

        double sumProduct = 0;
        double sumActualSquared = 0;
        double sumPredictedSquared = 0;

        for (int i = 0; i < actualValues.size(); i++) {
            double actualDiff = actualValues.get(i) - meanActual;
            double predictedDiff = predictedValues.get(i) - meanPredicted;

            sumProduct += actualDiff * predictedDiff;
            sumActualSquared += actualDiff * actualDiff;
            sumPredictedSquared += predictedDiff * predictedDiff;
        }

        return sumProduct / Math.sqrt(sumActualSquared * sumPredictedSquared);
    }

    // Hilfsmethode zur Berechnung des Mittelwerts einer Liste von Werten
    public static double mean(ArrayList<Double> values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.size();
    }
}


