package ranking;

import interfaces.RankInterface;
import objects.Datumsintervall;

public class GewinnStatObj implements Comparable<GewinnStatObj>, RankInterface
{
	// dieses objekt speichert einen einzelnen Gewinn

	// username
	String uname = null;
	// Dies ist die gesammte Gewinnsumme
	float gewinnsumme = 0;

	// Die gewinnablweichung ist die abweichung vom mittelgewinn.
	float gewabweichungssumme = 0;
	// mittelgewinn ist der durchschnittliche Gewinn
	float mittelgewinn = 0;

	// die anzahl der stützstellen gibt an aus wieviele Werten sich der Gewinn
	// zusammensetzt
	int stuetzstellenanz = 0;
	// die bewertungstage geben den Zeitraum in tagen an, an dem der Kurs
	// analysiert wurde
	int bewertungstage = 0;
	// min und maxdatum für die Gewinnbetrachtung
	Datumsintervall datinterv = null;

	// Wird für das Sortieren verwendet
	public int compareTo(GewinnStatObj gstat)
	{

		if (gewabweichungssumme > gstat.gewabweichungssumme)
			return -1;
		if (gewabweichungssumme < gstat.gewabweichungssumme)
			return 1;

		return 0;
	}

	public String getInfoString()
	{
		return ("<" + this.getUname() + "> gsum<" + this.getGewinnsumme()
				+ "> stabil<" + this.getGewabweichungsSumme() + ">");
	}

	public String getSaveString()
	{
		return ("<" + this.getUname() + "> gsum<" + this.getGewinnsumme()
				+ "> stabil<" + this.getGewabweichungsSumme() + ">");
	}

	public float getGewinnsumme()
	{
		return gewinnsumme;
	}

	public void setGewinnsumme(float gewinnsumme)
	{
		this.gewinnsumme = gewinnsumme;
	}

	public String getUname()
	{
		return uname;
	}

	public void setUname(String uname)
	{
		this.uname = uname;
	}

	public float getGewabweichungsSumme()
	{
		return gewabweichungssumme;
	}

	public void setGewabweichungsSumme(float gewabweichung)
	{
		this.gewabweichungssumme = gewabweichung;
	}

	public float getMittelgewinn()
	{
		return mittelgewinn;
	}

	public void setMittelgewinn(float mittelgewinn)
	{
		this.mittelgewinn = mittelgewinn;
	}

	public int getStuetzstellenanz()
	{
		return stuetzstellenanz;
	}

	public void setStuetzstellenanz(int stuetzstellenanz)
	{
		this.stuetzstellenanz = stuetzstellenanz;
	}

	public int getBewertungstage()
	{
		return bewertungstage;
	}

	public void setBewertungstage(int bewertungstage)
	{
		this.bewertungstage = bewertungstage;
	}

	public Datumsintervall getDatinterv()
	{
		return datinterv;
	}

	public void setDatinterv(Datumsintervall datinterv)
	{
		this.datinterv = datinterv;
	}
}
