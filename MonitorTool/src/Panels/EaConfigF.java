package Panels;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import hiflsklasse.Inf;
import hiflsklasse.Tracer;

public class EaConfigF
{
	//dies ist die klasse die die KOnfigurationsdaten im File verwaltet
	
	private String conffile_glob = null;

	public EaConfigF(String configfilename)
	{
		FileInputStream propInFile;
		conffile_glob = configfilename;
		
	}

	public String getLots()
	{
		File confglob_f=new File(conffile_glob);
		if(confglob_f.exists()==false)
			return ("default");
		
		Inf inf=new Inf();
		inf.setFilename(conffile_glob);
		inf.readZeile();
		String zeile=inf.readZeile();
		inf.close();
		String[] result = zeile.split("=");
		String lots=result[1];
		lots=lots.replace(";", "");
		return lots;
	}

	public void setLots(String lots)
	{
		File oldfile=new File(conffile_glob);
		if(oldfile.exists())
			if(oldfile.delete()==false)
				Tracer.WriteTrace(10, "E:'setLots'can´t delete file<"+conffile_glob+">");
		Inf inf=new Inf();
		inf.setFilename(conffile_glob);
		inf.writezeile("\\Install\\Mt4_additionalcode SQ 4.X");
		inf.writezeile("extern double mmLotsIfNoMM = "+lots+";");
		inf.close();
	}

	
}
