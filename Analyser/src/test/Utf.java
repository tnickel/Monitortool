package test;

import internetPackage.DownloadManager;
import mainPackage.GC;

public class Utf
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		DownloadManager dm = new DownloadManager(GC.MAXLOW);
		String filename = GC.rootpath + "\\tmp\\webseite" + ".html";
		String webnam = "http://www.wallstreet-online.de/diskussion/1029640-2041-2050/biophan-sekt-oder-selters#neuster_beitrag";
		int status = dm.DownloadHtmlPage(webnam, filename, 0, 50000, 1, 1, 1);
	}

}
