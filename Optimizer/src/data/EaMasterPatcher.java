package data;


import gui.Mbox;

import java.io.File;


public class EaMasterPatcher 
{
	private MqlIndikatorEa eaindikator_glob=null;
	private MqlPatchEa eapatch_glob=null;
	private String[] PatchArray1_glob=null;
	private String[] PatchArray2_glob=null;
	private String patchfilename=null;
	
	public EaMasterPatcher(String mqlindiquellfile, String mqlpatchea)
	{
		//mqlindiquellfile=ist das file wo alle indikatoren drin stehen
		//mqlpatchea= ist der ea der gepatched wird
		
		File f1= new File(mqlindiquellfile);
		if(f1.exists()==false)
		{
			Mbox.Infobox("file <"+mqlindiquellfile+"> not available");
			return;
		}
		File f2= new File(mqlpatchea);
		if(f2.exists()==false)
		{
			Mbox.Infobox("file <"+mqlpatchea+"> not available");
			return;
		}
		
		//den patchfilename bestimmen, d.h. '_' voranstellen
		String patchpath= new File(mqlpatchea).getParent();
		String fnam    = new File(mqlpatchea).getName();
		patchfilename=patchpath+"\\_"+fnam;
		
		eaindikator_glob = new MqlIndikatorEa();
		eaindikator_glob.setFilename(mqlindiquellfile);
		
		File eafile=new File(mqlindiquellfile);
		if(eafile.exists()==false)
			Mbox.Infobox("warning mqlindifile <"+mqlindiquellfile+"> not available");
		
		File patchfile=new File(patchfilename);
		if(patchfile.exists()==false)
			Mbox.Infobox("warning mqlindifile <"+patchfilename+"> not available");
		
		eapatch_glob=new MqlPatchEa();
		eapatch_glob.setFilename(mqlpatchea);
	}
	public void patchEa(String stratname,String indistring1, String indistring2,int timeframe)
	{
		//der ea wird gepatched, d.h. die beiden indikatoren indistring1 und indistring2 werden
		//in den patchea eingebracht
		//stratname=Indi sell/buy<0022a, 0022b>
		if((indistring1==null)||(indistring2==null))
		{
			Mbox.Infobox("no selection");
			return;
		}
		
		String action1="",action2="";
		if(stratname.contains("Indi"))
		{
			action1=stratname.substring(stratname.indexOf(" ")+1,stratname.indexOf("/"));
			action2=stratname.substring(stratname.indexOf("/")+1,stratname.indexOf("<"));
		}
		System.out.println("stratname="+stratname);
		
		//die Funktionen holen
		PatchArray1_glob=eaindikator_glob.getPatchArray(indistring1);
		PatchArray2_glob=eaindikator_glob.getPatchArray(indistring2);
		
		//die Funktionen einbauen
		eapatch_glob.modifyAddFuntion(action1,indistring1, PatchArray1_glob,timeframe);
		eapatch_glob.modifyAddFuntion(action2,indistring2, PatchArray2_glob,timeframe);
		
		//den Ea speichern
		
		File pfile= new File(patchfilename);
		if(pfile.exists())
			pfile.delete();
		
		eapatch_glob.saveEa(patchfilename);
	}
	
}
