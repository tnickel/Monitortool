package modtools;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import data.GlobalVar;
import hiflsklasse.StrWork;
import hiflsklasse.Tracer;

public class Magicfix
{
	static public void renameMagicDate(String fnam, String mtype)
	{
		System.out.println("fname=" + fnam);
		System.out.println("type=" + mtype);
		fnam = fnam.replace("Strategy", "");
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
		Date currentTime = new Date();
		int datecounter = (GlobalVar.getDatecounter() + 1);
		String nickcounter_str = StrWork.intToFourDigitString(datecounter);
		
		String datestring = (formatter.format(currentTime) + nickcounter_str);
		String fnam2 = fnam.substring(0, fnam.lastIndexOf(" "));
		String fnam3 = fnam2 + datestring;
		String fnam4 = fnam3.replace("date", "");
		String postfix = null;
		if (mtype.equals("mt4"))
			postfix = ".mq4";
		else
			postfix = ".mq5";
		
		String neufile = fnam4 + postfix;
		
		File altfile_f = new File(fnam + postfix);
		File neufile_f = new File(neufile);
		if (altfile_f.renameTo(neufile_f) == false)
			Tracer.WriteTrace(10, "E: can´t rename file <" + fnam + "> nach <" + neufile + ">");
		
		File altfilesqx_f = new File(fnam + ".sqx");
		File neufilesqx_f = new File(neufile.replace(postfix, ".sqx"));
		
		if (altfilesqx_f.exists())
			if (altfilesqx_f.renameTo(neufilesqx_f) == false)
				Tracer.WriteTrace(20, "I: can´t rename file <" + fnam + "> nach <" + neufile + ">");
			
		if (datecounter > 900)
			datecounter = 0;
		GlobalVar.setDatecounter(datecounter);
		GlobalVar.save();
		
	}
}
