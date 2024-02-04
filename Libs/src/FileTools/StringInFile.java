package FileTools;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import hiflsklasse.Tracer;

public class StringInFile
{

	
	public static boolean isStringInFile(String filename, String targetString, int mode)
	{
		//hier wird geprüft ob ein targetstring in einem file vorhanden ist, wird wird automatisch die coding beachtet
		//es gibt 2 unterschiedliche modi
		// mode=0=contains
		// mode=1=exakt
		if (isInFile(filename, targetString, StandardCharsets.UTF_16, mode) == false)
		{
			if (isInFile(filename, targetString, StandardCharsets.UTF_8, mode) == false)
				return false;
		}
		return true;
	}
	
	public static boolean Replace(String filename, String sourcestring, String targetstring)
	{
		//allgemeine funktion um einen quellstring durch einen targetstring in einer datei zu ersetzen, die
		//coding wird automatisch ermittelt.
		
		Charset coding = null;
		// hier wird nur exakt ersetzt
		// 1) als erstes wird die codierung ermittelt
		if (isInFile(filename, sourcestring, StandardCharsets.UTF_16, 1) == true)
			coding = StandardCharsets.UTF_16;
		else if (isInFile(filename, sourcestring, StandardCharsets.UTF_8, 1) == true)
			coding = StandardCharsets.UTF_8;
		else
			Tracer.WriteTrace(10, "sourcestring<" + sourcestring + "> not found in file <" + filename + ">");
		
		ReplaceAndWrite(filename, sourcestring,targetstring,coding);
		
		return true;
	}
	
	static void ReplaceAndWrite(String filename, String sourcestring, String targetstring, Charset coding)
	{
		//hier wird der sourcestring durch einen targetstring in einem file ersetzt, die coding wird beachtet
		List<String> list;
		List<String> modifiedList = new ArrayList<>();
		try
		{
			list = Files.readAllLines(Paths.get(filename), coding);
			
			for (String elem : list)
				modifiedList.add(elem.replace(sourcestring,targetstring));

			
			Path path = Paths.get(filename);
			Files.write(path, modifiedList);
		
		
		
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private static boolean isInFile(String filename, String targetString, Charset coding, int mode)
	{
		//hier wird untersucht ob der targetstring in einem file vorkommt. Die coding wird beachtet z.b. utf-8
		try
		{
			List<String> list = Files.readAllLines(Paths.get(filename), coding);
			
			for (String elem : list)
			{
				if ((mode == 0) && (elem.toLowerCase().contains(targetString.toLowerCase())))
					return true;
				if ((mode == 1) && (elem.toLowerCase().equals(targetString.toLowerCase())))
					return true;
			}
			return false;
		} catch (IOException e)
		{
			return false;
		}
	}
	
}
