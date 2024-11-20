package WekaTools;

import Metriklibs.FileAccessDyn;
import data.Metrikglobalconf;
import gui.JLibsProgressWin;
import hiflsklasse.BasicWekaTools;
import hilfsklasse.Tracer;
import weka.core.Instances;

public class WekaTabTools extends BasicWekaTools
{
	public void attributeReduction(String inputname, String outputname,String methode)
	{
		FileAccessDyn fdirs = new FileAccessDyn();
		fdirs.initFileSystemList(Metrikglobalconf.getFilterpath(), 0);
		
		
		
		int anz = fdirs.holeFileAnz();
		// gehe durch alle workflows
		
		try
		{
			
			int progcount = 0;
			JLibsProgressWin jp = new JLibsProgressWin("Reduce Metrics", 0, anz);
			for (int i = 0; i < anz; i++)
			{
				WekaAttributeReduction wekareduct = new WekaAttributeReduction();
				String workdir = (Metrikglobalconf.getFilterpath() + "\\" + fdirs.holeFileSystemName()).toLowerCase();
				String inputfile = workdir + "\\" + inputname ;
				String outputfile = workdir + "\\" + outputname ;
				jp.update(i);
				if (isValidWorkdir(workdir) == false)
					continue;
				Instances reducedData = wekareduct.reduceAttributes(inputfile,methode);
				// Speichern der reduzierten Daten
				
				wekareduct.saveReducedData(reducedData, outputfile);
				
			}
			jp.end();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Tracer.WriteTrace(20, "All workflows exported");
	}
}
