package data;

import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;
import hiflsklasse.Viewer;

import java.io.File;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import StartFrame.Brokerview;

public class Ea implements Comparable<Ea>
{
	int magic=0;
	String broker=null;
	//falls der ea auf dem realaccount installiert ist dann ist das 1
	int inst=0;
	//auto=0=> automatic off, auto=1=> automatik on, auto=2=> lateswitchon = aktiv
	//die automatik wird erst aktiviert wenn der gdx unterschritten wird.
	
	int auto=0;
	int on=0;
	//falls gd20flag=1 ist der GD20 überschritten
	int gd20flag=0;
	//gibt an von welchen broker dieser ea installiert wurde
	//für die automatik
	String InstFrom=null;

	int period=0;
	int gdx=0;
	String eafilename=null;
	String symbol=null;
	String info=null;
	String info2=null;
	String description=null;
	//0=default, 1=gdx, 2=line, 3=fullautomatic
	int tradelogiktype=0;
	//falls dies flag gesetzt ist wird nur ausgeschaltet und nie wieder an
	int switchOnlyOffFlag=0;
	//hier wird bis zum einschalten erst eine bestimmte Zeit gewartet
	int waitAfterOffTime=0;
	//falls dieses flag=1, wird die automatik erst bei unterschreiten des gdx aktiviert
	int lateswitchonflag=0;
	
	int switchonval=0;
	int switchoffval=0;
	//speicher die Zeit wo der ea das letzte mal ausgeschaltet worden ist
	Date lastofftime=null;

	
	//reallotsize, uselotsize und lotsizekeyword wird nicht mehr verwendet
	double reallotsize=0.01;//alt
	boolean uselotsize=false;//alt
	String lotsizekeyword=null;//alt
	
	
	public int getMagic()
	{
		return magic;
	}

	public void setMagic(int magic)
	{
		this.magic = magic;
	}




	public String getBroker()
	{
		return broker;
	}




	public void setBroker(String broker)
	{
		this.broker = broker;
	}




	public int getInst()
	{
		return inst;
	}




	public void setInst(int inst)
	{
		this.inst = inst;
	}




	public int getAuto()
	{
		return auto;
	}




	public void setAuto(int auto)
	{
		this.auto = auto;
	}




	public int checkOn(String metatraderroot)
	{
		//prüft auf platte nach ob wirklich on
		if(FileAccess.FileAvailable(metatraderroot+"\\files\\"+magic+".on"))
		{
			on=1;
			return 1;
		}
		
		on=0;
		return 0;
	}
	public int getOn()
	{
		return on;
	}



	public void setOn(int on)
	{
		this.on = on;
	}



	//dieser ea wurde installiert von dem Broker
	public String getInstFrom()
	{
		return InstFrom;
	}




	public void setInstFrom(String instFrom)
	{
		InstFrom = instFrom;
	}




	public int getPeriod()
	{
		return period;
	}




	public void setPeriod(int period)
	{
		this.period = period;
	}




	public String getEafilename()
	{
		return eafilename;
	}




	public void setEafilename(String eafilename)
	{
		this.eafilename = eafilename;
	}




	public String getSymbol()
	{
		return symbol;
	}




	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}




	public int getGd20flag()
	{
		return gd20flag;
	}

	public void setGd20flag(int gd20flag)
	{
		this.gd20flag = gd20flag;
	}

	public String getInfo()
	{
		return info;
	}

	public void setInfo(String info)
	{
		this.info = info;
	}

	public String getInfo2()
	{
		return info2;
	}

	public void setInfo2(String info2)
	{
		this.info2 = info2;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public int getTradelogiktype()
	{
		return tradelogiktype;
	}

	public void setTradelogiktype(int tradelogiktype)
	{
		this.tradelogiktype = tradelogiktype;
	}

	public int getSwitchOnlyOffFlag()
	{
		return switchOnlyOffFlag;
	}

	public void setSwitchOnlyOffFlag(int switchOnlyOffFlag)
	{
		this.switchOnlyOffFlag = switchOnlyOffFlag;
	}

	public int getWaitAfterOffTime()
	{
		return waitAfterOffTime;
	}

	public void setWaitAfterOffTime(int waitAfterOffTime)
	{
		this.waitAfterOffTime = waitAfterOffTime;
	}

	public int getSwitchonval()
	{
		return switchonval;
	}

	public void setSwitchonval(int switchonval)
	{
		this.switchonval = switchonval;
	}

	public int getSwitchoffval()
	{
		return switchoffval;
	}

	public void setSwitchoffval(int switchoffval)
	{
		this.switchoffval = switchoffval;
	}

	public Date getLastofftime()
	{
		return lastofftime;
	}

	public void setLastofftime(Date lastofftime)
	{
		this.lastofftime = lastofftime;
	}

	public String getlotsize_str(Brokerview brokerview)
	{
		    String netshare=calcNetworkshare( brokerview);
			//holt sich die lotsize von platte
			String fnam=netshare+"\\files\\"+magic+".lot";
			File fnamf=new File(fnam);
		
			if(fnamf.exists())
			{
				Inf inf = new Inf();
				inf.setFilename(fnam);
				String mem=inf.readMemFile();
				if(mem.contains("mmLotsIfNoMM" )==true)
				{	//den vorderen Teil abschneiden
					mem=mem.substring(mem.indexOf("mmLotsIfNoMM =")+14);
					//den hinteren teil abschneiden
					mem=mem.substring(0,mem.indexOf(";"));
					
					
					return (mem);
				}
				else
					 return "...";
			}
		
		return "...";
	}

	
	
	
	public void setReallotsize(Brokerview brokerview,float reallotsize)
	{
		//setzt die lotsize auf platte
		String netshare=calcNetworkshare( brokerview);
		String fnam=netshare+"\\files\\"+magic+".lot";
		File fnamf=new File(fnam);
		if(fnamf.exists())
			fnamf.delete();
		
		
			Inf inf = new Inf();
			inf.setFilename(fnam);
			inf.writezeile(String.valueOf(reallotsize));
			inf.close();
			
		
	}

	public String calcNetworkshare(Brokerview brokerview)
	{
		if(brokerview==null)
			return null;
		
		// jetzt wird der realbrokershare ermittelt
				Metaconfig meconf = brokerview
						.getMetaconfigByBrokername(broker);
				if (meconf == null)
					return null;

				String realbrokerroot = meconf.getMqldata();
				if (realbrokerroot == null)
					return null;
				return realbrokerroot;

	}
	
	
	
	

	public int getGdx()
	{
		return gdx;
	}

	public void setGdx(int gdx)
	{
		this.gdx = gdx;
	}

	public int getLateswitchonflag()
	{
		return lateswitchonflag;
	}

	public void setLateswitchonflag(int lateswitchonflag)
	{
		this.lateswitchonflag = lateswitchonflag;
	}

	
	
	public String holeStrFilename(Ealiste eal,Metaconfig meconf,String tradecomment,int nachfrageflag)
	{
		//falls nachfrageflag==1 dann wird nochmal nach dem *.str file explizit nachgefragt
		
		// den str-filenamen holen
		String filename = null;
		while (5 == 5)
		{
			// Versuch1:falls der ea auf diesen Rechner installiert wurde und
			// das File auch existiert
			filename = meconf.getMqlquellverz() + "\\" + this.getEafilename();
			if(filename!=null)
				filename=filename.replace(".mq4", ".str");
			if (FileAccess.FileAvailable(filename) == true)
				break;

			// Versuch2: der Eafilename ist der comment und das File exisitert
			// auch
			filename = meconf.getMqlquellverz() + "\\" + tradecomment + ".str";
			if (FileAccess.FileAvailable(filename) == true)
				break;

			//nur falls nachfrage gewünscht dann frage auch.
			if(nachfrageflag==0)
				break;
			
			// Versuch3: kann das File nicht finden, dann frage nach
			Viewer v = new Viewer();
			filename = v.fileRequesterStr(Display.getDefault(),
					"Please select a Backtestfile", meconf.getMqlquellverz(),
					SWT.OPEN);
			this.setEafilename(filename.substring(filename.lastIndexOf("\\")+1));
			
			if(filename.length()>2)
				eal.store(0);
			break;
		}
		return filename;
	}
	public Boolean checkIfInstalled(Metaconfig meconf)
	{
			String fnam=meconf.getMqldata()+"\\experts\\"+eafilename;
			fnam=fnam.replace(".mq4", "");
			fnam=fnam.replace(".", "");
			fnam=fnam+".mq4";
		
			File eanam=new File(fnam);
			if(eanam.exists()==false)
				Tracer.WriteTrace(20, "I:EA <"+eanam.getAbsolutePath()+"> -> old trades of this ea, I don´t show this");
			
			return(eanam.exists());
	
	}
	
	public String getTradelogikinfo()
	{
		if(tradelogiktype==0)
			return("...");
		else if(tradelogiktype==1)
			return(String.valueOf(gdx));
		else if(tradelogiktype==2)
			return ("lim");
		else if(tradelogiktype==3)
			return("faut");
		else return("err");
	}
	
	public int compareTo(Ea ea)
	{
		

		return 0;

	}
}
