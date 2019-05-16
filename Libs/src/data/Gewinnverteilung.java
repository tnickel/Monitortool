package data;

import hiflsklasse.Tracer;

public class Gewinnverteilung
{
	// das ganze fängt bei 0 an 0=1970
	// 30=2000
	// 60=2030
	private float[] jahrsum = new float[60];
	// first regcognized year
	private int first_year = 60;
	// last regcognized year
	private int last_year = 0;

	public Gewinnverteilung()
	{
	}

	public void addGewinn(int jahrdatum, float gew)
	{
		if (jahrdatum > last_year)
			last_year = jahrdatum;

		if (jahrdatum < first_year)
			first_year = jahrdatum;

		float js = jahrsum[jahrdatum];
		js = js + gew;
		jahrsum[jahrdatum] = js;

	}

	public Float getGewinn(int jahr)
	{
		float js = jahrsum[jahr];
		return (js);
	}

	public boolean checkMaxlosses(int maxlooses, float maxdd, String fname,
			boolean lasttwoyearsflag,boolean cat2yearsflag,float equityfaktor)
	{
		// hier wird geprüft wieviele Jahre im loss sind
		// true: wenn alles im grünen ist
		// maxlooses: soviel losses sind erlaubt
		// maxdd: der maximale dd der über die jahre aufgetreten ist
		// maxddfaktor: ist ist der faktor der beim maximalen dd tolleriert wird
		// lasttwoyearsflag: wenn true, dann muss die strategie in den letzten 2
		// Jahren gewinne einfahren
		// cut2yearsflag:if true, than the first und last year will be cutted
		int akt_anzlooses = 0;
		
		
		int tmp_first_year=first_year;
		int tmp_last_year=last_year;
		if(cat2yearsflag==true)
		{
			tmp_first_year=first_year+1;
			tmp_last_year=last_year-1;
		}
		
		// check first years, in den ersten jahren dürfen anz fails dabei sein
		for (int i = tmp_first_year; i <= tmp_last_year; i++)
		{
			float js = jahrsum[i];
			// falls der gewinn zu niedrig in einem der letzten 4 Jahre dann ist
			// das das aus
			if (js < maxdd*equityfaktor)
			{
				akt_anzlooses++;
			}
		}
		String msg = "maxdd=" + maxdd + " ";
		for (int i = tmp_first_year; i <= tmp_last_year; i++)
		{
			float js = jahrsum[i];
			msg = msg + " " + js + " : ";
		}

		if (akt_anzlooses > maxlooses)
		{
			// hier wurde insgesammt zu viel geloost
			Tracer.WriteTrace(20, "Debug Test2:looses: <" + fname + "> [" + msg
					+ "]");
			return false;
		}
		if (lasttwoyearsflag == true)
		{
			// prüft die letzten 2 jahre verlust sind
			int anzyears = tmp_last_year - tmp_first_year + 1;

			// falls nur 1 yar vorhanden dann ist immer true
			if (anzyears <= 1)
			{
				Tracer.WriteTrace(20, "Debug Test2: one year: ok <"+fname+">["+msg+"]");
				return true;
			} else
			{
				// prüfe die letzten 2 jahre
				if (jahrsum[tmp_last_year] < maxdd)
				{
					Tracer.WriteTrace(20, "Debug Test2: last 2 year looses  <"+fname+">["+msg+"]");
					return false;
				}
				if (jahrsum[tmp_last_year - 1] < maxdd)
				{
					Tracer.WriteTrace(20, "Debug Test2: last 2 year looses  <"+fname+">["+msg+"]");
					return false;
				}
			}

		} else
		{
			Tracer.WriteTrace(20, "Debug Test2:ok: <" + fname + "> [" + msg
					+ "]");
			return true;
		}
		Tracer.WriteTrace(20, "Debug Test2:ok: <" + fname + "> [" + msg
				+ "]");
		return true;
	}
	
}
