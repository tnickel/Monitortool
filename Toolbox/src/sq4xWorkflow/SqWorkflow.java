package sq4xWorkflow;

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
	private String sourcedir_g, destdir_g,outputname_g;
	private int steps_g, anzdays_g;
	
	public void setAnzDays(String parameter)
	{
		this.anzdays_g = Integer.valueOf(parameter);
	}
	
	public void setSteps(String steps)
	{
		this.steps_g = Integer.valueOf(steps);
	}


	public void setOutputname_g(String outputname)
	{
		this.outputname_g = outputname;
	}

	public void setSourcedir(String sourcedir)
	{
		this.sourcedir_g = sourcedir;
	}
	
	public void setDestdir(String destdir)
	{
		this.destdir_g = destdir;
	}
	
	public void calcFilter()
	{
		int daysbackcounter=0;
		//some initialisation progressslider and dfformat
		JToolboxProgressWin jp = new JToolboxProgressWin("calc Workflows", 0, (int) steps_g);
		DecimalFormat df = new DecimalFormat("0000");
		
		//start new projectfile
		ProjectFile psq = new ProjectFile(sourcedir_g + "\\project.cfx");
		for(int i=0; i<steps_g; i++)
		{
			jp.update(i);
			//modify project
			psq.modifyProject(daysbackcounter);
			//save projekt
			psq.saveTmpProjectfile();
			String wfname=outputname_g+"_-"+df.format(daysbackcounter);
			//copy to destination
			psq.copyToSq(destdir_g, wfname);
			daysbackcounter=daysbackcounter+anzdays_g;
			Tracer.WriteTrace(20, "I:generated workflow <"+wfname+">");
		}
		jp.end();
		Tracer.WriteTrace(10, "ready");
	}
	
}
