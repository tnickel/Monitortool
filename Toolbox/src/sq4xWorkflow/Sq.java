package sq4xWorkflow;

import java.io.File;

import hiflsklasse.Tracer;

public class Sq
{
	//help functions for sq
	public String getSqRootpath(String path)
	{
		String rp=null;
		//C:\\forex\\SQ4\\SQ_11_08_2020_ssd\\user\\projects
		
		if(path.contains("\\user"))
		{
			rp=path.substring(0,path.indexOf("\\user"));
			File lfile=new File(rp+"\\StrategyQuantX.exe");
			if(lfile.exists()==false)
				Tracer.WriteTrace(10, "E: SQ dir <"+rp+"> don´t contains StrategyQuantX.exe");
			return rp;
	
		}
		if(rp==null)
			Tracer.WriteTrace(10, "E: can´t extrakt rootpath out of <"+path+">");
		return rp;
	}
}
