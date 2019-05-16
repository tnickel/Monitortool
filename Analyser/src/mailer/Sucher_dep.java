package mailer;

import hilfsklasse.FileAccess;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;

public class Sucher_dep
{
	//Die Klasse dient zum Suchen von Schlüsselwörtern in dateien
	
	
	protected Boolean SucheSchluesselwortFile(String filenamsearch,
			String schluesselwort, String mindat,int keineTeilwoerterflag)
	{
		// Hier wird ein File auf ein Schlüsselwort durchsucht
		// Welche schlüsselwörter kommen vor ?, das Ergebniss wird in der
		// Suchliste vermerkt
		String filedat=null;
		
		String filenam = filenamsearch.substring(filenamsearch.lastIndexOf("\\") + 1);

		if(filenam.contains("_")==false)
		{
			Tracer.WriteTrace(20, "Warning: fehlerhaftes extrahiertes pdf <"+filenam+">");
			return false;
		}
			
			filedat = filenam.substring(0, filenam.indexOf("_"));

	
		
		// Step 1: schaue nur in den neuen falls,falls file zu alt dann nix
		if ((Tools.datum_ist_aelter_gleich(mindat, filedat)) == false)
			return false;

		// Step 2: wenn suchwort gewünscht und nicht vorkommt dann false
		if (FileAccess.CheckFileKeyword(filenamsearch, schluesselwort,keineTeilwoerterflag) == false)
			return false;
		
		return true;

	}
}
