package Sync;

import gui.Mbox;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;

import java.io.File;
import java.io.IOException;

import mtools.Mlist;
import data.Rootpath;

public class Lock
{
	static public boolean waitFreeLock(String file)
	{
		int counter=0;
		while (checklockOwncheck(file) == true)
		{
			try
			{
				counter++;
				Thread.sleep(2000);
				System.out.println("exporter<" + file + "> locked");
				Mlist.add("<" + file + "> locked");
				Mlist.add("Wait free lock<"+file+">", 1);
				
				if(counter>30)
					Mbox.Infobox("warning: lock<"+file+"> locked longer than 90 sec");
				if(counter>31)
				{
					Mbox.Infobox("warning: Do you want to kill this lock<"+file+"> ");
					File f= new File(file);
					f.delete();
				}
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
	static private boolean checklock(String fnam)
	{
		if(fnam==null)
			return false;
		
		// prüft ob der history exporter gelocked ist
		File f = new File(fnam);
		if (f.exists() == true)
			return true;
		else
			return false;

	}
	static private boolean checklockOwncheck(String fnam)
	{
		//prüft einen lock
		//falls es der eigene Lock ist darf dieser auch wieder gelöscht werden
		//den eigenen lock erkennt man am rootpath
		//return:
		//true: falls noch fremder lock gesetzt
		//false: falls kein lock gefunden wurde und somit alles frei ist
		
		if(fnam==null)
			return false;
		
		File f = new File(fnam);
		if (f.exists() == true)
		{
			//wenn es der eigene lock ist darf man ihn entfernen (programm ist ja abgestürtzt etc)
			//man meldet dann false zurück.
			Inf inf= new Inf();
			inf.setFilename(fnam);
			String lockstring=inf.readZeile("UTF-8");
			inf.close();
			//falls eigener lock
			if((lockstring!=null)&&(lockstring.equalsIgnoreCase(Rootpath.getRootpath())==true))
			{
				//lösche den eigenen Lock
				f.delete();
				return false;
			}
			else
			return true;
		}
		else
			return false;

	}
	static public void lock(String fnam,String message)
	{
		if(fnam==null)
			return;
		
		//setzt einen lock
		//fnam=filename des locks
		//message=message die im lock abgelegt wird
		File f = new File(fnam);
		try
		{
			f.createNewFile();
			Inf inf= new Inf();
			inf.setFilename(fnam);
			inf.writezeile(message);
			inf.close();
		} catch (IOException e)
		{
			Tracer.WriteTrace(20, "W:Path not found <"+fnam+">");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static public void unlock(String fnam)
	{
		File f = new File(fnam);
		f.delete();
	}
}
