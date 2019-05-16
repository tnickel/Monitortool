package wartung;

import hilfsklasse.FileAccess;
import hilfsklasse.SG;
import hilfsklasse.ToolsException;

import java.io.BufferedReader;
import java.io.IOException;

import mainPackage.GC;

public class TransferWebseiten
{
	// die webseiten die als letztes geladen wurden werden nach
	// export exportiert

	public TransferWebseiten()
	{
	}

	public void export()
	{
		// alle Webseiten nach export kopieren

		BufferedReader inf;
		String quelle = null, ziel = null, file_str = null;
		try
		{
			inf = FileAccess.ReadFileOpen(FileAccess.convsonderz(GC.rootpath
					+ "\\db\\downloadinfo.txt"));

			String zeile = "";

			// Kopiere die Webseiten
			while ((zeile = inf.readLine()) != null)
			{

				// System.out.println("zeile="+zeile);
				try
				{
					file_str = SG.nteilstring(zeile, "#", 2);
				} catch (ToolsException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// e:\offline\Threads\BluePearlMining\BluePearlMining_8031.html.gzip
				quelle = file_str;
				ziel = file_str.substring(file_str.lastIndexOf("\\"));
				FileAccess.copyFile(quelle, GC.rootpath + "\\export\\webpage\\"
						+ ziel);
			}
			// Kopiere die Admin-files
			FileAccess.copyFile(GC.rootpath + "\\db\\aktdb.db", GC.rootpath
					+ "\\export\\aktdb.db");
			FileAccess.copyFile(GC.rootpath + "\\db\\threads.db", GC.rootpath
					+ "\\export\\threads.db");
			FileAccess.copyFile(GC.rootpath + "\\db\\userstore.db", GC.rootpath
					+ "\\export\\userstore.db");
			FileAccess.copyFile(GC.rootpath + "\\db\\downloadinfo.txt",
					GC.rootpath + "\\export\\downloadinfo.txt");

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
