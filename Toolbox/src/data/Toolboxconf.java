package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Toolboxconf
{
	static private Properties p = new Properties();
	static String conffile_glob=null;
	public Toolboxconf(String path)
	{
		
		//hier wird die config eingelesen
		conffile_glob = path+"\\bin\\toobox.conf";

		File tconffile= new File(conffile_glob);
		if(tconffile.exists()==false)
			return;
		
		FileInputStream propInFile;
		try
		{
			propInFile = new FileInputStream(conffile_glob);
		
		p = new Properties();
		p.load(propInFile);
		propInFile.close();
		} catch ( IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public  static boolean setPropAttribute(String key, String value)
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
	
	/**
	 * Gets the prop attribute.
	 *
	 * @param prop the prop
	 * @return the prop attribute
	 */
	public  static String getPropAttribute(String prop)
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
}
