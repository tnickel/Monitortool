package sq4xWorkflow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import FileTools.Filefunkt;
import hiflsklasse.FileAccess;
import hiflsklasse.Tracer;
import work.JToolboxProgressWin;

public class SqResults
{
	//sammelt die resultate ein und kopier die in das resultdir
	private String resultdir_g, sqroodir_g;
	
	SqResults()
	{
	}
	
	public void setResultdir(String resultdir)
	{
		//dies ist das zielverzeichniss wo das hin son
		this.resultdir_g = resultdir;
	}
	
	public void setSqRoodir(String sqroodir)
	{
		//hier ist das rootverzeichniss des sq
		this.sqroodir_g = sqroodir;
	}
	
	public void collectResults()
	{
		//resultdir ist der verzeichniss wo alles gesammelt wird
		//jedes result bekommt im Namen den Projektnamen
		//z.B. Q63 GBPUSD L0_+00000_portfolio.sqx
		File resultdir_f=new File(resultdir_g);
		if(resultdir_f.exists()==false)
			resultdir_f.mkdir();
		
		//gehe durch alle verzeichnisse 
		FileAccess.initFileSystemList(sqroodir_g+ "\\user\\Projects\\", 0);
		int anz = FileAccess.holeFileAnz();

		JToolboxProgressWin jp = new JToolboxProgressWin("store results to "+resultdir_g, 0,
				(int) Math.abs((anz)));
		for (int i = 0; i < anz; i++)
		{
			jp.update(i);
			//bsp: projetname=Q63 GBPUSD L0_--01800
			String projectname=FileAccess.holeFileSystemName();
			String portfoliosrcdir = sqroodir_g + "\\user\\Projects\\" + projectname
					+ "\\databanks\\portfolio";
			File quelldir_f=new File(portfoliosrcdir);
			File zieldir_f=new File(resultdir_g);
			
			Filefunkt ff=new Filefunkt();
			try
			{
				//kopiert alle sqx aus dem quelldir nach zieldir, in den zielnamen wird der projectname eingebaut
				//bsp f�r zielfile
				Tracer.WriteTrace(20, "I:collect portfolio <"+quelldir_f.getAbsolutePath()+">");
				ff.copyDirAllSqxProjektname(quelldir_f, zieldir_f,projectname);
			} catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		
		}
		jp.end();
	}
	public void copyMasterfile(String masterfile,String destdir)
	{
		
		File f1=new File(masterfile);
		String masterfilename=f1.getName();
		
		File f2=new File(destdir+"\\"+masterfilename);
		try
		{
			Filefunkt.copyFileUsingChannel(f1,f2);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void copyResultfile(String resultfile,String destdir)
{
		
		File f1=new File(resultfile);
		String masterfilename=f1.getName();
		
		File f2=new File(destdir+"\\"+masterfilename);
		try
		{
			Filefunkt.copyFileUsingChannel(f1,f2);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
