package stores;

import hilfsklasse.DateExcecption;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;

import java.util.Collections;
import java.util.HashMap;

import kurse.Kursvalue;
import kurse.KursvalueNativeDbObj;
import objects.Obj;
import slider.KursSlider;

import comperatoren.KursvalueNativeComperator;

import db.DB;

public class KursvalueNativeDB extends DB
{
	// verwaltet die Kurse
	// für ein Symbol + Datum erhält man einen Kurs
	private String symb = null;
	private HashMap<String, Float> kursmap = new HashMap<String, Float>();
	private HashMap<String, Float> volumenmap = new HashMap<String, Float>();

	private int blockflag2 = 0;
	private KursSlider slider = new KursSlider(3);
	// kleinstes datum
	private String mindate_g = null;
	private String maxdate_g = null;

	public KursvalueNativeDB()
	{
	}

	public int compareTo(Obj argument)
	{
		return 0;
	}

	public String getSymb()
	{
		return symb;
	}

	public void setSymb(String symb)
	{
		this.symb = symb;
	}

	public String calcMindate()
	{
		return mindate_g;
	}

	public String calcMaxdate()
	{
		return maxdate_g;
	}
	
	private void initkursmap()
	{
		// hier wird eine Kursmap zur schnelleren Berechnung aufgebaut
		int maxzaehler = this.GetanzObj();

		for (int i = 0; i < maxzaehler; i++)
		{
			KursvalueNativeDbObj kvobj = (KursvalueNativeDbObj) GetObjectIDX(i);
			String kursdatum = Tools.entferneZeit(kvobj.getDate());

			if(i==0)
				maxdate_g=Tools.entferneZeit(kvobj.getDate());
			
			if (i == maxzaehler-1)
				mindate_g = Tools.entferneZeit(kvobj.getDate());

			float kursval = kvobj.getClose();
			if (kursmap.containsKey(kursdatum) == false)
				kursmap.put(kursdatum, kursval);

			if (volumenmap.containsKey(kursdatum) == false)
				volumenmap.put(kursdatum, kvobj.getVolume());
		}
		// plausicheck
		if (maxzaehler != -1)
			if (kursmap.size() != maxzaehler)
				Tracer.WriteTrace(10,
						"Error: internal kursmap hat zu wenig Kurse symb<"
								+ symb + "> kursmapsize<" + kursmap.size()
								+ "> kurseanz<" + maxzaehler + ">");
	}

	public void LadeKursSymb(String symbol)
	{
		symb = symbol;

		LoadDB("kursvalue", symbol, 1);
		initkursmap();
	}

	public Kursvalue holeKurswert(String suchdatum, int warningflag)
	{
		// hier wird der Kurswert ermittelt
		// Wenn ein Kurs gefunden wurde ist das validflag ==1 gesetzt
		// bei keinem Kurs ist das validflag==0;

		int trycount = 0;
		float kursval = 0;
		Kursvalue kursvalue = new Kursvalue();
		kursvalue.setValidflag(false);

		if (suchdatum == null)
		{
			Tracer.WriteTrace(20,
					"Error: suchdatum ==null => setze Kurs =null symb<" + symb
							+ ">");
			return kursvalue;
		}

		suchdatum = Tools.entferneZeit(suchdatum);
		if (suchdatum.contains("."))
			suchdatum = Tools.convDatumPunktStrich(suchdatum);

		if (kursmap.containsKey(suchdatum))
		{
			kursval = kursmap.get(suchdatum);
			kursvalue.setKv(kursval);
			kursvalue.setVolumen(volumenmap.get(suchdatum));
			kursvalue.setDate(new String(suchdatum));
			kursvalue.setValidflag(true);
		} else
		{
			// ziehe 1 Tage ab und schaue nach ob das dann drin ist?
			while (trycount < 3)
			{
				try
				{
					suchdatum = Tools.convDatumPunktStrich(Tools
							.entferneZeit(Tools.subTimeHours(suchdatum, 48)));
				} catch (DateExcecption e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					Tracer.WriteTrace(10,
							"Error:internal suchdatum falsch date<" + suchdatum
									+ ">");
				}
				if (kursmap.containsKey(suchdatum))
				{ // kurs gefunden
					kursval = kursmap.get(suchdatum);
					kursvalue = new Kursvalue();
					kursvalue.setKv(kursval);
					kursvalue.setVolumen(volumenmap.get(suchdatum));
					kursvalue.setDate(new String(suchdatum));
					kursvalue.setValidflag(true);
					break;
				} else
				{ // kein Kurs versuche nochmal
					trycount++;
					continue;
				}
			}
		}
		return kursvalue;
		// speichere das Sliderelement
		// slider.addSliderElem(kursval);

		/*
		 * if (kursval == 0) { float slkurs = slider.calcSliderValue();
		 * Tracer.WriteTrace(20, "Warning: kursvalue ==0, Wende SliderKurs<" +
		 * slkurs + "> an symb<" + symb + "> kursdatum<" + suchdatum + ">");
		 * return slkurs; } else return kursval;
		 */
	}

	public void AddNeuerKurswert(KursvalueNativeDbObj kvn)
	{
		// den kurswert in die db aufnehmen falls er noch nicht drin ist
		if (kursmap.containsKey(kvn.getDate()) == false)
		{
			kursmap.put(kvn.getDate(), kvn.getAdjclose());
			volumenmap.put(kvn.getDate(),kvn.getVolume());
			this.AddObject(kvn);
		} else
			Tracer.WriteTrace(20,
					"Warning Kurs schon in der kursvalue native db date<"
							+ kvn.getDate() + ">");
	}
	public boolean plausiVorNeuaufnahme(Object obj)
	{
		return true;
	}
	public void SortWriteDB()
	{
		KursvalueNativeComperator c = new KursvalueNativeComperator();
		Collections.sort(this.dbliste, c);

		KursvalueNativeDbObj obj = (KursvalueNativeDbObj) this.GetObjectIDX(0);

		if(obj==null)
		{
			Tracer.WriteTrace(20, "Warning: kursvalue.db ist leer");
			return;
		}
		this.WriteDB();
	}
	public int getKursanzahl()
	{
		return(this.GetanzObj());
	}
	public void postprocess()
	{}
}
