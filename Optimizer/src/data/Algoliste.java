package data;


import hiflsklasse.FileAccess;

import java.util.ArrayList;

public class Algoliste
{
	//## die algoliste speichert alle namen der verfügbaren optimierungsalgorithmen
	 private static ArrayList<Algodat> algolist = new ArrayList<Algodat>();
	
	public static  void init(String keyword)
	{
		String rootdir=Config.getRootdir();
	
		if(algolist.size()>0)
			algolist.clear();
		
		
		//baue hier die Algolist auf
		FileAccess.initFileSystemList(rootdir+"\\data", 1);
		int anz=FileAccess.holeFileAnz();
		for(int i=0; i<anz; i++)
		{
			String fnam=FileAccess.holeFileSystemName();
			if(fnam.contains(".txt")==false)
				continue;
			
			if(fnam.contains(keyword)==false)
				continue;
			
			Algodat adat= new Algodat();
			adat.setActivated(false);
			adat.setName(fnam);
			adat.setPath(rootdir+"\\data\\"+fnam);
			algolist.add(adat);
		}
		//spezielle Algorithmen hinzufügen
		Algodat adat= new Algodat();
		adat.setName("OptGdx");
		algolist.add(adat);
		adat= new Algodat();
		adat.setName("Oracle");
		algolist.add(adat);
		adat= new Algodat();
		adat.setName("hour/weekday");
		algolist.add(adat);
	}
	
	public static int getAnz()
	{
		return algolist.size();
	}
	
	public static  Algodat getAlgodat(int index)
	{
		return(algolist.get(index));
	}
	
	public static String[] calcStringliste()
	{
		int anz=algolist.size();
		String[] aliste2 = new String[algolist.size() ];

		for(int i=0;i<anz; i++)
		{
			aliste2[i]=algolist.get(i).getName();
		}
		return aliste2;
	}
}
