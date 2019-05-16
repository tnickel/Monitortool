package data;

import hiflsklasse.Inf;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class TradeResultliste
{
	ArrayList<Tradefilterresultelem> reslist = new ArrayList<Tradefilterresultelem>();

	public void sorting()
	{
		 Collections.sort(reslist);
	}

	public void addE(Tradefilterresultelem el)
	{
		reslist.add(el);
	}
	
	public void writefile(String hitfile)
	{
		// baue die Resultliste als file auf
		String tabellenkopf = "Stratname#Tradeanz#Maxhits#Hitsumme#proz#Hitverteilung***";

		File hitfile_f = new File(hitfile);
		if (hitfile_f.exists())
			hitfile_f.delete();
		Inf hitfileinf = new Inf();
		hitfileinf.setFilename(hitfile);
		hitfileinf.writezeile(tabellenkopf);

		int size = reslist.size();
		for (int i = 0; i < size; i++)
		{
			Tradefilterresultelem elm = reslist.get(i);
			String outstring = elm.getOutstring();
			hitfileinf.writezeile(outstring);
		}
		hitfileinf.close();
	}
}
