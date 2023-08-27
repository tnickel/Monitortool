package modtools;

import hiflsklasse.Tracer;

public class MtTypeSizePeriod
{
	private int mt5type = 0;
	private int mt5size = 0;
	private int mt4period = 0;
	private String periodstring = null;
	private String expertname_glob = null;
	
	// Diese Klasse wird benötigt um die einträge für das chart-profile zu berechnen
	public MtTypeSizePeriod(String expertname)
	{
		expertname_glob = expertname;
		// hole aus dem komplettnamen z.B. Q67 EURUSD M15 3.100.112.mq4 => M15
		if ((expertname.endsWith(".mq4") == false) && (expertname.endsWith(".mq5") == false))
		{
			Tracer.WriteTrace(10, "expertname should end with .mq4/5 but I got <" + expertname + ">");
			
		}
		
		String[] parts = expertname.split(" ");
		int anzp = parts.length;
		if (anzp < 3)
		{
			Tracer.WriteTrace(10,
					"wrong format expertname should have [Currencystring] [Timeframe] [Magic].mq4 \n for example 'Q67 EURUSD M15 3.100.112.mq4'");
		}
		
		String period_found = parts[anzp - 2];
		
		if (period_found == null)
		{
			Tracer.WriteTrace(10, "problem with period in <" + expertname + "> -> STOP");
			
		}
		periodstring = period_found;
	}
	
	
	
	public int getMt5Type()
	{
		String[] periodenzeichen =
		{ "M15", "H1", "M1", "M5", "M30", "H4", "D1","WE","MO" };
		Integer[] type =
		{ 0, 1, 0, 0, 0, 1, 1,2,3 };
		
		// hole aus dem komplettnamen z.B. Q67 EURUSD M15 3.100.112.mq4 => M15
		String period_found = periodstring;
		
		int anz = periodenzeichen.length;
		for (int i = 0; i < anz; i++)
		{
			if (period_found.contains(periodenzeichen[i]))
				return type[i];
		}
		Tracer.WriteTrace(10, "E: getMt4Period no period found in <" + expertname_glob + ">");
		return 0;
	}
	
	public int getMt5Size()
	{
		String[] periodenzeichen =
			{ "M15", "H1", "M1", "M5", "M30", "H4", "D1","WE","MO" };
			Integer[] size =
			{ 15, 1, 1, 5, 30, 4, 24,1,1 };
			
			// hole aus dem komplettnamen z.B. Q67 EURUSD M15 3.100.112.mq4 => M15
			String period_found = periodstring;
			
			int anz = periodenzeichen.length;
			for (int i = 0; i < anz; i++)
			{
				if (period_found.contains(periodenzeichen[i]))
					return size[i];
			}
			Tracer.WriteTrace(10, "E: getMt4Period no period found in <" + expertname_glob + ">");
			return 0;
	}
	
	public int getMt4Period()
	{
		// expertname_glob=Q67 EURUSD M15 3.100.112.mq4
		
		String[] periodenzeichen =
		{ "M15", "H1", "M1", "M5", "M30", "H4", "D1" };
		Integer[] frame =
		{ 15, 60, 1, 5, 30, 240, 1440 };
		
		// hole aus dem komplettnamen z.B. Q67 EURUSD M15 3.100.112.mq4 => M15
		String period_found = periodstring;
		
		int anz = periodenzeichen.length;
		for (int i = 0; i < anz; i++)
		{
			if (period_found.contains(periodenzeichen[i]))
				return frame[i];
		}
		Tracer.WriteTrace(10, "E: getMt4Period no period found in <" + expertname_glob + ">");
		return mt4period;
	}
	
}
