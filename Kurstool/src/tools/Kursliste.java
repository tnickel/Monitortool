package tools;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.TDate;
import hilfsklasse.Tracer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;


public class Kursliste
{
	// das hashset dient zum schnelle überprüfen ob ein kurswert drin ist
	HashSet<String> datumsmenge = new HashSet<String>();
	// in kl sind die kurse
	ArrayList<Kurs1> kl = new ArrayList<Kurs1>();
	String globFilename = null;

	// datumsliste um die dati zu überprüfen
	private List<String> datumscheckliste = new ArrayList<String>();

	Kursliste(String file)
	{
		globFilename = file;

		if(FileAccess.FileAvailable(file)==false)
			Tracer.WriteTrace(10, "File <"+file+"> nicht vorhanden -> stop");
		
		if (FileAccess.FileAvailable(file))
			readAll();
	}

	public void readAll()
	{
		int count = 0;
		String zeil = null;
		Inf infquell = new Inf();
		infquell.setFilename(globFilename);

		while ((zeil = infquell.readZeile()) != null)
		{
			//prueft auf header
			if(zeil.contains("Time"))
				continue;
			
			
			//prüft ob duka format
			if(zeil.contains("\t")==true)
			{
				zeil=zeil.replaceAll(",", ".");
				zeil=zeil.replace("-", ".");
				zeil=zeil.replace("-", ".");
				zeil=zeil.replaceAll("\t", ",");
			}
				
			count++;
			if(count%1000==0)
				System.out.println("read<"+count+">");
			
			Kurs1 k1 = new Kurs1(zeil);
			kl.add(k1);
			/*if (count % 1000 == 0)
				System.out.println("counter<" + count + "> dat<"
						+ k1.getDatum() + ">");*/
		}
		infquell.close();

	}

	public void checkDateliste(String datelistenam, String datemessagefile)
	{
		// datelistnam: ist der name des files wo alle dati drin sind
		// datemessagefile: ist der name des files wo die fehlermeldungen
		// reinkommen

		int count = 0;
		String zeil = null;
		Inf infquell = new Inf();
		infquell.setFilename(datelistenam);

		Inf emsg = new Inf();
		emsg.setFilename(datemessagefile);

		while ((zeil = infquell.readZeile()) != null)
		{
			String dat = zeil.substring(0, zeil.indexOf("\t"));
			dat = dat.replace("-", ".");
			dat = dat.replace("-", ".");
			datumscheckliste.add(dat + ",00:00");

			/*if (count % 100 == 0)
				System.out.println("counter<" + count + "> dat<" + dat + ">");*/
		}
		infquell.close();

		// Jetzt wird überprüft ob jedes datum in dem gemergedten file vorkommt
		int anz = datumscheckliste.size();
		for (int i = 0; i < anz; i++)
		{
			String dat = datumscheckliste.get(i);
			dat=dat.substring(0,dat.indexOf(",00:00"));
			
		
			
			String dat2=dat.replace(".","-");
			dat2=dat2.replace(".","-");
			TDate tdat = new TDate(dat2);
			int day = tdat.getVal("wo");

			

			if ((day >=2) && (day <= 6))
			{
				if ((datumsmenge.contains(dat+",00:00")) == false)
				{
					

					// datum fehlt
					emsg.writezeile("Datum<" + dat + "> fehlt");
				}
			}

		}
	}

	public void writeMetatrader()
	{

		Inf infziel = new Inf();
		infziel.setFilename(globFilename);

		int anz = kl.size();
		for (int i = 0; i < anz; i++)
		{
			Kurs1 Kursx = kl.get(i);
			infziel.writezeile(Kursx.getZeile());
			/*if (i % 1000 == 0)
				System.out.println("i=" + i);*/
		}
		infziel.close();
	}

	public void writeFsb()
	{

		Inf infziel = new Inf();
		infziel.setFilename(globFilename);

		int anz = kl.size();
		for (int i = 0; i < anz; i++)
		{
			Kurs1 Kursx = kl.get(i);
			infziel.writezeile(Kursx.getFsbZeile());
			/*if (i % 1000 == 0)
				System.out.println("i=" + i);*/
		}
		infziel.close();
	}
	
	public void writeNinja(String fnam)
	{

		Inf infziel = new Inf();
		infziel.setFilename(fnam);

		int anz = kl.size();
		for (int i = 0; i < anz; i++)
		{
			Kurs1 Kursx = kl.get(i);
			infziel.writezeile(Kursx.getFsbZeile());
			/*if (i % 1000 == 0)
				System.out.println("i=" + i);*/
		}
		infziel.close();
	}

	public void writeDukaTick(String fnam)
	{
		String uhrzeit="";
		Inf infziel = new Inf();
		infziel.setFilename(fnam);
		Random ran= new Random();
		ran.setSeed(5555555);
		
		
		//letzte Zufallszahl
		int lastval=0;
		//zufallszahl die der Zeit angehangen wird
		int rand=0; 
		
		int anz = kl.size();
		for (int i = 0; i < anz; i++)
		{
			String lastuhrzeit=uhrzeit;
			Kurs1 Kursx = kl.get(i);
			uhrzeit=Kursx.getDatum();

			if(i%1000==0)
				System.out.println("Write<"+i+">");
			
			if(uhrzeit.equalsIgnoreCase(lastuhrzeit)==false)
			{
				//neue Minute
	
				lastval=0;
				//erste zahl 100 bis 300 ms
				rand=ran.nextInt(200)+100;
				lastval=rand;
			}
			else
			{   
				//gleiche Minute, aber nächster Tick
				//100 bis 300 millisekunden weiter
				int neurand=ran.nextInt(200)+100;
				
				rand=(lastval+neurand)%1000;
				lastval=rand;
			}
			
		
			
			infziel.writezeile(Kursx.getDukaTickZeile(rand));
			/*if (i % 1000 == 0)
				System.out.println("i=" + i);*/
		}
		infziel.close();
	}
	
	
	public boolean hasValue(String datum)
	{
		if (datumsmenge != null)
			if (datumsmenge.contains(datum) == true)
				return true;

		return false;

	}

	public Kurs1 getValue(String datum)
	{

		int anz = kl.size();

		for (int i = 0; i < anz; anz++)
		{
			Kurs1 k1 = kl.get(i);
			if (k1.getDatum().equalsIgnoreCase(datum) == true)
				return k1;
		}
		return null;

	}

	public boolean addValue(Kurs1 k1)
	{

		String dat = k1.getDatum();

		

		if (this.hasValue(dat) == false)
		{
			kl.add(k1);
			datumsmenge.add(k1.getDatum());
			return true;
		} else
			return false;
	}

	public int getAnz()
	{
		return kl.size();
	}

	public Kurs1 getIDX(int index)
	{
		int anz = kl.size();
		if (index == anz)
			return null;

		Kurs1 k = kl.get(index);
		return k;
	}
}
