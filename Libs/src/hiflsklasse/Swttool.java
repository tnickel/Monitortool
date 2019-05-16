package hiflsklasse;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class Swttool
{
	static public String holeButtonEventText(SelectionEvent evt)
	{
		String input=(String)evt.toString();
		String teil=input.substring(input.indexOf("Button {")+8);
		teil=teil.substring(0,teil.indexOf("}"));
		return new String(teil);
	}
	static public int holeButtonEventZahl(SelectionEvent evt,String keyword)
	{
		String input=(String)evt.toString();
		int lenght=(keyword+" {").length();
		String teil=input.substring(input.indexOf((keyword+" {"))+lenght);
		teil=teil.substring(0,teil.indexOf("}"));
		teil=teil.replace("M", "");
		return(Integer.valueOf(teil));

	}
	
	static public void wupdate(Display dis)
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
	
	static public void baueTabellenkopf(Table table,String kopfstring)
	{
		TableColumn column=null;
		
		int anz = Tools.countZeichen(kopfstring, "#");
				
		for(int i=1; i<anz+2; i++)
		{
			String string;
			try
			{
				string = Tools.nteilstring(kopfstring, "#", i);
				
				column = new TableColumn(table, SWT.NONE);
				column.setText(string);
				
				
			} catch (ToolsException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				Tracer.WriteTrace(10, "error: internal 345335");
			}
		}
	}
	static public void baueTabellenkopfDispose(Table table,String kopfstring)
	{
		TableColumn column=null;
		
		int canz=table.getColumnCount();
		
		
		for(int i=0; i<canz; i++)
		{
			column=table.getColumn(0);
			
			
			
			
			if(column!=null)
				column.dispose();
		}
		
		int anz = Tools.countZeichen(kopfstring, "#");
				
		for(int i=1; i<anz+2; i++)
		{
			String string;
			try
			{
				string = Tools.nteilstring(kopfstring, "#", i);
				
				column = new TableColumn(table, SWT.NONE);
				column.setText(string);
				
				
				
			} catch (ToolsException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				Tracer.WriteTrace(10, "error: internal 345335");
			}
		}
	}


}
