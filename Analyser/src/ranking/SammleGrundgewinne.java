package ranking;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Prop2;
import hilfsklasse.Swttool;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import mainPackage.GC;
import objects.Htmlpage;
import objects.ThreadDbObj;
import objects.UserDbObj;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

import slider.Sliderbewert;
import stores.MidDB;
import stores.PostDBx;
import stores.ThreadsDB;
import stores.UserDB;
import stores.UserGewStrategieDB2;
import attribute.ThreadAttribStoreI;
import bank.Bank;

import compressor.CompressedThreadDBx;

import db.CompressedPostingObj;
import except.CompressedThreadException;

public class SammleGrundgewinne
{
	static UserGewStrategieDB2 ugewinnstrategie2_g = new UserGewStrategieDB2("UserThreadVirtualKonto\\Summengewinnedb");
	// frage wie kommen neue objekte in ugewinnstrategie2_g ??

	//static UserDB udb = new UserDB("observeuser.txt", "boostraenge.txt");
	private UserDB udb_glob=null;
	/*private Prop2 prop2 = new Prop2(GC.rootpath
			+ "\\db\\UserThreadVirtualKonto\\config_usergewinne.prop");*/
	static PostDBx pdb = new PostDBx();
	private Bank bank = new Bank();
	// static Inf infMaster = new Inf();
	static Inf infMaster2 = new Inf();
	// static private PostSlider sl = new PostSlider(udb);

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

	private GewinnBewertung gewinnBewertung =null;

	public SammleGrundgewinne(UserDB udb)
	{
		udb_glob=udb;
		gewinnBewertung= new GewinnBewertung(
				ugewinnstrategie2_g, udb_glob,null);
	}
	
	private void BewerteStandartuserposting(Sliderbewert slb, ThreadDbObj tdbo,
			CompressedPostingObj copost, UserDB udb,
			ThreadAttribStoreI attribstore, int ipos, int iposmax)
	{
		// dieses Posting wird bzgl. Gewinninformation bewertet und der Gewinn
		// wird in den internen Strukturen abgespeichert

		String threadname = tdbo.getThreadname();

		// a)jedes Posting eines users wird gewertet
		// Man möchte für das Ranking ja rausfinden wer am meisten Gewinne macht
		// Bei der Bewertung werden mehrere Statistiken gesammelt
		gewinnBewertung.werteVerfahren0(0, copost, threadname, tdbo
				.getThreadid(), tdbo.getThreadname(), lasterrorsymbol_glob,
				tdbo.getThreadname(), tdbo.getSymbol(), ipos, iposmax);
	}

	private void BewertePosting(CompressedThreadDBx compt, ThreadDbObj tdbo,
			CompressedPostingObj copost, UserDB udb,
			ThreadAttribStoreI attribstore, int ipos, int iposmax)
	{
		BewerteStandartuserposting(null, tdbo, copost, udb, attribstore, ipos,
				iposmax);
	}

	private void DurchlaufeAlleThreads(int lastkontozaehler, int bundelsize,
			int auswertungsflag, int endzaehler,int turboflag,ProgressBar pb,Display dis,Text aus,ThreadsDB tdb)
	{
		// turboflag, falls 1 dann werden keine threads kompresst, sondern
		// einfach nur geonmmen was da ist

		//ThreadsDB tdb = new ThreadsDB();
		MidDB middb = new MidDB(tdb);
		tdb.ResetDB();
		int anzthreads = tdb.GetanzObj();
		int maxpos = 0;

		if (endzaehler > anzthreads)
			maxpos = anzthreads;
		else
			maxpos = endzaehler;

		pb.setMaximum(lastkontozaehler);
		pb.setMaximum(maxpos);
		
		for (int i = lastkontozaehler; i < maxpos; i++)
		{
			pb.setSelection(i);
			Swttool.wupdate(dis);
			ThreadDbObj tdbo = (ThreadDbObj) tdb.GetObjectIDX(i);
			if (Tools.isKorrektSymbol(tdbo.getSymbol()) == false)
				continue;

			// erst mal den ganzen Thread kompressen
			char first = String.valueOf(tdbo.getThreadid()).charAt(0);
			String fn = GC.rootpath + "\\db\\compressedthreads\\@" + first
					+ "\\" + tdbo.getThreadid() + ".db";
			String fnmaster = tdbo.GetSavePageName2(1);
			String ausgabe="Bewerte <"+i+"|"+lastkontozaehler+"> compresse/bewerte<"+fn+">";
			aus.setText(ausgabe);
			Swttool.wupdate(dis);
			
			// schaut nach ob sich das Datum von der masterseite gegenüber
			// dem
			// compressed threads unterscheidet
			if (turboflag == 0)
			{
				if (FileAccess.zeitdifferenz(fn, fnmaster) > 24)
					tdbo.updateCompressedThread(middb,i, anzthreads, tdb, udb_glob, 0);
			}
			CompressedThreadDBx compt = null;
			try
			{
				compt = new CompressedThreadDBx(Integer.toString(tdbo
						.getThreadid()));
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
			int postanz = compt.getanz();

			gewinnBewertung
					.setNeuerThread(tdbo, tdbo.GetSavePageName_genDir(0),tdb);

			for (int j = 0; j < postanz; j++)
			{
				CompressedPostingObj copost = compt.getObjectIDX(j);
				if (copost == null)
				{
					Tracer.WriteTrace(20, "Warning: copost==null");
					continue;
				}

				BewertePosting(compt, tdbo, copost, udb_glob, tstore_g, i,
						anzthreads);

				if (j % 200 == 0)
					System.out.println("i,j<" + i + "|" + j + "> ");
			}
			if (tstore_g != null)
				tstore_g.WriteDB();

			if (i % bundelsize == 1)
			{
				Prop2.setprop("lastvirtKontozaehler", String.valueOf(i));
				ugewinnstrategie2_g.GibZwischenGewinnListeAusCalcGewinnDelMem(
						0, auswertungsflag, i, bank, 0,null);
				ugewinnstrategie2_g.WriteDB();
				if (i > 1)
					ugewinnstrategie2_g.WriteDB();
			}
		}
		Prop2.setprop("lastvirtKontozaehler", String.valueOf(maxpos));
		// zahle am Ende den Bonus aus
		ugewinnstrategie2_g.GibZwischenGewinnListeAusCalcGewinnDelMem(
				GC.DEFAULT, auswertungsflag, anzthreads, bank, 1,null);
		// udb.WriteDB();
		ugewinnstrategie2_g.calcGewinnraenge(aus,dis,pb);
		ugewinnstrategie2_g.WriteDB();
	}

	public void CalcGewinnRaengeSt2(Text aktion, Display dis,ProgressBar pb)
	{
		ugewinnstrategie2_g.calcGewinnraenge(aktion, dis,pb);
		ugewinnstrategie2_g.WriteDB();
	}

	public void StartBewertungSt1(int suchmode, int bundelsize,
			int auswertungsflag, int endzaehler,int turboflag,ProgressBar pb,int startindex,Text aktion,Display dis,ThreadsDB tdb)
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
		// endzaehler: bis zu diesem threadcounter wird gewertet

		//String laststring = Prop2.getprop("lastvirtKontozaehler");
		//int lastkontozaehler = Integer.parseInt(laststring);

		if (startindex == 0)
		{
			if(aktion != null)
				aktion.setText("lösche alte Ergebnisse");
			if (FileAccess.FileAvailable(GC.rootpath
					+ "\\db\\UserThreadVirtualKonto\\uebersicht.txt"))
				FileAccess.FileDelete(GC.rootpath
						+ "\\db\\UserThreadVirtualKonto\\uebersicht.txt",0);
		}
		Swttool.wupdate(dis);
		// lösche alle gewinne da die liste neu erstellt wird
		if (startindex == 0)
			ugewinnstrategie2_g.GibZwischenGewinnListeAusCalcGewinnDelMem(
					GC.LOESCHE, auswertungsflag, 0, bank, 0,null);
		
		
		Swttool.wupdate(dis);
		DurchlaufeAlleThreads(startindex, bundelsize, auswertungsflag,
				endzaehler,turboflag,pb,dis,aktion,tdb);
	}
}
