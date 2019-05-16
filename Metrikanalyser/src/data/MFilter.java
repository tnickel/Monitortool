package data;

public class MFilter
{
	//filterkriterien
	boolean stabilflag=false;
	boolean anzstratflag=true;
	float minstabil=(float)0.2;
	int minanzahlstrat=5;
	
	//für fitnessfunktion
	int profitfaktor=1;
	int stabilfaktor=1;
	
	
	public boolean isStabilflag()
	{
		return stabilflag;
	}
	public void setStabilflag(boolean stabilflag)
	{
		this.stabilflag = stabilflag;
	}
	public boolean isAnzstratflag()
	{
		return anzstratflag;
	}
	public void setAnzstratflag(boolean anzstratflag)
	{
		this.anzstratflag = anzstratflag;
	}
	public float getMinstabil()
	{
		return minstabil;
	}
	public void setMinstabil(float minstabil)
	{
		this.minstabil = minstabil;
	}
	public int getMinanzahlstrat()
	{
		return minanzahlstrat;
	}
	public void setMinanzahlstrat(int minanzahlstrat)
	{
		this.minanzahlstrat = minanzahlstrat;
	}
	public int getProfitfaktor()
	{
		return profitfaktor;
	}
	public void setProfitfaktor(int profitfaktor)
	{
		this.profitfaktor = profitfaktor;
	}
	public int getStabilfaktor()
	{
		return stabilfaktor;
	}
	public void setStabilfaktor(int stabilfaktor)
	{
		this.stabilfaktor = stabilfaktor;
	}
	
	
	
}
