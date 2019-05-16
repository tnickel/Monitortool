package db;

import hilfsklasse.FileAccess;
import hilfsklasse.Tracer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import mainPackage.GC;

public class DBlauf
/*
 * datenbank läuferklasse, hier werden einzelne Datenfiles um weitere Infos
 * erweitet Die Datenfiles werden Zeilenweise eingelesen und es wird eine Liste
 * erstellt Diese Liste kann ggf. erweitert werden
 */
{
	static String rootpath = GC.rootpath;

	private String filename = null;
	private String speicherfile = null, kennung = null;
	private ArrayList<String> dbliste = new ArrayList<String>();
	private int maxzaehler = 0;

	public DBlauf()
	{
	}

	public DBlauf(String file, String kenn)
	{
		// bsp: file=
		LoadDB(file, kenn);
	}

	public boolean LoadDB(String spfile, String kenn)
	{
		if (speicherfile != null)
		{
			System.out.println("DB-File constructor, db schon geladen");
			return true;
		}

		Tracer.WriteTrace(40, this.getClass().getName() + "LoadDBlauf");

		kennung = kenn;
		if(spfile.contains("compressed")==true)
		{
			 char first=kenn.charAt(0);
			 speicherfile = "\\db\\" + spfile + "\\@"+first+"\\" + kennung + ".db";
		}
		else
		 speicherfile = "\\db\\" + spfile + "\\" + kennung + ".db";

		filename = rootpath + speicherfile;

		if (ReadDB(0) == false)
			Tracer.WriteTrace(40, this.getClass().getName() + "I:<"
					+ speicherfile + "> not available 03");
		return true;
	}

	private boolean ReadDB(int openflag)
	{
		// ist im Augenblick nur für das objekt "WebobserveObj" ausgelegt
		BufferedReader inf = null;

		if (FileAccess.FileAvailable(filename) == false)
		{
			Tracer.WriteTrace(40, this.getClass().getSimpleName() + "<"
					+ filename + "> not available");
			return false;
		}

		inf = FileAccess.ReadFileOpen(filename);

		String zeile = "";
		try
		{
			while ((zeile = inf.readLine()) != null)
			{
				Tracer.WriteTrace(50, this.getClass().getSimpleName()
						+ "zeile=<" + zeile + ">");
				if (zeile.contains("*****") == true)
				{
					continue;
				}
				if (zeile.length() < 2)
					continue;

				dbliste.add(zeile);
			}
			inf.close();
			maxzaehler = dbliste.size();
			return true;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	public boolean WriteDB()
	{
		String zeile = null;
		int i = 0, anz = 0, oldlen = 0, newlen = 0;
		anz = dbliste.size();

		BufferedWriter ouf;
		Tracer.WriteTrace(50, this.getClass().getName() + "WriteDB");
		try
		{
			ouf = FileAccess.WriteFileOpen(FileAccess.convsonderz(filename
					+ ".tmp"), "UTF-8");
			ouf.write("******" + speicherfile + " anz=" + anz);
			ouf.newLine();

			for (i = 0; i < anz; i++)
			{
				zeile = dbliste.get(i).toString();

				if (zeile.contains("##") == true)
				{
					Tracer.WriteTrace(20, "Warning: zeile <" + zeile
							+ "> hat ## -> verwerfe");
					continue;
				}

				Tracer.WriteTrace(50, this.getClass().getName() + "write<"
						+ zeile + ">");
				ouf.write(zeile);
				ouf.newLine();
			}
			ouf.close();

			// length protection, compare lenght
			oldlen = FileAccess.FileLength(filename);
			newlen = FileAccess.FileLength(filename + ".tmp");

			if ((newlen < oldlen - 50)&&(filename.contains("compressed")==false))
			{
				// lenght error, new is shorter then the oldfile
				Tracer.WriteTrace(10, this.getClass().getName()
						+ "ERROR: length protection error !!!<" + filename + "> <"
						+ filename + ".tmp" + "> oldlen<" + oldlen
						+ "> newlen<" + newlen + ">");
				
			}
			FileAccess.Rename(filename, filename + ".bak");
			FileAccess.Rename(filename + ".tmp", filename);
			return true;

		} catch (FileNotFoundException e)
		{

			e.printStackTrace();
			Tracer.WriteTrace(10, this.getClass().getName()
					+ "ERROR: file not found<" + filename + ">");
			return false;
		} catch (IOException e)
		{

			e.printStackTrace();
			Tracer.WriteTrace(10, this.getClass().getName()
					+ "ERROR: IO-error<" + filename + ">");
			return false;
		}
	}

	public boolean UpdateZeile(String zeile, int verglcounter)
	{
		// das objekt besteht aus datum+uhrzeit#zaehler
		// 17.05.08 174930#45454
		boolean b = false;
		int n = 0;

		if ((n = IstVorhanden(zeile, verglcounter)) < 0)
		{
			// element noch nicht drin
			b = dbliste.add(zeile);
			if (b == true)
				maxzaehler++;
		} else
		{
			// falls das element schon da ist wird es ersetzt
			dbliste.remove(n);
			dbliste.add(n, zeile);
		}
		return (b);
	}

	protected String GetLastZeile()
	{
		// holt die zuletzt eingefügte Zeile
		int anz = dbliste.size();
		if (anz > 0)
			return dbliste.get(anz - 1);
		else
			return null;
	}

	private int IstVorhanden(String zeile, int anzzeichen)
	{
		// prüft ob das datum schon vorhanden ist
		// es werden die ersten 8 bytes vergleichen 08.03.08
		// anzzeichen=8
		int n = dbliste.size();

		for (int i = 0; i < n; i++)
		{
			String dat2 = dbliste.get(i);
			if (zeile.regionMatches(0, dat2, 0, anzzeichen) == true)
				return i;
		}
		return -1;
	}

	protected int getanzObj()
	{
		return dbliste.size();
	}

	protected String getZeileIDX(int indx)
	{
		if (dbliste.size() == 0)
			return null;

		return (dbliste.get(indx));
	}

	protected void cleanMemDB()
	{
		// löscht die DB im Speicher
		dbliste.clear();
	}
}
