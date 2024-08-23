package WekaTools;

import java.io.File;

import weka.classifiers.misc.InputMappedClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.CSVLoader;

public class WekaModelTester
{
	  public static void main(String[] args) throws Exception {
	        // Pfade zu den Dateien
	        String modelPath = "C:\\forex\\Metrikanalyser\\AR\\Q105 EURUSD H1 v1.41 org 11-15\\Q105 EURUSD H1 v1.41 org 11-15_+00000/randomForest.model";
	        String dataPath = "C:\\forex\\Metrikanalyser\\AR\\Q105 EURUSD H1 v1.41 org 11-15\\Q105 EURUSD H1 v1.41 org 11-15_--00252/exported_for_weka252.csv";

	        // Lade das Modell
	        //System.out.println("Loading model from: " + modelPath);
	        RandomForest forest = (RandomForest) SerializationHelper.read(modelPath);

	        // Wrap the RandomForest in an InputMappedClassifier
	        InputMappedClassifier inputMappedClassifier = new InputMappedClassifier();
	        inputMappedClassifier.setClassifier(forest);

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

	        // Debug-Ausgabe für Klassenattribut
	        System.out.println("Class attribute for new data set to index: " + newData.classIndex());
	        System.out.println("Class attribute name: " + newData.classAttribute().name());

	        // Map the input data to the classifier
	        inputMappedClassifier.buildClassifier(newData);

	        // Klassifiziere die neuen Daten
	        for (int i = 0; i < newData.numInstances(); i++) {
	            double predictedValue = inputMappedClassifier.classifyInstance(newData.instance(i));
	            //System.out.println("Instance " + i + ": Predicted class=" + predictedValue);
	        }
	    }
	
}
