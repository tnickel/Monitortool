package data;

import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.SG;
import hiflsklasse.Tracer;

import java.io.File;
import java.util.ArrayList;

import work.MboxToolQuest;

public class Endtestdata
{
	//Endtestdata hat die Aufgabe die Endtestdaten in einer speicherstruktur
	//zu halten.
	
	private static ArrayList<Profitelement> profitliste_intern = new ArrayList<Profitelement>();
	private static String filename_glob = null;
    //was ist dieser mode_glob
	private int mode_glob=0;
	
	public Endtestdata()
	{
		filename_glob = GlobToolbox.getRootpath() + "\\data\\endtest.xml";
	}

	public boolean checkDatafile()
	{
		// prüft ob das datenfile da ist
		File dfile = new File(filename_glob);
		return (dfile.exists());

	}

	public void loadEndtestdata()
	{
		if (profitliste_intern.size() > 0)
			return;

		if (FileAccess.FileAvailable0(filename_glob) == false)
		{
			MboxToolQuest.Questbox("no datafile <" + filename_glob + ">");
			return;
		}
		// tradeliste laden
		Inf inf = new Inf();
		inf.setFilename(filename_glob);

		// profitliste einlesen
		profitliste_intern = (ArrayList<Profitelement>) inf.loadXST();
		inf.close();
		
		
	}

	private void loadMode()
	{
		//der mode gibt an ob is-netprofit, oss-netprofit, NetProfitRobust
		String fnam=filename_glob.replace(".xml", ".txt");
		
		if(FileAccess.FileAvailable(fnam)==false)
			return;
		
		Inf inf2= new Inf();
		inf2.setFilename(filename_glob.replace(".xml", ".txt"));
		String mode=inf2.readZeile();
		mode_glob=SG.get_zahl(mode);
		inf2.close();
	}
	
	public void saveEndtestdata()
	{

		// speichern
		Inf inf = new Inf();
		inf.setFilename(filename_glob);
		inf.saveXST(profitliste_intern);
		inf.close();
		
		
		String inf2nam=filename_glob.replace(".xml", ".txt");
		File i2= new File(inf2nam);
		if(i2.exists())
			i2.delete();
		
		Inf inf2= new Inf();
		inf2.setFilename(inf2nam);
		inf2.writezeile(String.valueOf(mode_glob));
		inf2.close();
		

	}

	public void deleteEnddatafile()
	{
		profitliste_intern.clear();
	
		File endf = new File(filename_glob);
		if (endf.exists() == false)
		{
			{
				MboxToolQuest.Questbox("no datafile <" + filename_glob
						+ "> to delete");
				return;
			}
		} else
			endf.delete();
	}

	public void addProfit(Profitelement prof)
	{
		profitliste_intern.add(prof);
		
	}

	public float getProfit(String filename)
	{
		String suchname = filename.substring(filename.lastIndexOf("\\") + 1);

		if(suchname.contains(".txt"))
			suchname=suchname.replace(".txt", ".str");
		
		int anz = profitliste_intern.size();
		for (int i = 0; i < anz; i++)
		{
			Profitelement prof = profitliste_intern.get(i);
			if (prof.getFilename().equalsIgnoreCase(suchname) == true)
				return (prof.getGrossprofit()+prof.getGrossloss());
		}

		// den profit nicht gefunden
		Tracer.WriteTrace(20,"Error filename <" + suchname+ "> not found in profitlist");

		return -99999999;
	}
	public int getMode()
	{
		if(mode_glob==0)
			loadMode();
		
		return(mode_glob);
	}
	public void setMode(int m)
	{
		mode_glob=m;
	}
}
