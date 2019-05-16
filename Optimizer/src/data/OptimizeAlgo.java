package data;


import gui.Mbox;
import jhilf.JProgressWin;
import Opti.GdXCalculation;
import Opti.IndiCalculation;

public class OptimizeAlgo
{
	// #### dies ist die Hauptklasse die für die Optimierung verantwortlich ist

	// hier werden die optimierten Zwischenergebnisse gespeichert
	public OptimizeResultliste oges_glob = null;
	static int progresscount = 0;
	static int pval = 0;

	// dies ist die orginal Tradeliste
	private Tradeliste tradeliste_orginal_glob = null;

	public OptimizeAlgo(Tradeliste tradeliste_orginal)
	{
		tradeliste_orginal_glob = tradeliste_orginal;
	}

	public OptimizeResultliste HoleOptimizeResultliste()
	{
		//die gesammtresultliste wird zurückgegeben
		return oges_glob;
	}
	public String getStratName(int index)
	{
		if (oges_glob == null)
			Mbox.Infobox("error internal optimizeResultliste empty");

		if (oges_glob.getElem(index) == null)
			Mbox.Infobox("error internal elem is empty");

		// der Strategiename für den index wird ausgegeben
		return (oges_glob.getElem(index).getResultname());
	}

	public void cleanOptimizeResult()
	{
		oges_glob = new OptimizeResultliste(tradeliste_orginal_glob);
	}

	public void optimizeAll(Timefilter timefilter)
	{
		// Dies ist die hauptoptimierungsfunktion die alles was selektiert
		// worden ist optimiert

		if (oges_glob == null)
			oges_glob = new OptimizeResultliste(tradeliste_orginal_glob);

		// gehe durch alle Indikatorlisten bsp. algoliste:EURUSD M5
		int anz = Algoliste.getAnz();

		//das activeflag prüft ob überhaupt ein Timefilter aktiviert ist
		boolean activeflag = false;
		for (int i = 0; i < anz; i++)
		{
			Algodat algodat = Algoliste.getAlgodat(i);

			//falls der Timeframe aktiviert ist
			if (algodat.isActivated() == true)
				activeflag = true;
		}
		if (activeflag == false)
		{
			Mbox.Infobox("Error: no optialgo activated");
			return;
		}

		//gehe durch alle Indikatoren durch
		//ein Eindikator ist ein File
		for (int i = 0; i < anz; i++)
		{
			Algodat algodat = Algoliste.getAlgodat(i);

			// falls indikatorliste aktiviert ist
			if (algodat.isActivated() == false)
				continue;

			// falls GDX-Optimierung
			if (algodat.getName().contains("Gdx") == true)
				optimizeGD();
			// falls Oracle-optimierung
			// Die Oracle-Optimierung wählt immer das optimum was möglich ist
			else if (algodat.getName().contains("Oracle") == true)
				optimizeIndiOracle();
			//falls die stunden anzeige, d.h. keine optimierung
			else if (algodat.getName().contains("hour") == true)
				optimizeIndiHour(timefilter);
			else
			{
				// es wird eine normale indikatorliste optimiert
				String fnam = algodat.getPath();
				//hier wird die Optimierung gestartet
				optimizeIndiFile(fnam, algodat.getName());
			}
		}
	}

	public OptimizeResult holeBestOptimizeResult()
	{
		if (oges_glob == null)
		{
			Mbox.Infobox("no optimize result selected");
			return null;

		}

		//das beste resultat holen
		return (oges_glob.holeBestOptimizeResult());
	}

	public OptimizeResult holeOptimizeResult(String resultname,String optoalgoname)
	{
		//hole ein optimiertes ergebniss anhand des namens
		return (oges_glob.holeOptimizeResult(resultname, optoalgoname));
	}

	public int holeOptimizeResultindex(String resultname,String optoalgoname)
	{
		//hole den index für einen resultnamen
		return (oges_glob.holeOptimizeIndex(resultname,optoalgoname));
	}

	private void optimizeGD()
	{

		GdXCalculation gdxcal = new GdXCalculation();

		// eine neue Optimize Gesammtliste anlegen
		if (oges_glob == null)
			oges_glob = new OptimizeResultliste(tradeliste_orginal_glob);

		int max = tradeliste_orginal_glob.getsize() / 2;
		if (max > 500)
			max = 500;

		JProgressWin jp = new JProgressWin("optimize", 0, max);
		jp.update(0);

		for (progresscount = 3; progresscount < max; progresscount++)

		{
			pval = progresscount;
			jp.update(progresscount);

			OptimizeResult os = gdxcal.calcGdxProfit(tradeliste_orginal_glob,
					progresscount);
			os.setResultname("Gdx<" + progresscount + ">");
			os.setOptalgoname("GDX");

			// das Ergebniss in der gesammtliste speichern
			oges_glob.add(os);
		}
		jp.end();
		return;
	}

	private void optimizeIndiFile(String filename, String optoalgoname)
	{
		// idee:
		// Wir wollten ja long und shortseite extra markieren.
		// Fall1:
		// d.h. Indi i=0 ist long und indi i+1 ist short
		// Ergebnissanzeige ist Indi buy/sell
		// Fall2:
		// d.h. Indi i=0 ist short und indi i+1 ist long
		// Ergebnissanzeige ist sell/buy
		//wenn dann heisst es 
		//buy/sell <0001a,0001b>
		//sell/buy <0001a,0001b>
		//buy/sell <0002a,0002b>
		//sell/buy <0002a,0002b>
		
		
		if (oges_glob == null)
			oges_glob = new OptimizeResultliste(tradeliste_orginal_glob);
		// filename="C:\\Forex\\optimizer\\data\\FilteranalyseEURUSD.txt"
		IndicatordataHashmap_0A indidat = new IndicatordataHashmap_0A(filename);

		// indical objekt was die berechnung für einen vektor übernimmt
		IndiCalculation indical = new IndiCalculation(indidat);

		// anz=99 indikatoren
		int anz = indidat.getAnzIndi();

		JProgressWin jp = new JProgressWin("optimize " + optoalgoname, 0,
				anz - 1);
		jp.update(0);

		if (anz % 2 != 1)
			Mbox.Infobox("indicator<" + filename + "> have not odd<" + anz
					+ "> parameter");

		//gehe durch alle indikatoren
		//parameter 0 ist der price
		for (progresscount = 1; progresscount < anz; progresscount = progresscount + 2)// gehe
																						// 2
																						// weiter
																						// =
																						// 1
																						// Tupel
		{
			System.out.println("Strategie wird mit Indicator= <"
					+ progresscount + ">  optimiert");

			// buy,sell optimieren
			OptimizeResult os1 = indical.workIndi(tradeliste_orginal_glob,
					progresscount,progresscount+1, "buy/sell");
			os1.setResultname("Indi buy/sell<" + indical.getIndiName(progresscount)+ ", "+indical.getIndiName(progresscount+1) +">");
			os1.setOptalgoname(optoalgoname);
			// das Ergebniss in der gesammtliste speichern
			oges_glob.add(os1);

			// sell, buy optimieren
			OptimizeResult os2 = indical.workIndi(tradeliste_orginal_glob,
					progresscount,progresscount+1, "sell/buy");
			os2.setResultname("Indi sell/buy<" + indical.getIndiName(progresscount)+ ", "+indical.getIndiName(progresscount+1) +">");
			os2.setOptalgoname(optoalgoname);
			// das Ergebniss in der gesammtliste speichern
			oges_glob.add(os2);

			//den progressount refreshen
			jp.update(progresscount);
		}
		jp.end();

	}

	private void optimizeIndiOracle()
	{
		//dies ist der oracle-algorithmus
		if (oges_glob == null)
			oges_glob = new OptimizeResultliste(tradeliste_orginal_glob);

		IndiCalculation indical = new IndiCalculation(null);
		OptimizeResult os = indical.workIndiOracle(tradeliste_orginal_glob);
		os.setResultname("Indi<ORACLE>");
		os.setOptalgoname("ORACLE");

		// das Ergebniss in der gesammtliste speichern
		oges_glob.add(os);
	}

	private void optimizeIndiHour(Timefilter timefilter)
	{
		//es wird nach der Stunde optimiert
		if (oges_glob == null)
			oges_glob = new OptimizeResultliste(tradeliste_orginal_glob);

		IndiCalculation indical = new IndiCalculation(null);
		OptimizeResult os = indical.workIndiHour(tradeliste_orginal_glob,
				timefilter);
		os.setResultname("Indi<hour/weekday>");
		os.setOptalgoname("hour/weekday");

		// das Ergebniss in der gesammtliste speichern
		oges_glob.add(os);
	}

	public void setSelectedAlgoStrategies(String selstrat)
	{

		int anz = Algoliste.getAnz();
		for (int i = 0; i < anz; i++)
		{

			Algodat algodat = Algoliste.getAlgodat(i);
			String algoname = algodat.getName();
			if (selstrat.contains(algoname) == true)
				algodat.setActivated(true);
			else
				algodat.setActivated(false);
		}
	}
}
