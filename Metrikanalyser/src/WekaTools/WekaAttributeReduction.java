package WekaTools;

import java.io.File;

import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.ASSearch;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.BestFirst;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GainRatioAttributeEval;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;

	public class WekaAttributeReduction {

	    // Valid combinations of evaluators and search methods
	    private static final String CFS_BESTFIRST = "cfssubseteval_bestfirst";
	    private static final String CFS_GREEDYSTEPWISE = "cfssubseteval_greedystepwise";
	    private static final String INFOGAIN_RANKER = "infogain_ranker";
	    private static final String GAINRATIO_RANKER = "gainratio_ranker";

	    public Instances reduceAttributes(String inputFilePath, String method) throws Exception {
	        // Laden des Datensatzes (CSV-Datei)
	        CSVLoader loader = new CSVLoader();
	        loader.setSource(new File(inputFilePath));
	        Instances data = loader.getDataSet();

	        // Sicherstellen, dass der Datensatz ein Klassenattribut hat
	        if (data.classIndex() == -1) {
	            data.setClassIndex(data.numAttributes() - 1);  // Setzt das letzte Attribut als Klassenattribut
	        }

	        // Validieren der Kombination und Aufrufen der entsprechenden Methode
	        ASEvaluation evaluator = null;
	        ASSearch search = null;

	        switch (method.toLowerCase()) {
	            case CFS_BESTFIRST:
	                evaluator = new CfsSubsetEval();
	                search = new BestFirst();
	                break;
	            case CFS_GREEDYSTEPWISE:
	                evaluator = new CfsSubsetEval();
	                search = new weka.attributeSelection.GreedyStepwise();
	                break;

	            case INFOGAIN_RANKER:
	                evaluator = new InfoGainAttributeEval();
	                search = new Ranker();
	                break;
	            case GAINRATIO_RANKER:
	                evaluator = new GainRatioAttributeEval();
	                search = new Ranker();
	                break;
	            default:
	                throw new IllegalArgumentException("Unknown or unsupported method: " + method);
	        }

	        // Erstellen des AttributeSelection Objekts
	        AttributeSelection attributeSelection = new AttributeSelection();
	        attributeSelection.setEvaluator(evaluator);
	        attributeSelection.setSearch(search);

	        // Anwenden der Attributauswahl auf den Datensatz
	        attributeSelection.SelectAttributes(data);
	        Instances reducedData = attributeSelection.reduceDimensionality(data);

	        // Sicherstellen, dass das erste Attribut nicht entfernt wird
	        reducedData = ensureFirstAttributeRetained(data, reducedData);

	        return reducedData;
	    }

	    private Instances ensureFirstAttributeRetained(Instances originalData, Instances reducedData) {
	        // Überprüfen, ob das erste Attribut entfernt wurde
	        if (!reducedData.attribute(0).name().equals(originalData.attribute(0).name())) {
	            // Erstellen einer neuen Instanz mit dem ersten Attribut
	            Instances newData = new Instances(reducedData);
	            newData.insertAttributeAt(originalData.attribute(0), 0);

	            // Kopieren der Werte des ersten Attributs zurück
	            for (int i = 0; i < newData.numInstances(); i++) {
	                newData.instance(i).setValue(0, originalData.instance(i).value(0));
	            }

	            return newData;
	        }
	        return reducedData;  // Falls das erste Attribut nicht entfernt wurde, einfach das reduzierte Dataset zurückgeben
	    }

	    public void saveReducedData(Instances reducedData, String outputFilePath) throws Exception {
	        // Speichern des reduzierten Datensatzes als CSV
	        CSVSaver saver = new CSVSaver();
	        saver.setInstances(reducedData);  // Setzen des reduzierten Datensatzes
	        saver.setFile(new File(outputFilePath));
	        saver.writeBatch();  // Speichern der Datei

	        System.out.println("Reduced dataset saved as CSV.");
	    }
	}
