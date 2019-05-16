package html;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Tracer;
import internetPackage.DownloadManager;
import mainPackage.GC;

public class HtmlMeistDiskutiert extends HtmlCore
{
	private String zeile;

	public HtmlMeistDiskutiert()
	{

	}

	public void DownloadPage()
	{
		String zieldatei = GC.rootpath + "\\tmp\\meistdiskutiert.html";
		String quellpath = "http://www.wallstreet-online.de/statistik/top-aktien-meistdiskutiert";

		if (FileAccess.existiertAnzStunden(zieldatei) > 1)
		{	
			//lade nur neu falls das file zu alt
			if (FileAccess.FileAvailable(zieldatei))
				FileAccess.FileDelete(zieldatei, 0);

			DownloadManager dm = new DownloadManager(1);
			dm.DownloadHtmlPage(quellpath, zieldatei, 1, 50000, 1, 1, 1);
		}
		if (FileAccess.FileLength(zieldatei) < 20000)
			Tracer.WriteTrace(10, "Wo-Fehler Seite zu kurz seite<" + quellpath
					+ "> len<" + FileAccess.FileLength(zieldatei) + ">");

		super.ReadHtmlPage(zieldatei);
	}

	public void ErstelleConfigMeistDiskutiert()
	{
		Inf inf = new Inf();
		int anz=0;
		String fnam=GC.rootpath
				+ "\\conf\\boersenblaetter\\Configmeistdiskutiert.txt";
		inf.setFilename(fnam);

		if(FileAccess.FileAvailable(fnam))
			FileAccess.FileDelete(fnam, 0);
		
		String zeile = "";
		while ((zeile = getNextListeneintrag()) !=null)
		{
			if(anz>=50)
				break;
			String suchwort = zeile.substring(zeile.indexOf("\"   >")+"\"   >".length(),
					zeile.indexOf("</a><div"));
			inf.writezeile("Suchwort:" + suchwort);
			inf.writezeile(suchwort);
			anz++;
		}
		inf.close();

	}

	private String getNextListeneintrag()
	{
		zeile = super.GetFullHtmlMeistDiskutiertPosting();
		// falls am ende der Threadpostliste
		if (zeile.equals("0") == true)
			return null;
		return zeile;
	}

}
