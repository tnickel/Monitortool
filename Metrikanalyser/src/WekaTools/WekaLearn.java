
package WekaTools;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import FileTools.Filefunkt;
import hiflsklasse.FileAccess;
import hiflsklasse.Tracer;
import hilfsklasse.Inf;
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
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;

public class WekaLearn
{
	
	

	public static boolean loadAndTrainModel(String csvtrainingFile, String modeloutpath, int crossvalidateinstanzanz,
	        WekaResultCollector wres, int index, int anztrees, boolean usenorm) throws Exception
	{
	    try
	    {
	        wres.addWekaRespDir(index, csvtrainingFile.substring(0, csvtrainingFile.lastIndexOf("\\")));
	        
	        // Lade den Datensatz von einer CSV-Datei
	        CSVLoader loader = new CSVLoader();
	        loader.setSource(new File(csvtrainingFile));
	        Instances idata = loader.getDataSet();
	        Instances data = null;
	        
	        if (!new File(csvtrainingFile).exists())
	        {
	            Tracer.WriteTrace(10, "E: file <" + csvtrainingFile + "> not available for learning ");
	            return false;
	        }
	        
	        // Debug-Ausgabe für Datensatz
	        System.out.println("Datensatz geladen: " + idata.numInstances() + " Instanzen, " + idata.numAttributes()
	                + " Attribute.");
	        
	        // Konvertiere das "Strategy_Name"-Attribut in nominal, falls erforderlich
	        Attribute strategyNameAttr = idata.attribute("Strategy_Name");
	        if (strategyNameAttr != null && idata.attribute(strategyNameAttr.index()).isNumeric()) {
	            NumericToNominal convert = new NumericToNominal();
	            convert.setAttributeIndices(String.valueOf(strategyNameAttr.index() + 1)); // Weka ist 1-basiert
	            convert.setInputFormat(idata);
	            idata = Filter.useFilter(idata, convert);
	        }
	        
	        // Setze die Klassenattributsindex (letztes Attribut)
	        if (idata.classIndex() == -1)
	        {
	            idata.setClassIndex(idata.numAttributes() - 1);
	        }
	        
	        // Debug-Ausgabe für Klassenattribut
	        System.out.println("Class attribut will be set to Index: " + idata.classIndex() + ", Name: "
	                + idata.classAttribute().name());
	        
	        // Überprüfen, ob das Klassenattribut in den Trainingsdaten enthalten ist
	        boolean classAttrInData = false;
	        for (int i = 0; i < idata.numAttributes() - 1; i++)
	        {
	            if (idata.attribute(i).name().equals(idata.classAttribute().name()))
	            {
	                classAttrInData = true;
	                break;
	            }
	        }
	        
	        if (classAttrInData)
	        {
	            System.out.println("Warnung: This is the attribut to classified '" + idata.classAttribute().name()
	                    + "' is in the trainings data.");
	            Tracer.WriteTrace(10, "E:weka_endtest is in training data-->Stop");
	        } else
	        {
	            System.out.println("Security-Check: The attribut to classify  '" + idata.classAttribute().name()
	                    + "' is not in the trainings data-> Check ok");
	        }
	        
	        // Normalisiere die Daten, falls erforderlich
	        if (usenorm == true)
	        {
	            Normalize normalize = new Normalize();
	            normalize.setInputFormat(idata);
	            data = Filter.useFilter(idata, normalize);
	        } else
	        {
	            data = idata;
	        }
	        
	        // Debug-Ausgabe für Normalisierung
	        Tracer.WriteTrace(20, "Data will be normalized");
	        
	        // Erstelle und konfiguriere den RandomForest-Klassifikator
	        RandomForest forest = new RandomForest();
	        String[] options = new String[8];
	        options[0] = "-I";
	        options[1] = String.valueOf(anztrees); // Anzahl der Bäume im Wald
	        options[2] = "-K";
	        options[3] = "0"; // Anzahl der zu betrachtenden Features pro Knoten
	        options[4] = "-S";
	        options[5] = "1"; // Seed für den Zufallszahlengenerator
	        options[6] = "-num-slots";
	        options[7] = "16"; // Anzahl der Threads/Slots
	        
	        forest.setOptions(options);
	        
	        // Debug-Ausgabe für Optionen
	        System.out.println("RandomForest option set: " + java.util.Arrays.toString(forest.getOptions()));
	        
	        // Trainiere das Modell
	        forest.buildClassifier(data);
	        
	        // Feature-Importanz überprüfen (falls erforderlich)
	        checkFeatureImportance(data, csvtrainingFile);
	        
	        // Evaluiere das Modell
	        Evaluation eval = new Evaluation(data);
	        eval.crossValidateModel(forest, data, crossvalidateinstanzanz, new Random(1));
	        
	        // Ausgabe der Evaluierung
	        System.out.println(eval.toSummaryString("\nResults\n======\n", false));
	        String summary = eval.toSummaryString("\nResults\n======\n", false);
	        wres.addWekaEvalResp(index, eval, modeloutpath, csvtrainingFile);
	        
	        // Modell speichern
	        SerializationHelper.write(modeloutpath, forest);
	        
	        // Speicherbereinigung
	        idata = null;
	        data = null;
	        forest = null;
	        loader = null;
	        eval = null;
	        System.gc();
	        return true;
	        
	    } catch (Exception e)
	    {
	        e.printStackTrace();
	        Tracer.WriteTrace(10, "E:Weka exception<" + e.getMessage() + ">-->Stop");
	        return false;
	    }
	}


	
	private static void checkFeatureImportance(Instances data, String file) throws Exception
	{
		int onceflag = 0;
		// Verwende CorrelationAttributeEval, um numerische Klassen zu unterstützen
		AttributeSelection attrSelection = new AttributeSelection();
		CorrelationAttributeEval eval = new CorrelationAttributeEval(); // Korrelation als Evaluator
		Ranker ranker = new Ranker();
		attrSelection.setEvaluator(eval);
		attrSelection.setSearch(ranker);
		
		// Führe die Attributauswahl durch
		attrSelection.SelectAttributes(data);
		
		// Abrufen der Wichtigkeit der Attribute und Warnungen ausgeben
		double maxFeatureImportanceThreshold = 0.98; // Beispielwert
		
		for (int i = 0; i < data.numAttributes() - 1; i++) // Looping through all features except the class attribute
		{
			double importance = eval.evaluateAttribute(i);
			// System.out.println("Feature: " + data.attribute(i).name() + " - Correlation:
			// " + importance);
			if ((importance > maxFeatureImportanceThreshold) && (onceflag == 0))
			{
				System.out.println(
						"Warnung: Feature '" + data.attribute(i).name() + "' has a high correlation of " + importance);
				Tracer.WriteTrace(10, "Warnung: Feature '" + data.attribute(i).name() + "' has a high correlation of "
						+ importance + " i=" + i + "file<" + file + ">");
				onceflag = 1;
			}
		}
	}
	
	public static void loadAndTrainModelFilter2_depricated(String csvtrainingFile, String modeloutpath,
			int crossvalidateinstanzanz, WekaResultCollector wres, int index, int anztrees) throws Exception
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
			
			// Finde den Index des Attributs "Strategy_Name"
			int strategyNameIndex = -1;
			for (int i = 0; i < data.numAttributes(); i++)
			{
				if (data.attribute(i).name().equals("Strategy_Name"))
				{
					strategyNameIndex = i + 1; // Weka-Indices sind 1-basiert
					break;
				}
			}
			
			if (strategyNameIndex == -1)
			{
				throw new IllegalArgumentException("Attribute 'Strategy_Name' not found in the dataset.");
			}
			
			// Initialize AttributeSelection
			AttributeSelection attrSelection = new AttributeSelection();
			CorrelationAttributeEval eval = new CorrelationAttributeEval();
			Ranker search = new Ranker();
			
			// Specify the index of the attribute to keep
			String attributesToKeep = String.valueOf(strategyNameIndex);
			search.setOptions(new String[]
			{ "-T", "0.1", "-P", attributesToKeep });
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
			System.out.println("Datensatz geladen: " + newData.numInstances() + " Instanzen, " + newData.numAttributes()
					+ " Attribute.");
			
			// Setze die Klassenattributsindex (letztes Attribut)
			if (newData.classIndex() == -1)
			{
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
			options[7] = "16"; // Number of slots
			
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
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static WekaClassifierElem classifyNewData(String modelPath, String newDataPath,
			int instanzanzCrossvalidation, boolean normflag, String workdir) throws Exception
	{
		String classifyresultsfile = null;
		String goodbadpredictionfile=null;
		
		Inf inf = null;
		Inf goodbadinf=null;
		if (workdir != null)
		{
			classifyresultsfile = workdir + "\\classifyresults.txt";
			FileAccess.FileDelete(classifyresultsfile, 1);
			inf = new Inf();
			inf.setFilename(classifyresultsfile);
			
			goodbadpredictionfile = workdir + "\\goodbadprediction.txt";
			FileAccess.FileDelete(goodbadpredictionfile, 1);
			goodbadinf = new Inf();
			goodbadinf.setFilename(goodbadpredictionfile);
		}
		try
		{
			// Load the model
			RandomForest forest = (RandomForest) SerializationHelper.read(modelPath);
			
			if (forest == null)
			{
				Tracer.WriteTrace(20, "E: no modelfile<" + modelPath + "> ");
				return null;
			}
			
			// Load the new data from a CSV file
			CSVLoader loader = new CSVLoader();
			loader.setSource(new File(newDataPath));
			Instances inewData = loader.getDataSet();
			Instances newData = null;
			
			// Convert the "Strategy_Name" attribute to nominal if necessary
			Attribute strategyNameAttr = inewData.attribute("Strategy_Name");
			if (strategyNameAttr != null && inewData.attribute(strategyNameAttr.index()).isNumeric())
			{
				// Convert the numeric "Strategy_Name" attribute to nominal
				NumericToNominal convert = new NumericToNominal();
				convert.setAttributeIndices(String.valueOf(strategyNameAttr.index() + 1)); // Weka is 1-based for
																							// filters
				convert.setInputFormat(inewData);
				inewData = Filter.useFilter(inewData, convert);
			}
			
			// Set the class attribute index (last attribute)
			if (inewData.classIndex() == -1)
			{
				inewData.setClassIndex(inewData.numAttributes() - 1);
			}
			
			if (normflag == true)
			{
				// Normalize the new data
				Normalize normalize = new Normalize();
				normalize.setInputFormat(inewData);
				newData = Filter.useFilter(inewData, normalize);
			} else
			{
				newData = inewData;
			}
			
			// Debug output for new data
			System.out.println("New data loaded: " + newData.numInstances() + " instances, " + newData.numAttributes()
					+ " attributes.");
			
			// Variables to calculate metrics
			ArrayList<Double> actualValues = new ArrayList<>();
			ArrayList<Double> predictedValues = new ArrayList<>();
			double sumAbsoluteError = 0;
			double sumSquaredError = 0;
			double sumActual = 0;
			double sumActualSquared = 0;
			
			// Classify the new data
			WekaClassifierElem wekaresult = new WekaClassifierElem(0);
			
			for (int i = 0; i < newData.numInstances(); i++)
			{
				double actualValue = newData.instance(i).classValue();
				double predictedValue = forest.classifyInstance(newData.instance(i));
				actualValues.add(actualValue);
				predictedValues.add(predictedValue);
				double error = Math.abs(actualValue - predictedValue);
				sumAbsoluteError += error;
				sumSquaredError += error * error;
				sumActual += actualValue;
				sumActualSquared += actualValue * actualValue;
				
				// Get the actual class value
				String actualClassValue;
				if (newData.classAttribute().isNominal() || newData.classAttribute().isString())
				{
					actualClassValue = newData.classAttribute().value((int) actualValue);
				} else
				{
					actualClassValue = String.valueOf(actualValue);
				}
				
				// Get the predicted class value
				String predictedClassValue;
				if (newData.classAttribute().isNominal())
				{
					predictedClassValue = newData.classAttribute().value((int) predictedValue);
				} else
				{
					predictedClassValue = String.valueOf(predictedValue);
				}
				
				// Get the value of "Strategy_Name" attribute for the current instance
				if (strategyNameAttr != null)
				{
					String strategyNameValue;
					if (strategyNameAttr.isNominal() || strategyNameAttr.isString())
					{
						strategyNameValue = newData.instance(i).stringValue(strategyNameAttr);
					} else
					{
						strategyNameValue = String.valueOf(newData.instance(i).value(strategyNameAttr));
					}
					String zeile = "Instance " + i + ": Strategy Name=" + strategyNameValue + ", Actual class="
							+ actualClassValue + ", Predicted class=" + predictedClassValue;
					if (workdir != null)
						inf.writezeile(zeile);
				} else
				{
					System.out.println("Attribute 'Strategy_Name' not found in instance " + i);
				}
			}
			
			if (workdir != null)
				inf.close();
			
			WriteGoodBadPrediction(goodbadinf,predictedValues);
			goodbadinf.close();
			System.out.print("Generate Statistics...");
			// Calculate and print summary metrics
			int n = newData.numInstances();
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
	
	static private void WriteGoodBadPrediction(Inf inf,ArrayList<Double> predictedValues)
	{
		int anz=predictedValues.size();
		int anzgood=0;
		int anzbad=0;
		for(int i=0; i<anz; i++)
		{
			double val=predictedValues.get(i);
			if(val>0)
				anzgood++;
			else if(val<=0)
				anzbad++;
			else
				Tracer.WriteTrace(10, "E:internal error 4954943775");
			
		}
		inf.writezeile("Goodcounter#Badcounter");
		inf.writezeile(anzgood+"#"+anzbad);
		
		
	}

	public static WekaClassifierElem classifyNewDataCopyStrategies(String datadir, String modelPath, String newDataPath,
	        int instanzanzCrossvalidationk, double minprofit, boolean allowcopyflag, boolean normflag,
	        boolean takebestflag, int anzbest, String attribfilename, boolean useminprofitPlusTakeNBestflag)
	        throws Exception
	{
	    String spath = datadir + "\\_99_dir\\str__selected_sq4_endtestfiles";
	    String predictionlog = datadir + "\\_99_dir\\prediction_log.txt";

	    FileAccess.FileDelete(predictionlog, 1);
	    Inf inf = new Inf();
	    inf.setFilename(predictionlog);

	    FileAccess.cleanDirectory(new File(spath));

	    double sumAllFilesProfit = 0;

	    try
	    {
	        // Load the model
	        RandomForest forest = (RandomForest) SerializationHelper.read(modelPath);

	        // Load the new data from a CSV file
	        Tracer.WriteTrace(20,
	                "Loading new data to classify from: " + newDataPath + " use learner from:" + modelPath);
	        CSVLoader loader = new CSVLoader();
	        loader.setSource(new File(newDataPath));
	        Instances inewData = loader.getDataSet();
	        Instances newData = null;

	        // Convert the "Strategy_Name" attribute to nominal if necessary
	        Attribute strategyNameAttr = inewData.attribute("Strategy_Name");
	        if (strategyNameAttr != null && inewData.attribute(strategyNameAttr.index()).isNumeric()) {
	            // Convert the numeric "Strategy_Name" attribute to nominal
	            NumericToNominal convert = new NumericToNominal();
	            convert.setAttributeIndices(String.valueOf(strategyNameAttr.index() + 1)); // Weka is 1-based for filters
	            convert.setInputFormat(inewData);
	            inewData = Filter.useFilter(inewData, convert);
	        }

	        // Set the class attribute index (last attribute)
	        if (inewData.classIndex() == -1)
	        {
	            inewData.setClassIndex(inewData.numAttributes() - 1);
	        }

	        if (normflag == true)
	        {
	            // Normalize the data
	            Normalize normalize = new Normalize();
	            normalize.setInputFormat(inewData);
	            newData = Filter.useFilter(inewData, normalize);
	        }
	        else
	        {
	            newData = inewData;
	        }

	        // Variables to calculate metrics
	        ArrayList<Double> actualValues = new ArrayList<>();
	        ArrayList<Double> predictedValues = new ArrayList<>();

	        WekaClassifierBestlist bestlist = new WekaClassifierBestlist();
	        bestlist.setCopyflag(allowcopyflag);
	        bestlist.setMinprofit(minprofit);

	        WekaClassifierElem wekaclassify = new WekaClassifierElem(0);
	        // Classify the new data
	        for (int i = 0; i < newData.numInstances(); i++)
	        {
	            double actualValue = newData.instance(i).classValue();
	            double predictedValue = forest.classifyInstance(newData.instance(i));
	            actualValues.add(actualValue);
	            predictedValues.add(predictedValue);

	            // Get the actual class value
	            String actualClassValue;
	            if (newData.classAttribute().isNominal() || newData.classAttribute().isString())
	            {
	                actualClassValue = newData.classAttribute().value((int) actualValue);
	            }
	            else
	            {
	                actualClassValue = String.valueOf(actualValue);
	            }

	            // Get the predicted class value
	            String predictedClassValue;
	            if (newData.classAttribute().isNominal())
	            {
	                predictedClassValue = newData.classAttribute().value((int) predictedValue);
	            }
	            else
	            {
	                predictedClassValue = String.valueOf(predictedValue);
	            }

	            // Handling the "Strategy_Name" attribute
	            if (strategyNameAttr != null)
	            {
	                String strategyNameValue;
	                if (strategyNameAttr.isNominal() || strategyNameAttr.isString())
	                {
	                    strategyNameValue = newData.instance(i).stringValue(strategyNameAttr);
	                }
	                else
	                {
	                    strategyNameValue = String.valueOf(newData.instance(i).value(strategyNameAttr));
	                }
	                inf.writezeile("Instance " + i + ": Strategy_Name=" + strategyNameValue + ", Actual class="
	                        + actualClassValue + ", Predicted class=" + predictedClassValue);

	                sumAllFilesProfit = sumAllFilesProfit + Double.valueOf(actualClassValue);

	                // Copy Strategy path
	                String s1path = datadir + "\\_99_dir\\str__all_sq4_endtestfiles\\" + strategyNameValue + ".sqx";
	                String s2path = datadir + "\\_99_dir\\str__selected_sq4_endtestfiles\\" + strategyNameValue
	                        + ".sqx";

	                bestlist.add(s1path, s2path, Double.valueOf(predictedClassValue), Double.valueOf(actualClassValue));

	                inf.writezeile("Instance " + i + ": Strategy_Name=" + strategyNameValue + " Actual class="
	                        + actualClassValue + ", Predicted class=" + predictedClassValue + "\t");

	            }
	            else
	            {
	                System.out.println("Attribute 'Strategy_Name' not found in instance " + i);
	            }
	        }
	        inf.close();

	        int n = newData.numInstances();

	        double correlation = calculateCorrelation(actualValues, predictedValues);
	        System.out.println("Correlation coefficient: " + correlation);

	        if (useminprofitPlusTakeNBestflag == true)
	            bestlist.filterBest("minval+takebest", anzbest);
	        else if (takebestflag == false)
	            bestlist.filterBest("minval", 0);
	        else
	            bestlist.filterBest("takebest", anzbest);

	        bestlist.copyFiles();
	        wekaclassify.setCorrelationVal(correlation);
	        wekaclassify.setCopycounter(bestlist.getCopycounter());
	        wekaclassify.setSelectedFilesProfit(bestlist.getSumSelectedProfits());
	        wekaclassify.setAllFilesProfit(sumAllFilesProfit);
	        forest = null;
	        inewData = null;
	        newData = null;
	        System.gc();
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