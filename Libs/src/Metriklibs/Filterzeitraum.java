package Metriklibs;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;

import data.CorelSetting;
import hiflsklasse.Inf;

public class Filterzeitraum
{
	// fuer diesen Zeitraum gelten diese filter z.b. Filter01-directory
	// Die Filterfiles haben den aufbau <name>.Filter und befinden sich in
	// data//analyse1//_1_name//<name>.filter
	// ein Filterzeitraum ist eine Liste von Filterelementen. Ein Filterelement ist eine Metrik mit schranken
	private ArrayList<Filterentry> filterzeitraum = new ArrayList<Filterentry>();
	//Dies ist der Filename des Filters
	private String filename_glob = null;

	public void setFilename(String fnam)
	{
		filename_glob = fnam;
	}

	public String holeFilename()
	{
		return filename_glob;
	}

	public void readFilter()
	{
		// hier wird der Filter (Metriken) eingelesen und der Filterzeitraum aufgebaut
		// erst mal alles löschen
		if (filterzeitraum != null)
			filterzeitraum.clear();

		String fname = filename_glob.replace(".csv", ".filter");
		Inf inf = new Inf();
		inf.setFilename(fname);

		while (5 == 5)
		{
			String zeile = inf.readZeile();

			if (zeile == null)
				break;

			if (zeile.contains("*Infozeile*"))
				continue;
		
			Filterentry fi = new Filterentry();
			//0Attribut	1minMalue	2Maxvalue	3Minfilevalue	4Maxfilevalue	5#Steps	6aktMinValue	7aktMaxValue	8optflag	*Infozeile*
			String[] segs = zeile.split(Pattern.quote("\t"));
			fi.setAttribut(segs[0]);
			fi.setOptflag(Integer.valueOf(segs[8]));
			fi.setAktMaxValue(Float.valueOf(segs[7]));
			fi.setAktMinValue(Float.valueOf(segs[6]));
			fi.setAnzSteps(Integer.valueOf(segs[5]));
			fi.setMaxfilealue(Float.valueOf(segs[4]));
			fi.setMinfilevalue(Float.valueOf(segs[3]));
			fi.setMaxvalue(Float.valueOf(segs[2]));
			fi.setMinvalue(Float.valueOf(segs[1]));
			filterzeitraum.add(fi);
		}
	}

	public void addFilterEntry(MinMaxFilter mimax)
	{
		Filterentry fi = new Filterentry();
		fi.setAttribut(mimax.getAttribut());
		fi.setMaxfilealue(mimax.getMaxfilevalue());
		fi.setMinfilevalue(mimax.getMinfilevalue());
		fi.setMaxvalue(mimax.getMaxvalue());
		fi.setMinvalue(mimax.getMinvalue());
		fi.setAnzSteps(mimax.getAnzSteps());

		fi.setAktMinValue(mimax.getAktMinValue());
		fi.setAktMaxValue(mimax.getAktMaxValue());

		filterzeitraum.add(fi);
	}

	public void modifyAllAttribRandom()
	{
		// die schranken werden zufällig abgeändert
		int anz = filterzeitraum.size();
		Random ran = new Random();
		
		//gehe hier durch die einzelnen attribute
		for (int i = 0; i < anz; i++)
		{
			// gehe durch die einzelnen attribute
			Filterentry fi = filterzeitraum.get(i);
			
			// nur wenn das optflag gesetzt ist darf was optimiert werden
			if (fi.getOptflag() == 0)
				continue;

			int anzsteps = fi.getAnzSteps();
			float minfilevalue = fi.getMinfilevalue();
			float maxfilevalue = fi.getMaxfilealue();
			// zufallszahl
			int intran_low = (ran.nextInt(anzsteps))/2;
			int intran_high = (ran.nextInt(anzsteps))/2;
			// stepsize
			float stepsize = Math.abs(maxfilevalue - minfilevalue) / anzsteps;
			float offset1 = (float) intran_low * stepsize;
			float offset2 = (float) intran_high * stepsize;
			float offset_low = 0, offset_high = 0;
			if (offset1 < offset2)
			{
				offset_low = offset1;
				offset_high = offset2;
			} else
			{
				offset_low = offset2;
				offset_high = offset1;
			}
			// die schranken von oben und unten entsprechend anpassen
			/*System.out.println("filname<" + filename_glob + "> min<"
					+ (minfilevalue + offset_low) + "> max<"
					+ (maxfilevalue - offset_high) + ">");*/
			fi.setAktMinValue(minfilevalue + offset_low);
			fi.setAktMaxValue(maxfilevalue - offset_high);
		}
	}
	public void modifyPersonCorel(int filterindex,CorelSetting corelsetting)
	{
		// die schranken werden zufällig abgeändert
		int anz = filterzeitraum.size();
		Random ran = new Random();
		
		//gehe hier durch die einzelnen attribute
		for (int i = 0; i < anz; i++)
		{
			// gehe durch die einzelnen attribute
			Filterentry fi = filterzeitraum.get(i);
			

			String attrib = fi.getAttribut();
			
			float corelval=CorrelationResultliste.holeAttribCorel(filterindex,attrib);
			
			
			//dann überlege ob dieser Wert überhaupt verändert wird
			//je höher die correlation desto grösser ist die wkeit das hier geändert wird
			if(checkcorelschranke(corelval, corelsetting)==false)
			{
				fi.setOptflag(0);
				continue;
			}
			
			
			int anzsteps = corelsetting.getAnzSteps();//steps werden auf 100 festgesetzt  fi.getAnzSteps();
			
			float minfilevalue = fi.getMinfilevalue();
			float maxfilevalue = fi.getMaxfilealue();
			// zufallszahl
			int intran_low = (ran.nextInt(anzsteps))/2;
			int intran_high = (ran.nextInt(anzsteps))/2;
			// stepsize
			float stepsize = Math.abs(maxfilevalue - minfilevalue) / anzsteps;
			float offset1 = (float) intran_low * stepsize;
			float offset2 = (float) intran_high * stepsize;
			float offset_low = 0, offset_high = 0;
			if (offset1 < offset2)
			{
				offset_low = offset1;
				offset_high = offset2;
			} else
			{
				offset_low = offset2;
				offset_high = offset1;
			}
			// die schranken von oben und unten entsprechend anpassen
			/*System.out.println("filname<" + filename_glob + "> min<"
					+ (minfilevalue + offset_low) + "> max<"
					+ (maxfilevalue - offset_high) + ">");*/
			fi.setAktMinValue(minfilevalue + offset_low);
			fi.setAktMaxValue(maxfilevalue - offset_high);
			fi.setOptflag(1);
		}
	}
	
	private Boolean checkcorelschranke(float corelval,CorelSetting corelsetting)
	{
		//corelval: ist der korrelationswert, je höher, desto höher ist die wkeit das true zürückgeliefert wird
		
		//wenn korrelationswert zu gering dann wird nix geändert
		if(corelval<corelsetting.getMinCorelLevel())
			return false;
		
		//Es wird wkeit=x hoch 1.5 gerechnet
		//1.5 ist der steigungsfaktor
		//ist er z.B = 2 dann liegt die gewichtung auf grossen X, kleine x werden
		//dann fast immer mit false beantwortet
		
		//0.1 => 0.1 hoch 1.5 = 3,1%
		//0.2 => 0.2 hoch 1.5 = 8,9%
		//0.5 => 0.5 hoch 1.5 = 35%
		
		double x = Math.pow(corelval, corelsetting.getCorrelFaktor());
		
		double ran=Math.random();

		if(ran<x)
			return true;
		else
			return false;
	}
	
	public Filterentry holeFilterEntry(String attribut)
	{
		int anz = filterzeitraum.size();
		for (int i = 0; i < anz; i++)
		{
			Filterentry fi = filterzeitraum.get(i);
			String attrib = fi.getAttribut();
			if (attrib.equals(attribut))
				return fi;
		}
		return null;
	}

	public void writeFilterSettings()
	{
		String fname = filename_glob.replace(".csv", ".filter");

		Inf inf = new Inf();
		inf.setFilename(fname);

		File fnam = new File(fname);
		if (fnam.exists())
			fnam.delete();

		writeSettings(inf);
	}

	public void writeFilterSettingsBest100dir(String filterpath,String fname, float value)
	{
		//hier werden die gefundenen Settings in best100dir gespeichert 
		
		Inf inf = new Inf();
		/*DecimalFormat dez = new DecimalFormat("000000,");
		String gewname = (dez.format(value));*/
		String gewname=String.valueOf(value);

		//Nachkommastellen abschneiden
		if(gewname.contains("."))
			gewname=gewname.substring(0,gewname.indexOf("."));
		
		String fp=filterpath+"\\"+gewname+"_"+fname+".filter";
		
		inf.setFilename(fp );
		File fnam = new File(fp);
		if (fnam.exists())
			fnam.delete();

		writeSettings(inf);
		inf.close();
	}
	
	
	

	private void writeSettings(Inf inf)
	{
		int anz = filterzeitraum.size();
		inf.writezeile("Attribut\tminMalue\tMaxvalue\tMinfilevalue\tMaxfilevalue\t#Steps\taktMinValue\taktMaxValue\toptflag\t*Infozeile*");
		for (int i = 0; i < anz; i++)
		{
			Filterentry fi = filterzeitraum.get(i);
			inf.writezeile(fi.getAttribut() + "\t" + fi.getAktMinValue() + "\t"
					+ fi.getAktMaxValue() + "\t" + fi.getMinfilevalue() + "\t"
					+ fi.getMaxfilealue() + "\t" + fi.getAnzSteps() + "\t"
					+ fi.getAktMinValue() + "\t" + fi.getAktMaxValue() + "\t"
					+ fi.getOptflag());
		}
	}
}
