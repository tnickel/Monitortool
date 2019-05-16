package mtools;

import hiflsklasse.Tools;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

public class Mlist
{
	static List messagelist=null;
	static Display display_glob=null;
	static int displayflag=1;
	public Mlist(List ml,Display dis_glob)
	{
		display_glob=dis_glob;
		messagelist=ml;
	}
	static public void on()
	{
		displayflag=1;
	}
	static public void off()
	{
		displayflag=0;
	}
	
	static public void add(String text)
	{
		if(displayflag==0)
			return;
		String timestr=Tools.get_aktdatetime_str();
		timestr=timestr.substring(timestr.indexOf(" "));
		messagelist.add(timestr+" "+text,0);
		Display.getCurrent().readAndDispatch();
	}
	static public void add(String text,int forceflag)
	{
		if(displayflag==0)
			return;
		String timestr=Tools.get_aktdatetime_str();
		timestr=timestr.substring(timestr.indexOf(" "));
		messagelist.add(timestr+" "+text,0);

		if(forceflag==1)
			mupdate(display_glob);
	}
	static public void clear()
	{
		if(displayflag==0)
			return;
		messagelist.removeAll();
		mupdate(display_glob);
	}
	static public void mupdate(Display dis)
	{
		if(dis==null)
			return;
	
		Shell sh=dis.getActiveShell();
		
		if(sh==null)
			return;
		while (!sh.isDisposed())
		{
			
			if (!dis.readAndDispatch())
				break;
		}
	}
}
