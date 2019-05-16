package hilfsklasse;

import java.io.IOException;

import javax.swing.JFrame;

public class Sysaktion
{
	static private int shutdownflag = 1;

	public Sysaktion()
	{
		shutdownflag = 1;
	}

	public void CheckSleepmode()
	{
		if (shutdownflag == 0)
		{
			System.out.println("Warning no Sleepmode after last aktion !!");
		} else
			System.out.println("System go in Sleep after last Aktion");
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void GoSleep()
	{

		shutdownflag = 1;

		if (shutdownflag == 0)
		{
			System.out.println("Warning no Sleepmode after last aktion");
			return;
		}
		JFrame f = new JFrame("System wird in 120 in Sleep gefahren");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(600, 200);
		f.setVisible(true);
		try
		{
			System.out.println("Stop System in 300 sec´s");
			Thread.sleep(300000);
			Runtime rt = Runtime.getRuntime();
			Process proc;

			proc = rt.exec("rundll32.exe powrprof.dll, SetSuspendState");
			int exitVal = proc.exitValue();
			System.out.println("Process exitValue: " + exitVal);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
