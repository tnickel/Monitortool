package sq4xWorkflow;

import java.io.IOException;
import java.text.DecimalFormat;

public class SqWorkflow
// this class read all Project workflows and work with it
// sourcedir: directory of the sourcedir of all workflows,this file will never
// modified
// destdir: this the targetdir of the modified files
// configfile: In this file are the actions what the workflowclass has to do.
{
	private String parameter_g, sourcedir_g, destdir_g;
	
	public void setParameter(String parameter)
	{
		this.parameter_g = parameter;
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
		// read the projekt in memory
		try
		{
			DecimalFormat df = new DecimalFormat("00000");
			
			ProjectFile psq = new ProjectFile(sourcedir_g + "\\project.cfx");
			//String rootdir = sourcedir_g.substring(0, sourcedir_g.lastIndexOf("\\"));
			psq.modifyProject(parameter_g);
			psq.saveTmpProjectfile();
			for (int i = 0; i < 3; i++)
				psq.copyToSq(destdir_g, "AutoGenWorkflow" + df.format(i));
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
