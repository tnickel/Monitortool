package applets;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Bubbles extends Applet implements Runnable
{
	int x, y;
	Random rand;
	Color c;
	int red, green, blue;
	Thread myThread;

	@Override
	public void init()
	{
		rand = new Random();
	}

	@Override
	public void start()
	{
		myThread = new Thread(this);
		myThread.start();
	}

	@Override
	public void stop()
	{
		myThread.stop();
	}

	public void run()
	{

		while (true)
		{

			x = rand.nextInt() % size().width;
			x = Math.abs(x);
			y = rand.nextInt() % size().height;
			y = Math.abs(y);

			red = rand.nextInt() % 255;
			red = Math.abs(red);

			green = rand.nextInt() % 255;
			green = Math.abs(green);

			blue = rand.nextInt() % 255;
			blue = Math.abs(blue);

			c = new Color(red, green, blue);

			Graphics g = this.getGraphics();
			g.setColor(c);
			g.fillOval(x, y, 30, 30);
			g.setColor(Color.black);
			g.drawString("Java", x + 5, y + 18);

			try
			{
				Thread.sleep(5);
			} catch (InterruptedException e)
			{
			}
		}
	}
}
