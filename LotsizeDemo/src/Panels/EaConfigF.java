package Panels;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class EaConfigF
{
	//dies ist die klasse die die KOnfigurationsdaten im File verwaltet
	private Properties p = new Properties();
	private String conffile_glob = null;
	
	private String lots="0.01";
	
	
	
	public EaConfigF(String configfilename)
	{
		FileInputStream propInFile;
		conffile_glob = configfilename;

		try
		{
			propInFile = new FileInputStream(conffile_glob);

			p = new Properties();
			p.load(propInFile);
			propInFile.close();
			
			
			lots=getPropAttribute("Lots");
			
						
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean setPropAttribute(String key, String value)
	{
		try
		{
			p.setProperty(key, value);
			FileOutputStream out = new FileOutputStream(conffile_glob);
			p.store(out, "---No Comment---");
			out.close();
			return (true);
		} catch (NullPointerException e)
		{
			e.printStackTrace();
			return (false);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	private String getPropAttribute(String prop)
	{
		String str = null;
		try
		{
			str = p.get(prop).toString();
			return (str);
		} catch (NullPointerException e)
		{
			return ("");
		}
	}

	

	public String getLots()
	{
		return lots;
	}

	public void setLots(String lots)
	{
		this.lots = lots;
		setPropAttribute("Lots", lots);
	}

	





	

	
	
}
