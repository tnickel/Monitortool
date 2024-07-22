package WekaTools;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import hiflsklasse.FileAccess2;
import hilfsklasse.Tracer;
import weka.classifiers.Evaluation;

public class WekaResultCollector
{
	private ArrayList<WekaResultElem> results;
	private String protokollfile_glob = null;
	
	public WekaResultCollector(String protokollfile)
	{
		if (protokollfile == null)
			Tracer.WriteTrace(10, "E:protokollfile is not set !--> stop");
		
		if (new File(protokollfile).exists())
			if (new File(protokollfile).delete() == false)
				Tracer.WriteTrace(10, "E:Can´t delte file <" + protokollfile + ">");
		protokollfile_glob = protokollfile;
		
		results = new ArrayList<>();
	}
	
	public void addWekaRespDir(int index, String dir)
	{
		WekaResultElem wr = getOrCreateResultElem(index);
		wr.setDirname(dir);
	}
	
	public void addWekaEvalResp(int index, Evaluation eval,String forest,String data)
	{
		WekaResultElem wr = getOrCreateResultElem(index);
		wr.setEvaluation(eval,forest,data);
		
	}
	
	private WekaResultElem getOrCreateResultElem(int index)
	{
		ensureCapacity(index);
		WekaResultElem wr = results.get(index);
		if (wr == null)
		{
			wr = new WekaResultElem();
			results.set(index, wr);
		}
		return wr;
	}
	
	private void ensureCapacity(int index)
	{
		// stellt die kapazität sicher
		results.ensureCapacity(index + 1);
		while (index >= results.size())
		{
			results.add(null);
		}
	}
	
	public void writeProtokoll()
	{
		FileAccess2 fa2 = new FileAccess2();
		
		int anz = results.size();
		for (int i = 0; i < anz; i++)
		{
			WekaResultElem res = results.get(i);
			// fa2.appendZeile(protokollfile_glob, "Results for Learning
			// below"+res.getDirname(), true);
			fa2.appendZeile(protokollfile_glob,
					"Results for Learning below" + res.getEvaluation().toSummaryString(res.getDirname(), false), true);
			fa2.appendZeile(protokollfile_glob,
					"forest=" + res.getForestpath(),true);
			fa2.appendZeile(protokollfile_glob,
					"data=" + res.getDatapath(),true);
			fa2.appendZeile(protokollfile_glob,
					"====================================================" ,true);
		}
		double avgsum=0;
		for (int i = 0; i < anz; i++)
		{
			try
			{
				DecimalFormat df = new DecimalFormat("#.00");
				WekaResultElem res = results.get(i);
				String dirnam=res.getDirname();
				dirnam=dirnam.substring(dirnam.indexOf("_"));
				
				String corvalstring=df.format(res.getEvaluation().correlationCoefficient());
				
				
				fa2.appendZeile(protokollfile_glob, dirnam+"\t\t="+corvalstring, true);
				avgsum=avgsum+Math.abs(res.getEvaluation().correlationCoefficient());
				
				
				
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		fa2.appendZeile(protokollfile_glob,"-------------------------------------------",true);
		fa2.appendZeile(protokollfile_glob,"Avg correlation ="+avgsum/anz,true);
		fa2.close();
		
	}
}