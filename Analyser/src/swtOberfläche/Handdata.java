package swtOberfläche;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Sys;
import hilfsklasse.Tracer;

import java.util.ArrayList;
import java.util.List;

import mainPackage.GC;

import org.eclipse.swt.widgets.Text;

public class Handdata
{
	//Kurse Beschreibung der Klasse:
	//Die Kasse Handdata verwaltet die daten die in Handdata abgelegt sind
	//Handdata==Daten von Hand eingegeben
	//Handdata wird auf mehreren Rechnern verwaltet und synchronisiert
	//Beispiel:offline/handata/Rechnername <Rechnername>/<typedir>/....
	//typedir= spezifiziert die Handdataklasse (zb. userdaten, aktiendaten etc.)
	//anzeige Handdata: liest von allen Verzeichnissen die informationen für einen user und stellt
	//die Info geschlossen dar.
	//Beim speichern werden die gesammelten Informationen im <Rechnername> -Verzeichniss abgelegt und 
	//die Verzeichnisse welche Infos von Fremdsystemen haben nach tmp/wastehandata verschoben (bzgl. des users)
	//Handdata akkumuliert also die infos von verschiedenen Rechnern und sammelt diese Info in 
	//dem aktuellen Rechnerverzeichniss
	//Hinweis: Man kopiert immer nur Verzeichnisse von Fremdrechnern in das Hostsystem
	//Das Verzeichniss des Hostsystems wird niemals auf dem Hostsystem gelöscht
	
	private String typedir_g =null;
	
	//Dies ist die Hostliste für die Handdata
	private java.util.List<String> hostliste = 	 new ArrayList<String> ();
	
	public Handdata(String typedir)
	{
		typedir_g=new String(typedir);
		hostliste=buildHostliste(GC.rootpath+"\\handdata");
	}
	
	public boolean HasHanddata(String name)
	{
		//Prüft nach ob für einen User Handdata da sind
		int anz=hostliste.size();
		for(int i=0; i<anz; i++)
		{
			String fnam = GC.rootpath + "\\handdata\\"+hostliste.get(i)+
			"\\"+typedir_g+"\\" + name + ".txt";
			if(FileAccess.FileAvailable(fnam)==true)
				return true;
		}
		return false;
	}
	
	public boolean HasPrognose(String name)
	{
		// prüft nach ob das Schlüsselwort prognose im text
		// vorkommt 
		int anz=hostliste.size();
		for(int i=0; i<anz; i++)
		{
			String fnam = GC.rootpath + "\\handdata\\"+hostliste.get(i)+
			"\\"+typedir_g+"\\" + name + ".txt";
			
			if (FileAccess.CheckFileKeyword(fnam, "prognose",0) == true)
				return true;
		}
		return false;
	}
	
	
		
	public String getHandataFilename(String name)
	{
		//ermittelt den Filenamen für die Handdata für einen user
		//Achtung es wird aber nur ein Filename ermittelt, es können
		//für einen User aber durchaus mehrere Filenamen in verschiedenen
		//Verzeichnissen existieren
		int anz=hostliste.size();
		for(int i=0; i<anz; i++)
		{
			String fnam = GC.rootpath + "\\handdata\\"+hostliste.get(i)+
			"\\"+typedir_g+"\\" + name + ".txt";
			if(FileAccess.FileAvailable(fnam)==true)
				return fnam;
		}
		return null;
	}
	
	private  List<String> buildHostliste(String pfad)
	//baut eine Hostliste für die Handdata auf
	{
		//Verzeichnisse einlesen
		FileAccess.initFileSystemList(pfad, 0);
		
		int anz=FileAccess.holeFileAnz();
		for(int i=0; i<anz; i++)
		{
			String hnam=FileAccess.holeFileSystemName();
			hostliste.add(hnam);
		}
		return hostliste;
	}
	private void zeigeEinenEintrag(final Text t,String file)
	{
		String zeile = null;
		Inf inf = new Inf();
		inf.setFilename(file);
		while ((zeile = inf.readZeile()) != null)
		{
			System.out.println("zeile=<" + zeile + ">");
			t.append(zeile);
			t.append("\n");
		}
		inf.close();
	}
	
	public void anzeigeHanddata(final Text t,String name )
	{
		int foundflag=0;
		t.selectAll();
		t.cut();
		t.clearSelection();
		
		//alle daten von allen rechnern werden zusammen eingelesen und angezeigt

		int anz=hostliste.size();
		for(int i=0; i<anz; i++)
		{
			String fnam = GC.rootpath + "\\handdata\\"+hostliste.get(i)+
			"\\"+typedir_g+"\\" + name + ".txt";
			if(FileAccess.FileAvailable0(fnam)==true)
			{
				zeigeEinenEintrag( t,fnam);
				foundflag=1;
			}
		}
	
		if(foundflag==0)
			t.setText("...........");
	}
	private void loescheFremddata(String name)
	{
		//für den username werden nur die handdata dieses Systems beibeihalten
		//der rest wird ins waste gepackt
		
		String hostname = Sys.getHostname();
		
		int anz=hostliste.size();
		for(int i=0; i<anz; i++)
		{
			String fnam = GC.rootpath + "\\handdata\\"+hostliste.get(i)+
			"\\"+typedir_g+"\\" + name + ".txt";
			if(fnam.toLowerCase().contains((hostname.toLowerCase())))
				continue;
			else
			{
				//lösche den fnam
				if(FileAccess.FileAvailable(fnam))
				{
					if(FileAccess.FileMove(fnam, GC.rootpath+"\\tmp\\wastehanddata\\"+name+".txt")==false)
						Tracer.WriteTrace(10, "error: internal fjjfjffj");
						
				}
			}
		}
	}
	
	public void speichereHanddata(final Text t,String name)
	{
		//delflag=1; dann werden alle handdata die nicht das hostsystem sind, ins waste gepackt
		String hostname = Sys.getHostname();
		
		// Save Button gedrückt
		String fnam = GC.rootpath + "\\handdata\\Rechnername " + hostname
				+ "\\"+typedir_g+"\\" + name + ".txt";
		String fnams = GC.rootpath + "\\handdata\\Rechnername " + hostname
				+ "\\"+typedir_g+"\\" + name + ".txt.sav";

		// Den save löschen
		if (FileAccess.FileAvailable0(fnams))
			FileAccess.FileDelete(fnams,1);

		if (FileAccess.FileAvailable0(fnam))
			FileAccess.FileMove(fnam, fnams);

		Inf inf = new Inf();
		inf.setFilename(fnam);
		String zeile = t.getText();
		inf.writezeile(zeile);
		
		//löscht alle handdate die nicht von diesem hostsystem kommen
		loescheFremddata(name);
	}
}
