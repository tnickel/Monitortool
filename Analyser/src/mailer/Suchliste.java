package mailer;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;

import java.util.ArrayList;
import java.util.List;

public class Suchliste
{
	// Beispiel für ein configfile:
	// Suchwort:Sunwin
	// Sunwin
	// A0D9SL
	// Suchwort:Allianz
	// Allianz
	// Suchwort:Gold
	// gold

	// Beispiel für eine foundliste
	// Commerzbank(oberbegriff), file1, file2, file3
	// Allianz(oberbegriff), filex, filey

	// Beispiel für foundpatterns
	// Commerzbank, [Alle foundpatterns zu commerzbank, über alle files] die
	// commerzbank ist gut, commerzbank mit der wkn
	// Allianz dito.

	// Maximal 1000 Suchbegriffe
	private List<String>[] suchliste = new List[1000];
	// Für jeden Suchbegriff wird festgehalten wo er vorkommt

	private List<Boerblatt>[] found = new List[1000];

	// ausliste ist die ausschlussliste, diese Liste beinhaltet die "verbotenen"
	// keyworter
	private List<String>[] ausliste = new List[1000];

	public Suchliste(String suchfile, String ausfile)
	{
		// Hier wird die suchliste aufgebaut
		// pos0:Oberbegriff, pos1...posn Suchwörter

		baueListe(suchliste, suchfile);
		baueListe(ausliste, ausfile);

	}

	private void baueListe(List<String>[] list, String file)
	{
		Inf inf = new Inf();
		inf.setFilename(file);

		// Zeilen und spaltenindex
		int zidx = -1;

		String line = inf.readZeile();
		while (line != null)
		{
			if (line.length() == 0)
				break;

			// neues Suchwort gefunden
			if (line.toLowerCase().contains("suchwort:") == true)
			{
				// ein neues Suchwort gefunden
				zidx++;
				String sw = line.substring(line.indexOf("Suchwort:") + 9);
				list[zidx] = new ArrayList<String>();
				list[zidx].add(sw);
				line = inf.readZeile();

				// hole das nächste Suchwort
				continue;
			}
			// hinter dem Suchwort reihe alles ein
			list[zidx].add(line);
			line = inf.readZeile();
		}
		inf.close();
	}

	/****************************************************************************/
	public int getHauptkeyanz()
	{
		int anz = suchliste.length;
		return anz;
	}

	public String getIDXHauptkey(int index)
	{
		// holt den iten hauptkey
		String hkey = suchliste[index].get(0);
		return hkey;
	}

	public int getSubkeyanz(int index)
	{
		int subkeyanz = suchliste[index].size() - 1;
		return subkeyanz;
	}

	public String getSubkey(int hauptindex, int subindex)
	{
		String subkey = suchliste[hauptindex].get(subindex);
		return subkey;
	}

	public int getResultanz(int index)
	{
		if (found == null)
			return 0;
		if (found[index] == null)
			return 0;

		int foundanz = found[index].size();
		return foundanz;
	}

	public String getResult(int hindex, int subindex)
	{
		String fnam = found[hindex].get(subindex).getFnam();
		return fnam;
	}

	public String getFoundpattern(int hindex, int subindex)
	{
		if (found[hindex] == null)
			return "x";
		if (found[hindex].get(subindex) == null)
			return "xx";

		Boerblatt bb = found[hindex].get(subindex);
		String pattern = bb.getPatternstring();
		return pattern;
	}

	public int getResultGesAnz()
	{
		// Holt die Gesammtanzahl der gefundenen Resultate
		int hauptkeyanz = getHauptkeyanz();
		int tanz = 0;
		for (int i = 0; i < hauptkeyanz; i++)
		{
			tanz = tanz + getResultanz(i);
		}
		return tanz;
	}

	/****************************************************************************/
	public void checkSuchwoerter(String fnam, int keineTeilwoerterflag,
			int ausflag)
	{
		// geht durch die Liste und schaut ob die suchwörter drin sind
		// Wenn ja, dann erstelle eine ergebnissliste

		int anz = suchliste.length;
		for (int suchhauptindex = 0; suchhauptindex < anz; suchhauptindex++)
		{
			// suche das keyword i

			// falls das Ende erreicht ist
			if (suchliste[suchhauptindex] == null)
				break;

			int anzw = suchliste[suchhauptindex].size();
			// suche ein einzelnes Suchwort, gehe durch die synonyme
			for (int suchunterindex = 1; suchunterindex < anzw; suchunterindex++)
			{
				// Nächstes Suchwort
				String suchwort = suchliste[suchhauptindex].get(suchunterindex);

				// ermittelt die Foundpatterns für ein file
				String foundstrings = FileAccess.GetFileKeywordStrings(fnam,
						suchwort, keineTeilwoerterflag, 20);

				// in dem file sind keine foundpatterns, also wurde nix gefunden
				if ((foundstrings == null) || (foundstrings.length() == 0))
					continue;

				// jetzt muss noch geprüft werden ob die tmpfoundpatterns nicht
				// zum Ausschluss führen.
				// Bsp: suchhauptwort commerzbank
				// Unter commerzbank befinden sich die unterSuchwörter
				// commerz, wkn....

				// Es wurden gefunden
				// Die commerzbank ist gut, das zertifikat der commerbank

				// Die Ausschlussliste für commerzbank beinhaltet aber
				// "das zertifikat der"
				// => dies führt zum Ausschluss

				// Prüfung:
				// falls Ausschluss
				if ((ausflag == 1)
						&& (checkAusschluss(suchhauptindex, foundstrings) == true))
					continue;

				// falls was gefunden wurde, dann wird das gefundene gespeichert
				if (foundstrings.length() > 0)
				{
					// schluesselwort i kommt in dem Textfile vor, gehe weiter
					if (found[suchhauptindex] == null)
						found[suchhauptindex] = new ArrayList<Boerblatt>();

					Boerblatt bb = new Boerblatt();
					bb.setFnam(fnam);
					bb.setPattern(foundstrings);

					// addiere das suchergebniss, füge die Ergebnisse der
					// subkeys zusammen
					AddSuchergebniss(suchhauptindex, bb);

					System.out.println("i<" + suchhauptindex + "> j<"
							+ suchunterindex + "> fnam<" + fnam
							+ "> foundmuster<" + bb.getPatternstring()
							+ "> wurde hinzugefügt");
				}

			}

		}
	}

	private void AddSuchergebniss(int sindex, Boerblatt bb)
	{
		// Bsp:
		// Suchwort:commerzbank (Suchhauptwort)
		// commerz, wkn80728,....(Hier stehen die suchunterwörter)
		// das gefundene wird nach Prüfung zu der Liste der Ergebnisse
		// hinzugefügt

		// Prüfung:
		// Schaue nach ob der Filename schon drin ist,
		// wenn Filename schon da, dann erweitere nur um das pattern

		// Schaue nach ob für das suchwort schon Files gefunden wurden
		if (found[sindex].size() == 0)
		{
			// Für dieses suchwort wurden noch keine Files gefunden
			found[sindex].add(bb);
		} else
		{
			// das Suchwort wurde schon in verschiedenen Files gefunden
			// Schaue nach ob das Passende File schon in der Liste ist,
			// wenn Ja erweitere die Patterns

			Boerblatt bx = null;
			int npos = GetFileindex(sindex, bb);
			if (npos >= 0)
			{ // passendes file gefunden, dann füge die patterns zusammen
				bx = found[sindex].get(npos);
				String patt = bx.getPatternstring();
				String patt2 = bb.getPatternstring();
				patt = patt.concat("@@" + patt2);
				System.out.println("neues pattern=<" + patt + ">");

				// kontrolle

				System.out.println("kontrolle=<"
						+ found[sindex].get(npos).getPatternstring() + ">");
			} else
			{
				// file ist noch nicht drin, dann nimm so auf
				found[sindex].add(bb);
			}
		}

	}

	private int GetFileindex(int sindex, Boerblatt bb)
	{
		// schaut nach ob das File schon in der Foundliste ist.
		// Wenn ja dann liefere den Index zurück
		int anz = found[sindex].size();
		for (int l = 0; l < anz; l++)
		{
			Boerblatt bx = found[sindex].get(l);
			if (bx.getFnam().equalsIgnoreCase(bb.getFnam()) == true)
				return l; // filenamen gefunden
		}
		// nix gefunden
		return -1;
	}

	private boolean checkAusschluss(int index, String patterns)
	{
		// schaue nach ob es ein einziges foundpattern gibt für das es kein
		// Ausschlusswort gibt
		// d.h. if validpadanz>0
		// Bsp: patterns:commerzbank kaufen, zertifikat der commerzbank
		// ausliste:(0)commerzbank,zertifikat der
		// =>validpadanz =1

		String suchwort = suchliste[index].get(0);

		int anz = ausliste.length;
		for (int auslistpos = 0; auslistpos < anz; auslistpos++)
		{

			// ende erreicht
			if (ausliste[auslistpos] == null)
				return false;

			// a) Schaue ob für das Suchwort überhaupt eine Ausliste vorhanden
			if (ausliste[auslistpos].get(0).equalsIgnoreCase(suchwort) == true)
			{
				// Ausliste GEFUNDEN
				// für das suchwort gibt es verbotwörter

				if (checkIsInAusliste(patterns, ausliste[auslistpos]) == true)
				{
					return true;
				}

				return false;
			}
		}
		// b) Keine Ausliste für das Suchwort gefunden dann false
		return false;
	}

	private boolean checkIsInAusliste(String pattern, List<String> auslist)
	{
		int anz = auslist.size();
		for (int i = 1; i < anz; i++)
		{

			pattern = pattern.toLowerCase();
			String auselem = (String) auslist.get(i).toLowerCase();

			// falls pattern in auslist
			if (pattern.contains(auselem) == true)
				return true;
		}

		return false;
	}

	public void gibSuchwortlisteAus(String fnam)
	{
		// Hier werden die gefundenen Ergebnisse in ein Textfile ausgegeben
		Inf inf = new Inf();
		inf.setFilename(fnam);

		int anz = suchliste.length;
		for (int i = 0; i < anz; i++)
		{
			// gehe durch die Hauptkeywoerter
			if (suchliste[i] == null)
				break;

			String hauptkey = suchliste[i].get(0);

			inf.writezeile("Hauptkey<" + hauptkey + ">");
			int subkeys = suchliste[i].size();
			String substring = "";
			for (int j = 1; j < subkeys; j++)
			{
				String subkey = suchliste[i].get(j);
				substring = substring.concat(subkey + " # ");
			}
			inf.writezeile(substring);
			inf.writezeile("gefunden in:");

			if (found[i] == null)
			{
				inf.writezeile("______________________________");
				continue;
			}
			int foundanz = found[i].size();
			for (int j = 0; j < foundanz; j++)
			{
				String fnamout = found[i].get(j).getFnam();
				inf.writezeile(fnamout);
			}
			inf.writezeile("______________________________");
		}
	}

}
