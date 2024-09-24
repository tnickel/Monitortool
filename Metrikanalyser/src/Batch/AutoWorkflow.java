package Batch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Metriklibs.Attribliste;
import Metriklibs.FileAccessDyn;
import Metriklibs.Metriktabellen;
import Metriklibs.StrategieMengen;
import WekaTools.WekaClassifierElem;
import WekaTools.WekaMatrixCollector;
import WekaTools.WekaResultCollector;
import calcPack.CalcCorrelationAllWorkflows;
import csvtools.CSVProcessor;
import data.Metrikglobalconf;
import gui.HeatMapLine;
import gui.HeatMapMatrix;
import gui.JLibsProgressWin;
import gui.PeriodGewinnchart;
import hiflsklasse.FileAccess;
import hilfsklasse.Tracer;
import com.opencsv.exceptions.CsvValidationException;

public class AutoWorkflow
{
	StrategieMengen strategienselector_glob = null;
	private int maxeportattributes_glob = 0;
	private ArrayList<String> verzl = new ArrayList<String>();
	
	public AutoWorkflow(StrategieMengen strategienselector, String maxExportattributes)
	// maxexportattributes=5000 maximal 5000 attributes sollen exportiert werden
	{
		strategienselector_glob = strategienselector;
	}
	
	public void FullAnalysis(boolean usebad, int maxstrategies,String endtestattribname)
	{
		ExportAllAttributesWeka(usebad, maxstrategies,endtestattribname);
	}
	
	static public void DeleteAllEndtestfiles()
	{
		FileAccessDyn fdirs = new FileAccessDyn();
		fdirs.initFileSystemList(Metrikglobalconf.getFilterpath(), 0);
		
		int anz = fdirs.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String workdir = (Metrikglobalconf.getFilterpath() + "\\" + fdirs.holeFileSystemName()).toLowerCase()
					+ "\\_99_dir\\str__all_sq4_endtestfiles";
			
			FileAccess.deleteDirectoryContentPostfix(new File(workdir), ".sqx");
			
		}
		
	}
	
	public void PredictAllWeka(int instanzanzahlcrossvalidate, boolean normflag, String wekaattribfilename)
	{
		// hier wird durch das Verzeichnis gegangen und die vorhersagen auf den eigenen
		// gelerten daten gemacht.
		
		WekaResultCollector wres = new WekaResultCollector(
				Metrikglobalconf.getFilterpath() + "\\Workflowname\\ForestPredictResults.txt");
		
		FileAccessDyn fdirs = new FileAccessDyn();
		fdirs.initFileSystemList(Metrikglobalconf.getFilterpath(), 0);
		
		int anz = fdirs.holeFileAnz();
		
		int progcount = 0;
		JLibsProgressWin jp = new JLibsProgressWin("Workflow predict", 0, anz);
		
		for (int i = 0; i < anz; i++)
		{
			jp.update(i);
			String workdir = (Metrikglobalconf.getFilterpath() + "\\" + fdirs.holeFileSystemName()).toLowerCase();
			if (isValidWorkdir(workdir) == false)
				continue;
			
			WekaClassifierElem ws = PredictOneWeka(workdir, wres, i, instanzanzahlcrossvalidate, normflag,
					wekaattribfilename);
			if(ws==null)
			{
				Tracer.WriteTrace(20, "E: no prediction possible <"+workdir+">");
				continue;
			}
		}
		jp.end();
		wres.writeProtokoll();
	}
	
	public void PredictAllMatrixWeka(int instanzanzahlcrossvalidate, boolean normflag, String wekaattribfilename)
	{
		// hier wird i,j durchgelaufen und jeder mit jeden evaluiert, also klassifiziert
		// und das ganze in der Matrix abgespeichert.
		
		// hier wird durch das Verzeichnis gegangen und die vorhersagen auf den eigenen
		// gelerten daten gemacht.
		String outfile = Metrikglobalconf.getFilterpath() + "\\Workflowname\\ForestPredictMatrixResults.txt";
		WekaMatrixCollector wcollect = new WekaMatrixCollector(outfile);
		
		FileAccessDyn fdirs = new FileAccessDyn();
		fdirs.initFileSystemList(Metrikglobalconf.getFilterpath(), 0);
		
		// 1ter Schritt baue Verzeichnissliste auf
		int anz = fdirs.holeFileAnz();
		// temporär!!
		
		for (int i = 0; i < anz; i++)
		{
			String workdir = (Metrikglobalconf.getFilterpath() + "\\" + fdirs.holeFileSystemName()).toLowerCase();
			if (isValidWorkdir(workdir) == false)
				continue;
			
			verzl.add(workdir);
			
		}
		String csvfileDest = "";
		
		// 2ter Schritt, gehe durch die Matrik und klassifiziere alles und speichere
		anz = verzl.size();
		int progcount = 0;
		JLibsProgressWin jp = new JLibsProgressWin("Build Correlation matrix", 0, anz * anz);
		for (int i = 0; i < anz; i++)
			for (int j = 0; j < anz; j++)
			{
				progcount++;
				jp.update(progcount);
				// PredictOneWeka( workdir, wres,i,instanzanzahlcrossvalidate);
				String workdirSource = verzl.get(i);
				String workdirDest = verzl.get(j);
				try
				{
					
					csvfileDest = workdirDest + "\\"+wekaattribfilename;
					
					WekaClassifierElem wc = WekaTools.WekaLearn.classifyNewData(workdirSource + "\\randomforest.model",
							csvfileDest, instanzanzahlcrossvalidate, normflag, workdirDest);
					if(wc==null)
						continue;
					
					wcollect.AddResult(i, j, wc);
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		jp.end();
		// 3ter Schritt gib die Ergebniss aus.
		Double averageval = wcollect.WriteProtokoll1();
		HeatMapMatrix hm = new HeatMapMatrix("Heatmap Matrix average value=" + averageval, outfile);
		hm.start(outfile);
	}
	
	public void PredictLastPeriod(int instanzanzahlcrossvalidate, boolean reducedflag, boolean normflag, String workdir,
			String attribfilename)
	{
		// Hier wollen wir nicht die ganze Matrik lernen sondern mit den daten der
		// vorherigen Periode die aktuelle vorhersagen
		// Dies ist ja auch der eigentliche Anwendungsfall. Mit der Matrix haben wir uns
		// immer nur so einen Überblick verschafft.
		// if normflag==1 dann wird normalisiert
		WekaMatrixCollector wcollect = new WekaMatrixCollector(
				Metrikglobalconf.getFilterpath() + "\\Workflowname\\ForestPredictPastPeriod.txt");
		
		FileAccessDyn fdirs = new FileAccessDyn();
		fdirs.initFileSystemList(Metrikglobalconf.getFilterpath(), 0);
		
		// 1ter Schritt baue Verzeichnissliste auf
		int anz = fdirs.holeFileAnz();
		// temporär!!
		
		for (int i = 0; i < anz; i++)
		{
			String workdir2 = (Metrikglobalconf.getFilterpath() + "\\" + fdirs.holeFileSystemName()).toLowerCase();
			if (isValidWorkdir(workdir2) == false)
				continue;
			
			verzl.add(workdir2);
		}
		
		// 2ter Schritt, gehe durch die Matrik und klassifiziere alles und speichere
		anz = verzl.size();
		int progcount = 0;
		JLibsProgressWin jp = new JLibsProgressWin("Build Correlation matrix", 0, anz);
		for (int i = anz - 1; i > 0; i--)
		
		{
			progcount++;
			jp.update(progcount);
			
			String modeldir = verzl.get(i);
			String datadir = verzl.get(i - 1);
			String csvfile = datadir + "\\"+attribfilename;
		
			try
			{
				Tracer.WriteTrace(20, "____________________________________________________________");
				Tracer.WriteTrace(20, "predict period<" + datadir + "> with learned data from<" + modeldir + ">");
				WekaClassifierElem wc = WekaTools.WekaLearn.classifyNewData(modeldir + "\\randomforest.model", csvfile,
						instanzanzahlcrossvalidate, normflag, workdir);
				if (wc==null)
				{
					Tracer.WriteTrace(20, "E: no classification possible <"+modeldir+">");
					continue;
				}
				wcollect.AddResult(i, 0, wc);
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		jp.end();
		// 3ter Schritt gib die Ergebniss aus.
		wcollect.WriteProtokoll1();
	}
	
	public double PredictLastPeriodCopyStrategies(int instanzanzahlcrossvalidate, double minprofit, boolean copyflag,
			boolean normflag, boolean takebestflag, int anzbest, String attribfilename, boolean useminprofitPlusTakeNBestflag)
	{
		// Hier wollen wir nicht die ganze Matrik lernen sondern mit den daten der
		// vorherigen Periode die aktuelle vorhersagen
		// Dies ist ja auch der eigentliche Anwendungsfall. Mit der Matrix haben wir uns
		// immer nur so einen Überblick verschafft.
		// takenbestflag= falls das gesetzt ist werden die n besten strategien genommen
		// die minprofit überschritten hatten
		// anzbest= ist die anzahl der strategien die wir kopieren wollen.
		// button3useminprofittakenbest= if this flag is set then use both, takeminprofit + take n best
		String outfile_str = Metrikglobalconf.getFilterpath() + "\\Workflowname\\ForestPredictPastPeriod2.txt";
		String profitfile_str = Metrikglobalconf.getFilterpath() + "\\Workflowname\\ForestPredictPastPeriodProfit.txt";
		String profitfileVal_str = Metrikglobalconf.getFilterpath() + "\\Workflowname\\ForestPredictPastPeriodVal.txt";
		WekaMatrixCollector wcollect = new WekaMatrixCollector(outfile_str);
		
		FileAccessDyn fdirs = new FileAccessDyn();
		fdirs.initFileSystemList(Metrikglobalconf.getFilterpath(), 0);
		
		// 1ter Schritt baue Verzeichnissliste auf
		int anz = fdirs.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String workdir = (Metrikglobalconf.getFilterpath() + "\\" + fdirs.holeFileSystemName()).toLowerCase();
			if (isValidWorkdir(workdir) == false)
				continue;
			
			verzl.add(workdir);
		}
		
		// 2ter Schritt, gehe durch die Matrix und klassifiziere alles und speichere
		anz = verzl.size();
		int progcount = 0;
		JLibsProgressWin jp = new JLibsProgressWin("Build Correlation with previous data", 0, anz);
		for (int i = anz - 1; i > 0; i--)
		
		{
			progcount++;
			jp.update(progcount);
			
			String modeldir = verzl.get(i);
			String datadir = verzl.get(i - 1);
			String csvfile = datadir + "\\exported_for_weka.csv";
			
			try
			{
				Tracer.WriteTrace(20, "_________________________________________________________________");
				Tracer.WriteTrace(20, "predict period<" + datadir + "> with learned data from<" + modeldir + ">");
				WekaClassifierElem wc = WekaTools.WekaLearn.classifyNewDataCopyStrategies(datadir,
						modeldir + "\\randomforest.model", csvfile, instanzanzahlcrossvalidate, minprofit, copyflag,
						normflag, takebestflag, anzbest, attribfilename,useminprofitPlusTakeNBestflag);
				
				if(wc==null)
				{
					Tracer.WriteTrace(20, "E: no classification possible <"+modeldir+">");
					continue;
				}
				
				wcollect.AddResult(i, 0, wc);
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		jp.end();
		// 3ter Schritt gib die Ergebniss aus.
		// es werden hier zwei unterschiedliche protokolle geschrieben
		double val = wcollect.WriteProtokoll1();
		wcollect.WriteProtokoll2Profit(profitfile_str);
		wcollect.WriteProtokoll3Values(profitfileVal_str);
		PeriodGewinnchart pergew = new PeriodGewinnchart();
		pergew.createChartPanel(profitfileVal_str);
		pergew.show(profitfileVal_str);
		
		int copycount = wcollect.GetCopyCounterSum();
		
		HeatMapLine heatMapLine = new HeatMapLine("Heatmap Prediction averagevalue=" + val + "count=" + copycount,
				outfile_str);
		heatMapLine.start(outfile_str);
		return val;
	}
	
	private WekaClassifierElem PredictOneWeka(String workdir, WekaResultCollector wres, int index,
			int instanzanzahlcrossvalidate, boolean normflag, String wekaattribfilename)
	{
		String csvfile = "";
		
		csvfile = workdir + "\\" + wekaattribfilename;
		
		try
		{
			WekaClassifierElem ws = WekaTools.WekaLearn.classifyNewData(workdir + "\\randomforest.model", csvfile,
					instanzanzahlcrossvalidate, normflag, workdir);
			return ws;
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public double LearnAllWekaExported(int instanzanzahlcrossvalidate, int anztrees, boolean usenorm,
			String wekaattribfilename)
	{
		WekaResultCollector wres = new WekaResultCollector(
				Metrikglobalconf.getFilterpath() + "\\Workflowname\\ForestLearnerResults.txt");
		
		FileAccessDyn fdirs = new FileAccessDyn();
		fdirs.initFileSystemList(Metrikglobalconf.getFilterpath(), 0);
		
		int anz = fdirs.holeFileAnz();
		
		int progcount = 0;
		JLibsProgressWin jp = new JLibsProgressWin("Learn Workflow", 0, anz);
		
		for (int i = 0; i < anz; i++)
		{
			jp.update(i);
			String workdir = (Metrikglobalconf.getFilterpath() + "\\" + fdirs.holeFileSystemName()).toLowerCase();
			if (isValidWorkdir(workdir) == false)
				continue;
			
			if(LearnOneWekaExported(workdir, instanzanzahlcrossvalidate, wres, i, anztrees, usenorm, wekaattribfilename)==false)
				Tracer.WriteTrace(10, "E:Weka learner problem <"+wekaattribfilename+">");
		}
		jp.end();
		wres.writeProtokoll();
		return (wres.getAvgsum());
	}
	
	private boolean LearnOneWekaExported(String workdir, int instanzanzahlcrossvalidate, WekaResultCollector wres,
			int index, int anztrees, boolean usenorm, String wekaattribfilename)
	{
		String csvfile = "";
		String randomForestmodelFile = workdir + "\\randomForest.model";
		
		csvfile = workdir + "\\" + wekaattribfilename;
		try
		{
			
			if(WekaTools.WekaLearn.loadAndTrainModel(csvfile, randomForestmodelFile, instanzanzahlcrossvalidate, wres,
					index, anztrees, usenorm)==false)
				return false;
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Tracer.WriteTrace(10, "E: Weka error<"+e.getMessage()+">");
			
		}
		return true;
	}
	
	public void ExportAllAttributesWeka(boolean usebadendtest, int maxstrategies, String endtestattribname)
	{
		// wir müssen hier durch alle Verzeichnisse laufen und dann jedes mal
		// exportieren.
		// holt die liste der Verzeichnisse
		//subset=ist ein string der nur ein subset definiert.
		FileAccessDyn fdirs = new FileAccessDyn();
		fdirs.initFileSystemList(Metrikglobalconf.getFilterpath(), 0);
		
		int anz = fdirs.holeFileAnz();
		// gehe durch alle workflows
		
		int progcount = 0;
		JLibsProgressWin jp = new JLibsProgressWin("Generate all Metrics for Weka", 0, anz);
		for (int i = 0; i < anz; i++)
		{
			String workdir = (Metrikglobalconf.getFilterpath() + "\\" + fdirs.holeFileSystemName()).toLowerCase();
			String workfile = workdir + "\\exported_for_weka.csv";
			jp.update(i);
			if (isValidWorkdir(workdir) == false)
				continue;
			
			ExportOneWorkflow(maxstrategies, workfile, workdir, usebadendtest,endtestattribname);
		}
		jp.end();
		Tracer.WriteTrace(20, "All workflows exported");
	}
	
	public void FilterRelevantMetrics(String endtestattribname, String corealgotype, boolean showoutputflag)
	{
		// erst mal die Megamatrix aufbauen
		CalcCorrelationAllWorkflows cal = new CalcCorrelationAllWorkflows();
		cal.CalcCorrelationAllWorkflows(endtestattribname, corealgotype, showoutputflag);
		Attribliste attribliste = cal.getGoodAttribliste();
		
		// dann die attribute speichern
		FileAccessDyn fdirs = new FileAccessDyn();
		fdirs.initFileSystemList(Metrikglobalconf.getFilterpath(), 0);
		
		int anz = fdirs.holeFileAnz();
		
		int progcount = 0;
		JLibsProgressWin jp = new JLibsProgressWin("Filter relevant Metrics", 0, anz);
		// gehe durch alle workflows
		for (int i = 0; i < anz; i++)
		{
			String workdir = (Metrikglobalconf.getFilterpath() + "\\" + fdirs.holeFileSystemName()).toLowerCase();
			String workfile = workdir + "\\exported_for_weka.csv";
			String outfile = workdir + "\\exported_for_weka_reduced.csv";
			jp.update(i);
			if (isValidWorkdir(workdir) == false)
				continue;
			
			try
			{
				CSVProcessor processor = new CSVProcessor(workfile);
				
				List<String> attributesToKeep = attribliste.getAttribliste();
				processor.filterAttributes(attributesToKeep);
				
				processor.saveToFile(outfile);
				Tracer.WriteTrace(20, "I: Convert <" + workfile + "> to <" + outfile + ">");
			} catch (IOException | CsvValidationException e)
			{
				e.printStackTrace();
			}
		}
		jp.end();
	}
	
	public void ExportOneWorkflow(int maxeport, String attribfile, String workdir, boolean usebadendtest,String endtestattribname)
	{
		// temprootdir ist z.B.
		// C:\forex\Metrikanalyser\AR\Q105 EURUSD H1 v1.41 org 11-15\Q105 EURUSD H1
		// v1.41 org 11-15_+00000
		
		Tracer.WriteTrace(20, "I:WekaExport:generate file<" + attribfile + "> ");
		Metriktabellen met = new Metriktabellen();
		// alle tabelle auf einmal einlesen
		met.readAllTabellen(strategienselector_glob, workdir);
		// der letzte parameter ist false da wir nicht binar exportieren wollen
		// temprootdir ist das aktuelle verzeichniss wo wir arbeiten
		met.exportAllAttributesForWeka(attribfile, maxeport, workdir, usebadendtest,endtestattribname);
		met=null;
		System.gc();
	}
	
	private boolean isValidWorkdir(String dir)
	{
		if ((dir.endsWith("endtest")) || (dir.endsWith("long")) || (dir.endsWith("workflowname")))
			return false;
		return true;
		
	}
}
