package swtDropwindow;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import data.DropGlobalVar;
import dia.Fdia;

public class EditconfigWork
{

	static public String setMetatraderpath(Text path)
	{
		String fnam = Fdia.DirDialog(Display.getDefault(),
				DropGlobalVar.getMetarootpath());

		// falls cancel dann wird nix geändert
		if (fnam == null)
			fnam = DropGlobalVar.getMetarootpath();
		System.out.println("fnam=" + fnam);
		path.setText(fnam);
		return fnam;
	}
	static public String setDropboxdir(Text dropboxdir)
	{
		String fnam = Fdia.DirDialog(Display.getDefault(),
				DropGlobalVar.getDropboxdir());

		// falls cancel dann wird nix geändert
		if (fnam == null)
			fnam = DropGlobalVar.getDropboxdir();
		System.out.println("fnam=" + fnam);
		dropboxdir.setText(fnam);
		return fnam;
	}
	static public String setFstdir(Text fstdir)
	{
		String fnam = Fdia.DirDialog(Display.getDefault(),
				DropGlobalVar.getDropboxdir());

		// falls cancel dann wird nix geändert
		if (fnam == null)
			fnam = DropGlobalVar.getFstrootpath();
		System.out.println("fnam=" + fnam);
		fstdir.setText(fnam);
		return fnam;
	}
	static public String setUpdatedir(Text updatepath)
	{
		String fnam = Fdia.DirDialog(Display.getDefault(),
				DropGlobalVar.getUpdatedir());

		// falls cancel dann wird nix geändert
		if (fnam == null)
			fnam = DropGlobalVar.getFstrootpath();
		System.out.println("fnam=" + fnam);
		updatepath.setText(fnam);
		return fnam;
	}
}
