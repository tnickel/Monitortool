package stores;

import hilfsklasse.SG;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;

import java.util.HashSet;

import objects.Obj;
import objects.SymbolErsetzungsObj;
import db.DB;

public class SymbErsetzungsDB extends DB
{
	public SymbErsetzungsDB()
	{
		LoadDB("symbolersetzung", null, 0);
		// mache konsistenzcheck !!!
		// d.h. jedes Symbol darf nur einmal vorkommen
		SymbolKonsistenzcheck();
		
	}
	public boolean plausiVorNeuaufnahme(Object obj)
	{
		return true;
	}
	public int compareTo(Obj argument)
	{
		return 0;
	}

	public SymbolErsetzungsObj SearchSymbolObj(String symb)
	{
		// sucht das Symbol in der Translatortabelle und gibt ggf. das
		// Translatorobjekt zurück
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			SymbolErsetzungsObj sytra = (SymbolErsetzungsObj) this
					.GetObjectIDX(i);
			for (int j = 0; j < 3; j++)
			{
				String sy = sytra.getSymbol(j);
				if (sy.equalsIgnoreCase(symb) == true)
					return sytra;
			}
		}
		return null;
	}

	public SymbolErsetzungsObj SearchMidObj(int mid)
	{
		// sucht nach MidObj in der Translatortabelle und gibt ggf. das
		// Translatorobjekt zurück
		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			SymbolErsetzungsObj sytra = (SymbolErsetzungsObj) this
					.GetObjectIDX(i);

			int m = sytra.getMasterid();
			// Objekt mit Mid gefunden
			if (mid == m)
				return sytra;
		}
		return null;
	}

	public boolean istVorhanden(String symb)
	{
		if (SearchSymbolObj(symb) != null)
			return true;
		else
			return false;
	}

	public boolean wechseleMasterid(int oldmaster, int newmaster, String symbol)
	{
		Boolean ret = false;
		SymbolErsetzungsObj symbErsetzungObj = null;

		if (oldmaster == newmaster)
		{
			Tracer.WriteTrace(20,
					"Warning: masteridwechsel nicht möglich masterid´s gleich von<"
							+ oldmaster + "> nach<" + newmaster + ">");
			return false;
		}
		if (Tools.isKorrektSymbol(symbol) == false)
			Tracer.WriteTrace(10, "Error: dies ist kein korrektes Symbol<"
					+ symbol + "> für den Masteridwechsel von<" + oldmaster
					+ "> nach<" + newmaster + ">");

		Tracer.WriteTrace(20, "Info: Masteridwechsel midold<" + oldmaster
				+ "> midnew<" + newmaster + "> symb<" + symbol + ">");

		
		// gehe hier restriktiver vor, übersetze einfach alle alten MID´s durch
		// neue
		if ((symbErsetzungObj = this.SearchMidObj(oldmaster)) != null)
		{
			symbErsetzungObj.setMasterid(newmaster);
			this.WriteDB();
		} else
		{
			Tracer.WriteTrace(20,
					"ERROR: mid nicht gefunden im translator,Masteridwechsel midold<"
							+ oldmaster + "> midnew<" + newmaster + "> symb<"
							+ symbol + ">");
			return false;
		}

		// schaue nach ob die alte mid nochmal vorhanden ist
		if ((symbErsetzungObj = this.SearchMidObj(oldmaster)) != null)
			Tracer.WriteTrace(10, "mid nocheinmal in symboltranslator oldmid<"
					+ oldmaster + ">, internal");

		SymbolKonsistenzcheck();
		return true;
	}

	public boolean erweitereSymbolersetzung(int mid, String alt_symb,
			String alt_boer, String neu_symb, String neu_boer)
	{
		// Fall 1: 'alt_symb' ist bekannt
		// Fall 1a)Passende Ersetzung schon in Tabelle -> ist schon alles ok
		// (nix doppelt moppeln)
		// Fall 1b)Die Zeile ist noch nicht passend
		// Fall 1bx1)Das neue Symbol ist schon an anderer Stelle-> tabelle
		// korrupt
		// Fall 1bx2)Neue Symbol ist ganz neu (noch nicht in tabelle) -> nimm
		// auf

		// Fall 2: 'alt_symb' ist neu->nimm auf
		// Fall 2a) 'neu_symb' ist schon in Tabelle, dann nimm auf (Sind jetzt 3
		// Symbole in Zeile)
		// Fall 2b) 'neu_symb' neu, -> nimm auf (als 2er Ersetzung)
		Boolean ret = false;
		SymbolErsetzungsObj symbErsetzungObj = null;

		if ((SG.is_zahl(neu_symb) == true)
				|| (SG.is_zahl(alt_symb) == true))
		{// wird nicht angepasst da symbol eine Zahl ist
			Tracer.WriteTrace(20,
					"Warning: Symbolersetzung wird nicht angepasst da ein symbol zahl ist,mid<"
							+ mid + "> alt_symb<" + alt_symb + ">alt börse<"
							+ alt_boer + "> neu_symb<" + neu_symb
							+ "> neu börse<" + neu_boer + ">");
			return false;
		}
		Tracer.WriteTrace(20, "Info: Symbolersetzung wird angepasst mid<" + mid
				+ "> alt_symb<" + alt_symb + ">alt börse<" + alt_boer
				+ "> neu_symb<" + neu_symb + "> neu börse<" + neu_boer + ">");

		// Fall 1:falls schon eine Symbolersetzung für dies Symbol 'alt_symb'
		// gibt
		if ((symbErsetzungObj = this.SearchSymbolObj(alt_symb)) != null)
		{
			// Es existiert also schon eine Symbolersetzung, prüfe jetzt nach ob
			// das neue Symbol in dieser
			// Ersetung vorkommt oder im Rest der Tabelle

			// Fall 1a) alles passend:das neue Symbol ist bereits passend in der
			// Ersetzungszeile
			if (symbErsetzungObj.hasSymbol(neu_symb) == true)
			{
				Tracer.WriteTrace(20,
						"Warning: Passende Symbolersetzung schon vorhanden trans<"
								+ symbErsetzungObj.toString()
								+ ">, mache nix doppelt da schon da");
				ret = true;
			} else
			// Fall 1b)neues Symbol ist nicht in 'der' Ersetzungzeile
			{
				// 1bb)das neue Symbol ist an einer anderen Stelle in der
				// gesammten
				// Tabelle
				if (this.istVorhanden(neu_symb) == true)
				{
					// sowas darf nie passieren !!!!
					Tracer
							.WriteTrace(
									20,
									"Warning: Korrupte Symbolersetzungstabelle, neues Symbol <"
											+ neu_symb
											+ ">, schon in der Symbolersetzungstabelle, aber unter einer andern pos");
					return false;
				} else
				{// Fall 1bx2): neue Symbol ist noch nicht in Tabelle, alles ok,
					// dann
					// nimm auf
					ret = symbErsetzungObj
							.addNewSymbol(mid, neu_symb, neu_boer);
				}
			}
		} else
		{// Fall2: das alte Symbol 'alt-symb 'ist Brandneu

			// prüfe nach ob altes und neues Symbol noch nicht in der
			// Symbolersetzung sind !!!

			// hier nur ein Plausi
			if (this.istVorhanden(alt_symb) == true)
				Tracer.WriteTrace(10, "internal Error: Plausi Symbol <"
						+ alt_symb + "> ist schon in der Symbolersetzung");

			// Fall2a: prüfe ob das neue Symbol schon in der Tabelle
			if (this.istVorhanden(neu_symb) == true)
			{
				// also: das alte Symbol ist brandneu und das neue ist aber
				// schon in der Tabelle
				// -> dann nimm das alte Symbol hinzu
				if ((symbErsetzungObj = this.SearchSymbolObj(neu_symb)) != null)
				{
					symbErsetzungObj.addNewSymbol(mid, alt_symb, alt_boer);
					Tracer.WriteTrace(20,
							"Info:jetzt 3 Symbole in der Symbolersetzung zeile<"
									+ symbErsetzungObj.toString() + ">");
				} else
					Tracer.WriteTrace(10,
							"Error:internal Resultat von .istVorhanden symb<"
									+ neu_symb + "> != SearchSymbolObj");

			}
			// Fall 2b: neue Symbol noch nicht drin, dann generiere
			else
			{
				SymbolErsetzungsObj symbobj = new SymbolErsetzungsObj(mid + "#"
						+ alt_symb + "#" + alt_boer + "#x#x#" + neu_symb + "#"
						+ neu_boer + "#x#x#0#0#0#0#0");
				this.AddObject(symbobj);
				ret = true;
			}
		}
		this.WriteDB();
		SymbolKonsistenzcheck();
		return ret;
	}

	private void SymbolKonsistenzcheck()
	{
		// prüfe nach ob jedes Symbol nur einmal in der Symbolersetzungstablle
		// vorkommt
		// wenn nicht liegt ne Inskonsistenz vor !!
		HashSet<String> symbolmenge = new HashSet<String>();

		int anz = this.GetanzObj();
		for (int i = 0; i < anz; i++)
		{
			SymbolErsetzungsObj sytra = (SymbolErsetzungsObj) this
					.GetObjectIDX(i);
			for (int j = 0; j < 3; j++)
			{
				String symb = sytra.getSymbol(j);
				if (symb == null)
					continue;
				if (symb.length() < 2)
					continue;
				if (Tools.isKorrektSymbol(symb) == false)
					Tracer.WriteTrace(10, "Error: Symbol <" + symb
							+ "> ist nicht korrekt in symboltranslatortabelle");

				if (symbolmenge.contains(symb) == false)
					symbolmenge.add(symb);
				else
					Tracer.WriteTrace(10, "Error: Symbol <" + symb
							+ "> doppelt in symboltranslatortabelle");
			}
		}
		// Tracer.WriteTrace(20, "I:Symbolkonsistenzcheck ok");
	}
	public void postprocess()
	{}
}
