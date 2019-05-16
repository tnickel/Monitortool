package filter;

import hilfsklasse.Tools;
import objects.ThreadDbObj;

public class ThreadsDbFilter extends Filter
{
	// Es werden nur Threads angezeigt die ein bestimmtes Alter haben
	private int maxthreadalter = 0;
	private String lastdownload = null;
	private String lastpost = null;
	private String datealt30 = null, datealtmax = null;

	public int getMaxthreadalter()
	{
		return maxthreadalter;
	}

	public void setMaxthreadalter(int maxthreadalter)
	{
		this.maxthreadalter = maxthreadalter;

	}

	public Boolean checkFilter(ThreadDbObj tdbo, Boolean altesAusblendenFlag)
	{
		lastdownload = Tools.entferneZeit(tdbo.getLastdownloadtime());
		lastpost = Tools.entferneZeit(tdbo.getLastThreadPosttime());

		if (altesAusblendenFlag == true)
		{
			datealt30 = Tools.modifziereDatum(super.adate, 0, 0, maxthreadalter
					* -1, 0);

			// falls lastdownload älter als 30 Tage
			if (Tools.datum_ist_aelter_gleich(lastdownload, datealt30) == true)
				// veraltet->melde bad
				return false;

			// prüfe wann das letzte Mal im Thread gepostet wurde
			if (Tools.datum_ist_aelter_gleich(lastpost, datealt30) == true)
				// veraltet->melde bad zurück da das letzte postdatum zu alt
				return false;

			return true;
		}

		return true;
	}
}
