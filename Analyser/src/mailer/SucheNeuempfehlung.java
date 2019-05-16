package mailer;

import hilfsklasse.FileAccess;
import hilfsklasse.FileAccessDyn;
import hilfsklasse.Memsucher;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;

import java.util.HashMap;
import java.util.HashSet;

import mainPackage.GC;
import objects.BBeventDbObj;
import objects.BoersenblattDbObj;
import objects.KeyDbObj;
import stores.BBeventDB;
import stores.BoersenBlaetterDB;
import stores.KeyDB;
import stores.ThreadsDB;

public class SucheNeuempfehlung extends Sucher_dep
{
	// das ist die DB wo die ergebnisse drin sind
	// private BoersenBlaetterDB boerblattdb_glob=new BoersenBlaetterDB();

	// keydb ist die db mit den suchw�rtern
	private KeyDB kdb = new KeyDB();

	// hier werden f�r alle Schl�sselw�rter geschaut in welchen
	// Mails die sind
	// Es werden dann auch die Datenbasen abgeglichen
	// F�r jedes Datenfile existiert ein Eintrag in BoersenBlaetterDB
	public void sucheAlles(ThreadsDB tdb, String mindat,
			BoersenBlaetterDB boerblattdb)
	{
		// gehe durch jedes File und schaue ob die Suchw�rter drin sind
		// Wenn ja wird dies in Boerblatt.db gemerkt

		// der Hitcounter zeigt an wie oft jedes keywort vorkommt
		kdb.clearHitcounter();

		FileAccessDyn fdyn = new FileAccessDyn();
		fdyn.initFileSystemList(GC.textzielbase, 0);

		// gehe durch alle Verzeichnisse
		int anz = fdyn.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String fnam = fdyn.holeFileSystemName();
			System.out.println("Betrachte Verzeichniss(" + i + "|" + anz + ")<"
					+ fnam + ">");

			// Durchsucht ein Verzeichniss nach Keyw�rtern
			checkVerzeichnisL(fnam, mindat, kdb, tdb, boerblattdb);
		}

		tdb.WriteDB();
	}

	private void checkVerzeichnisL(String verznam, String mindat, KeyDB kdb,
			ThreadsDB tdb, BoersenBlaetterDB boerblattdb)
	{
		//bdb ist die Neuempfehlungsdb, hier werden die bisherigen Suchergebnisse gespeichert
		BBeventDB bdb = new BBeventDB();

		// �berpr�ft alle Files in dem Verzeichniss auf die Keyw�rter der Keydb
		FileAccessDyn fdyn2 = new FileAccessDyn();
		fdyn2.initFileSystemList(GC.textzielbase + "\\" + verznam, 1);
		int anz = fdyn2.holeFileAnz();
		int geshitzaehler = 0;

		// Gesammtkeymenge alle Suchwkns
		HashSet<String> symbol_gesmenge = boerblattdb.getBoerGesMenge();

		// Liste von Boersenbl�ttern mit Keymengen
		// Also f�r jedes Boersenblatt wird gespeichert welche Schl�sselw�rter drin sind
		HashMap<String, HashSet<String>> boermenge = boerblattdb.getBoerMenge();

		String aktdate = Tools.entferneZeit(Tools.get_aktdatetime_str());

		// gehe durch alle files in einem Verzeichniss
		for (int i = 0; i < anz; i++)
		{
			System.out.print("," + i + "");
			// �berpr�ft einen Dateinamen nach Keyw�rtern
			String pdfnam = fdyn2.holeFileSystemName();
			String fnamtext = GC.textzielbase + "\\" + verznam + "\\" + pdfnam;
			System.out.println("Betrachte file<" + fnamtext + ">");

			int len = FileAccess.FileLength(fnamtext);
			if (len > 1000 * 1000)
			{
				Tracer.WriteTrace(20, "Info: file<" + fnamtext + "> zu gross");
				continue;
			}

			Memsucher mem = new Memsucher(fnamtext);

			if (mem.getlen() == 0)
			{
				Tracer.WriteTrace(20, "Warning: filelen==0 <" + fnamtext
						+ "> forget it");
				continue;
			}
			
			//schaue nur in neuere Files (alter h�chstens 20 Tage)
			if (mem.checkFiledat(mindat) == false)
				continue;

			// gehe durch alle Schl�sselw�rter
			int kanz = kdb.GetanzObj();
			for (int j = 0; j < kanz; j++)
			{
				KeyDbObj keyobj = (KeyDbObj) kdb.GetObjectIDX(j);
				String suchwort = keyobj.getKeyword();

				System.out.println("Betrachte Schl�sselwort<"
						+ keyobj.getKeyword() + ">");
				if (j % 100 == 0)
					System.out.print(".");

				// falls das Schluessenwort in dem datenfile gefunden wurde
				if ((suchwort.length() > 1)
						&& (mem.lookKeyword(keyobj)) == true)
				{
					// nimm das schl�sselwort in die ergebnissliste auf
					boerblattdb.AddKeyword(verznam, fnamtext, keyobj);

					if (symbol_gesmenge.contains(suchwort) == false)
					{
						Tracer.WriteTrace(
								20,
								"Info:A***** NEUEMPFEHLUNG �BER ALLES Ein ganz neues Keywort<"
										+ suchwort
										+ "> (�ber alle Boersenbl�tter)gefunden");
						symbol_gesmenge.add(suchwort);

						BBeventDbObj bb = new BBeventDbObj(aktdate + "#"
								+ "NEUEMPFEHLUNG UEBER ALLE BB" + "#"
								+ suchwort + "#" + keyobj.getAname() + "#"
								+ verznam + "#" + fnamtext);
						bdb.AddObject(bb);
					}

					// Falls der Boersenblatt schon in der Liste
					if (boermenge.containsKey(verznam) == true)
					{
						HashSet<String> symbmenge = boermenge.get(verznam);
						// schaue nach ob f�r dieses Boersenblatt das
						// WknSuchwort schon bekannt
						if (symbmenge.contains(suchwort) == false)
						{
							Tracer.WriteTrace(20,
									"Info: NEUEMPFEHLUNG FUER EIN BOERSENBLATT Ein neues Keywort<"
											+ suchwort + "> f�r das Boerblatt<"
											+ verznam + "> gefunden");
							symbmenge.add(suchwort);
				
						}
					} else
					{
						// Das Boersenblatt ist ganz neu dann nimm auf
						Tracer.WriteTrace(20,
								"Info: Ganz neues Boersenblatt!! Suchwort <"
										+ suchwort + "> f�r das Boerblatt<"
										+ verznam + "> gefunden");

						HashSet<String> symbmenge = new HashSet<String>();
						symbmenge.add(suchwort);
						boermenge.put(verznam, symbmenge);
					}

					BBeventDbObj bb = new BBeventDbObj(aktdate + "#"
							+ "NEUEMPFEHLUNG FUER BB" + "#"
							+ suchwort + "#" + keyobj.getAname() + "#"
							+ verznam + "#" + fnamtext);
					bdb.AddObject(bb);
					
					
					// Erh�he den Hitcounter,
					keyobj.setHitcounter((keyobj.getHitcounter()) + 1);

					geshitzaehler++;

					// Markiere in tdb das das B�rsenblatt eine Mid pusched
					int mid = keyobj.getMid();
					String lastpusch = BoersenblattDbObj.calcMsgDate(fnamtext);
					tdb.setBBlastPusch(mid, lastpusch);
				}
				
				// System.out.println("");
			}
			bdb.WriteDB();
			Tracer.WriteTrace(20, "Info: geshitzaehler<" + geshitzaehler + ">");
			boerblattdb.WriteDB();
			kdb.WriteDB();
		}
		System.out.println("");
	}
}
