package data;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import Metriklibs.Filterfile;
import datefunkt.Mondate;
import filterPack.Filterfiles;
import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Swttool;
import hilfsklasse.Tracer;

public class Best100Portfolios
{
	//hier in der liste werden die 100 besten portfolios abgespeicher
	private ArrayList<Portfolio> best100list = new ArrayList<Portfolio>();
	private String best100prot_glob = null;

	public Best100Portfolios()
	{
		// lösche das alte protokoll
		String fnamroot = Metrikglobalconf.endtestpath_glob;
		best100prot_glob = fnamroot + "\\best100prot.txt";
		File b100f = new File(best100prot_glob);
		if (b100f.exists())
			b100f.delete();
	}

	// diese Datenstruktur speichert die besten 100 portfolios ab
	public void checkAdd(Stratliste stratliste, Filterfiles filt,
			EndtestResult endresult, MFilter mfilter)
	{
		// stratliste: das sind die stratmene die nach den endtestfiltern noch
		// übrig ist
		// filt:dies sind die angewendeten filter, z.b 5 verzeichnisse dann 5
		// filter
		// endresult: dies ist der value des gew.
		// fkt:hier wird geprüft ob man die stratliste noch zu den best100
		// ergebnissen hinzufügen kann

		// prüft erst mal die filterbedinungen
		// prüft im Augenblick nur die anzahl
		if (mfilter.isAnzstratflag() == true)
		{
			// die anzahl prüfen, wenn zu wenig dann wird hier nix aufgenommen
			if (stratliste.anz() < mfilter.getMinanzahlstrat())
				return;
		}

		// prüft ob noch platz ist, wenn ja nimm einfach auf
		if ((best100list.size() < 100)
				&& (checkIstNeuerWert(endresult) == true))
		{
			// es ist noch platz also nehme auf
			Portfolio b100d = new Portfolio();
			b100d.setStratliste(stratliste);

			if (filt.getAnz() < 2)
				Tracer.WriteTrace(
						10,
						"Error: Anzahl<"
								+ filt.getAnz()
								+ "> Filtzeiträume muss mind 2 sein (1 Filter + 1 Endtest");

			nimmAuf(b100d, filt, endresult);

			Collections.sort(best100list);
			return; // ende
		}
		// hier wird die bestliste sortiert
		// element i=99 ist das schlechteste
		Collections.sort(best100list);

		// das neue ist besser als das schlechteste im array
		if ((checkBesser(endresult) == true)
				&& (checkIstNeuerWert(endresult) == true))
		{
			// dann schmeisse das letzte weg und nimm das neue auf
			best100list.remove(99);
			Portfolio b100d = new Portfolio();
			b100d.setStratliste(stratliste);

			nimmAuf(b100d, filt, endresult);
		}
		// sortiere die best100-liste
		Collections.sort(best100list);
	}

	private void nimmAuf(Portfolio b100d, Filterfiles filt,
			EndtestResult endresult)
	{
		// ein neues element wird in der bestenliste aufgenommen
		b100d.setFilt(filt);
		b100d.setEndresult(endresult);
		best100list.add(b100d);

		String d=Mondate.getAktDate().toString();
		
		// protokolliere
		Inf inf = new Inf();
		inf.setFilename(best100prot_glob);
		inf.writezeile(d+" : #<" + b100d.getStratliste().anz() + "> \t fitness<"
				+ b100d.getEndresult().getFitnessvalue() + ">");
		inf.close();
	}

	public float holeBestvalue()
	{
		// das beste steht am anfang

		if (best100list == null)
			return 0;

		if (best100list.size() == 0)
			return 0;

		Portfolio b100d = best100list.get(0);
		if (b100d.getEndresult() == null)
			Tracer.WriteTrace(10, "E:inernal no endresult");

		//max 1 nachkommastelle
		
		float val = b100d.getEndresult().getFitnessvalue();
		return val;
	}

	public Portfolio holeBestPortfolio()
	{
		// das beste steht am anfang

		if (best100list == null)
			return null;

		if (best100list.size() == 0)
			return null;

		//an position steht die liste der strategien die am besten abgeschnitten haben
		Portfolio bestPortfolio = best100list.get(0);
		if (bestPortfolio.getEndresult() == null)
			Tracer.WriteTrace(10, "E:inernal no endresult");

		return bestPortfolio;
	}
	public Filterfiles holeFilterFiles(int index)
	{
		if (best100list == null)
			return null;

		if (best100list.size() == 0)
			return null;
		Portfolio bestPortfolio = best100list.get(index);
		if (bestPortfolio.getEndresult() == null)
			Tracer.WriteTrace(10, "E:inernal no endresult");
		
		Filterfiles fi=bestPortfolio.getFilt();
		return fi;
	}
	
	public int calcAnzStratBestvalue()
	{
		// rechnet die anzahl der Strategien des besten ergebnisses aus

		if (best100list == null)
			return 0;

		if (best100list.size() == 0)
			return 0;

		Portfolio b100d = best100list.get(0);
		return (b100d.getStratliste().anz());

	}

	private boolean checkBesser(EndtestResult endresult)
	{
		// prüft nach ob der Endresult besser ist als das schlechteste
		// besser heisst wirklich grösser und nicht >=

		// letztes Element holen(Das letzte Element ist das schlechteste)
		int lastelemnr = best100list.size() - 1;
		Portfolio best100schlechteste = best100list.get(lastelemnr);
		// schaue nach ob neues endresult besser ist
		if (endresult.getFitnessvalue() > best100schlechteste.getEndresult()
				.getFitnessvalue())
			return true;
		else
			return false;
	}

	private boolean checkIstNeuerWert(EndtestResult endresult)
	{
		// true: dies ist ein neuer wert
		// false: wert ist schon in der liste vorhanden
		// Prüft nach ob dies ein neuer Wert ist, also das Resultat noch
		// garnicht in der liste
		float endval = endresult.getFitnessvalue();
		// gehe durch die liste
		int anz = best100list.size();
		for (int i = 0; i < anz; i++)
		{
			// ein element holen
			Portfolio best100d = best100list.get(i);
			float val = best100d.getEndresult().getFitnessvalue();

			// das endresult ist schon in der liste
			if (endval == val)
				return false;
		}
		// das Endresult konnte noch nicht gefunden werden, somit ist das
		// endresult noch nicht in der liste
		return true;
	}

	public Filterfiles holeFilterfiles(int index)
	{
		Portfolio best100d = best100list.get(index);
		Filterfiles fi=best100d.getFilt();
		return fi;
		
	}
	
	public Portfolio holePortfolio(int index)
	{
		if(index>=best100list.size())
		{
			Tracer.WriteTrace(20, "E:index i<"+index+"> bigger than best100list n<"+best100list.size()+">");
			return null;
		}
		Portfolio best100d = best100list.get(index);
		return best100d;
	}
	
	public void sort()
	{
		// hier wird die best 100 liste sortiert
		Collections.sort(best100list);
	}

	public void speichereDieResultate()
	{
		// die ganzen Resultate werden abgespeichert
		String fnamroot = Metrikglobalconf.endtestpath_glob;
		String best100dir = fnamroot + "\\best100dir";
		File best100dirf = new File(best100dir);

		// lösche Verzeichniss mit Inhalt und baue neu
		FileAccess.deleteDirectory(best100dirf);
		best100dirf.mkdir();

		int anzergeb = best100list.size();
		// speichere alle 100 Ergebnisse wenn sie da sind
		for (int i = 0; i < anzergeb; i++)
		{
			// speichere die n-resultate für jeden eintrag ist der best100 liste
			Portfolio b100d = best100list.get(i);

			Filterfiles filtr = b100d.holeFilterzeitraume();

			// jetzt werden die Ergebnisse gespeichert
			int anzfil = filtr.getAnz();
			for (int j = 0; j < anzfil; j++)
			{
				// filterzeitraum n=i, jte filter
				Filterfile fil = filtr.holeFilterzeitraumNummerI(j);

				// der filternam wird hier kaputt gemacht, das darf so nicht
				// sein !!!
				String filternam = i + "_" + j;
				if (fil == null)
					continue;

				fil.writeFilterSettingsBest100dir(best100dir, filternam, b100d
						.getEndresult().getFitnessvalue());
			}
		}
	}

	public void setTable(Table table)
	{
		Display dis= Display.getDefault();
		table.removeAll();
		
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		table.setLayoutData(data);

		Color black = dis.getSystemColor(SWT.COLOR_BLACK);
		Color green = dis.getSystemColor(SWT.COLOR_GREEN);
		Color red = dis.getSystemColor(SWT.COLOR_RED);

		Swttool.baueTabellenkopfDispose(table, "pos#fitness#anzStrat#unseen1#unseen2#unseen3#unseen4#unseen5");

		int ipos = 0;
		int anz=best100list.size();
		
		for (int i = 0; i < anz; i++)
		{
			Swttool.wupdate(dis);
			TableItem item = new TableItem(table, SWT.NONE);
			item.setForeground(black);
			// pos
			item.setText(0, String.valueOf(ipos));

			// 
			Portfolio b100d=best100list.get(i);

			float val=b100d.getEndresult().fitnessvalue;
			
			Stratliste stratl=b100d.getStratliste();
			int anzstrat=stratl.anz();
			//Filterzeitraume fil=b100d.getFilt();
			
			//pos0 den index setzen
			item.setText(0,String.valueOf(i));
			//pos1 den fitnesswert setzen
			item.setText(1, String.valueOf(val));
			//post2 anz Strategien
			item.setText(2, String.valueOf(anzstrat));
			
			//pos3 fitness unknown data
			item.setText(3,String.valueOf(b100d.getEndresultUnknownData(1)));
			item.setText(4,String.valueOf(b100d.getEndresultUnknownData(2)));
			item.setText(5,String.valueOf(b100d.getEndresultUnknownData(3)));
			item.setText(6,String.valueOf(b100d.getEndresultUnknownData(4)));
			item.setText(7,String.valueOf(b100d.getEndresultUnknownData(5)));
		}

		for (int i = 0; i <8; i++)
		{
			table.getColumn(i).pack();

		}
		return ;
	}
	//holeCloneBestPortfolio();
}
