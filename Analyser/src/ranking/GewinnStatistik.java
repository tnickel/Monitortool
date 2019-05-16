package ranking;

import hilfsklasse.Inf;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import db.CompressedPostingObj;

public class GewinnStatistik
{
	// einige Variablen für die Statistikauswertung
	private String lastthreadname = null;
	private int slasttid[] = new int[10];
	private int lpostid = 0;
	private float sumsumverlust[] = new float[10];
	private float sumsumgewinn[] = new float[10];
	private float sumgewinn[] = new float[10];
	private float sumverlust[] = new float[10];
	private float sum1daygewinn = 0; // jeden Tag nur maximal einmal rechnen
	private float sum1dayverlust = 0;
	private float sumsum1daygewinn = 0;// summe über alle aktien
	private float sumsum1dayverlust = 0;
	private int anzpostings = 0;
	private int anzgewinnpostings[] = new int[10];
	private int anzverlustpostings[] = new int[10];
	private int anznullpostings[] = new int[10];

	// andere interne daten
	private String lastdate_g = "";

	public void speichereGewinnStatistik(int index, Inf infMaster,
			String threadname, CompressedPostingObj copost, float teilgewinn,
			int threadpos, int posmax, String aktthreadnamz, String symb)
	{
		// diese Funktion speichert zusatzwerte für einen Thread
		// Dies Info wird am Ende des Scannvorganges für eines Thread in
		// ...UserThreadVirtualKonto/uebersicht.txt
		// an den Aktienwert angehangen

		// werte nur index 0 aus (Verfahren 0=> naives verfahren)
		if (index != 0)
			return;

		String datum = Tools.entferneZeit(copost.getDatetime());

		int stid = copost.getThreadid();
		int postid = copost.getPostid();

		if ((stid != slasttid[index])||(lastthreadname.equalsIgnoreCase(threadname)==false))
		{
			// Es kommt ein neuer Thread

			// Schreibe Statistikdaten für den letzten Thread
			infMaster.writezeile("--<" + lastthreadname + "> #post<"
					+ anzpostings + "> #threadgew<" + anzgewinnpostings[index]
					+ "> SumGew<" + sumgewinn[index] + "> sumverlustpost<"
					+ anzverlustpostings[index] + "> SV<" + sumverlust[index]
					+ "> #0<" + anznullpostings[index] + "> SumSumGew<"
					+ sumsumgewinn[index] + "> SumSumVerl<"
					+ sumsumverlust[index] + ">");
			infMaster.writezeile("--< sum1daygew<" + sum1daygewinn
					+ "> sum1dayverl<" + sum1dayverlust + "> sumsum1daygew<"
					+ sumsum1daygewinn + "> sumsum1dayverl<"
					+ sumsum1dayverlust + ">");
			// Info für den aktuellen Thread
			infMaster.writezeile("(" + threadpos + "|" + posmax
					+ ")Berechne neues virt Konto für thread<" + aktthreadnamz
					+ "> symb<" + symb + "> ");

			// ab jetzt ist dies der neue Thread
			slasttid[index] = stid;
			lastthreadname = threadname;
			lastdate_g = "";

			// lösche Variablen
			anzpostings = 0;
			anzgewinnpostings[index] = 0;
			anzverlustpostings[index] = 0;
			anznullpostings[index] = 0;
			sumgewinn[index] = 0;
			sumverlust[index] = 0;
			sum1daygewinn = 0;
			sum1dayverlust = 0;
			lpostid = 0;
		} else
		{
			// Dies ist noch der alte Thread

			// plausichecks
			if (threadname.equals(lastthreadname) == false)
				Tracer.WriteTrace(10,
						"Error:plausicheck threadnamen müssen gleich sein tid<"
								+ stid + "> lasttid<" + slasttid[index]
								+ "> threadname<" + threadname
								+ "> lastthreadname<" + lastthreadname + "> index<"+index+">");

			if (postid <= lpostid)
				Tracer.WriteTrace(10, "Error:plausicheck postid<" + postid
						+ "> <= lastpostid<" + lpostid + "> threadname<"
						+ threadname + "> tid<" + lastthreadname + ">");

			if (datum.equalsIgnoreCase(lastdate_g) == false)
			{
				// ein neuer Tag
				lastdate_g = datum;
				if (teilgewinn > 0)
				{
					sum1daygewinn = sum1daygewinn + teilgewinn;
					sumsum1daygewinn = sumsum1daygewinn + teilgewinn;
				}
				if (teilgewinn < 0)
				{
					sum1dayverlust = sum1dayverlust + teilgewinn;
					sumsum1dayverlust = sumsum1dayverlust + teilgewinn;
				}
			}

			if (teilgewinn > 100000)
			{

				Tracer.WriteTrace(20,
						"Warning:plausicheck teilgewinn zu hoch, wohl ein Fehler ?<"
								+ teilgewinn + "> tname<" + threadname
								+ "> postid<" + postid
								+ "> wird nicht gewertet!");

			}
			anzpostings++;
			// zaehle nur gewinne !!
			if (Math.abs(teilgewinn) < 0.01)
			{
				anznullpostings[index]++;
			} else if (teilgewinn > 0)
			{
				anzgewinnpostings[index]++;
				sumsumgewinn[index] = sumsumgewinn[index] + teilgewinn;
				sumgewinn[index] = sumgewinn[index] + teilgewinn;
			} else if (teilgewinn < 0)
			{
				sumsumverlust[index] = sumsumverlust[index] + teilgewinn;
				anzverlustpostings[index]++;
				sumverlust[index] = sumverlust[index] + teilgewinn;
			} else
			{
				Tracer.WriteTrace(10, "Error: internal error Teilgewinn<"
						+ teilgewinn + ">");
			}
		}
	}
}
