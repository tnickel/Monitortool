package swingdemo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Swlearning
{
	public void showframe()
	{
		JFrame frame = new JFrame("JFrame Source Demo");
		// Add a window listner for close button
		frame.addWindowListener(new WindowAdapter()
		{

			@Override
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		// This is an empty content area in the frame
		JLabel jlbempty = new JLabel("");
		jlbempty.setPreferredSize(new Dimension(375, 300));
		frame.getContentPane().add(jlbempty, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
}
