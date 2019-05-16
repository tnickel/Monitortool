package montool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class MonDia
{
	private static String verzprefix_glob=null;
	public static String FileDialog(Display dis,String verzprefix)
	{
		FileDialog fileDialog = new FileDialog(dis.getActiveShell(), SWT.READ_ONLY);
		fileDialog.setText("read");
		if((verzprefix!=null)&&(verzprefix.length()>0))
			verzprefix_glob=verzprefix;
		
		fileDialog.setFilterPath(verzprefix_glob);

		String fileName = fileDialog.open();
		return fileName;
	}
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
