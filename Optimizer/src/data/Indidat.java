package data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

public class Indidat
{
	//hier wird nur >>eine<< Zeile vom Indikator gepsichert
	// diese klasse speichert eine zeile vom den eingabedaten
	// 2011.01.02;22:00;1.33444;0
	private Date dt = null;
	private float price =0;
	
	//gibt an wieviele daten die eingabezeile hat
	private int anzdat=0;

	private ArrayList<Boolean> b = new ArrayList<Boolean>();

	public Indidat(String zeile)
	{
		//in dieser Zeile ist ein einzelner indikator
		SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm");

		String eingabe = zeile;
		String[] parts = eingabe.split(Pattern.quote(";"));

		String datumstr = new String(parts[0] + " " + parts[1]);
		price=Float.valueOf(parts[2]);
		
		anzdat=parts.length-3;
		try
		{
			dt = df.parse(datumstr);
			
			int anz = parts.length;
			for (int i = 3; i < anz; i++)
			{
				boolean val = false;
				if (parts[i].equals("1"))
					val = true;

				b.add(val);
			}
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	
	public float getPrice()
	{
		return price;
	}



	public void setPrice(float price)
	{
		this.price = price;
	}



	public Date getDt()
	{
		return dt;
	}

	public void setDt(Date dt)
	{
		this.dt = dt;
	}

	public boolean getArrayPos(int index)
	{
		if(index<0)
			return false;
		else
			return (b.get(index));
	}
	
	public ArrayList<Boolean> getB()
	{
		return b;
	}

	public void setB(ArrayList<Boolean> b)
	{
		this.b = b;
	}

	public int calcAnzDat()
	{
		return anzdat;
	}
}
