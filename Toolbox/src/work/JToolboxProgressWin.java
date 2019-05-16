package work;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JProgressBar;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class JToolboxProgressWin
{
	final int MAX = 100;
	final JFrame frame = new JFrame("JProgress Window");
	final JProgressBar pb = new JProgressBar();

	public JToolboxProgressWin(String title,int min, int max)
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
		frame.setSize(974, 110);
		frame.setVisible(true);
		frame.setPreferredSize(new java.awt.Dimension(974, 110));
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
