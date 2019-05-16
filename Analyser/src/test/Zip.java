package test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip
{

	public static void main(String args[])
	{
		PrintWriter stdout = new PrintWriter(System.out, true);
		int read = 0;
		FileInputStream in;
		byte[] data = new byte[1024];
		try
		{
			// Zip-Archiv mit Stream verbinden
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					args[0]));
			// Archivierungs-Modus setzen
			out.setMethod(ZipOutputStream.DEFLATED);
			// Hinzuf�gen der einzelnen Eintr�ge
			for (int i = 1; i < args.length; i++)
			{
				try
				{
					stdout.println(args[i]);
					// Eintrag f�r neue Datei anlegen
					ZipEntry entry = new ZipEntry(args[i]);
					in = new FileInputStream(args[i]);
					// Neuer Eintrag dem Archiv hinzuf�gen
					out.putNextEntry(entry);
					// Hinzuf�gen der Daten zum neuen Eintrag
					while ((read = in.read(data, 0, 1024)) != -1)
						out.write(data, 0, read);
					out.closeEntry(); // Neuen Eintrag abschlie�en
					in.close();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			out.close();
		} catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

}