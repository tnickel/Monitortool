package hilfsklasse;

import java.io.BufferedWriter;
import java.io.IOException;

public class Infbuf
{
	//schnellere Inf-Klasse,
	//die Klasse puffert immer muss aber am Ende geschlossen werden
	private BufferedWriter bw_g = null;
	private String filename_g = null;

	// construktor
	public  Infbuf(String fnam)
	{
		bw_g = FileAccess.WriteFileOpenAppend(fnam);
		filename_g = fnam;
	}
	

	public boolean appendzeile(String filename, String zeile,
			boolean carrigereturn)
	{
		if (bw_g == null)
		{
			Tracer.WriteTrace(10, "Warning: filefehler01<" + filename + ">");
			return false;
		}
	
		if (zeile.contains("Bitte nicht spammen"))
			return true;
	
		try
		{
			bw_g.write(zeile);
			if (carrigereturn == true)
				bw_g.newLine();
			
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
		
		try
		{
		
			if (bw_g == null)
			{
				Tracer.WriteTrace(10, "Warning: Schreibfehler<" + filename_g + ">");
				return false;
			}
			bw_g.write(zeile);
			bw_g.newLine();
			
				
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public void flush()
	{
		try
		{
			bw_g.flush();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void close()
	{
		try
		{
			bw_g.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
