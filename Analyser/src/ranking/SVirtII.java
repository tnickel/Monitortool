package ranking;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Prop2;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;

import java.util.HashSet;

import mainPackage.GC;
import objects.Htmlpage;
import objects.ThreadDbObj;
import objects.UserDbObj;
import objects.UserGewStrategieObjII;
import slider.Sliderbewert;
import stores.MidDB;
import stores.PostDBx;
import stores.SlideruebersichtDB;
import stores.ThreadsDB;
import stores.UserDB;
import stores.UserGewStrategieDB2;
import attribute.ThreadAttribStoreI;
import bank.Bank;

import compressor.Compressor;

import db.CompressedPostingObj;
import except.CompressedThreadException;

public class SVirtII extends SVirtBase
{
	static UserGewStrategieDB2 ugewinnstrategie2_g = new UserGewStrategieDB2("UserThreadVirtualKonto\\Bank\\Gewinnverlaufdb");
	// frage wie kommen neue objekte in ugewinnstrategie2_g ??

	static UserDB udb = new UserDB("observeuser.txt", "boostraenge.txt");
	private Prop2 prop2 = new Prop2(GC.rootpath
			+ "\\db\\UserThreadVirtualKonto\\config_usergewinne.prop");
	static PostDBx pdb = new PostDBx();
	ThreadsDB tdb_g = new ThreadsDB();

	static Inf infMaster2 = new Inf();
	
	ThreadAttribStoreI tstore_g = null;
	String postinfname = null;
	int lastpostid_glob = 0, breakindex = 0, aktpostanz = 0;
	String lastdate_g = null, lastdate2_g = null;
	String froot = GC.rootpath;
	String lastcreatedatetime = null, aktdate = null;
	String startzeit = null;
	String lasterrorsymbol_glob = null, lastthreadname_glob = null;
	UserDbObj userdbobj = null;
	Htmlpage htmlZahlen = null, htmlTexte = null, htmlUserinfo = null;

	static GewinnBewertung gewinnBewertung = new GewinnBewertung(
			ugewinnstrategie2_g, udb,null);
	static Bank bank = new Bank();

	

	private void BewerteAlleMasteruser(Sliderbewert slb, ThreadDbObj tdbo,
			CompressedPostingObj copost, UserDB udb,
			 int ipos, int iposmax)
	{
		// ipos=aktuelle position
		// iposmax=bis zu dieser position läuft der Alg.
		// ipos ist nur für die Ausgabe

		int anz = ugewinnstrategie2_g.GetanzObj();
		ugewinnstrategie2_g.ResetDB();

		// bewerte die einzelnen Masteruser mit der passenden Strategie
		for (int i = 0; i < anz; i++)
		{
			UserGewStrategieObjII u2obj = (UserGewStrategieObjII) ugewinnstrategie2_g
					.GetObjectIDX(i);
			if (u2obj.getUsertype_G() == GC.UsertypeMasteruser)
			{
				// falls das ein Masteruser ist, dann wende die
				// Handelsstrategie des Masterusers an
				gewinnBewertung.addMasteruserGewinnSt3(u2obj, slb, copost, tdbo,
						ipos, iposmax, lasterrorsymbol_glob);
			}
		}

		return;
	}

	private void BewerteEinzelneMasteruser(Sliderbewert slb, ThreadDbObj tdbo,
			CompressedPostingObj copost, UserDB udb, int ipos, int iposmax)
	{
		HashSet<String> masternamen = new HashSet<String>();
		masternamen.add("MasteruserI1946");
		/*masternamen.add("MasteruserI1955");
		masternamen.add("MasteruserI1703");
		masternamen.add("MasteruserI3953");*/

		int anz = ugewinnstrategie2_g.GetanzObj();
		ugewinnstrategie2_g.ResetDB();

		// bewerte die einzelnen Masteruser mit der passenden Strategie
		for (int i = 0; i < anz; i++)
		{
			UserGewStrategieObjII u2obj = (UserGewStrategieObjII) ugewinnstrategie2_g
					.GetObjectIDX(i);
			if (u2obj.getUsertype_G() == GC.UsertypeMasteruser)
			{
				String unam = u2obj.getUsername_G();
				if (masternamen.contains(unam) == false)
					continue;

				gewinnBewertung.addMasteruserGewinnSt3(u2obj, slb, copost, tdbo,
						ipos, iposmax, lasterrorsymbol_glob);

			}
		}

		return;
	}

	private void SetzeAttribute(CompressedPostingObj copost,
			ThreadAttribStoreI attribstore,Sliderbewert slb)
	{
		gewinnBewertung.SetzeAttributeSt4(attribstore, copost,slb);
	}

	private void BewerteStandartuserposting(Compressor comp, Sliderbewert slb,
			ThreadDbObj tdbo, CompressedPostingObj copost, UserDB udb,
			ThreadAttribStoreI attribstore, int ipos, int iposmax, Bank bank,
			int sliderauswertungsflag)
	{
		// dieses Posting wird bzgl. Gewinninformation bewertet und der Gewinn
		// wird in den internen Strukturen abgespeichert
		int useranz = udb.GetanzObj();
		String pusername = copost.getUsername();
		String threadname = tdbo.getThreadname();

		int rank = 0;

		UserDbObj udbo = udb.getUserobj(pusername);

		if (udbo == null)
			rank = useranz / 2;
		else
			rank = udbo.getRang();

		if (rank < 0)
			rank = useranz / 2;

		if (rank < 0)
			Tracer.WriteTrace(10, "Error:internal rang muss >0 sein rank<"
					+ rank + ">");

		// a)jedes Posting eines users wird gewertet
		if (4 == 5)
			gewinnBewertung.werteVerfahren0(0, copost, threadname, tdbo
					.getThreadid(), tdbo.getThreadname(), lasterrorsymbol_glob,
					tdbo.getThreadname(), tdbo.getSymbol(), ipos, iposmax);

		String date = Tools.entferneZeit(copost.getDatetime());
		if ((date.equals(lastdate_g) == false) && (4 == 4))
		{

			// neues Datum, Bewerte Masterposting
			lastdate_g = date;

			if ((4 == 5) && (sliderauswertungsflag == 1))
				gewinnBewertung.werteVerfahren3Masteruser(slb, "Masteruser", 3,
						copost, threadname, tdbo.getThreadid(), tdbo
								.getThreadname(), lasterrorsymbol_glob, tdbo
								.getThreadname(), ipos, iposmax, 1000, 5,
						attribstore);

			if ((4 == 5) && (sliderauswertungsflag == 1))
				gewinnBewertung.werteVerfahren3Masteruser(slb, "MasteruserSI",
						3, copost, threadname, tdbo.getThreadid(), tdbo
								.getThreadname(), lasterrorsymbol_glob, tdbo
								.getThreadname(), ipos, iposmax, 2000, 15,
						attribstore);

			if ((4 == 5) && (sliderauswertungsflag == 1))
				gewinnBewertung.werteVerfahren3Masteruser(slb, "MasteruserTSI",
						3, copost, threadname, tdbo.getThreadid(), tdbo
								.getThreadname(), lasterrorsymbol_glob, tdbo
								.getThreadname(), ipos, iposmax, 5000, 40,
						attribstore);

			if ((4 == 5) && (sliderauswertungsflag == 1))
				gewinnBewertung.werteVerfahren4Masteruser(slb, "MasteruserX",
						3, copost, threadname, tdbo.getThreadid(), tdbo
								.getThreadname(), lasterrorsymbol_glob, tdbo
								.getThreadname(), ipos, iposmax, 5000, 15,
						attribstore);

			// dies ist ein dummy masteruser der einfach nur kauft. Hier möchte
			// man sehen was der dumme für
			// gewinne macht
			if ((4 == 5) && (sliderauswertungsflag == 1))
				gewinnBewertung.werteVerfahren5Masteruser("MasteruserRAN", 0,
						copost, threadname, tdbo.getThreadid(), tdbo
								.getThreadname(), lasterrorsymbol_glob, tdbo
								.getThreadname(), ipos, iposmax, 5000, 15,
						attribstore);

			// 38x1
			if (4 == 4)
				gewinnBewertung.werteVerfahren38x1Masteruser(comp,
						"Masteruser38x1", copost, tdbo, lasterrorsymbol_glob,
						bank);
			// 38x2
			if (4 == 4)
				gewinnBewertung.werteVerfahren38x2Masteruser(comp,
						"Masteruser38x2", copost, tdbo, lasterrorsymbol_glob,
						bank);
			// 38x3
			if (4 == 4)
				gewinnBewertung.werteVerfahren38x3Masteruser(comp,
						"Masteruser38x3", copost, tdbo, lasterrorsymbol_glob,
						bank);
			// 38x4
			if (4 == 4)
				gewinnBewertung.werteVerfahren38x4Masteruser(comp,
						"Masteruser38x4", copost, tdbo, lasterrorsymbol_glob,
						bank);
			// 38-200b
			if ((4 == 5) && (sliderauswertungsflag == 1))
				gewinnBewertung.werteVerfahren13Masteruser(comp,
						"Masteruser38-200b", 4, copost, threadname, tdbo
								.getThreadid(), tdbo.getThreadname(),
						lasterrorsymbol_glob, tdbo.getThreadname(), ipos,
						iposmax, 5000, 15, attribstore);

			if ((4 == 5) && (sliderauswertungsflag == 1))
				gewinnBewertung.werteVerfahren14Masteruser(comp,
						"Masteruser38-200b2", 4, copost, threadname, tdbo
								.getThreadid(), tdbo.getThreadname(),
						lasterrorsymbol_glob, tdbo.getThreadname(), ipos,
						iposmax, 5000, 15, attribstore);

			if ((4 == 5) && (sliderauswertungsflag == 1))
				gewinnBewertung.werteVerfahren15Masteruser(comp,
						"Masteruser38-200b3", 4, copost, threadname, tdbo
								.getThreadid(), tdbo.getThreadname(),
						lasterrorsymbol_glob, tdbo.getThreadname(), ipos,
						iposmax, 5000, 15, attribstore);

			// 38-200vertrau
			if ((4 == 5) && (sliderauswertungsflag == 1))
				gewinnBewertung.werteVerfahren7Masteruser(comp, slb,
						"Masteruser38-200vertrau", 4, copost, threadname, tdbo
								.getThreadid(), tdbo.getThreadname(),
						lasterrorsymbol_glob, tdbo.getThreadname(), ipos,
						iposmax, 5000, 15, attribstore);

			// MasteruserXBad (dieser Masteruser handelt bei schlechten Usern
			if ((4 == 5) && (sliderauswertungsflag == 1))
				gewinnBewertung.werteVerfahren8MasteruserBad(comp, slb,
						"MasteruserXbad", 3, copost, threadname, tdbo
								.getThreadid(), tdbo.getThreadname(),
						lasterrorsymbol_glob, tdbo.getThreadname(), ipos,
						iposmax, 5000, 15, attribstore);

			// der MasteruserX2 ist ein experimentaler user des MasterusersX
			if ((4 == 5) && (sliderauswertungsflag == 1))
				gewinnBewertung.werteVerfahren9Masteruser(slb, "MasteruserX2",
						3, copost, threadname, tdbo.getThreadid(), tdbo
								.getThreadname(), lasterrorsymbol_glob, tdbo
								.getThreadname(), ipos, iposmax, 10000, 15,
						attribstore);
			// der MasteruserX3 ist ein experimentaler user des MasterusersX
			if ((4 == 5) && (sliderauswertungsflag == 1))
				gewinnBewertung.werteVerfahren10Masteruser(slb, "MasteruserX3",
						3, copost, threadname, tdbo.getThreadid(), tdbo
								.getThreadname(), lasterrorsymbol_glob, tdbo
								.getThreadname(), ipos, iposmax, 12000, 15,
						attribstore);

			// der MasteruserX4 ist ein experimentaler user des MasterusersX
			if ((4 == 5) && (sliderauswertungsflag == 1))
				gewinnBewertung.werteVerfahren11Masteruser(slb, "MasteruserX4",
						3, copost, threadname, tdbo.getThreadid(), tdbo
								.getThreadname(), lasterrorsymbol_glob, tdbo
								.getThreadname(), ipos, iposmax, 5000, 5,
						attribstore);
			// MasteruserXNeu
			if ((4 == 5) && (sliderauswertungsflag == 1))
				gewinnBewertung.werteVerfahren12Masteruser(slb,
						"MasteruserXNeu", 3, copost, threadname, tdbo
								.getThreadid(), tdbo.getThreadname(),
						lasterrorsymbol_glob, tdbo.getThreadname(), ipos,
						iposmax, 25000, 10, attribstore);
		}
	}

	private void BewertePosting(Compressor comp, ThreadDbObj tdbo,
			CompressedPostingObj copost, UserDB udb,
			ThreadAttribStoreI attribstore, int ipos, int iposmax, PostDBx pdb,
			Bank bank, int sliderauswertungsflag,
			int standartuserauswertungsflag)
	{
		Sliderbewert slb = null;

		if (sliderauswertungsflag == 1)
		{
			gewinnBewertung.addSliderpostingSt1(copost);
			slb = gewinnBewertung
					.BewerteSliderSt2(GC.SLIDERINDEX20TAGE, comp, pdb);
		}
		// Der Standartuser wird nur beim Ranking berücksichtigt
		if (standartuserauswertungsflag == 1)
			BewerteStandartuserposting(comp, slb, tdbo, copost, udb,
					attribstore, ipos, iposmax, bank, sliderauswertungsflag);

		if (istNeuesDatum(copost) == false)
			return;

		// hier wird geprüft ob man schon mal was verkaufen kann
		if (standartuserauswertungsflag == 1)
			bank.ueberpruefeAlleDepots(
					Tools.entferneZeit(copost.getDatetime()), 0);

		if (sliderauswertungsflag == 1)
		{
			//Hier werden alle Masteruser bewertet
			/*BewerteAlleMasteruser(slb, tdbo, copost, udb, ipos,
					iposmax);*/
			
			//Bewerte nur spezielle Masteruser aus dem Pool(1...6000)
			BewerteEinzelneMasteruser(slb, tdbo, copost, udb, ipos, iposmax);

			SetzeAttribute(copost, attribstore,slb);
		}
	}

	private void DurchlaufeAlleThreads(int lastkontozaehler, int bundelsize,
			int auswertungsflag, int sliderauswertungsflag,int standartuserauswertungsflag,ThreadsDB tdb)
	{
		SlideruebersichtDB sldb = null;
		String lastdate = null;
		Compressor comp = null;
		MidDB middb=new MidDB(tdb);

		if (sliderauswertungsflag == 1)
			sldb = new SlideruebersichtDB();

		tdb_g.ResetDB();
		int anzthreads = tdb_g.GetanzObj();

		for (int i = 0; i < anzthreads; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) tdb_g.GetObjectIDX(i);
			if (Tools.isKorrektSymbol(tdbo.getSymbol()) == false)
				continue;

			if ((tdbo.getThreadname().contains("dax"))
					|| (tdbo.getThreadname().contains("DAX")))
				continue;

			// erst mal den ganzen Thread kompressen
			char first = String.valueOf(tdbo.getThreadid()).charAt(0);
			String fn = GC.rootpath + "\\db\\compressedthreads\\@" + first
					+ "\\" + tdbo.getThreadid() + ".db";
			String fnmaster = tdbo.GetSavePageName2(1);

			// schaut nach ob sich das Datum von der masterseite gegenüber dem
			// compressed threads unterscheidet

			if (FileAccess.zeitdifferenz(fn, fnmaster) > 24)
				tdbo.updateCompressedThread(middb,i, anzthreads, tdb_g, udb, 0);

			try
			{
				comp = new Compressor(tdbo.getThreadid(), tdbo.getSymbol(),tdb);
			} catch (CompressedThreadException e)
			{
				{
					Inf inf = new Inf();
					inf
							.setFilename(GC.rootpath
									+ "\\db\\reporting\\corruptThread_von_Hand_korrektor.txt");
					inf.writezeile(e.getMessage());
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
			}
			int postanz = comp.getanz();

			gewinnBewertung
					.setNeuerThread(tdbo, tdbo.GetSavePageName_genDir(0),tdb);

			//Attributstore generieren
			tstore_g = new ThreadAttribStoreI(tdbo.getThreadid(),"\\db\\Attribute\\Threads");

			//Alle Postings des Thread auswerten
			for (int j = 0; j < postanz; j++)
			{
				CompressedPostingObj copost = comp.getObjectIDX(j);
				if (copost == null)
				{
					Tracer.WriteTrace(20, "Warning: copost==null");
					continue;
				}
				lastdate = Tools.entferneZeit(copost.getDatetime());
				BewertePosting(comp, tdbo, copost, udb, tstore_g, i,
						anzthreads, pdb, bank, sliderauswertungsflag,
						standartuserauswertungsflag);

				if ((j % 200 == 0) && (sliderauswertungsflag == 1))
					System.out.println("i,j<" + i + "|" + j + "> "
							+ gewinnBewertung.showSlider());
				else if (j % 200 == 0)
					System.out.println("i,j<" + i + "|" + j + ">");
			}

			// nach einer Runde immer Verkaufen, hier kann man nicht mehrere
			// Runden Puffern
			// da sonst die Verkaufszeiten nicht mehr stimmen.
			bank.ueberpruefeAlleDepots(lastdate, 1);
			bank.speichere();
			if (i % bundelsize == 0)
			{
				if (sliderauswertungsflag == 1)
					sldb
							.workSlideruebersicht(
									"0",
									tdb_g,
									GC.rootpath
											+ "\\db\\userthreadvirtualkonto\\sliderübersicht.csv",
									0, 0, 2000, 60, 0);
			}
			// thread ausgewertet, slider speichern und löschen und gehe weiter

			if (sliderauswertungsflag == 1)
				if ((i % 100 == 0) || (i == (anzthreads - 1)))
					gewinnBewertung.calcAddUpdateStoreSliderGenPrognose(tdbo,
							sldb, comp, 0, 0, pdb);
				else
					gewinnBewertung.calcAddUpdateStoreSliderGenPrognose(tdbo,
							sldb, comp, 0, GC.TURBOFLAG, pdb);

			// lösche den Speicher
			if (sliderauswertungsflag == 1)
				comp.cleanSlider();

			if (tstore_g != null)
				tstore_g.WriteDB();

			if (i % bundelsize == 1)
			{
				Prop2.setprop("lastvirtKontozaehler", String.valueOf(i));
				ugewinnstrategie2_g.GibZwischenGewinnListeAusCalcGewinnDelMem(
						0, auswertungsflag, i, bank, 0,null);
				// thread beendet und bundelsize überschritten

				ugewinnstrategie2_g.WriteDB();
				if (i > 1)
					ugewinnstrategie2_g.WriteDB();
			}
		}
		// am Ende zahle den Bonus aus
		ugewinnstrategie2_g.GibZwischenGewinnListeAusCalcGewinnDelMem(
				GC.DEFAULT, auswertungsflag, anzthreads, bank, 1,null);

		// udb.WriteDB();
		ugewinnstrategie2_g.WriteDB();
	}

	public void SucheBestenMasteruser(int suchmode, int bundelsize,
			int auswertungsflag, int sliderauswertungsflag,int standartuserauswertungsflag,ThreadsDB tdb)
	{

		// suchmode gibt an wie im fehlerfall umzugehen ist
		// GC.Default = Geht gnadenlos weiter falls fehlerhafte kurse gemeldet
		// werden
		// GC.SUCHMODE_UEBERSPRINGE_FEHLER
		// bundlesize:grösse für die auswertung, es werden #bundlesize Aktien
		// bewertet und dann
		// auf Festplatte das Ergebniss geschrieben
		// auswertungsflag: 0 default
		// 1 schreibe keine Einzelgewinne
		// sliderauswertungsflag 0: die slider werden nicht ausgewertet
		// 1: mit auswertung der slider

		String laststring = Prop2.getprop("lastvirtKontozaehler");
		int lastkontozaehler = Integer.parseInt(laststring);

		if (lastkontozaehler == 0)
			if (FileAccess.FileAvailable(GC.rootpath
					+ "\\db\\UserThreadVirtualKonto\\uebersicht.txt"))
				FileAccess.FileDelete(GC.rootpath
						+ "\\db\\UserThreadVirtualKonto\\uebersicht.txt",0);
		if (lastkontozaehler == 0)
			ugewinnstrategie2_g.initMasteruser();

		// lösche alle gewinne da die liste neu erstellt wird
		if (lastkontozaehler == 0)
			ugewinnstrategie2_g.GibZwischenGewinnListeAusCalcGewinnDelMem(
					GC.LOESCHE, auswertungsflag, 0, bank, 0,null);
		DurchlaufeAlleThreads(lastkontozaehler, bundelsize, auswertungsflag,
				sliderauswertungsflag,standartuserauswertungsflag,tdb);
	}
}
