package slider;

import handelsstrategien.Handelsstrategie;
import hilfsklasse.DateExcecption;
import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;

import java.util.ArrayList;
import java.util.HashSet;

import mainPackage.GC;
import objects.BadObjectException;
import objects.ThreadDbObj;
import objects.UserDbObj;
import ranking.RangObj;
import ranking.Vertrauen;
import stores.PostDBx;
import stores.SlideruebersichtDB;
import stores.UserDB;

import compressor.CompressedThreadDBx;

import db.CompressedPostingObj;

public class PostSlider
{
	// die Klasse Slider beinhaltet die lezten Postings für einen Thread
	// man möchte hier ermitteln welche User hier so gepostet haben
	// waren erfahrene User zuvor am Werk kann durchaus ein Wetteinsatz höher
	// ausfallen
	// Wird eine bestimmte Erfahrungsschwelle überschritten dann wird gekauft !!

	// die sliderliste enthält die postings für eine tid, es gibt max 10 Slider
	private ArrayList<PostSliderElem>[] sliderliste = new ArrayList[10];

	// der 3 Monats slider, speichert usermenge und postings
	// dieser 3 Monatsslider arbeitet nur mir den 20er Tage Slider im index
	// GC.SLIDER20TAgeZeitraum
	// zusammen
	private HashSet<String> slidermenge90 = null;
	private ArrayList<PostSliderElem> sliderliste90 = null;

	// protokolliert welcher username schon für NeueUserImSlider protokolliert
	// wurde
	private HashSet<String> NeueUserImSlider_prot = null;

	private int tid_glob = 0;
	private int mid_glob = 0;
	private int rankfehlzaehler_g = 0, rankokzaehler_g = 0;
	private int[] slidergroesse_g = new int[10];
	private UserDB udb_glob=null;
	private String aktname_g = null;
	private ThreadDbObj tdbo_g = null;
	private Handelsstrategie handel=null;

	private Inf infvertrau = new Inf();

	public PostSlider(UserDB udb)
	{
		udb_glob = udb;
		// inf.setFilename(GC.rootpath+"\\db\\reporting\\sliderinfo.txt");
		infvertrau.setFilename(GC.rootpath
				+ "\\db\\reporting\\slidervertrauinfo.txt");
		handel=new Handelsstrategie(this,udb.GetanzObj());
	}

	public void initSlider(ThreadDbObj tdbo, int index, int groesse)
	{
		slidergroesse_g[index] = groesse;
		if (sliderliste[index] == null)
			sliderliste[index] = new ArrayList<PostSliderElem>();
		else
			sliderliste[index] = new ArrayList<PostSliderElem>();

		if (index == GC.SLIDERINDEX20TAGE)
		{
			slidermenge90 = new HashSet<String>();
			sliderliste90 = new ArrayList();
		}
		NeueUserImSlider_prot = new HashSet<String>();
		aktname_g = new String(tdbo.getThreadname());
		tid_glob = tdbo.getThreadid();
		tdbo_g = tdbo;
		mid_glob = tdbo.getMasterid();

	}
	public int getPostanzahl(int index)
	{
		return(sliderliste[index].size());
	}
	
	public String showSliderValues()
	{
		int a = 0, b = 0;
		a = sliderliste[1].size();

		b = sliderliste90.size();
		return ("SL20(" + a + ") SL90(" + b + ")");
	}

	public void setThreadname(String threadnam)
	{
		// Setzt den Threadnamen für den Slider

		String tempstr = "";
		aktname_g = new String(threadnam);

		// plausicheck
		// hier wird der gesetzte threadname noch mal zur sicherheit mit dem
		// tdbo_g verglichen
		// doppelt hält besser

		if (threadnam.contains("%3F"))
			tempstr = threadnam.replace("%3F", "?");
		else
			tempstr = threadnam;

		if (tempstr.contains("_0.html"))
			tempstr = tempstr.replace("_0.html", "");
		if (tempstr.startsWith("\\"))
			tempstr = tempstr.replace("\\", "");

		// zweiter versuch
		if (tempstr.equals(tdbo_g.getThreadname()) == true)
			return;
		if (tdbo_g.getThreadname().equals(tempstr) == true)
			return;

		Tracer
				.WriteTrace(10,
						"Error:internal threadnam passt nicht threadnam<"
								+ threadnam + "> tdbothreadnam<"
								+ tdbo_g.getThreadname() + ">");

	}

	public int getMasterid()
	{
		return mid_glob;
	}

	public void setMasterid(int mid_glob)
	{
		this.mid_glob = mid_glob;
	}

	public boolean addSliderPostingPostanzahl(int index, int tid,
			CompressedPostingObj copost)
	{
		// der slider betrachtet nur die anzahl der Postings
		if (slidergroesse_g[index] == 0)
			Tracer.WriteTrace(10,
					"Error:Es wurde vergessen die Slidergroesse zu setzten sliderindex=<"
							+ index + ">");

		if (tid != tdbo_g.getThreadid())
		{
			Tracer.WriteTrace(10, "Error:internal tid passt nicht zu tdbo <"
					+ tid + "> tid<" + tdbo_g.getThreadid() + ">");
		}

		int anz = sliderliste[index].size();
		// falls die tid sich ändert wird die sliderliste gelöscht und eine
		// neue wird begonnen

		PostSliderElem selem = new PostSliderElem(copost);
		UserDbObj udbo = udb_glob.getUserobj(copost.getUsername());
		selem.addUserObj(udbo);
		selem.plausicheck();
		sliderliste[index].add(selem);

		anz = sliderliste[index].size();
		// falls slider voll, dann lösche das element 0;
		if (anz > slidergroesse_g[index])
			sliderliste[index].remove(0);

		return true;
	}

	public boolean addSliderPostingZeitraum(int index, int tid,
			CompressedPostingObj copost)
	{
		// dieser Slider betrachtet den Zeitraum von 20 tagen, d.h. hier werden
		// die postings der letzten 20 Tage gesammelt

	
		
		if (tid != tdbo_g.getThreadid())
		{
			Tracer.WriteTrace(10, "Error:internal tid passt nicht zu tdbo x2<"
					+ tid + "> tid<" + tdbo_g.getThreadid() + ">");
		}

		final int maxslidertage = 20;
		if (slidergroesse_g[index] == 0)
			Tracer.WriteTrace(10,
					"Error:Es wurde vergessen die Slidergroesse zu setzten sliderindex=<"
							+ index + ">");

		int anz = sliderliste[index].size();

		// falls die tid sich ändert wird die sliderliste gelöscht und eine
		// neue wird begonnen

		PostSliderElem selem = new PostSliderElem(copost);
		UserDbObj udbo = udb_glob.getUserobj(copost.getUsername());
		selem.addUserObj(udbo);
		selem.plausicheck();
		sliderliste[index].add(selem);

		anz = sliderliste[index].size();

		// alter des neusten postings
		String datneu = copost.getDatetime();
		String datalt = sliderliste[index].get(0).elem_glob.getDatetime();
		int tagediff = Tools.zeitdifferenz_tage(datneu, datalt);

		// prüft das alter des ältesten Postings
		while (tagediff > maxslidertage)
		{
			// Ein Element vom Slider 20 wandert in den Slider90 wenn das Datum
			// überschritten
			// wurde !!!!
			slidermenge90
					.add(sliderliste[index].get(0).elem_glob.getUsername());
			sliderliste90.add(sliderliste[index].get(0));

			// und lösche es aus dem 20 tage slider
			sliderliste[index].remove(0);

			datneu = copost.getDatetime();
			datalt = sliderliste[index].get(0).elem_glob.getDatetime();
			tagediff = Tools.zeitdifferenz_tage(datneu, datalt);
		}
		return true;
	}

	public void dumpslider(int sliderindex, String fnam)
	{
		int anz = sliderliste[sliderindex].size();
		Inf inf = new Inf();
		inf.setFilename(fnam);
		inf.writezeile("*******************************************");
		for (int i = 0; i < anz; i++)
		{
			if (sliderliste[sliderindex] == null)
				continue;

			PostSliderElem se = sliderliste[sliderindex].get(i);
			if (se == null)
				continue;
			if (se.udbo_glob == null)
				continue;

			String unam = se.udbo_glob.get_username();
			int rank = se.getRank();
			String datum = se.elem_glob.getDatetime();
			String outstr = "i<" + i + "> dat<" + datum + "> unam<" + unam
					+ "> rank<" + rank + ">";
			inf.writezeile(outstr);
		}
	}

	private void gibSliderAus(ArrayList<PostSliderElem> sl, String verz,
			ThreadDbObj tdbo)
	{
		String outstr=null;
		Inf infslider = new Inf();
		int anz = sl.size();
		String fnam = verz + "\\" + tdbo.getThreadid() + ".txt";

		if (FileAccess.FileAvailable(fnam))
			FileAccess.FileDelete(fnam,0);
		infslider.setFilename(fnam);
		infslider.writezeile("***threadname=<" + tdbo.getThreadname()
				+ "> tid<" + tdbo.getThreadid() + "> anz<" + anz + "> ");
		infslider.writezeile("***i#datum#username#rank");
		// Speichere alle slider für den Index
		for (int i = 0; i < anz; i++)
		{
			if (sl == null)
				continue;

			PostSliderElem se = sl.get(i);
			if (se == null)
				continue;
			if (se.udbo_glob == null)
				continue;

			String unam = se.udbo_glob.get_username();
			int rank = se.getRank();
			String datum = se.elem_glob.getDatetime();
			if(se.udbo_glob.getBoostrang()==0)
				outstr = i + "#" + datum + "#" + unam + "#" + rank;
			else
			{
				Tracer.WriteTrace(20, "Info: Boostrang nicht erlaubt unam<"+unam+">");
				outstr = i + "#" + datum + "#" + unam + "#" + rank+"*** Boostrang="+se.udbo_glob.getBoostrang();
			}
			infslider.writezeile(outstr);
		}
	}

	
	
	public void calcAddUpdateSliderGenUebersichtGenPrognosen(int sliderindex,
			ThreadDbObj tdbo, SlideruebersichtDB sldb,
			CompressedThreadDBx compt, int wochenindex, int turboflag,
			PostDBx pdb)
	{
		// Hier werden die Slider gespeichert
		// Ausserdem wird die Slideruebersicht generiert
		// turboflag=1, beim turboflag wird die sldb nicht gespeichert

		// plausi
		if (tdbo.getThreadid() != tdbo_g.getThreadid())
		{
			Tracer.WriteTrace(10, "Error:internal tid passt nicht zu tdbo x3<"
					+ tdbo.getThreadid() + "> tid<" + tdbo_g.getThreadid()
					+ ">");
		}
		if (wochenindex == 0)
		{
			// schreibe den slider 20 in ein File
			gibSliderAus(sliderliste[sliderindex],
					GC.rootpath + "\\db\\slider", tdbo);

			// schreibe den slider90 in ein File
			gibSliderAus(sliderliste90, GC.rootpath + "\\db\\slider90", tdbo);
		}

		// für den Slider mit den Index wird der mittlere Rank ermittelt
		float mr = calcMittlererRank(sliderindex);
		if(mr<10)
			Tracer.WriteTrace(20, "Error: internal mittlerer Rank zu klein mr<"+mr+">");
		
		SlideruebersichtObj slobj;
		Sliderbewert slb = this.BewerteSlider(sliderindex, compt, pdb);

		try
		{
			//username#maxgewinn#gewinnrank#handelsstrategie#sliderindex#maxfaktor#minfaktor#gefaelle#
			//startsumme#maxdays#vertrauensfaktorAlgo# minguteUser# guteUserfaktor#gutePostingsfaktor#minaktivität
			//MasteruserI1946#1.7976508E7#31667#1#1# 20000.0#0.1#5.0#
			//100.0#10#1# 5# 2.2#2.2#3
	

			//Das Vertrauen ist abhängig vom Handelsalgorithmus
			/*Vertrauen vert=this.calcSliderVertrauensfaktorMasteruser(
				2.2f, 2.2f,  slb, sliderindex, 20000, 5, 5,3);*/
		
			Vertrauen vert=handel.VertrauensfaktorMasteruser(2.2f, 2.2f,  slb, sliderindex,sliderliste[sliderindex].size(), 20000, 5, 5,3);
			
			float f=vert.getVertrauensfaktor_g();
			
			String time_sliderupdate = Tools.get_aktdatetime_str();

			slobj = new SlideruebersichtObj(tdbo.getThreadid() + "#"
					+ wochenindex + "#" + tdbo.getThreadname() + "#"
					+ slb.getPostanz() + "#" + mr + "#"
					+ slb.getAnzgutePostings() + "#"
					+ slb.getAnzschlechtePostings() + "#" + slb.getGuteU()
					+ "#" + slb.getSchlechteU() + "#" + slb.getUseranz() + "#"
					+ slb.getAnzbaduser() + "#" + slb.getNeueGuteUserImSlider()
					+ "#" + slb.getNeueSchlechteUserImSlider() + "#"
					+ slb.getNeueBaduserImSlider() + "#" + 0 + "#" + f + "#"
					+ time_sliderupdate + "#" + tdbo.getSymbol() + "#"
					+ slb.getHandelshinweis());

			//hier wird der Attributspeicher auf Platte aktualisiert
		
			
			tdbo.setLastsliderupdateSORT(time_sliderupdate);
			sldb.UpdateSliderObject(slobj);

			// speichere beim turboflag==1 nicht die sldb
			if (turboflag == 0)
				sldb.WriteDB();
		} catch (BadObjectException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean addSliderPosting(int index, int tid,
			CompressedPostingObj copost)
	{

		if (tid != tdbo_g.getThreadid())
		{
			Tracer.WriteTrace(20, "Error:internal tid passt nicht zu tdbo x4<"
					+ tid + "> tid<" + tdbo_g.getThreadid() + ">");
			return false;
		}

		// slider 0, betrachtet postinganzahl von 20 (wird nicht verwendet)
		if (index == 0)
			addSliderPostingPostanzahl(index, tid, copost);
		// slider 1, betrachtet den Zeitrum von 20 Tagen
		else if (index == 1)
			// wird auch interessensfaktor genannt
			addSliderPostingZeitraum(index, tid, copost);
		else
			Tracer.WriteTrace(10, "Error: internal index<" + index
					+ "> nicht erlaubt !!");

		return true;

	}

	public float calcMittlererRank(int index)
	{
		// der mittlere Rank ist der durchschnitt der ränge im Slider mit dem
		// index
		int sumrp = 0, rank = 0, brang = 0;
		float durchschnittrank = 0;

		if (slidergroesse_g[index] == 0)
			Tracer.WriteTrace(10,
					"Error:Es wurde vergessen die Slidergroesse zu setzten sliderindex=<"
							+ index + ">");

		float maxanzahluser = udb_glob.GetanzObj();

		// ermittelt die anz user im Slider
		int slideruseranz = sliderliste[index].size();
		for (int i = 0; i < slideruseranz; i++)
		{
			// ermittel für alle user den durchschnittlichen rank
			PostSliderElem se = sliderliste[index].get(i);
			rank = se.getRank();
			if (rank <= 0)
			{
				rankfehlzaehler_g++;
				if (rankfehlzaehler_g % 2000000 == 0)
					Tracer.WriteTrace(20, "Warning: fehlz<" + rankfehlzaehler_g
							+ "> okzaehl<" + rankokzaehler_g + "> rank<" + rank
							+ "> username <" + se.udbo_glob.get_username()
							+ "> nicht erlaubt!! setze rank<"
							+ (int) (maxanzahluser / 2) + ">");
				rank = (int) (maxanzahluser / 2);
			} else
				rankokzaehler_g++;

			int br = se.getBoostrang();
			
			brang = brang + br;

			sumrp = sumrp + rank;
		}

		if (sumrp < 0)
			Tracer.WriteTrace(10, "Error: internalerror sumrp<" + sumrp
					+ "> kann nicht sein!!!");

		if (brang > 0)
		{
			
			if (sumrp - brang > 0)
				sumrp = sumrp - brang;
			else
				sumrp = 1;
		}

		if (slideruseranz > 1)
		{
			durchschnittrank = (float) sumrp / (float) slideruseranz;
		} else
			durchschnittrank = 1;

		if (durchschnittrank < 0)
			Tracer.WriteTrace(10, "Error: internalerror durchrank<"
					+ durchschnittrank + "> kann nicht sein!!!");
		return durchschnittrank;
	}

	private void initUsermenge3Monate(int index, CompressedThreadDBx compt)
	{
		// diese Menge hat die usernamen und Postings der letzten 3 Monate
		String datum3Mend = null, datum3Mstart = null;

		if ((sliderliste[index] == null)
				|| (sliderliste[index].isEmpty() == true))
			return;

		// hole das datum des ältesten Sliderelementes
		PostSliderElem pe = sliderliste[index].get(0);
		String datumSliderstart = Tools
				.entferneZeit(pe.elem_glob.getDatetime());

		// Mache nur ein Init des 90Tage Sliders wenn auch Postings vorhanden
		// sind
		CompressedPostingObj co = compt.getObjectIDX(0);
		String codat = Tools.entferneZeit(co.getDatetime());

		// mache kein Init, da alle Postings im Slider20 sind
		if (Tools.datum_ist_aelter_gleich(codat, datumSliderstart) == true)
			return;

		// ermittle start und endzeit für den 90Tage slider
		try
		{
			datum3Mend = Tools.subTimeHours(datumSliderstart, 24);
			datum3Mstart = Tools.modifziereDatum(datum3Mend, 0, 0, -90, 0);
		} catch (DateExcecption e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int anz = compt.getanz();
		for (int i = 0; i < anz; i++)
		{
			co = compt.getObjectIDX(i);
			codat = Tools.entferneZeit(co.getDatetime());

			if (Tools.datum_im_intervall(codat, datum3Mstart, datum3Mend) == true)
			{
				// baue die usermenge auf
				slidermenge90.add(co.getUsername());
				// baue die userliste auf
				PostSliderElem pe90 = new PostSliderElem(co);
				sliderliste90.add(pe90);
			}
		}
		return;
	}

	private void calcUsermenge3Monate(int index, CompressedThreadDBx compt)
	// ermittelt die usemenge die 3 Monate vor dem Slider im Thread ist
	// sehr rechenintensiv, hier muss noch optimiert werden !!!!!!!!!!!!!!!!!!
	{
		String datum3Mend = null;
		String datum3Mstart = null;
		// bei der ersten anfrage die menge aufbauen
		if (slidermenge90.isEmpty() == true)
		{
			initUsermenge3Monate(index, compt);
			return;
		}

		// passe den 90 Tage slider an(alte elemente raus)
		PostSliderElem pe = sliderliste[index].get(0);
		String datumSliderstart = Tools
				.entferneZeit(pe.elem_glob.getDatetime());
		// ermittle start und endzeit für den 90Tage slider
		try
		{
			datum3Mend = Tools.entferneZeit(Tools.subTimeHours(
					datumSliderstart, 24));
			datum3Mstart = Tools.entferneZeit(Tools.modifziereDatum(datum3Mend,
					0, 0, -90, 0));
		} catch (DateExcecption e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// entferne die alte elemente

		while (sliderliste90.isEmpty() == false)
		{
			pe = sliderliste90.get(0);
			String pedat = Tools.entferneZeit(pe.elem_glob.getDatetime());
			if (Tools.datum_im_intervall(pedat, datum3Mstart, datum3Mend) == false)
			{
				sliderliste90.remove(0);
				continue;
			} else
				break;
		}
		// baue die Slidermenge neu auf
		int anz = sliderliste90.size();
		slidermenge90.clear();
		for (int i = 0; i < anz; i++)
		{
			slidermenge90.add(sliderliste90.get(i).elem_glob.getUsername());
		}
	}

	private void ProtokolliereNeuenUserImSlider(int tid,
			CompressedThreadDBx compt, String unam, int r, PostDBx pdb)
	{
		// der User "unam" ist neu in diesem Thread
		// protokolliere den user

		int postanz = pdb.getUserPostinganz(unam);

		if (NeueUserImSlider_prot.contains(unam) == false)
		{
			int hatschonmal_flag = 0;
			String hatmessage = "";
			// falls noch nicht protokolliert für diesen thread
			// Unterscheide user ist "total neu" oder user war schon mal hier

			// schaut nach wieviele postings der thread hat
			int threadpostanz = compt.getanz();

			// holt sich die id, des ersten postings dieses users
			int userfirst = pdb.getUserPostnummerIDX(unam, 0);
			// schaut nach ober der user früher schon mal im thread gepostet hat
			int min = (int) (((float) threadpostanz / 100f) * 80f);
			if ((userfirst > 0) && (userfirst < min))
			{
				// user hat früher schon mal in diesen Thread gepostet
				hatschonmal_flag = 1;
			} else
				hatmessage = "**** User ist absolut neu im Thread ****";

			Inf inf = new Inf();
			inf.setFilename(GC.rootpath
					+ "\\db\\UserThreadVirtualKonto\\NeueUserImThread\\" + tid
					+ ".txt");

			String msg = "date<" + Tools.get_aktdatetime_str() + ">" + ":unam<"
					+ unam + "> rang<" + r + ">" + hatmessage;

			inf.writezeile(msg);
			Tracer.WriteTrace(50, "I:Neuer User im Slider" + msg);
			NeueUserImSlider_prot.add(unam);
		}
	}

	public Sliderbewert BewerteSlider(int index, CompressedThreadDBx compt,
			PostDBx pdb)
	{
		//index: gibt an welcher der Slidertypen gemeint ist
		//compt: gibt die compressed DBX an
		//pdb: ist die postdbx
		
		// Hier findet eine spezielle Sliderbewertung statt, die wird höchstens
		// einmal am Tag gemacht

		if (slidergroesse_g[index] == 0)
			Tracer.WriteTrace(10,
					"Error:Es wurde vergessen die Slidergroesse zu setzten sliderindex=<"
							+ index + ">");

		Sliderbewert slb = new Sliderbewert();
		// Hinweise zu den Mengen:
		// usermengeSlider20=menge von usern im slider
		// usermenge3Monate=menge von usern in den 3 Monaten vor dem Slider
		// also Zeitrum -(3Monate+20tage)--> bis zum Sliderstartdatum
		HashSet<String> usermengeSlider20 = new HashSet<String>();
		// Moni moni = new Moni();
		// moni.begin("calcUsermenge3Monate");
		calcUsermenge3Monate(index, compt);
		// moni.end("calc3Monate end.........");
		// postanzahl setzen
		int sliderpostings = sliderliste[index].size();
		slb.setPostanz(sliderpostings);

		// usermenge 20 aufbauen
		for (int i = 0; i < sliderpostings; i++)
		{
			PostSliderElem pe = sliderliste[index].get(i);
			if (pe.udbo_glob == null)
				continue;

			String unam = pe.udbo_glob.get_username();
			usermengeSlider20.add(unam);
		}
		slb.setUseranz(usermengeSlider20.size());

		// ermittle anz. gute-schlechte Postings
		int gutepostings = 0, schlechtepostings = 0;
		int slideruseranz = sliderliste[index].size();
		float sumra=0;
		for (int i = 0; i < slideruseranz; i++)
		{
			// ermittel für alle user den durchschnittlichen rank
			PostSliderElem se = sliderliste[index].get(i);
			int rank = se.getRank();
			sumra=sumra+(float)rank;
			if (RangObj.isGuterUser(rank))
				gutepostings++;
			else if (RangObj.isSchlechterUser(rank))
				schlechtepostings++;

		}
		
		if(slideruseranz>0)
			slb.setMitlrank(sumra/slideruseranz);

		slb.setAnzgutePostings(gutepostings);
		slb.setAnzschlechtePostings(schlechtepostings);

		// zaehlt: gute, schlechte und bad-user im Slider
		int gute = 0, schlechte = 0, bad = 0, neuegute = 0, neueschlechte = 0, neuebad = 0;
		for (int i = 0; i < slideruseranz; i++)
		{
			// ermittel für alle user den durchschnittlichen rank
			PostSliderElem se = sliderliste[index].get(i);
			if (se.udbo_glob == null)
				continue;
			String unam = se.udbo_glob.get_username();

			// schaut nach ob der user schon gezaehlt wurde
			if (usermengeSlider20.contains(unam) == true)
			{
				int rank = se.getRank();
				if (RangObj.isGuterUser(rank))
					gute++;
				else if (RangObj.isSchlechterUser(rank))
					schlechte++;
				else if (RangObj.isBadUser(rank))
					bad++;

				// schaut nach ob der user neu ist
				// user ist neu wenn er im 20Tage slider ist, aber nicht im
				// 90Tageslider
				if (slidermenge90.contains(unam) == false)
				{
					int r = se.getRank();
					ProtokolliereNeuenUserImSlider(tid_glob, compt, unam, r,
							pdb);

					if (RangObj.isGuterUser(r))
						neuegute++;
					else if (RangObj.isSchlechterUser(rank))
						neueschlechte++;
					else if (RangObj.isBadUser(rank))
						neuebad++;
				}

				usermengeSlider20.remove(unam);

				// Falls der Slider20 ausgewertet wurde, wird beendet
				if (usermengeSlider20.isEmpty() == true)
					break;
			}

		}
		slb.setGuteU(gute);
		slb.setSchlechteU(schlechte);
		slb.setAnzbaduser(bad);
		slb.setNeueGuteUserImSlider(neuegute);
		slb.setNeueSchlechteUserImSlider(neueschlechte);
		slb.setNeueBaduserImSlider(neuebad);
		return slb;
	}

	public float calcSliderUebersichtGuete(Sliderbewert slb, int sliderindex,
			float maxfaktor, float minfaktor, float gefaelle, int minguteuser)
	{
		// allgemeiner Vertrauensfaktor, dient in erster Linie die
		// Sliderübersicht zu sortieren
		float vertrauensfaktor = 0;
		float durchschnittrank = calcMittlererRank(sliderindex);
		float maxanzahluser = udb_glob.GetanzObj();

		// es müssen mindestens 3 postings in dem 20tage slider sein
		if (slb.getPostanz() < 3)
			return -9999999999f;

		vertrauensfaktor = (-(maxfaktor / (maxanzahluser / gefaelle)) * durchschnittrank)
				+ (maxfaktor + minfaktor);
		return vertrauensfaktor;
	}

	public Vertrauen calcSliderVertrauensfaktor(Sliderbewert slb,
			int sliderindex, float maxfaktor, float minfaktor, float gefaelle)
	{
		// index:0 (slider 0, betrachtet anz letzten postings)
		// index:1 (slider 1, betrachtet den letzten Zeitraum, also z.B. die
		// letzten 20 Tage)
		// index selektiert den Slider, bei index=0 ist Slider 0 gemeint
		// Es wird zwischen min und maxfaktor interpolliert, je nachdem wie hoch
		// der Vertrauensfaktor in dem slidingfenster ist
		// maxfaktor:ist der Faktor der den Gewinneinsatz erhöht
		// maxfaktor sollte 50 sein
		// minfaktor sollte 0.1 sein
		// gefaelle ca.5, je groesse das gefaelle je schneller klingt das
		// vertrauen ab
		// gefälle 1 => linear, bei gefälle 5 fällt die gerade stärker

		float durchschnittrank = calcMittlererRank(sliderindex);
		int slideruseranz = sliderliste[sliderindex].size();
		float maxanzahluser = udb_glob.GetanzObj();

		float vertrauensfaktor = (-(maxfaktor / (maxanzahluser / gefaelle)) * durchschnittrank)
				+ (maxfaktor + minfaktor);
		if (vertrauensfaktor < 0)
			vertrauensfaktor = minfaktor;

		if (slideruseranz < 5)
			vertrauensfaktor = minfaktor;

		int sumrp = (int) durchschnittrank * slideruseranz;

		if (vertrauensfaktor < 0)
			Tracer.WriteTrace(10, "Error:internal vertrau<" + vertrauensfaktor
					+ "> sumrp<" + sumrp + "> slideruseranz<" + slideruseranz
					+ "> druchrank<" + durchschnittrank + "> minfaktor<"
					+ minfaktor + "> maxfaktor<" + maxfaktor + "> gefaelle<"
					+ gefaelle + ">");

		Vertrauen vertrau = new Vertrauen(vertrauensfaktor, sumrp,
				slideruseranz, durchschnittrank, minfaktor, maxfaktor,
				gefaelle, 0, 0);

		return vertrau;
	}

	public Vertrauen calcSliderVertrauensfaktor2(Sliderbewert slb,
			int sliderindex, float maxfaktor, float minfaktor, float gefaelle)
	{
		float vertrauensfaktor = 0;
		float durchschnittrank = calcMittlererRank(sliderindex);
		int slideruseranz = sliderliste[sliderindex].size();

		int anzPostingsGuteUser = slb.getAnzgutePostings();
		int anzPostingsSchlechteUser = slb.getAnzschlechtePostings();

		float maxanzahluser = udb_glob.GetanzObj();

		if (anzPostingsGuteUser < 5)
		{
			vertrauensfaktor = 0;
		}

		else if (anzPostingsGuteUser < anzPostingsSchlechteUser)
		{
			vertrauensfaktor = 0;
		} else if (slideruseranz < 5)
		{
			vertrauensfaktor = 0;
		} else
		{
			vertrauensfaktor = (-(maxfaktor / (maxanzahluser / gefaelle)) * durchschnittrank)
					+ (maxfaktor + minfaktor);
			if (vertrauensfaktor < 0)
				vertrauensfaktor = 0;
		}

		int sumrp = (int) durchschnittrank * slideruseranz;
		Vertrauen vertrau = new Vertrauen(vertrauensfaktor, sumrp,
				slideruseranz, durchschnittrank, minfaktor, maxfaktor,
				gefaelle, anzPostingsGuteUser, anzPostingsSchlechteUser);

		if (vertrauensfaktor < 0)
			Tracer.WriteTrace(10, "Error:internal vertrau<" + vertrauensfaktor
					+ "> sumrp<" + sumrp + "> slideruseranz<" + slideruseranz
					+ "> druchrank<" + durchschnittrank + "> minfaktor<"
					+ minfaktor + "> maxfaktor<" + maxfaktor + "> gefaelle<"
					+ gefaelle + ">");

		return vertrau;
	}

	public Vertrauen calcSliderVertrauensfaktor3(Sliderbewert slb,
			int sliderindex, float maxfaktor, float minfaktor, float gefaelle,
			int minguteuser)
	{
		float vertrauensfaktor = 0;
		float durchschnittrank = calcMittlererRank(sliderindex);
		int slideruseranz = sliderliste[sliderindex].size();

		int anzPostingsGuteUser = slb.getAnzgutePostings();
		int anzPostingsSchlechteUser = slb.getAnzschlechtePostings();

		float maxanzahluser = udb_glob.GetanzObj();

		if (anzPostingsGuteUser < minguteuser)
		{
			vertrauensfaktor = 0;
		}

		else if (anzPostingsGuteUser < anzPostingsSchlechteUser)
		{
			vertrauensfaktor = 0;
		} else if (slideruseranz < 5)
		{
			vertrauensfaktor = 0;
		} else
		{
			vertrauensfaktor = (-(maxfaktor / (maxanzahluser / gefaelle)) * durchschnittrank)
					+ (maxfaktor + minfaktor);
			if (vertrauensfaktor < 0)
				vertrauensfaktor = 0;
		}

		int sumrp = (int) durchschnittrank * slideruseranz;
		Vertrauen vertrau = new Vertrauen(vertrauensfaktor, sumrp,
				slideruseranz, durchschnittrank, minfaktor, maxfaktor,
				gefaelle, anzPostingsGuteUser, anzPostingsSchlechteUser);

		if (vertrauensfaktor < 0)
			Tracer.WriteTrace(10, "Error:internal vertrau<" + vertrauensfaktor
					+ "> sumrp<" + sumrp + "> slideruseranz<" + slideruseranz
					+ "> druchrank<" + durchschnittrank + "> minfaktor<"
					+ minfaktor + "> maxfaktor<" + maxfaktor + "> gefaelle<"
					+ gefaelle + ">");

		return vertrau;
	}

	public Vertrauen calcSliderVertrauensfaktor4(Sliderbewert slb,
			int sliderindex, float maxfaktor, float minfaktor, float gefaelle,
			int minguteuser)
	{
		float vertrauensfaktor = 0;
		float durchschnittrank = calcMittlererRank(sliderindex);
		int sliderpostingsanz = sliderliste[sliderindex].size();
		// Inf inf = new Inf();
		// inf.setFilename(GC.rootpath +
		// "\\db\\reporting\\vertrauensfaktor4.csv");

		float maxanzahluser = udb_glob.GetanzObj();

		if ((slb.getGuteU() * 2) < slb.getSchlechteU())
			vertrauensfaktor = 0;
		else if (slb.getGuteU() < minguteuser)
		{
			vertrauensfaktor = 0;
		}

		else if (slb.getAnzgutePostings() < slb.getAnzschlechtePostings())
		{
			vertrauensfaktor = 0;

		}
		// bei zu wenig Postings oder zu wenigen usern ist auch nix los
		else if ((sliderpostingsanz < 5) || (slb.getUseranz() < 5))
		{
			vertrauensfaktor = 0;
		} else
		{
			vertrauensfaktor = (-(maxfaktor / (maxanzahluser / gefaelle)) * durchschnittrank)
					+ (maxfaktor + minfaktor);
			/*
			 * inf.writezeile("+Postings<" + slb.getAnzgutePostings() +
			 * ">; -Postings<" + slb.getAnzschlechtePostings() + ">; anzUser<" +
			 * slb.getPostanz() + ">; guteU<" + slb.getGuteU() + ">; schlU<" +
			 * slb.getSchlechteU() + ">; maxfaktor<" + maxfaktor +
			 * ">; gefaelle<" + gefaelle + ">; durchrank<" + durchschnittrank +
			 * ">; maxfaktor<" + maxfaktor + ">; vertrauensfaktor<" +
			 * vertrauensfaktor + ">");
			 */
			if (vertrauensfaktor < 0)
				vertrauensfaktor = 0;
		}

		int sumrp = (int) durchschnittrank * sliderpostingsanz;
		Vertrauen vertrau = new Vertrauen(vertrauensfaktor, sumrp,
				sliderpostingsanz, durchschnittrank, minfaktor, maxfaktor,
				gefaelle, slb.getAnzgutePostings(), slb
						.getAnzschlechtePostings());

		if (vertrauensfaktor < 0)
			Tracer.WriteTrace(10, "Error:internal vertrau<" + vertrauensfaktor
					+ "> sumrp<" + sumrp + "> sliderpostingsanz<"
					+ sliderpostingsanz + "> druchrank<" + durchschnittrank
					+ "> minfaktor<" + minfaktor + "> maxfaktor<" + maxfaktor
					+ "> gefaelle<" + gefaelle + ">");

		return vertrau;
	}

	public Vertrauen calcSliderVertrauensfaktor5(Sliderbewert slb,
			int sliderindex, float maxfaktor, float minfaktor, float gefaelle,
			int minguteuser)
	{
		float vertrauensfaktor = 0;
		float durchschnittrank = calcMittlererRank(sliderindex);
		int sliderpostingsanz = sliderliste[sliderindex].size();
		Inf inf = new Inf();
		inf.setFilename(GC.rootpath + "\\db\\reporting\\vertrauensfaktor5.csv");

		float maxanzahluser = udb_glob.GetanzObj();

		if ((slb.getGuteU()) < slb.getSchlechteU())
			vertrauensfaktor = 0;
		else if (slb.getGuteU() < minguteuser)
		{
			vertrauensfaktor = 0;
		}

		else if (slb.getAnzgutePostings() < slb.getAnzschlechtePostings())
		{
			vertrauensfaktor = 0;

		} else if (slb.getNeueGuteUserImSlider() < slb
				.getNeueSchlechteUserImSlider())
			vertrauensfaktor = 0;
		// bei zu wenig Postings oder zu wenigen usern ist auch nix los
		else if ((sliderpostingsanz < 5) || (slb.getUseranz() < 5))
		{
			vertrauensfaktor = 0;
		} else
		{
			vertrauensfaktor = (-(maxfaktor / (maxanzahluser / gefaelle)) * durchschnittrank)
					+ (maxfaktor + minfaktor);
			inf.writezeile("+Postings<" + slb.getAnzgutePostings()
					+ ">; -Postings<" + slb.getAnzschlechtePostings()
					+ ">; anzUser<" + slb.getPostanz() + ">; guteU<"
					+ slb.getGuteU() + ">; schlU<" + slb.getSchlechteU()
					+ ">; maxfaktor<" + maxfaktor + ">; gefaelle<" + gefaelle
					+ ">; durchrank<" + durchschnittrank + ">; maxfaktor<"
					+ maxfaktor + ">; vertrauensfaktor<" + vertrauensfaktor
					+ ">");
			if (vertrauensfaktor < 0)
				vertrauensfaktor = 0;
		}

		int sumrp = (int) durchschnittrank * sliderpostingsanz;
		Vertrauen vertrau = new Vertrauen(vertrauensfaktor, sumrp,
				sliderpostingsanz, durchschnittrank, minfaktor, maxfaktor,
				gefaelle, slb.getAnzgutePostings(), slb
						.getAnzschlechtePostings());

		if (vertrauensfaktor < 0)
			Tracer.WriteTrace(10, "Error:internal vertrau<" + vertrauensfaktor
					+ "> sumrp<" + sumrp + "> sliderpostingsanz<"
					+ sliderpostingsanz + "> druchrank<" + durchschnittrank
					+ "> minfaktor<" + minfaktor + "> maxfaktor<" + maxfaktor
					+ "> gefaelle<" + gefaelle + ">");

		return vertrau;
	}

	public Vertrauen calcSliderVertrauensfaktorMasteruserxxx(
			float gutUfaktor, float gutPfaktor, Sliderbewert slb,
			int sliderindex, float maxfaktor, float gefaelle, int minguteuser,
			int minaktivitaet)
	{
		float vertrauensfaktor = 0;
		float durchschnittrank = calcMittlererRank(sliderindex);
		int sliderpostingsanz = sliderliste[sliderindex].size();
		String handelsabbruch="fehler";
		Inf inf = new Inf();
		inf.setFilename(GC.rootpath + "\\db\\reporting\\vertrauensfaktor4.csv");

		float maxanzahluser = udb_glob.GetanzObj();

		if ((slb.getGuteU() * gutUfaktor) < slb.getSchlechteU())
		{
			handelsabbruch=new String("weil guteU("+slb.getGuteU() * gutUfaktor+")"+"< schlU("+slb.getSchlechteU()+")");
			vertrauensfaktor = 0;
		}
		else if (slb.getGuteU() < minguteuser)
		{
			handelsabbruch=new String("weil guteU("+slb.getGuteU() +")"+"< ("+minguteuser+")");
			vertrauensfaktor = 0;
		}

		else if (slb.getAnzgutePostings() * gutPfaktor < slb
				.getAnzschlechtePostings())
		{
			handelsabbruch="weil guteP("+slb.getAnzgutePostings() * gutPfaktor+")<schlP("+slb
			.getAnzschlechtePostings()+")";
			vertrauensfaktor = 0;

		}
		// bei zu wenig Postings oder zu wenigen usern ist auch nix los
		else if ((sliderpostingsanz < minaktivitaet)
				|| (slb.getUseranz() < minaktivitaet))
		{
			if((sliderpostingsanz < minaktivitaet))
				handelsabbruch="zu wenig postings min="+minaktivitaet+"";
			else
				handelsabbruch="zu wenig user min="+minaktivitaet+"";
			
			vertrauensfaktor = 0;
		} else
		{
			vertrauensfaktor = (-(maxfaktor / (maxanzahluser / gefaelle)) * durchschnittrank)
					+ (maxfaktor + 0.1f);
			inf.writezeile("+Postings<" + slb.getAnzgutePostings()
					+ ">; -Postings<" + slb.getAnzschlechtePostings()
					+ ">; anzUser<" + slb.getPostanz() + ">; guteU<"
					+ slb.getGuteU() + ">; schlU<" + slb.getSchlechteU()
					+ ">; maxfaktor<" + maxfaktor + ">; gefaelle<" + gefaelle
					+ ">; durchrank<" + durchschnittrank + ">; maxfaktor<"
					+ maxfaktor + ">; vertrauensfaktor<" + vertrauensfaktor
					+ ">");
			if (vertrauensfaktor < 0)
			{
				handelsabbruch="neg. vertrauensfaktor("+vertrauensfaktor+")";
				vertrauensfaktor = 0;
			}
		}

		int sumrp = (int) durchschnittrank * sliderpostingsanz;
		Vertrauen vertrau = new Vertrauen(vertrauensfaktor, sumrp,
				sliderpostingsanz, durchschnittrank, 0.1f, maxfaktor, gefaelle,
				slb.getAnzgutePostings(), slb.getAnzschlechtePostings());

		if(vertrauensfaktor>0)
			slb.setHandelshinweis(new String("Kaufe fuer <"+vertrauensfaktor*100+">Euro"));
		else
			slb.setHandelshinweis(new String(handelsabbruch));
		
		if (vertrauensfaktor < 0)
			Tracer.WriteTrace(10, "Error:internal vertrau<" + vertrauensfaktor
					+ "> sumrp<" + sumrp + "> sliderpostingsanz<"
					+ sliderpostingsanz + "> druchrank<" + durchschnittrank
					+ "> minfaktor<" + 0.1f + "> maxfaktor<" + maxfaktor
					+ "> gefaelle<" + gefaelle + ">");

		return vertrau;
	}

	public Vertrauen calcSliderVertrauensfaktor2bad(Sliderbewert slb,
			int sliderindex, float maxfaktor, float minfaktor, float gefaelle)
	{
		// dieser Vertrauensfaktor ist für den BAD user
		float vertrauensfaktor = 0;
		float durchschnittrank = calcMittlererRank(sliderindex);
		int slideruseranz = sliderliste[sliderindex].size();

		int anzPostingsGuteUser = slb.getAnzgutePostings();
		int anzPostingsSchlechteUser = slb.getAnzschlechtePostings();

		float maxanzahluser = udb_glob.GetanzObj();

		if (anzPostingsGuteUser > anzPostingsSchlechteUser)
		{
			vertrauensfaktor = 0;
		} else if (slideruseranz < 5)
		{
			vertrauensfaktor = 0;
		} else
		{
			vertrauensfaktor = (-(maxfaktor / (maxanzahluser / gefaelle)) * durchschnittrank)
					+ (maxfaktor + minfaktor);
			if (vertrauensfaktor < 0)
				vertrauensfaktor = 0;
		}

		int sumrp = (int) durchschnittrank * slideruseranz;
		Vertrauen vertrau = new Vertrauen(vertrauensfaktor, sumrp,
				slideruseranz, durchschnittrank, minfaktor, maxfaktor,
				gefaelle, anzPostingsGuteUser, anzPostingsSchlechteUser);

		if (vertrauensfaktor < 0)
			Tracer.WriteTrace(10, "Error:internal vertrau<" + vertrauensfaktor
					+ "> sumrp<" + sumrp + "> slideruseranz<" + slideruseranz
					+ "> druchrank<" + durchschnittrank + "> minfaktor<"
					+ minfaktor + "> maxfaktor<" + maxfaktor + "> gefaelle<"
					+ gefaelle + ">");

		return vertrau;
	}

	public float holeAttribSlider20TageAnzahl()
	{
		// je mehr postings in slider index=1 sind desto grösser das interesse
		// in index 1 sind sämtliche postings die nicht älter als 20 Tage sind
		// sind dort viele drin sind in letzter Zeit hohe posting aktivitäten in
		// dem thread

		// mit dem index 1 wird in dem slider ein Zeitraum von x Tagen
		// betrachtet.
		// je voller der Slider desto mehr Interesse der User an der Aktie
		return sliderliste[1].size();
	}

}
