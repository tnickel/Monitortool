package data;

import hiflsklasse.Inf;

import java.io.File;
import java.util.ArrayList;

import jhilf.JProgressWin;

public class IndicatordataListe_0B extends IndiGrundfunkt
{
	// diese Klasse speichert alle Zeilen der indikatoren.
	// eine Zeile hat n Attribute
	// hier wird alles in einer Liste gespeichert, das hat den vorteil wenn man
	// das
	// in einer tablle anzeigen möchte

	// datum, indi1, indi2, indi3.... indiN
		private ArrayList<Indidat> indilist = new ArrayList<Indidat>();
	
	public int getanzelems()
	{
		return indilist.size();
	}
	
	public Indidat getIndi(int index)
	{
		return indilist.get(index);
	}
	

	public IndicatordataListe_0B(String fnam)
	{

		String zeile = null;
		Inf inf = new Inf();
		inf.setFilename(fnam);
		
		int count = 0, progresscount = 0;

		File fname = new File(fnam);
		long flen = fname.length();
		JProgressWin jp = new JProgressWin("load data <" + fnam + ">", 0,
				(int) (flen));
		jp.update(0);

		while ((zeile = inf.readZeile()) != null)
		{

			if (count % 100 == 0)
			{
				System.out.println("count=" + count);
				jp.update(progresscount);

			}

			// in ist der vektor mit 99 attributen
			Indidat in = new Indidat(zeile);
			count++;
			progresscount = progresscount + zeile.length();

			indilist.add(in);

		}

		jp.end();
	}

}
