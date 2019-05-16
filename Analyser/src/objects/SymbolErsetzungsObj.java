package objects;

import hilfsklasse.Inf;
import hilfsklasse.SG;
import hilfsklasse.Tools;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;
import interfaces.DBObject;
import mainPackage.GC;
import stores.ThreadsDB;

public class SymbolErsetzungsObj extends Obj implements DBObject
{
	// so ein symbolErsetzungsObjekt gilt immer für ein Symbol
	// die Masterid dient nur zu kontrolle
	// jedes Symbol darf nur ein einziges Mal in allen Symbolersetzungen
	// vorkommen

	private int masterid_tabelle;
	private String[] Symbol = new String[3];
	private String[] boerse = new String[3];
	private String[] startdat = new String[3];
	private String[] enddat = new String[3];
	private String lastchange;

	// intern
	private int symbolanz = 0;

	public int compareTo(Obj argument)
	{
		return 0;
	}

	public SymbolErsetzungsObj(String zeile)
	{
		if (SG.countZeichen(zeile, "#") != 13)
		{

			Tracer.WriteTrace(10, ":ERROR:symbolliste fehlerhaft zeile=<"
					+ zeile + ">");
			return;
		}
		int pos = 1;
		try
		{
			masterid_tabelle = Integer.valueOf(SG.nteilstring(zeile, "#", pos));
			pos = pos + 1;

			for (int j = 0; j < 3; j++)
			{
				Symbol[j] = new String(SG.nteilstring(zeile, "#", pos));
				pos = pos + 1;
				if (Symbol[j].length() > 1)
					symbolanz++;
				boerse[j] = new String(SG.nteilstring(zeile, "#", pos));
				pos = pos + 1;
				startdat[j] = new String(SG.nteilstring(zeile, "#", pos));
				pos = pos + 1;
				enddat[j] = new String(SG.nteilstring(zeile, "#", pos));
				pos = pos + 1;
			}
			lastchange = new String(SG.nteilstring(zeile, "#", pos));

		} catch (ToolsException e)
		{
			Tracer.WriteTrace(10, ":ERROR:nteilstring exception<" + zeile
					+ "> pos<" + pos + " >not found>");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getAnzSymbole()
	{
		// schaut nach wieviele Symbole dieses objekt hat,
		// maximal 5 möglich
		int anz = 0;
		for (int i = 0; i < 3; i++)
			if (Symbol[i].length() >= 2)
				anz++;
		return anz;
	}

	public int getThreadid()
	{
		return 0;
	}

	public int getMasterid()
	{
		return masterid_tabelle;
	}

	public void setMasterid(int masterid)
	{
		this.masterid_tabelle = masterid;
	}

	public String getLastchange()
	{
		return lastchange;
	}

	public void setLastchange(String lastchange)
	{
		this.lastchange = lastchange;
	}

	public String getSymbol(int index)
	{
		return Symbol[index];
	}

	public boolean setSymbol(int index, String symb)
	{
		Symbol[index] = new String(symb);
		return true;
	}

	public boolean hasSymbol(String symb)
	{
		int anz = getAnzSymbole();
		if (anz == 0)
			return false;
		for (int i = 0; i < anz; i++)
		{
			if (Symbol[i] != null)
			{
				if (Symbol[i].equalsIgnoreCase(symb) == true)
					return true;
			}
		}
		return false;
	}

	void GenFehlerReportingliste(int mid1, int mid2, String nsymb,
			String tabellenstring)
	{
		ThreadsDB tdb = new ThreadsDB();
		int anz = tdb.GetanzObj();

		// die Reportingliste versucht zu klären welche masterid zu welchen
		// Symbol gehört
		Inf inf = new Inf();
		inf.setFilename(GC.rootpath + "\\db\\reporting\\SymbolErrorRep.txt");
		inf.writezeile("****" + Tools.get_aktdatetime_str()
				+ "*******************************");
		inf.writezeile("Tabelle<" + tabellenstring + ">");
		inf.writezeile("midneu<" + mid1
				+ "> wird in threads.db gesucht____________________________");
		for (int i = 0; i < anz; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) tdb.GetObjectIDX(i);
			if (tdbo.getMasterid() == mid1)
				inf.writezeile("tid<" + tdbo.getThreadid() + "> mid<"
						+ tdbo.getMasterid() + "> symb<" + tdbo.getSymbol()
						+ "> tnam<" + tdbo.getThreadname() + ">");
		}
		inf.writezeile("midtabelle<" + mid2
				+ "> wird in threads.db gesucht________________________");
		for (int i = 0; i < anz; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) tdb.GetObjectIDX(i);
			if (tdbo.getMasterid() == mid2)
				inf.writezeile("tid<" + tdbo.getThreadid() + "> mid<"
						+ tdbo.getMasterid() + "> symb<" + tdbo.getSymbol()
						+ "> tnam<" + tdbo.getThreadname() + ">");
		}
		inf.writezeile("neues symbol<" + nsymb
				+ "> wird in threads.db gesucht_____________________");
		for (int i = 0; i < anz; i++)
		{
			ThreadDbObj tdbo = (ThreadDbObj) tdb.GetObjectIDX(i);
			if (tdbo.getSymbol().equals(nsymb))
				inf.writezeile("tid<" + tdbo.getThreadid() + "> mid<"
						+ tdbo.getMasterid() + "> symb<" + tdbo.getSymbol()
						+ "> tnam<" + tdbo.getThreadname() + ">");
		}
		inf
				.writezeile("Ende*******************************************************************");
	}

	public boolean addNewSymbol(int mid, String nsymb, String nboer)
	{
		// mid: masterid
		// nsymb: neues symbol
		// nboer: neue Börse

		// die masterid muss auf jeden Fall gleich sein wenn ein neues Symbol
		// kommt
		// Das Symbol wurde möglicherweise unter einer anderen Mid gespeichert->
		// führt zu problemen
		// Jedes Symbol hat eine eindeutige Masterid
		if (mid != masterid_tabelle)
		{
			Tracer.WriteTrace(20,
					"Error: Symbolersetzungfehler  => Korrektur von Hand !!!");
			Tracer.WriteTrace(20,
					"Schaue logfile $Root/db/reporting/SymbolErrorRep.txt");
			Tracer.WriteTrace(20, "Generiere Reportingliste aus threads.db");

			GenFehlerReportingliste(mid, masterid_tabelle, nsymb, this
					.toString());

			Tracer.WriteTrace(20,
					
					"Error: Internal Error Mid passt nicht zum Symbol: midneu<"
							+ mid + "> masterid in tabelle<" + masterid_tabelle
							+ "> nsymb<" + nsymb + "> symb-tabelle<"
							+ this.toString() + ">");
			return false;
		}
		for (int j = 0; j < symbolanz; j++)
		{
			// falls symbol schon drin
			if (Symbol[j].equals(nsymb) == true)
				Tracer.WriteTrace(10, "Error: Internal nsymb<" + nsymb
						+ "> schon in der Symboldatenbank tostring<"
						+ this.toString() + ">");
			return false;
		}
		// nimm auf
		symbolanz++;

		if (symbolanz == 3)
			Tracer.WriteTrace(10, "Error: Internal Symboltabelle voll symb<"
					+ this.toString() + "> anz<" + symbolanz + ">");

		Symbol[symbolanz] = new String(nsymb);
		boerse[symbolanz] = new String(nboer);
		startdat[symbolanz] = new String("x");
		enddat[symbolanz] = new String("x");
		return true;
	}

	public String getBoerse(int index)
	{
		return boerse[index];
	}

	public boolean setBoerse(int index, String symb)
	{
		boerse[index] = new String(symb);
		return true;
	}

	public String getStartdat(int index)
	{
		return startdat[index];
	}

	public boolean setStartdat(int index, String symb)
	{
		startdat[index] = new String(symb);
		return true;
	}

	public String getEnddat(int index)
	{
		return enddat[index];
	}

	public boolean setEnddat(int index, String symb)
	{
		enddat[index] = new String(symb);
		return true;
	}

	@Override
	public String toString()
	{
		String retstr = new String("");
		if (masterid_tabelle == 14662)
			System.out.println("found salzgitter");

		retstr = retstr.concat(Integer.toString(masterid_tabelle));
		retstr = retstr.concat("#");
		for (int i = 0; i < 3; i++)
			retstr = retstr.concat(Symbol[i] + "#" + boerse[i] + "#"
					+ startdat[i] + "#" + enddat[i] + "#");
		retstr = retstr.concat(lastchange);
		return new String(retstr);
	}

	@Override
	public String GetSaveInfostring()
	{
		return "masterid#Symbol1#Boerse1#Startdat1#Enddat1#Symbol2#Boerse2#Startdat2#Enddat2#Symbol3#Boerse3#Startdat3#Enddat3#lastchange";
	}
	public void postprocess()
	{}
}
