package hilfsrepeattask;

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

import org.eclipse.swt.widgets.ProgressBar;

import ranking.GewinnBewertung;
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

public class SlidWorker
{
	// Es werden alle Slider weiter aufgefüllt und die Prognosen in
	// der Prognosen.db gepspeichert
	static UserGewStrategieDB2 ugewinndbII = new UserGewStrategieDB2(
			"UserThreadVirtualKonto\\Summengewinnedb");
	// static UserDB udb = new UserDB("observeuser.txt", "boostraenge.txt");
	private UserDB udb_g = null;
	private Prop2 prop2 = new Prop2(GC.rootpath
			+ "\\db\\UserThreadVirtualKonto\\config_usergewinne.prop");
	static PostDBx pdb = new PostDBx();
	private ThreadsDB tdb_g = null;

	// static Inf infMaster = new Inf();
	static Inf infMaster2 = new Inf();

	ThreadAttribStoreI tstore_g = null;
	String postinfname = null;
	int lastpostid_glob = 0, breakindex = 0, aktpostanz = 0;
	String lastdate_g = null;
	String froot = GC.rootpath;
	String lastcreatedatetime = null, aktdate = null;
	String startzeit = null;
	String lasterrorsymbol_glob = null, lastthreadname_glob = null;
	UserDbObj userdbobj = null;
	Htmlpage htmlZahlen = null, htmlTexte = null, htmlUserinfo = null;

	GewinnBewertung gewinnBewertung_glob = null;

	public SlidWorker(ThreadsDB tdb, UserDB udb)
	{
		tdb_g = tdb;
		udb_g = udb;
		gewinnBewertung_glob = new GewinnBewertung(ugewinndbII, udb_g, null);
	}

	private void BewerteSliderPosting(ThreadDbObj tdbo,
			CompressedPostingObj copost, UserDB udb,
			ThreadAttribStoreI attribstore, int ipos, int iposmax,Compressor comp)
	{
		// dieses Posting wird bzgl. Gewinninformation bewertet und der Gewinn
		// wird in den internen Strukturen abgespeichert

		// Das Posting wird in den Slider aufgenommen
		gewinnBewertung_glob.addSliderpostingSt1(copost);
		
		// jeden Tag werden einmal die Attribute Gesetzt !!!
		String date = Tools.entferneZeit(copost.getDatetime());
		if (date.equals(lastdate_g) == false)
		{
			// Jeden Tag nur einmal Attribute setzen
			//Bewerte den Slider
			Sliderbewert slbewert=gewinnBewertung_glob.holeSliderbewertung(comp, pdb);
			
			// Hier werden einfach die Slider-Attribute in den Attributstores
			// gespeichert
			if ((gewinnBewertung_glob.SetzeAttributeSt4(attribstore, copost,
					slbewert)) == false)
				Tracer.WriteTrace(20,
						"Warning: Fehler bei der Attributssetzung !!");
			lastdate_g = date;
		}
	}

	private void durchlaufeDiesenThreadGenPrognosen(int sliderstart,
			int postanz, ThreadDbObj tdbo, int anzthreads, String enddate,
			int wochen1flag, int wochen2flag, SlideruebersichtDB sldb,
			float zeitdiffminuten, float zeitlimit_minuten, int gelSlider,
			int i, Compressor comp)
	{
		// Zentrale Frage? wo werden denn hier die Prognosen generiert

		//a)Durchläuft eine Anzahl postings um einen Slider aufzubauen
		for (int j = sliderstart; j < postanz; j++)
		{
			int wochenindex = 0;
			//b)holt ein Posting
			CompressedPostingObj copost = comp.getObjectIDX(j);
			if (copost == null)
			{
				Tracer.WriteTrace(20,
						"Warning: copost==null compressed thread tid<"
								+ tdbo.getThreadid() + ">");
				continue;
			}

			// gib die zwischenslider 1 woche und 2 Wochen vor schluss aus
			int zeitdifftage = Tools.zeitdifferenz_tage(enddate, Tools
					.entferneZeit(copost.getDatetime()));
			if (zeitdifftage <= 8)
				wochenindex = 1;
			if ((zeitdifftage <= 14) && (zeitdifftage > 8))
				wochenindex = 2;

			//c)Bewerte dieses Postings bzw. store dieses in den Slider
			BewerteSliderPosting(tdbo, copost, udb_g, tstore_g, wochenindex,
					anzthreads,comp);

			//d) falls 1 Wochen vor dem Ende->speichere einmal und generiere die Prognose
			if ((wochenindex == 1) && (wochen1flag == 0))
			{
				wochen1flag = 1;
				gewinnBewertung_glob.calcAddUpdateStoreSliderGenPrognose(tdbo,
						sldb, comp, wochenindex, GC.TURBOFLAG, pdb);
			}
			// falls 2 wochen vor dem Ende
			if ((wochenindex == 2) && (wochen2flag == 0))
			{
				wochen2flag = 1;
				gewinnBewertung_glob.calcAddUpdateStoreSliderGenPrognose(tdbo,
						sldb, comp, wochenindex, GC.TURBOFLAG, pdb);
			}

			if (j % 200 == 0)
				System.out.println("i,j<" + i + "|" + j + "> worktime<"
						+ zeitdiffminuten + "|" + zeitlimit_minuten
						+ "> gelad Slider<" + gelSlider + ">");
		}
	}

	private void gibZwischenübersichtAus(int maxslidertage,
			Compressor comp, SlideruebersichtDB sldb,
			int auswertungsflag, int threadindex, Bank bank, int boniflag)
	{
		//maxslidertage: gibt nur slider aus die ein maximales alter haben
		//comp: komprimierte thread
		//sldb
		//bundelsize: hier wird anz gesammelt bis augegeben wird
		//auswertungsflag
		//threadindex: bei dem thread mit index i sind wir jetzt
		//bank:
		//boniflag:
		
		// gib die Zwischensliderübersicht aus
		tdb_g.WriteDB();
		sldb.WriteDB();
		sldb.workSlideruebersicht("1", tdb_g, GC.rootpath
				+ "\\db\\userthreadvirtualkonto\\slideruebersicht", 0, 0, 2000,
				maxslidertage, 0);
		sldb.workSlideruebersicht("2", tdb_g, GC.rootpath
				+ "\\db\\userthreadvirtualkonto\\slideruebersicht0_2", 0, 2,
				2000, maxslidertage, 0);
		// gib hier auch die alten slider aus
		sldb.workSlideruebersicht("3", tdb_g, GC.rootpath
				+ "\\db\\userthreadvirtualkonto\\slideruebersicht500T", 0, 0,
				2000, 500, GC.ALLFLAG);

		
		comp.cleanSlider();

		if (tstore_g != null)
			tstore_g.WriteDB();

		Prop2.setprop("lastvirtKontozaehler", String.valueOf(threadindex));
		ugewinndbII.GibZwischenGewinnListeAusCalcGewinnDelMem(0,
				auswertungsflag, threadindex, bank, boniflag, null);
		ugewinndbII.WriteDB();
		if (threadindex > 1)
			ugewinndbII.WriteDB();
	}

	private void abschluss(SlideruebersichtDB sldb, int auswertungsflag,
			int minprio, int maxprio, int maxslidertage, String zwischenzeit,
			float zeitdiffminuten, Bank bank, String startzeit,
			float zeitlimit_minuten, Inf inf, int anzthreads, int gelSlider,
			int boniflag)
	{
		// zeile ist nur zum test
		sldb.erstellePrognosen("P1", tdb_g, GC.rootpath
				+ "\\db\\userthreadvirtualkonto\\prognosen");

		// zeit überschritten
		inf.writezeile(Tools.get_aktdatetime_str() + "Slider fertig; minprio<"
				+ minprio + ">; maxprio<" + maxprio + ">; limitMin<"
				+ zeitlimit_minuten + ">; startz<" + startzeit + ">; endz<"
				+ zwischenzeit + "> ; dauer<" + zeitdiffminuten
				+ "> ; gel.Slider<" + gelSlider + ">");

		// sortiere und schreibe die DB (sortiere die alten slider an den
		// Anfang)

		tdb_g.WriteDB();
		sldb.WriteDB();
		sldb.workSlideruebersicht("1", tdb_g, GC.rootpath
				+ "\\db\\userthreadvirtualkonto\\slideruebersicht", 0, 0, 2000,
				maxslidertage, 0);
		sldb.workSlideruebersicht("2", tdb_g, GC.rootpath
				+ "\\db\\userthreadvirtualkonto\\slideruebersicht0_2", 0, 2,
				2000, maxslidertage, 0);

		// gib hier auch die alten Slider aus
		sldb.workSlideruebersicht("3", tdb_g, GC.rootpath
				+ "\\db\\userthreadvirtualkonto\\slideruebersicht500T_all", 0,
				0, 2000, 50, GC.ALLFLAG);

		sldb.erstellePrognosen("P1", tdb_g, GC.rootpath
				+ "\\db\\userthreadvirtualkonto\\prognosen");

		ugewinndbII.GibZwischenGewinnListeAusCalcGewinnDelMem(GC.DEFAULT,
				auswertungsflag, anzthreads, bank, boniflag, null);
		// udb.WriteDB();
		ugewinndbII.WriteDB();
	}

	private void fehlermeldungCorruptThread(ThreadDbObj tdbo)
	{
		Inf inf2 = new Inf();
		String msg = Tools.get_aktdatetime_str()
				+ " : Warning: endpost==null,compressed thread error tid<"
				+ tdbo.getThreadid() + ">";
		inf2.setFilename(GC.rootpath
				+ "\\db\\reporting\\corruptThread_von_Hand_korrektor.txt");
		inf2.writezeile(msg);
	}

	private void DurchlaufeGenPrognosen(SlideruebersichtDB sldb,
			int thresholdstunden, int bundelsize, int auswertungsflag,
			int minprio, int maxprio, int zeitlimitMinuten, int firstflag,
			int maxslidertage, ProgressBar pb,HashSet<Integer> tidmenge)
	{
		// durchlaufe alle Threads nach den Prioritätsklassen und genieriere die
		// Prognosen
		// Hier wird mit einem Threshold und Zeitlimit gearbeitet
		// threadsholdmin= thresholdminuten bis der Slider wieder upgedatet wird
		String zwischenzeit = "";
		float zeitdiffminuten = 0;
		Inf infc = new Inf();
		Bank bank = new Bank();
		infc.setFilename(GC.rootpath + "\\db\\reporting\\corruptThread.txt");

		sldb.ResetDB();
		String startzeit = Tools.get_aktdatetime_str();
		float zeitlimit_minuten = zeitlimitMinuten;
		Inf inf = new Inf();
		inf.setFilename(GC.rootpath + "\\db\\reporting\\zeitlimit.csv");
		if (firstflag == 1)
			inf
					.writezeile("________________________________________________________");

		tdb_g.ResetDB();
		MidDB middb=new MidDB(tdb_g);
		int sliderstartid = 0;
		int ausgabecounter = 0;
		int anzthreads = tdb_g.GetanzObj();
		int gelSlider = 0, wochen1flag = 0, wochen2flag = 0;
		pb.setMinimum(0);
		pb.setMaximum(zeitlimitMinuten);

		for (int threadidx = 0; threadidx < anzthreads; threadidx++)
		{
			
				
			zwischenzeit = Tools.get_aktdatetime_str();
			zeitdiffminuten = Tools.zeitdifferenz_minuten(startzeit,
					zwischenzeit);
			pb.setSelection((int) zeitdiffminuten);
			if (zeitdiffminuten > zeitlimit_minuten)
			{
				// zeit überschritten
				inf
						.writezeile(Tools.get_aktdatetime_str()
								+ "Zeitlimit Slider überschritten; minprio<"
								+ minprio + ">; maxprio<" + maxprio
								+ ">; limitMin<" + zeitlimit_minuten
								+ ">; startz<" + startzeit + ">; endz<"
								+ zwischenzeit + "> ; dauer<" + zeitdiffminuten
								+ "> ; gel.Slider<" + gelSlider + ">");
				break;
			}
			ThreadDbObj tdbo = (ThreadDbObj) tdb_g.GetObjectIDX(threadidx);

			if(tdbo.getState()!=0)
			{
				Tracer.WriteTrace(20, "Info: Thread <"+tdbo.getThreadname()+"> <"+tdbo.getEroeffnet()+">has state <"+tdbo.getState()+"> => bad thread, go on 454532222");
				threadidx++;
				continue;
			}
			
			//Falls nur bestimmte tid´s betrachtet werden
			if(tidmenge!=null)
				if(tidmenge.contains(tdbo.getThreadid())==false)
					continue;
			
			int prio = sldb.calcPrio(tdbo, GC.ALTERFLAG);

			if ((prio < minprio) || (prio > maxprio))
				continue;

			if (Tools.isKorrektSymbol(tdbo.getSymbol()) == false)
			{
				tdbo.setLastsliderupdateSORT(Tools.get_aktdatetime_str());
				tdbo.setErrorcode("01:Thread fehlerhaft:kein korrektes symbol<"
						+ tdbo.getSymbol() + ">");
				continue;
			}

			String lastupdate = sldb.getLastupdatetime(tdbo);
			if (Tools.zeitdifferenz_stunden(Tools.get_aktdatetime_str(),
					lastupdate) < thresholdstunden)
				continue;

			// erst mal den ganzen Thread kompressen
			char first = String.valueOf(tdbo.getThreadid()).charAt(0);

			String fn = GC.rootpath + "\\db\\compressedthreads\\@" + first
					+ "\\" + tdbo.getThreadid() + ".db";
			String fnmaster = tdbo.GetSavePageName2(1);

			System.out.println("Betrachte tid<"+tdbo.getThreadid() +"> fn<"+fn+">");
			if(tdbo.getThreadid()==772388)
				System.out.println("found Betrachte tid<772388>");
			
			
			// schaut nach ob sich das Datum von der masterseite gegenüber
			// dem
			// compressed threads unterscheidet, wenn ja wird compressed
			// thread
			// erstellt

			int zeitdiff = FileAccess.zeitdifferenz(fn, fnmaster);
			if (zeitdiff > 24)
				tdbo.updateCompressedThread(middb,threadidx, anzthreads, tdb_g,
						udb_g, 0);

			Compressor comp = null;
			try
			{
				comp = new Compressor(tdbo.getThreadid(), tdbo.getSymbol(),
						tdb_g);
			} catch (CompressedThreadException e)
			{
				Inf inf2 = new Inf();
				inf2
						.setFilename(GC.rootpath
								+ "\\db\\reporting\\corruptThread_losche_automatisch.txt");
				inf2.writezeile(e.getMessage());
				
				
				if(FileAccess.FileAvailable(fn)==true)
					FileAccess.FileDelete(fn, 0);
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				// aktualisiert die Zeit wann der slider das letzte mal
				// bearbeitet wurde
				tdbo.setLastsliderupdateSORT(Tools.get_aktdatetime_str());
				
				continue;
			}

			// lade postdbx (wird benötigt um festzustellen ob neue user
			// hinzugekommen sind)
			String filepostdbnam = GC.rootpath + "\\db\\threaddata\\"
					+ tdbo.getThreadname() + "_" + tdbo.getThreadid() + ".db";
			pdb.ReadPostDB(filepostdbnam);

			int postanz = comp.getanz();
			gewinnBewertung_glob.setNeuerThread(tdbo, tdbo
					.GetSavePageName_genDir(0), tdb_g);

			// Die Attribute wie Slidergüte etc. werde hier im tstore
			// gespeichert
			tstore_g = new ThreadAttribStoreI(tdbo.getThreadid(), "\\db\\Attribute\\Threads");

			// Holt sich die groesse des maximalen sliders
			int maxslidergroesse = tstore_g
					.getMaxattribGroesse(GC.ATTRIB_USERIN20TAGEN);

			// es wird nicht alles nochmal neu geladen sondern nur die
			// letzten
			// Postings + Sicherheit
			sliderstartid = postanz - (int) ((maxslidergroesse) * 1.3);
			if (sliderstartid < 0)
				sliderstartid = 0;
			if (maxslidergroesse == 0)
				sliderstartid = 0;

			CompressedPostingObj endpost = comp.getObjectIDX(postanz - 1);
			if (endpost == null)
			{
				fehlermeldungCorruptThread(tdbo);
				tdbo.setLastsliderupdateSORT(Tools.get_aktdatetime_str());
				continue;
			}

			String enddate = Tools.entferneZeit(endpost.getDatetime());

			// hier wird der thread durchlaufen und der slider aufgebaut !!
			// Auch die Attribute werden gesetzt !!
			durchlaufeDiesenThreadGenPrognosen(sliderstartid, postanz, tdbo,
					anzthreads, enddate, wochen1flag, wochen2flag, sldb,
					zeitdiffminuten, zeitlimit_minuten, gelSlider, threadidx,
					comp);

			// thread ausgewertet, slider speichern und löschen und gehe
			// weiter
			if ((threadidx % 100 == 0) || (threadidx == anzthreads - 1))
				gewinnBewertung_glob.calcAddUpdateStoreSliderGenPrognose(tdbo,
						sldb, comp, 0, 0, pdb);
			else
				gewinnBewertung_glob.calcAddUpdateStoreSliderGenPrognose(tdbo,
						sldb, comp, 0, GC.TURBOFLAG, pdb);

			wochen1flag = 0;
			wochen2flag = 0;

			if (gelSlider %bundelsize==0)
				gibZwischenübersichtAus(maxslidertage, comp,
						sldb,  auswertungsflag, threadidx, bank, 0);

			
			
			//Die Attribute werden gespeichert
			tstore_g.WriteDB();
			gelSlider++;
		}

		abschluss(sldb, auswertungsflag, minprio, maxprio, maxslidertage,
				zwischenzeit, zeitdiffminuten, bank, startzeit,
				zeitlimit_minuten, inf, anzthreads, gelSlider, 1);
	}

	public void GenPrognosen(SlideruebersichtDB sldb, int threshold, int suchmode,
			int bundelsize, int auswertungsflag, int minprio, int maxprio,
			int zeitlimit, int firstflag, int maxslidertage, ProgressBar pb,HashSet<Integer> tidmenge)
	{
		DurchlaufeGenPrognosen(sldb, threshold, bundelsize, auswertungsflag,
				minprio, maxprio, zeitlimit, firstflag, maxslidertage, pb,tidmenge);
	}
}
