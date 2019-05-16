package hilfsklasse;

import java.io.File;
import java.util.ArrayList;

import ComData.FileAccess;
import ComData.GC;
import ComData.Inf;

import objects.Obj;
import objects.ThreadDbObj;

public class IsValid
{
	static public boolean Thread(String fnamz)
	{
		if (fnamz.contains("0279"))
		{
			Tracer.WriteTrace(20,
					"W:Threadid 0279 not allowed go on !!! (do nothing)");
			return false;
		} else
			return true;
	}

	static public boolean isValidUsername(String username)
	{
		if ((username.contains("?") == true)
				|| (username.contains("#") == true)
				|| (username.contains("/") == true)
				|| (username.contains("<") == true)
				|| (username.contains(">") == true))
			return false;
		else
			return true;
	}

	static public boolean checkUserobjektName(String zeile)
	{
		if ((zeile.equals("#0#null#0#0#0#0") == true)
				|| (zeile.contains("\\") == true)
				// || (zeile.contains(":") == true)
				|| (zeile.contains(")") == true)
				|| (zeile.contains("(") == true)
				|| (zeile.contains("pinguin") == true)
				|| (zeile.contains("/") == true))
			return false;
		else
			return true;
	}

	static public boolean checkFilesystemname(String filename)
	{
		// prüft ob der Filenamen für das Usersystem geeignet ist
		if (filename == null)
		{
			Tracer.WriteTrace(20, "Warning: checkFilesystemname = null");
			return false;
		}
		if ((filename.contains("#") == true)||(filename.contains(":")==true)||(filename.contains("?")==true))
		{
			Tracer.WriteTrace(20, "Warning: checkFilesystemname <" + filename
					+ ">");
			return false;
		}

		
		//Filecheck
		Inf inf = new Inf();
		String fnam = GC.rootpath + "\\tmp\\filetest\\" + filename + ".txt";
		inf.setFilename(fnam);
		inf.writezeile("testtesttesttesttest");
		if (FileAccess.FileLength(fnam) < 10)
		{
			FileAccess.FileDelete(fnam,0);
			return false;
		} 
		FileAccess.FileDelete(fnam,0);
		
		//Directorycheck
		File fnamdir=new File(GC.rootpath + "\\tmp\\filetest\\" + filename);
		fnamdir.mkdir();
		
		if(fnamdir.isDirectory()==false)
		{
			fnamdir.delete();
			Tracer.WriteTrace(20, "Warning: checkFilesystemnameDIR <" + fnamdir.toString()
					+ ">");
			return false;
		}
		fnamdir.delete();
		
		//alle checks durchlaufen
		return true;
		
	}

	static public String BenutzernameNachverarbeitung(String z)
	// Der aus GetBenutzername gewonne Benutzername wird nachverarbeitet
	// z.B. [VIP] aus dem Benutzernamen entfernt und evtl. Sonderzeichen etc.
	{
		if (z.contains("<") == true)
		{
			return (z.substring(0, z.indexOf("<")));
		} else if (z.contains("*") == true)
		{
			Tracer.WriteTrace(20, "Warning: Benuterzname hat Sonderzeichen <"
					+ z + ">  und wird bereinigt");
			return (z.substring(0, z.indexOf("*")));
		} else
			return z;
	}
	
	static public String ThreadnameNachverarbeitung(String z)
	{
		if (z.contains("?") == true)
		{
			String nstr=z.replaceAll("?", "");
			
			Tracer.WriteTrace(20, "Warning: Thraename hat Sonderzeichen <"
					+ z + ">  und wird bereinigt als <"+nstr+">");
			return (nstr);
		} 
		return z;
	}
	
	static public boolean threadname(ArrayList<Obj> dbliste,ThreadDbObj tdbo)
	{
		if(tdbo.getThreadname().contains("?")==true)
		{
			Tracer.WriteTrace(20, "W:Threadname is not valid<"+tdbo.getThreadname()+">, drop it");
			return false;
		}
		
		if ((tdbo.getPageanz() > 30000)
				|| (tdbo
						.getThreadname()
						.contains(
								"wallstreet-online User stellen Weltrekord auf") == true))
		{
			Tracer.WriteTrace(20, "W:Threadname <"
					+ tdbo.getThreadname() + "> Symb<"
					+ tdbo.getSymbol() + "> Pageanz<"
					+ tdbo.getPageanz() + "> to big ->drop it");
			return false;
		}
		//prüft jetzt ob der Threadname schon vorkommt
		String tnam=tdbo.getThreadname();
		
		int anz=dbliste.size();
		for(int i=0; i<anz; i++)
		{
			ThreadDbObj tdbo2=(ThreadDbObj)dbliste.get(i);
			String tnam2=tdbo2.getThreadname();
			
			if(tnam.equals(tnam2)==true)
				Tracer.WriteTrace(10, "Error: internal tnam<"+tnam+"> kann nicht zur tdb hinzugefügt werden da schon vorhanden tnam2<"+tnam2+">!!");
		}
		
		return true;
	}
}
