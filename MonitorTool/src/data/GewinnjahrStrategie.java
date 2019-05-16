package data;

public class GewinnjahrStrategie
{
	// hier ist der Gewinn von einer Strategie drin (bzw. hier wird
	// nur immer eine Strategie behandelt
	// [40jahre][12monate]
	protected float[][] monatsgewinn = new float[20][12];
	protected int[][] postradezaehler = new int[20][12];
	protected int[][] negtradezaehler = new int[20][12];
	protected float[][] grossprofit = new float[20][12];
	protected float[][] grossloss = new float[20][12];
	// gewanz=gibt an wieviele trades in der strategie sind
	protected int[][] gewanz = new int[20][12];

	// dies ist der index des firstMonats, firstJahr
	protected int fmonatsindex = -99;
	protected int fjahresindex = -99;

	public GewinnjahrStrategie()
	{

	}

	public float getMonatsgewinn(int jahrindex, int i)
	{
		return monatsgewinn[jahrindex][i];
	}

	public float addGewinn(int jahrindex, int monatsindex, float profit)
	{
		// addiert den Gewinn
		// return:liefert den Gewinn für den trade i zurück

		// falls fmonatsindex noch nicht initialisiert ist, dann setzte den
		// ersten Monat
		if (fmonatsindex < 0)
			fmonatsindex = monatsindex;
		if (fjahresindex < 0)
			fjahresindex = jahrindex;

		// hier wird ein profit einfach aufaddiert

		// gesammtgewinnzaehler erhöhen
		gewanz[jahrindex][monatsindex]++;

		if (profit > 0)
		{
			postradezaehler[jahrindex][monatsindex]++;
			grossprofit[jahrindex][monatsindex] += profit;
		}
		if (profit <= 0)
		{
			negtradezaehler[jahrindex][monatsindex]++;
			grossloss[jahrindex][monatsindex] += profit;
		}
		monatsgewinn[jahrindex][monatsindex] += profit;
		return profit;
	}

	public float calcMonatsgewinn(int jahr, int i)
	{
		// hier wird der monatsgewinn für den monat i berechnet
		// i=0 januar
		return (monatsgewinn[jahr][i]);
	}

	public float calcGesammtgewinn()
	{
		float gesgew = 0;
		// berechnet den Gesammtgewinn für diese strategie
		for (int j = 0; j < 20; j++)
		{
			for (int i = 0; i < 12; i++)
			{
				gesgew += monatsgewinn[j][i];
			}
		}
		return gesgew;
	}

	protected int getFmonatsindex()
	{
		return fmonatsindex;
	}
	protected int getFjahresindex()
	{
		return fjahresindex;
	}

}
