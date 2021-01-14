package data;

import java.io.File;
import java.util.ArrayList;

import StartFrame.Brokerview;
import gui.Mbox;
import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;
import modtools.ChrFile;

public class SymbolReplaceList
{
	// replacelist contains the information form replacesymbols.txt
	private ArrayList<SymbolReplaceElem> replist = new ArrayList<SymbolReplaceElem>();
	// srp is the symbol replaceresultlist for the end overview
	private ArrayList<String> srp = new ArrayList<String>();
	private String repfile_g = GlobalVar.getRootpath() + "\\conf\\replacesymbols.txt";
	private Brokerview brokerview_glob;
	
	public SymbolReplaceList(Brokerview brokerview)
	{
		brokerview_glob = brokerview;
		WriteDefaultReplacesymbols();
		ReadReplaceList();
	}
	
	private void WriteDefaultReplacesymbols()
	{
		File rpf = new File(repfile_g);
		if (rpf.exists() == false)
		{
			Inf inf = new Inf();
			inf.setFilename(repfile_g);
			inf.writezeile("#example for symbol replacement, '#' is a comment line");
			inf.writezeile("#ag2102,ag2115");
			inf.close();
		}
	}
	
	private void ReadReplaceList()
	{
		String zeile = "";
		Inf inf = new Inf();
		inf.setFilename(repfile_g);
		
		while ((zeile = inf.readZeile()) != null)
		{
			
			if (zeile.startsWith("#"))
				continue;
			if (zeile.length() > 4)
			{
				if (zeile.contains(",") == false)
					Tracer.WriteTrace(10, "E: missing ',' as delimiter in file <" + repfile_g + ">");
				
				String[] parts = zeile.split(",");
				SymbolReplaceElem se = new SymbolReplaceElem(parts[0], parts[1]);
				replist.add(se);
			}
		}
		inf.close();
	}
	
	public void ReplaceAllSymbols()
	{
		
		// go throuh all brokers
		int anz = brokerview_glob.getAnz();
		for (int i = 0; i < anz; i++)
		{
			Metaconfig mconf = brokerview_glob.getElem(i);
			String path = mconf.getNetworkshare_INSTALLDIR() + "\\profiles\\default";
			Tracer.WriteTrace(20, "I:check symbols on broker <" + path + ">");
			ReplaceBrokerSymbols(path);
		}
	}
	
	public void ShowReplaceResults()
	{
		String[] marray = new String[100];
		
		int anz = srp.size();
		if (anz >= 100)
			Tracer.WriteTrace(10, "E: index to high");
		
		if (anz > 1)
		{
			
			for (int i = 0; i < anz; i++)
				marray[i] = i+1+":"+srp.get(i);
		}
		else
			marray[0]="0 replacements";
		
		Mbox.Infobox2(anz+" Symbol Replacements", marray);
	}
	
	private void ReplaceBrokerSymbols(String path)
	{
		// replace all symbols for this broker
		FileAccess.initFileSystemList(path, 1);
		int anz = FileAccess.holeFileAnz();
		
		// go throuh all chartX.chr
		for (int i = 0; i < anz; i++)
		{
			String fnam = FileAccess.holeFileSystemName();
			if (fnam.startsWith("chart"))
				ReplaceSymbolsChart(fnam, path);
			
		}
	}
	
	private void ReplaceSymbolsChart(String fnam, String path)
	{
		// replace all symbols in this chartX.chr
		
		String fname = path + "\\" + fnam;
		ChrFile cfgpatch = new ChrFile();
		cfgpatch.setFilename(fname);
		cfgpatch.readMemFile();
		
		// search orginal symbol
		String orgsymbol = cfgpatch.getOrginalSymbol();
		if (orgsymbol == null)
		{
			// Tracer.WriteTrace(20, "W:no orginal symbol found in fnam<"+fnam+">");
			return;
		}
		//
		int anz = replist.size();
		for (int i = 0; i < anz; i++)
		{
			String symbol = replist.get(i).getOrgsymb();
			String newsymbol = replist.get(i).getDestsymb();
			
			// found symbol to replace
			if (orgsymbol.toLowerCase().equals(symbol.toLowerCase()))
			{
				Tracer.WriteTrace(20,
						"I:replace symbol<" + symbol + "> to new symbol<" + newsymbol + "> in file<" + fname + ">");
				cfgpatch.setSymbol(newsymbol);
				cfgpatch.writeMemFile(fname);
				srp.add("old<" + symbol + "> new<" + newsymbol + "> file<" + fname + ">");
				return;
			}
		}
	}
}