package hiflsklasse;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class UI
{
	public UI()
	{
	}

	/**
	 * 
	 */
	public void FehlermeldungStop(String meldung)
	{
		Object[] optionen =
		{ "Weitermachen", "Abbruch" };
		int n = JOptionPane.showOptionDialog(new JFrame(), meldung,
				"ERROR !!!!", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE, null, optionen, optionen[1]);

		if (n == 1)
			System.exit(99);
	}

	public boolean FehlermeldungAbfrage(String meldung)
	{
		Object[] optionen =
		{ "Weitermachen", "Abbruch" };
		int n = JOptionPane.showOptionDialog(new JFrame(), meldung,
				"ERROR !!!!", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE, null, optionen, optionen[1]);

		if (n == 1)
			return false;
		else
			return true;
	}
}