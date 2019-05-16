package html;

public class Signalpage extends HtmlCore
{
	public Signalpage(String filename)
	{
		super.ReadHtmlPage(filename);
	}

	public String GetNextSignalString()
	{
		String symb = null;
		String signal = GetFullHtmlSignal();
		if (signal == null)
			return null;
		String datum = this
				.GetKeywordPart2(signal, signal, "<nobr>", "</nobr>");
		String signalstr = signal.substring(0, signal.indexOf("</td>"));
		String keyword = signal
				.substring(signal.indexOf("</nobr></td> <td>") + 17);
		int pos = keyword.indexOf("x");
		if (pos < 3)
			symb = "bullisch";
		else
			symb = "bearisch";
		String retstring = datum + "#" + signalstr + "#" + symb;
		return retstring;
	}
}
