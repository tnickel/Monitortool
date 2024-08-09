
package WekaTools;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import FileTools.Filefunkt;
import hiflsklasse.FileAccess;
import hiflsklasse.Tracer;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.CorrelationAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;

public class WekaLearn
{
	
	public static void loadAndTrainModel(String csvtrainingFile, String modeloutpath, int crossvalidateinstanzanz,
			WekaResultCollector wres, int index, int anztrees) throws Exception
	// crossvalidateinstanzanz = Anzahl der Instanzen bei der Cross-Validierung
	// modeloutpath, das ist das outputfile= RandomForest.model
	{
		try
		{
			wres.addWekaRespDir(index, csvtrainingFile.substring(0, csvtrainingFile.lastIndexOf("\\")));
			
			// Lade den Datensatz von einer CSV-Datei
			CSVLoader loader = new CSVLoader();
			loader.setSource(new File(csvtrainingFile));
			Instances data = loader.getDataSet();
			
		
			
			
			
			
			
			// Debug-Ausgabe für Datensatz
			System.out.println("Datensatz geladen: " + data.numInstances() + " Instanzen, " + data.numAttributes()
					+ " Attribute.");
			
			// Setze die Klassenattributsindex (letztes Attribut)
			if (data.classIndex() == -1)
			{
				data.setClassIndex(data.numAttributes() - 1);
			}
			
			// Debug-Ausgabe für Klassenattribut
			System.out.println("Klassenattribut gesetzt auf Index: " + data.classIndex());
			
			// Normalisiere die Daten
			Normalize normalize = new Normalize();
			normalize.setInputFormat(data);
			Instances normalizedData = Filter.useFilter(data, normalize);
			
			// Debug-Ausgabe für Normalisierung
			System.out.println("Daten wurden normalisiert.");
			
			// Erstelle und konfiguriere den RandomForest-Klassifikator
			RandomForest forest = new RandomForest();
			String[] options = new String[8];
			options[0] = "-I";
			options[1] = String.valueOf(anztrees); // Number of trees in the forest
			options[2] = "-K";
			options[3] = "0"; // Number of features to consider per node
			options[4] = "-S";
			options[5] = "1"; // Seed for the random number generator
			options[6] = "-num-slots";
			options[7] = "32"; // Number of slots
			
			forest.setOptions(options);
			
			// Debug-Ausgabe für Optionen
			System.out.println("RandomForest Optionen gesetzt: " + java.util.Arrays.toString(forest.getOptions()));
			
			// Trainiere das Modell
			forest.buildClassifier(normalizedData);
			
			// Evaluiere das Modell
			Evaluation eval = new Evaluation(normalizedData);
			eval.crossValidateModel(forest, normalizedData, crossvalidateinstanzanz, new Random(1));
			
			// Ausgabe der Evaluierung
			System.out.println(eval.toSummaryString("\nErgebnisse\n======\n", false));
			String summary = eval.toSummaryString("\nErgebnisse\n======\n", false);
			wres.addWekaEvalResp(index, eval, modeloutpath, csvtrainingFile);
			
			// Modell speichern
			SerializationHelper.write(modeloutpath, forest);
			
			// Laden Sie das Modell (optional, wenn Sie es später verwenden möchten)
			// RandomForest loadedForest = (RandomForest)
			// SerializationHelper.read("randomForest.model");
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void loadAndTrainModelFilter2_depricated(String csvtrainingFile, String modeloutpath, int crossvalidateinstanzanz,
			WekaResultCollector wres, int index, int anztrees) throws Exception
	// crossvalidateinstanzanz = Anzahl der Instanzen bei der Cross-Validierung
	// modeloutpath, das ist das outputfile= RandomForest.model
	{
		   try {
	            wres.addWekaRespDir(index, csvtrainingFile.substring(0, csvtrainingFile.lastIndexOf("\\")));

	            // Lade den Datensatz von einer CSV-Datei
	            CSVLoader loader = new CSVLoader();
	            loader.setSource(new File(csvtrainingFile));
	            Instances data = loader.getDataSet();

	            // Finde den Index des Attributs "Strategy_Name"
	            int strategyNameIndex = -1;
	            for (int i = 0; i < data.numAttributes(); i++) {
	                if (data.attribute(i).name().equals("Strategy_Name")) {
	                    strategyNameIndex = i + 1; // Weka-Indices sind 1-basiert
	                    break;
	                }
	            }

	            if (strategyNameIndex == -1) {
	                throw new IllegalArgumentException("Attribute 'Strategy_Name' not found in the dataset.");
	            }

	            // Initialize AttributeSelection
	            AttributeSelection attrSelection = new AttributeSelection();
	            CorrelationAttributeEval eval = new CorrelationAttributeEval();
	            Ranker search = new Ranker();

	            // Specify the index of the attribute to keep
	            String attributesToKeep = String.valueOf(strategyNameIndex);
	            search.setOptions(new String[]{"-T", "0.1", "-P", attributesToKeep});
	            attrSelection.setEvaluator(eval);
	            attrSelection.setSearch(search);

	            // Perform attribute selection
	            attrSelection.SelectAttributes(data);

	            // Create a new dataset with only the selected attributes
	            Instances newData = attrSelection.reduceDimensionality(data);

	            // Save the reduced dataset to a new CSV file
	            CSVSaver saver = new CSVSaver();
	            saver.setInstances(newData);
	            String outfile = csvtrainingFile.substring(0, csvtrainingFile.lastIndexOf(".")) + "_reduced.csv";
	            saver.setFile(new File(outfile));
	            saver.writeBatch();

	            // Debug-Ausgabe für Datensatz
	            System.out.println("Datensatz geladen: " + newData.numInstances() + " Instanzen, " + newData.numAttributes() + " Attribute.");

	            // Setze die Klassenattributsindex (letztes Attribut)
	            if (newData.classIndex() == -1) {
	                newData.setClassIndex(newData.numAttributes() - 1);
	            }

	            // Debug-Ausgabe für Klassenattribut
	            System.out.println("Klassenattribut gesetzt auf Index: " + newData.classIndex());

	            // Normalisiere die Daten
	            Normalize normalize = new Normalize();
	            normalize.setInputFormat(newData);
	            Instances normalizedData = Filter.useFilter(newData, normalize);

	            // Debug-Ausgabe für Normalisierung
	            System.out.println("Daten wurden normalisiert.");

	            // Erstelle und konfiguriere den RandomForest-Klassifikator
	            RandomForest forest = new RandomForest();
	            String[] options = new String[8];
	            options[0] = "-I";
	            options[1] = String.valueOf(anztrees); // Number of trees in the forest
	            options[2] = "-K";
	            options[3] = "0"; // Number of features to consider per node
	            options[4] = "-S";
	            options[5] = "1"; // Seed for the random number generator
	            options[6] = "-num-slots";
	            options[7] = "32"; // Number of slots

	            forest.setOptions(options);

	            // Debug-Ausgabe für Optionen
	            System.out.println("RandomForest Optionen gesetzt: " + java.util.Arrays.toString(forest.getOptions()));

	            // Trainiere das Modell
	            forest.buildClassifier(normalizedData);

	            // Evaluiere das Modell
	            Evaluation evalution = new Evaluation(normalizedData);
	            evalution.crossValidateModel(forest, normalizedData, crossvalidateinstanzanz, new Random(1));

	            // Ausgabe der Evaluierung
	            System.out.println(evalution.toSummaryString("\nErgebnisse\n======\n", false));
	            String summary = evalution.toSummaryString("\nErgebnisse\n======\n", false);
	            wres.addWekaEvalResp(index, evalution, modeloutpath, csvtrainingFile);

	            // Modell speichern
	            SerializationHelper.write(modeloutpath, forest);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    
	}
	public static WekaClassifierElem classifyNewData(String modelPath, String newDataPath,
			int instanzanzCrossvalidation) throws Exception
	{
		// instanzanzCrossvalidation = Anzahl der Instanzen bei der Cross-Validierung.
		// Bei 500er Batchsize sollte man instanzanzahl auf 3 setzen.
		try
		{
			// Load the model
			//System.out.println("Loading model from: " + modelPath);
			RandomForest forest = (RandomForest) SerializationHelper.read(modelPath);
			
			// Load the new data from a CSV file
			//System.out.println("Loading new data from: " + newDataPath);
			CSVLoader loader = new CSVLoader();
			loader.setSource(new File(newDataPath));
			Instances newData = loader.getDataSet();
			
			// Debug output for new data
			//System.out.println("New data loaded: " + newData.numInstances() + " instances, " + newData.numAttributes()
				//	+ " attributes.");
			
			// Set the class attribute index (last attribute)
			if (newData.classIndex() == -1)
			{
				newData.setClassIndex(newData.numAttributes() - 1);
			}
			
			// Normalisiere die Daten
			Normalize normalize = new Normalize();
			normalize.setInputFormat(newData);
			Instances normalizedNewData = Filter.useFilter(newData, normalize);
			
			// Debug output for normalized data
			//System.out.println("New data normalized.");
			
			// Variables to calculate metrics
			ArrayList<Double> actualValues = new ArrayList<>();
			ArrayList<Double> predictedValues = new ArrayList<>();
			double sumAbsoluteError = 0;
			double sumSquaredError = 0;
			double sumActual = 0;
			double sumActualSquared = 0;
			
			// Classify the new data
			WekaClassifierElem wekaresult=new WekaClassifierElem(0);
			
			for (int i = 0; i < normalizedNewData.numInstances(); i++)
			{
				double actualValue = normalizedNewData.instance(i).classValue();
				double predictedValue = forest.classifyInstance(normalizedNewData.instance(i));
				actualValues.add(actualValue);
				predictedValues.add(predictedValue);
				double error = Math.abs(actualValue - predictedValue);
				sumAbsoluteError += error;
				sumSquaredError += error * error;
				sumActual += actualValue;
				sumActualSquared += actualValue * actualValue;
				
				// Get the actual class value
				String actualClassValue;
				if (normalizedNewData.classAttribute().isNominal() || normalizedNewData.classAttribute().isString())
				{
					actualClassValue = normalizedNewData.classAttribute().value((int) actualValue);
				} else
				{
					actualClassValue = String.valueOf(actualValue);
				}
				
				// Get the predicted class value
				String predictedClassValue;
				if (normalizedNewData.classAttribute().isNominal())
				{
					predictedClassValue = normalizedNewData.classAttribute().value((int) predictedValue);
				} else
				{
					predictedClassValue = String.valueOf(predictedValue);
				}
				
				String attributeName = normalizedNewData.classAttribute().name();
				// System.out.println("endtest attribname=" + attributeName);
				
				// Get the value of "Strategy Name" attribute for the current instance
				Attribute strategyNameAttr = normalizedNewData.attribute("Strategy_Name");
				if (strategyNameAttr != null)
				{
					String strategyNameValue;
					if (strategyNameAttr.isNominal() || strategyNameAttr.isString())
					{
						strategyNameValue = normalizedNewData.instance(i).stringValue(strategyNameAttr);
					} else
					{
						strategyNameValue = String.valueOf(normalizedNewData.instance(i).value(strategyNameAttr));
					}
					//System.out.println("Instance " + i + ": Strategy Name=" + strategyNameValue + ", Actual class="
						//	+ actualClassValue + ", Predicted class=" + predictedClassValue);
				} else
				{
					System.out.println("Attribute 'Strategy_Name' not found in instance " + i);
				}
				
			}
			
			System.out.print("Generate Statistics...");
			// Calculate and print summary metrics
			int n = normalizedNewData.numInstances();
			double mae = sumAbsoluteError / n;
			double rmse = Math.sqrt(sumSquaredError / n);
			double meanActual = sumActual / n;
			double rae = sumAbsoluteError / totalAbsoluteError(actualValues, meanActual) * 100;
			double rrse = Math.sqrt(sumSquaredError / totalSquaredError(actualValues, meanActual)) * 100;
			double correlation = calculateCorrelation(actualValues, predictedValues);
			
			System.out.println("\nSummary of Predictions:");
			System.out.println("Correlation coefficient: " + correlation);
			System.out.println("Mean absolute error: " + mae);
			System.out.println("Root mean squared error: " + rmse);
			System.out.println("Relative absolute error: " + rae + " %");
			System.out.println("Root relative squared error: " + rrse + " %");
			Tracer.WriteTrace(20, "I**:classifier new data model<" + modelPath + "> data<" + newDataPath + "> result<"
					+ correlation + ">");
			wekaresult.setCorrelationVal(correlation);
			return wekaresult;
			
		} catch (Exception e)
		{
			e.printStackTrace();
			return new WekaClassifierElem(0);
		}
	}
	
	public static WekaClassifierElem classifyNewDataCopyStrategies(String datadir, String modelPath, String newDataPath,
			int instanzanzCrossvalidationk,double minprofit,boolean copyflag) throws Exception
	{
		// instanzanzCrossvalidation = Anzahl der Instanzen bei der Cross-Validierung.
		// Bei 500er Batchsize sollte man instanzanzahl auf 3 setzen.
		
		// clean directory
		String spath = datadir + "\\_99_dir\\str__selected_sq4_endtestfiles";
		FileAccess.cleanDirectory(new File(spath));
		int copycounter=0;
		
		try
		{
			// Load the model
			//System.out.println("Loading model from: " + modelPath);
			RandomForest forest = (RandomForest) SerializationHelper.read(modelPath);
			
			// Load the new data from a CSV file
			//System.out.println("Loading new data from: " + newDataPath);
			CSVLoader loader = new CSVLoader();
			loader.setSource(new File(newDataPath));
			Instances newData = loader.getDataSet();
			
			// Debug output for new data
			//System.out.println("New data loaded: " + newData.numInstances() + " instances, " + newData.numAttributes()
				//	+ " attributes.");
			
			// Set the class attribute index (last attribute)
			if (newData.classIndex() == -1)
			{
				newData.setClassIndex(newData.numAttributes() - 1);
			}
			
			// Normalisiere die Daten
			Normalize normalize = new Normalize();
			normalize.setInputFormat(newData);
			Instances normalizedNewData = Filter.useFilter(newData, normalize);
			
			// Debug output for normalized data
			//System.out.println("New data normalized.");
			
			// Variables to calculate metrics
			ArrayList<Double> actualValues = new ArrayList<>();
			ArrayList<Double> predictedValues = new ArrayList<>();
			double sumAbsoluteError = 0;
			double sumSquaredError = 0;
			double sumActual = 0;
			double sumActualSquared = 0;
			
			WekaClassifierElem wekaclassify=new WekaClassifierElem(0);
			// Classify the new data
			for (int i = 0; i < normalizedNewData.numInstances(); i++)
			{
				double actualValue = normalizedNewData.instance(i).classValue();
				double predictedValue = forest.classifyInstance(normalizedNewData.instance(i));
				actualValues.add(actualValue);
				predictedValues.add(predictedValue);
				double error = Math.abs(actualValue - predictedValue);
				sumAbsoluteError += error;
				sumSquaredError += error * error;
				sumActual += actualValue;
				sumActualSquared += actualValue * actualValue;
				
				// Get the actual class value
				String actualClassValue;
				if (normalizedNewData.classAttribute().isNominal() || normalizedNewData.classAttribute().isString())
				{
					actualClassValue = normalizedNewData.classAttribute().value((int) actualValue);
				} else
				{
					actualClassValue = String.valueOf(actualValue);
				}
				
				// Get the predicted class value
				String predictedClassValue;
				if (normalizedNewData.classAttribute().isNominal())
				{
					predictedClassValue = normalizedNewData.classAttribute().value((int) predictedValue);
				} else
				{
					predictedClassValue = String.valueOf(predictedValue);
				}
				
				Attribute strategyNameAttr = normalizedNewData.attribute("Strategy_Name");
				if (strategyNameAttr != null)
				{
					String strategyNameValue;
					if (strategyNameAttr.isNominal() || strategyNameAttr.isString())
					{
						strategyNameValue = normalizedNewData.instance(i).stringValue(strategyNameAttr);
					} else
					{
						strategyNameValue = String.valueOf(normalizedNewData.instance(i).value(strategyNameAttr));
					}
					System.out.println("Instance " + i + ": Strategy_Name=" + strategyNameValue + ", Actual class="
							+ actualClassValue + ", Predicted class=" + predictedClassValue);

					
					
					if (Double.valueOf(predictedClassValue) > minprofit)
					{
						// copy Strategy
						String s1path = datadir + "\\_99_dir\\str__all_sq4_endtestfiles\\" + strategyNameValue + ".sqx";
						String s2path = datadir + "\\_99_dir\\str__selected_sq4_endtestfiles\\" + strategyNameValue
								+ ".sqx";
						if (new File(s1path).exists())
						{
							Filefunkt fc=new Filefunkt();
							if(copyflag==true)
							fc.copyFile(new File(s1path), new File(s2path));
							copycounter++;
							
						}
					}
				} else
				{
					System.out.println("Attribute 'Strategy_Name' not found in instance " + i);
				}
			}
			
			//System.out.print("Generate Statistics...");
			// Calculate and print summary metrics
			int n = normalizedNewData.numInstances();
			double mae = sumAbsoluteError / n;
			double rmse = Math.sqrt(sumSquaredError / n);
			double meanActual = sumActual / n;
			double rae = sumAbsoluteError / totalAbsoluteError(actualValues, meanActual) * 100;
			double rrse = Math.sqrt(sumSquaredError / totalSquaredError(actualValues, meanActual)) * 100;
			double correlation = calculateCorrelation(actualValues, predictedValues);
			
			//System.out.println("\nSummary of Predictions:");
			System.out.println("Correlation coefficient: " + correlation);
			//System.out.println("Mean absolute error: " + mae);
			//System.out.println("Root mean squared error: " + rmse);
			//System.out.println("Relative absolute error: " + rae + " %");
			//System.out.println("Root relative squared error: " + rrse + " %");
			//Tracer.WriteTrace(20, "I**:classifier new data model<" + modelPath + "> data<" + newDataPath + "> result<"
			//		+ correlation + ">");
			Tracer.WriteTrace(20, "I: I have copied #strategies=<"+copycounter+"/"+normalizedNewData.numInstances()+"> to selected endtest");
			wekaclassify.setCorrelationVal(correlation);
			wekaclassify.setCopycounter(copycounter);
			return wekaclassify;
			
		} catch (Exception e)
		{
			e.printStackTrace();
			return new WekaClassifierElem(0);
		}
	}
	
	// Method to calculate the correlation between actual and predicted values
	private static double calculateCorrelation(ArrayList<Double> actualValues, ArrayList<Double> predictedValues)
	{
		double meanActual = mean(actualValues);
		double meanPredicted = mean(predictedValues);
		
		double sumProduct = 0;
		double sumActualSquared = 0;
		double sumPredictedSquared = 0;
		
		for (int i = 0; i < actualValues.size(); i++)
		{
			double actualDiff = actualValues.get(i) - meanActual;
			double predictedDiff = predictedValues.get(i) - meanPredicted;
			
			sumProduct += actualDiff * predictedDiff;
			sumActualSquared += actualDiff * actualDiff;
			sumPredictedSquared += predictedDiff * predictedDiff;
		}
		
		return sumProduct / Math.sqrt(sumActualSquared * sumPredictedSquared);
	}
	
	// Helper method to calculate the mean of a list of values
	private static double mean(ArrayList<Double> values)
	{
		double sum = 0;
		for (double value : values)
		{
			sum += value;
		}
		return sum / values.size();
	}
	
	// Helper method to calculate total absolute error
	private static double totalAbsoluteError(ArrayList<Double> values, double mean)
	{
		double totalError = 0;
		for (double value : values)
		{
			totalError += Math.abs(value - mean);
		}
		return totalError;
	}
	
	// Helper method to calculate total squared error
	private static double totalSquaredError(ArrayList<Double> values, double mean)
	{
		double totalError = 0;
		for (double value : values)
		{
			double diff = value - mean;
			totalError += diff * diff;
		}
		return totalError;
	}
	
	public static Instances loadNewData(String newFilePath) throws Exception
	{
		// Load new CSV data
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File(newFilePath));
		loader.setOptions(new String[]
		{ "-H" }); // Ensure the header is used
		Instances newData = loader.getDataSet();
		// Set the class index (target variable)
		newData.setClassIndex(newData.numAttributes() - 1);
		return newData;
	}
	
	// Methode zum Ersetzen fehlender Werte
	public static Instances replaceMissingValues(Instances data) throws Exception
	{
		ReplaceMissingValues replaceMissingValues = new ReplaceMissingValues();
		replaceMissingValues.setInputFormat(data);
		return Filter.useFilter(data, replaceMissingValues);
	}
}