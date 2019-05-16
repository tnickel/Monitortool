package ftool;

import java.io.File;
import java.io.IOException;

import mtools.Mlist;

public class DropFile
{
	static public boolean waitFreeLock(String lockfilenam, int maxwait)
	{
		//wartet auf einen freien lock
		//true: falls alles geklappt hat
		//false: falls maxwait überschritten wurde
		
		File flock= new File(lockfilenam);
		
		int count=0;
		while(flock.exists())
		{
			count++;
			if(count>maxwait)
			{
				Mlist.add("Warning lock > 180 sec <"+lockfilenam+">", 1);
				return false;
			}
			try
			{
				Thread.sleep(1000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return true;
	}
	static public void setLock(String lockfilenam)
	{
		File flock= new File(lockfilenam);
		try
		{
			if(flock.createNewFile()==false)
				Mlist.add("Error: can´t write <"+lockfilenam+">", 1);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	static public void freeLock(String lockfilenam)
	{
		File flock= new File(lockfilenam);
		if(flock.delete()==false)
			Mlist.add("Error: can´t delete <"+lockfilenam+">", 1);
		
	}
	static public void sleep(int millisecs)
	{
		
		try
		{
			Thread.sleep(millisecs);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
