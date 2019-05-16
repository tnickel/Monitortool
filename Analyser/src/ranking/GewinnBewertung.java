package ranking;

import handelsstrategien.Handelsstrategie;
import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Ma;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import kurse.KursValueDB;
import kurse.Kursvalue;
import mainPackage.GC;
import objects.Gewinn;
import objects.ThreadDbObj;
import objects.UserEinPostingGewinnObj;
import objects.UserGewStrategieObjII;
import slider.KursSlider;
import slider.PostSlider;
import slider.Sliderbewert;
import stores.PostDBx;
import stores.SlideruebersichtDB;
import stores.ThreadsDB;
import stores.UserDB;
import stores.UserGewStrategieDB2;
import attribute.ThreadAttribStoreI;
import bank.Bank;
import bank.Depot;

import compressor.CompressedThreadDBx;
import compressor.Compressor;

import db.CompressedPostingObj;

public class GewinnBewertung
{
	// Diese Klasse führt die Gewinnbewertung durch

	// die KursvalueDB wird in 2 instancen behandelt. Man möchte bei der
	// Kurssuche optimieren, das suchdatum bei
	// einer Abfrage muss immer aufsteigend sein (bei einer instanz würde die 10
	// Tage-Abfrage ein Problem bereiten)
	static Inf infMaster = new Inf();
	static Inf infvertrau = new Inf();
	KursValueDB kvdba_g = null;
	String lastkursinfo1_g = null, lastkursinfo2_g = null;
	GewinnStatistik gstati = new GewinnStatistik();
	UserGewStrategieDB2 ugewinndbII_g = null;
	PostSlider postslider_glob = null;
	String pfad_g=null;
	Handelsstrategie handel = null;
	String symb_g = null;
	float lastk1_g = 0, lastk2_g = 0;
	String lastdate1_g = "", lastdate2_g = "";
	String lastthreadname_g = "";

	public GewinnBewertung(UserGewStrategieDB2 ugewinndbIIx, UserDB udb,String pfad)
	{
		String fnam="";
		postslider_glob = new PostSlider(udb);
		handel = new Handelsstrategie(postslider_glob, udb.GetanzObj());
		ugewinndbII_g = ugewinndbIIx;
		
		if(pfad==null)
			pfad_g=GC.rootpath+ "\\db\\UserThreadVirtualKonto\\VertrauSlider.txt";
		else
			pfad_g=GC.rootpath+ "\\db\\UserThreadVirtualKonto\\"+pfad+"\\VertrauSlider.txt";
		
		infvertrau.setFilename(pfad_g);
	}
	public Sliderbewert holeSliderbewertung(CompressedThreadDBx compt, PostDBx pdb)
	{
		Sliderbewert sb=postslider_glob.BewerteSlider(GC.SLIDERINDEX20TAGE, compt, pdb);
		return sb;
	}
	
	public PostSlider holePostslider()
	{
		return postslider_glob;
	}

	public void setNeuerThread(ThreadDbObj tdbo, String pagename,ThreadsDB tdb)
	{
		symb_g = new String(tdbo.getSymbol());
		postslider_glob.initSlider(tdbo, GC.SLIDERINDEX20TAGE, GC.SLMAXSLIDER);

		// für den slider wird ein threadname gesetzt
		postslider_glob.setThreadname(pagename.substring(pagename.lastIndexOf("\\")));
		kvdba_g = new KursValueDB(symb_g, 0,tdb);
	}

	public void calcAddUpdateStoreSliderGenPrognose(ThreadDbObj tdbo,
			SlideruebersichtDB sldb, CompressedThreadDBx compt,
			int wochenindex, int turboflag, PostDBx pdb)
	{
		if ((postslider_glob == null))
			return;
		// beim turboflag wird sldb nicht gespeichert
		postslider_glob.calcAddUpdateSliderGenUebersichtGenPrognosen(GC.SLIDERINDEX20TAGE,
				tdbo, sldb, compt, wochenindex, turboflag, pdb);
	}

	public String showSlider()
	{
		return (postslider_glob.showSliderValues());
	}
	public void dumpSlider(String fnam)
	{
		postslider_glob.dumpslider(1, fnam);
	}

	private void protoGrosserGewinn(CompressedPostingObj copost,
			float startsumme, String aktthreadnamz, Sliderbewert slb,
			String methode, float vertrauensfaktor, String gewnam,
			Kursvalue k1obj, Kursvalue k2obj, float aktanz, float Gewinn)
	{
		String verzdat = Tools.entferneZeit(Tools.get_aktdatetime_str());
		String fna = gewnam + "_" + copost.getThreadid() + "_"
				+ copost.getPostid();
		String infofile = GC.rootpath + "\\db\\reporting\\ListeGrosseGewinne\\"
				+ verzdat;
		FileAccess.checkgenDirectory(infofile);
		infofile = infofile + "\\" + fna;

		// Protokolliere einen grossen Gewinn
		Inf inf = new Inf();
		inf.setFilename(GC.rootpath + "\\db\\reporting\\grosseGewinne.txt");
		String msg = Tools.get_aktdatetime_str() + " Algo<" + methode
				+ "> unam<" + gewnam + "> Gew<" + Gewinn + "> k1<"
				+ k1obj.getKv() + "> k2<" + k2obj.getKv() + "> anz<" + aktanz
				+ "> tid<" + copost.getThreadid() + "> tid<"
				+ copost.getKennung_threadid_g() + "> pid<"
				+ copost.getPostid() + "> nam<" + aktthreadnamz + "> infofile<"
				+ fna + ">";
		inf.writezeile(msg);

		if (slb != null)
		{
			String algobeschreibung = "nicht bekannt";
			UserGewStrategieObjII u2 = ugewinndbII_g.sucheUser(gewnam);
			if (u2 != null)
				algobeschreibung = u2.getAlgoBeschreibungsstring();

			// Protokolliere die erweiterten Infos
			Inf infdetail = new Inf();
			infdetail.setFilename(infofile);
			String msgdetail = msg;
			infdetail.writezeile(msgdetail);
			msgdetail = Tools.get_aktdatetime_str() + ": Aktname <"
					+ aktthreadnamz + "> tid<" + copost.getThreadid() + ">";
			infdetail.writezeile(msgdetail);
			msgdetail = "Alobeschreibung<" + algobeschreibung + ">";
			infdetail.writezeile(msgdetail);
			msgdetail = "Vertrauensfaktor<" + vertrauensfaktor
					+ "> startsumme<" + startsumme + ">";
			infdetail.writezeile(msgdetail);
			msgdetail = "GewDatum<" + copost.getDatetime()
					+ "> : Sliderpostanz <" + slb.getPostanz() + "> mitlRang<"
					+ slb.getMitlrank() + ">";
			infdetail.writezeile(msgdetail);
			msgdetail = "(+/-)Postings (" + slb.getAnzgutePostings() + "/"
					+ slb.getAnzschlechtePostings() + ")";
			infdetail.writezeile(msgdetail);
			msgdetail = "#user (+/-/bad) (" + slb.getGuteU() + "/"
					+ slb.getSchlechteU() + "/" + slb.getSchlechteU() + ")";
			infdetail.writezeile(msgdetail);
			msgdetail = "neue user (+/-/bad) (" + slb.getNeueGuteUserImSlider()
					+ "/" + slb.getNeueSchlechteUserImSlider() + "/"
					+ slb.getNeueBaduserImSlider() + ")";
			infdetail.writezeile(msgdetail);
			msgdetail = "link < http://aktien.wallstreet-online.de/"
					+ this.postslider_glob.getMasterid() + ">";
			infdetail.writezeile(msgdetail);

			// gib den 20Tage Slider aus
			postslider_glob.dumpslider(GC.SLIDERINDEX20TAGE, infofile);
			msgdetail = "_________________________________________________________";
			infdetail.writezeile(msgdetail);
		}
	}

	private Gewinn einKurswertNull(String lasterrorsymbol,
			String aktthreadnamz, Kursvalue k1obj, Kursvalue k2obj,
			String kdate1)
	{
		// falls der fehler bei einem neuen symbol auftritt, nur der
		// erste Fehler für ein symbol
		// wird geblockt, solange bis wieder ne positive Meldung kommt
		String msg = "Warning:Kurswert konnte nicht ermittelt werden tname<"
				+ aktthreadnamz + "> symb<" + symb_g + "> k1<" + k1obj.getKv()
				+ "> k2<" + k2obj.getKv() + "> kdate<" + kdate1
				+ "> unterdrücke gleiche Meldungen";

		if (lasterrorsymbol != symb_g)
			Tracer.WriteTrace(20, msg);
		lasterrorsymbol = symb_g;
		return (new Gewinn(0, 0, 0, null, null, "Ein val=0"));
	}

	private boolean kursschwankungsfehler(String aktthreadnamz,
			Kursvalue k1obj, Kursvalue k2obj, String kdate1)
	{
		float min = 0, max = 0;
		float v1 = k1obj.getKv();
		float v2 = k2obj.getKv();

		if (v1 < v2)
		{
			min = v1;
			max = v2;
		} else
		{
			min = v2;
			max = v1;
		}
		// proz>100%
		float proz = (100f / min * max);
		float prozabweichung = Math.abs(proz - 100f);

		if ((min == 0) || (max == 0))
			return false;

		if ((prozabweichung > 200) && (prozabweichung < 500))
		{
			Inf inf = new Inf();
			inf.setFilename(GC.rootpath
					+ "\\db\\reporting\\KursschwankungsWarnung200.txt");
			String msg = "Warnung: Kursschwankung zu stark schwankung<"
					+ prozabweichung + ">% dat<" + kdate1 + "> minval<" + min
					+ "> maxval<" + max + "> symb<" + symb_g + "> tnam<"
					+ aktthreadnamz + ">";
			inf.writezeile(msg);
		}

		if (prozabweichung > 500)
		{
			Inf inf = new Inf();
			inf.setFilename(GC.rootpath
					+ "\\db\\reporting\\Kursschwankungsfehler500.txt");
			String msg = "Error: Kursschwankung zu stark schwankung<"
					+ prozabweichung + ">% dat<" + kdate1 + "> minval<" + min
					+ "> maxval<" + max + "> symb<" + symb_g + "> tnam<"
					+ aktthreadnamz + ">";
			inf.writezeile(msg);
			return true;
		} else
			return false;
	}

	public Gewinn calcGewinnNachXtagen(String username,
			CompressedPostingObj copost, float startsumme, int xtage,
			String lasterrorsymbol, String aktthreadnamz, Sliderbewert slb,
			String methode, float vertrauensfaktor)
	{
		// sliderbewert= wird für die Statistik benötigt
		String gewnam = methode;

		if (methode != null)
			gewnam = methode;
		else
			gewnam = username;

		if (startsumme < 0)
			Tracer.WriteTrace(10,
					"Error: internal startsumme muss grösser 0 sein startsum<"
							+ startsumme + ">");

		Kursvalue k1obj = new Kursvalue(), k2obj = new Kursvalue();
		String kdate1 = Tools.entferneZeit(copost.getDatetime());
		String kdate2 = Tools.entferneZeit(Tools.modifziereDatum(kdate1, 0, 0,
				xtage, 0));

		// caching
		if ((kdate1.equalsIgnoreCase(lastdate1_g))
				&& (kdate2.equalsIgnoreCase(lastdate2_g))
				&& (lastthreadname_g.equalsIgnoreCase(aktthreadnamz)))
		{
			// falls die daten gleich sind dann nimm die schon berechneten
			// Kurse
			k1obj.setKv(lastk1_g);
			k1obj.setDate(lastdate1_g);
			k1obj.setKursinfo(lastkursinfo1_g);
			k2obj.setKv(lastk2_g);
			k2obj.setDate(lastdate2_g);
			k2obj.setKursinfo(lastkursinfo2_g);

		} else
		{ // daten sind nicht gleich, dann berechne die kurse neu
			k1obj = kvdba_g.holeKurswert(kdate1, 0);
			k2obj = kvdba_g.holeKurswert(kdate2, 0);
			lastk1_g = k1obj.getKv();
			lastk2_g = k2obj.getKv();
			lastdate1_g = kdate1;
			lastdate2_g = kdate2;
			lastthreadname_g = aktthreadnamz;
			lastkursinfo1_g = k1obj.getKursinfo();
			lastkursinfo2_g = k2obj.getKursinfo();
		}

		float aktanz = (startsumme / k1obj.getKv());
		float val2 = k2obj.getKv() * aktanz;
		float Gewinn = val2 - (k1obj.getKv() * aktanz);

		// check KursProzentabweichung, bei einer zu starken Kursschwankung wird
		// von fehlerhaften Kursen ausgegangen und für den User kein Gewinn
		// verzeichnet
		if (kursschwankungsfehler(aktthreadnamz, k1obj, k2obj, kdate1) == true)
			return (new Gewinn(0, 0, 0, null, null, "ERR:Kursschwankung"));

		if (Gewinn > 100000)
			protoGrosserGewinn(copost, startsumme, aktthreadnamz, slb, methode,
					vertrauensfaktor, gewnam, k1obj, k2obj, aktanz, Gewinn);

		if ((k1obj.getKv() < 0.01) || (k2obj.getKv() < 0.01))
		{
			String msg = "Warning:Kurswert Zu gering k<0.01 tname<"
					+ aktthreadnamz + "> symb<" + symb_g + "> k1<"
					+ k1obj.getKv() + "> k2<" + k2obj.getKv() + "> kdate<"
					+ kdate1 + "> unterdrücke gleiche Meldungen";
			if ((lasterrorsymbol == null) || (symb_g == null))
				return (new Gewinn(0, 0, 0, null, null, "0"));

			if ((lasterrorsymbol.equalsIgnoreCase(symb_g) == false))
				Tracer.WriteTrace(20, msg);
			lasterrorsymbol = symb_g;
			return (new Gewinn(0, 0, 0, null, null, "0(0.01)"));
		}

		if (k1obj.getKv() == 0 || k2obj.getKv() == 0)
			return einKurswertNull(lasterrorsymbol, aktthreadnamz, k1obj,
					k2obj, kdate1);

		// lösche wieder die fehlermeldung
		if ((k1obj.getKv() > 0.03) && (k2obj.getKv() > 0.03))
			lasterrorsymbol = null;

		return (new Gewinn(Gewinn, k1obj.getKv(), k2obj.getKv(), k1obj
				.getDate(), k2obj.getDate(), k1obj.getKursinfo()));
	}

	private float calcZwischenGewinn(float startsumme, float k1, float k2)
	{
		float aktanz = (startsumme / k1);
		float val2 = k2 * aktanz;
		float Gewinn = val2 - (k1 * aktanz);
		return Gewinn;
	}

	public Gewinn calcGewinnMitLimit(Compressor comp,
			CompressedPostingObj copost, float startsumme, int maxdays,
			String lasterrorsymbol, String aktthreadnamz)
	{
		// postelem: hier ist das Posting drin
		// pusername: ist der User der gepostet hat
		// startsumme: mit dieser Summe wird die Anlage gestartet
		// gewinnerwartung: ist diese Gewinnerwartung erreicht dann wird
		// verkauft
		// maxdays: dies ist die maximale Zeit die gewartet wird, dann wird
		// verkauft
		// lasterrorsymbol: dies ist das symbol bei dem das letzte mal ein
		// errorlog statt fand
		// aktthreadnamz: dies ist der Name des Threads

		KursSlider k10 = new KursSlider(3);
		Inf inf = new Inf();
		inf.setFilename(GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\alg38detail.txt");

		float k1 = 0, k2 = 0, last10kurs = 0;

		if (startsumme < 0)
			Tracer.WriteTrace(10,
					"Error: internal startsumme muss grösser 0 sein startsum<"
							+ startsumme + ">");

		Kursvalue k1obj = new Kursvalue(), k2obj = new Kursvalue();
		String kdate1 = Tools.entferneZeit(copost.getDatetime());
		String kdate2 = Tools.entferneZeit(copost.getDatetime());

		// startkurs
		k1obj = comp.holeKurswert(kdate1, 0);
		k1 = k1obj.getKv();
		inf.writezeile("................................");
		// halte auf jeden Fall erst mal 5 Tage
		for (int indx = 1; indx < 6; indx++)
		{
			kdate2 = Tools.entferneZeit(Tools.modifziereDatum(kdate1, 0, 0,
					indx, 0));
			k2obj = comp.holeKurswert(kdate2, 0);
			k10.addSliderElem(k2obj.getKv());

			String msg = "ind<" + indx + "> kdate<" + kdate2 + "> kv<"
					+ k2obj.getKv() + "> kv5Slider<" + k10.calcSliderValue()
					+ ">";
			inf.writezeile(msg);
		}
		k2 = k2obj.getKv();
		// durschnittskurs der letzten 5 Tage
		last10kurs = k10.calcSliderValue();

		for (int indx = 6; indx < maxdays; indx++)
		{
			// Im Minus dann verkaufe
			if (k10.calcSliderValue() < k1 * 0.9)
			{
				float Gewinn = calcZwischenGewinn(startsumme, k1obj.getKv(),
						k2obj.getKv());
				return (new Gewinn(Gewinn, k1obj.getKv(), k2obj.getKv(), null,
						null, ""));
			}

			// betrachte den weiteren Verlauf
			kdate2 = Tools.entferneZeit(Tools.modifziereDatum(kdate1, 0, 0,
					indx, 0));
			k2obj = kvdba_g.holeKurswert(kdate2, 0);
			last10kurs = k10.calcSliderValue();
			k10.addSliderElem(k2obj.getKv());

			// verkaufe wenn in den letzten 5 Tagen keine Kurssteigerung mehr da
			// ist
			float k10val = k10.calcSliderValue();
			String msg = "ind<" + indx + "> kdate<" + kdate2 + "> kv<"
					+ k2obj.getKv() + "> kv10val<" + k10.calcSliderValue()
					+ "> last10kurs<" + last10kurs + ">";
			inf.writezeile(msg);
			if (k10val <= last10kurs - (last10kurs * (5 / 100)))
			{
				float Gewinn = calcZwischenGewinn(startsumme, k1obj.getKv(),
						k2obj.getKv());
				return (new Gewinn(Gewinn, k1obj.getKv(), k2obj.getKv(), null,
						null, ""));
			}
		}
		return (new Gewinn(0, 0, 0, null, null, ""));
	}

	/**
	 * @deprecated Use {@link #addSliderpostingSt1(CompressedPostingObj)} instead
	 */
	public void addSliderposting(CompressedPostingObj copost)
	{
		addSliderpostingSt1(copost);
	}

	public void addSliderpostingSt1(CompressedPostingObj copost)
	{
		// Hier werden die Slider mit infos gefüllt
		// Sliderindex 0: 20 Posting Slider (wird im Normalfall nicht verwendet)
		// Sliderindex 1: 20 Tage Slider
		postslider_glob.addSliderPosting(GC.SLIDERINDEX20TAGE, copost.getThreadid(),
				copost);
	}

	public Sliderbewert BewerteSliderSt2(int index, CompressedThreadDBx compt,
			PostDBx pdb)
	{
		Sliderbewert sb = postslider_glob.BewerteSlider(index, compt, pdb);
		return sb;
	}
	
	public boolean SetzeAttributeSt4(ThreadAttribStoreI attribstore,
			CompressedPostingObj copost,Sliderbewert slb)
	{
		if(slb==null)
		{
			Tracer.WriteTrace(10, "Error: slb==null");
			return false;
		}
		float anzUser20Tage = postslider_glob.holeAttribSlider20TageAnzahl();
		float mittlererrank = slb.getMitlrank();
		String datum = Tools.entferneZeit(copost.getDatetime());
		
		//allgemeine Attribute
		attribstore.setAttrib(datum, GC.ATTRIB_LASTPOSTID, Float.valueOf(copost
				.getPostid()));
		attribstore.setAttrib(datum, GC.ATTRIB_USERIN20TAGEN, anzUser20Tage);
		attribstore.setAttrib(datum, GC.ATTRIB_MITTLERERRANK, mittlererrank);
		attribstore.setAttrib(datum,GC.ATTRIB_anzGuteUser,slb.getGuteU());
		attribstore.setAttrib(datum,GC.ATTRIB_anzSchlechteUser,slb.getAnzschlechtePostings());
		attribstore.setAttrib(datum,GC.ATTRIB_anzBadUser,slb.getAnzbaduser());
		attribstore.setAttrib(datum,GC.ATTRIB_anzSchlechtePostings,slb.getAnzschlechtePostings());						
		attribstore.setAttrib(datum,GC.ATTRIB_anzGutePostings,slb.getAnzgutePostings());
		attribstore.setAttrib(datum,GC.ATTRIB_anzNeueGuteUser,slb.getNeueGuteUserImSlider());
		attribstore.setAttrib(datum,GC.ATTRIB_anzNeueSchlechteUser,slb.getNeueSchlechteUserImSlider());
		attribstore.setAttrib(datum,GC.ATTRIB_anzNeueBadUser,slb.getNeueBaduserImSlider());
		
		
		//Attribute für spezielle Gewinnalgorithmen ablegen
		//a)Masteruser Handelshinweis (kaufen/oder Grund warum nicht)
		
		if((slb!=null)&&(slb.getHandelshinweis()!=null))
			attribstore.setHandelshinweis(datum,slb.getHandelshinweis());
		
		return true;
	}

	public void werteVerfahren0(int verfindex, CompressedPostingObj copost,
			String threadname, int threadid, String aktthreadname,
			String lasterrorsymbol, String aktthreadnamz, String symb,
			int threadpos, int posmax)
	{
		Tracer.WriteTrace(50, "Info:copost auswertung tid<"
				+ copost.getThreadid() + "> tname<" + threadname + ">");

		// a)Verfahren0, alle user alle Threads gleich behandeln
		// berechne den Gewinn für 100 Euro Anlage und 10 Tage laufzeit
		Gewinn gewinn = calcGewinnNachXtagen(null, copost, 100, 10,
				lasterrorsymbol, aktthreadnamz, null, "werteVerfahren0", 0);

		infMaster.setFilename(GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\uebersicht.txt");
		gstati.speichereGewinnStatistik(verfindex, infMaster, threadname,
				copost, gewinn.getGewinn(), threadpos, posmax, aktthreadnamz,
				symb);

		// gewinnobj genierieren
		UserEinPostingGewinnObj ueingewobj = new UserEinPostingGewinnObj(copost
				.getDatetime(), aktthreadname, copost.getThreadid(), copost
				.getPostid(), gewinn.getGewinn(), gewinn.getKurs1(), gewinn
				.getKurs2(), 0, symb, gewinn.getKursinfo());

		// gewinnobj speichern
		// long vorher=Tools.get_aktdate_lo();

		ugewinndbII_g.addGewinnObj(verfindex, copost.getUsername(), ueingewobj);
	}

	public void werteVerfahren3Masteruser(Sliderbewert slb, String pusername,
			int index, CompressedPostingObj copost, String threadname,
			int threadid, String aktthreadname, String lasterrorsymbol,
			String aktthreadnamz, int threadpos, int posmax, float maxfaktor,
			float gefaelle, ThreadAttribStoreI attribstore)
	{
		// c)Verfahren3, Sliding-Fenster betrachte einen Postingzeitraum (z.B.
		// 20 Tage)
		// wieviele erfahrene User sind in den letzten
		// Postings vorher in diesem Thread
		// Je mehr erfahrene User in dem Slidingfenster je höher setze den
		// Gewinn ein. Sind nur luschen im Fenster, dann setze nix ein

		String cname = this.getClass().getCanonicalName();

		Vertrauen vertrau = postslider_glob.calcSliderVertrauensfaktor(slb,
				GC.SLIDERINDEX20TAGE, maxfaktor, (float) 0.1, gefaelle);

		float vertrauensfaktor = vertrau.getVertrauensfaktor_g();
		Gewinn gewinn = calcGewinnNachXtagen(pusername, copost,
				vertrauensfaktor * 100, 10, lasterrorsymbol, aktthreadnamz,
				slb, "werteVerfahren3Masteruser", vertrauensfaktor);

		if (gewinn.getGewinn() == 0)
			return;

		UserEinPostingGewinnObj ueingewobj = new UserEinPostingGewinnObj(copost
				.getDatetime(), aktthreadname, copost.getThreadid(), copost
				.getPostid(), gewinn.getGewinn(), gewinn.getKurs1(), gewinn
				.getKurs2(), 0, symb_g, gewinn.getKursinfo());

		ugewinndbII_g.addGewinnObj(index, pusername, ueingewobj);
		// Protokolliere bei hohen Vertrauen
		if (vertrauensfaktor > 1)
		{
			if (pusername.equalsIgnoreCase("MasteruserSI"))
			{
				String msg = "date<" + Tools.entferneZeit(copost.getDatetime())
						+ "> tnam<" + Tools.holeSubstring(aktthreadname, 15)
						+ "> tid<" + copost.getThreadid() + "> pid<"
						+ copost.getPostid() + "> K1<" + gewinn.getKurs1()
						+ "> K2<" + gewinn.getKurs2() + "> Gew<"
						+ gewinn.getGewinn() + "> vertrau<" + vertrauensfaktor
						+ "> anzSlUser<" + vertrau.getSliderelemanz_g()
						+ "> avRank<" + vertrau.getDurchschnittrank_g() + ">";
				infvertrau.writezeile(msg);
			}
		}

	}

	public void werteVerfahren4Masteruser(Sliderbewert slb, String pusername,
			int index, CompressedPostingObj copost, String threadname,
			int threadid, String aktthreadname, String lasterrorsymbol,
			String aktthreadnamz, int threadpos, int posmax, float maxfaktor,
			float gefaelle, ThreadAttribStoreI tstore)
	{
		// c)Verfahren3, Sliding-Fenster betrachte einen Postingzeitraum (z.B.
		// 20 Tage)
		// wieviele erfahrene User sind in den letzten
		// Postings vorher in diesem Thread
		// Je mehr erfahrene User in dem Slidingfenster je höher setze den
		// Gewinn ein. Sind nur luschen im Fenster, dann setze nix ein
		// Inf infkg = new Inf();
		// Inf infg = new Inf();
		Inf inf = new Inf();

		/*
		 * String fnamkg = GC.rootpath +
		 * "\\db\\UserThreadVirtualKonto\\MasteruserListen\\" + threadid +
		 * "_keinGewinn" + ".txt"; String fnamg = GC.rootpath +
		 * "\\db\\UserThreadVirtualKonto\\MasteruserListen\\" + threadid +
		 * "_Gewinn" + ".txt";
		 */
		String fnam = GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\MasteruserListen\\" + threadid
				+ "_gute_schlechte" + ".txt";

		/*
		 * infkg.setFilename(fnamkg); infg.setFilename(fnamg);
		 */
		inf.setFilename(fnam);

		Vertrauen vertrau = postslider_glob.calcSliderVertrauensfaktor2(slb,
				GC.SLIDERINDEX20TAGE, maxfaktor, (float) 0.1, gefaelle);

		/*
		 * if (vertrau.getVertrauensfaktor_g() == 0)
		 * sl_g.dumpslider(GC.SLIDERINDEX20TAGE, fnamkg); else
		 * sl_g.dumpslider(GC.SLIDERINDEX20TAGE, fnamg);
		 */

		float vertrauensfaktor = vertrau.getVertrauensfaktor_g();
		Gewinn gewinn = calcGewinnNachXtagen(pusername, copost,
				vertrauensfaktor * 100, 10, lasterrorsymbol, aktthreadnamz,
				slb, "werteVerfahren4Masteruser", vertrauensfaktor);

		inf.writezeile("tid<" + threadid + "> postanz<"
				+ vertrau.getSliderelemanz_g() + "> mitlRan<"
				+ vertrau.getDurchschnittrank_g() + "> guteU<"
				+ vertrau.getAnzgute_g() + "> schlechteU<"
				+ vertrau.getAnzschlechte_g() + "> k1<" + gewinn.getKurs1()
				+ "> k2<" + gewinn.getKurs2() + "> Gewinn<"
				+ gewinn.getGewinn() + ">");

		/*
		 * infg.writezeile("vertrau <" + vertrau.getVertrauensfaktor_g() +
		 * "> mtlrank<" + mittlererrank1 + "> interesse <" + interesse + "> v1<"
		 * + gewinn.getKurs1() + "> v2<" + gewinn.getKurs2() + "> gewinn<" +
		 * gewinn.getGewinn() + ">");
		 */
		if (gewinn.getGewinn() == 0)
			return;
		UserEinPostingGewinnObj ueingewobj = new UserEinPostingGewinnObj(copost
				.getDatetime(), aktthreadname, copost.getThreadid(), copost
				.getPostid(), gewinn.getGewinn(), gewinn.getKurs1(), gewinn
				.getKurs2(), 0, symb_g, gewinn.getKursinfo());

		ugewinndbII_g.addGewinnObj(index, pusername, ueingewobj);
		// Protokolliere bei hohen Vertrauen

		if (pusername.equalsIgnoreCase("MasteruserX"))
		{
			String msg = "date<" + Tools.entferneZeit(copost.getDatetime())
					+ "> tnam<" + Tools.holeSubstring(aktthreadname, 15)
					+ "> tid<" + copost.getThreadid() + "> pid<"
					+ copost.getPostid() + "> K1<" + gewinn.getKurs1()
					+ "> K2<" + gewinn.getKurs2() + "> Gew<"
					+ gewinn.getGewinn() + "> vertrau<" + vertrauensfaktor
					+ "> anzSlUser<" + vertrau.getSliderelemanz_g()
					+ "> avRank<" + vertrau.getDurchschnittrank_g() + ">";
			infvertrau.writezeile(msg);
		}

	}

	public void werteVerfahren5Masteruser(String pusername, int index,
			CompressedPostingObj copost, String threadname, int threadid,
			String aktthreadname, String lasterrorsymbol, String aktthreadnamz,
			int threadpos, int posmax, float maxfaktor, float gefaelle,
			ThreadAttribStoreI tstore)
	{

		Inf inf = new Inf();
		String fnam = GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\randomuser.txt";
		inf.setFilename(fnam);

		Gewinn gewinn = calcGewinnNachXtagen(pusername, copost, 100, 10,
				lasterrorsymbol, aktthreadnamz, null,
				"werteVerfahren5Masteruser", 0);

		inf.writezeile("tnam<" + aktthreadnamz + "> k1<" + gewinn.getKurs1()
				+ "> k2<" + gewinn.getKurs2() + "> gewinn<"
				+ gewinn.getGewinn() + ">");
		if (gewinn.getGewinn() == 0)
			return;
		UserEinPostingGewinnObj ueingewobj = new UserEinPostingGewinnObj(copost
				.getDatetime(), aktthreadname, copost.getThreadid(), copost
				.getPostid(), gewinn.getGewinn(), gewinn.getKurs1(), gewinn
				.getKurs2(), 0, symb_g, gewinn.getKursinfo());

		ugewinndbII_g.addGewinnObj(index, pusername, ueingewobj);

	}

	public void werteVerfahren38x1Masteruser(Compressor comp, String pusername,
			CompressedPostingObj copost, ThreadDbObj tdbo,
			String lasterrorsymbol, Bank bank)
	{
		// Falls Steigung >2 dann investiere
		Inf inf = new Inf();
		String fnam = GC.rootpath + "\\db\\UserThreadVirtualKonto\\alg38x1.txt";
		inf.setFilename(fnam);
		Kursvalue kv = comp.holeEinzelkurswert(copost);

		float kv30 = comp.holeKurswert38();
		float kv200 = comp.holeKurswert200();
		float s = comp.holeSteigung();

		// holt das depot für den user
		Depot dep = bank.getDepot(pusername);

		String msg = "<" + tdbo.getThreadname() + "> tid<" + tdbo.getThreadid()
				+ "> date<" + Tools.entferneZeit(copost.getDatetime())
				+ "> kv<" + kv.getKv() + "> kv30<" + kv30 + "> kv200<" + kv200
				+ "> s<" + s + ">";
		inf.writezeile(msg);

		if (s > 2)
		{
			String grund = "Steigung s=" + s;
			// Kaufe ins Depot

			float cash = dep.getCashbestand();
			if (cash > 1000)
			{
				// Wenn noch Geld da ist dann Kaufe für 1000 Euro
				float anz = 1000f / kv.getKv();

				dep.kauf(Tools.entferneZeit(copost.getDatetime()), tdbo
						.getThreadname(), tdbo.getSymbol(), tdbo.getThreadid(),
						copost.getPostid(), pusername, kv.getKv(), kv30, anz,
						grund);
			}
		} else
			dep.setzeLastKurse(tdbo.getSymbol(), kv.getKv(), kv30);
	}

	public void werteVerfahren38x2Masteruser(Compressor comp, String pusername,
			CompressedPostingObj copost, ThreadDbObj tdbo,
			String lasterrorsymbol, Bank bank)
	{
		// Falls Steigung >2 dann investiere
		Inf inf = new Inf();
		String fnam = GC.rootpath + "\\db\\UserThreadVirtualKonto\\alg38x2.txt";
		inf.setFilename(fnam);
		Kursvalue kv = comp.holeEinzelkurswert(copost);

		float kv30 = comp.holeKurswert38();
		float kv200 = comp.holeKurswert200();
		float s = comp.holeSteigung();

		// holt das depot für den user
		Depot dep = bank.getDepot(pusername);

		String msg = "<" + tdbo.getThreadname() + "> tid<" + tdbo.getThreadid()
				+ "> date<" + Tools.entferneZeit(copost.getDatetime())
				+ "> kv<" + kv.getKv() + "> kv30<" + kv30 + "> kv200<" + kv200
				+ "> s<" + s + ">";
		inf.writezeile(msg);

		if (s > 3)
		{
			String grund = "Steigung s=" + s;
			// Kaufe ins Depot

			float cash = dep.getCashbestand();
			if (cash > 1500)
			{
				// Wenn noch Geld da ist dann Kaufe für 1000 Euro
				float anz = 1500f / kv.getKv();

				dep.kauf(Tools.entferneZeit(copost.getDatetime()), tdbo
						.getThreadname(), tdbo.getSymbol(), tdbo.getThreadid(),
						copost.getPostid(), pusername, kv.getKv(), kv30, anz,
						grund);
			}
		} else
			dep.setzeLastKurse(tdbo.getSymbol(), kv.getKv(), kv30);
	}

	public void werteVerfahren38x3Masteruser(Compressor comp, String pusername,
			CompressedPostingObj copost, ThreadDbObj tdbo,
			String lasterrorsymbol, Bank bank)
	{
		// Falls Steigung >2 dann investiere
		Inf inf = new Inf();
		String fnam = GC.rootpath + "\\db\\UserThreadVirtualKonto\\alg38x3.txt";
		inf.setFilename(fnam);
		Kursvalue kv = comp.holeEinzelkurswert(copost);
		float kv30 = comp.holeKurswert38();
		float kv200 = comp.holeKurswert200();
		float s = comp.holeSteigung();

		// holt das depot für den user
		Depot dep = bank.getDepot(pusername);

		String msg = "<" + tdbo.getThreadname() + "> tid<" + tdbo.getThreadid()
				+ "> date<" + Tools.entferneZeit(copost.getDatetime())
				+ "> kv<" + kv.getKv() + "> kv30<" + kv30 + "> kv200<" + kv200
				+ "> s<" + s + ">";
		inf.writezeile(msg);

		if (s > 5)
		{
			String grund = "Steigung s=" + s;
			// Kaufe ins Depot

			float cash = dep.getCashbestand();
			if (cash > 10000)
			{
				// Wenn noch Geld da ist dann Kaufe für 1000 Euro
				float anz = 10000f / kv.getKv();

				dep.kauf(Tools.entferneZeit(copost.getDatetime()), tdbo
						.getThreadname(), tdbo.getSymbol(), tdbo.getThreadid(),
						copost.getPostid(), pusername, kv.getKv(), kv30, anz,
						grund);
			}
		} else
			dep.setzeLastKurse(tdbo.getSymbol(), kv.getKv(), kv30);
	}

	public void werteVerfahren38x4Masteruser(Compressor comp, String pusername,
			CompressedPostingObj copost, ThreadDbObj tdbo,
			String lasterrorsymbol, Bank bank)
	{
		// Falls Steigung >2 dann investiere
		Inf inf = new Inf();
		String fnam = GC.rootpath + "\\db\\UserThreadVirtualKonto\\alg38x4.txt";
		inf.setFilename(fnam);
		Kursvalue kv = comp.holeEinzelkurswert(copost);

		float kv30 = comp.holeKurswert38();
		float kv200 = comp.holeKurswert200();
		float s = comp.holeSteigung();

		// holt das depot für den user
		Depot dep = bank.getDepot(pusername);

		// prüft ob im depot schon die tid drin ist, wenn ja mache nix
		if (dep.hasSymbol(tdbo.getSymbol()) == true)
			return;

		String msg = "<" + tdbo.getThreadname() + "> tid<" + tdbo.getThreadid()
				+ "> date<" + Tools.entferneZeit(copost.getDatetime())
				+ "> kv<" + kv.getKv() + "> kv30<" + kv30 + "> kv200<" + kv200
				+ "> s<" + s + ">";
		inf.writezeile(msg);

		// kaufe kräftig wenn nix im depot
		if (s > 2)
		{
			String grund = "Steigung s=" + s;
			// Kaufe ins Depot

			float cash = dep.getCashbestand();
			float euro = Ma.calcKaufbetrag(s) * 5;

			if (cash > euro)
			{
				// Wenn noch Geld da ist dann Kaufe für 1000 Euro
				float anz = euro / kv.getKv();

				dep.kauf(Tools.entferneZeit(copost.getDatetime()), tdbo
						.getThreadname(), tdbo.getSymbol(), tdbo.getThreadid(),
						copost.getPostid(), pusername, kv.getKv(), kv30, anz,
						grund);
			}
		} else
			dep.setzeLastKurse(tdbo.getSymbol(), kv.getKv(), kv30);
	}

	private float calcSteigung(float k, float k38, float k200)
	{
		if ((k38 == 0) || (k200 == 0))
			return 0;

		float s = (k * k38) / (k200 * k200);

		if ((k38 > k200) && (k > k38))
			return s;
		else
			return 0;
	}

	public void werteVerfahren13Masteruser(Compressor comp, String pusername,
			int index, CompressedPostingObj copost, String threadname,
			int threadid, String aktthreadname, String lasterrorsymbol,
			String aktthreadnamz, int threadpos, int posmax, float maxfaktor,
			float gefaelle, ThreadAttribStoreI tstore)
	{
		// Falls Steigung >2 dann investiere
		Inf inf = new Inf();
		String fnam = GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\alg38-200b.txt";
		inf.setFilename(fnam);
		Kursvalue kv = comp.holeEinzelkurswert(copost);

		float kv30 = comp.holeKurswert38();
		float kv200 = comp.holeKurswert200();
		float s = this.calcSteigung(kv.getKv(), kv30, kv200);

		if (s > 2)
		{
			// investiere kurzfristig 10Tage
			Gewinn gewinn = calcGewinnNachXtagen(pusername, copost, 500, 10,
					lasterrorsymbol, aktthreadnamz, null, "kv38200b", 0);

			inf.writezeile("tnam<" + aktthreadnamz + "> kv30<" + kv30 + "> k1<"
					+ gewinn.getKurs1() + "> k30<" + gewinn.getKurs2()
					+ "> gewinn<" + gewinn.getGewinn() + ">");

			UserEinPostingGewinnObj ueingewobj = new UserEinPostingGewinnObj(
					copost.getDatetime(), aktthreadname, copost.getThreadid(),
					copost.getPostid(), gewinn.getGewinn(), gewinn.getKurs1(),
					gewinn.getKurs2(), 0, symb_g, gewinn.getKursinfo());

			ugewinndbII_g.addGewinnObj(index, pusername, ueingewobj);
		}

	}

	public void werteVerfahren14Masteruser(Compressor comp, String pusername,
			int index, CompressedPostingObj copost, String threadname,
			int threadid, String aktthreadname, String lasterrorsymbol,
			String aktthreadnamz, int threadpos, int posmax, float maxfaktor,
			float gefaelle, ThreadAttribStoreI tstore)
	{
		// Falls Steigung >2 dann investiere

		Inf inf = new Inf();
		String fnam = GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\alg38-200b2.txt";
		inf.setFilename(fnam);
		Kursvalue kv = comp.holeEinzelkurswert(copost);

		float kv30 = comp.holeKurswert38();
		float kv200 = comp.holeKurswert200();
		float s = this.calcSteigung(kv.getKv(), kv30, kv200);

		if (s > 2)
		{
			// investiere kurzfristig 10Tage
			Gewinn gewinn = calcGewinnNachXtagen(pusername, copost, 5000, 10,
					lasterrorsymbol, aktthreadnamz, null, "kv38200b2", 0);

			inf.writezeile("tnam<" + aktthreadnamz + "> kv30<" + kv30 + "> k1<"
					+ gewinn.getKurs1() + "> k30<" + gewinn.getKurs2()
					+ "> gewinn<" + gewinn.getGewinn() + ">");

			UserEinPostingGewinnObj ueingewobj = new UserEinPostingGewinnObj(
					copost.getDatetime(), aktthreadname, copost.getThreadid(),
					copost.getPostid(), gewinn.getGewinn(), gewinn.getKurs1(),
					gewinn.getKurs2(), 0, symb_g, gewinn.getKursinfo());

			ugewinndbII_g.addGewinnObj(index, pusername, ueingewobj);
		}
		if (s > 5)
		{
			// investiere kurzfristig 10Tage
			Gewinn gewinn = calcGewinnNachXtagen(pusername, copost, 15000, 10,
					lasterrorsymbol, aktthreadnamz, null, "kv38200b2", 0);

			inf.writezeile("tnam<" + aktthreadnamz + "> kv30<" + kv30 + "> k1<"
					+ gewinn.getKurs1() + "> k30<" + gewinn.getKurs2()
					+ "> gewinn<" + gewinn.getGewinn() + ">");

			UserEinPostingGewinnObj ueingewobj = new UserEinPostingGewinnObj(
					copost.getDatetime(), aktthreadname, copost.getThreadid(),
					copost.getPostid(), gewinn.getGewinn(), gewinn.getKurs1(),
					gewinn.getKurs2(), 0, symb_g, gewinn.getKursinfo());

			ugewinndbII_g.addGewinnObj(index, pusername, ueingewobj);
		}
	}

	public void werteVerfahren15Masteruser(Compressor comp, String pusername,
			int index, CompressedPostingObj copost, String threadname,
			int threadid, String aktthreadname, String lasterrorsymbol,
			String aktthreadnamz, int threadpos, int posmax, float maxfaktor,
			float gefaelle, ThreadAttribStoreI tstore)
	{
		// Falls Steigung >2 dann investiere

		Inf inf = new Inf();
		String fnam = GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\alg38-200b3.txt";
		inf.setFilename(fnam);
		Kursvalue kv = comp.holeEinzelkurswert(copost);

		float kv30 = comp.holeKurswert38();
		float kv200 = comp.holeKurswert200();
		float s = this.calcSteigung(kv.getKv(), kv30, kv200);

		if (s > 2)
		{
			// investiere kurzfristig 10Tage
			Gewinn gewinn = calcGewinnNachXtagen(pusername, copost, 100000, 10,
					lasterrorsymbol, aktthreadnamz, null, "kv38200b3", 0);

			inf.writezeile("tnam<" + aktthreadnamz + "> kv30<" + kv30 + "> k1<"
					+ gewinn.getKurs1() + "> k30<" + gewinn.getKurs2()
					+ "> gewinn<" + gewinn.getGewinn() + ">");

			UserEinPostingGewinnObj ueingewobj = new UserEinPostingGewinnObj(
					copost.getDatetime(), aktthreadname, copost.getThreadid(),
					copost.getPostid(), gewinn.getGewinn(), gewinn.getKurs1(),
					gewinn.getKurs2(), 0, symb_g, gewinn.getKursinfo());

			ugewinndbII_g.addGewinnObj(index, pusername, ueingewobj);
		}
		if (s > 4)
		{
			// investiere kurzfristig 10Tage
			Gewinn gewinn = calcGewinnNachXtagen(pusername, copost, 250000, 10,
					lasterrorsymbol, aktthreadnamz, null, "kv38200b3", 0);

			inf.writezeile("tnam<" + aktthreadnamz + "> kv30<" + kv30 + "> k1<"
					+ gewinn.getKurs1() + "> k30<" + gewinn.getKurs2()
					+ "> gewinn<" + gewinn.getGewinn() + ">");

			UserEinPostingGewinnObj ueingewobj = new UserEinPostingGewinnObj(
					copost.getDatetime(), aktthreadname, copost.getThreadid(),
					copost.getPostid(), gewinn.getGewinn(), gewinn.getKurs1(),
					gewinn.getKurs2(), 0, symb_g, gewinn.getKursinfo());

			ugewinndbII_g.addGewinnObj(index, pusername, ueingewobj);
		}
		if (s > 6)
		{
			// investiere kurzfristig 10Tage
			Gewinn gewinn = calcGewinnNachXtagen(pusername, copost, 350000, 10,
					lasterrorsymbol, aktthreadnamz, null, "kv38200b3", 0);

			inf.writezeile("tnam<" + aktthreadnamz + "> kv30<" + kv30 + "> k1<"
					+ gewinn.getKurs1() + "> k30<" + gewinn.getKurs2()
					+ "> gewinn<" + gewinn.getGewinn() + ">");

			UserEinPostingGewinnObj ueingewobj = new UserEinPostingGewinnObj(
					copost.getDatetime(), aktthreadname, copost.getThreadid(),
					copost.getPostid(), gewinn.getGewinn(), gewinn.getKurs1(),
					gewinn.getKurs2(), 0, symb_g, gewinn.getKursinfo());

			ugewinndbII_g.addGewinnObj(index, pusername, ueingewobj);
		}

	}

	public void werteVerfahren7Masteruser(Compressor comp, Sliderbewert slb,
			String pusername, int index, CompressedPostingObj copost,
			String threadname, int threadid, String aktthreadname,
			String lasterrorsymbol, String aktthreadnamz, int threadpos,
			int posmax, float maxfaktor, float gefaelle,
			ThreadAttribStoreI tstore)
	{
		Inf inf = new Inf();
		String fnam = GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\alg38-200vertrau.txt";
		inf.setFilename(fnam);
		Kursvalue kv = comp.holeEinzelkurswert(copost);

		// Rechne die Steigung aus
		float kv30 = comp.holeKurswert38();
		float kv200 = comp.holeKurswert200();
		float s = this.calcSteigung(kv.getKv(), kv30, kv200);

		// Rechne das Vertrauen aus
		Vertrauen vertrau = postslider_glob.calcSliderVertrauensfaktor3(slb,
				GC.SLIDERINDEX20TAGE, maxfaktor, (float) 0.1, gefaelle, 20);

		float vertrauensfaktor = vertrau.getVertrauensfaktor_g();

		// Wenn Steigung und Vertrauen da sind dann investiere kurzfristig
		if ((s > 2) && (vertrauensfaktor > 0))
		{
			// investiere kurzfristig 10Tage
			Gewinn gewinn = calcGewinnNachXtagen(pusername, copost,
					vertrauensfaktor * 100, 10, lasterrorsymbol, aktthreadnamz,
					null, "kv38200vertrau", 0);

			inf.writezeile("tnam<" + aktthreadnamz + "> kv30<" + kv30 + "> k1<"
					+ gewinn.getKurs1() + "> k30<" + gewinn.getKurs2()
					+ "> gewinn<" + gewinn.getGewinn() + ">");

			UserEinPostingGewinnObj ueingewobj = new UserEinPostingGewinnObj(
					copost.getDatetime(), aktthreadname, copost.getThreadid(),
					copost.getPostid(), gewinn.getGewinn(), gewinn.getKurs1(),
					gewinn.getKurs2(), 0, symb_g, gewinn.getKursinfo());

			ugewinndbII_g.addGewinnObj(index, pusername, ueingewobj);
		}

	}

	public void werteVerfahren8MasteruserBad(Compressor comp, Sliderbewert slb,
			String pusername, int index, CompressedPostingObj copost,
			String threadname, int threadid, String aktthreadname,
			String lasterrorsymbol, String aktthreadnamz, int threadpos,
			int posmax, float maxfaktor, float gefaelle,
			ThreadAttribStoreI tstore)
	{
		// c)Verfahren3, Sliding-Fenster betrachte einen Postingzeitraum (z.B.
		// 20 Tage)
		// wieviele erfahrene User sind in den letzten
		// Postings vorher in diesem Thread
		// Je mehr erfahrene User in dem Slidingfenster je höher setze den
		// Gewinn ein. Sind nur luschen im Fenster, dann setze nix ein
		// Inf infkg = new Inf();
		// Inf infg = new Inf();
		Inf inf = new Inf();

		String fnam = GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\MasteruserListen\\" + threadid
				+ "_gute_schlechte_bad" + ".txt";

		/*
		 * infkg.setFilename(fnamkg); infg.setFilename(fnamg);
		 */
		inf.setFilename(fnam);

		Vertrauen vertrau = postslider_glob.calcSliderVertrauensfaktor2bad(slb,
				GC.SLIDERINDEX20TAGE, maxfaktor, (float) 0.1, gefaelle);

		float vertrauensfaktor = vertrau.getVertrauensfaktor_g();
		Gewinn gewinn = calcGewinnNachXtagen(pusername, copost,
				vertrauensfaktor * 100, 10, lasterrorsymbol, aktthreadnamz,
				slb, "werteVerfahren8MasteruserBad", vertrauensfaktor);

		inf.writezeile("tid<" + threadid + "> postanz<"
				+ vertrau.getSliderelemanz_g() + "> mitlRan<"
				+ vertrau.getDurchschnittrank_g() + "> guteU<"
				+ vertrau.getAnzgute_g() + "> schlechteU<"
				+ vertrau.getAnzschlechte_g() + "> k1<" + gewinn.getKurs1()
				+ "> k2<" + gewinn.getKurs2() + "> Gewinn<"
				+ gewinn.getGewinn() + ">");

		if (gewinn.getGewinn() == 0)
			return;

		UserEinPostingGewinnObj ueingewobj = new UserEinPostingGewinnObj(copost
				.getDatetime(), aktthreadname, copost.getThreadid(), copost
				.getPostid(), gewinn.getGewinn(), gewinn.getKurs1(), gewinn
				.getKurs2(), 0, symb_g, gewinn.getKursinfo());

		ugewinndbII_g.addGewinnObj(index, pusername, ueingewobj);
		// Protokolliere bei hohen Vertrauen

		if (pusername.equalsIgnoreCase("MasteruserXbad"))
		{
			String msg = "date<" + Tools.entferneZeit(copost.getDatetime())
					+ "> tnam<" + Tools.holeSubstring(aktthreadname, 15)
					+ "> tid<" + copost.getThreadid() + "> pid<"
					+ copost.getPostid() + "> K1<" + gewinn.getKurs1()
					+ "> K2<" + gewinn.getKurs2() + "> Gew<"
					+ gewinn.getGewinn() + "> vertrau<" + vertrauensfaktor
					+ "> anzSlUser<" + vertrau.getSliderelemanz_g()
					+ "> avRank<" + vertrau.getDurchschnittrank_g() + ">";
			infvertrau.writezeile(msg);
		}

	}

	public void werteVerfahren9Masteruser(Sliderbewert slb, String pusername,
			int index, CompressedPostingObj copost, String threadname,
			int threadid, String aktthreadname, String lasterrorsymbol,
			String aktthreadnamz, int threadpos, int posmax, float maxfaktor,
			float gefaelle, ThreadAttribStoreI tstore)
	{
		Inf inf = new Inf();
		String fnam = GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\MasteruserListen\\" + threadid
				+ "_gute_schlechte" + ".txt";

		/*
		 * infkg.setFilename(fnamkg); infg.setFilename(fnamg);
		 */
		inf.setFilename(fnam);

		Vertrauen vertrau = postslider_glob.calcSliderVertrauensfaktor3(slb,
				GC.SLIDERINDEX20TAGE, maxfaktor, (float) 0.1, gefaelle, 20);

		float vertrauensfaktor = vertrau.getVertrauensfaktor_g();
		Gewinn gewinn = calcGewinnNachXtagen(pusername, copost,
				vertrauensfaktor * 100, 10, lasterrorsymbol, aktthreadnamz,
				slb, "werteVerfahren9Masteruser", vertrauensfaktor);

		inf.writezeile("tid<" + threadid + "> postanz<"
				+ vertrau.getSliderelemanz_g() + "> mitlRan<"
				+ vertrau.getDurchschnittrank_g() + "> guteU<"
				+ vertrau.getAnzgute_g() + "> schlechteU<"
				+ vertrau.getAnzschlechte_g() + "> k1<" + gewinn.getKurs1()
				+ "> k2<" + gewinn.getKurs2() + "> Gewinn<"
				+ gewinn.getGewinn() + ">");

		if (gewinn.getGewinn() == 0)
			return;

		UserEinPostingGewinnObj ueingewobj = new UserEinPostingGewinnObj(copost
				.getDatetime(), aktthreadname, copost.getThreadid(), copost
				.getPostid(), gewinn.getGewinn(), gewinn.getKurs1(), gewinn
				.getKurs2(), 0, symb_g, gewinn.getKursinfo());

		ugewinndbII_g.addGewinnObj(index, pusername, ueingewobj);

		// Protokolliere bei hohen Vertrauen
		if (pusername.equalsIgnoreCase("MasteruserX"))
		{
			String msg = "date<" + Tools.entferneZeit(copost.getDatetime())
					+ "> tnam<" + Tools.holeSubstring(aktthreadname, 15)
					+ "> tid<" + copost.getThreadid() + "> pid<"
					+ copost.getPostid() + "> K1<" + gewinn.getKurs1()
					+ "> K2<" + gewinn.getKurs2() + "> Gew<"
					+ gewinn.getGewinn() + "> vertrau<" + vertrauensfaktor
					+ "> anzSlUser<" + vertrau.getSliderelemanz_g()
					+ "> avRank<" + vertrau.getDurchschnittrank_g() + ">";
			infvertrau.writezeile(msg);
		}
	}

	public void werteVerfahren10Masteruser(Sliderbewert slb, String pusername,
			int index, CompressedPostingObj copost, String threadname,
			int threadid, String aktthreadname, String lasterrorsymbol,
			String aktthreadnamz, int threadpos, int posmax, float maxfaktor,
			float gefaelle, ThreadAttribStoreI tstore)
	{
		Inf inf = new Inf();
		String fnam = GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\MasteruserListen\\" + threadid
				+ "_gute_schlechte10" + ".txt";

		inf.setFilename(fnam);
		Vertrauen vertrau = postslider_glob.calcSliderVertrauensfaktor3(slb,
				GC.SLIDERINDEX20TAGE, maxfaktor, (float) 0.1, gefaelle, 30);

		float vertrauensfaktor = vertrau.getVertrauensfaktor_g();
		Gewinn gewinn = calcGewinnNachXtagen(pusername, copost,
				vertrauensfaktor * 100, 10, lasterrorsymbol, aktthreadnamz,
				slb, "werteVerfahren10Masteruser", vertrauensfaktor);

		inf.writezeile("tid<" + threadid + "> postanz<"
				+ vertrau.getSliderelemanz_g() + "> mitlRan<"
				+ vertrau.getDurchschnittrank_g() + "> guteU<"
				+ vertrau.getAnzgute_g() + "> schlechteU<"
				+ vertrau.getAnzschlechte_g() + "> k1<" + gewinn.getKurs1()
				+ "> k2<" + gewinn.getKurs2() + "> Gewinn<"
				+ gewinn.getGewinn() + ">");

		if (gewinn.getGewinn() == 0)
			return;

		UserEinPostingGewinnObj ueingewobj = new UserEinPostingGewinnObj(copost
				.getDatetime(), aktthreadname, copost.getThreadid(), copost
				.getPostid(), gewinn.getGewinn(), gewinn.getKurs1(), gewinn
				.getKurs2(), 0, symb_g, gewinn.getKursinfo());

		ugewinndbII_g.addGewinnObj(index, pusername, ueingewobj);
		// Protokolliere bei hohen Vertrauen

		if (pusername.equalsIgnoreCase("MasteruserX3"))
		{
			String msg = "date<" + Tools.entferneZeit(copost.getDatetime())
					+ "> tnam<" + Tools.holeSubstring(aktthreadname, 15)
					+ "> tid<" + copost.getThreadid() + "> pid<"
					+ copost.getPostid() + "> K1<" + gewinn.getKurs1()
					+ "> K2<" + gewinn.getKurs2() + "> Gew<"
					+ gewinn.getGewinn() + "> vertrau<" + vertrauensfaktor
					+ "> anzSlUser<" + vertrau.getSliderelemanz_g()
					+ "> avRank<" + vertrau.getDurchschnittrank_g() + ">";
			infvertrau.writezeile(msg);
		}
	}

	public void werteVerfahren11Masteruser(Sliderbewert slb, String pusername,
			int index, CompressedPostingObj copost, String threadname,
			int threadid, String aktthreadname, String lasterrorsymbol,
			String aktthreadnamz, int threadpos, int posmax, float maxfaktor,
			float gefaelle, ThreadAttribStoreI tstore)
	{

		Inf inf = new Inf();

		String fnam = GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\MasteruserListen\\" + threadid
				+ "_gute_schlechte11" + ".txt";

		inf.setFilename(fnam);

		Vertrauen vertrau = postslider_glob.calcSliderVertrauensfaktor4(slb,
				GC.SLIDERINDEX20TAGE, maxfaktor, (float) 0.1, gefaelle, 10);

		float vertrauensfaktor = vertrau.getVertrauensfaktor_g();
		Gewinn gewinn = calcGewinnNachXtagen(pusername, copost,
				vertrauensfaktor * 100, 10, lasterrorsymbol, aktthreadnamz,
				slb, "werteVerfahren11Masteruser", vertrauensfaktor);

		inf.writezeile("tid<" + threadid + "> postanz<"
				+ vertrau.getSliderelemanz_g() + "> mitlRan<"
				+ vertrau.getDurchschnittrank_g() + "> guteU<"
				+ vertrau.getAnzgute_g() + "> schlechteU<"
				+ vertrau.getAnzschlechte_g() + "> k1<" + gewinn.getKurs1()
				+ "> k2<" + gewinn.getKurs2() + "> Gewinn<"
				+ gewinn.getGewinn() + ">");

		if (gewinn.getGewinn() == 0)
			return;

		UserEinPostingGewinnObj ueingewobj = new UserEinPostingGewinnObj(copost
				.getDatetime(), aktthreadname, copost.getThreadid(), copost
				.getPostid(), gewinn.getGewinn(), gewinn.getKurs1(), gewinn
				.getKurs2(), 0, symb_g, gewinn.getKursinfo());

		ugewinndbII_g.addGewinnObj(index, pusername, ueingewobj);
		// Protokolliere bei hohen Vertrauen

		if (pusername.equalsIgnoreCase("MasteruserX4"))
		{
			String msg = "date<" + Tools.entferneZeit(copost.getDatetime())
					+ "> tnam<" + Tools.holeSubstring(aktthreadname, 15)
					+ "> tid<" + copost.getThreadid() + "> pid<"
					+ copost.getPostid() + "> K1<" + gewinn.getKurs1()
					+ "> K2<" + gewinn.getKurs2() + "> Gew<"
					+ gewinn.getGewinn() + "> vertrau<" + vertrauensfaktor
					+ "> anzSlUser<" + vertrau.getSliderelemanz_g()
					+ "> avRank<" + vertrau.getDurchschnittrank_g() + ">";
			infvertrau.writezeile(msg);
		}
	}

	public void werteVerfahren12Masteruser(Sliderbewert slb, String pusername,
			int index, CompressedPostingObj copost, String threadname,
			int threadid, String aktthreadname, String lasterrorsymbol,
			String aktthreadnamz, int threadpos, int posmax, float maxfaktor,
			float gefaelle, ThreadAttribStoreI tstore)
	{
		Inf inf = new Inf();
		String fnam = GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\MasteruserListen\\" + threadid
				+ "_gute_schlechte11" + ".txt";
		inf.setFilename(fnam);

		Vertrauen vertrau = postslider_glob.calcSliderVertrauensfaktor5(slb,
				GC.SLIDERINDEX20TAGE, maxfaktor, (float) 0.1, gefaelle, 10);

		float vertrauensfaktor = vertrau.getVertrauensfaktor_g();
		Gewinn gewinn = calcGewinnNachXtagen(pusername, copost,
				vertrauensfaktor * 100, 10, lasterrorsymbol, aktthreadnamz,
				slb, "werteVerfahren12Masteruser", vertrauensfaktor);

		inf.writezeile("tid<" + threadid + "> postanz<"
				+ vertrau.getSliderelemanz_g() + "> mitlRan<"
				+ vertrau.getDurchschnittrank_g() + "> guteU<"
				+ vertrau.getAnzgute_g() + "> schlechteU<"
				+ vertrau.getAnzschlechte_g() + "> k1<" + gewinn.getKurs1()
				+ "> k2<" + gewinn.getKurs2() + "> Gewinn<"
				+ gewinn.getGewinn() + ">");

		if (gewinn.getGewinn() == 0)
			return;

		UserEinPostingGewinnObj ueingewobj = new UserEinPostingGewinnObj(copost
				.getDatetime(), aktthreadname, copost.getThreadid(), copost
				.getPostid(), gewinn.getGewinn(), gewinn.getKurs1(), gewinn
				.getKurs2(), 0, symb_g, gewinn.getKursinfo());

		ugewinndbII_g.addGewinnObj(index, pusername, ueingewobj);
		// Protokolliere bei hohen Vertrauen

		if (pusername.equalsIgnoreCase("MasteruserXNeu"))
		{
			String msg = "date<" + Tools.entferneZeit(copost.getDatetime())
					+ "> tnam<" + Tools.holeSubstring(aktthreadname, 15)
					+ "> tid<" + copost.getThreadid() + "> pid<"
					+ copost.getPostid() + "> K1<" + gewinn.getKurs1()
					+ "> K2<" + gewinn.getKurs2() + "> Gew<"
					+ gewinn.getGewinn() + "> vertrau<" + vertrauensfaktor
					+ "> anzSlUser<" + vertrau.getSliderelemanz_g()
					+ "> avRank<" + vertrau.getDurchschnittrank_g() + ">";
			infvertrau.writezeile(msg);
		}
	}

	public void addMasteruserGewinnSt3(UserGewStrategieObjII u2obj,
			Sliderbewert slb, CompressedPostingObj copost, ThreadDbObj tdbo,
			int threadpos, int posmax, String lasterrorsymbol)
	{
		Gewinn gewinn = null;
		Vertrauen vertrau = handel.VertrauensfaktorMasteruser(u2obj
				.getGuteUserfaktor_G(), u2obj.getGutePostingsfaktor_G(), slb,
				GC.SLIDERINDEX20TAGE, postslider_glob.getPostanzahl(GC.SLIDERINDEX20TAGE),
				u2obj.getMaxfaktor_G(), u2obj.getGefaelle_G(), u2obj
						.getMinguteUser_G(), u2obj.getMinaktivität_G());

		

		float vertrauensfaktor = vertrau.getVertrauensfaktor_g();
		if (vertrauensfaktor > 0)
		{
			gewinn = calcGewinnNachXtagen(null, copost, vertrauensfaktor * 100,
					10, lasterrorsymbol, tdbo.getThreadname(), slb, u2obj
							.getUsername_G(), vertrauensfaktor);
		}
		if ((gewinn==null)||(gewinn.getGewinn() == 0))
			return;

		UserEinPostingGewinnObj ueingewobj = new UserEinPostingGewinnObj(copost
				.getDatetime(), tdbo.getThreadname(), copost.getThreadid(),
				copost.getPostid(), gewinn.getGewinn(), gewinn.getKurs1(),
				gewinn.getKurs2(), tdbo.getMasterid(), symb_g, gewinn
						.getKursinfo());

		ugewinndbII_g.addGewinnObj(3, u2obj.getUsername_G(), ueingewobj);
	}
}
