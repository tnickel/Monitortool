package wartung;

import internetPackage.DownloadManager;
import mainPackage.GC;

public class SammleUserLangeOnline
{
	// sammelt die onlineinformation über die user
	// möchte feststellen welche user oft und lange online sind

	public void downloadOnlineliste()
	{
		DownloadManager dm = new DownloadManager(GC.MAXLOW);

		// String filename = GC.rootpath + "\\downloaded\\onlineliste\\" +
		// username
		// + "_postings" + ".html";
		String webnam = "http://www.wallstreet-online.de/dyn/userzentrum/online.html?filter=27";

		// int status = dm.DownloadHtmlPage(webnam, filename, 0, 50000, 1, 1,
		// 0);

		/*
		 * UserhtmlpagesPostings uhtmlpost = new
		 * UserhtmlpagesPostings(filename); // hole naechstes posting while
		 * (uhtmlpost.getNextListeneintrag() != null)
		 * 
		 * }
		 */
	}
}
