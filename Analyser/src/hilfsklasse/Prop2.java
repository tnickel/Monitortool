package hilfsklasse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Prop2
{
	static private String propfile = null;
	static private Properties p2 = null;

	public Prop2(String propf)
	{
		propfile = propf;

		if (FileAccess.FileLength(propfile) < 5)
		{
			Tracer.WriteTrace(10, "Error:configfile <" + propfile
					+ "> nicht gefunden");
		}

		try
		{
			FileInputStream propInFile = new FileInputStream(propfile);
			p2 = new Properties();
			p2.load(propInFile);
			propInFile.close();

		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static public String getprop(String prop)
	{
		String str = null;
		try
		{
			str = p2.get(prop).toString();
			return (str);
		} catch (NullPointerException e)
		{ // falls Schlüsselwort nicht definiert kommt diese exception
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return (null);
		}
	}

	static public boolean setprop(String key, String value)
	{
		try
		{
			p2.setProperty(key, value);
			FileOutputStream out = new FileOutputStream(propfile);
			p2.store(out, "---No Comment---");
			out.close();
			return (true);
		} catch (NullPointerException e)
		{ // falls Schlüsselwort nicht definiert kommt diese exception
			// TODO Auto-generated catch block
			e.printStackTrace();
			return (false);
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
