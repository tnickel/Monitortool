package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import hiflsklasse.Tracer;

public class Mbox
{
	static public void Infobox(String infostring)
	{
		if(infostring ==null)
			infostring="empty";
		
		Display display=Display.getDefault();
		Shell shell=display.getActiveShell();
		
		if(shell!=null)
		{
		MessageBox infoBox = new MessageBox(shell, SWT.ICON_INFORMATION);
        infoBox.setText("About Launcher");
        infoBox.setMessage(infostring);
        infoBox.open();
		}
		else
			Tracer.WriteTrace(10, "Info: <"+infostring+">");
	}
	static public void Infobox2(String header,String[] infostring)
	{
		String info_a="";
	
		if(infostring==null)
			info_a="empty";
		
		int anz=infostring.length;
		for(int i=0; i<anz; i++)
		{
			if(infostring[i]==null)
				break;
			info_a=info_a+infostring[i]+"\n";
		}
		
		Display display=Display.getDefault();
		Shell shell=display.getActiveShell();
		
		if(shell!=null)
		{
		MessageBox infoBox = new MessageBox(shell, SWT.ICON_INFORMATION);
        infoBox.setText(header);
        infoBox.setMessage(info_a);
        infoBox.open();
		}
		else
			Tracer.WriteTrace(10, "Info: <"+infostring+">");
	}
	static public int QuestBox(String header,String message)
	{
		
		MessageBox dialog =
			    new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_QUESTION | SWT.OK| SWT.CANCEL);
			dialog.setText(header);
			dialog.setMessage(message);
			return(dialog.open());
	}
}
	

