package bank;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Prop2;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mainPackage.GC;
import objects.UserEinPostingGewinnObj;
import stores.UserSummenGewinneDBI;

public class Depot
{
	private float cashbestand_g = 0;
	private float startcash = 0;
	private int id_g = 0;
	private Inf inf_g = new Inf();
	private Inf infsum_g = new Inf();
	float gesgew_g = 0, gesverl_g = 0;
	private int gewintervall_glob = 0;

	// Jedes Depot was einen eindeutigen Gewinnalgorithmus besitzt, hat eine
	// eindeutige Zuordnung zu einem Gewinnspeicher
	private UserSummenGewinneDBI userSummenGewinne = null;

	// Ein depot ist immer für einen User/algo, ein depot beinhaltet eine liste
	// von käufen
	private String uname = null;
	public ArrayList<Kauf> kaufliste = new ArrayList<Kauf>();

	// die lastkurse map speichert zu einem symbol die letzten kurse
	private HashMap<String, Float> lastkurse = new HashMap<String, Float>();
	private HashMap<String, Float> lastK38kurse = new HashMap<String, Float>();

	// zaehlt wieviele Symbole von welcher sorte in der Kaufliste sind
	public HashMap<String, Integer> symbolmenge = new HashMap<String, Integer>();
	// zaehlt die Summengewinne und Sumenverluste für eine Aktie
	public HashMap<String, Float> summengewinne = new HashMap<String, Float>();
	public HashMap<String, Float> summenverluste = new HashMap<String, Float>();

	Depot(float startc, String name)
	{
		// holt das Gewinnintervall z.B. 120 Tage werden hier betrachtet
		gewintervall_glob = Integer.parseInt(Prop2
				.getprop("usergewinnintervall"));

		// hier wird einem Algorithmus ein startcash mitgegeben
		startcash = startc;
		cashbestand_g = startc;
		this.uname = new String(name);
		String fna = GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\Bank\\Einzelgewinne\\" + name
				+ ".txt";
		String fna2 = GC.rootpath
				+ "\\db\\UserThreadVirtualKonto\\bank\\summengewinne\\" + name
				+ ".txt";
		if (FileAccess.FileAvailable(fna))
			FileAccess.FileDelete(fna,0);
		if (FileAccess.FileAvailable(fna2))
			FileAccess.FileDelete(fna2,0);

		inf_g.setFilename(fna);
		infsum_g.setFilename(fna2);

		String msg = null;
		msg = "Erklärungen:";
		inf_g.writezeile(msg);
		infsum_g.writezeile(msg);

		msg = "Kennung:KAUF:Aktienname:Symbol:Kaufdatum:Kaufpreis:anzahlAktien im Depot nach kauf:aktueller Kurs:<Grund warum gekauft>:Restcashbestand";
		inf_g.writezeile(msg);
		msg = "Kennung:Kaufdatum:Aktienname:Symbol:Verkaufdatum:Kaufpreis:Verkaufspreis:Verkaufsanzahl|Aktienanzahl nach Verk. im Depot:Gewinn/Verlust:<Grund warum gekauft>:cashbestand";
		inf_g.writezeile(msg);
		msg = "End Leerverkäufe finden immer statt wenn das aktuelle Datum erreicht worden ist, dann wird alles verkauft um den Gewinn/Verlust zu ermitteln";
		inf_g.writezeile(msg);
		msg = "__________________________________________________________________________________________________";
		inf_g.writezeile(msg);

		msg = "Aktie <Aktienname> symb<Symbol> Gewinn/Verlust<???>";
		infsum_g.writezeile(msg);

		clear();

		// lade für diesen User die DBI-Daten beim
		// Initialisieren(Summengewinne.db rein)
		userSummenGewinne = new UserSummenGewinneDBI("UserThreadVirtualKonto\\Bank\\Gewinnverlaufdb",name, gewintervall_glob, 0);
	}

	private void clear()
	{
		kaufliste.clear();
		lastkurse.clear();
		lastK38kurse.clear();
		symbolmenge.clear();
	}

	private void removeAllSymbols(String symb)
	{
		// entfernt alle vorkommen von symb aus der kaufliste
		int anz = kaufliste.size();

		for (int i = 0; i < anz; i++)
		{
			Kauf kauf = kaufliste.get(i);

			if (kauf.getSymbol().equals(symb))
			{
				kaufliste.remove(kauf);
				anz--;
				i--;
			}
		}
		System.out.println("Kaufliste leer");
	}

	private void entferneAlteKauefe()
	{
		// immer wenn das Validflag ==0 ist wird der kauf entfernt
		// entfernt alle vorkommen von symb aus der kaufliste
		int anz = kaufliste.size();

		for (int i = 0; i < anz; i++)
		{
			Kauf kauf = kaufliste.get(i);

			if (kauf.getValidflag() == 0)
			{
				kaufliste.remove(kauf);
				anz--;
				i--;
			}
		}
		System.out.println("Kaufliste leer");
	}

	private float calcAktienanzahl(String symb)
	{
		int anz = kaufliste.size();
		float aktanz = 0;

		for (int i = 0; i < anz; i++)
		{
			Kauf kauf = kaufliste.get(i);

			if ((kauf.getValidflag() == 1) && (kauf.getSymbol().equals(symb)))
			{
				aktanz = aktanz + kauf.getAnzahl();
			}
		}
		return aktanz;
	}

	public Boolean hasSymbol(String symb)
	{
		if (symbolmenge.containsKey(symb) == true)
			return true;
		return false;
	}

	private float calcGewinnsumme(String symb)
	{
		if (summengewinne.containsKey(symb))
			return (summengewinne.get(symb));
		else
			return 0;
	}

	private float calcVerlustsumme(String symb)
	{
		if (summenverluste.containsKey(symb))
			return (summenverluste.get(symb));
		else
			return 0;
	}

	private int getAnzKauefe(String symb)
	{
		if (symbolmenge.containsKey(symb) == true)
			return (symbolmenge.get(symb));
		else
			return 0;
	}

	private void erhoeheSymbolMengenZaehler(String symb)
	{
		// symbol existiert schon
		if (symbolmenge.containsKey(symb) == true)
		{
			int anz = symbolmenge.get(symb);
			anz = anz + 1;
			symbolmenge.put(symb, anz);
		}// symbol wird neue angelegt
		else
			symbolmenge.put(symb, 1);
	}

	private void setlastaccessdate(String symb, String date)
	{

	}

	private void vermindereSymbolKaufzaehler(String symb)
	{
		// symbol existiert schon
		if (symbolmenge.containsKey(symb) == true)
		{
			int anz = symbolmenge.get(symb);
			if (anz > 0)
				anz = anz - 1;
			else
				Tracer.WriteTrace(10, "internal error, anz=" + anz);
			symbolmenge.put(symb, anz);
		}// symbol wird neue angelegt
		else
			Tracer.WriteTrace(10, "symbol nicht vorhanden symb=" + symb);

	}

	public void setzeLastKurse(String symb, float kv, float kv38)
	{
		lastkurse.put(symb, kv);
		lastK38kurse.put(symb, kv38);
	}

	private float getKurs(String symb)
	{
		float k = 0;
		if (lastkurse.containsKey(symb) == false)
			Tracer.WriteTrace(10, "Error:internal kein kurs für symbol <"
					+ symb + ">");
		else
			k = lastkurse.get(symb);
		return k;
	}

	public float getCashbestand()
	{
		return cashbestand_g;
	}

	public void setCashbestand(float cashbestand)
	{
		this.cashbestand_g = cashbestand;
	}

	public String getUname()
	{
		return uname;
	}

	public void setUname(String uname)
	{
		this.uname = uname;
	}

	public void kauf(String kaufdatum, String aktname, String symbol, int tid,
			int postid, String uname, float kurspreis, float k38kurs,
			float anzahl, String grund)
	{
		id_g++;
		// hier wird der lastkurs für das Symbol gespeichert
		lastkurse.put(symbol, kurspreis);
		lastK38kurse.put(symbol, k38kurs);
		this.erhoeheSymbolMengenZaehler(symbol);

		Kauf kauf = new Kauf(id_g, symbol, tid, postid, kurspreis, anzahl,
				kaufdatum, uname, aktname, 1);
		kaufliste.add(kauf);

		// ermittelt die Aktienanzahl für ein Symbol
		float gesanz = this.calcAktienanzahl(symbol);

		// Vermindere den Cashbestand
		cashbestand_g = cashbestand_g - (anzahl * kurspreis);

		// logge das ganze
		String msg = id_g + ":" + "KAUF" + ":" + aktname + ":Symb=" + symbol
				+ ":" + kaufdatum + " KP=" + kurspreis * anzahl + "Euro|"
				+ gesanz + "anz" + " Kurs=" + kurspreis + "<" + grund + ">"
				+ " RC=" + cashbestand_g;
		inf_g.writezeile(msg);
	}

	public void verkauf(Kauf kauf, String grund, String verkaufdatum)
	{
		String gewmsg = null;
		float kaufsumme = 0, verkaufsumme = 0;
		float gew = 0;

		// plausi verkaufdatum>kaufdatum
		if (Tools.datum_ist_aelter_gleich(kauf.getKaufdatum(), verkaufdatum) == false)
			Tracer.WriteTrace(10, "Error plausi kaufdatum<"
					+ kauf.getKaufdatum() + "> verkaufdatum<" + verkaufdatum
					+ ">");

		// zaehlt wieviele Aktien noch im depot sind nach dem verkauf
		float restgesanz = this.calcAktienanzahl(kauf.getSymbol())
				- kauf.getAnzahl();

		// Erhöhe den Cashbestand
		float aktkurspreis = lastkurse.get(kauf.getSymbol());
		cashbestand_g = cashbestand_g + (kauf.getAnzahl() * aktkurspreis);

		kaufsumme = kauf.getAnzahl() * kauf.getKaufpreis();
		verkaufsumme = kauf.getAnzahl() * aktkurspreis;

		kauf.setValidflag(0);
		this.vermindereSymbolKaufzaehler(kauf.getSymbol());

		gew = verkaufsumme - kaufsumme;
		gewmsg = " gew=" + gew;

		if (kaufsumme < verkaufsumme)
		{
			// verkaufe mit gewinn
			float gesgewinnsumme = calcGewinnsumme(kauf.getSymbol());
			gesgewinnsumme = gesgewinnsumme + gew;
			// dies ist der gewinn für das summengewinnedepot
			summengewinne.put(kauf.getSymbol(), gesgewinnsumme);
		} else
		{
			float gesverlustsumme = calcVerlustsumme(kauf.getSymbol());
			gesverlustsumme = gesverlustsumme + (verkaufsumme - kaufsumme);
			summenverluste.put(kauf.getSymbol(), gesverlustsumme);
		}

		// logge das ganze
		String msg = kauf.getId() + ":" + kauf.getKaufdatum() + ":" + "Verkauf"
				+ ":" + kauf.getAktname() + ":Symb=" + kauf.getSymbol() + ":"
				+ verkaufdatum + " Kp=" + kauf.getKaufpreis() + " Vk="
				+ aktkurspreis + " Anzahl=" + kauf.getAnzahl() + "|"
				+ restgesanz + gewmsg + "<" + grund + ">" + " RC="
				+ cashbestand_g;
		inf_g.writezeile(msg);

		// speichere Auch das Gewinn/Verlustobjekt in SummengewinneDB
		UserEinPostingGewinnObj ueingewobj = new UserEinPostingGewinnObj(kauf
				.getKaufdatum(), kauf.getAktname(), kauf.getTid(), kauf
				.getPostid(), gew, kauf.getKaufpreis(), aktkurspreis, 0,kauf.getSymbol(),"xyz??");

		userSummenGewinne.addEinzelgewinn(kauf.getUname(), ueingewobj);
	}

	protected float getAktuellerGesGewinn()
	{
		// geht durch das depot und ermittelt den gesammtgewinn
		// geht durch die kauflisten und ermittelt die gesammtsumme
		// der startcash wird hierbei abgezogen
		int anz = kaufliste.size();
		float tempsum = 0;

		for (int i = 0; i < anz; i++)
		{
			Kauf kauf = kaufliste.get(i);
			if (kauf.getValidflag() == 0)
				continue;
			float anzahl = kauf.getAnzahl();
			String symb = kauf.getSymbol();
			float akurs = lastkurse.get(symb);
			float val = anzahl * akurs;
			tempsum = tempsum + val;
		}
		float gew = tempsum + cashbestand_g - startcash;
		return gew;
	}

	private void verkaufeSymbolLeer(String symb, String gewinnmsg,
			String verkaufdatum)
	{
		// liefert den gesammtgewinn oder gesammtverlust zurück
		float geskaufpreis = 0, gesverkaufpreis = 0;
		String info = null;
		String algo = "", aktie = "";
		String msg = null;

		int anz = kaufliste.size();

		for (int i = 0; i < anz; i++)
		{
			Kauf kauf = kaufliste.get(i);
			if ((kauf.getSymbol().equals(symb)) && (kauf.getValidflag() == 1))
			{
				geskaufpreis = geskaufpreis
						+ (kauf.getAnzahl() * kauf.getKaufpreis());
				gesverkaufpreis = gesverkaufpreis
						+ (kauf.getAnzahl() * this.getKurs(symb));
				this.verkauf(kauf, gewinnmsg, verkaufdatum);
				algo = kauf.getUname();
				aktie = kauf.getAktname();
			}
		}
		// logge die summen
		if (geskaufpreis < gesverkaufpreis)
			msg = "Aktie <" + aktie + "> symb<" + symb + "> Gewinn<"
					+ (gesverkaufpreis - geskaufpreis) + ">";
		else
			msg = "Aktie <" + aktie + "> symb<" + symb + "> Verlust<"
					+ (gesverkaufpreis - geskaufpreis) + ">";
		infsum_g.writezeile(msg);

		this.removeAllSymbols(symb);
		this.entferneAlteKauefe();

		float gw = gesverkaufpreis - geskaufpreis;

		if (gw > 0)
		{
			gesgew_g = gesgew_g + gw;
			info = "Gesammtgewinn";
		} else
		{
			gesverl_g = gesverl_g + gw;
			info = "Gesammtverlust";
		}
		msg = "Leerverkauf Einzelzaehler" + info + "<" + gw + "> für symbol<"
				+ symb + "> gesgew<" + gesgew_g + "> gesverl<" + gesverl_g
				+ ">";
		inf_g.writezeile(msg);

		// speichere die Gewinne schon mal in der DB
		this.speichereGewinne();
	}

	protected void ueberpruefeVerkauefe(String testdatum, int leerverkaufsflag)
	{
		// schaut nach ob man von den Aktien was verkaufen kann um somit wieder
		// cash zu sammeln
		// übermittle, den gewinn oder verlust
		int symbanz = symbolmenge.size();
		ArrayList<String> sliste = new ArrayList<String>();

		String grund = null;

		if ((symbanz == 0) && (kaufliste.size() > 0))
		{

			if (kaufliste.size() > 0)
			{
				for (int i = 0; i < kaufliste.size(); i++)
				{
					Kauf kauf = kaufliste.get(i);
					Tracer.WriteTrace(20, "Kaufliste: " + kauf.getAktname());
				}
			}
			Tracer.WriteTrace(10,
					"Error: internal symbolliste inkonsistenz symbanz<"
							+ symbanz + "> kauflisteanz<" + kaufliste.size()
							+ ">");
		}

		// nix zu tun
		if (symbanz == 0)
			return;

		// baue die sliste auf, die wird für das durchlaufen der Menge benötigt
		for (Map.Entry<String, Integer> e : symbolmenge.entrySet())
		{
			String symb = e.getKey();
			int val = e.getValue();
			if (val == 0)
				continue;
			sliste.add(symb);
		}

		// betrachte jedes Symbol einzeln
		for (int i = 0; i < sliste.size(); i++)
		{
			String symb = sliste.get(i);

			if (leerverkaufsflag == 1)
			{
				grund = "EndLeerverkauf";
				verkaufeSymbolLeer(symb, grund, testdatum);
				symbolmenge.remove(symb);
			} else
			{
				// Verkaufe falls akt. Kurs unter den aktuellen K38-10% fällt
				float k = lastkurse.get(symb);
				float k38 = lastK38kurse.get(symb);

				// bedingung für den komplettverkauf einer Aktie
				float kverk = (k38 - (k38 * 0.1f));
				// float kverk = k38;
				if (k < kverk)
				{
					String msg2 = "k(" + k + ")<k38/10(" + (k38 - (k38 * 0.1f))
							+ ")";
					verkaufeSymbolLeer(symb, "Grund:" + msg2, testdatum);
					symbolmenge.remove(symb);

				}
			}
		}
	}

	void speichereGewinne()
	{
		userSummenGewinne.WriteDB();
	}
}
