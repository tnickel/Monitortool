package data;

import hiflsklasse.Tracer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class CorelAllResult
{
	//Idee: Hier werden alle Resultate gespeichert
	//Für jeder Strategie der vergleichsdatenreihe wird ermittelt wie stark die zu den anderen Datenreihen
	//Korreliert

	//hashmap für einen Namen einen index, die map speichert zu jedem filenamen einen index
	HashMap<String, Integer> namenpos = new HashMap<String, Integer>();
	int namennextpos=0;
	int maxindex_glob=0;
	//der hitcounter wird in einer hitliste gespeichert
	ArrayList<HitElem> hitlist=new ArrayList<HitElem>();
	
	//hier werden die maximalen hits für einen filenamen gespeichert
	//diese Info braucht man man man prozentuale Angaben machen möchte
	HashMap<String,Integer> maxhitmap = new HashMap<String,Integer>();
	
	//hier werden die anz trades für einen filenamen gespeichert
	HashMap<String,Integer> tradeanz = new HashMap<String,Integer>();
	
	public void clear()
	{
		namenpos= new HashMap<String, Integer>();
		tradeanz= new HashMap<String,Integer>();
		hitlist=new ArrayList<HitElem>();
		maxhitmap = new HashMap<String,Integer>();
	}
	public boolean setTradeanz(String fnam,int anz)
	{
		if(tradeanz.containsKey(fnam)==true)
			Tracer.WriteTrace(10, "E:internal fnam<"+fnam+"> already set in tradeanz");
		else
			tradeanz.put(fnam, anz);
		return true;
	}
	public int getTradeanz(String fnam)
	{
		if(tradeanz.containsKey(fnam)==false)
			return 0;
		else
			return(tradeanz.get(fnam));
	}
	
	
	public boolean setMaxhits(String fnam,int maxhit)
	{
		if(maxhitmap.containsKey(fnam)==true)
			Tracer.WriteTrace(10, "E:internal fnam<"+fnam+"> already set in maxhitmap");
		else
			maxhitmap.put(fnam, maxhit);
		return true;
	}
	public int getMaxhits(String fnam)
	{
		if(maxhitmap.containsKey(fnam)==false)
			return 0;
		else
			return(maxhitmap.get(fnam));
	}
	public boolean addHits(String fnam, int index,int anzhits )
	{
		//fnam: 	name der Strategie
		//index: 	dies ist der index 1...n der Datenreihe
		//anzhits:  soviele Hitpunkte möchte ich fnamen geben
		
		int pos;
		//den positionsspeicher in der Hitlist ermitteln
		//die speicher sind in listen gespeichert. Zu jedem namen gibt es einen
		//eindeutigen Index
		if(namenpos.containsKey(fnam)==true)
			pos=namenpos.get(fnam);
		else
		{
			namenpos.put(fnam,namennextpos);
			pos=namennextpos;
			namennextpos++;
		}
		
		//schaue nach ob es schon ein element gibt was die Hits speichert

		HitElem hi=null;
		//am anfang ist die hitliste leer, erzeuge das erste element
		if(hitlist.isEmpty()==true)
		{
			hi= new HitElem();
			hi.setcounter(index, anzhits);
			hitlist.add(hi);
		}
		else if(pos>=hitlist.size())
		{
			//element ist noch nicht in der hitliste dann erzeuge das element
			hi= new HitElem();
			hi.setcounter(index, anzhits);
			hitlist.add(hi);
		}
		else 
		{
			//das element sollte in der liste sein dann hole es
			//und erhöhe den index
			hi=hitlist.get(pos);
			hi.inc(index, anzhits);
		}
		return true;
	}
	
	
	public int getHitpoints(String stratnam)
	{
		int pos =0;
		//gibt für einen Stratnam/fnam die hitpoints zurück
		//und zwar über alle indexe
		if(namenpos.containsKey(stratnam)==true)
			 pos=namenpos.get(stratnam);
		else
			return 0;
		
		HitElem hi= hitlist.get(pos);
		int sum=hi.gethitsum();
		return sum;
	}
	public String getHitproz(String stratnam)
	{
		int pos =0;
		//gibt für einen Stratnam/fnam die hitpoints zurück
		//und zwar über alle indexe
		if(namenpos.containsKey(stratnam)==true)
			 pos=namenpos.get(stratnam);
		else
			return "0.0";
		
		HitElem hi= hitlist.get(pos);
		float sum=(float)hi.gethitsum();
		float max=(float)maxhitmap.get(stratnam);
		float proz=(float)((sum/max)*100);
		DecimalFormat format = new DecimalFormat("#####.##"); 

		String retval=format.format(proz);
		retval=retval.replace(",",".");
		return retval;
	}
	public String getHitstring(String stratnam)
	{
		//für einen Strategienamen wird der hitstring erzeugt
		//Aus einem hitstring geht hervor wieviele Hits eine Strategie auf einer
		//datenreihe beinhaltet, es wird ausserdem die Hitsumme angegeben
		int pos =0;
		if(namenpos.containsKey(stratnam)==true)
			 pos=namenpos.get(stratnam);
		else
			return "?";
		
		HitElem hi= hitlist.get(pos);
		String hs=hi.gethistring();
		return hs;
	}
}
