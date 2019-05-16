package jhilf;


import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.border.Border;

public class ProgressSample
{
	public static void main(String args[])
	{
		JFrame f = new JFrame("JProgressBar Sample");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container content = f.getContentPane();
		JProgressBar progressBar = new JProgressBar();
		progressBar.setValue(25);
		progressBar.setStringPainted(true);
		Border border = BorderFactory.createTitledBorder("Reading...");
		progressBar.setBorder(border);
		content.add(progressBar, BorderLayout.NORTH);
		f.setSize(300, 100);
		f.setVisible(true);
		 progressBar.paint(progressBar.getGraphics());	
	}
}
