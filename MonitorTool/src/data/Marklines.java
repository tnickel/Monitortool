package data;

import hiflsklasse.Tracer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import org.eclipse.swt.widgets.Text;

public class Marklines
{
	// diese struktur übernimmt die farbverarbeitung von zwei tradelisten
	// Es wird zuerst der farbbereich festgestellt wo sich die beiden
	// tradelisten zeitraummässig überschneiden
	// => dies wird mit grau dargestellt farbe==1
	// Wenn die Trades sowohl in Liste 1 also auch Liste2 vorkommen, dann werden
	// sie grün dargestellt

	private Tradeliste tl_links_glob = null;
	private Tradeliste tl_rechts_glob = null;
	private ArrayList<Integer> farbliste_links_glob = new ArrayList<Integer>();
	private ArrayList<Integer> farbliste_rechts_glob = new ArrayList<Integer>();

	public Marklines()
	{
	}

	/*
	 * get farbmarkierung für die tradeposition 0=keine farbe (keine
	 * bereichsüberschneidung 1=grau (die bereiche überschneiden sich) 2=grün
	 * (bereiche überschneiden sich und trade kommt auch vor) Beim init wird die
	 * Farbstruktur und farbmengen angelegt
	 */
	public void initMark(Tradeliste tl_links, Tradeliste tl_rechts, int gmtdiff,int minutentolleranz,Text prozgleich)
	{
		//prozgleich: gibt die prozentuale Übereinstimmung an
		//also verhältniss von grün/grau
		if ((tl_links == null) || (tl_rechts == null))
			return;

		tl_links_glob = tl_links;
		tl_rechts_glob = tl_rechts;

		// hier wird die farbe für eine bestimmte position zurückgeliefert

		// hier werden erst mal ein paar vorarbeiten gemacht
		tl_links_glob.calcMinMaxdateOpentime();
		Date mindate_links = tl_links_glob.getMindate();
		Date maxdate_links = tl_links_glob.getMaxdate();
		Tracer.WriteTrace(20, "I:links mindate<"+mindate_links.toGMTString()+"> maxdate<"+maxdate_links.toGMTString()+">");
		tl_rechts_glob.calcMinMaxdateOpentime();
		Date mindate_rechts = tl_rechts_glob.getMindate();
		Date maxdate_rechts = tl_rechts_glob.getMaxdate();
		Tracer.WriteTrace(20, "I:rechts mindate<"+mindate_rechts.toGMTString()+"> maxdate<"+maxdate_rechts.toGMTString()+">");

		//bestimmte Zeitbereich auf tagesbasis und markiere grau
		genMarkGrey(tl_links_glob, farbliste_links_glob, mindate_rechts,
				maxdate_rechts);
		genMarkGrey(tl_rechts_glob, farbliste_rechts_glob, mindate_links,
				maxdate_links);

		//bestimme Zeitbereich auf Minutenbais und markiere gruen wenn gleich
		markGreen(tl_links_glob, farbliste_links_glob, tl_rechts_glob, gmtdiff,minutentolleranz);
		markGreen(tl_rechts_glob, farbliste_rechts_glob, tl_links_glob, gmtdiff,minutentolleranz);

		int pgleich=calcUebereinstimmung();
		prozgleich.setText(String.valueOf(pgleich));
		return;
	}

	public int calcUebereinstimmung()
	//berechnet das verhältniss von grün zur graufläche
	{
		int anz= farbliste_links_glob.size();
		float graycounter=0;
		float greencounter=0;
		
		for(int i=0; i<anz; i++)
		{
			int farbe=farbliste_links_glob.get(i);
			if(farbe==1)
				graycounter++;
			else if(farbe==2)
				greencounter++;
		}
		if(greencounter+graycounter==0)
			return 0;
		
		float ret=greencounter/(greencounter+graycounter)*100;
		
		return ((int)ret);
		
	}
	
	
	public int getFarbe(int sideflag, int pos)
	{
		// sideflag gibt linke oder rechte Seite an
		// 0=rechte seite
		// 1=linke seite
		// pos ist die position für die ich die Farbe brauche
		// holt die farbe aus dem array

		if ((tl_links_glob == null) || (tl_rechts_glob == null))
			return 0;

		if (sideflag == 0)
			return farbliste_links_glob.get(pos);
		else if (sideflag == 1)
			return farbliste_rechts_glob.get(pos);
		else
			Tracer.WriteTrace(10, "E:internal Error dfdf");
		return (-1);
	}

	private void genMarkGrey(Tradeliste tl, ArrayList<Integer> farbliste,
			Date mindate_andere, Date maxdate_andere)
	{
		// betrachte jedes element dieser liste ob es zwischen min und max-date
		// der anderen liste ist
		// wenn es so ist wird dieses element grau markiert

		if (mindate_andere == null)
			Tracer.WriteTrace(10, "I:mindate andere==null");
		if (maxdate_andere == null)
			Tracer.WriteTrace(10, "I:maxdate_andere==null");

		if (tl == null)
		{
			Tracer.WriteTrace(20, "I:tl==null");
			return;
		}

		farbliste.clear();

		int anz = tl.getsize();
		for (int i = 0; i < anz; i++)
		{
			Trade tr = tl.getelem(i);
			if (tr == null)
			{
				Tracer.WriteTrace(20, "I:Trace nr<" + i + "> ==null");
				continue;
			}
			Date d = tr.getOpentimeDate();
			// falls der Trade im bereich der schranken der anderen seite ist
			if ((d.after(mindate_andere) == true)
					&& (d.before(maxdate_andere) == true))
			{
				// markiere Trade gray
				farbliste.add(i, 1);
			} else if ( (d.compareTo(mindate_andere) == 0)
					|| (d.compareTo(maxdate_andere) == 0))
			{
				// falls trades an der schranke dann markiere
				// markiere Trade gray
				farbliste.add(i, 1);
			} else
			{
				// keine Markierung
				farbliste.add(i, 0);
				System.out.println("else");
			}
		}
	}

	private void markGreen(Tradeliste tl1, ArrayList<Integer> farbliste,
			Tradeliste tl2, int gmtdiff,int minutentolleranz)
	{
		// jeder Trade der grey markierten liste(tl1) wird überprüft ob er in
		// der Tradeliste (tl2) drin ist.
		// jeder Trade der grey markiert ist und in der tl2 ist wird grün
		// markiert
		// um die sache zu vereinfachen wird nicht auf sekunden überprüft
		// sondern auf Minuten auf gerundet.
		// für tl2 wird eine Menge von Zeiten gebildet und dann jedes element
		// von tl1 überprüft ob es in der tl2-Menge ist

		// ermittle die Zeitenmenge für die zweite liste
		HashSet<Date> openzeitenmenge = tl2.calcTradezeitenmenge(gmtdiff,1,minutentolleranz);
		HashSet<Date> closezeitenmenge = tl2.calcTradezeitenmenge(gmtdiff,0,minutentolleranz);
		
		// jetzt wird überprüft ob jeder trade der grau ist aus der ersten liste
		// in der zweiten vorkommt
		int anz = tl1.getsize();
		for (int i = 0; i < anz; i++)
		{
			// falls grau markiert
			if (farbliste.get(i) == 1)
			{
				Trade tr = tl1.getelem(i);
				Date opentime = tr.getOpentimeDate();
				Date closetime = tr.getClosetimeDate();

				//minuten auf 0 setzen
				Calendar cal = Calendar.getInstance();
				cal.setTime(opentime);
				cal.set(Calendar.SECOND, 0);
				opentime=cal.getTime();

				cal = Calendar.getInstance();
				cal.setTime(closetime);
				cal.set(Calendar.SECOND, 0);
				closetime=cal.getTime();
				
				// es wird ein trade aus liste1 überprüft ob er in der Zweiten
				// menge ist
				// falls ja dann markiere grün
				if ((openzeitenmenge.contains(opentime) == true) && (closezeitenmenge.contains(closetime)==true))
				{
					farbliste.set(i, 2);
				}
			}
		}
	}
}
