package sq4xWorkflow;

import java.io.File;
import java.text.DecimalFormat;

import hiflsklasse.Tracer;
import work.JToolboxProgressWin;

public class SqWorkflow
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
		this.masterfile_g = masterfile_g;
	}
	
	public void setSqrootdir(String sqrootdir_g)
	{
		this.sqrootdir_g = sqrootdir_g;
	}
	
	public void setResultdir(String resultdir_g)
	{
		this.resultdir_g = resultdir_g;
	}
	
	public void setSharedDrive(String dir)
	{
		this.shareddrive_g = dir;
	}
	
	public void setBackupDrive(String dir)
	{
		this.backupdrive_g = dir;
	}
	
	public void setBackcount(String days)
	{
		// negative for the past
		this.backcount_g = Integer.valueOf(days);
	}
	
	public void setFuturecount(String days)
	{
		this.futurecount_g = Integer.valueOf(days);
	}
	
	public void setStepvalue(String steps)
	{
		this.stepvalue_g = Integer.valueOf(steps);
	}
	
	public void setOutputname_g(String outputname)
	{
		this.outputname_g = outputname;
	}
	
	public void calcFilter()
	{
		// Hier wird die workflowgenerierung druchgeführt
		
		int offset = 0;
		// some initialisation progressslider and dfformat
		JToolboxProgressWin jp = new JToolboxProgressWin("calc Workflows", 0,
				(int) Math.abs((futurecount_g - backcount_g)));
		
		// start new projectfile
		Tracer.WriteTrace(20, "I:read masterfile<" + masterfile_g + ">");
		ProjectFile psq = new ProjectFile(masterfile_g);
		for (int i = -backcount_g, loopcount = 0; i <= futurecount_g; i++, loopcount++)
		{
			jp.update(loopcount);
			// modify project
			
			offset = i * stepvalue_g;
			psq.modifyProject(offset);
			// save projekt
			psq.saveTmpProjectfile();
			
			String workflowname = calcWorkflowname(i, offset);
			
			// copy to destination
			psq.copyToSq(sqrootdir_g, workflowname);
			
			Tracer.WriteTrace(20, "I:generated workflow <" + workflowname + ">");
		}
		jp.end();
		
	}
	
	public void collectResults()
	{
		// die normalen results in das erste Zielverzeichniss vom SQ
		SqResults sr = new SqResults();
		sr.setResultdir(resultdir_g);
		sr.setSqRoodir(sqrootdir_g);
		sr.collectResults();
		
		//copy to goggledrive
		copyDrive(sqrootdir_g,shareddrive_g, outputname_g,masterfile_g);
		
		//copy to backupdrive
		if(backupdrive_g!=null)
			copyDrive(sqrootdir_g,backupdrive_g,outputname_g,masterfile_g);
		
	
	}
	
	private void copyDrive(String sqrootdir,String shareddrive,String outputname,String masterfile)
	{
		//sqrootdir: is the path to the sq, this is the rootpath
		//shareddrive: is the path to the backup/shared-drive
		//outputname: is the uniqe name for the workflow e.g. Q52 EURUSD H1
		
		// verzeichnissstruktur in googledrive herstellen
		String destdir = shareddrive + "\\" + outputname;
		String portfolios = destdir + "\\portfolios";

		//check if destdir exists
		File wfdir_f = new File(destdir);
		if (wfdir_f.exists() == false)
			wfdir_f.mkdir();
		
		//make dir portfolios for storing all portfolios
		File wfport_f = new File(portfolios);
		if (wfport_f.exists() == false)
			wfport_f.mkdir();
		
		// results in portfolios ins googledrive kopieren
		SqResults gr = new SqResults();
		gr.setResultdir(portfolios);
		gr.setSqRoodir(sqrootdir);
		gr.collectResults();

		// masterfile kopieren
		gr.copyMasterfile(masterfile, shareddrive+"\\" + outputname);
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
	
}
