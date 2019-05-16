package mtools;

import hiflsklasse.Tracer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class DisTool
{
	private static Display dis_glob=null;
	private static Shell shell_glob=null;
	public DisTool(Display dis,Shell shell)
	{
		dis_glob=dis;
		shell_glob=shell;
	}
	
	static public void UpdateDisplay()
	{
	
		Display dis=dis_glob;
		
		if (dis == null)
			return;
		
		
		int count = 0;
		Shell sh = shell_glob;

		if (sh == null)
			return;
		 while (!sh.isDisposed()) {
			 
		      if (!dis.readAndDispatch()) 
		      {
		    	count ++;
		    	if(count>3)
					break;
		        dis.sleep();
		      }
		    }
	}
	static public void UpdateDisplay2()
	{
	
		Display dis=dis_glob;
		
		if (dis == null)
			return;
		
		
		int count = 0;
		Shell sh = shell_glob;

		if (sh == null)
			return;
		 while (!sh.isDisposed()) {
			 
			  if (!dis.readAndDispatch()) 
		      {
		    	break;
		      }
		    }
	}
	static public void waitCursor()
	{
		shell_glob.setCursor(new Cursor(dis_glob, SWT.CURSOR_WAIT));
	}
	static public void arrowCursor()
	{
		if(dis_glob==null)
			Tracer.WriteTrace(10, "internal error disglob=null");
		Shell sh=shell_glob;
		if(sh==null)
			Tracer.WriteTrace(10, "internal errorshell=null");
		
		sh.setCursor(new Cursor(dis_glob, SWT.CURSOR_ARROW));
	}
}
