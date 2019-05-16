package except;

import hilfsklasse.Inf;
import hilfsklasse.Tools;
import mainPackage.GC;

public class CompressedThreadException extends Exception
{

	private static final long serialVersionUID = 134356L;

	public CompressedThreadException()
	{
	}

	public CompressedThreadException(String s)
	{
		super(s);
		Inf inf= new Inf();
		inf.setFilename(GC.rootpath+"\\db\\reporting\\corruptThread_von_Hand_korrektor.txt");
		inf.writezeile(Tools.get_aktdatetime_str()+" : "+s);
		
	}
}
