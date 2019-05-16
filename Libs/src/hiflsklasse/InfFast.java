package hiflsklasse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class InfFast
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
	
	public StringBuffer readZeile()
	{
		//ISO-8859-1
		
		if (br == null)
			br = FileAccess.ReadFileOpen(filename_glob,"ISO-8859-1");
		StringBuffer str;
		try
		{
			str = new StringBuffer(br.readLine());
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
	
	public StringBuffer readMemFile()
	{
		int n = 0;
		StringBuffer zeile = null, mem = null;
		while ((zeile = readZeile()) != null)
		{
			n++;
			if (mem == null)
				mem = new StringBuffer();
			else
				mem = mem.append(zeile);
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
}
