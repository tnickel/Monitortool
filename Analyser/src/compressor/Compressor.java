package compressor;

import hilfsklasse.Tools;
import kurse.KursValueDB;
import kurse.Kursvalue;
import stores.ThreadsDB;
import db.CompressedPostingObj;
import except.CompressedThreadException;

public class Compressor extends CompressedThreadDBx
{
	// Dies ist eine Oberklassse von compressedTrheadDBx
	// der Compressor übernimmt verwaltungsaufgaben von CompressedThread
	KursValueDB kvdbc_g = null;
	
	
	public Compressor(int tid, String symb,ThreadsDB tdb) throws CompressedThreadException
	{
		super(Integer.toString(tid));

		if (this.getanz() == 0)
			throw new CompressedThreadException("compressed thread tid<" + tid
					+ "> symbol<" + symb + "> empty");

		kvdbc_g = new KursValueDB(symb,0,tdb);

		// fülle Slider 200
		// versuche schon mal den Slider mit 200Tagen vorher zu füllen
		// Dies wird gemacht damit der Kv200 schon früher verfügbar ist.
		// Meist fangen die Diskussionsrunden viel später an und die Kurse sind
		// schon viele Tage vorher verfügbar
		String kursstart = kvdbc_g.calcMindate();
		fuelleSlider200(kursstart);
	}

	private void fuelleSlider200(String kursstart)
	{
		CompressedPostingObj co = this.getObjectIDX(0);
		String startpostdatum = Tools.entferneZeit(co.getDatetime());

		// Die vorlaufberechnung macht nur Sinn wenn die Kursdaten älter als
		// die Postdaten in den Threads sind
		if (Tools.datum_ist_aelter(kursstart, startpostdatum) == false)
			return;

		// ermittel die Zeitdifferenz in tagen, also die vorlauftage
		int vorlauftage = Tools.zeitdifferenz_tage(startpostdatum, kursstart);

		// mehr als 200 vorlauftage den Slider zu füllen macht keinen Sinn
		if (vorlauftage > 200)
			vorlauftage = 200;

		// 200Tage subtrahieren
		String laufdatum = Tools.modifziereDatum(startpostdatum, 0, 0,
				-vorlauftage, 0);

		for (int i = 0; i < vorlauftage; i++)
		{
			holeKurswert(laufdatum, 1);
			laufdatum = Tools.modifziereDatum(laufdatum, 0, 0, 1, 0);
		}

	}

	public Kursvalue holeKurswert(String suchdatum, int sliderflag)
	{
		Kursvalue val = kvdbc_g.holeKurswert(suchdatum, sliderflag);
		return val;
	}

	public Kursvalue holeEinzelkurswert(CompressedPostingObj copost)
	{
		Kursvalue k1obj = null;
		String kdate1 = Tools.entferneZeit(copost.getDatetime());
		k1obj = this.holeKurswert(kdate1, 1);
		return k1obj;
	}

	public float holeKurswert38()
	{
		return kvdbc_g.holeKurswert38();
	}

	public float holeKurswert200()
	{
		return kvdbc_g.holeKurswert200();
	}

	public float holeSteigung()
	{
		return kvdbc_g.holeSteigung();
	}

	public void cleanSlider()
	{
		if (kvdbc_g == null)
			return;
		kvdbc_g.cleanSlider();

	}
}
