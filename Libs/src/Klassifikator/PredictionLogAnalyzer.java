package Klassifikator;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gui.JLibsProgressWin;

public class PredictionLogAnalyzer
{
    private static boolean isFirstDirectory = true;
    private static boolean useWeightedSum = false;
    private static int periodCounter = 1;

    public static void main(String[] args)
    {
        if (args.length < 1)
        {
            System.out.println("Bitte geben Sie das Root-Verzeichnis an.");
            return;
        }

        String rootDirectory = args[0];
        File rootDir = new File(rootDirectory);

        if (!rootDir.isDirectory())
        {
            System.out.println("Das angegebene Root-Verzeichnis ist ungültig.");
            return;
        }

        try
        {
            boolean useWeighted = args.length >= 2 && Boolean.parseBoolean(args[1]);
            processDirectories(rootDir, useWeighted);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void processDirectories(File rootDir, boolean useWeighted) throws IOException
    {
        useWeightedSum = useWeighted;
        int progresslidercounter = 0;

        //hier ForestPredictPastPeriodProfit.txt löschen
        File outputFile = new File(rootDir, "WORKFLOWNAME/ForestPredictPastPeriodProfit.txt");
        if (outputFile.exists()) {
            outputFile.delete();
            periodCounter = 1;
        }
      
        File[] subDirs = rootDir.listFiles(new FileFilter() 
        {
            @Override
            public boolean accept(File file)
            {
                return file.isDirectory();
            }
        });

        JLibsProgressWin jp = new JLibsProgressWin("Calc Prediction", 0, subDirs.length * 5);
        
        if (subDirs != null && subDirs.length > 0)
        {
            for (File dir : subDirs)
            {
                // "Workflowname"-Verzeichnis ignorieren
                if (!dir.getName().equals("WORKFLOWNAME"))
                {
                    File predictionDir = new File(dir, "_99_dir");
                    if (predictionDir.isDirectory())
                    {
                        processPredictionFiles(predictionDir, new File(rootDir, "WORKFLOWNAME"));
                        jp.update(progresslidercounter);
                        progresslidercounter++;
                    }
                    periodCounter++;
                }
            }
        }
        jp.end();
    }

    private static void processPredictionFiles(File predictionDir, File workflowDir) throws IOException
    {
        Map<String, List<Double>> strategyPredictions = new HashMap<>();
        Map<String, List<Double>> strategyActuals = new HashMap<>();

        File[] predictionFiles = predictionDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name)
            {
                return name.startsWith("prediction_log");
            }
        });

        if (predictionFiles != null)
        {
            for (File file : predictionFiles)
            {
                BufferedReader reader = null;
                try
                {
                    reader = new BufferedReader(new FileReader(file));
                    String line;
                    reader.readLine(); // Skip header line

                    while ((line = reader.readLine()) != null)
                    {
                        String[] parts = line.split("#");
                        if (parts.length == 3)
                        {
                            String strategyName = parts[0];
                            double actualValue = Double.parseDouble(parts[1]);
                            double predictedValue = Double.parseDouble(parts[2]);

                            if (!strategyPredictions.containsKey(strategyName))
                            {
                                strategyPredictions.put(strategyName, new ArrayList<Double>());
                            }
                            if (!strategyActuals.containsKey(strategyName))
                            {
                                strategyActuals.put(strategyName, new ArrayList<Double>());
                            }
                            strategyPredictions.get(strategyName).add(predictedValue);
                            strategyActuals.get(strategyName).add(actualValue);
                        }
                    }
                } finally
                {
                    if (reader != null)
                    {
                        reader.close();
                    }
                }
            }

            writeSummedPredictions(predictionDir, strategyPredictions, strategyActuals, workflowDir);
        }
    }

    private static void writeSummedPredictions(File predictionDir, Map<String, List<Double>> strategyPredictions,
            Map<String, List<Double>> strategyActuals, File workflowDir) throws IOException
    {
        File outputFile = new File(predictionDir, "sum_predict.txt");

        BufferedWriter writer = null;
        try
        {
            writer = new BufferedWriter(new FileWriter(outputFile));

            // Sortiere die Strategien nach den summierten Prediction-Werten in absteigender
            // Reihenfolge
            List<Map.Entry<String, List<Double>>> sortedEntries = new ArrayList<>(strategyPredictions.entrySet());
            Collections.sort(sortedEntries, new Comparator<Map.Entry<String, List<Double>>>() {
                @Override
                public int compare(Map.Entry<String, List<Double>> e1, Map.Entry<String, List<Double>> e2)
                {
                    double sum1 = useWeightedSum ? calculateWeightedSum(e1.getValue()) : calculateUnweightedSum(e1.getValue());
                    double sum2 = useWeightedSum ? calculateWeightedSum(e2.getValue()) : calculateUnweightedSum(e2.getValue());
                    return Double.compare(sum2, sum1);
                }
            });

            for (Map.Entry<String, List<Double>> entry : sortedEntries)
            {
                String strategyName = entry.getKey();
                List<Double> predictions = entry.getValue();

                // Berechne die Summe der Prediction-Werte (gewichtete oder ungewichtete)
                double sum = useWeightedSum ? calculateWeightedSum(predictions) : calculateUnweightedSum(predictions);

                // Erstelle die Zeile im gewünschten Format
                StringBuilder predictionsList = new StringBuilder();
                for (Double value : predictions)
                {
                    if (predictionsList.length() > 0)
                    {
                        predictionsList.append("#");
                    }
                    predictionsList.append(value);
                }

                String line = strategyName + "#" + sum + "#" + predictionsList.toString();
                writer.write(line);
                writer.newLine();
            }
        } finally
        {
            if (writer != null)
            {
                writer.close();
            }
        }

        System.out.println("Ergebnisse gespeichert in: " + outputFile.getAbsolutePath());

        writeForestPredictPastPeriodProfit(strategyActuals, workflowDir, predictionDir, strategyPredictions);
    }

    private static double calculateWeightedSum(List<Double> values)
    {
        double sum = 0.0;
        double weight = 1.0;
        for (int i = 0; i < values.size(); i++)
        {
            sum += values.get(i) * weight;
            weight = Math.max(0.0, weight - 0.1); // Gewicht um 10% reduzieren, mindestens 0
        }
        return sum;
    }

    private static double calculateUnweightedSum(List<Double> values)
    {
        double sum = 0.0;
        for (Double value : values)
        {
            sum += value;
        }
        return sum;
    }

    private static void writeForestPredictPastPeriodProfit(Map<String, List<Double>> strategyActuals, File workflowDir,
            File predictionDir, Map<String, List<Double>> strategyPredictions) throws IOException
    {
        File outputFile = new File(workflowDir, "ForestPredictPastPeriodProfit.txt");
        if (isFirstDirectory && outputFile.exists())
        {
            outputFile.delete();
        }
        isFirstDirectory = false;

        BufferedWriter writer = null;
        try
        {
            writer = new BufferedWriter(new FileWriter(outputFile, true));
            if (!outputFile.exists() || outputFile.length() == 0)
            {
                writer.write("period	allProfitval	selProfitval	anzStrategies");
                writer.newLine();
            }

            // **Die folgenden Schritte greifen auf die tatsächlichen Gewinne (Actual-Werte)
            // zu und nicht auf die Vorhersagewerte**
            // Berechne den Summenwert der besten 10 Strategien und den Summenwert aller
            // Strategien anhand der tatsächlichen Gewinne
            double allProfitSum = 0.0;
            double best10ProfitSum = 0.0;
            int count = 0;
            for (Map.Entry<String, List<Double>> entry : strategyActuals.entrySet())
            {
                double strategySum = useWeightedSum ? calculateWeightedSum(entry.getValue()) : calculateUnweightedSum(entry.getValue());
                allProfitSum += strategySum;
                if (count < 10)
                {
                    best10ProfitSum += strategySum;
                }
                count++;
            }
            int period = periodCounter;

            // Schreibe die Ergebnisszeile
            String resultLine = period + "\t" + allProfitSum + "\t" + best10ProfitSum + "\t"
                    + Math.min(10, strategyActuals.size());
            writer.write(resultLine);
            writer.newLine();
        } finally
        {
            if (writer != null)
            {
                writer.close();
            }
        }

        System.out.println("ForestPredictPastPeriodProfit gespeichert in: " + outputFile.getAbsolutePath());
        copyBestStrategies(predictionDir, strategyPredictions);
    }

    private static void copyBestStrategies(File predictionDir, Map<String, List<Double>> strategyPredictions)
            throws IOException
    {
        File sourceDir = new File(predictionDir, "str__all_sq4_endtestfiles");
        File targetDir = new File(predictionDir, "str__selected_sq4_endtestfiles");

        // Lösche das Zielverzeichnis, falls vorhanden, und erstelle es neu
        if (targetDir.exists())
        {
            for (File file : targetDir.listFiles())
            {
                if (!file.delete())
                {
                    System.err.println("Konnte Datei nicht löschen: " + file.getAbsolutePath());
                }
            }
        } else
        {
            if (!targetDir.mkdirs())
            {
                System.err.println("Konnte Zielverzeichnis nicht erstellen: " + targetDir.getAbsolutePath());
                return;
            }
        }

        // Kopiere die besten 10 Strategien basierend auf den Vorhersagewerten
        List<Map.Entry<String, List<Double>>> sortedEntries = new ArrayList<>(strategyPredictions.entrySet());
        Collections.sort(sortedEntries, new Comparator<Map.Entry<String, List<Double>>>() {
            @Override
            public int compare(Map.Entry<String, List<Double>> e1, Map.Entry<String, List<Double>> e2)
            {
                double sum1 = useWeightedSum ? calculateWeightedSum(e1.getValue()) : calculateUnweightedSum(e1.getValue());
                double sum2 = useWeightedSum ? calculateWeightedSum(e2.getValue()) : calculateUnweightedSum(e2.getValue());
                return Double.compare(sum2, sum1);
            }
        });

        int count = 0;
        for (Map.Entry<String, List<Double>> entry : sortedEntries)
        {
            if (count >= 10)
            {
                break;
            }
            File sourceFile = new File(sourceDir, entry.getKey() + ".sqx");
            File targetFile = new File(targetDir, entry.getKey() + ".sqx");
            if (sourceFile.exists())
            {
                try
                {
                    Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Kopiere " + sourceFile.getName() + " nach " + targetFile.getAbsolutePath());
                } catch (IOException e)
                {
                    System.err.println("Fehler beim Kopieren der Datei " + sourceFile.getAbsolutePath() + " nach "
                            + targetFile.getAbsolutePath());
                    e.printStackTrace();
                }
            } else
            {
                System.err.println("Quelldatei nicht gefunden: " + sourceFile.getAbsolutePath());
            }
            count++;
        }

        System.out.println("Die besten 10 Strategien wurden nach " + targetDir.getAbsolutePath() + " kopiert.");
    }

    private static int getNextPeriodNumber(File file) throws IOException
    {
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new FileReader(file));
            String line;
            int lastPeriod = 0;
            while ((line = reader.readLine()) != null)
            {
                if (!line.startsWith("period"))
                {
                    String[] parts = line.split("\t");
                    if (parts.length > 0)
                    {
                        lastPeriod = Integer.parseInt(parts[0]);
                    }
                }
            }
            return lastPeriod + 1;
        } finally
        {
            if (reader != null)
            {
                reader.close();
            }
        }
    }
}
