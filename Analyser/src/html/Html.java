package html;

public class Html
{
	protected String HLink(String filename, String beschriftung)
	{
		// <a href="../..//db/slider/952430.txt" > 26 </a>
		String linkstr = new String("<a href=\"" + filename + "\" >"
				+ beschriftung + "</a>");
		return linkstr;
	}

	protected String WLink(String webpath, String beschriftung)
	{
		// <a
		// href=http://www.wallstreet-online.de/diskussion/840377-61-70/realtos-ende-vom-ende-zurueck-zu-10->
		// link</a>
		String linkstr = new String("<a href=" + webpath + " >" + beschriftung
				+ "</a>");
		return linkstr;
	}
}
