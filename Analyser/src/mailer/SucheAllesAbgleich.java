package mailer;

import hilfsklasse.FileAccessDyn;
import hilfsklasse.Memsucher;
import hilfsklasse.Tracer;
import mainPackage.GC;
import objects.BoersenblattDbObj;
import objects.KeyDbObj;
import stores.BoersenBlaetterDB;
import stores.KeyDB;
import stores.ThreadsDB;

public class SucheAllesAbgleich extends Sucher
{	
	//das ist die DB wo die ergebnisse drin sind
	private BoersenBlaetterDB boerblattdb_glob=new BoersenBlaetterDB();

	//keydb ist die db mit den suchwörtern
	private KeyDB kdb= new KeyDB();
	
	//hier werden für alle Schlüsselwörter geschaut in welchen 
	//Mails die sind
	//Es werden dann auch die Datenbasen abgeglichen
	//Für jedes Datenfile existiert ein Eintrag in BoersenBlaetterDB
	public void sucheAlles(ThreadsDB tdb,String mindat)
	{
		//gehe durch jedes File und schaue ob die Suchwörter drin sind
		//Wenn ja wird dies in Boerblatt.db gemerkt
		
		//der Hitcounter zeigt an wie oft jedes keywort vorkommt
		kdb.clearHitcounter();
		
		FileAccessDyn fdyn = new FileAccessDyn();
		fdyn.initFileSystemList(GC.textzielbase, 0);

		// gehe durch alle Verzeichnisse
		int anz = fdyn.holeFileAnz();
		
		for (int i = 0; i < anz; i++)
		{
			String fnam = fdyn.holeFileSystemName();
			System.out.println("Betrachte Verzeichniss("+i+"|"+anz+")<"+fnam+">");
		
			//Durchsucht ein Verzeichniss nach Keywörtern
			checkVerzeichnisL(fnam,  mindat,kdb,tdb);
		}
		
		tdb.WriteDB();
	}
	
	private void checkVerzeichnisL(String verznam,String mindat,KeyDB kdb,ThreadsDB tdb)
	{
		//überprüft alle Files in dem Verzeichniss auf die Keywörter der Keydb
		FileAccessDyn fdyn2 = new FileAccessDyn();
		fdyn2.initFileSystemList(GC.textzielbase + "\\" + verznam, 1);
		int anz = fdyn2.holeFileAnz();
		int geshitzaehler=0;

		//gehe durch alle files in einem Verzeichniss
		for (int i = 0; i < anz; i++)
		{
			System.out.print(","+i+"");
			//Überprüft einen Dateinamen nach Keywörtern
			String pdfnam = fdyn2.holeFileSystemName();
			String fnamtext = GC.textzielbase + "\\" + verznam + "\\" + pdfnam;
			Memsucher mem=new Memsucher(fnamtext);
			
			if(mem.checkFiledat(mindat)==false)
				continue;
			
			//gehe durch alle Schlüsselwörter
			int kanz=kdb.GetanzObj();
			for(int j=0; j<kanz; j++)
			{
				KeyDbObj keyobj=(KeyDbObj)kdb.GetObjectIDX(j);
				
				
				if(j%100==0)
					System.out.print(".");

				//falls das Schluessenwort in dem datenfile ist
				if((keyobj.getKeyword().length()>1)&&(mem.lookKeyword(keyobj))==true)
				{
					//nimm das schlüsselwort auf
					boerblattdb_glob.AddKeyword(verznam,fnamtext,keyobj);
					
					//Erhöhe den Hitcounter, 
					keyobj.setHitcounter((keyobj.getHitcounter())+1);
					geshitzaehler++;
					
					//Markiere in tdb das das Börsenblatt eine Mid pusched
					int mid=keyobj.getMid();
					String lastpusch=BoersenblattDbObj.calcMsgDate(fnamtext);
					tdb.setBBlastPusch(mid,lastpusch);
				}
				System.out.println("");
			}
			Tracer.WriteTrace(20, "Info: geshitzaehler<"+geshitzaehler+">");
			boerblattdb_glob.WriteDB();
			kdb.WriteDB();
		}
		System.out.println("");
	}
}
