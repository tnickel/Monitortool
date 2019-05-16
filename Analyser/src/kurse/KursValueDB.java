package kurse;

import hilfsklasse.Inf;
import hilfsklasse.Ma;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import internetPackage.DownloadManager;

import java.util.HashMap;
import java.util.HashSet;

import mainPackage.GC;
import objects.SymbolErsetzungsObj;
import slider.KursSlider;
import stores.KursvalueNativeDB;
import stores.SymbErsetzungsDB;
import stores.ThreadsDB;

public class KursValueDB
{
	// an diese Klasse wird eine Kursanfrage gestellt
	// diese Klasse verwaltet auch verschiedene Symbole(Splitting)
	private static SymbErsetzungsDB symbtrans = new SymbErsetzungsDB();
	// lade das ersetzungsobjekt mit null, also es existiert erst mal keien
	// Ersetzungstabelle
	private SymbolErsetzungsObj symbErsetzungObj = null;
	private String symbol_glob = null;
	private int stoperrorlogging_glob = 0;
	private KursSlider k38 = new KursSlider(38);
	private KursSlider vol38 = new KursSlider(38);
	private KursSlider s10 = new KursSlider(10);
	private KursSlider s38 = new KursSlider(38);
	private KursSlider k200 = new KursSlider(200);
	private KursSlider vol200 = new KursSlider(200);
	private int lastkursindex_g = 99;
	private Kursvalue letzteKursanfrage_g = null;
	private int fehlzaehler_g = 0;

	// hier sind die 3 kurse drin, diese Oberklasse verwaltet die 3 Kurse
	private KursvalueNativeDB[] KursValueNativeDB = new KursvalueNativeDB[3];

	// Hier sind die Kurse von WO-Online drin
	//private KursvalueNativeDB KursValueWo = new KursvalueNativeDB();

	// static private ThreadsDB tdb_g = null;
	static DownloadManager downloader = new DownloadManager(GC.MAXLOW);
	private int mid_g = 0;
	private int checkonlyflag_g = 0;
	private static ThreadsDB tdb_g = null;//new ThreadsDB();

	// Die menge sammelt die symbole die schon überprüft worden sind,
	// wir wollen ja nix doppelt überprüfen
	private static HashSet<String> checkedmenge = new HashSet<String>();

	
	
	public KursValueDB(String symbol, int checkonlyflag,ThreadsDB tdb)
	{
		// falls checkonlyflag ==1 werden nur die Kurse geprüft und nix
		// nachgeladen
		checkonlyflag_g = checkonlyflag;
		symbol_glob = new String(symbol);
		tdb_g=tdb;
		// schaue nach ob es für dies symbol eine Ersetzungstabelle gibt
		// wenn ja lade die Kurse für alle Symbole
		if ((symbErsetzungObj = symbtrans.SearchSymbolObj(symbol)) != null)
		{
			int anz = symbErsetzungObj.getAnzSymbole();
			for (int i = 0; i < anz; i++)
			{
				KursValueNativeDB[i] = new KursvalueNativeDB();
				KursValueNativeDB[i]
						.LadeKursSymb(symbErsetzungObj.getSymbol(i));
			}
		} else
		{
			// lade nur dies eine symbol da es kein Ersetzungsobjekt gibt
			KursValueNativeDB[0] = new KursvalueNativeDB();
			KursValueNativeDB[0].LadeKursSymb(symbol);
		}
		
		if (checkedmenge.contains(symbol) == false)
		{
			Tracer.WriteTrace(20, "Info: symbol<" + symbol
					+ "> wird in der Symbolersetzung gechecked");

				if(symbol.toLowerCase().equals("e2n")==true)
					System.out.println("found e2n");
				
			CheckSymbolersetzungstabelle();
			checkedmenge.add(symbol);
		}
		Tracer.WriteTrace(50, "fertig");
	}

	public int calcKursanzahl()
	{
		//ermittelt die gesammtkursanzahl die gespeichert ist für ein Symbol
		int gesanz=0;
		for(int i=0; i<3; i++)
		{
			if(KursValueNativeDB[i]!=null)
			{
				int kanz=KursValueNativeDB[i].getKursanzahl();
				gesanz=gesanz+kanz;
			}
		}
		return gesanz;
	}
	
	

	

	public String calcMindate()
	{
		String mindate = GC.enddatum;
		// ermittelt das kleinste datum aus allen Kursen
		for (int i = 0; i < 3; i++)
		{
			if (KursValueNativeDB[i] != null)
			{
				String dat = KursValueNativeDB[i].calcMindate();
				// suche neues mindate
				if ((mindate != null) && (dat != null))
					if (Tools.datum_ist_aelter_gleich(mindate, dat) == false)
						mindate = dat;
			}
		}
		return mindate;
	}
	public String calcMaxdate()
	{
		String maxdate = GC.startdatum;
		// ermittelt das kleinste datum aus allen Kursen
		for (int i = 0; i < 3; i++)
		{
			if (KursValueNativeDB[i] != null)
			{
				String dat = KursValueNativeDB[i].calcMaxdate();
				// suche neues mindate
				if ((maxdate != null) && (dat != null))
					if (Tools.datum_ist_aelter_gleich(dat,maxdate) == false)
						maxdate = dat;
			}
		}
		return maxdate;
	}
	public void cleanSlider()
	{
		k38 = new KursSlider(38);
		k200 = new KursSlider(200);
		s10 = new KursSlider(10);
		s38 = new KursSlider(38);
	}

	public boolean changeMaster(int oldmaster, int newmaster, String symbol)
	{
		return (symbtrans.wechseleMasterid(oldmaster, newmaster, symbol));
	}

	public boolean erwSymbolersetzung(int mid, String alt_symb,
			String alt_boer, String neu_symb, String neu_boer)
	{
		return (symbtrans.erweitereSymbolersetzung(mid, alt_symb, alt_boer,
				neu_symb, neu_boer));
	}

	public int calcAnzKurswerte()
	{
		int anzsymb = 0, anzkurse = 0;

		if (symbErsetzungObj != null)
		{// es gibt eine Ersetzungstabelle
			anzsymb = symbErsetzungObj.getAnzSymbole();
			int sumanz = 0;

			for (int i = 0; i < anzsymb; i++)
				sumanz = sumanz + KursValueNativeDB[i].GetanzObj();
			return sumanz;
		} else
		{
			// es gibt keine Ersetzungstabelle
			anzkurse = KursValueNativeDB[0].GetanzObj();
			return anzkurse;
		}
	}

	private Kursvalue holeKurswertAusSymbolersetzung(String suchdatum)
	{
		int i = 0;
		Kursvalue kobj = new Kursvalue();
		kobj.setValidflag(false);
		
		int anz = symbErsetzungObj.getAnzSymbole();

		// Falls lastkursindex schon initialisiert ist
		if (lastkursindex_g != 99)
		{
			// falls ein Normaler kurswert ohne Symbolersetzung da ist
			if (KursValueNativeDB[lastkursindex_g] != null)
			{
				kobj = KursValueNativeDB[lastkursindex_g].holeKurswert(
						suchdatum, 0);
				kobj.setKursinfo("Symbolersetzung symb'"
						+ KursValueNativeDB[lastkursindex_g].getSymb() + "'");
			}
		}

		// falls kein normaler Kurswert gefunden wurde schaue in der
		// Ersetzungstabelle nach
		if ((kobj == null) || (kobj.isValidflag() == false))
		{
			// suche in der symbolersetzung nach dem kurs
			for (i = 0; i < anz; i++)
			{
				// wir möchten ja den letzten index nicht doppelt
				// untersuchen
				if (i == lastkursindex_g)
					continue;

				kobj = KursValueNativeDB[i].holeKurswert(suchdatum, 0);

				if (kobj.isValidflag() == true)
				{ // Kurs gefunden
					symbol_glob = new String(KursValueNativeDB[i].getSymb());
					kobj.setKursinfo("Symbolersetzung symb'"
							+ KursValueNativeDB[i].getSymb() + "'");
					break;
				}
			}
		}

		

		if (kobj.isValidflag() != false)
		{
			// kurs gefunden
			lastkursindex_g = i;
			stoperrorlogging_glob = 0;

			if (kobj.isValidflag() == true)
			{

				return kobj;
			}
			// k==0 => kein kurs gefunden
			String msg2 = "Warning: Kein Kurs gefunden symbol<" + symbol_glob
					+ "> mid<" + mid_g + "> datum<" + suchdatum
					+ "> unterdrücke w. Meld !!!";
			if (stoperrorlogging_glob == 0)
			{
				stoperrorlogging_glob = 1;
				Tracer.WriteTrace(20, msg2);
				return kobj;
			}
		}
		return kobj;
	}

	

	public Kursvalue holeKurswert(String suchdatum, int sliderflag)
	{
		// sliderflag:1, dann speicherer die kursabfrage im Kursslider
		// sliderflag:0, dann wird der slider nicht betrachtet
		Kursvalue kobj = new Kursvalue();

		// falls Ersetzungstabelle da ist geht die tabelle durch bis kurs
		// gefunden
		if (symbErsetzungObj != null)
		{
			kobj = holeKurswertAusSymbolersetzung(suchdatum);
		} 
		
		letzteKursanfrage_g = kobj;

		if ((kobj.isValidflag() == true) && (sliderflag == 1))
		{
			float steigungsfaktor = 0;
			// kursslider
			k38.addSliderElem(kobj.getKv());
			k200.addSliderElem(kobj.getKv());

			// volumenslider
			vol38.addSliderElem(kobj.getVolumen());
			vol200.addSliderElem(kobj.getVolumen());

			// steigungsfaktor slider
			float k = kobj.getKv();
			if ((k38.getSliderFuellstand() < 38)
					|| (k200.getSliderFuellstand() < 200))
				steigungsfaktor = 0;
			else
				steigungsfaktor = Ma.calcSteigung(k, k38.calcSliderValue(),
						k200.calcSliderValue());
			s10.addSliderElem(steigungsfaktor);
			s38.addSliderElem(steigungsfaktor);
		}

		if (kobj.isValidflag() == false)
		{
			String msg = "Warning: Kein Kurs gefunden symbol<" + symbol_glob
					+ "> mid<" + mid_g + "> datum<" + suchdatum + ">";
			if (stoperrorlogging_glob == 0)
				Tracer.WriteTrace(20, msg);
			hilfsklasse.Inf inf = new Inf();
			inf.setFilename(GC.rootpath
					+ "\\db\\reporting\\keinKursfürSymbol.txt");
			inf.writezeile(msg);
			kobj.setKursinfo("Kein Kurs");
		}

		if (kobj.getKursinfo().length() < 3)
			System.out.println("kein kursinfo");

		return kobj;
	}

	public float holeKurswert38()
	{
		if (k38.getSliderFuellstand() < 37)
			return 0;
		float k = k38.calcSliderValue();
		return k;
	}

	public float holeKurswert200()
	{
		if (k200.getSliderFuellstand() < 199)
			return 0;
		float k = k200.calcSliderValue();
		return k;
	}

	public float holeVolumenwert38()
	{
		if (vol38.getSliderFuellstand() < 37)
			return 0;
		float k = vol38.calcSliderValue();
		return k;
	}

	public float holeVolumenwert200()
	{
		if (vol200.getSliderFuellstand() < 199)
			return 0;
		float k = vol200.calcSliderValue();
		return k;
	}

	public float holeS10()
	{
		// hole durchschnittlichen steigungsfaktor der letzten 10 Tage
		if (s10.getSliderFuellstand() < 10)
			return 0;
		float s = s10.calcSliderValue();
		return s;
	}

	public float holeS38()
	{
		// hole durchschnittlichen steigungsfaktor der letzten 10 Tage
		if (s38.getSliderFuellstand() < 10)
			return 0;
		float s = s38.calcSliderValue();
		return s;
	}

	public float holeSteigung()
	{
		float k = letzteKursanfrage_g.getKv();
		return (calcSteigung(k));
	}

	private float calcSteigung(float k)
	{
		float k38 = holeKurswert38();
		float k200 = holeKurswert200();

		if ((k38 == 0) || (k200 == 0))
			return 0;
		float s = (k * k38) / (k200 * k200);

		if ((k38 > k200) && (k > k38))
			return s;
		else
			return 0;
	}

	private void CheckSymbolersetzungstabelle()
	{
		// Prüft ob die Symbolersetzungstablle konsistent
		// Bei diesem Plausicheck wird überprüft ob alle Symbole in einer
		// Symbolzeile auch zusammengehören
		// Hier werden alle Symbollisten geladen und geschaut ob die einzelnen
		// Symbollisten überschneidungen im Datum haben
		// Wenn ja, dann wird geprüft ob der Kurs gleich ist

		HashMap<String, Float> allKurs = new HashMap<String, Float>();

		// Baue die Kurstabellen auf
		for (int j = 0; j < 3; j++)
		{
			if (KursValueNativeDB[j] != null)
			{
				int anz = KursValueNativeDB[j].GetanzObj();

				for (int k = 0; k < anz; k++)
				{
					// Sammle die Kurse
					KursvalueNativeDbObj kn = (KursvalueNativeDbObj) KursValueNativeDB[j]
							.GetObjectIDX(k);
					String dat = kn.getDate();
					Float kurs = kn.getAdjclose();

					if (allKurs.containsKey(dat) == false)
						allKurs.put(dat, kurs);
					else
					{
						// Falls schon drin, dann prüfe ob gleich
						Float kall = allKurs.get(dat);

						// 100% Abweichung ist erlaubt, bei gleichem datum
						if (Tools.istKursKorrekt(kall, kurs, 100) == false)
						{
							Tracer
									.WriteTrace(20,
											"Internal Error: Symbolersetzungstabelle fehlerhaft");
							Tracer.WriteTrace(20, "dat<" + dat + "> kall<"
									+ kall + "> != kurs<" + kurs + ">");
							String symbstring = "";
							for (int a = 0; a < 3; a++)
							{
								if (KursValueNativeDB[a] != null)
									symbstring = symbstring.concat("<" + a
											+ ">"
											+ KursValueNativeDB[a].getSymb()
											+ ":");
							}
							Tracer
									.WriteTrace(10, "Symbole<" + symbstring
											+ ">");
						}
					}

				}
			}

		}
	}
}
