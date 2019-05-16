package hilfsklasse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class FileAccessDyn
{
	private List<File> threadverzliste = new ArrayList<File>();
	int verzpos = 0;
	int maxthreadentrys_all_types = 0;
	int maxentry = 0;
	int unixflag = 0;
	
	
	public  boolean copyFile2(String srcfilename, String destfilename)
	{
	
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			
			try
			{
				inputChannel = new FileInputStream(new File(srcfilename)).getChannel();
				outputChannel = new FileOutputStream(new File(destfilename)).getChannel();
				outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
			} catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				Tracer.WriteTrace(20, "E: can�t copy from<"+srcfilename+"> to<"+destfilename+">");
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				Tracer.WriteTrace(20, "E:IO exception in copy from<"+srcfilename+"> to<"+destfilename+">");
			}
			
		} finally {
			try
			{
				if(inputChannel!=null)
					inputChannel.close();
				if(outputChannel!=null)
				outputChannel.close();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				Tracer.WriteTrace(20, "io exception 44445444545");
			}
		
		}
	return true;
	}
	public BufferedWriter WriteFileOpen(String filename, String coding)
	{

		/* BufferedWriter ouf = null; */
		BufferedWriter ouf = null;
		FileOutputStream fouf = null;
		try
		{
			if (filename.contains(".gzip") == true)
			{
				// coding=null oder coding ="UTF-8"
				/*
				 * // speichere komprimiert ouf = new BufferedWriter(new
				 * OutputStreamWriter( new GZIPOutputStream(new
				 * FileOutputStream( convsonderz(filename))),"UTF-8"));
				 */
				fouf = new FileOutputStream(convsonderz(filename));
				GZIPOutputStream gouf = new GZIPOutputStream(fouf);
				if (coding == null)
					ouf = new BufferedWriter(new OutputStreamWriter(gouf));
				else
					ouf = new BufferedWriter(new OutputStreamWriter(gouf,
							coding));
			} else
			{
				fouf = new FileOutputStream(convsonderz(filename));
				// speichere im Klartext
				if (coding == null)
					ouf = new BufferedWriter(new OutputStreamWriter(fouf));
				else
					ouf = new BufferedWriter(new OutputStreamWriter(fouf,
							coding));
				/*
				 * ouf = new BufferedWriter(new
				 * FileWriter(convsonderz(filename)));
				 */
			}
			return (ouf);

		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (null);
	}

	private boolean FileAvailable_(String filename)
	{
		FileInputStream fi = null;

		try
		{
			fi = new FileInputStream(convsonderz(filename));

			if (fi.available() > 10)
			{
				fi.close();
				return (true);
			}
			fi.close();
		} catch (FileNotFoundException e)
		{

			// TODO Auto-generated catch block

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return (false);
	}

	public boolean FileAvailable(String filename)
	{

		if (FileAvailable_(convsonderz(filename)) == true)
			return true;

		return false;

	}

	public String convsonderz(String name)
	{
		String retstr = null;

		if (name.contains("?") == true)
		{
			while (name.contains("?") == true)
			{
				// wandle alle ? um
				String left = null, right = null;
				left = name.substring(0, name.indexOf("?"));
				right = name.substring(name.indexOf("?") + 1, name.length());
				retstr = left + "%3F" + right;
				name = retstr;
			}
			return name;
		}

		if (name.contains(".gzip.gzip"))
		{
			Tracer.WriteTrace(10, "Error: double gzip error fname<" + name
					+ ">");
			return null;
		}
		// I:\ sowas soll erlaubt sein, deswegen substiring von 2
		else if (name.substring(2, name.length()).contains(":") == true)
		{
			// entferne aus den hinteren teil die : zeichen und dersetzte durch
			// _
			name = name.substring(0, 2)
					+ name.substring(2, name.length()).replaceAll(":", "_");
			return name;

		} else
			return name;
	}

	public BufferedWriter WriteFileOpenAppend(String filename)
	{

		BufferedWriter ouf = null;
		FileOutputStream fouf = null;
		try
		{
			if (filename.contains(".gzip") == true)
			{
				// speichere komprimiert
				fouf = new FileOutputStream(filename, true);
				GZIPOutputStream gouf = new GZIPOutputStream(fouf);
				ouf = new BufferedWriter(new OutputStreamWriter(gouf, "UTF-8"));
			} else
			{
				// speichere im Klartext
				fouf = new FileOutputStream(convsonderz(filename), true);
				ouf = new BufferedWriter(new OutputStreamWriter(fouf, "UTF-8"));
			}
			return (ouf);

		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (null);
	}
	
	//Verzeichniss-Bearbeitung ********************
	public void initFileSystemList(String directoryPath, int flag)
	{// erstellt eine Liste aller Verzeichnissnamen f�r einen pfad
		// flag = 0 => liste der Verzeichnisse wird erstellt
		// flag = 1 => liste der Dateien wird erstellt
		int i = 0;
		verzpos = 0;
		threadverzliste.clear();
		maxentry = 0;
		File directoryFile = new File(convsonderz(directoryPath));
		if (directoryFile.exists())
		{
			File[] filesEntries = directoryFile.listFiles();
			maxthreadentrys_all_types = filesEntries.length;

			for (i = 0; i < maxthreadentrys_all_types; i++)
			{

				File fileEntry = filesEntries[i];

				if (fileEntry.exists() == true)
				{
					if ((fileEntry.isDirectory()) && (flag == 0))
					{
						/*
						 * System.out.println("Entering directory \"" +
						 * fileEntry + "\"...");
						 */
						threadverzliste.add(fileEntry);
						maxentry = maxentry + 1;
					} else if ((fileEntry.isFile()) && (flag == 1))
					{
						/*
						 * System.out.println("Entering File \"" + fileEntry +
						 * "\"...");
						 */
						threadverzliste.add(fileEntry);
						maxentry = maxentry + 1;
					}
				}
			}
		}
	}

	public void holeFileSystemNameReset()
	{

		verzpos = 0;
	}

	public String holeFileSystemName()
	{// Holt ein Namen (Verzeichnissnamen oder Filenamen)aus der Liste

		String name = null;
		if (verzpos == maxentry)
			return (null);

		if (threadverzliste.get(verzpos) != null)
		{
			name = threadverzliste.get(verzpos).toString();
			if (unixflag == 0)
				name = name.substring(name.lastIndexOf("\\") + 1);
			else
				name = name.substring(name.lastIndexOf("/") + 1);
		} else
			name = "0";
		verzpos++;
		return (name);
	}

	public int holeFileAnz()
	{
		return (maxentry);
	}
	/***********************************************/
}
