package hiflsklasse;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class UI
{
	public UI()
	{
	}

	/**
	 * 
	 */
	public void FehlermeldungStop(String meldung, String fehlertypemessage)
	{
		// Schriftgröße auf 20 Pixel setzen
		UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 30));
		UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 30));

		Object[] optionen =
		{ "Weitermachen", "Abbruch" };
		int n = JOptionPane.showOptionDialog(new JFrame(), meldung,
				fehlertypemessage, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE, null, optionen, optionen[1]);

		if (n == 1)
			System.exit(99);
	}

	public boolean FehlermeldungAbfrage(String meldung, String fehlertypemessage)
	{
		// Schriftgröße auf 20 Pixel setzen
		UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 30));
		UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 30));

		Object[] optionen =
		{ "Weitermachen", "Abbruch" };
		int n = JOptionPane.showOptionDialog(new JFrame(), meldung,
				fehlertypemessage, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE, null, optionen, optionen[1]);

		if (n == 1)
			return false;
		else
			return true;
	}
}