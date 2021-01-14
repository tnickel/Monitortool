package data;

public class SymbolReplaceElem
{
	protected String orgsymb=null;
	protected String destsymb=null;
	
	
	public SymbolReplaceElem()
	{}
	
	public SymbolReplaceElem(String symb1, String symb2)
	{
		System.out.println("replace");
		orgsymb=symb1;
		destsymb=symb2;
	}
	
	protected String getOrgsymb()
	{
		return orgsymb;
	}
	protected void setOrgsymb(String orgsymb)
	{
		this.orgsymb = orgsymb;
	}
	protected String getDestsymb()
	{
		return destsymb;
	}
	protected void setDestsymb(String destsymb)
	{
		this.destsymb = destsymb;
	}
	
	
}
