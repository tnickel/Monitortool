package kurse;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Ma;
import hilfsklasse.SG;
import hilfsklasse.Tools;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;
import internetPackage.DownloadManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import mainPackage.GC;
import objects.AktDbObj;
import objects.Obj38200;
import objects.ThreadDbObj;
import objects_gewinnausgabe.Liste38200;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;

import stores.AktDB;
import stores.MidDB;
import stores.ThreadsDB;
import swtViewer.ViewExternFileTable;
import db.DB;

public class KurseDB extends DB
{
	private static ThreadsDB tdb_glob=null;
	// Hier werden die kurse und symbole verwaltet
	// die Kurswerte kann man hier nicht abfragen.
	// Dies ist also eine reine Verwaltungsklasse
	public boolean plausiVorNeuaufnahme(Object obj)
	{
		return true;
	}

	private void ImportSymbole()
	{
		// hier werden neue Symbole hinzuimportiert
		// Man möchte die Datenbasis hier von Hand erweitern
		BufferedReader inf = null;

		int i = 0, n = 0;
		String filename = GC.rootpath + "\\db\\symbole.import";

		if (FileAccess.FileAvailable(filename) == false)
		{
			Tracer.WriteTrace(10, "Error: no importer");
		}

		inf = FileAccess.ReadFileOpen(filename, "UTF-8");
		String zeile = "";

		try
		{
			while ((zeile = inf.readLine()) != null)
			{
				String symbol = new String(SG.nteilstring(zeile, "#", 1));
				String boerse = new String(SG.nteilstring(zeile, "#", 2));

				if (IstSymbVorhanden(symbol) == true)
					continue;

				KursDbObj kobj = null;
				kobj = new KursDbObj(symbol + "#" + 0 + "#" + 0 + "#" + 0 + "#"
						+ "dummy" + "#" + 0 + "#" + 0 + "#" + 0 + "#" + boerse
						+ "#" + "0");
				dbliste.add(kobj);
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ToolsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public KurseDB(ThreadsDB tdb)
	{
		tdb_glob=tdb;
		Tracer.WriteTrace(20, "Info:Kontstruktor KurseDB");
		LoadDB("kurse", null, 0);
		this.ImportSymbole();
	}

	public boolean IstSymbVorhanden(String sym)
	{
		int i = 0;
		int anz = GetanzObj();
		for (i = 0; i < anz; i++)
		{
			KursDbObj kdbo = (KursDbObj) GetObjectIDX(i);
			if (kdbo.getSymbol().equalsIgnoreCase(sym) == true)
				return true;
		}
		return false;
	}

	public KursDbObj holeKursobj(String sym)
	{
		int i = 0;
		int anz = GetanzObj();
		for (i = 0; i < anz; i++)
		{
			KursDbObj kdbo = (KursDbObj) GetObjectIDX(i);
			if (kdbo.getSymbol().equalsIgnoreCase(sym) == true)
				return kdbo;
		}
		return null;
	}

	public int GetSymbolIDX(String sym)
	{
		int i = 0;
		int anz = GetanzObj();
		for (i = 0; i < anz; i++)
		{
			KursDbObj kdbo = (KursDbObj) GetObjectIDX(i);
			if (kdbo.getSymbol().equalsIgnoreCase(sym) == true)
				return i;
		}
		return -99;
	}

	public KursDbObj searchNextKursSteigf(int minsteigfaktor, int minanzwerte)
	// sucht den nächsten Kurs mit dem minimalen steigfaktor
	// return: objekt
	{

		KursDbObj kdbo = null;

		while ((kdbo = (KursDbObj) GetNextObject()) != null)
		{
			if ((kdbo.getSteigfaktor() >= minsteigfaktor)
					&& (kdbo.getAnzwerte() >= minanzwerte))
			{
				return kdbo;
			}
		}

		return kdbo;

	}

	public void addKurs(String symb, String boer)
	{
		// ein neues Kursobjekt wird hinzugefügt
		KursDbObj kobj = null;
		// symbol ist noch nicht gespeichert
		if ((kobj = holeKursobj(symb)) == null)
		{
			// Symbol ganz neu
			kobj = new KursDbObj(symb + "#" + "0" + "#" + "0" + "#" + "0" + "#"
					+ "dummy" + "#" + "0" + "#" + "0" + "#" + "0" + "#" + boer
					+ "#" + null + "#0#0");

			this.AddObject(kobj);
			this.WriteDB();
		} else
		{// symbol schon drin
			// ist boerse schon drin
			if (kobj.getBoerse() == null)
				kobj.setBoerse(boer);

			this.WriteDB();
		}
	}

	private void SonderfallWoFehlerbehandlung(AktDbObj aktdbobj,
			ThreadDbObj tdbo, String neu_symbol)
	{
		if (Tools.isKorrektSymbol(aktdbobj.getSymbol()) == true)
		{
			Tracer.WriteTrace(20, "Warning: WO Datenbank fehlerhaft neues tid<"
					+ tdbo.getThreadid() + "> neues tdb-symb<" + neu_symbol
					+ "> aktdb-symb<" + aktdbobj.getSymbol()
					+ "> -> gleiche tdb und akt mit sktdb-symb <"
					+ aktdbobj.getSymbol() + "> ab");
			tdbo.setSymbol(aktdbobj.getSymbol());

		} else
		{
			Tracer.WriteTrace(20, "Warning: WO Datenbank fehlerhaft neues tid<"
					+ tdbo.getThreadid() + "> neues tdb-symb<" + neu_symbol
					+ "> aktdb-symb<" + aktdbobj.getSymbol()
					+ "> -> beide Symbole defekt -> gehe weiter");
		}
	}

	public void ladeAlleKurseParallel(AktDB aktdb,int threshold,ProgressBar pb,Display dis,HashSet<String> symbolmenge)
	{
		// hier wereden alle Kurse parallel refreshed

		String symbol=null;
		DownloadManager dm = new DownloadManager(20);
		LadeKurs lk = new LadeKurs();
		this.ResetDB();

		int anz = this.GetanzObj();
		if (anz == 0)
			Tracer.WriteTrace(10,
					"Error: keine kurse.db verfügbar, lade langsam !!");
		if(pb!=null)
		{
		 pb.setMinimum(0);
		 pb.setMaximum(anz);
		}
		
		if (GC.ONLINEMODE == 0)
		{
			pb.setSelection(anz);
			Tracer.WriteTrace(20, "Info: offlinemode lade keine Kurse !!!");
			return;
		}
		
		for (int i = 0; i < anz; i++)
		{
			if(pb!=null)
			  pb.setSelection(i);
			if(dis!=null)
				dis.readAndDispatch();
			
			KursDbObj kobj = (KursDbObj) this.GetObjectIDX(i);
			symbol=kobj.getSymbol();
			String fnam = GC.rootpath + "\\db\\kurse\\" + symbol+ ".csv";

		
			
			if(symbolmenge!=null)
				if(symbolmenge.contains(symbol)==false)
					continue;
			
			int stunden = FileAccess.calcHours(fnam);

			if (stunden > threshold)
				System.out.println("lade neu(" + i + "|" + anz + ") symb<"
						+ kobj.getSymbol() + "> börse<" + kobj.getBoerse()
						+ ">");
			else
			{
				System.out.println("ok, alter<" + stunden + ">(" + i + "|"
						+ anz + ") symb<" + kobj.getSymbol() + "> börse<"
						+ kobj.getBoerse() + ">");
				continue;
			}
			
			//falls in kurse.db keine börse hinterlegt ist dann schaue in aktdb
			String boer=kobj.getBoerse();
			if((boer==null)||(boer.contains("null")))
			{
				AktDbObj aktobj=aktdb.SearchSymbObj(symbol);
				if(aktobj==null)
					Tracer.WriteTrace(20, "Error: symbol<"+symbol+"> ist nicht in aktdb.db");
				else
				{
					boer=aktobj.getBoerse();
					if((boer==null)||(boer.contains("NOKURS"))||(boer.length()==0))
					{
							Tracer.WriteTrace(20,"Error: borsenfehler in aktdb für symbol<"+symbol+">");
					}
				}
			}
			
			Boolean status = lk.LoadYahooKursSchnell(kobj.getSymbol(), kobj
					.getSymbol(), boer, dm);
			if (status == true)
				kobj.setLastload(Tools.get_aktdatetime_str());

			if (i % 500 == 0)
				this.WriteDB();

		}
		dm.waitEnd();
		this.WriteDB();
	}

	public void aktualisiereKursDB(MidDB middb,ThreadsDB tdb, int threshold,
			boolean forceflag)
	{
		// hier wird die KursDB aus aktdb und threadsdb aufgebaut
		// falls forceflag==1, dann wird die börse neu bestimmt und der Kurs neu
		// gesetzt

		// threshold: gibt anzahl Stunden an die verstrichen sein müssen bevor
		// neuer Kurs geladen wird
		// Läd alle Kurse der Threads DB
		// 1) Im ersten Schritt wird geschaut ob dies ein Aktienthread ist
		// 2) Dann wird dieser Thread in der Aktdb gesucht und geschaut ob schon
		// mal ein Kurs geladen wurde
		// 3) Der Kurs wird dann aktualisiert
		// schnellflag==1, dann lade nur die bekannten kurse und füge nix neues
		// hinzu
		// Achtung hier wird kein Kurs geladen !!!
		String alt_symbol = null, neu_symbol = null, alt_boerse = null, neu_boerse = null;
		int threadanzahl = tdb.GetanzObj();
		int stat = 0;

		DownloadManager dm = new DownloadManager(GC.MAXLOW);

		AktDB aktdb = new AktDB();
		tdb.ResetDB();

		if (GC.ONLINEMODE == 0)
		{
			Tracer.WriteTrace(20, "Info:offlinemode,-> gehe weiter");
			return;
		}

		for (int i = 0; i < threadanzahl; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) tdb.GetObjectIDX(i);
			int tid = tdbo.getThreadid();

			
			if ((tdbo.getMasterid() == 0) || (tdbo.getSymbol().length() == 0)
					&& (tdbo.getState() > 0))
			{
				Tracer.WriteTrace(20, "Warning: Kein Aktienthread mid<"
						+ tdbo.getMasterid() + "> symb<" + tdbo.getSymbol()
						+ "> state<" + tdbo.getState() + "> fahre fort");
				continue;
			}

			// überprüfe den status der Threadid
			AktDbObj aktdbobj = (AktDbObj) aktdb.GetObject(tid);
			if (aktdbobj == null)
			{
				Tracer
						.WriteTrace(20,
								"Warning:tid nicht in aktdb->gehe weiter");
				continue;
			}

			// wenn kein forceflag, dann wird nur aktualisiert wo schon daten da
			// sind
			String boerse = aktdbobj.getBoerse();
			if (forceflag == false)
				if (boerse.contains("NOKURS") == true)
					continue;

			if (aktdbobj == null)
			{
				Tracer.WriteTrace(20, "Error: internal error tid<" + tid
						+ "> in threads.db but not in aktdb");
				continue;
			}

			if (aktdbobj == null)
			{
				Tracer.WriteTrace(10, "Error: tid<" + tid
						+ "> not in aktdb internal error !!!");
				continue;
			}

			// überprüfe ob schon ein Kurs da ist falls loadmode ==0
			// bei loadmode ==0 und Kurs vorhanden mache nix
			String symb_aktdb = aktdbobj.getSymbol();

			stat = aktdbobj.getStatus();
			// falls forceflag ==1 dann bestimme und lade neu
			if (forceflag == true)
				if (resyncKurs(middb,tdb, dm, aktdb, i, tdbo, tid, aktdbobj) == false)
					continue;

			// d) setze die Daten in kursdb
			String symb = aktdbobj.getSymbol();
			String boer = aktdbobj.getBoerse();
			this.addKurs(symb, boer);
			// this.WriteDB();

			System.out
					.println("<" + i + "|" + threadanzahl + ">aktname<"
							+ aktdbobj.getAktname() + "> symbol<"
							+ aktdbobj.getSymbol() + "> wkn<"
							+ aktdbobj.getWkn() + ">");

			if (i % 500 == 0)
			{
				aktdb.WriteDB();
				tdb.WriteDB();
				this.WriteDB();
			}
		}
		this.WriteDB();
		aktdb.WriteDB();
		tdb.WriteDB();
		dm.waitEnd();
	}

	private Boolean resyncKurs(MidDB middb,ThreadsDB tdb, DownloadManager dm, AktDB aktdb,
			int i, ThreadDbObj tdbo, int tid, AktDbObj aktdbobj)
	{
		// hier wird für das Symbol die Börse neu bestimmt und die
		// datenbasis aktuallisiert
		// true, falls alles ok
		// false, im fehlerfall

		String alt_symbol;
		String neu_symbol;
		String alt_boerse;
		String neu_boerse;
		String mes=null;
		
		if ((aktdbobj.getAktname().length() < 2)
				|| (aktdbobj.getSymbol().length() < 2)
				|| (aktdbobj.getWkn().length() < 2))
		{
			// die grunddaten sind nicht da, dann lade die
			Tracer
					.WriteTrace(20,
							"I:lade Grunddaten aktname_symbol_wkn für tid<"
									+ tid + ">");
			aktdbobj.lade_aktname_symbol_wkn(middb,tdb,dm,i, 0, tdbo);
		}

		String symb_tdb = tdbo.getSymbol();
		String symb_aktdb = aktdbobj.getSymbol();

		// falls a)börsenplatz bestimmt und b)aktdbobj-symbol = tdb-symbol=>dann ist alles ok
		if ((aktdbobj.getBoerse().toLowerCase().equals("0")==false) &&
		(symb_tdb.equalsIgnoreCase(symb_aktdb) == true))
			return true;

		Inf inf = new Inf();
		inf.setFilename(GC.rootpath + "\\db\\reporting\\symbolfehler.txt");

		
		if(symb_tdb.equalsIgnoreCase(symb_aktdb) == false)
			mes = "Error: tid<"
				+ tid
				+ "> hat ungleiches symbol in tdb und aktdb tdb<"
				+ symb_tdb
				+ "> aktdb<"
				+ aktdbobj.getSymbol()
				+ "> Lade Primepage neu";
		else
			mes = "Error: tid<"
				+ tid
				+ "> hat defektes Kurssymbol aktdb.boerse<"
				+ aktdbobj.getBoerse()
				+ "> Lade Primepage neu";
			
		Tracer.WriteTrace(20, mes);
		inf.writezeile(mes);

		alt_symbol = aktdbobj.getSymbol();
		alt_boerse = aktdbobj.getBoerse();
		aktdbobj.lade_aktname_symbol_wkn(middb,tdb,dm,i, 1, tdbo);
		aktdbobj.SucheYahooBoerse(dm, tdbo);
		neu_symbol = aktdbobj.getSymbol();
		neu_boerse = aktdbobj.getBoerse();

		if (alt_boerse.equals(neu_boerse) == false)
			Tracer.WriteTrace(20, "I: die börse hat sich geändert alt<"
					+ alt_boerse + "> neu<" + neu_boerse + ">");

		if (Tools.isKorrektSymbol(neu_symbol) == true)
		{
			// Neues Symbol ist Korrekt
			mes = "Info: tid<"
					+ tid
					+ ">neues Symbol <"
					+ aktdbobj.getSymbol()
					+ "> wurde in aktdb.db aktuallisiert und wird in threads.db neu gesetzt";
			Tracer.WriteTrace(20, mes);
			inf.writezeile(mes);
			tdbo.setSymbol(aktdbobj.getSymbol());
			symb_tdb = tdbo.getSymbol();
			// aktdb.WriteDB();
			// tdb.WriteDB();
		} else
		{
			// neues Symbol ist nicht Korrekt
			Tracer.WriteTrace(20,
					"Warning:Neues Symbol ist nicht korrekt,symbalt<"
							+ alt_symbol + "> symbneu<" + neu_symbol
							+ ">, möglichw fehler bei WO gehe, weiter!!");

			// Sonderfall, das neue Symbol ist "0", aber in aktdb ist
			// noch ein gültiges
			// symbol

			if (neu_symbol.equalsIgnoreCase("0") == true)
			{
				SonderfallWoFehlerbehandlung(aktdbobj, tdbo, neu_symbol);
				// aktdb.WriteDB();
				// tdb.WriteDB();
			}
			return false;
		}

		// passe jetzt noch das Symbol in der Ersetzungstabelle an
		if ((Tools.isKorrektSymbol(alt_symbol) == true)
				&& (Tools.isKorrektSymbol(neu_symbol) == true)
				&& (alt_symbol.equals(neu_symbol) == false))
		{

			// ein neues Symbol wurde gefunden
			tdbo.ChangeSymbol(aktdbobj.getMasterid(), alt_symbol, alt_boerse,
					neu_symbol, neu_boerse,tdb_glob);
		}

		// aktdb.WriteDB();
		// tdb.WriteDB();

		if (symb_tdb.equals(aktdbobj.getSymbol()) == false)
		{
			Tracer
					.WriteTrace(
							20,
							"Error: tid<"
									+ tid
									+ "> Symbol ist immer noch ungleich, gehe zur nächsten Aktie<"
									+ symb_tdb + "> aktdb<"
									+ aktdbobj.getSymbol() + "> ");

			return false;
		}
		return true;
	}

	public void ladeAlleKurseUpdateKurseDB(AktDB aktdb,ThreadsDB tdb, int threshold,
			boolean schnellflag, boolean forceflag, ProgressBar pb, Display dis)
	{
		// schnellflag==1, (lade nur)
		// schnellflag==0, dann suche auch in aktdb und threadsdb nach neuen
		// symbolen
		// forceflag ==1, die kursedb wird neu überdacht, d.h. auch alle Börsen
		// werden
		// neu ausgetestet
		MidDB middb=new MidDB(tdb);
		
		if (schnellflag == true)
			ladeAlleKurseParallel(aktdb, threshold, pb, dis, null);

		else
		{
			// hier wird nach neuen Symbolen gesucht
			aktualisiereKursDB(middb,tdb, threshold, forceflag);
			ladeAlleKurseParallel(aktdb, threshold, pb, dis, null);
		}
	}

	public void suche38_200LimitsSortSteigung(int checkonlyflag,
			ProgressBar pb, Display dis)
	{
		// sucht alle 38/200 Kurse und sortiert anschliessend die tdb nach dem
		// Steigungsfaktor
		// danach wird in db/reporting/gregor.csv eine exel-Übersichtsliste über die Steigungen erstellt
		// checkonlyflag=0, => es wird ein kurs geladen falls er fehlt
		// checkonlyflag=1, => es wird nur gechecked
		String wkn = "";
		String boer = "";
		KursValueDB kvdb = null;
		AktDB aktdb = new AktDB();
		ThreadsDB tdb = new ThreadsDB();
		int anz = this.GetanzObj();
		HashSet<String> symberrormenge = new HashSet<String>();

		Liste38200 li38200 = new Liste38200(GC.rootpath
				+ "\\db\\reporting\\gregor.csv");

		String fehlkurse = GC.rootpath
				+ "\\db\\reporting\\kurs200tageProblem.txt";
		Inf inf = new Inf();
		inf.setFilename(fehlkurse);

		// Sortiere die tdb nach Steigungsfaktor
		tdb.sortSteigungsfaktorWriteDB();

		pb.setMinimum(0);
		pb.setMaximum(anz);

		// gehe durch alle Kurse und baue eine Liste mit Steigungsfaktoren
		for (int i = 0; i < anz; i++)
		{
			pb.setSelection(i);
			if (dis != null)
				dis.readAndDispatch();
			KursDbObj kdbo = (KursDbObj) this.GetObjectIDX(i);
			String symb = kdbo.getSymbol();

			if (Tools.isKorrektSymbol(symb) == false)
			{

				Tracer.WriteTrace(20, "Warning: symbol <" + symb
						+ "> ist nicht korrekt mache weiter");

				continue;
			}

			kvdb = new KursValueDB(symb, checkonlyflag,tdb_glob);
			// Es wird aktuelles Datum -2 Tage gerechnet
			String aktdate = Tools.modifziereDatum(Tools.entferneZeit(Tools
					.get_aktdatetime_str()), 0, 0, -2, 1);

			String date200 = Tools.modifziereDatum(aktdate, 0, 0, -220, 0);

			li38200.setKopfzeile("Kursbewertung -<" + aktdate + "> kv200<"
					+ date200 + ">-<" + aktdate + ">");

			int fehlcounter = 0;
			// betrachte die letzten 200 Tage
			for (int j = 0; j < 220; j++)
			{
				Kursvalue v = kvdb.holeKurswert(date200, 1);
				float val = v.getKv();

				if (val == 0)
					fehlcounter++;

				if (fehlcounter > 10)
				{
					if (symberrormenge.contains(symb) == false)
					{
						String msg = "Info: Kann für symbol <"
								+ symb
								+ "> nam<"
								+ tdb.sucheEinenThreadnamen(symb)
								+ "> nicht 200 Kurswerte ermitteln -> überspringe";
						Tracer.WriteTrace(20, msg);
						inf.writezeile(msg);
						symberrormenge.add(symb);
					}
					continue;
				}

				// System.out.println("kv=" + k2.getKv());
				date200 = Tools.modifziereDatum(date200, 0, 0, 1, 1);
			}
			float kv38 = kvdb.holeKurswert38();
			float vol38 = kvdb.holeVolumenwert38();
			float s10 = kvdb.holeS10();
			float s38 = kvdb.holeS38();
			float kv200 = kvdb.holeKurswert200();
			float vol200 = kvdb.holeVolumenwert200();
			Kursvalue kv = kvdb.holeKurswert(aktdate, 0);
			Obj38200 obj38200 = new Obj38200();
			obj38200.setK(kv.getKv());
			obj38200.setK200(kv200);
			obj38200.setK38(kv38);
			obj38200.setSymb(symb);
			String tnam = tdb.sucheEinenThreadnamen(symb);
			obj38200.setName(tnam);
			obj38200.setVolumen(kv.getVolumen());
			obj38200.setVolumen38(vol38);
			obj38200.setVolumen200(vol200);
			obj38200.setS(Ma.calcSteigung(kv.getKv(), kv38, kv200));
			obj38200.setS10(s10);
			obj38200.setS38(s38);
			int tid = tdb.holeTid(symb);
			obj38200.setThreadid(tid);
			obj38200.setMasterid(tdb.holeMasterid(tid));

			int symbindx = aktdb.SearchSymbIDX(symb);
			if (symbindx < 0)
			{
				wkn = "???";
				boer = "???";
				Tracer.WriteTrace(20, "Warning: symbol symbol<" + symb
						+ "> in Tdb unbekannt setze wkn='???' boer='???'");
			} else
			{
				AktDbObj aktdbobj = (AktDbObj) aktdb.GetObjectIDX(symbindx);
				if (aktdbobj == null)
					Tracer.WriteTrace(10, "Error: internal xxxx");
				wkn = aktdbobj.getWkn();
				boer = aktdbobj.getBoerse();
			}
			obj38200.setBoerse(boer);
			obj38200.setWkn(wkn);
			li38200.addObj(obj38200);
			String msg = i + " ; " + symb + " ; " + kv.getKv() + " ; " + kv38
					+ " ; " + kv200 + " ; " + wkn + " ; " + boer + " ; " + tnam;
			Tracer.WriteTrace(20, msg);

			// Speichere den Steigerungsfaktor für das symbol
			tdb.setSortkriterium_steigungsfaktor(symb, obj38200.getS());
		}
		li38200.sort();
		li38200.gibListeAus();
		li38200.updatePrognosenDB();
		li38200.erzeugeHtmlliste(GC.rootpath + "\\db\\reporting\\38200Kurse");

		// speichere die tdb sortiert nach dem Steigungsfaktor
		tdb.sortSteigungsfaktorWriteDB();
	}
	public void zeigeSwtTabelle(Display display, String symbol,String titel)
	{
		ViewExternFileTable viewer= new ViewExternFileTable();
		viewer.ShowTable(display, GC.rootpath+"\\db\\Kurse\\"+symbol+".csv",titel,null);
		
	}
	public HashMap<String,String> erstelleKursmenge(HashSet<String> symbolmenge)
	{
		//Erstellt zu einer Symbolmenge eine Kursmenge
		// Also symbol,Datum#kurs
		ArrayList<String> symbliste = new ArrayList<String>(symbolmenge);
		HashMap<String,String> kliste = new HashMap<String,String>();
		int anz=symbliste.size();

		for(int i=0; i<anz; i++)
		{
			String symb=symbliste.get(i);
			KursValueDB kvdb = new KursValueDB(symb, 1,tdb_glob);

			if(kvdb.calcAnzKurswerte()==0)
			{
				//kein Kurs vorhanden
				kliste.put(symb, "0");
				continue;
			}
			
			//hole den neusten Kurswert und speichere
			String maxdate = kvdb.calcMaxdate();
			Kursvalue k1obj = kvdb.holeKurswert(maxdate, 0);
			Float val = k1obj.getKv();

			kliste.put(symb,String.valueOf(val));
		}
		return kliste;
	}
	public void postprocess()
	{}
}
