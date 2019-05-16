package xml;

import gui.Mbox;
import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;

import java.text.DecimalFormat;

import data.Commentflags;

public class TnParser
{
	private String mem = null;

	public void loadFile(String filename, int maxline)
	{
		Inf inf = new Inf();

		inf.setFilename(filename);
		mem = inf.readMemFileDelimter(maxline);
		inf.close();
		if (mem == null)
			Tracer.WriteTrace(10, "E: file <" + filename + "> bad");

	}

	public void saveFile(String filename)
	{
		Inf inf = new Inf();
		inf.setFilename(filename);
		if (FileAccess.FileAvailable(filename))
			FileAccess.FileDelete(filename, 1);

		inf.AppendMemFileDelimiter(mem);
		inf.close();
	}

	public String takefirstAttrib(String attrib)
	{
		String a1 = "<" + attrib + ">";
		String a2 = "</" + attrib + ">";
		String found = mem.substring(mem.indexOf(a1) + a1.length(),
				mem.indexOf(a2));
		return found;
	}

	public String takefirstAttribSection(String sectionstring, int maxlen,
			String attrib)
	{
		// <ConfidenceLevel level="50">
		// <netProfit>1969.2964</netProfit>

		// sucht das erste Attribut in einer bestimmten sektion
		// die ersten maxlen-Bytes der sektion werden extrahiert

		if (mem == null)
			Tracer.WriteTrace(10, "mem=null");

		int pos1 = mem.indexOf(sectionstring);
		if (pos1 <= 0)
			Tracer.WriteTrace(10, "pos1=0");

		int pos2 = pos1 + maxlen;
		String substr = mem.substring(pos1, pos2);

		// dann wird in diesem extrahierten das attribut gesucht
		String a1 = "<" + attrib + ">";
		String a2 = "</" + attrib + ">";
		String found = substr.substring(substr.indexOf(a1) + a1.length(),
				substr.indexOf(a2));
		return found;
	}

	public String takeNetProfitConfidence(int confidenz)
	{
		// <ConfidenceLevel level="50">
		// <netProfit>1969.2964</netProfit>

		// holt den nettoprofit für einen bestimmten confidenzwert
		String sectionstring = "<ConfidenceLevel level=\"" + confidenz + "\">";

		String conf = takefirstAttribSection(sectionstring, 200, "netProfit");
		return conf;
	}

	public float takeNetMaxDDConfidence(int confidenz)
	{
		// <ConfidenceLevel level="50">
		// <maxPctDD>6.641995</maxPctDD>

		// holt den nettoprofit für einen bestimmten confidenzwert
		String sectionstring = "<ConfidenceLevel level=\"" + confidenz + "\">";

		float maxddr = Float.valueOf(takefirstAttribSection(sectionstring, 200,
				"maxPctDD"));
		return maxddr;
	}
	public float takeRetDDConfidence(int confidenz)
	{
		// <ConfidenceLevel level="50">
		// <retDDRatio>6.641995</retDDRatio>
		String sectionstring = "<ConfidenceLevel level=\"" + confidenz + "\">";
		float maxddr = Float.valueOf(takefirstAttribSection(sectionstring, 200,
				"retDDRatio"));
		return maxddr;
	}
	public String getCommentNote()
	{
		String a1 = "Strategy note=\"";
		String a2 = "\"";
		int index1 = mem.indexOf(a1) + a1.length();
		int index2 = mem.substring(index1).indexOf(a2);
		String found = mem.substring(index1, index1 + index2);
		return found;
	}

	public void setCommentNote(String neuenote)
	{
		String alt = "Strategy note=\"" + getCommentNote() + "\"";
		String neu = "Strategy note=" + "\"" + neuenote + "\"";
		mem = mem.replace(alt, neu);
	}

	public void addComments(Commentflags cf)
	{
		String altenote = getCommentNote();
		String pref = cf.getPrefix();
		String newcomment = null;
		DecimalFormat f1 = new DecimalFormat("0");
		DecimalFormat f2 = new DecimalFormat("0.00");

		String profit = takefirstAttrib("netProfit");
		String stabilitaet = takefirstAttrib("correlation");

		float prof = Float.valueOf(profit);
		float stabil = Float.valueOf(stabilitaet);

		if (pref.length() < 1)
		{
			Mbox.Infobox("please set prefix");
			return;
		}

		newcomment = altenote + pref;

		if ((cf.getButtonprofit() == 1) && (prof > cf.getNetprofitminval()))
		{
			// profit hinzufügen
			newcomment = newcomment + "p" + f1.format(prof) + "_";
		}
		if ((cf.getButtonstabilitaet() == 1) && (stabil > cf.getStabilminval()))
		{
			// stabilität hinzufügen
			newcomment = newcomment + "s"
					+ ((f2.format(stabil)).replaceFirst(",", ".")) + "_";
		}
		// den neuen kommentar setzen
		setCommentNote(newcomment);
	}

	public void cleanComment()
	{
		setCommentNote("");
	}

	public float getRetDD_IS()
	{
		String retdd = takefirstAttribSection(
				"<StatsData SampleType=\"InSample\">", 500, "retDDRatio");
		Float retdd_f = Float.valueOf(retdd);
		return retdd_f;
	}

	public float getMaxDrawdown_IS()
	{
		String ddstr = takefirstAttribSection(
				"<StatsData SampleType=\"InSample\">", 250, "drawdown");
		Float dd = Float.valueOf(ddstr);
		return dd;
	}

	public float getNettoprofit_IS()
	{
		String profit = takefirstAttribSection(
				"<StatsData SampleType=\"InSample\">", 100, "netProfit");
		Float prof = Float.valueOf(profit);
		return prof;
	}

	public float getNettoprofit_OOS()
	{
		String profit = takefirstAttribSection(
				"<StatsData SampleType=\"OutOfSample\">", 100, "netProfit");
		Float prof = Float.valueOf(profit);
		return prof;
	}

	public float getNettoprofit_Robust(int confidenz)
	{
		String profit = takeNetProfitConfidence(confidenz);
		Float prof = Float.valueOf(profit);
		return prof;
	}

	public float getMaxDD_Robust(int confidenz)
	{
		//MaxDrawdown Robust
		float maxddr = 0;
		try
		{
			maxddr = takeNetMaxDDConfidence(confidenz);
		} catch (StringIndexOutOfBoundsException e)
		{
			Tracer.WriteTrace(10,
					"I:strategyfile *.str dont have a robustnesstst, you can uncheck it");
			return 0;
		}
		return maxddr;
	}

	public float getMaxDrawdown_Robust(int confidenz)
	{
		float maxddr = takeNetMaxDDConfidence(confidenz);
		return maxddr;
	}
	public float getRetDD_Robust(int confidenz)
	{
		//Ret Robust
		float retddr = 0;
		try
		{
			retddr = takeRetDDConfidence(confidenz);
		} catch (StringIndexOutOfBoundsException e)
		{
			Tracer.WriteTrace(10,
					"I:strategyfile *.str dont have a robustnesstst, you can uncheck it");
			return 0;
		}
		return retddr;
	}
	public float getGrossProfit_IS()
	{
		//grossProfit
		String grossprof = takefirstAttribSection(
				"<StatsData SampleType=\"InSample\">", 800, "grossProfit");
		Float grossprof_f = Float.valueOf(grossprof);
		return grossprof_f;
	}
	public float getGrossLoss_IS()
	{
		String grossloss = takefirstAttribSection(
				"<StatsData SampleType=\"InSample\">", 800, "grossLoss");
		Float grossloss_f = Float.valueOf(grossloss);
		return grossloss_f;
	}
	public float getGrossProfit_OSS()
	{
		String grossprof = takefirstAttribSection(
				"<StatsData SampleType=\"OutOfSample\">", 800, "grossProfit");
		Float grossprof_f = Float.valueOf(grossprof);
		return grossprof_f;
	}
	public float getGrossLoss_OSS()
	{
		String grossloss = takefirstAttribSection(
				"<StatsData SampleType=\"OutOfSample\">", 800, "grossLoss");
		Float grossloss_f = Float.valueOf(grossloss);
		return grossloss_f;
	}
	public float getFloatValue(String sampletype, String attribute)
	{
		if((sampletype.equalsIgnoreCase("InSample")==false)&&(sampletype.equalsIgnoreCase("OutOfSample")==false))
			Tracer.WriteTrace(10, "E:unknown sampletype <"+sampletype+">");
		
		String attribstring=takefirstAttribSection(
				"<StatsData SampleType=\""+sampletype+"\">", 1600, attribute);
		float attrib_f=Float.valueOf(attribstring);
		return attrib_f;
	}
}
