package WekaTools;

public class ClassifiedElem implements Comparable<ClassifiedElem>
{
	String sourcepath=null;
	String destpath=null;
	double predictValue=0;
	double actualValue=0;
	int selected=0;
	
	public String getSourcepath()
	{
		return sourcepath;
	}
	public void setSourcepath(String sourcepath)
	{
		this.sourcepath = sourcepath;
	}
	public String getDestpath()
	{
		return destpath;
	}
	public void setDestpath(String destpath)
	{
		this.destpath = destpath;
	}
	public double getPredictValue()
	{
		return predictValue;
	}
	public void setPredictValue(double value)
	{
		this.predictValue = value;
	}
	
	public double getActualValue()
	{
		return actualValue;
	}
	public void setActualValue(double actualValue)
	{
		this.actualValue = actualValue;
	}
	public void setSelected(int sel)
	{
		selected=sel;
	}
	public int getSelected()
	{
		return selected;
	}
	 @Override
	    public int compareTo(ClassifiedElem other) 
	 {
		 double val1=other.getPredictValue();
		 double val2=this.getPredictValue();
		
		 
	        return Double.compare(val1,val2);
	    }
}
