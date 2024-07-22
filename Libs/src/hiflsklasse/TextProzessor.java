package hiflsklasse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

public class TextProzessor
{
	private ArrayList<String> al = new ArrayList<String>();
	private BufferedReader br = null;
	
	public TextProzessor(String filename)
	{
		if (br == null)
			br = FileAccess.ReadFileOpen(filename, "ISO-8859-1");
		String str = null;
		try
		{
			str = new String(br.readLine());
			while (str != null)
			{
				str = new String(br.readLine());
				al.add(str);
			}
			if (br != null)
				br.close();
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			Tracer.WriteTrace(10, "E: error read file <" + filename + ">");
		
			return;
		} catch (NullPointerException e)
		{
			
			return;
		}
	}
	
	public String getLineWithWords(String kw1, String kw2, String kw3)
	{
		ListIterator<String> it = al.listIterator();
		while (it.hasNext())
		{
			String line = it.next();
			if (((line.toLowerCase().contains(kw1.toLowerCase()))) && ((line.toLowerCase().contains(kw2.toLowerCase())))
					&& ((line.toLowerCase().contains(kw3.toLowerCase()))))
			{
				return line;
			}
			
		}
		
		return null;
	}
}
