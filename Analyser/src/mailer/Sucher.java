package mailer;

import hilfsklasse.FileAccess;
import hilfsklasse.Tools;

public class Sucher
{
	//Die Klasse dient zum Suchen von Schlüsselwörtern in dateien
	
	
	protected Boolean SucheSchluesselwortFile(String filenamsearch,
			String schluesselwort, String mindat,int keineTeilwoerterflag)
	{
		// Hier wird ein File auf ein Schlüsselwort durchsucht
		// Welche schlüsselwörter kommen vor ?, das Ergebniss wird in der
		// Suchliste vermerkt

		String filenam = filenamsearch.substring(filenamsearch.lastIndexOf("\\") + 1);
		String filedat = filenam.substring(0, filenam.indexOf("_"));

	
		
		// Step 1: schaue nur in den neuen falls,falls file zu alt dann nix
		if ((Tools.datum_ist_aelter_gleich(mindat, filedat)) == false)
			return false;

		// Step 2: wenn suchwort gewünscht und nicht vorkommt dann false
		if (FileAccess.CheckFileKeyword(filenamsearch, schluesselwort,keineTeilwoerterflag) == false)
			return false;
		
		return true;

	}
}
