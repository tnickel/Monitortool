package tools;

import hilfsklasse.Tools;


public class Kursworker
{
	Kursliste kl1 = null;
	Kursliste kl2 = null;
	Kursliste kl3 = null;

	public void merge(String quelle1, String quelle2, String ziel,String checkdatefile,String datemessagefile)
	{
		kl1 = new Kursliste(quelle1);
		kl2 = new Kursliste(quelle2);
		kl3 = new Kursliste(ziel);
		
		this.merge();
		kl3.checkDateliste(checkdatefile,datemessagefile);
	}

	public void convMetatraderToNinjatrader(String quelle, String ziel)
	{
		kl1 = new Kursliste(quelle);
		kl1.writeNinja(ziel);
	}
	
	public void convRealtickToDukatick(String quelle, String ziel)
	{
		//Hier werden die durch Metatrader aufgenommenen Tickdaten in das Dukatickdatenformat umgewandelt
		//Dies wird für die Birds Tick Data Site verwendet
		
		kl1 = new Kursliste(quelle);
		kl1.writeDukaTick(ziel);
		
		
	}
	
	private void merge()
	{
		int anz1 = kl1.getAnz();
		int anz2 = kl2.getAnz();
		String dat1=null;
		String dat2=null;
		int count1 = 0;
		int count2 = 0;
		int countgleich=0;
		int cx=0;

		while ((count1 <= anz1) || (count2 <= anz2))
		{
			cx++;
			Kurs1 k1 = kl1.getIDX(count1);
			Kurs1 k2 = kl2.getIDX(count2);
			//falls alles abgearbeitet
			if((k1==null)&&(k2==null))
			{
				break;
			}
			//falls k1 schon abgearbeitet
			else if(k1==null)
			{
				//falls k1 schon abgearbeitet dann nimm nur k2
				count2++;
				kl3.addValue(k2);
				System.out.println("nimm secondary date<"+k2.datum+">");
				continue;
			}
			//falls k2 schon abgearbeitet
			else if(k2==null)
			{
				//falls k2 schon abgearbeitet dann imm nur k1
				count1++;
				kl3.addValue(k1);
				continue;
			}
			
			//beiden Streams haben noch daten
			dat1 = k1.getDatum();
			dat2 = k2.getDatum();

			if (dat1.equals(dat2))
			{
				// beide daten gleich
				count1++;
				count2++;
				countgleich++;
				
				if(k1.getMax()!=k2.getMax())
				{
					//Tracer.WriteTrace(20, "inconsistenzerror datum<"+dat1+"> max1<"+k1.getMax()+"> max2<"+k2.getMax()+">");
				}
				
				if(k1.getClose()!=k2.getClose())
				{
					//Tracer.WriteTrace(20, "inconsistenzerror datum<"+dat1+"> max1<"+k1.getClose()+"> max2<"+k2.getClose()+">");
				}
				
				kl3.addValue(k1);
			} else if (Tools.datum_ist_aelter(dat1, dat2) == true)
			{
				// dat2 ist älter als dat2 dann nimm dat1(nimm jüngeres datum)
				count1++;
				kl3.addValue(k1);
			} else
			{
				// dat1 ist älter ist als dat2 dann nimm dat2(nimm jüngeres datum)
				count2++;
				kl3.addValue(k2);
				System.out.println("nimm secondary date<"+k2.datum+">");

			}
			/*if(cx%1000==0)
				System.out.println("cx<"+cx+">");*/
		}
		//Tracer.WriteTrace(20, "anz1<"+anz1+"> anz2<"+anz2+"> countgleich<"+countgleich+"> kl3anz<"+kl3.getAnz()+">");
		
		kl3.writeFsb();

	}
}
