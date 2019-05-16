package hilfsklasse;

import java.io.BufferedReader;

import ComData.FileAccess;

public class KeyReader
{

	private BufferedReader br = null;
	private int openmodeflag = 0;
	private String openformat = null;

	public void SetOpenmode(String openformat)
	{
		openmodeflag = 1;
		this.openformat = openformat;
	}

	public boolean SetReader(String fn)
	{

		if (openmodeflag == 1)
			br = FileAccess.ReadFileOpen(fn, openformat);
		else
			br = FileAccess.ReadFileOpen(fn);

		return true;
	}

	public String GetLine()
	{
		String line = FileAccess.ReadFileZeile(br);
		if (line == null)
		{
			FileAccess.ReadFileClose(br);
			return null;
		}
		return line;
	}

	public int GetNextVal()
	{
		String line = GetLine();
		if (line == null)
			return 0;

		int val = Integer.valueOf(line);
		return val;
	}

	public String GetNextLine()
	{
		String line = GetLine();
		return line;
	}
}
