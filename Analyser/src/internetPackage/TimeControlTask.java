package internetPackage;

import hilfsklasse.Tracer;

import java.util.TimerTask;

public class TimeControlTask extends TimerTask
{
	// Diese Klasse setzt einen �berwachungstreiber f�r einen Downloadprozess
	// Wenn dieser Timer ausgel�st wird wird der Zugeh�rige Thread gekillt.
	private LoadWebpageThread thr = null;
	private int timernummer = 0;

	@Override
	public void run()
	{
		// der Timer hat ausgel�st und der Prozess wird gekillt
		Tracer.WriteTrace(20, "Timer=<" + timernummer
				+ "> Zeit abgelaufen, hier wird gekillt !!!");
		thr.interrupt();
		thr = null; // setze die struktur wieder zu 0, damit syncend wieder was
					// leeres findet

	}

	public void SetTPointer(LoadWebpageThread p, int tnummer)
	{
		// hier wird der pointer zum Thread zugewiesen
		// der Thread wird gespeichert damit man weiss was man
		// nachher killen kann
		// Die Timernommer wird nur f�r die Ausgabe ben�tigt, damit
		// man weiss welcher Timer �berschritten wurde.
		thr = p;
		timernummer = tnummer;
	}

	@Override
	public String toString()
	{
		return ("timer-number<" + timernummer + ">");
	}

}
