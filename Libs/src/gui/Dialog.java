package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
public class Dialog
{
	

	public static String DirDialog(Display dis,String verzprefix)
	{
		String retstring=null;
		Shell shell = dis.getActiveShell();
		dis.getActiveShell().open();
		DirectoryDialog dialog = new DirectoryDialog(shell);
		String platform = SWT.getPlatform();
		dialog.setFilterPath(platform.equals("win32") || platform.equals("wpf") ? verzprefix
				: "/");
		
		retstring=dialog.open();
		return retstring;
	}
	
	
}
