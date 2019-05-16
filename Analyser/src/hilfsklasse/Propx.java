package hilfsklasse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Propx
{
	static private FileInputStream propInFile;
	static private Properties p2;
	private int i = 0;
	static private int unixflag = -1;
	private String drivechars = "cdefghijklmnopq";
	private char x;
	static private String Configfilename = null;

	// static private String propresult = null;

	public Propx()
	{

		File inputFile = null;

		if (Configfilename != null)// configname schon vorhanden
			return;

		// sucht rootverzeichniss, es wird nach "config.prop" gesucht
		for (i = 0; i < 14; i++)
		{
			x = drivechars.charAt(i);
			Configfilename = x + ":\\config.prop";
			// System.out.println("fnam=" + Configfilename);

			inputFile = new File(Configfilename);
			if (inputFile.length() > 10) // configfile gefunden
			{
				System.out.println("System=WINDOWS");
				unixflag = 0;
				break;
			}

			if (i == 13)
				System.out.println("ERROR no propertiefile");
		}
		// falls kein windows
		if (unixflag == -99)
		{
			Configfilename = "/media/mmc1/config.prop";
			inputFile = new File(Configfilename);
			if (inputFile.length() > 10) // configfile gefunden
			{
				unixflag = 1;
				System.out.println("System=UNIX");
			}
		}
		// System.out.println("Read property File name="+Configfilename);
		// read propertys

		try
		{
			propInFile = new FileInputStream(Configfilename);
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

	public static String getprop(String prop)
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

	public static boolean setprop(String key, String value)
	{

		try
		{
			p2.setProperty(key, value);
			FileOutputStream out = new FileOutputStream(Configfilename);
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

	public static int unixflag()
	{
		return (unixflag);
	}
}
