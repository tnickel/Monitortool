package hilfsrepeattask;

import hilfsklasse.FileAccess;
import html.Signalpage;
import internetPackage.DownloadManager;
import mainPackage.GC;
import objects.ThreadDbObj;
import stores.SignaleDBzeile;
import stores.ThreadsDB;

public class SammleSignale
{
	static DownloadManager Wpage = new DownloadManager(GC.MAXLOW);

	public void start()
	{
		String sigstring = null;
		ThreadsDB tdb = new ThreadsDB();
		ThreadDbObj tdbo = null;
		int anz = tdb.GetanzObj();
		int i = 1;

		tdb.ResetDB(-1);
		while ((tdbo = (ThreadDbObj) tdb.GetNextObject()) != null)
		{
			System.out.println("Signal (" + i + "|" + anz + ")");
			i++;
			String tname = tdbo.getThreadname();
			//int masterid = tdbo.getMasterid();
			String masterstring=tdbo.getMasterstring();
			SignaleDBzeile sigdb = new SignaleDBzeile(tname);
			// http://aktien.wallstreet-online.de/16080/chartsignale.html
			String urlstr = "http://aktien.wallstreet-online.de/" + masterstring
					+ "/chartsignale.html";
			String fname = GC.rootpath + "\\tmp\\chartsignal.html";
			String fnamz = fname + ".gzip";

			if (FileAccess.FileAvailable(fnamz))
				FileAccess.FileDelete(fnamz,0);

			Wpage.DownloadHtmlPage(urlstr, fnamz, 0, 50000, 1, 0, 0);
			Signalpage sigp = new Signalpage(fnamz);
			while ((sigstring = sigp.GetNextSignalString()) != null)
			{
				sigdb.UpdateZeile(sigstring, sigstring.length());

			}
			sigdb.WriteDB();
		}

	}
}
