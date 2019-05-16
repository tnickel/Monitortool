package Metriklibs;

import hiflsklasse.Inf;

import java.io.File;

public class Mecalc
{
	//hier werden berechnungen auf der Metriktabelle gemacht
	public void calc50_100_write(Metriktabelle met50,Metriktabelle met100,String zielfile,String attributname)
	{
		File zfile= new File(zielfile);
		if(zfile.exists())
			zfile.delete();
		
		Inf inf = new Inf();
		inf.setFilename(zielfile);
		inf.writezeile("Generation,"+attributname);
		
		int anz=met50.getAnz();
		//hole f�r jede Strat das attribut
		for(int i=1; i<anz; i++)
		{
			//Part 1: 
			//hole metrikzeile pos=i;
			Metrikzeile mez50= met50.holeMetrikzeilePosI(i);
			String stratname=mez50.holeEntry(0).getValue();
			
			//holt das attribut was zu betrachten ist
			Metrikentry met1_50=met50.holeMetrikentry(mez50, attributname);
			
			
			//Part 2:
			Metrikzeile mez100=met100.holeMetrikzeile(stratname);
			Metrikentry met2_100=met100.holeMetrikentry(mez100,attributname);
			
			//Berechnung;
			Float f100=Float.valueOf(met2_100.getValue());
			Float f50=Float.valueOf(met1_50.getValue());
			Float f3=Math.abs(f100-f50);
			inf.writezeile(stratname+","+f3);
		}
		inf.close();
	}
}
