package calcPack;

import java.io.File;

import work.CommentWork;
import Metriklibs.EndtestFitnessfilter;
import Metriklibs.Metriktabellen;
import data.EndtestResult;
import data.Metrikglobalconf;
import data.Stratliste;
import fileActorPack.Filemanager;
import filterPack.Filterzeitraume;

public class CalculationOneSetting
{
	//hier wird für ein einziges Setting der Filterwert berechnet
	
	Metriktabellen met = new Metriktabellen();
	Filterzeitraume filt= new Filterzeitraume();
	Stratliste stratliste= new Stratliste();
	//CommentWork wird für die gr.-Anzeige benötigt
	private CommentWork cw_glob = new CommentWork();
	public void readMetriktabellen()
	{
		//tabellen erst mal einlesen
		String rpath = Metrikglobalconf.getFilterpath();
 		met.readAllTabellen(rpath);
		
		//die filterzeiträume aus den Tabellen generieren, bzw
		//die einlesen wenn die auf platte
		filt.genAllFilterzeitraume(met);
	}
	public void genConfig()
	{
		//schreibt die ganzen filter in mehrere dateien
		filt.writeFilterSettings();
	}
	public void genStrDirs()
	{
		String path = Metrikglobalconf.getEndtestpath();
		String strdir=  path+"\\str__endtestfiles";
		String strseldir= path+"\\str__selected_endtestfiles";
		
		File strdirf= new File(strdir);
		if(strdirf.exists()==false)
			strdirf.mkdir();
		File strseldirf= new File(strseldir);
		if(strseldirf.exists()==false)
			strseldirf.mkdir();
		
	}
	public boolean checkConfigAvailable()
	{
		boolean flag=filt.checkConfigAvailable(met);
		return(flag);
	}
	public void readConfig()
	{
		filt=new Filterzeitraume();
		//liest die ganzen Metrikfilter ein
		filt.readFilterSettings(met);
	}
	public void cleanConfig()
	{
		filt.cleanFilterSettings(met);
	}
	
	public EndtestResult startCalculation(int kopierefilesflag,EndtestFitnessfilter endtestfitnessfilter)
	{
		//erst mal die strliste aufbauen
		stratliste.buildStratliste(met);
		//nur strategien nehmen die in der selliste stehen
		stratliste.filterSelfile(Metrikglobalconf.getFilterpath()+"\\filter.txt");

		//dann wird mit allen filtern rausgefiltert
		stratliste.filterAll(met, filt,0);
		
		//mit den reststrategien muss noch ein Endtest gemacht werden
		EndtestResult endresult=stratliste.Endtest(met,1,endtestfitnessfilter);
		
		//die Endtestfiles noch umkopieren
		if(kopierefilesflag==1)
			Filemanager.kopiereEndfiles(stratliste,met);
		return endresult;
	}
	public void showGraphik()
	{
		String endfile=met.holeEndtestFilnamen();
		String endtestdir_sel=endfile.substring(0,endfile.lastIndexOf("\\")+1)+"str__selected_endtestfiles";
		
		cw_glob.setWorkdir(endtestdir_sel);
		cw_glob.calcEndtestGraphic();
	}
}
