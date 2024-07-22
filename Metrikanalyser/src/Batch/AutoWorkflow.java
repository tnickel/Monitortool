package Batch;

import java.util.ArrayList;

import Metriklibs.FileAccessDyn;
import Metriklibs.Metriktabellen;
import Metriklibs.StrategieMengen;
import WekaTools.WekaClassifierElem;
import WekaTools.WekaMatrixCollector;
import WekaTools.WekaResultCollector;
import data.Metrikglobalconf;
import gui.JLibsProgressWin;
import hilfsklasse.Tracer;

public class AutoWorkflow
{
	StrategieMengen strategienselector_glob = null;
	private int maxeportattributes_glob = 0;
	private ArrayList<String> verzl=new ArrayList<String>();
	
	public AutoWorkflow(StrategieMengen strategienselector, String maxExportattributes)
	// maxexportattributes=5000 maximal 5000 attributes sollen exportiert werden
	{
		strategienselector_glob = strategienselector;
	}
	
	public void FullAnalysis()
	{
		ExportAllAttributesWeka();
	}
	
	public void PredictAllWeka(int instanzanzahlcrossvalidate)
	{
		//hier wird durch das Verzeichnis gegangen und die vorhersagen auf den eigenen gelerten daten gemacht.
		WekaResultCollector wres=new WekaResultCollector(Metrikglobalconf.getFilterpath()+"\\Workflowname\\ForestPredictResults.txt");
		
		FileAccessDyn fdirs = new FileAccessDyn();
		fdirs.initFileSystemList(Metrikglobalconf.getFilterpath(), 0);
		
		int anz = fdirs.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String workdir = (Metrikglobalconf.getFilterpath() + "\\" + fdirs.holeFileSystemName()).toLowerCase();
			if(isValidWorkdir(workdir)==false)
				continue;
			
			WekaClassifierElem ws=PredictOneWeka( workdir, wres,i,instanzanzahlcrossvalidate);

			
			
		}
		wres.writeProtokoll();
	}
	
	public void PredictAllMatrixWeka(int instanzanzahlcrossvalidate)
	{
		//hier wird i,j durchgelaufen und jeder mit jeden evaluiert, also klassifiziert und das ganze in der Matrix abgespeichert.
		
		//hier wird durch das Verzeichnis gegangen und die vorhersagen auf den eigenen gelerten daten gemacht.
		WekaMatrixCollector wcollect=new WekaMatrixCollector(Metrikglobalconf.getFilterpath()+"\\Workflowname\\ForestPredictMatrixResults.txt");
		
		FileAccessDyn fdirs = new FileAccessDyn();
		fdirs.initFileSystemList(Metrikglobalconf.getFilterpath(), 0);
		
		//1ter Schritt baue Verzeichnissliste auf
		int anz = fdirs.holeFileAnz();
		//temporär!! 
		
		for (int i = 0; i < anz; i++)
		{
			String workdir = (Metrikglobalconf.getFilterpath() + "\\" + fdirs.holeFileSystemName()).toLowerCase();
			if(isValidWorkdir(workdir)==false)
				continue;
			
			verzl.add(workdir);
			
		}
		
		
		//2ter Schritt, gehe durch die Matrik und klassifiziere alles und speichere
		anz=verzl.size();
		int progcount=0;
		JLibsProgressWin jp=new JLibsProgressWin("Build Correlation matrix",0,anz*anz);
		for(int i=0; i<anz; i++)
			for(int j=0; j<anz; j++)
			{
				progcount++;
				jp.update(progcount);
				//PredictOneWeka( workdir, wres,i,instanzanzahlcrossvalidate);
				String workdir1=verzl.get(i);
				String workdir2=verzl.get(j);
				try
				{
					WekaClassifierElem wc=WekaTools.WekaLearn.classifyNewData(workdir1+"\\randomforest.model", workdir2+"\\exported_for_weka.csv",instanzanzahlcrossvalidate);
					wcollect.AddResult(i, j, wc);
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		jp.end();
		//3ter Schritt gib die Ergebniss aus.
		wcollect.WriteProtokoll();
	}
	public void PredictLastPeriod(int instanzanzahlcrossvalidate)
	{
		//Hier wollen wir nicht die ganze Matrik lernen sondern mit den daten der vorherigen Periode die aktuelle vorhersagen
		//Dies ist ja auch der eigentliche Anwendungsfall. Mit der Matrix haben wir uns immer nur so einen Überblick verschafft.
		WekaMatrixCollector wcollect=new WekaMatrixCollector(Metrikglobalconf.getFilterpath()+"\\Workflowname\\ForestPredictPastPeriod.txt");
		
		FileAccessDyn fdirs = new FileAccessDyn();
		fdirs.initFileSystemList(Metrikglobalconf.getFilterpath(), 0);
		
		//1ter Schritt baue Verzeichnissliste auf
		int anz = fdirs.holeFileAnz();
		//temporär!! 
		
		for (int i = 0; i < anz; i++)
		{
			String workdir = (Metrikglobalconf.getFilterpath() + "\\" + fdirs.holeFileSystemName()).toLowerCase();
			if(isValidWorkdir(workdir)==false)
				continue;
			
			verzl.add(workdir);
			}
		
		
		//2ter Schritt, gehe durch die Matrik und klassifiziere alles und speichere
		anz=verzl.size();
		int progcount=0;
		JLibsProgressWin jp=new JLibsProgressWin("Build Correlation matrix",0,anz);
		for(int i=anz-1; i>0; i--)
		
			{
				progcount++;
				jp.update(progcount);
				
				String modeldir=verzl.get(i);
				String datadir=verzl.get(i-1);
				try
				{
					Tracer.WriteTrace(20, "predict period<"+datadir+"> with learned data from<"+modeldir+">");
					WekaClassifierElem wc=WekaTools.WekaLearn.classifyNewData(modeldir+"\\randomforest.model", datadir+"\\exported_for_weka.csv",instanzanzahlcrossvalidate);
					wcollect.AddResult(i, 0, wc);
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		jp.end();
		//3ter Schritt gib die Ergebniss aus.
		wcollect.WriteProtokoll();
	}
	private WekaClassifierElem PredictOneWeka(String workdir,WekaResultCollector wres,int index,int instanzanzahlcrossvalidate)
	{
		
		
		try
		{
			WekaClassifierElem ws=WekaTools.WekaLearn.classifyNewData(workdir+"\\randomforest.model", workdir+"\\exported_for_weka.csv",instanzanzahlcrossvalidate);
			return ws;
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void LearnAllWekaExported(int instanzanzahlcrossvalidate, int anztrees)
	{
		WekaResultCollector wres=new WekaResultCollector(Metrikglobalconf.getFilterpath()+"\\Workflowname\\ForestLearnerResults.txt");
		
		FileAccessDyn fdirs = new FileAccessDyn();
		fdirs.initFileSystemList(Metrikglobalconf.getFilterpath(), 0);
		
		int anz = fdirs.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String workdir = (Metrikglobalconf.getFilterpath() + "\\" + fdirs.holeFileSystemName()).toLowerCase();
			if(isValidWorkdir(workdir)==false)
				continue;
			
			LearnOneWekaExported( workdir,instanzanzahlcrossvalidate,wres, i,anztrees);
		}
		wres.writeProtokoll();
	}
	
	private void LearnOneWekaExported(String workdir,int instanzanzahlcrossvalidate,WekaResultCollector wres,int index,int anztrees)
	{
		String randomForestFile=workdir+"\\exported_for_weka.csv";
		try
		{
			WekaTools.WekaLearn.loadAndTrainModel(randomForestFile, instanzanzahlcrossvalidate,wres,index,anztrees);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void ExportAllAttributesWeka()
	{
		// wir müssen hier durch alle Verzeichnisse laufen und dann jedes mal
		// exportieren.
		// holt die liste der Verzeichnisse
		FileAccessDyn fdirs = new FileAccessDyn();
		fdirs.initFileSystemList(Metrikglobalconf.getFilterpath(), 0);
		
		
		int anz = fdirs.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String workdir = (Metrikglobalconf.getFilterpath() + "\\" + fdirs.holeFileSystemName()).toLowerCase();
			String workfile=workdir+"\\exported_for_weka.csv";
			
			if(isValidWorkdir(workdir)==false)
				continue;
			
			ExportOneAttribute(maxeportattributes_glob, workfile,workdir);
		}
		Tracer.WriteTrace(20, "All attribute exported");
	}
	
	public void ExportOneAttribute(int maxeport, String attribfile,String workdir)
	{
		// temprootdir ist z.B.
		// C:\forex\Metrikanalyser\AR\Q105 EURUSD H1 v1.41 org 11-15\Q105 EURUSD H1
		// v1.41 org 11-15_+00000
		
		Metriktabellen met = new Metriktabellen();
		met.readAllTabellen(strategienselector_glob, workdir);
		// der letzte parameter ist false da wir nicht binar exportieren wollen
		// temprootdir ist das aktuelle verzeichniss wo wir arbeiten
		met.exportAllAttributesForWeka(attribfile, maxeport, false,workdir);
	}

	private boolean isValidWorkdir(String dir)
	{
		if((dir.endsWith("endtest"))||(dir.endsWith("long"))||(dir.endsWith("workflowname")))
			return false;
		return true;
		
	}
}
