package data;

import hiflsklasse.Inf;

import java.io.File;

public class Lic
{
	private static int lic_glob = -1;

	public Lic()
	{
		if (lic_glob == -1)
		{
			// liccheck
			String fnam = Rootpath.getRootpath() + "\\conf\\lic.xml";
			File lfile = new File(fnam);
			// sagt ob eine lizenz da ist
			if ((lfile.exists())&&(lfile.length()>0))
			{
				Inf inf = new Inf();
				inf.setFilename(fnam);
				String licinfo = inf.readZeile();
				inf.close();
				
				if (licinfo.contains("freeware") == true)
					lic_glob = 0;
				else if (licinfo.contains("strategyquant") == true)
					lic_glob = 1;
				else if (licinfo.contains("full") == true)
					lic_glob = 5;
				else
					lic_glob = 0;
			} else
				lic_glob = 0;
		}
	}

	public static int getlic()
	{
		return lic_glob;
	}

	public static void setlic(int lic)
	{
		lic_glob = lic;
	}

	public static String getlicstring()
	{
		if(lic_glob==0)
			return("freeware");
		else if (lic_glob==1)
			return("quant version");
		else if(lic_glob==5)
			return("full");
		return ("invalid license");
		
	}
}
