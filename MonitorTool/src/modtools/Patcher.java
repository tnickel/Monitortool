package modtools;

import hiflsklasse.FileAccess;
import hiflsklasse.Tracer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class Patcher
{
	// Der Patcher bildet die grundklasse von cfg.patch und mql.patch

	private BufferedWriter bw_g = null;
	private BufferedReader br_g = null;
	private String filename_glob = null;
	public String zeilenspeicher[] = new String[20000];
	protected static int isSq4x = -1;

	public void setFilename(String fnam)
	{
		filename_glob = fnam;
		this.readMemFile();

		// set version
		if (isSq4x == -1)
		{
			if (checkKeyword("StrategyQuant version 3.8") == true)
				isSq4x = 0;
			else if (checkKeyword("new version") == true)
				isSq4x = 1;
			else
				Tracer.WriteTrace(10, "E:mqlSqPatcher can�t determine version");
		}

	}

	public boolean isSq3Ea()
	{
		if(isSq4x==0)
		  return true;
		else if(isSq4x==1)
			return false;
		else
			Tracer.WriteTrace(10, "E:Patcher version not initialilizied");
		return false;
	}

	public boolean isSq4Ea()
	{
		if(isSq4x==1)
			return true;
		else if(isSq4x==0)
			return false;
		else
			Tracer.WriteTrace(10, "E:Patcher version not initialilizied");
		return false;
		
	}

	public boolean checkKeyword(String keyword)
	{

		// pr�ft ob ein keywort in der datei vorkommt
		for (int i = 0; i < 20000; i++)
		{
			if (zeilenspeicher[i] == null)
				continue;

			if (zeilenspeicher[i].contains(keyword))
				return true;
		}
		return false;
	}

	public void addnewline(int pos, String newline)
	{
		// es wird eine neue Zeile in den Zeilenspeicher eingef�gt
		// hierzu wird der Rest nach hinten geschoben

		// gehe von hinten vor
		for (int i = 19999; i > pos; i--)
		{
			// h�nge um
			zeilenspeicher[i] = zeilenspeicher[i - 1];
		}
		zeilenspeicher[pos] = new String(newline);
	}

	public void setMagic(int magic)
	{

		int anz = zeilenspeicher.length;
		for (int i = 0; i < anz; i++)
		{
			String zeile = zeilenspeicher[i];
			if (zeile != null)
				if (zeile.contains("extern int MagicNumber") == true)
				{
					zeilenspeicher[i] = "extern int MagicNumber = " + magic
							+ ";";
					return;
				}
		}
		Tracer.WriteTrace(10, "Error: keine Magicnumber im mql-File<"
				+ filename_glob + "> gefunden--> STOP");
	}
	
	protected void addVariablesSq3()
	{
		int anz = zeilenspeicher.length;
		for (int i = 0; i < anz; i++)
		{
			String zeile = zeilenspeicher[i];
			if (zeile != null)
				if ((zeile.contains("color LabelColor ") == true)||(zeile.contains("#property link")==true))
				{
					 addnewline(i+1,"string lotconfigmem[20];");
					 return;
				}
		}
		
		Tracer.WriteTrace(10, "Error: kann zeilenspeicher nicht hinzufuegen advariables--> stop");
	}
	protected void addVariablesSq4()
	{
		int anz = zeilenspeicher.length;
		for (int i = 0; i < anz; i++)
		{
			String zeile = zeilenspeicher[i];
			if (zeile != null)
				if (zeile.contains("color LabelColor = White;") == true)
				{
					 addnewline(i+1,"string lotconfigmem[20];");
					 return;
				}
		}
		Tracer.WriteTrace(10, "Error: kann zeilenspeicher nicht hinzufuegen addvariables--> stop");
	}
	
	public void setLotsize_depricated(String lotsize)
	{
		int anz = zeilenspeicher.length;
		for (int i = 0; i < anz; i++)
		{
			String zeile = zeilenspeicher[i];
			if (zeile != null)
				if (zeile.contains("extern double Lots = ") == true)
				{
					zeilenspeicher[i] = "extern double Lots = " + lotsize + ";";
					return;
				}
		}

		Tracer.WriteTrace(10, "Error: keine lotsize im File <" + filename_glob
				+ "> gefunden e.g. suche 'extern double Lots = 0.1'");

	}

	public void readMemFile()
	{
		int n = 0;
		String zeile = null;
		while ((zeile = readZeile()) != null)
		{

			zeilenspeicher[n] = new String(zeile);
			n++;
		}
		this.close();
		return;

	}

	public void writeMemFile(String newFilename)
	{
		String fnam, zeile;
		int i = 0;

		if (newFilename == null)
			fnam = filename_glob;
		else
			fnam = newFilename;

		if (bw_g == null)
			bw_g = FileAccess.WriteFileOpen(fnam, "ISO-8859-1");

		while ((zeile = zeilenspeicher[i]) != null)
		{
			writezeile(zeile);
			i++;
		}
		try
		{
			bw_g.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String readZeile()
	{
		// ISO-8859-1

		if (br_g == null)
			br_g = FileAccess.ReadFileOpen(filename_glob, "ISO-8859-1");
		String str;
		try
		{
			str = new String(br_g.readLine());
			return str;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block

			return null;
		} catch (NullPointerException e)
		{
			// TODO Auto-generated catch block

			try
			{
				if (br_g != null)
					br_g.close();
			} catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;
		}
	}

	public boolean writezeile(String zeile)
	{

		try
		{

			if (bw_g == null)
			{
				Tracer.WriteTrace(10, "Warning: filefehler02 file=null<"
						+ filename_glob + ">");
				return false;
			}
			bw_g.write(zeile);
			bw_g.newLine();

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void close()
	{
		if (bw_g != null)
			try
			{
				bw_g.close();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
