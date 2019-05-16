package stores;

import hilfsklasse.Tracer;

import java.util.ArrayList;

import objects.KeyDbObj;

import org.eclipse.swt.widgets.Display;

import swtOberfl�che.Tab13ShowTexte;
import db.DB;

public class KeyDB  extends DB
{
	//Dies ist die PuscherDB die s�mtliche Puscher(B�rsenbl�tter enth�lt)
	public KeyDB()
	{

		Tracer.WriteTrace(20, "Info:konstruktor von UeberwachungDB");
		LoadDB("keydb", null, 0);

	}
	public boolean plausiVorNeuaufnahme(Object obj)
	{
		return true;
	}
	public void postprocess()
	{}
	
	public Boolean checkKey(String key)
	{
		int anz=this.GetanzObj();
		for(int i=0; i<anz; i++)
		{
			KeyDbObj ko = (KeyDbObj)this.GetObjectIDX(i);
			if(ko.getKeyword().equals(key))
				return true;
		}
		return false;
	}
	public void clearHitcounter()
	{
		int anz=this.GetanzObj();
		for(int i=0; i<anz; i++)
		{
			KeyDbObj ko = (KeyDbObj)this.GetObjectIDX(i);
			ko.setHitcounter(0);
		}
	}
	public void ErstellePuschedkeywortliste(Display dis,int mid,String fnam)
	{
		//F�r eine mid wird eine Liste von BB-Bl�ttern erstellt die diese Keywoerter enthalten
		//Step:1 Erstelle f�r eine Mid eine Keywortmenge
		//Step:2 Schaue in welchen BB diese Keywoerter vorkommen

		//Step 1: Erstelle Schl�sselmenge
		ArrayList<String> Schluesselmenge = new ArrayList<String>(); 
		
		
		int anz=this.GetanzObj();
		for(int i=0; i<anz; i++)
		{
			KeyDbObj ko = (KeyDbObj)this.GetObjectIDX(i);
			if(ko.getMid()==mid)
				Schluesselmenge.add(ko.getKeyword());
		}
		
		BoersenBlaetterDB bbdb= new BoersenBlaetterDB();
		//Erstellt f�r die Schluessenmenge eine Liste von BB wo �berall diese Keyw�rter vorkommen
		bbdb.genBBvorkommen(Schluesselmenge,fnam);

		Tab13ShowTexte st = new Tab13ShowTexte();
		st.init(dis, fnam, "boerblatt");
		
	}
	
}
