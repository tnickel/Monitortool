package attribute;

import objects.ThreadDbObj;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.SlidingCategoryDataset;

import stores.ThreadsDB;

public class Attrib
{
	//zeigt die Attribute in einer Uebersicht
	public void zeigeAttributsuebersicht()
	{
		SlidingCategoryDataset dataset;
		
		//zeige eine Attributsübersicht mit JfreeChart an
		ThreadsDB tdb = new ThreadsDB();
		int anz=tdb.GetanzObj();
		
		dataset = new SlidingCategoryDataset(createDataset(), 0,	 30);
	
		//hier wird das darstellbare dataset generiert !!
		for(int i=0; i<anz; i++)
		{
			ThreadDbObj tdbo=new ThreadDbObj();
			int tid=tdbo.getThreadid();
			ThreadAttribStoreI ta=new ThreadAttribStoreI(tid,"\\db\\Attribute\\Threads");
			
			
			
			//Holt die Werte und baut das Dataset auf
			//defaultdataset.addValue(0.5 * 100.0, "mmm", "xxxx " + i);
		}
		Diagramm1 di1=new Diagramm1("Start",dataset);
		di1.start();
	}
	private static CategoryDataset createDataset()
	{
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < 5000; i++)
		{
			dataset.addValue(Math.random() * 100.0, "S1", "Series " + i);
		}
		return dataset;
	}
}
