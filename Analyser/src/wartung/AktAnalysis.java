package wartung;

import hilfsklasse.DateExcecption;
import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.SG;
import hilfsklasse.Tools;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;

import java.io.BufferedReader;
import java.util.GregorianCalendar;

import kurse.KursDbObj;
import kurse.KurseDB;
import objects.AktDbObj;
import objects.ThreadDbObj;
import stores.AktDB;
import stores.PostDBx;
import stores.ThreadsDB;
import stores.UserDB;

public class AktAnalysis
{
	static private String froot = null;

	static private float[] kurswerte = new float[30];

	public AktAnalysis()
	{

	}

	public void Build_db_thread_big_liste(String filename)
	// Es wird eine postingliste angefertigt, d.h. es wird genau aufgeliste wo
	// und
	// wie oft ein user gepostet hat.
	{
		// lade die postingliste\\user_db
		// lade die threads_db
		// lade die post_db
		//

	}

	public void SucheNeueThreadsAusBesteAkts(ThreadsDB tdb)
	{
		// a) Es wird aus der AKTdb die Aktienwerte mit
		// Steigfaktor >8 betrachtet

		int kdb_i = 0, aktdb_i, threadid = 0, ret = 0;
		String symb = null, aktname = null, zeile = null, lastzeile = null;
		AktDB aktdb = new AktDB();

		//ThreadsDB tdb = new ThreadsDB();
		tdb.setAllMarker(0);
		KurseDB kdb = new KurseDB(tdb);
		kdb.ResetDB(-1);
		KursDbObj kdbo = null;
		Inf inf1 = new Inf();
		inf1.setFilename(froot + "\\db\\kurse.txt");

		while ((kdbo = kdb.searchNextKursSteigf(8, 100)) != null)
		{
			// hole das symbol
			symb = kdbo.getSymbol();

			// hole symbol, aktname und threadid
			aktdb_i = aktdb.SearchSymbIDX(symb);
			AktDbObj aktdbobj = (AktDbObj) aktdb.GetObjectIDX(aktdb_i);

			if (aktdb_i < 0)
			{
				System.out.println("ERROR aktdb_i =" + aktdb_i);
				continue;
			}

			threadid = aktdbobj.getThreadid();
			if (threadid < 0)
			{
				System.out.println("ERROR threadid =" + threadid);
				continue;
			}

			aktname = aktdbobj.getAktname();
			if (aktname.length() <= 1)
			{
				System.out.println("ERROR aktname =" + aktname);
				continue;
			}

			zeile = "SYMPT:found akt mit steigfaktor<" + kdbo.getSteigfaktor()
					+ " >  id<" + threadid + "> name<" + aktname + "> Symb<"
					+ symb + "> BDate<" + kdbo.getBreakdate() + ">";
			if (zeile.equals(lastzeile) == false)
			{
				lastzeile = zeile;
				Tracer.WriteTrace(20, "I:" + zeile);
				inf1.writezeile(zeile);
			}
			// prüfe ob threadid schon in der threads.db
			if ((ret = tdb.CheckThreadid(threadid)) >= 0)
			{// threadid- schon in threads-db=> gehe weiter
				ThreadDbObj tdbo = (ThreadDbObj) tdb.GetObjectIDX(ret);
				Tracer.WriteTrace(20, "I:setze marker für <"
						+ tdbo.getThreadname() + "><" + tdbo.getSymbol() + ">");
				tdbo.setMarker(1);
				continue;
			}

			// nimm thread in tdb auf
			zeile = "AKTION:Neu =>Nimm akt in threads.db auf und setzte Marker";
			Tracer.WriteTrace(40, "I:nim <" + aktname + "> <" + symb
					+ ">in threads.db auf und setzte Marker");

			inf1.writezeile(zeile);
			// threadname, id, date, status, masterid, symbol, pageanz, url,
			// imarker, breakid,
			// observerflag, lastloaded
			ThreadDbObj tdbo = new ThreadDbObj(aktname, threadid, "x", 0, 0,
					symb, 0, "0", 1, 0, 0, "0",null);
			tdb.AddObject(tdbo);
		}

		tdb.WriteDB();
	}

	public void BuildKurswerteDB(ThreadsDB tdb)
	{
		// kleinstes datum der Datenreihe
		BufferedReader br;
		String zeile = null, dateiname = null;

		AktDB akt = new AktDB();

		KurseDB kdb = new KurseDB(tdb);

		int tag, monat, jahr;
		long min_sec = 0, max_sec = 0, d1_sec = 0, mindate = 0, maxdate = 0;
		int aktcounter = 0, stuetzcounter = 0;
		int stepweite = 0;
		int steigfaktor = 0;
		String symbol = null;
		GregorianCalendar d1_date = new GregorianCalendar();

		// speichere in einer liste alle dateinamen von kurse
		FileAccess.initFileSystemList(froot + "\\db\\kurse", 1);
		// schaue nach wieviele Kurse vorhanden
		int anzSymbole = FileAccess.holeFileAnz();

		// Solange noch Kurse vorhanden
		while ((dateiname = FileAccess.holeFileSystemName()) != null)
		{
			int zeilencounter = 0, len = 0;
			String breakdate = null, date = null, open = null, high, low, close, volume = null, aclose = null, symb;
			stuetzcounter = 0;

			symbol = dateiname.substring(0, dateiname.lastIndexOf("."));

			// Falls Symbol schon vorhanden ist dann mache nix
			if (kdb.IstSymbVorhanden(symbol) == true)
				continue;

			br = FileAccess.ReadFileOpen(froot + "\\db\\kurse\\" + dateiname);
			len = FileAccess.FileLength(froot + "\\db\\kurse\\" + dateiname);

			if (len == 0)
			{
				Tracer.WriteTrace(20, "W:Keine kurswerte für Symbol<" + symbol
						+ "> dateilaenge=0, drop it");

				continue;
			}
			mindate = Tools.get_aktdate_lo();
			maxdate = Tools.get_mindate_lo();

			// stepweite messwerte werden ausgewertet bis aktcounter hoch-
			// gesetzt wird
			stepweite = (len / (43 * 10));
			// System.out.println("len="+len+" stepweite="+stepweite);
			for (int i = 0; i < 30; i++)
				kurswerte[i] = 0;

			stuetzcounter = 0;
			steigfaktor = 0;
			System.out.println("Berechne für (" + aktcounter + "|" + anzSymbole
					+ ") " + dateiname + ">");

			while ((zeile = FileAccess.ReadFileZeile(br)) != null)
			{
				if (zeile.contains("Date") == true)
					continue;
				try
				{
					date = SG.nteilstring(zeile, ",", 1);
					open = SG.nteilstring(zeile, ",", 2);
					high = SG.nteilstring(zeile, ",", 3);
					low = SG.nteilstring(zeile, ",", 4);
					close = SG.nteilstring(zeile, ",", 5);
					volume = SG.nteilstring(zeile, ",", 6);
					aclose = SG.nteilstring(zeile, ",", 7);
				} catch (ToolsException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try
				{
					d1_date.set(Tools.get_year_int(date), Tools
							.get_month_int(date) - 1, Tools.get_day_int(date));
				} catch (DateExcecption e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					Tracer.WriteTrace(10, this.getClass().getSimpleName()
							+ "Error: date format11 <" + date + ">");
				}
				d1_sec = d1_date.getTime().getTime();

				// kleinste und grösste datum ermitteln, normalerweise sind
				// daten sortiert
				if (d1_sec > maxdate)
					maxdate = d1_sec;
				if (d1_sec < mindate)
					mindate = d1_sec;

				// len/42 Stützwerte werden erwartet
				// d.h. bei einer Länge von 8000 Bytes werden ca. 195 Stützwerte
				// erwartet
				// => nimm beim Zähler von 195/10 = 19.5 eine Messung vor. also
				// 20,... 40... 60

				kurswerte[stuetzcounter] = kurswerte[stuetzcounter]
						+ Float.valueOf(aclose);
				// System.out.println("i=" + stuetzcounter + " closeval="
				// + Float.valueOf(aclose) + " Sum="
				// + kurswerte[stuetzcounter]);

				if (zeilencounter > (stepweite * (stuetzcounter + 1)))
				{
					// erhöhe den Stützcounter damit die folgenden werte
					// summiert werden
					stuetzcounter++;
				}
				zeilencounter++;
				// System.out.println("zeile=<" + zeile + ">");

				if ((stuetzcounter == 8) && (breakdate == null))
					breakdate = date;
			}

			for (int i = 0; i < 9; i++)
			{
				// System.out.println("i=" + i + " value=" + kurswerte[i]);
				if (kurswerte[i + 1] < kurswerte[i])
				{
					// System.out.println("vergleiche
					// Kurswert["+(i+1+"]="+kurswerte[i + 1]+"<
					// Kurswert["+i+"]="+kurswerte[i]));
					steigfaktor++;
				}
			}
			// System.out.println("Steigfaktor=" + steigfaktor[aktcounter]);
			aktcounter++;
			symb = dateiname.substring(0, dateiname.indexOf("."));

			KursDbObj kdbo = new KursDbObj(symb + "#"
					+ Tools.get_date_string(mindate, null) + "#"
					+ Tools.get_date_string(maxdate, null) + "#" + breakdate
					+ "#" + "dummy" + "#" + zeilencounter + "#"
					+ Integer.valueOf(volume) + "#" + steigfaktor);

			kdb.AddObject(kdbo);

		}
		kdb.WriteDB();
	}

	public void BerechneBreakIndexAusBesteAkts(ThreadsDB tdb)
	{
		// die als beste Akt markiert sind werden hier betrachtet
		// fuer die besten Akt wird der Breakindex ermittelt und
		// threads.db wird abgespeichert

		int threadIDX = 0, lastIDX = -1, kdbIDX = 0;
		Boolean postflag = false;
		String symb = null, breakdate = null;
		//ThreadsDB tdb = new ThreadsDB();
		UserDB udb = new UserDB("observeuser.txt","boostraenge.txt");

		tdb.ResetDB();
		KurseDB kdb = new KurseDB(tdb);
		PostDBx pdb = new PostDBx();

		// hole nächste gut bewerte AKT
		// hole als die nächste markierte AKT
		tdb.CleanBreakid();
		while ((threadIDX = tdb.getMarkedThread(lastIDX + 1, 1)) >= 0)
		{
			// setze den lastIDX höher
			lastIDX = threadIDX;
			// hole das passende Symbol
			ThreadDbObj tdbo = (ThreadDbObj) tdb.GetObjectIDX(threadIDX);
			symb = new String(tdbo.getSymbol());
			kdbIDX = kdb.GetSymbolIDX(symb);

			if (kdbIDX < 0)
			{
				Tracer
						.WriteTrace(
								20,
								"W:kann symb nicht in kurse.db<"
										+ symb
										+ ">finden !! Möglicherweise neues Symbol => mache weiter");

				continue;
			}
			KursDbObj kdbo = (KursDbObj) kdb.GetObjectIDX(kdbIDX);
			// breakdate ermitteln
			breakdate = kdbo.getBreakdate();
			if (breakdate == null)
			{
				Tracer.WriteTrace(20, "W:breakedate <" + breakdate + "> symb<"
						+ symb + "> => mache weiter");
				continue;
			}
			// die threadsdb auf den richtigen index setzen
			// anschliessend wird die postdb wieder analysiert und
			// das brakedate bzw. der zugeh. index ermittelt.
			// breakdate wird in kurse.db und breakindex in threads.db
			// abgelegt

			tdb.SetAktIDX(threadIDX);// selektieren
			// postdb mit breakdate neu berechnen, als Abfallprodukt gibt es den
			// breakindex,
			// dieser wird in threads.db gespeichert.

			postflag = pdb.BuildPostinglistEinThread(1, tdbo.getPageanz(),
					breakdate, 1, udb, tdbo, 0);

			if (postflag == false)
			{
				Tracer
						.WriteTrace(
								20,
								"W:symb <"
										+ symb
										+ "> nicht als thread vorhanden, Bitte erst herunterladen damit Auswertung der Superakt möglich => mache weiter");
				continue;

			} else
				tdb.WriteDB();
		}

	}

	public void BewerteBesteUser()
	{
		ThreadsDB tdb = new ThreadsDB();
	}

	/**
	 * @param dir
	 */

}
