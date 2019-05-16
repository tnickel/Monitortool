package hilfsklasse;

import mainPackage.GC;

public class Ma
// Klasse für mathematische Grundfunktionen
{
	static public float calcKaufbetrag(float x)
	{
		// je grösser der Steigungsfaktor ist, je mehr Kaufe
		// bei 2 => 1000 für 1000 Euro
		// bei 4 => 10000 Euro
		// bei 5 => 20000 Euro....

		/*
		 * m = delta y / delta x Gerade y =d+ (m*x)
		 */
		float[] s =
		{ 2, 4, 5, 10, 40 };
		float[] kaufwert =
		{ 1000, 10000, 20000, 100000, 1000000 };

		float s1 = 0, s2 = 0, k1 = 0, k2 = 0, dx = 0, dy = 0, m = 0;
		float ret = 0;

		if (x < 2)
			return 0;

		// bestimme das Intervall
		for (int i = 0; i < (s.length) - 1; i++)
		{
			if ((x > s[i]) && (x < s[i + 1]))
			{
				s1 = s[i];
				s2 = s[i + 1];
				k1 = kaufwert[i];
				k2 = kaufwert[i + 1];
				dx = s2 - s1;
				dy = k2 - k1;
				m = dy / dx;

				ret =  (m * (x-s1))+k1;
				return ret;
			}
		}
		return 0;
	}

	static public float calcSteigungsfaktor(String dat1, String dat2, float k1,
			float k2)
	{
		Inf inf = new Inf();
		inf.setFilename(GC.rootpath + "\\db\\develop\\alpha.txt");
		int anztage = Tools.zeitdifferenz_tage(dat1, dat2);
		double alpha = Math.toDegrees(Math.atan((k2 - k1) / anztage));

		String msg = "dat1<" + dat1 + "> dat2<" + dat2 + "> anztage<" + anztage
				+ "> k1<" + k1 + "> k2<" + k2 + "> alpha<" + alpha + ">";
		inf.writezeile(msg);
		return (float) alpha;
	}

	static public float calcRankingvertrauensfaktor(int useranzahl, int ran)
	{
		// ermittelt den Vertrauensfaktor für das Ranking
		double vertrauensfaktor = 0;
		// Je höher der Rank des users => je mehr vertrauen
		// rank 1= maxFaktor 50
		// rank n= Faktor 0
		// vertrauensfaktor=-(maxfaktor/maxanzahl)*x+(maxfaktor)
		// d.h. bei rank 1 kommt faktor 50 raus
		// bei rank maxanzahl kommt faktor 0 raus
		double maxanzahl = useranzahl;
		double rank = ran;
		double gefaelle = 5; // gefälle 1 => linear, bei gefälle 5 fällt
		// die gerade stärker
		vertrauensfaktor = (-(50 / (maxanzahl / gefaelle)) * rank)
				+ 50.1;

		if (rank < 0)
			vertrauensfaktor = (float) 0.1;

		if (vertrauensfaktor < 0)
			vertrauensfaktor = (float) 0.1;

		if (vertrauensfaktor < 0)
			Tracer.WriteTrace(10, "Error:internal vertrauensfaktor<"
					+ vertrauensfaktor + "> maxanzahl<" + maxanzahl
					+ "> gefaelle<" + gefaelle + "> rank<" + rank + ">");

		/*
		 * Tracer.WriteTrace(20, "I: vertrauensfaktor<" + vertrauensfaktor +
		 * "> maxanzahl<" + maxanzahl + "> gefaelle<" + gefaelle + "> rank<" +
		 * rank + ">");
		 */
		return (float) vertrauensfaktor;
	}

	static public int interpol(int x)
	{
		int y;
		/*
		 * 0:1000000 m=500000/10=50000 10:500000 m=400000/90=4444; 100:100000
		 * m=70000/900=77; 1000:30000 m=20000/9000=2,2 10000:10000
		 */

		// m = delta y / delta x
		/*
		 * Gerade y = max - (m*x) + d
		 */

		// zwischen 0...10 kommmt als ausgabe 1000000...500000
		if ((x >= 0) && (x <= 10))
		{
			// y = 1000000-(m*x)+d;
			y = 1000000 - (int) (50000 * (float) x) + 500000;
		}
		// zwischen 11 und 100 kommt raus 500000.... 100000
		else if ((x >= 10) && (x <= 100))
		{
			y = 500000 - (int) (4444f * x) + 100000;
		}
		//zwischen 101 und 1000 kommt raus 100000....30000
		else if ((x >= 100) && (x <= 1000))
		{
			y = 100000 - (int) (77f * x) + 30000;
		} 
		//zwischen 1001 und 10000 kommt raus 30000....10000
		else if ((x >= 1000) && (x <= 10000))
		{
			y = 30000 - (int) (2.2f * x) + 10000;
		} else
			y = 0;
		return y;
	}

	static public float calcSteigung(float k, float k38, float k200)
	{

		if ((k == 0) || (k38 == 0) || (k200 == 0))
			return 0;

		float s = (k * k38) / (k200 * k200);
		return s;
	}
	static public float scaleVertrauen(float vertrau, float maxval)
	{
		//skaliert den vertrauenswert
		//0-> maxval
		//>maxval -> null
		if (vertrau>maxval)
			return 0f;
		else return(maxval-vertrau);
	}
}
