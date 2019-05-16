import hilfsklasse.FileAccess;
import hilfsklasse.Tracer;
import tools.Kursworker;

public class main
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		//mergeKurse();
		//convMtToNinja();
		//realtickToDukatick();
		//convOandaToGb();
		
	}
	
	
	
	static private void realtickToDukatick()
	{
		String quell="m:\\forex\\kurse\\work\\DukaFormatConvert\\EURUSD_TickData.csv";
		String ziel="m:\\forex\\kurse\\work\\DukaFormatConvert\\EURUSD_ActiveTradesRealTickData_ConvDukaTickData.csv";
		
		if(FileAccess.FileAvailable(ziel))
			FileAccess.FileDelete(ziel, 0);
		
		Kursworker kw = new Kursworker();
		
		//Wandelt die Realticks vom Tickexporter ins Dukatickformat um
		kw.convRealtickToDukatick(quell, ziel);
		
	}
	
	
	static private void convMtToNinja()
	{
		String quell="m:\\forex\\kurse\\forex\\activTrades\\quelle\\";
		String ziel="m:\\forex\\kurse\\forex\\activTrades\\quelle\\";
		Kursworker kw = new Kursworker();
		kw.convMetatraderToNinjatrader(quell,ziel);
	}
	
	 static private void mergeKurse()
	{
		// TODO Auto-generated method stub
			Tracer.WriteTrace(20, "Hallo");
		
			String prefquell="m:\\forex\\kurse\\forex\\activTrades\\quelle\\";
			String prefziel="m:\\forex\\kurse\\forex\\activTrades\\ziel\\";
			String checkdatefile="c:\\forex\\fst\\DataDuka\\EURUSD1440.csv";
			String[] tupel = {"EURUSD1","EURUSD5","EURUSD15","EURUSD30","EURUSD60","EURUSD240","EURUSD1440"};
			
			int anz=tupel.length;
			for(int i=4; i<anz; i++)
			{
				
				merge(prefquell+tupel[i] +".csv",prefquell+tupel[i]+"b.csv",prefziel+tupel[i]+".csv",checkdatefile,prefziel+tupel[i]+"_check.txt");
			}
			
			
			
	}

	static private void merge(String q1, String q2, String z,String checkdatefile,String msgfile)
	{
		
		if(FileAccess.FileAvailable(z))
			FileAccess.FileDelete(z, 0);
		Kursworker kw = new Kursworker();
		kw.merge(q1,q2,z,checkdatefile,msgfile);
		
	}
	
}
