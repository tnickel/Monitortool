package Metriklibs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Display;

import gui.MultiTextFileViewer;
import hiflsklasse.Tracer;

public class CorelationMapper
{
	//was beinhaltet der CorrelationMapper
	//Aufgabe des Correlationsmappers ist es die Wichtigkeit der einzelnen Attribute festzuhalten
	//Das Festhalten wird schön fein säuberlich zwichen den _n_dirs getrennt. Deswegen wird zur trennung
	//@_n_dir nummer verwendet.
	
	
	//Dies map beinhaltet die Ergebnisse der Workflows
	//Wir wollen wissen welche Attribute wie wichtig sind
	//Dies machen wir getrennt für jedes _n_dir
	//Was beinhaltet diese Hashmap?:
	//String ist der Attributname+eine Kennung für das _n_dir
	//CoreResultStatistikElem = Hier steckt die ganze Statistik für dieses Attribut drin
	HashMap<String, CoreResultStatistikElem> map = new HashMap<>();
	
	public void addListe(CoreWorkflowResult workflowresult)
	{
		// workflowresult=hier stecken alle korrelationen über den workflow drin
		// workflown= die nummer vom workflow ist eigentlich unwichtig, höchstens zu loggen
		// Was für uns wichtig ist die nummer des subdirs innerhalb eines workflows
		// subdirnummer= diese Nummer mussen wir uns aus dem workflowresult holen.
		CoreResultStatistikElem crs = null;
		
		// dir_n nummer
		for (int subdirnummer = 0; subdirnummer < 100; subdirnummer++)
		{
			Corresultliste cr = workflowresult.getListe(subdirnummer);
			
			if (cr == null)
				continue;
			int anzattribs = cr.getSize();
			
			// gehe jetzt durch die attribute
			for (int j = 0; j < anzattribs; j++)
			{
				Corelresultelem ce = cr.getElem(j);
				String attrib = ce.getAttribname();
				double newval = ce.getVal();
				
				//jedes Element wird anhand des Namens identifiziert. wir haben hier noch eine subnummer. Diese brauchen wir weil wir
				//hier die analysen für jeden _x_dir getrennt machen wollen.
				//Also die attribute werden über alle Workflows aufsummiert, allerdings die subdirs werden hierbei sauber getrennt.
				String key = attrib + "@" + subdirnummer;
				
				// wenn das objekt schon da ist, dann hole es
				if (map.containsKey(key) == true)
					crs = map.get(key);
				else // lege objekt an
					crs = new CoreResultStatistikElem();
				
				// die corsumme für das attribut erhöhen
				double corsumme = crs.getCorsumme() + newval;
				crs.setCorsumme(corsumme);
				//wir wollen hier infostrings abspeichern, wir wollen wissen wie die corsumme zustande gekommen ist
				crs.ExtendExplainString(newval);
				// den counter erhöhen;
				int counter = crs.getAnzvorkommen() + 1;
				crs.setAnzvorkommen(counter);
				
				// wenn das neue objekt gut war.
				if (newval > 0.15)
				{
					int gcounter = crs.getAnzgut() + 1;
					crs.setAnzgut(gcounter);
				}
				// wenn das neue extrem schlecht war.
				if (newval < -0.15)
				{
					int bcounter = crs.getAnzbad() + 1;
					crs.setAnzbad(bcounter);
				}
				
				crs.calcStabil();
				
				// das modifizierte objekt wieder zurückputten
				map.put(key, crs);
			}
		}
	}
	public Attribliste CalcgoodAttribliste(int mingoodbad,double stabilmax)
	{
		//wir brauchen eine liste mit den attributen mit hoher korrelation und hohem vorkommen.
		//ein element ist gut wenn es mindestens 5 mal über dem schwellwert von 0.15 war und
		//eine summenkorrelation von 1 aufweisst.
		//Summenkorrelation ist die aufsummierung über alle workflows für eine bestimmte metrik
		
		Attribliste attriblist = new Attribliste();
		
		Tracer.WriteTrace(20, "#attribute before reduction =<"+map.size()+">");
		
		for (Map.Entry<String, CoreResultStatistikElem> entry : map.entrySet())
		{
			String key = entry.getKey();
			CoreResultStatistikElem statElem = (CoreResultStatistikElem) entry.getValue();
			double stabil=statElem.getStabil();

			//Jetzt wird geprüft ob das Element eine hohe correlation auffweisst
			
			double corsum=Math.abs(statElem.getCorsumme());
			int anz=Math.max(statElem.anzgut,statElem.anzbad);
			if((anz>mingoodbad)&&(stabil<stabilmax))
			{
				attriblist.add(key.replace("@", "").replace(" ", "_"));
				Tracer.WriteTrace(20 , "I: Erweitere gutliste attrib<"+key+"> sum<"+corsum+"> anz<"+anz+">");
			}
			
		
		}
		Tracer.WriteTrace(20, "#attribute after reduction =<"+attriblist.getSize()+">");
		return attriblist;
		
	}
	public Statliste generateWorkflowResults()
	{
		Statliste stat=new Statliste();
		// hier wird ausgewertet, d.h. aus der Map wird eine Liste gemacht. Die Statliste
		Tracer.WriteTrace(20, "I: The hashmap contains <"+map.size()+"> elements");
		for (Map.Entry<String, CoreResultStatistikElem> entry : map.entrySet())
		{
			String key = entry.getKey();
			CoreResultStatistikElem statElem = (CoreResultStatistikElem) entry.getValue();
			Tracer.WriteTrace(50, "Key: " + key + ", #= " + statElem.anzvorkommen+" #good="+statElem.anzgut+" cor="+statElem.getCorsumme());

			statElem.setAttribname(key);
			stat.addElem(statElem);
		}
		return stat;
	}
	public void showallWorkflowResults(ArrayList<String> filePaths)
	{
		MultiTextFileViewer mt = new MultiTextFileViewer();
		mt.showAll(filePaths,Display.getDefault().getActiveShell());
	}
}
