/**
 * 
 */
package data;

import hiflsklasse.FileAccess;
import hiflsklasse.Tracer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

/**
 * @author tnickel
 * 
 */
public class TradelistenZeitSpeicherInit
{
	// diese Datenstruktur beinhaltet alle Tradelisten fuer ein einzelnen
	// Filename/strategienamen
	// Es wird hier also immer nur ein einzelner Filenamen betrachtet

	// dies Array von tradelisten speichert f�r einen bestimmten EA/.str die n
	// Tradelisten
	ArrayList<Tradeliste> tralist = new ArrayList<Tradeliste>();

	// das ist die liste mit den verzeichnissen, jedes verzeichniss steht
	// f�r eine bestimmte
	// kursdatenreihe
	ArrayList<String> dirlist = new ArrayList<String>();

	public TradelistenZeitSpeicherInit(String rootdir, String filenamen)
	{
		// der Tradelistenzeitspeicher wird hier f�r einen filenamen f�r alle
		// Indexe aufgebaut
		// Man baut den Zeitspeicher am anfang einmal auf um dann schnell die
		// hits der trades zu ermitteln

		// rootdir: dies ist das Rootverzeichniss in dem sich s�mtliche
		// unterverzeichnisse befinden wie z.B.
		// 1, 2,... out
		tralist.clear();
		dirlist.clear();
		FileAccess.initFileSystemList(rootdir, 0);
		int anzdir = FileAccess.holeFileAnz();

		// 1) hole die Namen aller verzeichnisse
		for (int i = 0; i < anzdir; i++)
		{
			String nam = FileAccess.holeFileSystemName();
			if (nam.equalsIgnoreCase("out"))
				continue;
			// alle verzeichnisse ausser des outverzeichnisses in einer Liste
			// Speichern
			dirlist.add(nam);
		}

		// und jetzt baue noch die n anderen tradelisten auf damit contain
		// opentime funktoiniert

		// hole den n�chsten Verzeichnissnamen
		// Tolleranzliste f�r index 0 wird nicht gebraucht, es wird aber trotzdem berechnet um das 
		// array mit den richtigen Indizes zu f�llen
		for (int i = 0; i < anzdir; i++)
		{
			String nextdir = dirlist.get(i);
			Tradeliste eatl = new Tradeliste(null);
			String fna=rootdir+"\\"+nextdir + "\\" + filenamen;
			eatl.initBacktest(fna);
			Tracer.WriteTrace(20, "I:Build tolleranzliste fuer strat<"+filenamen+">");
			// hier werden die aufgeweichten tradelisten ermittelt, diese werden
			// ben�tigt um die hits zu berechnen
			// ein aufgeweichter Trade beinhaltet die open/closezeiten +- anz
			// Minuten
			// GMT=0, anzMinuten =5
			eatl.buildTolleranzlisten(0, 5);
			// Speichere die n�chste Tradeliste f�r einen bestimmten filenamen
			// in der
			// globalen liste
			tralist.add(eatl);
		}
	}

	public int getHitcount(int hitreihe, Trade tr)
	{
		// hitreihe: ist der index 1-n wo die hitreihe im zentralen speicher
		// gespeichert ist
		// tr: f�r diesen Trade m�chte man die anzahl hits ermitteln, das
		// maximum sind 2 hits

		// hole die tradeliste
		Tradeliste trlist = tralist.get(hitreihe);
		// aus der Tradeliste kann man f�r einen trade die hits ermitteln
		return (trlist.countHitTrades(tr));
	}
}
