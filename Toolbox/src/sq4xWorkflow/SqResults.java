package sq4xWorkflow;

import java.io.File;
import java.io.IOException;

import FileTools.Filefunkt;
import hiflsklasse.FileAccess;

public class SqResults
{
	private String resultdir_g, sqroodir_g;
	
	SqResults()
	{
	}
	
	public void setResultdir(String resultdir)
	{
		this.resultdir_g = resultdir;
	}
	
	public void setSqRoodir(String sqroodir)
	{
		this.sqroodir_g = sqroodir;
	}
	
	public void collectResults()
	{
		File resultdir_f=new File(resultdir_g);
		if(resultdir_f.exists()==false)
			resultdir_f.mkdir();
		
		FileAccess.initFileSystemList(sqroodir_g+ "\\user\\Projects\\", 0);
		int anz = FileAccess.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String projectname=FileAccess.holeFileSystemName();
			String portfolioname = sqroodir_g + "\\user\\Projects\\" + projectname
					+ "\\databanks\\portfolio\\Merged Portfolio.sqx";
			File fnam = new File(portfolioname);
			if(fnam.exists()==true)
			{
				File f1=new File(portfolioname);
				File f2=new File(resultdir_g+"\\"+projectname+"_portfolio.sqx");
				if(f2.exists())
					f2.delete();
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
	}
}
