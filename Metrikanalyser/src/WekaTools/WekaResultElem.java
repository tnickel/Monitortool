package WekaTools;

import weka.classifiers.Evaluation;

public class WekaResultElem
{
	//directoryname das wir lernen wollen
	private String dirname=null;
	//hier ist die responsemessage drin
	private Evaluation eval=null;
	private String forestpath=null;
	private String datapath=null;
	public WekaResultElem()
	{}
	public String getDirname()
	{
		return dirname;
	}
	public void setDirname(String dirname)
	{
		this.dirname = dirname;
	}
	public Evaluation getEvaluation()
	{
		return eval;
	}
	public void setEvaluation(Evaluation eval,String forest,String data)
	{
		this.eval = eval;
		this.forestpath=forest;
		this.datapath=data;
	}
	public String getForestpath()
	{
		return forestpath;
	}
	public void setForestpath(String forestpath)
	{
		this.forestpath = forestpath;
	}
	public String getDatapath()
	{
		return datapath;
	}
	public void setDatapath(String datapath)
	{
		this.datapath = datapath;
	}
	
	
	
}
