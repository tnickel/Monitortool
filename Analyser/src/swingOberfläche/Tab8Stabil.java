package swingOberfläche;

import hilfsklasse.FileAccess;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import mainPackage.GC;
import swingtest.DemoPanel;

public class Tab8Stabil
{
	public static JPanel createThreadUebersicht()
	{
		DemoPanel panel = new DemoPanel(new GridLayout(1, 2));

		// Den chart als Panel aufbauen
		

		// Die Selektionsliste aufbauen
		JList jl = Tab8Stabil.listeThreads();
		panel.add(jl);
		jl.setSelectedIndex(1);

		// Den Seitlichen Scroller generieren
		JScrollPane scrollPane = new JScrollPane(jl);
		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}
	
	public static JList listeThreads()
	{
		String[] listentry = new String[80000];
		FileAccess.initFileSystemList(GC.rootpath+"\\db\\UserThreadVirtualKonto\\Bank\\Gewinnverlaufdb", 1);
		int listcounter=0;
		String fnam=null;

		while((fnam=FileAccess.holeFileSystemName())!=null)
		{
			if(fnam.contains(".bak"))
				continue;
	
			listentry[listcounter] = new String(fnam);
			listcounter++;
		}					
		
		JList jlist = new JList(listentry);

		jlist.addMouseListener(new MA8Stabil());
		jlist.setVisibleRowCount(20);

		return jlist;
	}
}
