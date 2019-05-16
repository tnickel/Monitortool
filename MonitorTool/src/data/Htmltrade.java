package data;

import java.util.regex.Pattern;

public class Htmltrade
{
	int tradenumber=0;
	String date=null;
	String richtung=null;
	int auftragsnummer=0;
	float lotsize=0;
	float prize=0;
	//0: noch nicht verwendet, 1:schon verwendet 
	int status=0;
	float gewinn=0;
	
	public float getGewinn()
	{
		return gewinn;
	}

	public void setGewinn(float gewinn)
	{
		this.gewinn = gewinn;
	}

	public int getTradenumber()
	{
		return tradenumber;
	}

	public void setTradenumber(int tradenumber)
	{
		this.tradenumber = tradenumber;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public String getRichtung()
	{
		return richtung;
	}

	public void setRichtung(String richtung)
	{
		this.richtung = richtung;
	}

	public int getAuftragsnummer()
	{
		return auftragsnummer;
	}

	public void setAuftragsnummer(int auftragsnummer)
	{
		this.auftragsnummer = auftragsnummer;
	}

	public float getLotsize()
	{
		return lotsize;
	}

	public void setLotsize(float lotsize)
	{
		this.lotsize = lotsize;
	}

	public float getPrize()
	{
		return prize;
	}

	public void setPrize(float prize)
	{
		this.prize = prize;
	}

	//beim htmltrade ist nur ein opentrade/bzw. closetrade vorhanden
	//es muss im Prinzip der zugehörigen Gegenpart gefunden werden
	public Htmltrade(String tradezeile)
	{
		//<tr align=right><td>1</td>
		//<td class=msdate>2011.01.03 16:00</td>
		//<td>buy</td>
		//<td>1</td>
		//<td class=mspt>0.50</td>
		//<td style="mso-number-format:0\.00000;">1.33638</td>
		//<td style="mso-number-format:0\.00000;" align=right>0.00000</td>
		//<td style="mso-number-format:0\.00000;" align=right>0.00000</td>
		//<td class=mspt>-58.00</td>
		//<td colspan=2></td></tr>

		
		
		//die htmlzeile zerstückeln
		String[] segs = tradezeile.split( Pattern.quote( "/td" ) );
		
			int index1=segs[0].indexOf("<td>") + 4;
			int index2=segs[0].lastIndexOf("<");
		
			String tradenumberstr=segs[0].substring(index1,index2);
			tradenumber=Integer.valueOf(tradenumberstr);
			
			date=segs[1].substring(
					segs[1].indexOf("msdate>") + 7,
					segs[1].lastIndexOf("<"));
	
			date=date.concat(":00");
			
			richtung=segs[2].substring(
					segs[2].indexOf("<td>") + 4,
					segs[2].lastIndexOf("<"));
	
			auftragsnummer=Integer.valueOf(segs[3].substring(
					segs[3].indexOf("<td>") + 4,
					segs[3].lastIndexOf("<")));
			
			
			lotsize=Float.valueOf(segs[4].substring(
					segs[4].indexOf("mspt>") + 5,
					segs[4].lastIndexOf("<")));
			
			prize=Float.valueOf(segs[5].substring(
					segs[5].indexOf("0000;\">") + 7,
					segs[5].lastIndexOf("<")));
			
			if(segs[8].length()>17)
			{
				gewinn=Float.valueOf(segs[8].substring(
						segs[8].indexOf("mspt>") + 5,
						segs[8].lastIndexOf("<")));
			}
				
	}
	
}
