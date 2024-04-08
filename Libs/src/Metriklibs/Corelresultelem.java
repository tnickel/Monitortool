package Metriklibs;

public class Corelresultelem implements Comparable<Corelresultelem>
{
	//hier ist nur der korrelationswert für ein einziges attribut drin
	private double val=0;
	private String attribname=null;
	
	
	public double getVal()
	{
		
		return val;
	}
	public void setVal(double val)
	{
		this.val = val;
	}
	public String getAttribname()
	{
		return attribname;
	}
	public void setAttribname(String attribname)
	{
		this.attribname = attribname;
	}
	
	  public int compareTo(Corelresultelem m) 
	  {
	      if( m.val>this.val)
	    	  return 1;
	      if(m.val<this.val)
	    	  return -1;
	      else
	    	  return 0;
	  }
}
