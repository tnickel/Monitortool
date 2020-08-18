package sq4xWorkflow;

import org.eclipse.swt.widgets.Text;

public class SqWorkflow
//this class read all Project workflows and work with it
//sourcedir: directory of the sourcedir of all workflows,this file will never modified
//destdir: this the targetdir of the modified files
//configfile: In this file are the actions what the workflowclass has to do.
{
	private String configfile,sourcedir, destdir;

	public void setConfigfile(String configfile)
	{
		this.configfile = configfile;
	}

	public void setSourcedir(String sourcedir)
	{
		this.sourcedir = sourcedir;
	}

	public void setDestdir(String destdir)
	{
		this.destdir = destdir;
	}

	public void calcFilter()
	{
		Projektlist pl=new Projektlist(sourcedir);
	}
	
		
		
	
	public void genWorkflow()
	{
		
	}
}
