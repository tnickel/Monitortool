package swingHilfsfenster;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JFrame;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

public class ProgressBar2 extends JFrame implements ActionListener
{

	static ProgressMonitor pbar;

	static int counter = 0;
	static int maxcounter=0;

	public ProgressBar2()
	{
		super("Progress Monitor Demo");
		setSize(250, 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		pbar = new ProgressMonitor(null, "Monitoring Progress",
				"Initializing . . .", 0, maxcounter);

		// Fire a timer every once in a while to update the progress.
		Timer timer = new Timer(500, this);
		timer.start();
		setVisible(true);
	}

	public static void setCounter(int c)
	{
		counter=c;
	}
	
	public static void main(String args[],int max)
	{
		UIManager.put("ProgressMonitor.progressText", "This is progress?");
		UIManager.put("OptionPane.cancelButtonText", "Go Away");
		maxcounter=max;
		new ProgressBar2();
	}

	
	
	public void actionPerformed(ActionEvent e)
	{
		// Invoked by the timer every half second. Simply place
		// the progress monitor update on the event queue.
		SwingUtilities.invokeLater(new Update());
	}

	class Update implements Runnable
	{
		 NumberFormat numberFormat = new DecimalFormat("0.0");
		
		
		public void run()
		{
			if (pbar.isCanceled())
			{
				pbar.close();
				System.exit(1);
			}
			pbar.setProgress(counter);
			pbar.setNote("Operation is " + numberFormat.format(((float)counter/(float)maxcounter)) + "% complete");
			counter += 2000;
		}
	}
}