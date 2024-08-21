package Metriklibs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;

import gui.JLibsProgressWin;
import gui.ShowGoodBadCounter;
import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;

public class GeneratorStatistik
{
	private String logfile_goodbad_glob=null;
	private float goodfaktor_glob=0;
	public GeneratorStatistik()
	{
	}
	
	public void collectGoodBad(String filterpath)
	{
		
		FileAccessDyn fdirs = new FileAccessDyn();
		fdirs.initFileSystemList(filterpath, 0);
		
		int anz = fdirs.holeFileAnz();
		
		int progcount = 0;
		JLibsProgressWin jp = new JLibsProgressWin("Learn Workflow", 0, anz);
		
		String logfile= filterpath+"\\WORKFLOWNAME\\good_badcounter.txt";
		 logfile_goodbad_glob=logfile;
		FileAccess.FileDelete(logfile, 1);
		Inf inf = new Inf();
		inf.setFilename(logfile);
		inf.writezeile("periodnumber#goodcounter#badcounter");	
		
		int sum_good=0, sum_bad=0;
		
		for (int i = 0; i < anz; i++)
		{
			jp.update(i);
			String workdir = (filterpath + "\\" + fdirs.holeFileSystemName()).toLowerCase();
			if (isValidWorkdir(workdir) == false)
				continue;
			
			String sub=workdir.substring(workdir.lastIndexOf("_"));
			sub=sub.replace("_", "").replace("+", "").replace("-","");
			int number=Integer.valueOf(sub);
			
			String goodcounterfile = workdir + "\\_99_dir\\goodcounter\\DatabankExport.csv";
			if(new File(goodcounterfile).exists()==false)
			{
				Tracer.WriteTrace(10, "E: no goodcounterfile <"+goodcounterfile+"> available");
				return;
			}
			String badcounterfile = workdir + "\\_99_dir\\badcounter\\DatabankExport.csv";
			if(new File(badcounterfile).exists()==false)
			{
				Tracer.WriteTrace(10, "E: no badcounterfile <"+badcounterfile+"> available");
				return;
			}
			
			int goodcounter=countLinesInFile(goodcounterfile)-1;
			int badcounter=countLinesInFile(badcounterfile)-1;
			inf.writezeile(number+"#"+goodcounter+"#"+badcounter);
			sum_good=sum_good+goodcounter;
			sum_bad=sum_bad+badcounter;
		}
		goodfaktor_glob=(float)sum_good/((float)sum_bad+(float)sum_good);
		jp.end();
		inf.writezeile("### anz good=" + sum_good + " anz bad=" + sum_bad + " faktor= (good/bad)=" + goodfaktor_glob);

		inf.close();
	}
	
	private boolean isValidWorkdir(String dir)
	{
		if ((dir.endsWith("endtest")) || (dir.endsWith("long")) || (dir.endsWith("workflowname")))
			return false;
		return true;
		
	}
	
	private int countLinesInFile(String filePath)
	{
		int lines = 0;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
		{
			while (reader.readLine() != null)
			{
				lines++;
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return lines;
	}
	public void showGoodBadcounter()
	{
		 

	        JFrame frame = new JFrame("Show Good and Bad Counter faktor="+goodfaktor_glob);
	        ShowGoodBadCounter panel = new ShowGoodBadCounter(logfile_goodbad_glob);
	        frame.add(panel);
	        frame.setSize(1600, 1200);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setVisible(true);
		
		
		
	
	}
}
