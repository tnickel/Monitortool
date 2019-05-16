package objects;


public class Keyword 
{
	
	private String keyword=null;
	private int pos =0;
	private int type=0;
	//aufnahmedatum
	private String aufdatum=null;
	
	/*************************************************************************/
	public String getKeyword()
	{
		return keyword;
	}
	public void setKeyword(String keyword)
	{
		this.keyword = keyword;
	}
	public int getPos()
	{
		return pos;
	}
	public void setPos(int pos)
	{
		this.pos = pos;
	}
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type = type;
	}
	public String getAufdatum()
	{
		return aufdatum;
	}
	public void setAufdatum(String aufdatum)
	{
		this.aufdatum = aufdatum;
	}
	
}
