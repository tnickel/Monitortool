package WekaTools;

import java.io.File;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Tracer;

public class WekaMatrixCollector
{
	private String protokollfileGlob;
	private WekaClassifierElem[][] matrix;
	
	public WekaMatrixCollector(String protokollfile)
	{
		if (protokollfile == null)
		{
			throw new IllegalArgumentException("Protokollfile must not be null");
		}
		if (protokollfile == null)
			Tracer.WriteTrace(10, "E:protokollfile is not set !--> stop");
		
		if (new File(protokollfile).exists())
			if (new File(protokollfile).delete() == false)
				Tracer.WriteTrace(10, "E:Can´t delte file <" + protokollfile + ">");
			
		protokollfileGlob = protokollfile;
		matrix = new WekaClassifierElem[100][100]; // Initialisiere die 100x100 Matrix
	}
	
	public void AddResult(int x, int y, WekaClassifierElem elem)
	{
		matrix[x][y] = elem;
	}
	
	public int GetCopyCounterSum()
	{
		int copycountsum=0;
		for (int j = 0; j < 100; j++)
		{
			for (int i = 0; i < 100; i++)
			{
				WekaClassifierElem elem = matrix[i][j];
				if (elem != null)
				{
					//Tracer.WriteTrace(20, "I:bearbeite elmem<" + i + "><" + j + ">");
					int cc=elem.getCopycounter();
					copycountsum=copycountsum+cc;
					
				}
				
			}
			
		}
		return copycountsum;
	}
	
	public double WriteProtokoll1()
	{
		double cor_sum = 0;
		int anz_values = 0;
		
		int x = 0, y = 0;
		
		FileAccess.FileDelete(protokollfileGlob, 1);
		
		Inf inf = new Inf();
		inf.setFilename(protokollfileGlob);
		
		
		
		String zeile = "";
		for (int j = 0; j < 100; j++)
		{
			for (int i = 0; i < 100; i++)
			{
				WekaClassifierElem elem = matrix[i][j];
				if (elem != null)
				{
					//Tracer.WriteTrace(20, "I:bearbeite elmem<" + i + "><" + j + ">");
					Double val = round(elem.getCorrelationVal());
					cor_sum = cor_sum + val;
					anz_values++;
					
					zeile = zeile + val + "\t";
				}
				
			}
			inf.appendzeile(protokollfileGlob, zeile, true);
			zeile = "";
		}
		inf.appendzeile(protokollfileGlob, "========================", true);
		inf.appendzeile(protokollfileGlob, "average correlation=" + cor_sum / anz_values, true);
		Tracer.WriteTrace(20, "average correlation=" + cor_sum / anz_values);
		inf.close();
		return round(cor_sum/anz_values);
		
	}
	
	
	public void WriteProtokoll2Profit(String outfile)
	{
		int x = 0, y = 0;
		FileAccess.FileDelete(outfile, 1);
		Inf inf = new Inf();
		inf.setFilename(outfile);
		
		
		for (int j = 0; j < 100; j++)
		{
			for (int i = 0; i < 100; i++)
			{
				WekaClassifierElem elem = matrix[i][j];
				if (elem != null)
				{
					
					Double val = round(elem.getSelectedFilesProfit());
					if(val!=0)
					inf.appendzeile(outfile,"period<"+j+"|"+i+">  allProfitval<"+elem.getAllFilesProfit()+"> selProfit \t\t<"+val+"> anzStrat<"+elem.getCopycounter()+">",true);
				}
				
			}
			
		}
	}
	public void WriteProtokoll3Values(String outfile)
	{
		int x = 0, y = 0;
		FileAccess.FileDelete(outfile, 1);
		Inf inf = new Inf();
		inf.setFilename(outfile);
		
		
		for (int j = 0; j < 100; j++)
		{
			for (int i = 0; i < 100; i++)
			{
				WekaClassifierElem elem = matrix[i][j];
				if (elem != null)
				{
					
					Double val = round(elem.getSelectedFilesProfit());
					if(val!=0)
					inf.appendzeile(outfile,i+"#"+ val,true);
				}
				
			}
			
		}
	}
	
	private double round(double value)
	{
		return Math.round(value * 100.0) / 100.0;
	}
}