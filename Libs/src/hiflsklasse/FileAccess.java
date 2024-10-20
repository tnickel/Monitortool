package hiflsklasse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class FileAccess
{
	static private List<File> threadverzliste = new ArrayList<File>();
	
	static int verzpos = 0;
	static int maxthreadentrys_all_types = 0;
	static int maxentry = 0;
	static int unixflag = 0;
	
	public FileAccess()
	{
	}
	
	public static void genFile(String fname)
	{
		try
		{
			File f = new File(fname);
			if (f.exists() == false)
				f.createNewFile();
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	 public static boolean FileRename(String currentFileName, String newFileName) {
	        // Erstellen von File-Objekten f�r die alte und die neue Datei
	        File oldFile = new File(currentFileName);
	        File newFile = new File(newFileName);

	        // �berpr�fen, ob die alte Datei existiert
	        if (!oldFile.exists()) {
	            System.out.println("Die Datei " + currentFileName + " existiert nicht.");
	            Tracer.WriteTrace(10, "E: can�t rename because file don�t exists <"+currentFileName+">");
	            return false;
	        }

	        // Versuchen, die Datei umzubenennen
	        boolean renamed = oldFile.renameTo(newFile);
	        if (renamed) {
	            System.out.println("Die Datei wurde erfolgreich umbenannt.");
	        } else {
	            System.out.println("Fehler beim Umbenennen der Datei.");
	            Tracer.WriteTrace(10, "E: error in rename file from <"+currentFileName+"> to <"+newFileName+">");
	        }

	        return renamed;
	    }

	public static boolean checkOlderXDays(File file, int x)
	{
		// file:filenamen
		// x: x days
		// false: wenn file nicht �lter als x days
		// true: wenn file �lter als x days
		
		long diff = new Date().getTime() - file.lastModified();
		
		if (diff > x * 24 * 60 * 60 * 1000)
			return true;
		else
			return false;
	}
	
	public static int zeitdifferenz(String fnam1, String fnam2)
	{
		long st1 = existiertAnzStunden(fnam1);
		long st2 = existiertAnzStunden(fnam2);
		if ((st1 > 99999999) || (st2 > 99999999))
			return 99999999;
		else
			return (int) Math.abs((Math.abs(st1) - Math.abs(st2)));
	}
	
	public static long existiertAnzStunden(String fnam)
	{
		final long ONE_HOUR = 1000 * 60 * 60;
		File f = new File(fnam);
		if (f.exists() == false)
			return 999999999;
		if (f.length() == 0)
			return 999999999;
		long lastModified = f.lastModified();
		long currtime = System.currentTimeMillis();
		
		// ermittelt die Zeit in tagen seit dem letzten schreiben
		long timeSinceLastFileAccess = currtime - lastModified;
		long stunden = (timeSinceLastFileAccess / ONE_HOUR);
		return stunden;
	}
	
	static public String convsonderz(String name)
	{
		String retstr = null;
		
		if (unixflag == 1)
		{
			// "\" wird durch "/" ersetzt
			
			while (name.contains("\\") == true)
			{
				name = name.replace("\\", "/");
			}
			
		}
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
			Tracer.WriteTrace(10, "Error: double gzip error fname<" + name + ">");
			return null;
		}
		// I:\ sowas soll erlaubt sein, deswegen substiring von 2
		else if ((name.length() > 2) && (name.substring(2, name.length()).contains(":") == true))
		{
			// entferne aus den hinteren teil die : zeichen und dersetzte durch
			// _
			name = name.substring(0, 2) + name.substring(2, name.length()).replaceAll(":", "_");
			return name;
			
		} else
			return name;
	}
	
	static public BufferedWriter WriteFileOpenAppend(String filename)
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
	
	static public BufferedWriter WriteFileOpen(String filename, String coding)
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
				 * // speichere komprimiert ouf = new BufferedWriter(new OutputStreamWriter( new
				 * GZIPOutputStream(new FileOutputStream( convsonderz(filename))),"UTF-8"));
				 */
				fouf = new FileOutputStream(convsonderz(filename));
				GZIPOutputStream gouf = new GZIPOutputStream(fouf);
				if (coding == null)
					ouf = new BufferedWriter(new OutputStreamWriter(gouf));
				else
					ouf = new BufferedWriter(new OutputStreamWriter(gouf, coding));
			} else
			{
				fouf = new FileOutputStream(convsonderz(filename));
				// speichere im Klartext
				if (coding == null)
					ouf = new BufferedWriter(new OutputStreamWriter(fouf));
				else
					ouf = new BufferedWriter(new OutputStreamWriter(fouf, coding));
				/*
				 * ouf = new BufferedWriter(new FileWriter(convsonderz(filename)));
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
	
	static public BufferedWriter WriteFileOpenUnziped(String filename)
	{
		BufferedWriter ouf = null;
		FileOutputStream fouf = null;
		try
		{
			fouf = new FileOutputStream(filename);
			// speichere im Klartext
			ouf = new BufferedWriter(new OutputStreamWriter(fouf, "UTF-8"));
			
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
	
	static public void WriteFileClose(BufferedWriter ouf)
	{
		try
		{
			ouf.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static public void WriteFileZeile(BufferedWriter ouf, String zeile)
	{
		try
		{
			ouf.write(zeile);
			ouf.newLine();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static public BufferedReader ReadFileOpen(String filename)
	{
		BufferedReader inf = null;
		FileInputStream finp = null;
		
		String fn = null;
		filename = convsonderz(filename);
		
		try
		{
			if (filename.contains(".gzip") == true)
			{
				finp = new FileInputStream(filename);
				GZIPInputStream ginf = new GZIPInputStream(finp);
				inf = new BufferedReader(new InputStreamReader(ginf, "UTF-8"));
				
				// inf = new BufferedReader(new InputStreamReader(
				// new GZIPInputStream(new FileInputStream(filename))));
				
				return (inf);
			} else
			{
				finp = new FileInputStream(filename);
				// inf = new BufferedReader(new FileReader(filename));
				inf = new BufferedReader(new InputStreamReader(finp, "UTF-8"));
				return (inf);
			}
			
		} catch (FileNotFoundException e)
		{
			
			System.out.println("filename=" + filename);
			e.printStackTrace();
		} catch (IOException e)
		{
			if (finp != null)
				try
				{
					finp.close();
				} catch (IOException e2)
				{
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			
			String errormessage = null;
			System.out.println("filename=" + filename);
			errormessage = e.getMessage();
			if (errormessage.contains("Not in GZIP") == true)
			{
				System.out.println("Delete incorrect Zipfile");
				try
				{
					
					FileDelete(filename, 0);
					Thread.sleep(2000);
					return (null);
				} catch (InterruptedException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}
		return (null);
	}
	
	static public BufferedReader ReadFileOpen(String filename, String format)
	{
		// format="UTF-8"
		// oder format =null
		BufferedReader inf = null;
		FileInputStream finp = null;
		
		filename = convsonderz(filename);
		
		try
		{
			if ((filename.contains(".gzip") == true) || (filename.contains(".zip") == true)
					|| (filename.contains(".cfx") == true))
			{
				
				if (format != null)
					if (format.equals("UTF-8") == false)
						Tracer.WriteTrace(10, "Error: illegal Open-format <" + format + ">");
					
				finp = new FileInputStream(filename);
				GZIPInputStream ginf = new GZIPInputStream(finp);
				if (format == null)
					inf = new BufferedReader(new InputStreamReader(ginf));
				else
					inf = new BufferedReader(new InputStreamReader(ginf, format));
				return (inf);
			} else
			{
				finp = new FileInputStream(filename);
				if (format == null)
					inf = new BufferedReader(new InputStreamReader(finp, "UTF-8"));
				else
					inf = new BufferedReader(new InputStreamReader(finp, format));
				return (inf);
			}
			
		} catch (FileNotFoundException e)
		{
			
			System.out.println("filename=" + filename);
			e.printStackTrace();
		} catch (IOException e)
		{
			if (finp != null)
				try
				{
					finp.close();
				} catch (IOException e2)
				{
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			
			String errormessage = null;
			System.out.println("filename=" + filename);
			errormessage = e.getMessage();
			if (errormessage.contains("Not in GZIP") == true)
			{
				System.out.println("Delete incorrect Zipfile");
				try
				{
					
					FileDelete(filename, 0);
					Thread.sleep(2000);
					return (null);
				} catch (InterruptedException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}
		return (null);
	}
	
	static public void ReadFileClose(BufferedReader inf)
	{
		try
		{
			inf.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static public String ReadFileZeile(BufferedReader inf)
	{
		String zeile = null;
		try
		{
			zeile = inf.readLine();
			return (zeile);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (null);
	}
	
	static public boolean FileAvailable(String filename)
	{
		
		if (FileAvailable_(convsonderz(filename)) == true)
			return true;
		
		return false;
	}
	
	static public boolean FileAvailable0(String filename)
	{
		
		if (FileAvailable0_(convsonderz(filename)) == true)
			return true;
		
		return false;
	}
	
	static public boolean Rename(String oldfilename, String newfilename)
	{
		File fo1 = null, fo2 = null;
		
		fo1 = new File(convsonderz(oldfilename));
		fo2 = new File(convsonderz(newfilename));
		
		if (FileAccess.FileAvailable(newfilename) == true)
			FileAccess.FileDelete(newfilename, 0);
		
		fo1.renameTo(fo2);
		return true;
	}
	
	static private boolean FileAvailable_(String filename)
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
	
	static private boolean FileAvailable0_(String filename)
	{
		FileInputStream fi = null;
		
		try
		{
			fi = new FileInputStream(convsonderz(filename));
			
			if (fi.available() > 0)
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
	
	static public int FileLength(String filename)
	{
		File f = new File(filename);
		int len = (int) f.length();
		return (len);
	}
	
	static public String getVerzAttribLastModified(String verz)
	{
		
		File f = new File(verz);
		if (f.isDirectory() == false)
			Tracer.WriteTrace(10, "Error: internal Verzeichniss <" + verz + "> existiert nicht");
		
		GregorianCalendar x = new GregorianCalendar(TimeZone.getTimeZone("Europe/Berlin"));
		x.setTimeInMillis(f.lastModified());
		return ((x.getTime().toString()));
	}
	
	static public long getLastModifiedVerzSecs(String verz)
	{
		// ermittelt die zeit die vergangen ist seitdem das verzeichniss das
		// letzte mal modifiziert worden ist
		// die zeit wird in sekunden ermittelt
		
		File f = new File(verz);
		if (f.isDirectory() == false)
			Tracer.WriteTrace(10, "Error: internal Verzeichniss <" + verz + "> existiert nicht");
		
		long lastmodified = f.lastModified();
		
		Date aktdate = new Date();
		long akttime = aktdate.getTime();
		long diff = akttime - lastmodified; // in millisekunden
		
		long secs = (diff / 1000);
		return secs;
	}
	
	static public int calcHours(String filename)
	{
		// ermittelt das Alter in Stunden f�r ein File
		// return -99 falls file nicht existiert
		// ansonsten die Tage
		final long ONE_HOUR = 1000 * 60 * 60;
		File f = new File(filename);
		if (f.exists() == false)
			return 999999999;
		if (f.length() == 0)
			return 999999999;
		long lastModified = f.lastModified();
		long currtime = System.currentTimeMillis();
		
		// ermittelt die Zeit in tagen seit dem letzten schreiben
		long timeSinceLastFileAccess = currtime - lastModified;
		long stunden = (timeSinceLastFileAccess / ONE_HOUR);
		return (int) stunden;
	}
	
	static public Boolean CheckIsFileOlderHours(String filename, int x)
	// pr�ft ob ein file �lter als x stunden ist
	{
		/*
		 * final long ONE_HOUR = 1000 * 60 * 60; File f=new File(filename);
		 * 
		 * //falls das file existiert aber 0 bytes hat, dann ist was faul //file ist
		 * dann auf jeden Fall zu ald if(f.exists()==true) { if(f.length()<20) {
		 * Tracer.WriteTrace(20, "W:FileOlderCheck File <" + filename +
		 * "> exists but length <20 Bytes"); return true; } } //falls das file nicht
		 * existiert, dann ist es auf jeden fall zu alt if(f.exists()==false) return
		 * true;
		 * 
		 * long lastModified = f.lastModified(); long currtime =
		 * System.currentTimeMillis(); long timeSinceLastFileAccess = currtime -
		 * lastModified;
		 */
		int hours = calcHours(filename);
		/*
		 * if(timeSinceLastFileAccess >x*ONE_HOUR) return true; else return false;
		 */
		if (x == 0)
			return true;
		
		if (hours > x)
			return true;
		else
			return false;
		
	}
	
	static public boolean FileDelete(String filename, int errorstopflag)
	{
		
		File fo = null;
		
		fo = new File(convsonderz(filename));
		
		if (fo.exists() == false)
			return (false);
		
		if (fo.delete() == true)
			return (true);
		else
		{
			if (errorstopflag == 0)
				Tracer.WriteTrace(20, "W:can�t delete file <" + filename + ">");
			else
				Tracer.WriteTrace(10, "W:can�t delete file <" + filename + ">");
			return (false);
			
		}
	}
	static public boolean FileDelete2(String filename, int errorstopflag)
	{
		
		File fo = null;
		
		fo = new File(filename);
		
		if (fo.exists() == false)
			return (false);
		
		if (fo.delete() == true)
			return (true);
		else
		{
			if (errorstopflag == 0)
				Tracer.WriteTrace(20, "W:can�t delete file <" + filename + ">");
			else
				Tracer.WriteTrace(10, "W:can�t delete file <" + filename + ">");
			return (false);
			
		}
	}
	
	static public boolean FilesDelete(String directory, String postfix)
	{
		// l�scht alle Files mit einem bestimmten postfix in dem directory
		
		initFileSystemList(directory, 1);
		
		int anz = maxentry;
		for (int i = 0; i < anz; i++)
		{
			String nam = holeFileSystemName();
			if (nam.contains(postfix) == false)
				continue;
			
			String fnam = directory + "\\" + nam;
			if (FileAccess.FileAvailable0(fnam))
			{
				System.out.println("l�sche file <" + fnam + ">");
				FileAccess.FileDelete(fnam, 1);
			}
		}
		return true;
	}
	
	static public boolean FileMove(String qu, String zi)
	{
		File quelle = new File(qu);
		File ziel = new File(zi);
		
		if (ziel.exists() == true)
			ziel.delete();
		
		boolean flag = quelle.renameTo(ziel);
		if (flag == false)
		{
			Tracer.WriteTrace(20, "Error: can�t move file from<" + qu + "> to<" + zi + ">");
			
			return false;
		}
		return true;
	}
	
	static public boolean FileMoveBrokenPages(String fname)
	{
		String wasteverz = null;
		
		if (FileAvailable(fname) == false)
			return true;
		
		// transportiert das file nach brokenpages
		File fnam = new File(fname);
		String nam = fnam.getName();
		if ((fname.toUpperCase().contains("THREADS")) == true)
			wasteverz = "threads";
		else if ((fname.toUpperCase().contains("USERHTMLPAGES")) == true)
			wasteverz = "userhtmlpages";
		else if ((fname.toUpperCase().contains("THREADDATA")) == true)
			wasteverz = "threaddata";
		
		if (FileAvailable(GC.rootpath + "\\brokenpages\\" + wasteverz + "\\" + nam) == true)
			FileDelete(GC.rootpath + "\\brokenpages\\" + wasteverz + "\\" + nam, 0);
		
		FileMove(fname, GC.rootpath + "\\brokenpages\\" + wasteverz + "\\" + nam);
		
		return true;
	}
	
	static public boolean FileMoveBadPages(String fname)
	{
		
		// Tansportiert ein File zum unterverzeichnis bad
		// Bsp: offline\downloades\threads\name.db
		// nach offline\downloades\threads\bad\name.db
		
		File fnam = new File(fname);
		String nam = fnam.getName();
		String zielname = null;
		
		if (FileAccess.FileAvailable(fname) == false)
		{
			Tracer.WriteTrace(20, "I: file <" + fname + "> not available, can�t move  to bad page, ");
			return true;
		}
		if (fname.contains("\\") == false)
			Tracer.WriteTrace(20, "E:bad Filename <" + fname + ">");
		
		zielname = fname.substring(0, fname.lastIndexOf("\\"));
		zielname = zielname + "\\bad\\" + nam;
		
		if (FileAvailable(zielname))
			FileDelete(zielname, 0);
		
		if (FileMove(fname, zielname) == false)
			Tracer.WriteTrace(10, "E:Error copy file <" + fname + "> nach <" + zielname + ">");
		
		return true;
	}
	
	static public void initFileSystemList(String directoryPath, int flag)
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
						 * System.out.println("Entering directory \"" + fileEntry + "\"...");
						 */
						threadverzliste.add(fileEntry);
						maxentry = maxentry + 1;
					} else if ((fileEntry.isFile()) && (flag == 1))
					{
						/*
						 * System.out.println("Entering File \"" + fileEntry + "\"...");
						 */
						threadverzliste.add(fileEntry);
						maxentry = maxentry + 1;
					}
				}
			}
		}
	}
	
	static public void holeFileSystemNameReset()
	{
		
		verzpos = 0;
	}
	
	static public String holeFileSystemName()
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
	
	static public int holeFileAnz()
	{
		return (maxentry);
	}
	
	static public boolean checkgenDirectory(String directory)
	{
		// true wenn verzeichniss schon da war
		// false wenn verzeichniss erstellt wurde
		
		// �berpr�ft auf das vorhandensein eines Verzeichnisses
		// Wenn es nicht da ist wird das Verzeichniss generiert
		File f = new File(directory);
		if (f.isDirectory())
		{
			// System.out.println("Verzeichnis <" + directory
			// + "> vorhanden");
			return true;
		} else
		{
			f.mkdir();
			if (f.isDirectory() == false)
				Tracer.WriteTrace(10,
						"Error: fnam<" + f.getAbsolutePath() + "> konnte nicht erzeugt werden Schreibschutz !!!???");
			return false;
		}
	}
	
	static public boolean checkDirectory(String directory)
	{ // �berpr�ft auf das vorhandensein eines Verzeichnisses
		
		File f = new File(directory);
		if (f.isDirectory())
		{
			return true;
		} else
			return false;
	}
	
	public static void deleteDirectory(File dir)
	{
		File[] files = dir.listFiles();
		if (files != null)
		{
			for (int i = 0; i < files.length; i++)
			{
				if (files[i].isDirectory())
				{
					deleteDirectory(files[i]); // Verzeichnis leeren und
												// anschlie�end l�schen
				} else
				{
					files[i].delete(); // Datei l�schen
				}
			}
			dir.delete(); // Ordner l�schen
		}
	}
	
	public static void cleanDirectory(File dir)
	{
		File[] files = dir.listFiles();
		if (files != null)
		{
			for (int i = 0; i < files.length; i++)
			{
				if (files[i].isDirectory())
				{
					deleteDirectory(files[i]); // Verzeichnis leeren
				} else
				{
					files[i].delete(); // Datei l�schen
				}
			}
			
		}
	}
	
	public static void deleteDirectoryContent(File dir)
	{
		File[] files = dir.listFiles();
		if (files != null)
		{
			for (int i = 0; i < files.length; i++)
			{
				if (files[i].isDirectory())
				{
					deleteDirectory(files[i]); // Verzeichnis leeren und
												// anschlie�end l�schen
				} else
				{
					files[i].delete(); // Datei l�schen
				}
			}
			
		}
	}
	
	public static void deleteDirectoryContentPostfix(File dir, String postfix)
	{
		// loescht alle files innerhalb eines verzeichnisses die den postfix haben
		File[] files = dir.listFiles();
		if (files != null)
		{
			for (int i = 0; i < files.length; i++)
			{
				File fnam = files[i];
				if (fnam.getPath().endsWith(postfix))
				{
					files[i].delete(); // Datei l�schen
				} else
					Tracer.WriteTrace(20,
							"E:cant delete file <" + fnam.getName() + "> only postfix <" + postfix + "> is allowed");
			}
			
		}
	}
	
	public static int countDirectoryContent(File dir)
	{
		
		File[] files = dir.listFiles();
		return files.length;
		
	}
	
	public static boolean copyFile2_dep(String srcfilename, String destfilename)
	{
		
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try
		{
			
			try
			{
				inputChannel = new FileInputStream(new File(srcfilename)).getChannel();
				outputChannel = new FileOutputStream(new File(destfilename)).getChannel();
				outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
			} catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				Tracer.WriteTrace(20, "file not found 45");
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				Tracer.WriteTrace(20, "io exception 4454545");
			}
			
		} finally
		{
			try
			{
				if (inputChannel != null)
					inputChannel.close();
				if (outputChannel != null)
					outputChannel.close();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				Tracer.WriteTrace(20, "io exception 44445444545");
			}
			
		}
		return true;
	}
	
	static public boolean copyFile(String quelle, String ziel)
	{
		quelle = convsonderz(quelle);
		ziel = convsonderz(ziel);
		
		Path sourcePath = Paths.get(quelle);
		Path destinationPath = Paths.get(ziel);
		
		try
		{
			Files.copy(sourcePath, destinationPath);
			return true;
			
		} catch (IOException e)
		{
			Tracer.WriteTrace(10,"E:Fehler beim Kopieren der Datei: " + e.getMessage());
			return false;
		}
		
	}
	
	static public boolean copyFile3(String quelle, String ziel, int laengenvergleich)
	{
		File source = new File(convsonderz(quelle));
		File dest = new File(convsonderz(ziel));
		
		if ((laengenvergleich == 1) && (dest.exists()))
		{
			long len1 = source.length();
			long len2 = dest.length();
			if (len1 == len2)
				return true;
			
			if (dest.delete() == false)
				Tracer.WriteTrace(10, "E:cant delete destfile<" + dest.getAbsolutePath() + ">");
		}
		
		FileReader fileReader = null;
		FileWriter fileWriter = null;
		try
		{
			fileReader = new FileReader(source);
			fileWriter = new FileWriter(dest);
			long length = source.length();
			for (int i = 0; i < length; i++)
				fileWriter.write(fileReader.read());
			
			fileWriter.close();
			fileReader.close();
			
			return true;
		} catch (IOException e)
		{
			
			e.printStackTrace();
			return (false);
		}
		
	}
	
	static public boolean MatchFiles(String quelle1, String quelle2, String ziel, Boolean delflag)
	{
		// f�gt die beiden files1 und file2 zu dem neuen File zusammen
		// falls delflag == true dann l�sche die beiden quellfiles
		File source1 = new File(convsonderz(quelle1));
		File source2 = new File(convsonderz(quelle2));
		File dest = new File(convsonderz(ziel));
		FileReader fileReader1 = null;
		FileReader fileReader2 = null;
		FileWriter fileWriter = null;
		try
		{
			fileReader1 = new FileReader(source1);
			fileReader2 = new FileReader(source2);
			fileWriter = new FileWriter(dest);
			for (int i = 0; i < source1.length(); i++)
				fileWriter.write(fileReader1.read());
			
			fileReader1.close();
			for (int i = 0; i < source2.length(); i++)
				fileWriter.write(fileReader2.read());
			
			fileReader2.close();
			fileWriter.close();
			
			if (delflag == true)
			{
				if (FileAvailable0(quelle1))
					FileDelete(quelle1, 1);
				if (FileAvailable0(quelle2))
					FileDelete(quelle2, 1);
			}
			
			return true;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return (false);
		}
	}
	
	static public void ConcatFiles(String infile, String inoutfile)
	{
		// f�gt infile + inoutfile zu einem gemeinsamen inoutfile zusammen
		String tmpfile = GC.rootpath + "\\tmp\\concat.txt";
		MatchFiles(infile, inoutfile, tmpfile, false);
		Rename(tmpfile, inoutfile);
		FileDelete(tmpfile, 0);
		
	}
	
	static public String getFilenameEnd(String fnam)
	{
		if (unixflag == 0)
			return fnam.substring(fnam.lastIndexOf("\\"));
		else
			return fnam.substring(fnam.lastIndexOf("/"));
	}
	
	static public Boolean CheckFileKeyword(String fnam, String keyword, int keineTeilwoerterflag)
	{
		
		if (FileAccess.FileAvailable(fnam) == false)
			return false;
		File f = new File(fnam);
		
		Inf inf = new Inf();
		inf.setFilename(fnam);
		long glen = f.length();
		
		String mem = inf.readMemFile(glen);
		inf.close();
		
		if (mem == null)
			return false;
		
		if (keineTeilwoerterflag == 0)
		{
			// Es wird auch bei Teilwoertern positiv zur�ckgemeldet
			// Bsp: gesucht wird nike
			// panikel wird bei flag=0 als True gemeldet
			// pa(nike)l
			if (mem.toLowerCase().contains(keyword.toLowerCase()) == false)
				return false;
			else
				return true;
		} else
		{
			mem = mem.toLowerCase();
			keyword = keyword.toLowerCase();
			
			if ((mem.contains(keyword + " ") == true) && (mem.contains(" " + keyword) == true))
				return true;
			
			return false;
		}
	}
	
	static public String GetFileKeywordStrings(String fnam, String keyword, int keineTeilwoerterflag, int maxlen)
	{
		String foundstrings = new String("");
		// schaut nach ob das keyword in dem file "fnam" vorhanden ist
		// wenn ja werden alle Teilstrings einer maximalen laenge mit dem
		// keyword gesammelt
		
		// falls nix drin ist
		if (FileAccess.FileAvailable(fnam) == false)
			return null;
		
		File f = new File(fnam);
		
		Inf inf = new Inf();
		inf.setFilename(fnam);
		long glen = f.length();
		
		// den ganzen Text in den Speicher mem einlesen
		String mem = inf.readMemFile(glen);
		inf.close();
		
		// Alles in kleinschrift umwandeln
		mem = mem.toLowerCase();
		
		keyword = keyword.toLowerCase();
		int keywlen = keyword.length();
		
		if (mem == null)
			return null;
		
		int index = 0;
		int spos = 0;
		
		// nix drin
		if (mem.contains(keyword) == false)
			return null;
		
		// ermittle das n�chste Vorkommen
		while ((spos = mem.indexOf(keyword, index)) > 0)
		
		{
			glen = mem.length();
			int boundlinks = spos - maxlen;
			int boundrechts = spos + keywlen + maxlen;
			
			if (boundlinks < 0)
				boundlinks = 0;
			if (boundrechts >= glen)
				boundrechts = (int) glen - 1;
			
			String foundstring = mem.substring(boundlinks, boundrechts);
			
			// mache das alte keyword kaputt
			mem = mem.replaceFirst(keyword, "XXXXX");
			
			// falls keine Teilw�rter erlaubt sind muss vor oder danach ein
			// lehrzeichen sein
			if (keineTeilwoerterflag == 1)
				if ((foundstring.contains(keyword + " ") == false) && (foundstring.contains(" " + keyword) == false))
					continue;
				
			// speichere alles incl. maxlen-bytes davor und danach
			foundstrings = foundstrings.concat("@" + foundstring);
			
			// suche einen weiter
			index = spos + 1;
			if (index >= glen)
				break;
		}
		
		return foundstrings;
		
		/*
		 * if (keineTeilwoerterflag == 0) { // Es wird auch bei Teilwoertern positiv
		 * zur�ckgemeldet // Bsp: gesucht wird nike // panikel wird bei flag=0 als True
		 * gemeldet // pa(nike)l if (mem.toLowerCase().contains(keyword.toLowerCase())
		 * == false) return false; else return true; } else { mem = mem.toLowerCase();
		 * keyword = keyword.toLowerCase();
		 * 
		 * if(mem.contains(keyword+" ")==true) return true;
		 * if(mem.contains(" "+keyword)==true) return true;
		 * 
		 * 
		 * return false; }
		 */
	}
	
	static public void CopyDirectory(String quellverz, String zielverz, String postfix)
	{
		// kopiert das quellverz ins zielverzeichniss
		// falls postfix != null, werden nur files mit dem Postfix kopiert
		initFileSystemList(quellverz, 1);
		int anz = holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String fnam = holeFileSystemName();
			
			if (postfix != null)
				if (fnam.contains(postfix) == false)
					continue;
				
			copyFile2_dep(quellverz + "\\" + fnam, zielverz + "\\" + fnam);
			
		}
	}
	
	static public void CopyDirectory3(String quellverz, String zielverz, String postfix, int laengenvergleich)
	{
		// kopiert das quellverz ins zielverzeichniss
		// falls postfix != null, werden nur files mit dem Postfix kopiert
		initFileSystemList(quellverz, 1);
		int anz = holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String fnam = holeFileSystemName();
			
			if (postfix != null)
				if (fnam.contains(postfix) == false)
					continue;
				
			copyFile3(quellverz + "\\" + fnam, zielverz + "\\" + fnam, 1);
			
		}
	}
	
	public static void CopyDirectory4(File src, File dest) throws IOException
	// copy directory and all subdirectorys
	{
		if (!src.exists())
		{
			
			Tracer.WriteTrace(10, "Directory does not exist.<" + src.getPath() + ">");
			// just exit
			System.exit(0);
		}
		if (src.isDirectory())
		{
			
			// if directory not exists, create it
			if (!dest.exists())
			{
				dest.mkdir();
				System.out.println("Directory copied from " + src + "  to " + dest);
			}
			
			// list all the directory contents
			String files[] = src.list();
			
			for (String file : files)
			{
				// construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// recursive copy
				CopyDirectory4(srcFile, destFile);
			}
			
		} else
		{
			// if file, then copy it
			// Use bytes stream to support all file types
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);
			
			byte[] buffer = new byte[1024];
			
			int length;
			// copy the file content in bytes
			while ((length = in.read(buffer)) > 0)
			{
				out.write(buffer, 0, length);
			}
			
			in.close();
			out.close();
			System.out.println("File copied from " + src + " to " + dest);
		}
	}
	
	static public void FileReplaceString(String filename, String vorher, String nachher)
	{
		
		String quellfile = filename;
		String zielfiletmp = quellfile + ".tmp";
		
		Inf i1 = new Inf();
		i1.setFilename(quellfile);
		Inf i2 = new Inf();
		i2.setFilename(zielfiletmp);
		
		while (5 == 5)
		{
			String zeile = i1.readZeile();
			if (zeile == null)
				break;
			
			if (zeile.contains(vorher))
			{
				zeile = zeile.replace(vorher, nachher);
				
			}
			i2.writezeile(zeile);
		}
		i1.close();
		i2.close();
		
		if (new File(filename).exists())
			new File(filename).delete();
		
		copyFile(zielfiletmp, filename);
		FileAccess.FileDelete(zielfiletmp, 0);
	}
	
	static public void FileReplaceString2(String filePath, String searchString, String replacementString)
			throws IOException
	{
		
		File file = new File(filePath);
		File tempFile = new File(file.getAbsolutePath() + ".tmp");
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file));
				BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile)))
		{
			
			String line;
			while ((line = reader.readLine()) != null)
			{
				line = line.replace(searchString, replacementString);
				writer.write(line + "\n");
			}
		}
		
		if (file.delete())
		{
			if (!tempFile.renameTo(file))
			{
				throw new IOException("Konnte tempor�re Datei nicht in Originaldatei umbenennen.");
			}
		} else
		{
			throw new IOException("Konnte Originaldatei nicht l�schen.");
		}
	}
	
	static public boolean FileContainString(String filename, String suchtext)
	{
		String quellfile = filename;
		
		Inf i1 = new Inf();
		i1.setFilename(quellfile);
		
		while (5 == 5)
		{
			String zeile = i1.readZeile();
			if (zeile == null)
				break;
			
			if (zeile.contains(suchtext))
			{
				i1.close();
				return true;
			}
		}
		i1.close();
		return false;
		
	}
	
}
