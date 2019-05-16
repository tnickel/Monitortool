package dia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class Fdia
{
	public Fdia()
	{}
	
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
	public static String FileDialog(Display dis,String verzprefix)
	{
		FileDialog fileDialog = new FileDialog(dis.getActiveShell(), SWT.READ_ONLY);
		fileDialog.setText("read");
		fileDialog.setFilterPath(verzprefix);

		String fileName = fileDialog.open();
		
		
		return fileName;
	}

}
