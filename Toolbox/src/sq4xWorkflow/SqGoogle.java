package sq4xWorkflow;

import java.io.File;
import java.io.IOException;

import FileTools.Filefunkt;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;

public class SqGoogle
{
	
	
	static public String ReadInfomessage(String googlepath, String outname)
	{
		
		if ((googlepath != null) && (outname != null))
		{
			String fname = googlepath + "\\" + outname + "\\info.txt";
			if (new File(fname).exists())
			{
				Inf inf = new Inf();
				inf.setFilename(fname);
				
				String s = inf.readZeilen();
				inf.close();
				return s;
			}
		}
		return "empty";
	}
	static public void WriteInfomessage(String googlepath,String outname,String text)
	{
		String fname1 = googlepath + "\\" + outname + "\\info.txt";
		String fname2 = googlepath + "\\" + outname + "\\info_.txt";
		File fdir= new File(googlepath + "\\" + outname);
		if(fdir.exists()==false)
			fdir.mkdir();
		
		//die alte kopie löschen
		File fnam2=new File(fname2);
		if(fnam2.exists())
			if(fnam2.delete()==false)
				Tracer.WriteTrace(10, "E:internal can´t delete <"+fnam2.getAbsolutePath()+">");
		
		//den neuen text in info_ abspeichern
		Inf inf=new Inf();
		inf.setFilename(fname2);
		if(inf.writezeile(text)==false)
			Tracer.WriteTrace(10, "E:can´t write file <"+fname2+">");
		inf.close();
		
		//altes info.txt löschen
		File fnam1=new File(fname1);
		if(fnam1.exists())
			if(fnam1.delete()==false)
				Tracer.WriteTrace(10, "E:internal can´t delete <"+fnam1.getAbsolutePath()+">");
		
		//kopieren

		try
		{
			Filefunkt.copyFileUsingChannel(fnam2,fnam1);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
