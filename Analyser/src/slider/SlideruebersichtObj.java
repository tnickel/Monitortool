package slider;

import hilfsklasse.SG;
import hilfsklasse.Tools;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;
import interfaces.DBObject;
import objects.BadObjectException;
import objects.Obj;
import objects.ThreadDbObj;

public class SlideruebersichtObj extends Obj implements DBObject,
		Comparable<SlideruebersichtObj>
{
	private int threadid = 0;
	private int wochenindex = 0;
	private String aktname = null;
	private int postanzahl = 0;
	private float mitlrank = 0;
	private int guteU = 0;
	private int schlechteU = 0;
	private int guteP = 0;
	private int schlechteP = 0;
	// nach der Sliderguete wird sortiert
	private float sliderguete = 0;
	private String lastupdate = null;
	private int useranzahl = 0;
	private int baduseranzahl = 0;
	private int neueguteU = 0;
	private int neueschlechteU = 0;
	private int neuebadU = 0;
	private int hotflag = 0;
	private String symb = null;
	private String handelshinweis = "";
	// alter in tagen seit dem letzten posting
	private int lastpostdatealter = 0;

	// hotpoints sind die punkte die es als bonus gibt wenn was rot eingefärbt
	// ist
	private int hotpoints = 0;

	public SlideruebersichtObj()
	{
	}

	public SlideruebersichtObj(String zeile) throws BadObjectException
	{
		int anz = SG.countZeichen(zeile, "#");
		if ((anz < 17) || (anz > 18))
			Tracer.WriteTrace(10, "Error: internal Sliderobj <" + zeile
					+ "> anz<" + anz + ">");
		int ind = 1;
		try
		{
			threadid = Integer.valueOf(SG.nteilstring(zeile, "#", ind));
			ind++;
			wochenindex = Integer.valueOf(SG.nteilstring(zeile, "#", ind));
			ind++;
			aktname = new String(SG.nteilstring(zeile, "#", ind));
			aktname = SG.convFilename(aktname);
			ind++;
			postanzahl = Integer.valueOf(SG.nteilstring(zeile, "#", ind));
			ind++;
			mitlrank = Float.valueOf(SG.nteilstring(zeile, "#", ind));
			ind++;
			// dies ist die postinganzahl
			guteP = Integer.valueOf(SG.nteilstring(zeile, "#", ind));
			ind++;
			schlechteP = Integer.valueOf(SG.nteilstring(zeile, "#", ind));
			ind++;
			guteU = Integer.valueOf(SG.nteilstring(zeile, "#", ind));
			ind++;
			schlechteU = Integer.valueOf(SG.nteilstring(zeile, "#", ind));
			ind++;

			// hier die anzahl der verschiedenen User
			useranzahl = Integer.valueOf(SG.nteilstring(zeile, "#", ind));
			ind++;
			baduseranzahl = Integer.valueOf(SG.nteilstring(zeile, "#", ind));
			ind++;
			neueguteU = Integer.valueOf(SG.nteilstring(zeile, "#", ind));
			ind++;
			neueschlechteU = Integer.valueOf(SG.nteilstring(zeile, "#", ind));
			ind++;
			neuebadU = Integer.valueOf(SG.nteilstring(zeile, "#", ind));
			ind++;
			hotflag = Integer.valueOf(SG.nteilstring(zeile, "#", ind));
			ind++;
			sliderguete = Float.valueOf(SG.nteilstring(zeile, "#", ind));
			ind++;
			lastupdate = new String(SG.nteilstring(zeile, "#", ind));
			ind++;
			symb = new String(SG.nteilstring(zeile, "#", ind));
			if (anz == 18)
			{
				ind++;
				handelshinweis = new String(SG.nteilstring(zeile, "#", ind));
			}
		} catch (ToolsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int compareTo(SlideruebersichtObj argument)
	{
		int offset = 0;

		//sortierkriterien für die HTML-Übersicht
		
		// erstes Sortierkriterium ist die Sliderguete
		if (this.getSliderguete() != argument.getSliderguete())
		{
			if (this.getSliderguete() < argument.getSliderguete())
				return 1;
			else
				return -1;
		}
		// zweites Sortierkriterum ist das hotflag
		if (this.getHotflag() != argument.getHotflag())
		{
			if (this.getHotflag() != argument.getHotflag())
				return 1;
			else
				return -1;
		}
		// drittes Sortierriterium ist die postanzahl
		if (this.getPostanzahl() < argument.getPostanzahl())
			return 1;
		else
			return -1;

	}

	@Override
	public String GetSaveInfostring()
	{
		return "threadid#wochenindex#aktname#postanzahl#mitlrank#guteP#schlechteP#guteU#schlechteU#useranzahl#baduseranzahl#neuegute#neueschlecht#neuebad#hotflag#sliderguete#lastupdate#symb#handelshinweis";
	}

	private String calcInfStr(String T)
	{
		if (T.contains("#"))
			return (threadid + T + wochenindex + T + aktname + T + postanzahl
					+ T + mitlrank + T + guteP + T + schlechteP + T + guteU + T
					+ schlechteU + T + useranzahl + T + baduseranzahl + T
					+ neueguteU + T + neueschlechteU + T + neuebadU + T
					+ hotflag + T + sliderguete + T + lastupdate + T + symb + T + handelshinweis);
		else
			return (threadid + T + wochenindex + T + aktname + T
					+ postanzahl + T + (int) mitlrank + T + guteP + T
					+ schlechteP + T + guteU + T + schlechteU + T + useranzahl
					+ T + baduseranzahl + T + neueguteU + T + neueschlechteU
					+ T + neuebadU + T + hotflag + T + sliderguete + T
					+ lastupdate + T + symb + T + handelshinweis);
	}

	@Override
	public String toString()
	{
		String ret = calcInfStr("#");
		return (ret);
	}

	public String getExcelString()
	{
		String ret = calcInfStr(";");
		return (ret);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SlideruebersichtObj other = (SlideruebersichtObj) obj;
		if (threadid != other.threadid)
			return false;
		if (wochenindex != other.wochenindex)
			return false;
		return true;
	}

	public int getThreadid()
	{
		return threadid;
	}

	public int getWochenindex()
	{
		return wochenindex;
	}

	public void setWochenindex(int wochenindex)
	{
		this.wochenindex = wochenindex;
	}

	public String getAktname()
	{
		return aktname;
	}

	public void setAktname(String aktname)
	{
		this.aktname = aktname;
	}

	public int getPostanzahl()
	{
		return postanzahl;
	}

	public void setPostanzahl(int postanz)
	{
		this.postanzahl = postanz;
	}

	public float getMitlrank()
	{
		return mitlrank;
	}

	public void setMitlrank(float mitlrank)
	{
		this.mitlrank = mitlrank;
	}

	public String getLastupdate()
	{
		// die gleiche lastupdatetime steht in threadsdb
		return lastupdate;
	}

	public float getSliderguete()
	{
		return sliderguete;
	}

	public void setSliderguete(float sliderguete)
	{
		this.sliderguete = sliderguete;
	}

	public int getGuteU()
	{
		return guteU;
	}

	public void setGuteU(int guteU)
	{
		this.guteU = guteU;
	}

	public int getSchlechteU()
	{
		return schlechteU;
	}

	public void setSchlechteU(int schlechteU)
	{
		this.schlechteU = schlechteU;
	}

	public int getGuteP()
	{
		return guteP;
	}

	public void setGuteP(int guteP)
	{
		this.guteP = guteP;
	}

	public int getSchlechteP()
	{
		return schlechteP;
	}

	public void setSchlechteP(int schlechteP)
	{
		this.schlechteP = schlechteP;
	}

	public void setLastupdate(String lastupdate)
	{
		// die gleiche lastupdatetime steht in threadsdb
		this.lastupdate = lastupdate;
	}

	public void setThreadid(int threadid)
	{
		this.threadid = threadid;
	}

	public int getUseranzahl()
	{
		return useranzahl;
	}

	public void setUseranzahl(int useranzahl)
	{
		this.useranzahl = useranzahl;
	}

	public int getBaduseranzahl()
	{
		return baduseranzahl;
	}

	public void setBaduseranzahl(int baduseranzahl)
	{
		this.baduseranzahl = baduseranzahl;
	}

	public int getNeueguteU()
	{
		return neueguteU;
	}

	public void setNeueguteU(int neueguteU)
	{
		this.neueguteU = neueguteU;
	}

	public int getNeueschlechteU()
	{
		return neueschlechteU;
	}

	public void setNeueschlechteU(int neueschlechteU)
	{
		this.neueschlechteU = neueschlechteU;
	}

	public int getNeuebadU()
	{
		return neuebadU;
	}

	public void setNeuebadU(int neuebadU)
	{
		this.neuebadU = neuebadU;
	}

	public int getHotflag()
	{
		return hotflag;
	}

	public void setHotflag(int hotflag)
	{
		this.hotflag = hotflag;
	}

	public int getHotpoints()
	{
		return hotpoints;
	}

	public void setHotpoints(int hotpoints)
	{
		this.hotpoints = hotpoints;
	}

	public String getSymb()
	{
		return symb;
	}

	public void setSymb(String symb)
	{
		this.symb = symb;
	}

	public int getLastpostdatealter()
	{
		return lastpostdatealter;
	}

	public void setLastpostdatealter(int lastpostdatealter)
	{
		this.lastpostdatealter = lastpostdatealter;
	}

	public String getHandelshinweis()
	{
		return handelshinweis;
	}

	public void setHandelshinweis(String handelshinweis)
	{
		this.handelshinweis = handelshinweis;
	}

	public int calPrio(ThreadDbObj tdbo, int alterflag)
	{
		// falls alterflag =1 wird die prio auf 8 gesetzt wenn der thread zu alt
		// ist

		int useranz = this.getPostanzahl();

		if (alterflag == 1)
		{
			// alte threads wo 180 Tage nicht mehr gepostet wurde haben pio 8
			String aktdat = Tools.entferneZeit(Tools.get_aktdatetime_str());
			String dat = Tools.entferneZeit(tdbo.getLastThreadPosttime());
			if (Tools.isDate(dat) == true)
				if (Tools.zeitdifferenz_tage(aktdat, dat) > 180)
					return 8;
		}
		// mit dem hotflag kann alles zu prio 1 erklärt werden
		// prio 1
		if ((useranz >= 1000) || (this.getHotflag() == 1))
			return 1;
		// prio 2
		if ((useranz < 1000) && (useranz >= 100))
			return 2;
		if ((useranz < 100) && (useranz >= 50))
			return 3;
		if ((useranz < 50) && (useranz >= 40))
			return 4;
		if ((useranz < 40) && (useranz >= 30))
			return 5;
		if ((useranz < 30) && (useranz >= 20))
			return 6;
		if ((useranz < 20) && (useranz >= 10))
			return 7;
		if ((useranz < 10) && (useranz >= 5))
			return 8;
		else
			return 9;

	}
	public void postprocess()
	{}
}
