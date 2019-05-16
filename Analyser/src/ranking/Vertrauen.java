package ranking;

public class Vertrauen
{
	private float vertrauensfaktor_g = 0;
	private int sumrp_g = 0;
	private int sliderelemanz_g = 0;
	private float durchschnittrank_g = 0;
	private float minfaktor_g = 0;
	private float maxfaktor_g = 0;
	private float gefälle_g = 0;
	private int anzgute_g = 0;
	private int anzschlechte_g = 0;

	public Vertrauen(float vertrau, int sum, int anz, float durchrank,
			float minfak, float maxfak, float gef, int anzgute, int anzschlechte)
	{
		vertrauensfaktor_g = vertrau;
		sumrp_g = sum;
		sliderelemanz_g = anz;
		durchschnittrank_g = durchrank;
		minfaktor_g = minfak;
		maxfaktor_g = maxfak;
		gefälle_g = gef;
		anzgute_g = anzgute;
		anzschlechte_g = anzschlechte;
	}

	public int getAnzgute_g()
	{
		return anzgute_g;
	}

	public void setAnzgute_g(int anzgute_g)
	{
		this.anzgute_g = anzgute_g;
	}

	public int getAnzschlechte_g()
	{
		return anzschlechte_g;
	}

	public void setAnzschlechte_g(int anzschlechte_g)
	{
		this.anzschlechte_g = anzschlechte_g;
	}

	public float getVertrauensfaktor_g()
	{
		return vertrauensfaktor_g;
	}

	public void setVertrauensfaktor_g(float vertrauensfaktor_g)
	{
		this.vertrauensfaktor_g = vertrauensfaktor_g;
	}

	public int getSumrp_g()
	{
		return sumrp_g;
	}

	public void setSumrp_g(int sumrp_g)
	{
		this.sumrp_g = sumrp_g;
	}

	public int getSliderelemanz_g()
	{
		return sliderelemanz_g;
	}

	public void setSliderelemanz_g(int sliderelemanz_g)
	{
		this.sliderelemanz_g = sliderelemanz_g;
	}

	public float getDurchschnittrank_g()
	{
		return durchschnittrank_g;
	}

	public void setDurchschnittrank_g(float durchschnittrank_g)
	{
		this.durchschnittrank_g = durchschnittrank_g;
	}

	public float getMinfaktor_g()
	{
		return minfaktor_g;
	}

	public void setMinfaktor_g(float minfaktor_g)
	{
		this.minfaktor_g = minfaktor_g;
	}

	public float getMaxfaktor_g()
	{
		return maxfaktor_g;
	}

	public void setMaxfaktor_g(float maxfaktor_g)
	{
		this.maxfaktor_g = maxfaktor_g;
	}

	public float getGefälle_g()
	{
		return gefälle_g;
	}

	public void setGefälle_g(float gefälle_g)
	{
		this.gefälle_g = gefälle_g;
	}



}
