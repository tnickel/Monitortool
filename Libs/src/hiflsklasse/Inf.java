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

	public boolean appendzeile(String filename, String zeile,
			boolean carrigereturn)
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
	
	
	public void AppendMemFileDelimiter(String mem)
	{
		
	
		while(5==5)
		{
		//hole string bis zum nächsten delimiter
		String teilzeile=null;	
		if(mem.contains(GC.delimiter))	
			 teilzeile=mem.substring(0,mem.indexOf(GC.delimiter));
		else
		{
			//letzte Zeile dann schreibe alles
			writezeile(mem);
			break;
			
		}
		if(teilzeile==null)
			break;
		if(teilzeile.length()==0)
			break;
		
		//gehe weiter
		int pos1=mem.indexOf(GC.delimiter)+GC.delimiter.length();
		mem=mem.substring(pos1);
		writezeile(teilzeile);
		}
		
	}
	
	
	public String readZeile()
	{
		//ISO-8859-1
		
		if (br == null)
			br = FileAccess.ReadFileOpen(filename_glob,"ISO-8859-1");
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
				if (br!=null)
				br.close();
			} catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;
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
		//Hier wird das File in den Speicher gelesen, und auch gleichzeitig ein delimter gesetzt
		//delimiter="@//nl"
		int n = 0;
		String zeile = null, mem = null;
		while ((zeile = readZeile()) != null)
		{
			if(maxline>0)
				if(n>maxline)
					break;
			
			
			n++;
			if (mem == null)
				mem = new String(zeile);
			else
				mem = mem.concat(GC.delimiter+zeile);
			// System.out.println("lese zeile<"+n+">");
		}
		this.close();
		return mem;

	}
	
	
	public String readMemFile(long maxlen)
	{
		int n = 0,lenz=0;
		String zeile = null, mem = null;
		while ((zeile = readZeile()) != null)
		{
			lenz=lenz+zeile.length();
			n++;
			if (mem == null)
				mem = zeile;
			else
				mem = mem.concat(zeile);
			if(lenz>maxlen)
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
			zeile = zeile.replaceAll(suchwort, "<B><FONT COLOR=\"#FF0000\">"
					+ suchwort + "</Font></B>");
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
				String keyword3 = keyword.substring(0, 1).toUpperCase()
						+ keyword.substring(1).toLowerCase();
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
	public void saveXST( Object data)
	{
		initStream();
		File file = new File(filename_glob);
		try
		{
			FileWriter toFile = new FileWriter(file);
			toFile.write(xstream.toXML(data));
			toFile.flush();
			toFile.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public Object loadXST()
	{
		Object temp = null;
		File file = new File(filename_glob);
		initStream();
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			temp = (Object) xstream.fromXML(reader);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return temp;
	}

	private void initStream()
	{
		xstream = new XStream(new DomDriver());
	}
	
}
