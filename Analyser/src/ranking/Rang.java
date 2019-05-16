package ranking;

import hilfsklasse.FileAccess;
import hilfsklasse.Prop2;
import hilfsklasse.Tracer;
import mainPackage.GC;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

import stores.AktDB;
import stores.ThreadsDB;
import stores.UserDB;
import stores.UserPostingverhaltenDB;

public class Rang
{
	// diese Klasse dient zur Rangberechung und beinhaltet einige statische
	// Funktionen
	static private UserPostingverhaltenDB userpostverhdb_glob = new UserPostingverhaltenDB();
	

	public Rang()
	{
		Tracer.WriteTrace(10, "Info: bitte mit Rang(UserDB udb) aufrufen !!");
		
	}
	
	static public void calcSchedulerRank(int rangmode, ProgressBar pb,
			Display dis,Text text,String pref,String rangfile,ThreadsDB tdb,AktDB aktdb,UserDB udb)
	{
		// Diese Klasse berechnet das Ranking was im Scheduler verwendet wird
		// rangmode=0 => ohne usergewinne
		// rangmode=GC.USERGEWINNRANG

		String laststring = Prop2.getprop("lastvirtKontozaehler");
		int lastkontozaehler = Integer.parseInt(laststring);
		Rangparameter rp = new Rangparameter(rangfile);

		if (GC.ONLINEMODE == 0)
		{
			Tracer.WriteTrace(20, "Info: Offlinemode kein Ranking");
			return;
		}

		// Step1u.2 Nur einmal im Monat
		if (rangmode == GC.USERGEWINNRANG)
			if (FileAccess.CheckIsFileOlderHours(GC.rootpath
					+ "\\db\\UserThreadVirtualKonto\\uebersicht.txt", 30 * 24) == true)
			{
				// Step 0,1: Hier werden Gewinnränge anhand des Postverhaltens
				// berechnet
				// Step 0
				SammleGrundgewinne sg = new SammleGrundgewinne(udb);
				sg
						.StartBewertungSt1(GC.SUCHMODE_UEBERSPRINGE_FEHLER,
								200, GC.DEFAULT, 500000, 0, pb,
								lastkontozaehler, null, null,tdb);
				// Step 1
				sg.CalcGewinnRaengeSt2(text,dis,pb);
			}

		// Step2: Hier wird ein Gewinnrang anhand der Userdaten
		// berechnet
		// Step 2 alle Userdaten aktualisieren
		//UserDB udb = new UserDB("observeuser.txt", "boostraenge.txt");
		//AktDB aktdb = new AktDB();
		udb.AktuallisiereAlleUserCalRang(aktdb, rangmode, 0, 96, pb, dis,text,pref);

		// Step 3 (Step 0,1,2 werden zum Gesammtrang addiert)
		// Also Postverhalten(Wo postet der User zu welchen Zeiten) und
		// Userverhalten(Was macht der User überhaupt, sein Handeln ?) =>
		// resultieren zum Gesammtgewinnrang
		
		udb.BaueRankingListe(GC.USERGEWINNRANG,1,rp,aktdb,true,userpostverhdb_glob);
	
	}

	static public void calcTunedRang(int rangmode, ProgressBar pb, Display dis,
			int startindex, int bundlesize, Text aktion, ProgressBar pb1,
			ProgressBar pb2, ProgressBar pb3, ProgressBar pb4,String pref,String rangfile,AktDB aktdb,ThreadsDB tdb,UserDB udb)
	{
		Rangparameter rp = new Rangparameter(rangfile);
		// Hier in dieser Rangberechnung kann noch einiges getuned werden
		// rangmode=0 => ohne usergewinne
		// rangmode=GC.USERGEWINNRANG
		// aktion: gibt zusatzinfo zur jeweiligen aktion aus
		pb1.setMaximum(100);
		pb2.setMaximum(100);
		pb3.setMaximum(100);
		pb4.setMaximum(100);

		// Step1u.2 Nur einmal im Monat
		if (rangmode == GC.USERGEWINNRANG)
		{
			// Step 0,1: Hier werden Gewinnränge anhand des Postverhaltens
			// berechnet
			pb1.setSelection(100);
			// Step 0: Sammelt die Grundgewinne
			SammleGrundgewinne sg = new SammleGrundgewinne(udb);
			sg.StartBewertungSt1(GC.SUCHMODE_UEBERSPRINGE_FEHLER, bundlesize,
					GC.DEFAULT, 500000, 0, pb, startindex, aktion, dis,tdb);

			// Step 1: Erstellt die User-Gewinnliste
			pb2.setSelection(100);
			sg.CalcGewinnRaengeSt2(aktion, dis,pb);
		}

		// Step2: Hier wird ein Gewinnrang anhand der Userdaten
		// berechnet
		// Step 2 alle Userdaten aktualisieren
		//UserDB udb = new UserDB("observeuser.txt", "boostraenge.txt");
		//AktDB aktdb = new AktDB();
		// Step 2: Userdaten aktualisieren
		pb3.setSelection(100);
		udb.AktuallisiereAlleUserCalRang(aktdb, rangmode, 0, 96, pb, dis,aktion,pref);
		udb.WriteDB();
		
		// Step 3: (Step 0,1,2 werden zum Gesammtrang addiert)
		// Also Postverhalten(Wo postet der User zu welchen Zeiten) und
		// Userverhalten(Was macht der User überhaupt, sein Handeln ?) =>
		// resultieren zum Gesammtgewinnrang

		// Step 3: Rangliste bauen
		pb4.setSelection(100);
		udb.BaueRankingListe(GC.USERGEWINNRANG,1,rp,aktdb,true,userpostverhdb_glob);
		udb.WriteDB();
		System.out.println("fertig");
	}
}
