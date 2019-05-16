package ranking;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
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
import stores.ThreadsDB;
import stores.UserDB;
import stores.UserGewStrategieDB2;
import attribute.ThreadAttribStoreI;
import bank.Bank;

import compressor.Compressor;

import db.CompressedPostingObj;
import except.CompressedThreadException;

public class SVirtIII extends SVirtBase
{
	static UserGewStrategieDB2 ugewinnstrategie2_g = new UserGewStrategieDB2(
			"UserThreadVirtualKonto\\Einzelbewertung\\Bank\\Gewinnverlaufdb");
	// frage wie kommen neue objekte in ugewinnstrategie2_g ??
	static UserDB udb = new UserDB("observeuser.txt", "boostraenge.txt");
	static PostDBx pdb = new PostDBx();
	ThreadsDB tdb_g = null;
	static Inf infproto = new Inf();
	static Inf infMaster2 = new Inf();
	ThreadAttribStoreI tstore_g = null;
	String postinfname = null;
	int lastpostid_glob = 0, breakindex = 0, aktpostanz = 0;
	//String lastdate_g = null, lastdate2_g = null;
	String froot = GC.rootpath;
	String lastcreatedatetime = null, aktdate = null;
	String startzeit = null;
	String lasterrorsymbol_glob = null, lastthreadname_glob = null;
	UserDbObj userdbobj = null;
	Htmlpage htmlZahlen = null, htmlTexte = null, htmlUserinfo = null;

	static GewinnBewertung gewinnBewertung_glob = new GewinnBewertung(
			ugewinnstrategie2_g, udb, "Einzelbewertung");
	static Bank bank = new Bank();

	public SVirtIII(ThreadsDB tdb)
	{
		tdb_g=tdb;
	}
	private void BewerteEinzelneMasteruserSt3(Sliderbewert slb,
			ThreadDbObj tdbo, CompressedPostingObj copost, UserDB udb,
			int ipos, int iposmax)
	{
		HashSet<String> masternamen = new HashSet<String>();
		masternamen.add("MasteruserI1946");

		int anz = ugewinnstrategie2_g.GetanzObj();
		ugewinnstrategie2_g.ResetDB();

		// bewerte die einzelnen Masteruser mit der passenden Strategie
		for (int i = 0; i < anz; i++)
		{
			UserGewStrategieObjII GewinnstratObj = (UserGewStrategieObjII) ugewinnstrategie2_g
					.GetObjectIDX(i);
			if (GewinnstratObj.getUsertype_G() == GC.UsertypeMasteruser)
			{
				String unam = GewinnstratObj.getUsername_G();
				if (masternamen.contains(unam) == false)
					continue;

				gewinnBewertung_glob.addMasteruserGewinnSt3(GewinnstratObj,
						slb, copost, tdbo, ipos, iposmax, lasterrorsymbol_glob);
			}
		}
		return;
	}

	private void BewertePosting(Compressor comp, ThreadDbObj tdbo,
			CompressedPostingObj copost, UserDB udb,
			ThreadAttribStoreI attribstore, int ipos, int iposmax, PostDBx pdb,
			int standartuserauswertungsflag, int protoflag)
	{

		// Hier wird genau 1 Posting bewertet, und maximal 1 Posting pro Tag
		Sliderbewert slb = null;

		// Nimm Posting im Slider auf
		gewinnBewertung_glob.addSliderpostingSt1(copost);

		// Bewerte den Slider
		slb = gewinnBewertung_glob.BewerteSliderSt2(GC.SLIDERINDEX20TAGE, comp,
				pdb);

		//Gib den Slider aus
		if(protoflag==1)
			gewinnBewertung_glob.dumpSlider(GC.rootpath+"\\db\\userthreadvirtualkonto\\einzelbewertung\\attribute\\slider.txt");
		
		if(protoflag==1)
			infproto.writezeile("   Bewerte Slider postid<"+copost.getPostid()+"> dat<"+Tools.entferneZeit(copost.getDatetime())+"> unam<"+copost.getUsername()+"> "+slb.getSliderInfostring());
			
		if (istNeuesDatum(copost) == false)
			return;

		// Bewerte nur spezielle Masteruser aus dem Pool(1...6000)
		BewerteEinzelneMasteruserSt3(slb, tdbo, copost, udb, ipos, iposmax);

		// Speichere die Attribute
		if(protoflag==1)
			infproto.writezeile("Info: speichere ATTRIBUTstore postid<"+copost.getPostid()+"> dat<"+Tools.entferneZeit(copost.getDatetime())+">");
		
		gewinnBewertung_glob.SetzeAttributeSt4(attribstore, copost, slb);
	}

	private void DurchlaufeAlleThreads(int bundelsize, int auswertungsflag,
			int sliderauswertungsflag, int standartuserauswertungsflag,
			String attribpfad, int tid, int forceflag, int protoflag,ThreadsDB tdb)
	{
		String lastdate = null;
		Compressor comp = null;

		MidDB middb = new MidDB(tdb);
		tdb_g.ResetDB();
		int anzthreads = tdb_g.GetanzObj();
		int i = 0;

		while (i < 2)
		{
			i++;
			ThreadDbObj tdbo = (ThreadDbObj) tdb_g.SearchThreadid(tid);
			if (Tools.isKorrektSymbol(tdbo.getSymbol()) == false)
				continue;

			if (tdbo.getThreadname().toUpperCase().contains("DAX"))
				continue;

			if (tdbo.getSymbol().toUpperCase().contains("DAX"))
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
				if (protoflag == 1)
					infproto.writezeile("Info: Thread compressen<"
							+ tdbo.getThreadname() + "> tid<"
							+ tdbo.getThreadid() + ">");
				comp = new Compressor(tdbo.getThreadid(), tdbo.getSymbol(),tdb_g);
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

			gewinnBewertung_glob.setNeuerThread(tdbo, tdbo
					.GetSavePageName_genDir(0),tdb);

			// Attributstore generieren
			tstore_g = new ThreadAttribStoreI(tdbo.getThreadid(), attribpfad);

			if (forceflag == 0)
			{
				if (FileAccess.FileAvailable(GC.rootpath + attribpfad + "\\"
						+ tid + ".db") == true)
				{
					Tracer.WriteTrace(10, "tid <" + tid + "> pfad <"
							+ attribpfad + "> schon vorhanden");
					return;
				}
			}

			// Alle Postings des Thread auswerten
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
						anzthreads, pdb, standartuserauswertungsflag, protoflag);
			}
			if (tstore_g != null)
				tstore_g.WriteDB();
		}
		// lösche den Speicher
		if (sliderauswertungsflag == 1)
			comp.cleanSlider();

		// ugewinnstrategie2_g.WriteDB();
	}

	public void BewerteTidmenge(int suchmode, int bundelsize,
			int auswertungsflag, int sliderauswertungsflag,
			int standartuserauswertungsflag, String attribpfad, int tid,
			int forceflag, int protoflag,ThreadsDB tdb)
	{

		// Hier werden die Slider und die Prognosen für eine TIDmenge berechnet
		// bundlesize:grösse für die auswertung, es werden #bundlesize Aktien
		// bewertet und dann
		// auf Festplatte das Ergebniss geschrieben
		// auswertungsflag: 0 default
		// 1 schreibe keine Einzelgewinne
		// sliderauswertungsflag 0: die slider werden nicht ausgewertet
		// 1: mit auswertung der slider
		// protoflag 1: dann wird ein ausführliches protokoll geschrieben
		String protofile = GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\Einzelbewertung\\attribute\\proto_"
				+ tid + ".txt";
		String uebfile = GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\Einzelbewertung\\uebersicht.txt";

		if (FileAccess.FileAvailable(uebfile))
			FileAccess.FileDelete(uebfile,0);

		if (protoflag == 1)
		{
			if (FileAccess.FileAvailable(protofile))
				FileAccess.FileDelete(protofile,0);
			infproto.setFilename(protofile);
		}

		DurchlaufeAlleThreads(bundelsize, auswertungsflag,
				sliderauswertungsflag, standartuserauswertungsflag, attribpfad,
				tid, forceflag, protoflag,tdb);
	}
}
