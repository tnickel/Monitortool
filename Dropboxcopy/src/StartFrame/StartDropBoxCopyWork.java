package StartFrame;


import java.awt.Color;
import java.io.File;

import data.DropActor;
import data.DropGlobalVar;
import data.DropconfList;
import gui.Mbox;
import hilfsklasse.FileAccess;
import hilfsklasse.Swttool;
import mtools.DisTool;
import mtools.Mlist;

public class StartDropBoxCopyWork
{
	private DropconfList metaconfliste = new DropconfList();

	
	public void table1clicked(String name, Table table)
	{
		System.out.println(" name<" + name + ">");

		int anz = metaconfliste.getSize();

		for (int j = 0; j < anz; j++)
		{
			DropActor dcon = metaconfliste.getElem(j);
			TableItem ti = table.getItem(j);
			if (ti.getChecked() == true)
				dcon.setActiveflag(1);
			else
				dcon.setActiveflag(0);
		}
		metaconfliste.save();
	}

	public void scaninit(Table table1)
	{
		//metaconfliste.clear();

		String dirnam = DropGlobalVar.getMetarootpath();
		FileAccess.initFileSystemList(dirnam, 0);
		int anz = FileAccess.holeFileAnz();

		// Sucht die Metatraderverzeichnisse
		for (int i = 0; i < anz; i++)
		{
			String fnam = DropGlobalVar.getMetarootpath() + "\\"
					+ FileAccess.holeFileSystemName();
			File mnam = new File(fnam + "\\terminal.exe");
			Mlist.add("check <" + fnam + ">", 1);
			// falls dies ein Metatraderverzeichniss ist
			if (mnam.exists() == true)
			{
				metaconfliste.addElem(mnam.getAbsolutePath(), 1);
				metaconfliste.setValid(fnam+ "\\terminal.exe");
				Mlist.add("Found MT4 <" + mnam + ">");
			}
		}

		// Sucht in den FstVerzeichnissen
		dirnam = DropGlobalVar.getFstrootpath();
		FileAccess.initFileSystemList(dirnam, 0);
		anz = FileAccess.holeFileAnz();
		for (int i = 0; i < anz; i++)
		{
			String fnam = DropGlobalVar.getFstrootpath() + "\\"
					+ FileAccess.holeFileSystemName();
			File mnam = new File(fnam + "\\Forex Strategy Trader.exe");
			Mlist.add("check <" + fnam + ">", 1);
			// falls dies ein Fsttraderverzeichniss ist
			if (mnam.exists() == true)
			{
				metaconfliste.addElem(mnam.getAbsolutePath(), 2);
				metaconfliste.setValid(fnam+ "\\Forex Strategy Trader.exe");
				Mlist.add("Found FST <" + mnam + ">");
			}
		}

	
		
		anz = metaconfliste.getSize();
		// die dropboxverzeichnisse anlegen
		for (int i = 0; i < anz; i++)
		{
			DropActor dmef = metaconfliste.getElem(i);
			if (dmef.getActiveflag() == 1)
				dmef.initdropbox();
			if(dmef.getValidflag()==0)
			{
				Mlist.add("Remove <" + dmef.getTraderexe() + ">");
				metaconfliste.delElem(dmef);
				anz--;
				i--;
			}
		}
		metaconfliste.save();

	}

	public void refresh(Table table1)
	{
		viewTable(table1);
	}

	public void viewTable(Table table)
	{

		// hier die table 1 aufbauen und anzeigen

		Display dis = Display.getDefault();

		Color magenta = dis.getSystemColor(SWT.COLOR_MAGENTA);
		Color green = dis.getSystemColor(SWT.COLOR_GREEN);
		Color blue = dis.getSystemColor(SWT.COLOR_BLUE);
		table.removeAll();
		table.clearAll();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		Swttool.baueTabellenkopfDispose(table, "Pfad");

		int anz = metaconfliste.getSize();

		for (int j = 0; j < anz; j++)
		{
			DropActor dcon = metaconfliste.getElem(j);
			TableItem item = new TableItem(table, SWT.CHECK);
			item.setText(0, dcon.getTraderexe());
			if (dcon.getActiveflag() == 1)
				item.setChecked(true);
			else
				item.setChecked(false);

		}
		if (anz > 0)
			for (int i = 0; i < 1; i++)
			{
				table.getColumn(i).pack();
			}

	}

	public void save()
	{
		metaconfliste.save();
	}

	public void loop()
	{
		// hier findet ein loopdurchlauf statt

		int anz = metaconfliste.getSize();
		for (int j = 0; j < anz; j++)
		{
			DropActor dcon = metaconfliste.getElem(j);
			// Mlist.add("check <"+dcon.getPath()+">", 1);
			if (dcon.getActiveflag() == 1)
			{
				dcon.copyHistoryexporter();
				dcon.IoHinrichtung();
				dcon.IoRueckrichtung();
				dcon.copyOldHistoryexporter();
				dcon.restartcheck();
				dcon.copyLotfiles();
				dcon.copyLogfiles(300);
			
	
			}
			
		}
		DisTool.UpdateDisplay();
	}

	public void installDeleteEas(boolean cleanmetatraderflag)
	{
		int anz = metaconfliste.getSize();
		for (int j = 0; j < anz; j++)
		{
			DropActor dcon = metaconfliste.getElem(j);
			// Mlist.add("check <"+dcon.getPath()+">", 1);
			if (dcon.getActiveflag() == 1)
			{
				//erst löschen
				dcon.deleteEas();
				//wenn noch was da ist dann installieren
				dcon.installEas(cleanmetatraderflag);
				

			}
			DisTool.UpdateDisplay();
		}
		Mbox.Infobox("Installation ready");
	}
	public void exportEas()
	{
		int anz = metaconfliste.getSize();
		for (int j = 0; j < anz; j++)
		{
			DropActor dcon = metaconfliste.getElem(j);
			Mlist.add("export <"+dcon.getTraderexe()+">", 1);
			if (dcon.getActiveflag() == 1)
			{
				dcon.exportEas();

			}
			DisTool.UpdateDisplay();
		}
		Mbox.Infobox("Export in Dropbox ready");
	}
	public void restartAllMetatrader()
	{
		int anz = metaconfliste.getSize();
		for (int j = 0; j < anz; j++)
		{
			DropActor dcon = metaconfliste.getElem(j);
			Mlist.add("restart MT <"+dcon.getTraderexe()+">", 1);
			if (dcon.getActiveflag() == 1)
			{
				dcon.restartMetatrader();

			}
			DisTool.UpdateDisplay();
		}
		Mbox.Infobox("Restart ready");
		
	}
}
