package wartung;

import observerpackage.Webpagesobserver;

public class Pinksheets
{

	public void DownloadInsiderTrades()
	{

		Webpagesobserver webob = new Webpagesobserver(
				"http://www.pinksheets.com/pink/quote/quote.jsp?symbol=apio#getInsiderTrans",
				"\\download\\insidertrades", "\\db\\insider.db", "str1", "str2");
		webob.BuildWebpagesDB();

		// webob.CheckAllWebpagesChanged();
	}
}
