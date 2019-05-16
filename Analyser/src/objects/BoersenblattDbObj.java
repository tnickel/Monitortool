package objects;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Tools;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;
import interfaces.DBObject;

import java.util.ArrayList;

import mainPackage.GC;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


public class BoersenblattDbObj extends Obj implements DBObject
{
	//Hier wird genau ein BB verwaltet
	private String boerblattname=null;
	private String fname_abs=null;
	private String lastread="";
	
	
	
	XStream xstream = new XStream(new DomDriver());
	
	//Diese Liste  beinhaltet sämtliche Texte des BB
	private  ArrayList<Keyword> keylist = new ArrayList<Keyword>();

	public int getThreadid()
	{
		return 0;
	}
	public BoersenblattDbObj(String zeile)
	{
		//maxdays = maximale haltedauer in tagen des keywortes
		
		int anz = Tools.countZeichen(zeile, "#");
		if ((anz < 1) || (anz > 2))
		{
			System.out.println(BoersenblattDbObj.class.getName()
					+ ":ERROR:threadname fehlerhaft in boersenblattdb.db zeile=<"
					+ zeile + "> anz<" + anz + ">");
			Tracer.WriteTrace(10, BoersenblattDbObj.class.getName()
					+ ":ERROR:threadname fehlerhaft in boersenblattdb.db zeile=<"
					+ zeile + ">");
			return;
		}
		int pos = 1;
		try
		{
			boerblattname= new String(Tools.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			fname_abs = new String(Tools.nteilstring(zeile, "#", pos));
			pos = pos + 1;
			lastread=new String(Tools.nteilstring(zeile, "#", pos));
			pos = pos + 1;
		} catch (ToolsException e)
		{
			Tracer.WriteTrace(10, BoersenblattDbObj.class.getName()
					+ ":ERROR:nteilstring exception<" + zeile + "> pos<" + pos
					+ " >not found>");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		readExtension();
	}
	public String toString()
	{
		return (boerblattname+"#"+fname_abs+"#"+lastread  );
	}

	public String GetSaveInfostring()
	{
		return "boerblattname#fname#lastread";
	}

	public void readExtension()
	{
		String fna1=fname_abs.substring(fname_abs.lastIndexOf("\\")+1);
		String fnam=GC.rootpath+"\\sublisten\\Boersenblatt\\"+fna1;

		if(FileAccess.FileAvailable(fnam))
		{

			System.out.println("read Extensions von file<"+fnam+">");
			//Baut die Keywortliste auf
			//String zeile=null;
			Inf inf = new Inf();
			inf.setFilename(fnam);
			//zeile=inf.readZeile();
		
			
			
			
			//keylist einlesen
			keylist=(ArrayList<Keyword>)inf.loadXST();

			//gehe durch die Keyliste und entferne Schlüsselwörter die älter als 3 Monate sind
			int anz=keylist.size();
			String aktdat=Tools.entferneZeit(Tools.get_aktdatetime_str());
			
			for(int i=0; i<anz; i++)
			{
				Keyword kw=keylist.get(i);
				String date=Tools.entferneZeit(kw.getAufdatum());
				
				int zeitdiff=Tools.zeitdifferenz_tage(date, aktdat);
				if(zeitdiff>90)
				{
					Tracer.WriteTrace(20, "Info:Suchwort<"+kw.getKeyword()+"> wird entfernt da älter als 90 Tage adate<"+date+">");
					keylist.remove(i);
					anz--;
				}
				
			}
			
			
			
			
			//keylist =  (ArrayList<Keyword>)xstream.fromXML(zeile);
			inf.close();
		}
	}
	public void postprocess()
	{
		String fna1=fname_abs.substring(fname_abs.lastIndexOf("\\")+1);
		String fnam=GC.rootpath+"\\sublisten\\Boersenblatt\\"+fna1;
		String fnamuebersicht=GC.rootpath+"\\sublisten\\uebersicht_Boersenblätter.txt";
		
		//save extensions
		
		{
			//Baut die Boerblattliste auf
						
			if(FileAccess.FileAvailable(fnam))
				FileAccess.FileDelete(fnam, 0);
			
			//beim postprocess alte keywörter rausschmeissen
			
			
			if(keylist.size()==0)
				return;
			
			Inf inf = new Inf();
			inf.setFilename(fnam);
			inf.saveXST(keylist);
			inf.close();
			
			//Schreibe übersicht
			inf= new Inf();
			inf.setFilename(fnamuebersicht);
			
			inf.writezeile(fna1+"#"+keylist.size()+"#");
			
			String info="";
			for(int i=0; i<keylist.size(); i++)
			{
				info=info.concat(keylist.get(i).getKeyword()+ "#");
			}
			inf.writezeile(info);
			inf.close();
		}
	}
	public String getBoerblattname()
	{
		return boerblattname;
	}
	public void setBoerblattname(String boerblattname)
	{
		this.boerblattname = boerblattname;
	}
	public String getFname()
	{
		return fname_abs;
	}
	public void setFname(String fname)
	{
		this.fname_abs = fname;
	}
	
	public String getLastread()
	{
		return lastread;
	}
	public void setLastread(String lastread)
	{
		this.lastread = lastread;
	}
	public String calcMsgDate()
	{
		String fnam2=fname_abs.substring(fname_abs.lastIndexOf("\\")+1);
		String msgdate = fnam2.substring(0, fnam2.indexOf("_"));
		return msgdate;
	}
	public static String calcMsgDate(String fnam)
	{
		String fnam2=fnam.substring(fnam.lastIndexOf("\\")+1);
		String msgdate = fnam2.substring(0, fnam2.indexOf("_"));
		return msgdate;
	}
	/**************************************************************************/
	public boolean checkKeyword(String keyword)
	{
		if(keyword.equalsIgnoreCase("SSE")==true)
			System.out.println("1check sse");
		
		int anz=keylist.size();
		
		for(int i=0; i<anz; i++)
		{
			Keyword kw=keylist.get(i);
			String keystr=kw.getKeyword();
			
			//keyword ist drin -> true
			if(keystr.equalsIgnoreCase(keyword))
			{
				if(keyword.equalsIgnoreCase("SSE")==true)
					System.out.println("2sse ist drin!!");
				return true;
			}
		}
		if(keyword.equalsIgnoreCase("SSE")==true)
			System.out.println("2sse nicht drin!!");
		
		return false;
	}
	
	public String calcAllKeyString()
	{
		//Liefert einen String aller Keywörter zurück
		int anz=keylist.size();
		String ks="";
		
		for(int i=0; i<anz; i++)
		{
			Keyword kw=keylist.get(i);
			String keystr=kw.getKeyword();
			
			ks=ks.concat(keystr+";");
		}
		return ks;
	}
	
	public ArrayList<Keyword> getKeylist()
	{
		return keylist;
	}
	
	public void addKeyword(KeyDbObj keyobj)
	{
		String keyword=keyobj.getKeyword();
		int type=keyobj.getType();
		
		//falls Keyword schon drin dann fehler
		if(checkKeyword(keyword)==true)
			Tracer.WriteTrace(10, "Keyword schon drin in boersenblattdb kw<"+keyword+">");

		Keyword kw = new Keyword();
		kw.setKeyword(keyword);
		kw.setType(type);
		kw.setAufdatum(Tools.get_aktdatetime_str());
		//Task:Das keyword wird noch um das Aufnahmedatum erweitert
		//man möchte ja wissen wann das letzte Mal das Keyword hinzugekommen ist
		
		keylist.add(kw);
	}
	public int calcKeywordanzahl()
	{
		return(keylist.size());
	}
	
}
