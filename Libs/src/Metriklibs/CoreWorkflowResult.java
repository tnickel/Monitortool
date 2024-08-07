package Metriklibs;

import java.util.ArrayList;
import java.util.List;

import hiflsklasse.Tracer;

import java.util.ArrayList;
import java.util.List;

public class CoreWorkflowResult
{
	// Hier werden die Ergebnisse für einen Workflow gespeichert
	private List<Corresultliste> workflowresult;
	
	public CoreWorkflowResult()
	{
		// Initialisierung der ArrayList
		workflowresult = new ArrayList<Corresultliste>();
	}
	
	public void addListe(Corresultliste cliste, int i)
	{
		if (i > 99)
		{
			Tracer.WriteTrace(10, "E: Max 100 subdirs in workflow allowed");
			return;
		}
		// Sicherstellen, dass die Liste groß genug ist
		while (workflowresult.size() <= i)
		{
			workflowresult.add(null);
		}
		// Das Ergebnis an der entsprechenden Position speichern
		workflowresult.set(i, cliste);
	}
	
	public Corresultliste getListe(int i)
	{
		if (i < 0 || i >= workflowresult.size())
		{
			return null; // Index außerhalb des gültigen Bereichs
		}
		return workflowresult.get(i);
	}
}
