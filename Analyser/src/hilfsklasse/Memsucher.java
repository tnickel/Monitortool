package hilfsklasse;

import objects.KeyDbObj;

public class Memsucher
{
	private String mem = null;
	private int memlen_glob = 0;
	private String filedat = null;

	public Memsucher(String fnam)
	{
		if (FileAccess.FileAvailable(fnam) == false)
		{
			Tracer.WriteTrace(20, "Error: no file<" + fnam + ">");
			return;
		}
		Inf inf = new Inf();
		inf.setFilename(fnam);
		String mem1 = inf.readMemFile();
		mem = mem1.toLowerCase();
		inf.close();
		memlen_glob = mem.length();

		endferneDelimiter();
		
		String filenam = fnam.substring(fnam.lastIndexOf("\\") + 1);
		filedat = new String(filenam.substring(0, filenam.indexOf("_")));
	}

	public int getlen()
	{
		return memlen_glob;
	}

	public Boolean lookKeyword(KeyDbObj keyobj)
	{
		String suchwort = keyobj.getKeyword();

		// schaut ob das suchwort vorkommt
		if (mem == null)
			Tracer.WriteTrace(10, "Error: internal mem=0");

		int suchtype = keyobj.getType();

		if (suchtype == 0)
		{
			// Schlüsselwort kommt vor
			if (mem.contains(suchwort.toLowerCase()) == false)
				return false;
			else
				return true;
		} else if (suchtype == 1)
		{
			// Type1: Eine Stelle vor dem Suchwort ist es leer
			char c1 = ' ';
			int pos1 = 0;

			// Falls das Suchwort überhaupt nicht im text=> false
			pos1 = mem.indexOf(suchwort.toLowerCase());
			if (pos1 < 0)
				return false;

			// prüft das Zeichen vor dem Suchwort
			if (pos1 > 1)
			{
				c1 = mem.charAt(pos1 - 1);
				if (Character.isLetter(c1))
					return false;
			}
			return true;
		} else if (suchtype == 2)
		{
			// Prüft ob das Suchwort exakt dies wort ist. Also davor und danach
			// kein Zeichen mehr ist

			// c1= Zeichen davor, c2=Zeichen danach
			char c1 = ' ', c2 = ' ';
			int pos1 = 0, pos2 = 0;
			int endpos = mem.length();

			// Falls das Suchwort überhaupt nicht im text=> false
			pos1 = mem.indexOf(suchwort.toLowerCase());
			if (pos1 < 0)
				return false;

			// prüft das Zeichen hinter dem Suchwort
			// pos2= position hinter dem suchwort
			pos2 = pos1 + suchwort.length();
			if (pos2 < endpos)
			{
				c2 = mem.charAt(pos2);
				// Falls das gesuchte Wort ein Teilwort eines anderen Wortes ist
				// dann false
				if (Character.isLetter(c2))
					return false;
			}

			// prüft das Zeichen vor dem Suchwort
			if (pos1 > 1)
			{
				c1 = mem.charAt(pos1 - 1);
				if (Character.isLetter(c1))
					return false;
			}
		} else
			Tracer.WriteTrace(10, "Error: internal, unbekannter suchtype");
		return false;
	}

	public Boolean checkFiledat(String mindat)
	{
		// schaut ob das filedat neuer als das suchdat ist
		if ((Tools.datum_ist_aelter_gleich(mindat, filedat)) == false)
			return false;
		return true;
	}

	public void endferneDelimiter()
	{
		// delimiterposition suchen
		String md = "-----mime delimiter---";

		if (mem.contains(md))
		{
			int pos1 = mem.indexOf(md);
			int pos2 = pos1 + md.length() + 15;
			
			if(pos2>mem.length()-1)
				pos2=mem.length()-1;
			
			String killword = mem.substring(pos1 + md.length(), pos2);

			// rausschneiden
			mem=mem.replace(killword, "***Delimiter removed***");
		}
	}

}
