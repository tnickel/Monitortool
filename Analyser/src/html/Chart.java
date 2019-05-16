package html;

import hilfsklasse.SG;
import hilfsklasse.Tracer;

public class Chart extends HtmlCore
{
	// http://aktien.wallstreet-online.de/1310077/chart.html
	private String filename = null;

	private SG sg = new SG();

	public Chart()
	{
	}

	public Chart(String fnam)
	{
		filename = fnam;
		super.ReadHtmlPage(fnam);
	}

	public int GetMarketid()
	{
		String str = GetKeywordPartMaxdist(htmlseite_str, "market_id=", "&amp",
				20);

		if ((str == null) || (str.equals("0") == true))
		{
			Tracer.WriteTrace(20, "W:no marketid in htmlfile <" + filename
					+ ">");
			return -1;
		}
		int z = SG.get_zahl(str);
		if (z < 0)
			Tracer.WriteTrace(20, "W:no zahl marketid in htmlfile <" + filename
					+ "> str=<" + str + ">");
		return z;
	}

}
