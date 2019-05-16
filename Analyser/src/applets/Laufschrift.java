package applets;

import java.applet.Applet;
import java.awt.Graphics;

public class Laufschrift extends Applet implements Runnable
{

	int x, y, breite;
	Thread my_thread;
	String text;

	@Override
	public void init()
	{
		text = getParameter("text");
		y = 20;
		breite = 260;
		if (x > breite)
			x = breite;
	}

	@Override
	public void start()
	{
		my_thread = new Thread(this);
		my_thread.start();
	}

	public void run()
	{
		while (true)
		{
			repaint();
			x -= 10;
			if (x < 0)
				x = breite;
			try
			{ // Thread erfordert Ausnahme-Handler (try-catch-Klausel)
				Thread.sleep(100);
			} catch (InterruptedException e)
			{
			}
		}
	}

	@Override
	public void paint(Graphics g)
	{
		g.drawString(text, x, y);
	}
}