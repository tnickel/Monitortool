package droptool;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class dropdistool
{
	static public void UpdateDisplay()
	{
		Display dis=Display.getDefault();
		Shell sh=dis.getActiveShell();
		
		
		
		if(sh==null)
			return;
	
		 while (!sh.isDisposed()) {
			 
		      if (!dis.readAndDispatch()) 
		        break;
		    }
	}
}
