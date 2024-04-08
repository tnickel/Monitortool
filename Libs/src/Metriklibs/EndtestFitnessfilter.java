package Metriklibs;

public class EndtestFitnessfilter
{
	boolean nettoflag = false;
	boolean nettostabilflag = false;
	boolean nettorobustflag = false;
	boolean nettorobuststabilflag = false;
	boolean stabilflag=false;
	boolean nettoperstrategyflag=false;
	
	public EndtestFitnessfilter()
	{
	}

	public EndtestFitnessfilter(boolean nettoflag_x, boolean nettostabilflag_x,
			boolean nettorobustflag_x, boolean nettorobuststabilflag_x,boolean nettoperstrategyflag_x)
	{
		nettoflag = nettoflag_x;
		nettostabilflag = nettostabilflag_x;
		nettorobustflag = nettorobustflag_x;
		nettorobuststabilflag = nettorobuststabilflag_x;
		nettoperstrategyflag=nettoperstrategyflag_x;
	}

	public boolean isNettoflag()
	{
		return nettoflag;
	}

	public void setNettoflag(boolean nettoflag)
	{
		this.nettoflag = nettoflag;
	}

	public boolean isNettostabilflag()
	{
		return nettostabilflag;
	}

	public void setNettostabilflag(boolean nettostabilflag)
	{
		this.nettostabilflag = nettostabilflag;
	}

	public boolean isNettorobustflag()
	{
		return nettorobustflag;
	}

	public void setNettorobustflag(boolean nettorobustflag)
	{
		this.nettorobustflag = nettorobustflag;
	}

	public boolean isNettorobuststabilflag()
	{
		return nettorobuststabilflag;
	}

	public void setNettorobuststabilflag(boolean nettorobuststabilflag)
	{
		this.nettorobuststabilflag = nettorobuststabilflag;
	}

	public boolean isStabilflag()
	{
		return stabilflag;
	}

	public void setStabilflag(boolean stabilflag)
	{
		this.stabilflag = stabilflag;
	}

	public boolean isNettoperstrategyflag()
	{
		return nettoperstrategyflag;
	}

	public void setNettoperstrategyflag(boolean nettoperstrategyflag)
	{
		this.nettoperstrategyflag = nettoperstrategyflag;
	}

}
