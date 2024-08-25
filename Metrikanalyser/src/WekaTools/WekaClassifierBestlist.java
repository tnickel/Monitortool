package WekaTools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import FileTools.Filefunkt;
import hiflsklasse.Tracer;

public class WekaClassifierBestlist
{
	private boolean copyflag_glob = false;
	private ArrayList<ClassifiedElem> bestlist = new ArrayList<ClassifiedElem>();
	private int copycounter_glob = 0;
	private int selectedStrategiesCounter_glob = 0;
	//private double sumSelectedProfits_glob = 0;
	private double minprofit_glob = 0;
	
	public WekaClassifierBestlist()
	{
	}
	
	public void setCopyflag(boolean c)
	{
		copyflag_glob = c;
		
	}
	
	public int getCopycounter()
	{
		return copycounter_glob;
	}
	
	public int getAnzSelStrategies()
	{
		return selectedStrategiesCounter_glob;
	}
	
	public void add(String sourcepath, String destpath, double predictval, double aktval)
	{
		ClassifiedElem ce = new ClassifiedElem();
		ce.setSourcepath(sourcepath);
		ce.setDestpath(destpath);
		ce.setPredictValue(predictval);
		ce.setActualValue(aktval);
		bestlist.add(ce);
		
	}
	
	public double getSumSelectedProfits()
	{
		double sum_profi=0;
		int anz = bestlist.size();
		for (int i = 0; i < anz; i++)
		{
			ClassifiedElem ce = bestlist.get(i);
			if(ce.getSelected()==1)
			{
				double aktval = ce.getActualValue();
				sum_profi=sum_profi+aktval;
			}
		}
		return sum_profi;
	}
	
	public void setMinprofit(double min)
	{
		minprofit_glob = min;
	}
	
	public void filterBest(String mode, int takebestn)
	{
		// number=
		
		if (mode.equals("minval"))
			filterMinval();
		else if (mode.equals("takebest"))
			filterBest(takebestn);
		else
			Tracer.WriteTrace(10, "unknown mode <" + mode + ">");
	}
	
	private void filterBest(int maxbest)
	{
		
		//Nimm die n besten aus der sortierten bestliste
		
		double sum=0;
		// Sortieren mit Comparable
		Collections.sort(bestlist);
		int anz = bestlist.size();
		for (int i = 0; i < maxbest; i++)
		{
			ClassifiedElem ce = bestlist.get(i);
			double aktval = ce.getActualValue();
			ce.setSelected(1);
			
			sum=sum+aktval;
			String stratname=ce.getSourcepath();
			stratname=stratname.substring(stratname.lastIndexOf("\\"));
			Tracer.WriteTrace(20, "<"+i+"> of 10 best: name<"+stratname+"> aktval<"+aktval+">");
			if(i==maxbest-1)
				Tracer.WriteTrace(20, "name<"+stratname+"> sum<"+sum+">");
			selectedStrategiesCounter_glob++;
			
		}
		Tracer.WriteTrace(20, "xready");
	}
	
	private void filterMinval()
	{
		
		int anz = bestlist.size();
		for (int i = 0; i < anz; i++)
		{
			ClassifiedElem ce = bestlist.get(i);
			if (ce.getPredictValue() > minprofit_glob)
			{
				ce.setSelected(1);
				selectedStrategiesCounter_glob++;
			}
		}
	}
	
	public void copyFiles()
	{
		int anz = bestlist.size();
		copycounter_glob = 0;
		
		for (int i = 0; i < anz; i++)
		{
			ClassifiedElem ce = bestlist.get(i);
			if (ce.getSelected() == 0)
				continue;
			copycounter_glob++;
			String s1path = ce.getSourcepath();
			String s2path = ce.getDestpath();
			
			if (new File(s1path).exists())
			{
				Filefunkt fc = new Filefunkt();
				if (copyflag_glob == true)
				{
					try
					{
						fc.copyFile(new File(s1path), new File(s2path));
						
						
					} catch (IOException e)
					{
						// TODO Auto-generated catch block
						
						e.printStackTrace();
						Tracer.WriteTrace(20, e.getMessage());
						Tracer.WriteTrace(10, "E:error copy file <" + s1path + "> to <" + s2path + ">");
					}
					
				}
			}
			
		}
		if (copyflag_glob == true)
			Tracer.WriteTrace(20, "I: I have copied <" + copycounter_glob + "|" + anz + "> strategies");
		else
			Tracer.WriteTrace(20, "I: #selected strategies<" + selectedStrategiesCounter_glob + "|" + anz + ">");
	}
	
}
