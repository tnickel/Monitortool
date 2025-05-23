package hiflsklasse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Inf
{
	private BufferedWriter bw = null;
	private BufferedReader br = null;
	private String filename_glob = null;
	
	// construktor
	public void Inf()
	{
	}
	
	public void setFilename(String fnam)
	{
		filename_glob = fnam;
		
	}
	
	public boolean appendzeile(String filename, String zeile, boolean carrigereturn)
	{
		
		bw = FileAccess.WriteFileOpenAppend(filename);
		if (zeile.contains("Bitte nicht spammen"))
			return true;
		
		try
		{
			bw.write(zeile);
			if (carrigereturn == true)
				bw.newLine();
			bw.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean writezeile(String zeile)
	{
		bw = FileAccess.WriteFileOpenAppend(filename_glob);
		try
		{
			if (bw == null)
			{
				Tracer.WriteTrace(20, "Warning: filefehler<" + filename_glob + ">");
				return false;
			}
			bw.append(zeile);
			bw.newLine();
			bw.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean writeFastZeile(String zeile)
	{
		
		if (bw == null)
		{
			bw = FileAccess.WriteFileOpen(filename_glob, "UTF-8");
			
		}
		FileAccess.WriteFileZeile(bw, zeile);
		
		return true;
	}
	
	public void AppendMemFileDelimiter(String mem)
	{
		
		while (5 == 5)
		{
			// hole string bis zum n�chsten delimiter
			String teilzeile = null;
			if (mem.contains(GC.delimiter))
				teilzeile = mem.substring(0, mem.indexOf(GC.delimiter));
			else
			{
				// letzte Zeile dann schreibe alles
				writezeile(mem);
				break;
				
			}
			if (teilzeile == null)
				break;
			if (teilzeile.length() == 0)
				break;
			
			// gehe weiter
			int pos1 = mem.indexOf(GC.delimiter) + GC.delimiter.length();
			mem = mem.substring(pos1);
			writezeile(teilzeile);
		}
		
	}
	
	public String readZeile()
	{
		// ISO-8859-1
		
		if (br == null)
			br = FileAccess.ReadFileOpen(filename_glob, "ISO-8859-1");
		String str;
		try
		{
			str = new String(br.readLine());
			return str;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			
			return null;
		} catch (NullPointerException e)
		{
			// TODO Auto-generated catch block
			
			try
			{
				if (br != null)
					br.close();
			} catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;
		}
	}
	
	
	
	
	public String readZeilen()
	{
		// liest alle zeilen und liefert einen string zur�ck
		// wird f�r das swt mulilinetextobjekt ben�tigt
		
		String sumstr = "";
		if (br == null)
			br = FileAccess.ReadFileOpen(filename_glob);
		String str = "";
		
		try
		{
			while (str != null)
			{
				str = new String(br.readLine());
				sumstr = sumstr + str + "\n";
			}
			return sumstr;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			
			return null;
		} catch (NullPointerException e)
		{
			// TODO Auto-generated catch block
			
			return sumstr;
		}
		
	}
	
	public String readZeile(String format)
	{
		if (br == null)
			br = FileAccess.ReadFileOpen(filename_glob, format);
		String str;
		try
		{
			str = new String(br.readLine());
			return str;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			
			return null;
		} catch (NullPointerException e)
		{
			// TODO Auto-generated catch block
			
			return null;
		}
	}
	
	public String readMemFile()
	{
		int n = 0;
		String zeile = null, mem = null;
		while ((zeile = readZeile()) != null)
		{
			n++;
			if (mem == null)
				mem = zeile;
			else
				mem = mem.concat(zeile);
			// System.out.println("lese zeile<"+n+">");
		}
		this.close();
		return mem;
		
	}
	
	public String readMemFileDelimter(int maxline)
	{
		// Hier wird das File in den Speicher gelesen, und auch gleichzeitig ein
		// delimter gesetzt
		// delimiter="@//nl"
		int n = 0;
		String zeile = null;
		StringBuilder sb = null;
		while ((zeile = readZeile()) != null)
		{
			if (maxline > 0)
				if (n > maxline)
					break;
				
			n++;
			if (sb == null)
				sb = new StringBuilder(zeile);
			else
			{
				sb = sb.append(GC.delimiter);
				sb = sb.append(zeile);
				
				// System.out.println("lese zeile<"+n+">");
				
			}
		}
		this.close();
		return sb.toString();
		
	}
	
	public String readMemFile(long maxlen)
	{
		int n = 0, lenz = 0;
		String zeile = null, mem = null;
		while ((zeile = readZeile()) != null)
		{
			lenz = lenz + zeile.length();
			n++;
			if (mem == null)
				mem = zeile;
			else
				mem = mem.concat(zeile);
			if (lenz > maxlen)
				break;
			// System.out.println("lese zeile<"+n+">");
		}
		this.close();
		return mem;
		
	}
	
	private String checkZeile(String zeile, String suchwort)
	{
		if (zeile.contains((suchwort)) == true)
		{
			zeile = zeile.replaceAll(suchwort, "<B><FONT COLOR=\"#FF0000\">" + suchwort + "</Font></B>");
		}
		return zeile;
	}
	
	public String readMemFile(String keyword, int convhtmlflag)
	{
		int n = 0;
		String zeile = null, mem = null;
		while ((zeile = readZeile()) != null)
		{
			if (keyword.length() > 1)
			{
				// alles klein
				String keyword1 = keyword.toLowerCase();
				zeile = checkZeile(zeile, keyword1);
				
				// alles gross
				String keyword2 = keyword.toUpperCase();
				zeile = checkZeile(zeile, keyword2);
				
				// erste Buchstabe immer gross
				String keyword3 = keyword.substring(0, 1).toUpperCase() + keyword.substring(1).toLowerCase();
				zeile = checkZeile(zeile, keyword3);
			}
			if (convhtmlflag == 1)
				zeile = zeile + "<br>";
			
			n++;
			if (mem == null)
				mem = zeile;
			else
				mem = mem.concat(zeile);
			// System.out.println("lese zeile<"+n+">");
		}
		this.close();
		return mem;
		
	}
	
	public void close()
	{
		
		try
		{
			if (br != null)
				br.close();
			
			if (bw != null)
				bw.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private XStream xstream;
	
	public void saveXST(Object data)
	{
	    FileWriter toFile = null;
	    xstream = new XStream(new DomDriver());
	    
	    // Erweiterte Sicherheitseinstellungen f�r alle ben�tigten Pakete
	    xstream.allowTypesByWildcard(new String[] {
	        "data.*",
	        "StartFrame.*",
	        "hiflsklasse.*",
	        "Panels.*",
	        "Sync.*",
	        "aut.*",
	        "datefunkt.*",
	        "filter.*",
	        "gui.*",
	        "modtools.*",
	        "mtools.*",
	        "network.*",
	        "swtHilfsfenster.*",
	        "java.util.*",
	        "java.lang.*"
	    });
	    
	    // Keine Referenzen verwenden, um Probleme mit OrderRetainingMap zu vermeiden
	    xstream.setMode(XStream.NO_REFERENCES);
	    
	    File file = new File(filename_glob);
	    try
	    {
	        toFile = new FileWriter(file);
	        toFile.write(xstream.toXML(data));
	        toFile.flush();
	        toFile.close();
	    } 
	    catch (Exception e)
	    {
	        e.printStackTrace();
	        try
	        {
	            if (toFile != null)
	                toFile.close();
	        } 
	        catch (IOException e1)
	        {
	            e1.printStackTrace();
	        }
	    }
	}
	
	public Object loadXST()
	{
	    Object temp = null;
	    File file = new File(filename_glob);
	    xstream = new XStream(new DomDriver());
	    
	    // Erweiterte Sicherheitseinstellungen f�r alle ben�tigten Pakete
	    xstream.allowTypesByWildcard(new String[] {
	        "data.*",
	        "StartFrame.*",
	        "hiflsklasse.*",
	        "Panels.*",
	        "Sync.*",
	        "aut.*",
	        "datefunkt.*",
	        "filter.*",
	        "gui.*",
	        "modtools.*",
	        "mtools.*",
	        "network.*",
	        "swtHilfsfenster.*",
	        "java.util.*",
	        "java.lang.*"
	    });
	    
	    // Keine Referenzen verwenden, um Probleme mit OrderRetainingMap zu vermeiden
	    xstream.setMode(XStream.NO_REFERENCES);
	    
	    try
	    {
	        BufferedReader reader = new BufferedReader(new FileReader(file));
	        temp = (Object) xstream.fromXML(reader);
	        reader.close();
	    } 
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	    return temp;
	}
	
}
