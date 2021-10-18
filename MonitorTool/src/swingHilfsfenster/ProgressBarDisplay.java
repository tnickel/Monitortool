package swingHilfsfenster;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class ProgressBarDisplay
{
	final int MAX = 100;
	final JFrame frame = new JFrame("JProgress Window");
	final JProgressBar pb = new JProgressBar();

	public ProgressBarDisplay(String title,int min, int max)
	{
		// creates progress bar
		frame.setTitle(title);
		pb.setMinimum(min);
		pb.setMaximum(max);
		pb.setStringPainted(true);

		// add progress bar
		CardLayout frameLayout = new CardLayout();
		frame.getContentPane().setLayout(frameLayout);
		frame.getContentPane().add(pb, "pb");

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(1400, 130);
		frame.setVisible(true);
		frame.setPreferredSize(new java.awt.Dimension(1400, 130));
	}

	
	
	public void update(final int pcount)
	{
		pb.setValue(pcount);
		pb.paint(pb.getGraphics());
		pb.update(pb.getGraphics());
	}

	public void end()
	{
		frame.dispose();
	}
}
