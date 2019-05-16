package data;

import gui.Mbox;
import hilfsklasse.FileAccess;
import hilfsklasse.SG;

import java.util.ArrayList;


public class DropMagicliste
{
	// delete magicliste
	private ArrayList<Integer> delmaglist = new ArrayList<Integer>();

	// diese Klasse ist für das bearbeiten von listen von magics zuständig
	// wie z.B. löschen von files mit einer bestimmten magic

	public DropMagicliste(String verz)
	{
		// liest alle magicnummern in eine arraylist ein
		FileAccess.initFileSystemList(verz, 1);
		int anz = FileAccess.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String fnam = FileAccess.holeFileSystemName();
			if (fnam.endsWith(".del"))
			{
				int fmagic = calcMagic(fnam);
				delmaglist.add(fmagic);
			}
		}
	}

	public boolean checkDeleteFile(String fnam)
	{
		// prüft ob das File gelöscht werden soll
		// Ein File darf gelöscht werden, wenn die magic dieses files in der
		// delmaglist ist

		// aus dem filenamen die magic holen
		int fmagic = calcMagic(fnam);

		// prüfen und rückmelden ob die magic in der delmagiclist ist
		return (checkIsInDelmaglist(fmagic));

	}

	private boolean checkIsInDelmaglist(int magic)
	{
		// prüft ob magic in der delmaglist ist
		int anz = delmaglist.size();
		for (int i = 0; i < anz; i++)
		{
			if (delmaglist.get(i) == magic)
				return true;
		}
		return false;
	}

	private int calcMagic(String quellnam)
	{
		// B23 EURUSD M5_Strategy 454.6035.mq4
		// die Magic muss am ende stehen
		// Bei sowas 454.6035_optimized.mq4
		// oder 454.6035optimized wird ebenfalls die zahl extrahiert

		//sonderfall des .del falles abfragen
		if (quellnam.endsWith(".del"))
		{
			return (SG.get_zahl(quellnam.substring(0, quellnam.indexOf("."))));
		}

		if (quellnam.contains(" ") == false)
			return -99;

		String keyword = quellnam.substring(quellnam.lastIndexOf(" "),
				quellnam.lastIndexOf("."));

		keyword = keyword.replace(".", "");

		// die führenden nullen entfernen
		while ((keyword.startsWith("0")) || (keyword.startsWith(" ")))
			keyword = keyword.substring(1, keyword.length());

		//falls das keywort zu lang ist sind schmutzzeichen drin
		if (keyword.length() > 9)
		{
			int min=0;
			int max=keyword.length();
			
			for(int j=max; j>1; j--)
			{
				//schneide vorderen teil ab
				String subnumber=keyword.substring(0,j);
				//Mbox.Infobox("check substring<"+subnumber+">");
				if(isInteger(subnumber)==true)
				{
					//eine saubere Zahl konnte im vorderen Teil extrahiert werden, dann überprüfe hier nochmal
					//Mbox.Infobox("magickorrektor vorher<"+keyword+"> nachher<"+subnumber+">");
					return SG.get_zahl(subnumber);
				}
			}
			
			
			
			Mbox.Infobox("Magic  <" + keyword + "> too long max 9 digits");
		}
		return SG.get_zahl(keyword);
	}

	private boolean isInteger(String str) {
	    try {
	        Integer.parseInt(str);
	        return true;
	    } catch (NumberFormatException nfe) {}
	    return false;
	}
	
}
