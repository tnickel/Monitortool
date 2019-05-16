package objects;

import hilfsklasse.SG;
import hilfsklasse.ToolsException;
import interfaces.DBObject;

public class PrognoseDbObj extends Obj implements DBObject
{
	private String aufnahmedatum=null;
	//Eine eindeutige Kennung für die Prognose
	private String kennung=null;
	private String info=null;
	private int tid=0;
	
	private String aktname=null;
	private int sliderpostanz=0;
	private float mittlerRang=0;
	private int guteU = 0;
	private int schlechteU = 0;
	private int guteP = 0;
	private int schlechteP = 0;
	private float sliderguete = 0;
	private int useranzahl = 0;
	private int baduseranzahl = 0;
	private int neueguteU = 0;
	private int neueschlechteU = 0;
	private int neuebadU = 0;
	private String symb = null;
	private int alterletztePosting=0;
	private String handelshinweis = "";
	private float kurs=0;
	//prognosenalter in tage, wird aber nicht gespeichert
	private int prognosenalter=0;
	
	
	public PrognoseDbObj(String zeile) throws BadObjectException
	{
		int pos=1;
		try
		{
			aufnahmedatum = new String(SG.nteilstring(zeile, "#", pos));
			pos++;
			kennung=new String(SG.nteilstring(zeile, "#", pos));
			pos++;
			info= new String(SG.nteilstring(zeile, "#", pos));
			pos++;
			tid = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos++;
			aktname = new String(SG.nteilstring(zeile, "#", pos));
			pos++;
			sliderpostanz=Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos++;
			mittlerRang= Float.valueOf(SG.nteilstring(zeile, "#", pos));
			pos++;
			guteU = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos++;
			schlechteU = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos++;
			guteP = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos++;
			schlechteP = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos++;
			sliderguete = Float.valueOf(SG.nteilstring(zeile, "#", pos));
			pos++;
			useranzahl = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos++;
			baduseranzahl = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos++;
			neueguteU = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos++;
			neueschlechteU = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos++;
			neuebadU = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos++;
			symb = new String(SG.nteilstring(zeile, "#", pos));
			pos++;
			handelshinweis = new String(SG.nteilstring(zeile, "#", pos));
			pos++;
			kurs=Float.valueOf(SG.nteilstring(zeile, "#", pos));
			pos++;
			alterletztePosting=Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos++;
			
			
		} catch (ToolsException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String GetSaveInfostring()
	{
		return "aufnahmedatum#kennung#info#tid#aktname#sliderpostanz#mittlerRang#guteU#schlechteU#guteP#schlechteP#sliderguete#useranzahl#baduseranzahl#neueguteU#neueschlechteU#neuebadU#symb#handelshinweis#kurs#alterletztePosting";
	}

	@Override
	public String toString()
	{
		return (aufnahmedatum + "#"+kennung+ "#" +info+"#"+ tid + "#"  + aktname + "#"
				+ sliderpostanz + "#" + mittlerRang + "#" + guteU + "#" + schlechteU+ "#" +
				guteP+ "#" +schlechteP+ "#" +sliderguete+ "#" +useranzahl+ "#" +baduseranzahl+ "#" +neueguteU+ "#" +neueschlechteU+ "#" +neuebadU+ "#" +symb+ "#" +handelshinweis+ "#" +kurs+ "#"+alterletztePosting);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrognoseDbObj other = (PrognoseDbObj) obj;
		if(this.getTid()==other.getThreadid())
			if(this.aufnahmedatum.equals(other.getAufnahmedatum())==true)
				return true;
		
		return false;
	}
	public int getThreadid()
	{
		return tid;
	}

	public String getAufnahmedatum()
	{
		return aufnahmedatum;
	}

	public void setAufnahmedatum(String aufnahmedatum)
	{
		this.aufnahmedatum = aufnahmedatum;
	}

	public String getKennung()
	{
		return kennung;
	}

	public void setKennung(String kennung)
	{
		this.kennung = kennung;
	}

	public String getInfo()
	{
		return info;
	}

	public void setInfo(String info)
	{
		this.info = info;
	}

	public int getTid()
	{
		return tid;
	}

	public void setTid(int tid)
	{
		this.tid = tid;
	}

	public String getAktname()
	{
		return aktname;
	}

	public void setAktname(String aktname)
	{
		this.aktname = aktname;
	}

	public int getSliderpostanz()
	{
		return sliderpostanz;
	}

	public void setSliderpostanz(int sliderpostanz)
	{
		this.sliderpostanz = sliderpostanz;
	}

	public float getMittlerRang()
	{
		return mittlerRang;
	}

	public void setMittlerRang(float mittlerRang)
	{
		this.mittlerRang = mittlerRang;
	}

	public int getGuteU()
	{
		return guteU;
	}

	public void setGuteU(int guteU)
	{
		this.guteU = guteU;
	}

	public int getSchlechteU()
	{
		return schlechteU;
	}

	public void setSchlechteU(int schlechteU)
	{
		this.schlechteU = schlechteU;
	}

	public int getGuteP()
	{
		return guteP;
	}

	public void setGuteP(int guteP)
	{
		this.guteP = guteP;
	}

	public int getSchlechteP()
	{
		return schlechteP;
	}

	public void setSchlechteP(int schlechteP)
	{
		this.schlechteP = schlechteP;
	}

	public float getSliderguete()
	{
		return sliderguete;
	}

	public void setSliderguete(float sliderguete)
	{
		this.sliderguete = sliderguete;
	}

	public int getUseranzahl()
	{
		return useranzahl;
	}

	public void setUseranzahl(int useranzahl)
	{
		this.useranzahl = useranzahl;
	}

	public int getBaduseranzahl()
	{
		return baduseranzahl;
	}

	public void setBaduseranzahl(int baduseranzahl)
	{
		this.baduseranzahl = baduseranzahl;
	}

	public int getNeueguteU()
	{
		return neueguteU;
	}

	public void setNeueguteU(int neueguteU)
	{
		this.neueguteU = neueguteU;
	}

	public int getNeueschlechteU()
	{
		return neueschlechteU;
	}

	public void setNeueschlechteU(int neueschlechteU)
	{
		this.neueschlechteU = neueschlechteU;
	}

	public int getNeuebadU()
	{
		return neuebadU;
	}

	public void setNeuebadU(int neuebadU)
	{
		this.neuebadU = neuebadU;
	}

	public String getSymb()
	{
		return symb;
	}

	public void setSymb(String symb)
	{
		this.symb = symb;
	}

	public String getHandelshinweis()
	{
		return handelshinweis;
	}

	public void setHandelshinweis(String handelshinweis)
	{
		this.handelshinweis = handelshinweis;
	}

	public float getKurs()
	{
		return kurs;
	}

	public void setKurs(float kurs)
	{
		this.kurs = kurs;
	}

	public int getAlterletztePosting()
	{
		return alterletztePosting;
	}

	public void setAlterletztePosting(int alterletztePosting)
	{
		this.alterletztePosting = alterletztePosting;
	}

	public int getPrognosenalter()
	{
		return prognosenalter;
	}

	public void setPrognosenalter(int prognosenalter)
	{
		this.prognosenalter = prognosenalter;
	}

	public void postprocess()
	{}


}
