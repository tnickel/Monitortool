package applets;

import java.applet.Applet;
import java.awt.Font;
import java.awt.Graphics;

public class HalloApplet extends Applet
{

	Font f;

	@Override
	public void init()
	{

		int Groesse = 18; // einf. Typen: byte, short, int, long, float, double,
							// char, boolean
		String Schriftart = "Helvetica";

		f = new Font(Schriftart, Font.BOLD, Groesse); // new-Operator und
														// Konstruktor "Font"
		System.out.println("Java-Konsole: init... !"); // Netscape:
														// "Options"-Menü
	}

	@Override
	public void paint(Graphics g)
	{

		g.setFont(f);
		g.drawString("Hallo, Java-Fans !", 50, 25);
		System.out.println("Java-Konsole: paint... !");
	}

	@Override
	public void stop()
	{
		System.out.println("Java-Konsole: stop... !");
	}
}