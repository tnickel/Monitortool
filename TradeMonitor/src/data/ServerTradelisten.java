package data;

import hiflsklasse.Swttool;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import Metriklibs.FileAccessDyn;

public class ServerTradelisten
{
	//in dieser Datenstruktur werden alle Tradelisten verwaltet
	private ArrayList<ServerTradeliste> servertradelisten = new ArrayList<ServerTradeliste>();
	
	public void loadUebersichtlisten()
	{
		//hier werden alle Tradelisten eingelesen
		FileAccessDyn fdyn= new FileAccessDyn();
		fdyn.initFileSystemList(SMglobalConfig.getTradelistendir(), 1);
		int anz=fdyn.holeFileAnz();

		for(int i=0; i<anz; i++)
		{
			ServerTradeliste strl= new ServerTradeliste(SMglobalConfig.getTradelistendir()+"\\"+fdyn.holeFileSystemName());
			servertradelisten.add(strl);
		}
	}
	public void buildUebersichtsTable(Table table)
	{
		//hier wird die table der Tradelisten aufgebaut
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		Swttool.baueTabellenkopfDispose(table,
				"user");
		
		int anz=servertradelisten.size();
		for(int i=0; i<anz; i++)
		{
			TableItem item = new TableItem(table, SWT.NONE);
			String fnam=servertradelisten.get(i).getFilename();
			String nam=fnam.substring(fnam.lastIndexOf("\\")+1);
			item.setText(0, nam);
		}
		for (int i = 0; i < 1; i++)
		{
			table.getColumn(i).pack();
		}
	}
	public Tradeliste getTradeliste(String tradelistenname)
	{
		int anz=servertradelisten.size();
		ServerTradeliste trserver=null;
		for(int i=0; i<anz; i++)
		{
			String fnam=servertradelisten.get(i).getFilename();
			if(fnam.contains(tradelistenname)==true)
			{
				trserver=servertradelisten.get(i);
				Tradeliste t=trserver.getTradeliste();
				return t;
			}
		}
		return null;
	}
}
