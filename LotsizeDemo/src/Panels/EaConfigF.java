package Panels;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class EaConfigF
{
	//dies ist die klasse die die KOnfigurationsdaten im File verwaltet
	private Properties p = new Properties();
	private String conffile_glob = null;
	private String usemm="false";
	private String lots="0.01";
	private String riskinpercent="2.0";
	private String maximumlots="0.5";
	private String usefixedmoney="false";
	private String riskinmoney="100.0";
	
	
	public EaConfigF(String configfilename)
	{
		FileInputStream propInFile;
		conffile_glob = configfilename;

		try
		{
			propInFile = new FileInputStream(conffile_glob);

			p = new Properties();
			p.load(propInFile);
			propInFile.close();
			
			usemm=getPropAttribute("UseMoneyManagement");
			lots=getPropAttribute("Lots");
			riskinpercent=getPropAttribute("RiskInPercent");
			maximumlots=getPropAttribute("MaximumLots");
			usefixedmoney=getPropAttribute("UseFixedMoney");
			riskinmoney=getPropAttribute("RiskInMoney");
						
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean setPropAttribute(String key, String value)
	{
		try
		{
			p.setProperty(key, value);
			FileOutputStream out = new FileOutputStream(conffile_glob);
			p.store(out, "---No Comment---");
			out.close();
			return (true);
		} catch (NullPointerException e)
		{
			e.printStackTrace();
			return (false);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	private String getPropAttribute(String prop)
	{
		String str = null;
		try
		{
			str = p.get(prop).toString();
			return (str);
		} catch (NullPointerException e)
		{
			return ("");
		}
	}

	public String getUsemm()
	{
		return usemm;
	}

	public void setUsemm(String usemm)
	{
		this.usemm = usemm;
		setPropAttribute("UseMoneyManagement", usemm);
	}

	public String getLots()
	{
		return lots;
	}

	public void setLots(String lots)
	{
		this.lots = lots;
		setPropAttribute("Lots", lots);
	}

	public String getRiskinpercent()
	{
		return riskinpercent;
	}

	public void setRiskinpercent(String riskinpercent)
	{
		this.riskinpercent = riskinpercent;
		setPropAttribute("RiskInPercent", riskinpercent);
	}

	public String getMaximumlots()
	{
		return maximumlots;
	}

	public void setMaximumlots(String maximumlots)
	{
		this.maximumlots = maximumlots;
		setPropAttribute("MaximumLots", maximumlots);
	}

	public String getUsefixedmoney()
	{
		return usefixedmoney;
	}

	public void setUsefixedmoney(String usefixedmoney)
	{
		this.usefixedmoney = usefixedmoney;
		setPropAttribute("UseFixedMoney", usefixedmoney);
	}

	public String getRiskinmoney()
	{
		return riskinmoney;
	}

	public void setRiskinmoney(String riskinmoney)
	{
		this.riskinmoney = riskinmoney;
		setPropAttribute("RiskInMoney", riskinmoney);
	}

	
	
}
