package objects;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;

public class Htmlpage
{
	private String filename_glob = null;
	private Inf inf = new Inf();

	public Htmlpage()
	{
	}

	public Htmlpage(String fnam)
	{
		filename_glob = fnam;
		if (FileAccess.FileAvailable(filename_glob))
			FileAccess.FileDelete(filename_glob,0);

		inf.setFilename(fnam);
	}

	public String zeigeFilename()
	{
		return filename_glob;
	}

	public void Append(String line)
	{
		inf.appendzeile(filename_glob, line, true);
		
	}
	
	public void AppendApplet(String codebase, String code)
	{
		// bsp: codebase=\"../../../analyser/bin/\"
		// CODE=\"applets/UserInfoApplet.class\"
		inf.appendzeile(filename_glob, "<BODY>", true);
		inf
				.appendzeile(
						filename_glob,
						"<APPLET codebase=\"../../../analyser/bin/\"CODE=\"applets/UserInfoApplet.class\" WIDTH=260 HEIGHT=180 ALIGN=left>",
						true);
		inf.appendzeile(filename_glob, "</APPLET>", true);
		inf.appendzeile(filename_glob, "</BODY>", true);
	}

	private String BuildTargetName(ThreadDbObj tdbo, int postnr)
	{
		return tdbo.calcTargetname(postnr);
		
		
	}

	public void AppendThreadLink(ThreadDbObj tdbo, int postnr)
	{
		// Hier wird der Threadlink eingeblendet, durch klicken auf diesen
		// link kommt man automatisch in den thread zu den passenden posting
		// http://www.wallstreet-online.de/diskussion/1093872-5651-5660
		// threadid-postnummer-postnummer+9

		// <a href="http://babelfish.altavista.com/">den Babelfish</a><br>
		String zeile = "<a href=" + BuildTargetName(tdbo, postnr) + ">"
				+ BuildTargetName(tdbo, postnr) + "</a><br>";
		Append(zeile);

	}

	public void AppendBalken(String farbe, int hoehe)
	{
		// fuege den farbigen Balken ein
		Append("<hr style=\"color:" + farbe + "; background: " + farbe
				+ "; height:" + hoehe + "px;\"></hr>");
		
	}
	public void AppendBalkenInfo(String info,String farbe, int hoehe)
	{
		// fuege den farbigen Balken ein
		Append("<p>"+info+"</p><hr style=\"color:" + farbe + "; background: " + farbe
				+ "; height:" + hoehe + "px;\"></hr>");
		
	}
	public void AppendRet()
	{
		Append("<p>");
	}
}
