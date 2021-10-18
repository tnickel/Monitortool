package swtHilfsfenster;

import gui.Mbox;
import hiflsklasse.Tracer;
import modtools.Installer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;

import Metriklibs.FileAccessDyn;
import StartFrame.Tableview;
import data.Metaconfig;

public class SwtEditBrokerConfigWork
{
	private Installer installer = new Installer();

	public boolean installDemoEas(Display dis, ProgressBar progressBar1,
			Metaconfig metaconfig, Metaconfig metarealconfig,
			String mqlquellverz, Tableview tv)
	{
		// Sicherheitsabfrage ob wirklich die EA´s installiert werden sollen
		MessageBox dialog = new MessageBox(dis.getActiveShell(),
				SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
		dialog.setText("My info");
		dialog.setMessage("Do you really want to do this?");
		if (dialog.open() != 32)
			return false;

		if (checkFileanz(mqlquellverz, 100) == false)
			return false;

		if (metaconfig.getOnlyhandinstalled() == 0)
		{
			if ((metaconfig.getAccounttype() >= 0)
					&& (metaconfig.getAccounttype() < 3))
				installer.InstallMetatraderDemoEaFiles(dis, progressBar1,
						mqlquellverz, metaconfig, metarealconfig,
						tv.getEaliste(), tv);

			tv.getEaliste().store(0);
			System.out.println("Es wurden EAs installiert !!");
		} else
			Tracer.WriteTrace(10,
					"W: it is not allowd to install something because the not modifyflag ist set");
		return (true);
	}

	private boolean checkFileanz(String verz, int anz)
	{
		int mqlcounter = 0;
		FileAccessDyn fadyn = new FileAccessDyn();
		fadyn.initFileSystemList(verz, 1);
		int fanz = fadyn.holeFileAnz();
		for (int i = 0; i < fanz; i++)
		{
			String fnam = fadyn.holeFileSystemName();
			if (fnam.endsWith(".mq4"))
				mqlcounter++;

		}
		if (mqlcounter > anz)
		{
			Mbox.Infobox("Too much Metatrader in <" + verz + "> anz<"
					+ fadyn.holeFileAnz() + "> max allowed = " + anz);
			return false;
		}
		return true;
	}

	public boolean installRealEas(Display dis, ProgressBar progressBar1,
			Metaconfig metaconfig, Metaconfig metarealconfig,
			String mqlquellverz, String metatraderroot, Tableview tv)
	{
		// Sicherheitsabfrage ob wirklich die EA´s installiert werden sollen
		MessageBox dialog = new MessageBox(dis.getActiveShell(),
				SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
		dialog.setText("My info");
		dialog.setMessage("Do you really want to do this?");
		if (dialog.open() != 32)
			return false;
		installer
				.InstallMetatraderRealEaFiles(dis, progressBar1, mqlquellverz,
						metatraderroot, metaconfig, metarealconfig,
						tv.getEaliste(), tv);
		System.out.println("Es wurde installiert !!");
		tv.refresh();
		return (true);
	}

	public boolean cleanRealAccount(Display dis, ProgressBar progressBar1,
			Metaconfig metaconfig, String mqlquellverz, Metaconfig meconf_real)
	{
		// Sicherheitsabfrage ob wirklich die EA´s installiert werden sollen
		MessageBox dialog = new MessageBox(dis.getActiveShell(),
				SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
		dialog.setText("My info");
		dialog.setMessage("Do you really want to delete all EA´s on Realaccount ?");
		if (dialog.open() != 32)
			return false;

		installer.cleanRealAccount(dis, progressBar1, mqlquellverz,
				meconf_real, metaconfig);
		System.out.println("CleanRealaccount !!");
		return (true);
	}

	public void genNewMetatraderaccount(String mtroot)
	{
		installer.genNewMetatraderaccount(mtroot);
	}

	public void initMetatrader(Metaconfig metaconfig)
	{
		// wenn der button gedrückt dann wird auf jeden fall initialisiert
		installer.InitMetatrader(metaconfig, 1);
	}

	public boolean checkMqlQuellverzeichniss(String mqlverz)
	{
		// prüft ob sich in dem Verzeichnis mql-files befinden
		if (mqlverz.length() < 2)
			return false;

		FileAccessDyn fdyn = new FileAccessDyn();
		fdyn.initFileSystemList(mqlverz, 1);

		int anz = fdyn.holeFileAnz();

		if (anz == 0)
			return false;

		for (int i = 0; i < anz; i++)
		{
			String fnam = fdyn.holeFileSystemName();

			if (fnam.contains(".mq4"))
				return true;
		}
		return false;
	}

	public boolean checkMetatraderverzeichniss(String verz)
	{
		// prüft ob dies ein Metatraderverzeichniss ist
		// hier wird geprüft ob sich terminal.exe in dem verzeichniss befindet

		if ((verz.contains("dopbox") == true)
				|| (verz.contains("Dropbox") == true))
			return true;

		FileAccessDyn fdyn = new FileAccessDyn();
		fdyn.initFileSystemList(verz, 1);
		int anz = fdyn.holeFileAnz();
		if (anz == 0)
			return false;

		for (int i = 0; i < anz; i++)
		{
			String fnam = fdyn.holeFileSystemName();
			if ((fnam.contains("terminal.exe"))||(fnam.contains("terminal64.exe")))
				return true;
		}
		return false;
	}
}
