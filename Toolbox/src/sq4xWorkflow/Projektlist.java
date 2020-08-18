package sq4xWorkflow;

import java.io.File;
import java.util.ArrayList;

import hiflsklasse.FileAccess;
import hiflsklasse.Tracer;



public class Projektlist
{
	private ArrayList<Project> projektlist_glob = new ArrayList<Project>();
	
	public  Projektlist(String rootverz)
	{
		//build directorylist
		FileAccess.initFileSystemList(rootverz, 0);
		
		int anz=FileAccess.holeFileAnz();
		for(int i=0; i<anz; i++)
		{
			String dirnam=FileAccess.holeFileSystemName();
			String fnam=rootverz+"\\"+dirnam+"\\project.cfx";
			File ffnam=new File(fnam);

			Tracer.WriteTrace(20, "I:Projectlist work file <"+fnam+">");
			
			//if it is workflow projekt than add this to list
			if(ffnam.length()>30000)
			{
				Project pro=new Project(fnam);
				projektlist_glob.add(pro );
			}
		}
	}
	public void writeProjektlist(String rootverz)
	{
		
		
	}
}
