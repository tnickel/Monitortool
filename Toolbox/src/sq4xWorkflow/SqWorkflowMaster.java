package sq4xWorkflow;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;

import org.eclipse.swt.widgets.Display;

import FileTools.Filefunkt;
import FileTools.SqZipper;
import data.Toolboxconf;
import gui.Viewer;
import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;
import work.JToolboxProgressWin;

public class SqWorkflowMaster extends Sq
// this is the masterclass for all sq4x handling
// this class read all Project workflows and work with it
// sourcedir: directory of the sourcedir of all workflows,this file will never
// modified
// destdir: this the targetdir of the modified files
// configfile: In this file are the actions what the workflowclass has to do.
{
	private String masterfile_g, sqrootdir_g, outputname_g, resultdir_g, shareddrive_g, backupdrive_g;
	private int backcount_g, futurecount_g, stepvalue_g;
	
	public void setMasterfile(String masterfile_g)
	{
		// the masterfile is the sourcefile which contains the workflow
		// The workflow generator works with this file
		this.masterfile_g = masterfile_g;
	}
	
	public void setSqrootdir(String sqrootdir_g)
	{
		this.sqrootdir_g = sqrootdir_g;
	}
	
	public String getSqRootdir()
	{
		return this.sqrootdir_g;
	}
	
	public void setResultdir(String resultdir_g)
	{
		// the resultdir is the directory where the calculated workflows will be stored
		this.resultdir_g = resultdir_g;
	}
	
	public String getResultdir()
	{
		return this.resultdir_g;
	}
	
	public void setSharedDrive(String dir)
	{
		// the shared drive directory is the directory where the backups will be shared
		this.shareddrive_g = dir;
	}
	
	public String getSharedDrive()
	{
		return this.shareddrive_g;
	}
	
	public void setBackupDrive(String dir)
	{
		// in this directory, the backups will be copied
		this.backupdrive_g = dir;
	}
	
	public String getBackupDrive()
	{
		return this.backupdrive_g;
	}
	
	public void setBackcount(String days)
	{
		// negative for the the days in the past
		this.backcount_g = Integer.valueOf(days);
	}
	
	public void setFuturecount(String days)
	{
		// positive for days in the future
		this.futurecount_g = Integer.valueOf(days);
	}
	
	public void setStepvalue(String steps)
	{
		// this is the step for the day-stepping
		this.stepvalue_g = Integer.valueOf(steps);
	}
	
	public void setOutputname(String outputname)
	{
		// outputname for the workflow
		this.outputname_g = outputname;
	}
	
	public boolean checkEmptyShareddrive()
	{
		// true: if directory don�t exists or is empty
		// false: if directory contains files or other contents
		
		// check if googledir is empty
		File sdf = new File(shareddrive_g + "\\" + this.outputname_g + "\\portfolios");
		// if directory not exists-> empty = true
		if (sdf.isDirectory() == false)
			return true;
		
		// if directory is empty=>true
		if (sdf.listFiles() == null)
			return true;
		
		return false;
		
	}
	
	public int getAnzProjectfiles()
	{
		if (sqrootdir_g == null)
			return -1;
		
		File dir = new File(sqrootdir_g + "\\user\\projects");
		
		String[] dirl = dir.list();
		
		if ((dirl == null) || (dirl.length < 1))
			return -1;
		int fileanz = dirl.length;
		return fileanz;
	}
	
	public void deleteProjectfiles()
	{
		// check if rootdir is correct
		File ll = new File(sqrootdir_g + "\\StrategyQuantX.exe");
		if (ll.exists() == false)
			Tracer.WriteTrace(10, "E:Bad Strategyquant Installation Rootpath= <" + sqrootdir_g + ">");
		
		if (sqrootdir_g.length() < 5)
			Tracer.WriteTrace(10, "E:Bad Strategyquant Installation Rootpath, rootpathlength<5= <" + sqrootdir_g + ">");
		
		File dir = new File(sqrootdir_g + "\\user\\projects");
		Filefunkt.deleteSubDir(dir, sqrootdir_g);
	}
	
	public void genWorkflow(String EndtestDatabaseName)
	{
		// This is the masterfunction for the workflow generation
		// the results will be stored first in the tmp-directory
		
		// deleteProjectfiles();
		
		File tmpdir_f = new File("c:\\tmp");
		if (tmpdir_f.exists() == false)
			if (tmpdir_f.mkdir() == false)
				Tracer.WriteTrace(10, "E:error can�t generate c:\\tmp - directory --> stop");
			
		tmpdir_f = new File("c:\\tmp\\sq");
		if (tmpdir_f.exists() == false)
			if (tmpdir_f.mkdir() == false)
				Tracer.WriteTrace(10, "E:error can�t generate c:\\tmp - directory --> stop");
			
		// some initialisation progressslider and dfformat
		
		// unzipp all
		SqZipper sqzip = new SqZipper();
		try
		{
			sqzip.unzip(masterfile_g, "c:\\tmp\\sq");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// start new projectfile
		Tracer.WriteTrace(20, "I:read masterfile<" + masterfile_g + ">");
		
		if (Toolboxconf.getPropAttribute("shiftdays").equals("true"))
			LoopDeltaD(sqzip, EndtestDatabaseName, outputname_g);
		else
			LoopDeltaDFile(sqzip, EndtestDatabaseName, outputname_g);
		
	}
	
	private void LoopDeltaD(SqZipper sqzip, String EndtestnameFromDatabase, String projectdir)
	{
		// projectdir=Q109 EURUSD M1 weka12cprod
		// Endtestnamefromdatabase=Diese Datenbasis wollen wir auswerten, hier wollen
		// wir das startdatum und das Endatum wissen.
		int anzsteps = Math.abs(futurecount_g) + Math.abs(backcount_g) + 1;
		JToolboxProgressWin jp = new JToolboxProgressWin("calc Workflows", 0, (int) anzsteps);
		
		int offset = 0;
		SqGenerateWorkflowMain psq = new SqGenerateWorkflowMain("c:\\tmp\\sq");
		// generate many workflow, to this in the loop
		for (int i = -backcount_g, loopcount = 0; i <= futurecount_g; i++, loopcount++)
		{
			jp.update(loopcount);
			
			// modify project
			offset = i * stepvalue_g;
			String workflowname = calcWorkflowname(i, offset);
			
			String enddate = psq.modifyProject(offset, EndtestnameFromDatabase, workflowname, projectdir);
			// save projekt
			psq.saveToTmpDir();
			// das ganze muss wieder gezipped werden
			
			try
			{
				sqzip.zip("c:\\tmp\\sq", "c:\\tmp\\workflow_tmp.zip");
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// copy to destination
			psq.copyToSq(sqrootdir_g, "c:\\tmp\\workflow_tmp.zip", workflowname, enddate);
			
			// logfiles nicht l�schen da hier drin wichtig informationen sind
			// psq.cleanLogfiles(sqrootdir_g, workflowname);
			psq.Reset();
			Tracer.WriteTrace(20, "I:generated workflow <" + workflowname + ">");
		}
		jp.end();
		
	}
	
	private void LoopDeltaDFile(SqZipper sqzip, String EndtestDatabaseName, String projectdir)
	{
		// Das DeltaDfile ist ein configfile wo die Shifts vorgegeben werden
		Inf inf = new Inf();
		inf.setFilename(Toolboxconf.getPropAttribute("deltadaysfile"));
		String line = inf.readZeile();
		inf.close();
		
		String[] strArr = line.split(",");
		
		int anzsteps = strArr.length;
		JToolboxProgressWin jp = new JToolboxProgressWin("calc Workflows", 0, (int) anzsteps);
		
		int offset = 0;
		SqGenerateWorkflowMain psq = new SqGenerateWorkflowMain("c:\\tmp\\sq");
		// generate many workflow, to this in the loop
		for (int i = 0, loopcount = 0; i < anzsteps; i++, loopcount++)
		{
			jp.update(loopcount);
			
			// modify project
			offset = Integer.valueOf(strArr[i]);
			String workflowname = calcWorkflownameFilename(i, offset);
			String foundenddate = psq.modifyProject(offset, EndtestDatabaseName, workflowname, projectdir);
			// save projekt
			psq.saveToTmpDir();
			// das ganze muss wieder gezipped werden
			
			try
			{
				sqzip.zip("c:\\tmp\\sq", "c:\\tmp\\workflow_tmp.zip");
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// copy to destination
			psq.copyToSq(sqrootdir_g, "c:\\tmp\\workflow_tmp.zip", workflowname, foundenddate);
			psq.cleanLogfiles(sqrootdir_g, workflowname);
			psq.Reset();
			Tracer.WriteTrace(20, "I:generated workflow <" + workflowname + ">");
		}
		jp.end();
	}
	
	public void collectResults(Boolean copygoogledriveflag, Boolean copybackupflag, Boolean showresultsflag,
			String databankname, String cpart, int normation, boolean fullreportflag, String outputname,
			int maxstepsback)
	{
		// normation: 0-no normation
		// normation= n then calculate res=(sumwin/anz-strategies) * n
		// Die Resultsfiles werden aus den SQ workflowverzeichnissen geholt
		// die normalen results in das erste Zielverzeichniss vom SQ kopieren
		// maxdaysback: dient dazu die analyse nur bis zu einem bestimmten punk in die
		// vergangenheit laufen zu lassen
		
		// Step1: wir sammeln alles aus dem workflows und sammeln die daten in ein
		// bestimmtes verzeichniss vom sq
		SqCollectStoreResultsMain sr = new SqCollectStoreResultsMain();
		// resultdir=verzeichniss wo das hin soll
		sr.setResultdir(resultdir_g);
		// sqrootdir ist das rootdir des sqs den wir verwenden
		sr.setSqRoodir(sqrootdir_g);
		// databankname sagt uns was wir einsammeln wollen
		sr.collectResults(databankname);
		
		// get resultrootpath out of resultdir
		String resultroothpath = getSqRootpath(resultdir_g);
		
		// Step2: dann exportieren wir die gesammelten daten mit dem sqli interface
		// Falls showresults angew�hlt ist wird das ganze noch sch�n ausgegeben
		
		// Datenbank wird exportiert
		// wird mit cli befehlen gemacht siehe
		// https://strategyquant.com/doc/cli-command-line/introduction-to-cli/
		
		if (showresultsflag == true)
		{
			// die results werden exportiert
			SqExporterBatch se = new SqExporterBatch();
			se.setSqRootpath(resultroothpath);
			se.setSqWorkflowDir(resultroothpath + "\\user\\projects");
			se.exportDatabase();
			
			// baut aus dem exportierten datenbankfile eine verkleinerte Resultliste auf
			SqDatabaseHandler sb = new SqDatabaseHandler(sqrootdir_g);
			sb.SqReadBaseList(se.getDatabankfile(), sqrootdir_g, cpart, databankname, normation);
			sb.writeResultlist("c:\\tmp\\DatabankExportResultlist.csv", databankname, normation, fullreportflag,
					outputname, cpart, maxstepsback);
			sb.ShowChart();
			
		}
		// copy the results to goggledrive
		if ((shareddrive_g != null) && (copygoogledriveflag == true))
		{
			Tracer.WriteTrace(20, "I:copy strategies <" + sqrootdir_g + "> to shared drive<\"+sharedrive_g+\">");
			copyDrive(sqrootdir_g, shareddrive_g, outputname_g, masterfile_g, showresultsflag, databankname);
		}
		// copy the results to backupdrive
		if ((backupdrive_g != null) && (copybackupflag == true))
		{
			Tracer.WriteTrace(20, "I:copy strategies <" + sqrootdir_g + "> to backup drive<\"+backupdrive_g+\">");
			copyDrive(sqrootdir_g, backupdrive_g, outputname_g, masterfile_g, showresultsflag, databankname);
		}
		// zeige verkleinerte resultliste
		if (showresultsflag == true)
		{
			Viewer v = new Viewer();
			v.viewTableExtFile(Display.getCurrent(), "c:\\tmp\\DatabankExportResultlist.csv");
		}
		
	}
	
	public void copyMasterfile(String sourcefile, String shareddrive)
	{
		String projdir = sourcefile.substring(0, sourcefile.lastIndexOf("\\"));
		projdir = projdir.substring(projdir.lastIndexOf("\\") + 1);
		String dstdir = shareddrive + "\\" + projdir;
		
		String destfile = dstdir + "\\" + projdir + ".cfx";
		
		FileAccess.checkgenDirectory(dstdir);
		
		Path quellePfad = Paths.get(sourcefile);
		Path zielPfad = Paths.get(destfile);
		
		try
		{
			Files.copy(quellePfad, zielPfad, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void copyDrive(String sqrootdir, String shareddrive, String outputname, String masterfile,
			Boolean showresultsflag, String databankname)
	{
		// sqrootdir: is the path to the sq, this is the rootpath
		// shareddrive: is the path to the backup/shared-drive
		// outputname: is the uniqe name for the workflow e.g. Q52 EURUSD H1
		
		// verzeichnissstruktur in googledrive herstellen
		String destdir = shareddrive + "\\" + outputname;
		String portfolios = destdir + "\\portfolios";
		String projects = destdir + "\\projects";
		
		// check if destdir exists
		File wfdir_f = new File(destdir);
		if (wfdir_f.exists() == false)
			if (wfdir_f.mkdir() == false)
				Tracer.WriteTrace(10, "E: can�t create directory <" + wfdir_f.getPath() + "> --> stop");
			
		// make dir portfolios for storing all portfolios
		File wfport_f = new File(portfolios);
		if (wfport_f.exists() == false)
			if (wfport_f.mkdir() == false)
				Tracer.WriteTrace(10, "E: can�t create directory <" + wfport_f.getPath() + "> --> stop");
			
		// results in portfolios ins googledrive kopieren
		SqCollectStoreResultsMain gr = new SqCollectStoreResultsMain();
		gr.setResultdir(portfolios);
		gr.setSqRoodir(sqrootdir);
		gr.collectResults(databankname);
		
		// masterfile kopieren
		gr.copyMasterfile(masterfile, shareddrive + "\\" + outputname);
		
		// results aus tmp kopieren
		if (showresultsflag == true)
		{
			gr.copyResultfile("c:\\tmp\\DatabankExport.csv", shareddrive + "\\" + outputname);
			gr.copyResultfile("c:\\tmp\\DatabankExportResultlist.csv", shareddrive + "\\" + outputname);
		}
		// das ganze Projects kopieren
		// projects erzeugen
		File sqrootdir_f = new File(sqrootdir + "\\user\\projects");
		File projects_f = new File(projects);
		if (projects_f.exists() == false)
			if (projects_f.mkdir() == false)
				Tracer.WriteTrace(10, "E: can�t create directory <" + projects_f.getPath() + "> --> stop");
		try
		{
			FileAccess.CopyDirectory4(sqrootdir_f, projects_f);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Tracer.WriteTrace(10, "E:stop on filecopy");
		}
	}
	
	private String calcWorkflowname(int index, int offset)
	{
		DecimalFormat df = new DecimalFormat("00000");
		String wfname = null;
		if (index < 0)
			wfname = outputname_g + "_-" + df.format(offset);
		else
			wfname = outputname_g + "_+" + df.format(offset);
		
		return wfname;
	}
	
	private String calcWorkflownameFilename(int index, int offset)
	{
		DecimalFormat df = new DecimalFormat("00000");
		String wfname = null;
		if (index > 0)
			wfname = outputname_g + "_-" + df.format(offset);
		else
			wfname = outputname_g + "_+" + df.format(offset);
		
		return wfname;
	}
}
