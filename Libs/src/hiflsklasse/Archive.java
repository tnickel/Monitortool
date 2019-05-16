package hiflsklasse;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class Archive
{
	private ZipFile z = null;

	private static final byte[] buffer = new byte[0xFFFF];

	public Archive()
	{
	}

	private static void extractEntry(ZipFile zf, ZipEntry entry, String destDir)
			throws IOException
	{
		// interne Hilfsfunktion
		File file = new File(destDir, entry.getName());

		if (entry.isDirectory())
			file.mkdirs();
		else
		{
			new File(file.getParent()).mkdirs();

			InputStream is = null;
			OutputStream os = null;

			try
			{
				is = zf.getInputStream(entry);
				os = new FileOutputStream(file);

				for (int len; (len = is.read(buffer)) != -1;)
					os.write(buffer, 0, len);
			} finally
			{
				os.close();
				is.close();
			}
		}
	}

	public static void unzip(File file, File archiveFile, String destdir)
	{
		// extrahiert ein file aus einem archive

		try
		{
			ZipFile zipSrc = new ZipFile(archiveFile);

			Enumeration<? extends ZipEntry> srcEntries = zipSrc.entries();
			while (srcEntries.hasMoreElements())
			{
				// betrachte den aktuellen Eintrag
				ZipEntry entry = srcEntries.nextElement();
				if (entry.getName().equalsIgnoreCase(file.getName()) == true)
				{
					// gefunden !!!
					extractEntry(zipSrc, entry, destdir);
					break;
				}
			}

			zipSrc.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void zipAddFile(File file, File archiveFile)
	{
		// nimmt ein neues Element in das Ziparchive auf, falls es schon da ist
		// wird das alte File durch das neue ersetzt
		// Um ein Archive um ein File zu erweitern muss erst das alte archive
		// gelesen werden und das neue File dann angehangen werden
		File tmpFile = new File(String.valueOf(System.currentTimeMillis()));
		try
		{
			ZipFile zipSrc = new ZipFile(archiveFile);

			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(
					tmpFile));
			Enumeration<? extends ZipEntry> srcEntries = zipSrc.entries();
			while (srcEntries.hasMoreElements())
			{
				// betrachte den aktuellen Eintrag
				ZipEntry entry = srcEntries.nextElement();
				if (entry.getName().equalsIgnoreCase(file.getName()) == true)
					continue;

				// neuen Entry erzeugen
				ZipEntry newEntry = new ZipEntry(entry.getName());
				zos.putNextEntry(newEntry);

				// Die Daten anhängen
				BufferedInputStream bis = new BufferedInputStream(
						zipSrc.getInputStream(entry));

				while (bis.available() > 0)
				{
					zos.write(bis.read());
				}
				zos.closeEntry();
				bis.close();
			}

			ZipEntry newEntry = new ZipEntry(file.getName());
			zos.putNextEntry(newEntry);

			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			while (bis.available() > 0)
			{
				zos.write(bis.read());
			}
			zos.closeEntry();
			zos.finish();
			zos.close();
			zipSrc.close();
			archiveFile.delete();
			tmpFile.renameTo(archiveFile);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	static public BufferedReader zipGetStream(File file, File archiveFile)
	{
		try
		{
			ZipFile zipSrc = new ZipFile(archiveFile);

			Enumeration<? extends ZipEntry> srcEntries = zipSrc.entries();
			while (srcEntries.hasMoreElements())
			{
				// betrachte den aktuellen Eintrag
				ZipEntry entry = srcEntries.nextElement();
				if (entry.getName().equalsIgnoreCase(file.getName()) == true)
				{
					BufferedReader bu = new BufferedReader(
							new InputStreamReader(zipSrc.getInputStream(entry),
									"UTF-8"));
					return bu;
				}
			}

			zipSrc.close();
			return null;

		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static boolean zipisFileinArchive(File file, File archiveFile)
	{
		// überprüft ob ein file in einem zipverzeichniss eigepackt ist
		ZipFile z = null;
		try
		{
			z = new ZipFile(archiveFile);
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (Enumeration<? extends ZipEntry> e = z.entries(); e
				.hasMoreElements();)
		{
			ZipEntry ze = e.nextElement();
			// System.out.println(ze.getName());
			if (ze.getName().equalsIgnoreCase(file.getName()))
				return true;
		}
		return false;
	}

	public void GenZipArchive_depricated(String ziparchive, String filename)
	{
		int read = 0;
		FileInputStream in;
		byte[] data = new byte[100000];

		ZipOutputStream out;
		try
		{
			out = new ZipOutputStream(new FileOutputStream(ziparchive));

			// Archivierungs-Modus setzen
			out.setMethod(ZipOutputStream.DEFLATED);

			// Hinzufügen der einzelnen Einträge

			try
			{
				System.out.println(filename);
				// Eintrag für neue Datei anlegen
				ZipEntry entry = new ZipEntry("zip2");
				in = new FileInputStream(filename);
				// Neuer Eintrag dem Archiv hinzufügen
				out.putNextEntry(entry);

				// Hinzufügen der Daten zum neuen Eintrag
				while ((read = in.read(data, 0, 10000)) != -1)
					out.write(data, 0, read);
				out.closeEntry(); // Neuen Eintrag abschließen
				in.close();
				out.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	static public boolean gzipFileFast(String filename)
	{

		OutputStream os = null;
		InputStream is = null;
		byte[] buffer = new byte[8192];
		try
		{
			os = new GZIPOutputStream(new FileOutputStream(filename + ".gzip"));
			is = new FileInputStream(filename);

			for (int length; (length = is.read(buffer)) != -1;)
				os.write(buffer, 0, length);
		} catch (IOException e)
		{
			System.err.println("Fehler: Kann nicht packen " + filename);
		} finally
		{
			if (is != null)
				try
				{
					is.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			if (os != null)
				try
				{
					os.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
		}
		return true;
	}

	static public boolean gzipFile(String filename)
	{
		// zippt das file=filename
		BufferedReader in = FileAccess.ReadFileOpen(FileAccess
				.convsonderz(filename));
		BufferedWriter out = FileAccess.WriteFileOpen(
				FileAccess.convsonderz(filename + ".gzip"), "UTF-8");
		File source = new File(FileAccess.convsonderz(filename));
		String tmp = "";

		for (int i = 0; i < source.length(); i++)
			try
			{
				out.write(in.read());
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return (false);
			}
		FileAccess.ReadFileClose(in);
		FileAccess.WriteFileClose(out);
		return true;
	}

	public boolean gzipUnzipFile(String filename)
	{

		// zeigt an das beide filenamen gleich sind

		BufferedReader inf = null;
		BufferedWriter ouf = null;
		String outfilename = null;

		if (filename.contains(".gzip") == false)
			Tracer.WriteTrace(10, "file <" + filename + "> not zipped");

		inf = FileAccess.ReadFileOpen(filename);
		outfilename = filename.substring(0, filename.lastIndexOf(".gzip"));

		String zeile = "";
		try
		{
			while ((zeile = inf.readLine()) != null)
			{
				Tracer.WriteTrace(50, this.getClass().getSimpleName()
						+ "zeile=<" + zeile + ">");

				if (ouf == null)
					ouf = FileAccess.WriteFileOpen(outfilename, "UTF-8");
				ouf.write(zeile);
			}

			inf.close();
			if (ouf != null)
				ouf.close();

			return true;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Tracer.WriteTrace(10, "Error: decompression error <" + filename
					+ "> ");
			return false;
		}
	}

	public static Boolean generateZipFile(ArrayList<String> sourcesFilenames,
			String destinationDir, String zipFilename)
	{
		// Create a buffer for reading the files
		byte[] buf = new byte[1024];

		File zfile= new File(zipFilename);
		if(zfile.exists())
			zfile.delete();
		
		try
		{
			// VER SI HAY QUE CREAR EL ROOT PATH
			boolean result = (new File(destinationDir)).mkdirs();

			String zipFullFilename = destinationDir + "/" + zipFilename;

			System.out.println(result);

			// Create the ZIP file
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					zipFullFilename));
			// Compress the files
			for (String filename : sourcesFilenames)
			{
				FileInputStream in = new FileInputStream(filename);
				File file = new File(filename); // "Users/you/image.jpg"

				//subdir ist der oberverzeichniss z.b. experts bei ....\experts\xxx.mq4
				String subdir = file.getParent();
				subdir=subdir.substring(subdir.lastIndexOf("\\")+1);
				out.putNextEntry(new ZipEntry(subdir + "\\" + file.getName())); // "image.jpg"
				// Transfer bytes from the file to the ZIP file
				int len;
				while ((len = in.read(buf)) > 0)
				{
					out.write(buf, 0, len);
				}
				// Complete the entry
				out.closeEntry();
				in.close();
			} // Complete the ZIP file
			out.close();

			return true;
		} catch (IOException e)
		{
			System.out.println(e.toString());
			return false;
		}
	}
	public static Boolean generateZipFile2(ArrayList<String> sourcesFilenames,
			String destinationDir, String zipFilename)
	{
		// Create a buffer for reading the files
		byte[] buf = new byte[1024];

		File zfile= new File(zipFilename);
		if(zfile.exists())
			zfile.delete();
		
		try
		{
			// VER SI HAY QUE CREAR EL ROOT PATH
			boolean result = (new File(destinationDir)).mkdirs();

			String zipFullFilename = destinationDir + "/" + zipFilename;

			System.out.println(result);

			// Create the ZIP file
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					zipFullFilename));
			// Compress the files
			for (String filename : sourcesFilenames)
			{
				FileInputStream in = new FileInputStream(filename);
				File file = new File(filename); // "Users/you/image.jpg"

				//subdir ist der oberverzeichniss z.b. experts bei ....\experts\xxx.mq4
				String subdir = file.getParent();
				subdir=subdir.substring(subdir.indexOf("\\")+1);
				out.putNextEntry(new ZipEntry(subdir + "\\" + file.getName())); // "image.jpg"
				// Transfer bytes from the file to the ZIP file
				int len;
				while ((len = in.read(buf)) > 0)
				{
					out.write(buf, 0, len);
				}
				// Complete the entry
				out.closeEntry();
				in.close();
			} // Complete the ZIP file
			out.close();

			return true;
		} catch (IOException e)
		{
			System.out.println(e.toString());
			return false;
		}
	}
}
