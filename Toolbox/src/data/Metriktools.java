package data;

import montool.MonDia;

import org.eclipse.swt.widgets.Display;

import Metriklibs.Mecalc;
import Metriklibs.Metriktabelle;

public class Metriktools
{
	String conf50quelle=null;
	String conf100quelle=null;
	String zieltable=null;
	static String lastdir=null;
	public String getConf50quelle()
	{
		return conf50quelle;
	}
	public String setConf50quelle()
	{
		String dir=selectFile();
		this.conf50quelle = dir;
		return dir;
	}
	public String getConf100quelle()
	{
		return conf100quelle;
	}
	public String setConf100quelle()
	{
		String dir=selectFile();
		this.conf100quelle = dir;
		return dir;
	}
	public String getZieltable()
	{
		return zieltable;
	}
	public String setZieltable()
	{
		String dir=selectFile();
		this.zieltable = dir;
		return dir;
	}
	public void calcSave(String attributname)
	{
		Mecalc mecalc= new Mecalc();
		
		//die quellen einlesen
		Metriktabelle met50 = new Metriktabelle();
		met50.readExportedTableFile(null,conf50quelle);
		
		Metriktabelle met100 = new Metriktabelle();
		met100.readExportedTableFile(null,conf100quelle);
		
		mecalc.calc50_100_write(met50,met100,zieltable,attributname);
	}

	
	private String selectFile()
	{
		
		String fnam=MonDia.FileDialog(Display.getDefault(), "c:");
		return fnam;
	}
}
