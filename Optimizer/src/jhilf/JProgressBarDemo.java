package jhilf;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class JProgressBarDemo
{
	static int pval=0;
	
	public static void main(String[] args)
	{
		
		start();
		start();
	}
	
	
	public  static void start()
	{
		final int MAX = 100;
		final JFrame frame = new JFrame("JProgress Demo");

		// creates progress bar
		final JProgressBar pb = new JProgressBar();
		pb.setMinimum(0);
		pb.setMaximum(MAX);
		pb.setStringPainted(true);

		// add progress bar
		frame.setLayout(new FlowLayout());
		frame.getContentPane().add(pb);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 200);
		frame.setVisible(true);

		System.out.println("hallo");

		// update progressbar
		for (int i = 0; i <= MAX; i++)
		{
			pval=i;
			final int currentValue = i;
			/*try
			{*/
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						pb.setValue(pval);
					}
				});
				//java.lang.Thread.sleep(100);
			//}/* catch (InterruptedException e)
			/*{
				JOptionPane.showMessageDialog(frame, e.getMessage());
			}*/
			
			for(int j=0; j<5000; j++)
				System.out.println("j="+j);
			System.out.println("ready");
		}

	}
}