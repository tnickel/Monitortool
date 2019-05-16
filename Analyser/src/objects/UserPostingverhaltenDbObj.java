package objects;

import hilfsklasse.SG;
import hilfsklasse.Tools;
import hilfsklasse.ToolsException;
import interfaces.DBObject;

public class UserPostingverhaltenDbObj extends Obj  implements DBObject, Comparable<UserPostingverhaltenDbObj>
{
	private String uname = null;
	private int anzausgewpostings = 0;
	private int postanzExtLink = 0;
	private int postanzIcons = 0;
	private int postanzIconsSmile = 0;
	private int postanzIconsAngry = 0;
	// reaktive User warten bis die Situation reif ist
	private int postanzReaktiv = 0;
	// proaktive User sind sehr schnell (spekullativ)
	private int postanzProaktiv = 0;
	// optionale user sehen einen Schmetterling und hinterher
	private int postanzOptional = 0;
	// prozedurale usergreifen auf bewährtes zurück und haben bestimmte
	// Handlungsabläufe
	private int postanzahlProzedural = 0;
	
	private int rangpoints=0;
	private int rang=0;
	
	private String ranginfostring=null;

	public UserPostingverhaltenDbObj(String zeile) throws BadObjectException
	{
		int pos = 1;
		try
		{
			//System.out.println("zeile =<"+zeile+">");
			uname = new String(Tools.nteilstring(zeile, "#", pos));
			pos++;
			anzausgewpostings = Integer
					.valueOf(SG.nteilstring(zeile, "#", pos));
			pos++;
			postanzExtLink = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos++;
			postanzIcons = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos++;
			postanzIconsSmile = Integer
					.valueOf(SG.nteilstring(zeile, "#", pos));
			pos++;
			postanzIconsAngry = Integer
					.valueOf(SG.nteilstring(zeile, "#", pos));
			pos++;
			postanzReaktiv = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos++;
			postanzProaktiv = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos++;
			postanzOptional = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos++;
			postanzahlProzedural = Integer.valueOf(SG.nteilstring(zeile, "#",
					pos));
			pos++;
			rangpoints = Integer.valueOf(SG.nteilstring(zeile, "#",
					pos));
			pos++;
			ranginfostring = new String(Tools.nteilstring(zeile, "#", pos));
			pos++;
			rang=Integer.valueOf(SG.nteilstring(zeile, "#",
					pos));
			
		} catch (ToolsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getThreadid()
	{
		return 0;
	}

	public void postprocess()
	{
	}
	
	
	@Override
	public String GetSaveInfostring()
	{
		return "uname#anzausgewpostings#postanzExtLink#postanzIcons#postanzIconsSmile#postanzIconsAngry#postanzReaktiv#postanzProaktiv#postanzOptional#postanzahlProzedural#Rangpoints#Ranginfostring#rang";
	}

	@Override
	public String toString()
	{
		return (uname + "#"+anzausgewpostings+ "#" +postanzExtLink+"#"+ postanzIcons + "#"  + postanzIconsSmile + "#"
				+ postanzIconsAngry + "#" + postanzReaktiv + "#" + postanzProaktiv + "#" + postanzOptional+ "#" +
				postanzahlProzedural + "#"+ rangpoints+"#"+ranginfostring+"#"+rang);
	}
	/**************************** getter und setter ********************************/

	public String getUname()
	{
		return uname;
	}

	public void setUname(String uname)
	{
		this.uname = uname;
	}

	public int getAnzausgewpostings()
	{
		return anzausgewpostings;
	}

	public void setAnzausgewpostings(int anzausgewpostings)
	{
		this.anzausgewpostings = anzausgewpostings;
	}

	public int getPostanzExtLink()
	{
		return postanzExtLink;
	}

	public void setPostanzExtLink(int postanzExtLink)
	{
		this.postanzExtLink = postanzExtLink;
	}

	public int getPostanzIcons()
	{
		return postanzIcons;
	}

	public void setPostanzIcons(int postanzIcons)
	{
		this.postanzIcons = postanzIcons;
	}

	public int getPostanzIconsSmile()
	{
		return postanzIconsSmile;
	}

	public void setPostanzIconsSmile(int postanzIconsSmile)
	{
		this.postanzIconsSmile = postanzIconsSmile;
	}

	public int getPostanzIconsAngry()
	{
		return postanzIconsAngry;
	}

	public void setPostanzIconsAngry(int postanzIconsAngry)
	{
		this.postanzIconsAngry = postanzIconsAngry;
	}

	public int getPostanzReaktiv()
	{
		return postanzReaktiv;
	}

	public void setPostanzReaktiv(int postanzReaktiv)
	{
		this.postanzReaktiv = postanzReaktiv;
	}

	public int getPostanzProaktiv()
	{
		return postanzProaktiv;
	}

	public void setPostanzProaktiv(int postanzProaktiv)
	{
		this.postanzProaktiv = postanzProaktiv;
	}

	public int getPostanzOptional()
	{
		return postanzOptional;
	}

	public void setPostanzOptional(int postanzOptional)
	{
		this.postanzOptional = postanzOptional;
	}

	public int getPostanzahlProzedural()
	{
		return postanzahlProzedural;
	}

	public void setPostanzahlProzedural(int postanzahlProzedural)
	{
		this.postanzahlProzedural = postanzahlProzedural;
	}

	public int getRangpoints()
	{
		return rangpoints;
	}

	public void setRangpoints(int rangpoints)
	{
		this.rangpoints = rangpoints;
	}

	public String getRanginfostring()
	{
		return ranginfostring;
	}

	public void setRanginfostring(String ranginfostring)
	{
		this.ranginfostring = ranginfostring;
	}

	public int getRang()
	{
		return rang;
	}

	public void setRang(int rang)
	{
		this.rang = rang;
	}
	
	// Wird für das Sortieren verwendet
	public int compareTo(UserPostingverhaltenDbObj argument)
	{
		
		if (calcProzLinks() > argument.calcProzLinks())
			return -1;
		if (calcProzLinks() < argument.calcProzLinks())
			return 1;

		return 0;
	}
	
	/****************************** funktionen *******************************/
	public int calcRankingpoints()
	{
		this.rangpoints=postanzExtLink;
		return this.rangpoints;
	}
	public float calcProzLinks()
	//ermittelt den prozentualen Anteil von links
	{
		float gespost=(float)this.anzausgewpostings;
		float linkanz=(float)this.postanzExtLink;
		
		float proz=(100/gespost)*linkanz;
		return proz;
	}
	public float calcProzIcons()
	//ermittelt den prozentualen Anteil von links
	{
		float gespost=(float)this.anzausgewpostings;
		float sanz=(float)this.postanzIcons;
		
		float proz=(100/gespost)*sanz;
		return proz;
	}
}
