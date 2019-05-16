package swingdemo;

import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class GridLayoutDemo
{
	public final static boolean RIGHT_TO_LEFT = false;

	public static void addComponentsToPane(Container contentPane)
	{
		if (RIGHT_TO_LEFT)
		{
			contentPane
					.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}
		// Any number of rows and 2 columns
		contentPane.setLayout(new GridLayout(0, 2));

		contentPane.add(new JLabel("JLabel 1"));
		contentPane.add(new JButton("JButton 2"));
		contentPane.add(new JCheckBox("JCheckBox 3"));
		contentPane.add(new JTextField("Long-Named JTextField 4"));
		contentPane.add(new JButton("JButton 5"));
	}

	private static void createAndShowGUI()
	{
		JFrame.setDefaultLookAndFeelDecorated(true);

		JFrame frame = new JFrame("GridLayout Source Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set up the content pane and components in GridLayout
		addComponentsToPane(frame.getContentPane());

		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				createAndShowGUI();
			}
		});
	}
}