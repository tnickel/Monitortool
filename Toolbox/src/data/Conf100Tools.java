package data;

import gui.Mbox;
import hiflsklasse.FileAccess;
import hiflsklasse.SG;
import hiflsklasse.Tracer;
import montool.MonDia;

import org.eclipse.swt.widgets.Display;

import xml.TnParser;
import Metriklibs.FileAccessDyn;

public class Conf100Tools
{
	String quelldir=null;
	String zieldir=null;

	public String getQuelldir()
	{
		return quelldir;
	}
	public String setQuelldir()
	{
		String dir=selectDir();
		this.quelldir = dir;
		return dir;
	}

	public String getZieldir()
	{
		return zieldir;
	}
	public String setZieldir()
	{
		String dir=selectDir();
		this.zieldir = dir;
		return dir;
	}
	private String selectDir()
	{
		
		String fnam=MonDia.DirDialog(Display.getDefault(), "c:");
		return fnam;
	}
	public void filter(String text2maxddrobustdiff)
	{
		//1)str werden eingelesen
		//2)dann wird nach maxddrobustdiff gefiltert
		//3)und ins Ausgabeverzeichniss gepeichert
		float maxddrobustallowed=SG.get_float_zahl(text2maxddrobustdiff,5);
		int copycount=0;
		FileAccessDyn fdyn= new FileAccessDyn();
		fdyn.initFileSystemList(quelldir, 1);
		int anz=fdyn.holeFileAnz();
		for(int i=0; i<anz; i++)
		{
			String fnam1=fdyn.holeFileSystemName();
			String fnam=quelldir+"\\"+fnam1;
			float maxdd_diff=parseMaxddrobustDiff(fnam);
			
			if(maxdd_diff<maxddrobustallowed)
			{
				//der wert ist innerhalb der schranken dann kopiere um
				kopiereStr(quelldir,zieldir,fnam1);
				copycount++;
			}
		}
		Mbox.Infobox("#<"+copycount+">/<"+anz+"> strfiles copied ready");
	}
	private void kopiereStr(String quelldir,String zieldir,String fnam1)
	{
		FileAccess.copyFile(quelldir+"\\"+fnam1, zieldir+"\\"+fnam1);
	}
	
	private float parseMaxddrobustDiff(String fnam)
	{
		TnParser parser = new TnParser();

		//lese alles da robustnesswerte am ende liegen
		parser.loadFile(fnam, 0);

		// profit auslesen
		float maxddrobust100 = 0;
		float maxddrobust50=0;
		
		
		maxddrobust100 = parser.getMaxDD_Robust(100);
		maxddrobust50 = parser.getMaxDD_Robust(50);
		
		float result=maxddrobust100-maxddrobust50;

		System.out.println("maxrobust100<"+maxddrobust100+"> maxrobust50<"+maxddrobust50+"> diff<"+result+">");
		
		if(result<0)
			Tracer.WriteTrace(10, "E:internal error result=<"+result+">");

		return result;
	}
}
