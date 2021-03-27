package StartFrame;



import java.io.File;
import java.util.HashSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import data.Metaconfig;
import data.Metatraderlist;
import data.Rootpath;
import gui.Mbox;
import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.Swttool;
import hiflsklasse.Tracer;
import mtools.Mlist;

public class Brokerview
{
	//this is the main call of all brokers, this class controll all broker
	private Metatraderlist metatraderlist = new Metatraderlist();

	public void LoadBrokerTable()
	{
		metatraderlist.clear();
		preprocess();
	}

	public void moveBroker(String brokername, int direction)
	{
		// bewegt den Broker in der brokerliste
		// direction 0: up
		// direction 1: down
		int anz = metatraderlist.getsize();
		for (int i = 0; i < anz; i++)
		{
			Metaconfig meconf = metatraderlist.getelem(i);
			String brname = meconf.getBrokername();

			if (brokername.equals(brname))
			{
				// gefunden
				if ((direction == 0) && (i > 0))
					// nach oben
					// hole das element davor
					metatraderlist.changeItems(i, i - 1);
				else if ((direction == 1) && (i < anz - 1))
					// nach unten
					metatraderlist.changeItems(i, i + 1);

				break;

			}

		}

	}

	public HashSet<String> calcAllowedsaveBroker()
	{
		// erstelle die menge der broker wo die trades gespeichert werden dürfen
		HashSet<String> brokermap = new HashSet<String>();

		int anz = metatraderlist.getsize();
		for (int i = 0; i < anz; i++)
		{
			Metaconfig meconf = metatraderlist.getelem(i);
			String brname = meconf.getBrokername();
			Boolean storeable = meconf.getStoretrades();
			if (storeable == true)
				brokermap.add(brname);
		}
		return brokermap;
	}

	public void SaveBrokerTable()
	{
		postprocess();
	}

	public int getAccounttype(String brokername)
	{
		int anz = metatraderlist.getsize();
		for (int i = 0; i < anz; i++)
		{
			Metaconfig conf = metatraderlist.getelem(i);

			// sucht die metaconfig die gelöscht werden soll
			if (conf.getBrokername().equals(brokername))
			{
				return (conf.getAccounttype());
			}
		}
		Mlist.add("Error: Broker <" + brokername + "> nicht bekannt", 1);
		return -1;
	}

	public Metaconfig getMetaconfig(String expertpath)
	{
		return (metatraderlist.getMetaconfig(expertpath));
	}

	public Metaconfig getMetaconfigByBrokername(String brokername)
	{
		return (metatraderlist.getMetaconfigByBrokername(brokername));
	}

	
	
	public int getAnz()
	{
		return metatraderlist.getsize();
	}

	public Metaconfig getElem(int i)
	{
		Metaconfig me = metatraderlist.getelem(i);
		return me;

	}

	public void addElem(Metaconfig meconf)
	{
		if (checkConfigExists(meconf) == false)
			metatraderlist.addelem(meconf);
		else
			Mbox.Infobox("broker <" + meconf.getBrokername()
					+ "> schon vorhanden");
	}

	public void removeBroker(String brokername)
	{
		int anz = metatraderlist.getsize();
		for (int i = 0; i < anz; i++)
		{
			Metaconfig conf = metatraderlist.getelem(i);

			// sucht die metaconfig die gelöscht werden soll
			if (conf.getBrokername().equals(brokername))
			{
				metatraderlist.deleteelem(brokername);
				return;
			}
		}
		Tracer.WriteTrace(10, "broker <" + brokername
				+ "> zum löschen nicht vorhanden");
	}
	public int getNewTradecopymagic()
	{
		//schaut nach was die nächste freie tradecopymagic ist
		//geht hierzu durch die ganze brokerliste

		//max 100 channel möglich
		for (int i = 1; i < 100; i++)
		{
			if(checkFreeTradecopyMagic(i)==true)
			{
				Tracer.WriteTrace(20, "I:found new free tradecopymagic="+i+"");
				return i;
			}
		}
		Tracer.WriteTrace(10, "E:all tradedopymagics are occupied");
		return -99;
		
	}
	private boolean checkFreeTradecopyMagic(int magic)
	{
		//prüft ob die magic frei ist
		
		int anz = metatraderlist.getsize();
		for (int i = 0; i < anz; i++)
		{
			Metaconfig conf = metatraderlist.getelem(i);
			int m=conf.getTradecopymagic();
			if(m==magic)
				//magic schon belegt
				return false;
		}
		//die magic ist noch nicht belegt
		return true;
		
	}
	
	private boolean checkConfigExists(Metaconfig meconf)
	{
		// prüft ob diese Metratrader config schon vorhanden ist.
		// jeder brokername nur einmal
		int anz = metatraderlist.getsize();
		for (int i = 0; i < anz; i++)
		{
			Metaconfig conf = metatraderlist.getelem(i);
			if (conf.getBrokername().equals(meconf.getBrokername()))
				return true;
		}
		return false;
	}

	public String getConBroker(String brokername)
	{
		// ermittelt zu einem brokernamen den connected Masterbroker
		// der connected Masterbroker ist der Broker auf dem Life gehandelt wird
		int anz = metatraderlist.getsize();
		for (int i = 0; i < anz; i++)
		{
			Metaconfig conf = metatraderlist.getelem(i);
			if (conf.getBrokername().equals(brokername))
				return conf.getconnectedBroker();
		}
		Tracer.WriteTrace(20, "Error: Broker<" + brokername
				+ "> ist nicht konfiguriert");
		return "unbekannt";
	}

	public void toogleOnOffBroker(String brokername)
	{
		int anz = metatraderlist.getsize();
		for (int i = 0; i < anz; i++)
		{
			Metaconfig conf = metatraderlist.getelem(i);
			if (conf.getBrokername().equals(brokername))
			{
				if (conf.getOn() == 1)
					conf.setOn(0);
				else
					conf.setOn(1);
				return;
			}
		}
		Tracer.WriteTrace(20, "Error: Broker<" + brokername
				+ "> ist nicht konfiguriert");
		return;
	}

	public String getMqlData(String brokername)
	{
		// es wird für einen realshareborker der metatraderpfad rausgesucht

		int anz = metatraderlist.getsize();
		for (int i = 0; i < anz; i++)
		{
			Metaconfig metc = metatraderlist.getelem(i);

			if (metc.getBrokername().equalsIgnoreCase(brokername))
			{
				
				// in appdata sind die metatrader-root verzeichnisse
				return metc.getMqldata();
			}
		}
		return null;
	}

	public void ShowBrokerTable(Display dis, Table table, int forceflag)
	{
		//built the swt-table which show all broker
		
		Color gray = dis.getSystemColor(SWT.COLOR_GRAY);
		Color red = dis.getSystemColor(SWT.COLOR_RED);
		Color yellow = dis.getSystemColor(SWT.COLOR_DARK_YELLOW);
		Color blue = dis.getSystemColor(SWT.COLOR_BLUE);
		Device device = Display.getCurrent ();
		Color lblue = new Color(device,0,182,155);
		
		Color black =dis.getSystemColor(SWT.COLOR_BLACK);
		Color cyan =dis.getSystemColor(SWT.COLOR_CYAN);
		
		String zeil = null;
		table.removeAll();
		table.clearAll();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		//show brokername in blue if realaccount
		
		Swttool.baueTabellenkopfDispose(table,
				"path#On#S#rTime#T#Brokername#conBroker#Info#Accountnumber#LastInstallation#channel");

		int zeilcount = metatraderlist.getsize();

		for (int i = 0; i < zeilcount; i++)
		{

			TableItem item = new TableItem(table, SWT.NONE);
			Metaconfig me = metatraderlist.getelem(i);

			//set pongerror only if ea is switched on
			if ((me.getPongerrorflag() == 1)&&(me.getOn()==1))
				item.setBackground(yellow);
			else if (me.getWarningflag() == 1)
				item.setBackground(red);
			 else if (me.getAccounttype() == 2)
				item.setBackground(lblue); 

			if (me.getMqldata() != null)
				item.setText(0, String.valueOf(me.getMqldata()));
			else
				item.setText(0, String.valueOf(me.getNetworkshare_INSTALLDIR()));

			item.setText(1, String.valueOf(me.getOn()));

			// status setzen
			item.setText(2, getStatus(me, item, dis));

			
			item.setText(3, "*");
			item.setText(4, me.getAccounttypename());
			
			if(me.isRealbroker()==true)
			{	
			  item.setForeground(5, red);
			  item.setBackground(5, cyan);
			}
			else
			  item.setForeground(5,black);
			item.setText(5, String.valueOf(me.getBrokername()));

			item.setText(6, String.valueOf(me.getconnectedBroker()));
			item.setText(7, String.valueOf(me.getInfostring()));
			item.setText(8,getAccountNumber(me));
			if(me.getLastinstallation()!=null)
			  item.setText(9,me.getLastinstallation());
			if(me.isRealbroker()==true)
				item.setText(10,"----");
			else
			   item.setText(10,String.valueOf(me.getTradecopymagic()));
		}

		for (int i = 0; i < 11; i++)
		{
			table.getColumn(i).pack();
		}
		// die erste Splalte sehr klein machen
		table.getColumn(0).setWidth(5);

		if (forceflag == 1)
			postprocess();
	}
	private String getAccountNumber(Metaconfig meconf)
	{
		String fnam=meconf.getMqldata()+"\\Files\\Kontoinformation.txt";
		if(new File(fnam).exists()==false)
			return("no info");
		
		Inf inf=new Inf();
		inf.setFilename(fnam);
		String kontoinf=inf.readZeile();
		inf.close();
		if(kontoinf.length()<2)
			return("no info2");
		
		String[] parts = kontoinf.split("#");
		return parts[2];
	}
	
	public void setSortColumn(Table table, int column)
	{
		table.setSortColumn(table.getColumn(column));
	}

	public void setSortDirection(Table table, int direction)
	{
		table.setSortDirection(SWT.UP);
	}

	public void initTargetBrokerCombo(Combo targetbroker)
	{
		int anz = metatraderlist.getsize();
		for (int i = 0, pos = 0; i < anz; i++)
		{
			Metaconfig mc = metatraderlist.getelem(i);
			if ((mc.getAccounttype() == 0) || (mc.getAccounttype() == 1))
			{
				targetbroker.add(mc.getBrokername(), pos);
				pos++;
			}
		}
	}

	private void preprocess()
	{
		// die config einlesen
		String fnam = Rootpath.getRootpath() + "\\conf\\brokerconf.xml";

		if (FileAccess.FileAvailable(fnam) == false)
			return;

		Inf inf = new Inf();
		inf.setFilename(fnam);

		// keylist einlesen
		metatraderlist = (Metatraderlist) inf.loadXST();
		inf.close();
		
		
		
		// die magicliste initialisieren
		int anz = metatraderlist.getsize();
		for (int i = 0; i < anz; i++)
		{
			Metaconfig mc = metatraderlist.getelem(i);
			mc.initmagiclist();
		}
	}

	private synchronized void  postprocess()
	{
		// die config abspeichern

		File fnam = new File(Rootpath.getRootpath() + "\\conf\\brokerconf.xml");
		File fnamold = new File(Rootpath.getRootpath()
				+ "\\conf\\brokerconf.old");
		File fnamtmp = new File(Rootpath.getRootpath()
				+ "\\conf\\brokerconf.tmp");

		// speichere als temp
		Inf inf = new Inf();
		inf.setFilename(fnamtmp.getPath());
		inf.saveXST(metatraderlist);
		inf.close();

		// wenn *.tmp < 0.5 *old ist, dann stimmt was nicht, breche ab.
		double lenold=fnamold.length();
		double lentmp=fnamtmp.length();
		
		if(lentmp>(lenold/2))
		{ 
			//das neue ist länger, dann ist alles ok, das alte kann gelöscht werden
			if (fnamold.exists())
				if(fnamold.delete()==false)
					Tracer.WriteTrace(10, "E:postprocess:cant delete file <"+fnamold+">");
			
			// *.xml nach *.old umbenennen
			if (fnam.exists() == true)
				if(fnam.renameTo(fnamold)==false)
					Tracer.WriteTrace(10, "E:postprocess:cant rename file <"+fnam+">");

			if(fnamtmp.renameTo(fnam)==false)
				Tracer.WriteTrace(10, "E:postprocess:cant rename file <"+fnam+">");
		}
		else
		{
			//es hat nicht geklappt, das alte ist länger *.old
			//es hat also gecrashed
			Tracer.WriteTrace(20, "E:Fatal error problem with datafile <"+fnamold.getAbsolutePath()+"> I restore It");
			
			//fnamtmp löschen
			if((fnamtmp.exists())&&(fnamtmp.delete()==false))
				Tracer.WriteTrace(10, "E:postprocess:cant delete <"+fnamtmp.getAbsolutePath()+">");

			//das zielfile sollte nicht da sein
			if((fnam.exists())&&(fnam.delete()==false))
				Tracer.WriteTrace(10, "E:postprocess:cant delete <"+fnamtmp.getAbsolutePath()+">");
			
			//aus *.old wieder das zielfile herstellen
			if(FileAccess.copyFile(fnamold.getAbsolutePath(), fnam.getAbsolutePath())==false)
				Tracer.WriteTrace(10, "E:postprocess:cant copy from<"+fnamold.getAbsolutePath()+"> to<"+fnam.getAbsolutePath()+">");
			
		}
		
		
	}

	private String getStatus(Metaconfig me, TableItem item, Display dis)
	{
		org.eclipse.swt.graphics.Color red = dis.getSystemColor(SWT.COLOR_RED);
		// schaut nach wieviele Stunden das Logfile alt ist
		String netshare = me.getMqldata();
		String dirnam = netshare + "\\logs";

		File fd = new File(dirnam);
		if (fd.exists() == false)
			return ("noLogs");

		long secs = FileAccess.getLastModifiedVerzSecs(dirnam);
		long hours = secs / 3600;

		if (hours > 5)
			item.setForeground(2, red);

		return (String.valueOf(hours));
	}
}
