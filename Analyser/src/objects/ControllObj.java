package objects;

import hilfsklasse.FileAccess;
import hilfsklasse.SG;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

import mainPackage.GC;

public class ControllObj
{
	// private static Prop p = new Prop();

	final private static String dbfilename = GC.rootpath + "\\db\\task.db";
	// time of last analyzing the websides
	private static String lastAnalysingTime = null;
	// time of the last creation of the last directory
	private static String lastCreationDirTime = null;

	private static int postcount = 0;
	private static int durration = 0; // time in hours till next directory
										// generation

	public ControllObj()
	{
		int n = 0;

		BufferedReader inf = FileAccess.ReadFileOpen(FileAccess
				.convsonderz(dbfilename));

		String zeile = "";
		try
		{
			while ((zeile = inf.readLine()) != null)
			{
				if (zeile.contains("*****task") == true)
					continue;

				n = SG.countZeichen(zeile, "#");
				if (n != 3)
					Tracer.WriteTrace(10, this.getClass().getName()
							+ "Error: task.db");

				// System.out.println("zeile="+zeile);
				try
				{
					lastAnalysingTime = SG.nteilstring(zeile, "#", 1);
					lastCreationDirTime = SG.nteilstring(zeile, "#", 2);
					durration = Integer.parseInt(SG.nteilstring(zeile, "#", 3));
					postcount = Integer.parseInt(SG.nteilstring(zeile, "#", 4));
				} catch (NumberFormatException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ToolsException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getLastAnalysingTime()
	{
		return lastAnalysingTime;

	}

	public String getLastCreationDirTime()
	{
		return lastCreationDirTime;
	}

	public void setLastCreationDirTime(String lastCreationDirTime)
	{
		ControllObj.lastCreationDirTime = lastCreationDirTime;
	}

	public int getPostcount()
	{
		return postcount;

	}

	public void setLastAnalysingTime(String ldat)
	{
		lastAnalysingTime = ldat;
		Write();

	}

	public void setPostcount(int pc)
	{
		postcount = pc;
	}

	public int getDurration()
	{
		return durration;
	}

	public void setDurration(int durration)
	{
		ControllObj.durration = durration;
	}

	public boolean Write()
	{

		BufferedWriter ouf = FileAccess.WriteFileOpen(dbfilename, "UTF-8");
		try
		{
			ouf = FileAccess.WriteFileOpen(FileAccess.convsonderz(dbfilename),
					"UTF-8");
			ouf
					.write("*****task**, lastAnalysing#lastCreation#durration (hours)#counter");

			ouf.newLine();

			String zeile = lastAnalysingTime + "#" + lastCreationDirTime + "#"
					+ durration + "#" + postcount;
			ouf.write(zeile);
			ouf.newLine();

			ouf.close();
			return true;

		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

}
