package applets;

import hilfsklasse.KeyReader;

import java.applet.Applet;
import java.awt.Font;
import java.awt.Graphics;

public class UserInfoApplet extends Applet
{

	Font f;

	@Override
	public void init()
	{

		int Groesse = 10; // einf. Typen: byte, short, int, long, float,
		// double, char, boolean
		String Schriftart = "Helvetica";

		f = new Font(Schriftart, Font.BOLD, Groesse); // new-Operator und
		// Konstruktor "Font"
		System.out.println("Java-Konsole: init... !"); // Netscape:
		// "Options"-Menü
	}

	@Override
	public void paint(Graphics g)
	{
		// int yoffset=12;
		int ypos = 15;
		// String z=null;
		KeyReader kr = new KeyReader();
		kr.SetReader("f:\\offline\\handdata\\userinfo\\siebtersinn.txt");
		g.setFont(f);
		g.drawString("xUser:SiebterSinn", 10, ypos);
		System.out.println("Java-Konsole: paint1... !");
		System.out.println("Java-Konsole: paint... !");
		/*
		 * while((z=kr.GetLine())!=null) {
		 * System.out.println("Java-Konsole: paintx... !"); ypos=ypos+yoffset;
		 * g.drawString(z, 10, ypos); }
		 */
		System.out.println("Java-Konsole: paint... !");
	}

	@Override
	public void stop()
	{
		System.out.println("Java-Konsole: stop... !");
	}
}
