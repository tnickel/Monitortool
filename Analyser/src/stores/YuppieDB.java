package stores;

import hilfsklasse.KeyReader;
import hilfsklasse.SG;
import hilfsklasse.ToolsException;
import hilfsklasse.Tracer;
import mainPackage.GC;
import objects.YuppieDbObj;
import db.DB;

public class YuppieDB extends DB
{

	public YuppieDB()
	{
		System.out.println("YupieDB construktor");
		LoadDB("yuppie", null, 0);
	}
	public boolean plausiVorNeuaufnahme(Object obj)
	{
		return true;
	}
	public void GenerateMasterliste()
	{
		// muss einmal von hand aufgerufen werden
		String line = null;
		KeyReader kr = new KeyReader();
		kr.SetOpenmode("UTF-8");
		kr
				.SetReader(GC.rootpath
						+ "\\db\\masterfiles\\yuppie\\aktienliste.csv");

		while ((line = kr.GetLine()) != null)
		{
			System.out.println("line=" + line);
			YuppieDbObj yobj = new YuppieDbObj();
			try
			{
				String nam = new String(SG.nteilstring(line, ";", 1));
				String wkn = new String(SG.nteilstring(line, ";", 2));

				if (nam.contains("Unternehmen"))
					continue;

				yobj.setAktname(nam);
				yobj.setWkn(wkn);
				this.AddObject(yobj);
			} catch (ToolsException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				Tracer.WriteTrace(10, "Error: data format <" + line + ">");
			}
		}
		this.WriteDB();

	}
	public void postprocess()
	{}
}
