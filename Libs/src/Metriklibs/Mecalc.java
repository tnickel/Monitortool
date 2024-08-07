package Metriklibs;

import java.io.File;

import hiflsklasse.Inf;

public class Mecalc
{
	//hier werden berechnungen auf der Metriktabelle gemacht 
	//Was soll das, warum ist nicht beschrieben wozu die berechnungen gebrauht werden und was berechnet wird???
	//Funktion wird in der Toolbox benutzt
	public void calc50_100_write(Metriktabelle met50,Metriktabelle met100,String zielfile,String attributname)
	{
		
		File zfile= new File(zielfile);
		if(zfile.exists())
			zfile.delete();
		
		Inf inf = new Inf();
		inf.setFilename(zielfile);
		inf.writezeile("Generation,"+attributname);
		
		int anz=met50.getAnz();
		//hole für jede Strat das attribut
		for(int i=1; i<anz; i++)
		{
			//Part 1: 
			//hole metrikzeile pos=i;
			Metrikzeile mez50= met50.holeMetrikzeilePosI(i);
			String stratname=mez50.holeEntry(0).getValue();
			
			//holt das attribut was zu betrachten ist
			Metrikentry met1_50=met50.holeMetrikentry(mez50, attributname);
			
			
			//Part 2:
			Metrikzeile mez100=met100.getMetrikzeile(stratname);
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
