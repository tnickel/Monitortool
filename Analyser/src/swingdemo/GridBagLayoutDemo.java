package swingdemo;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class GridBagLayoutDemo
{

	public static void addComponentsToPane(Container pane)
	{

		JButton jbnButton;
		pane.setLayout(new GridBagLayout());
		GridBagConstraints gBC = new GridBagConstraints();
		gBC.fill = GridBagConstraints.HORIZONTAL;

		jbnButton = new JButton("Button 1");
		gBC.weightx = 0.5;
		gBC.gridx = 0;
		gBC.gridy = 0;
		pane.add(jbnButton, gBC);

		JTextField jtf = new JTextField("TextField 1");
		gBC.gridx = 2;
		gBC.gridy = 0;
		jtf.setEditable(false);
		pane.add(jtf, gBC);

		jbnButton = new JButton("Button 3");
		gBC.gridx = 2;
		gBC.gridy = 0;
		pane.add(jbnButton, gBC);

		jbnButton = new JButton("Button 4");
		gBC.ipady = 40; // This component has more breadth compared to other
						// buttons
		gBC.weightx = 0.0;
		gBC.gridwidth = 3;
		gBC.gridx = 0;
		gBC.gridy = 1;
		pane.add(jbnButton, gBC);

		JComboBox jcmbSample = new JComboBox(new String[]
		{ "ComboBox 1", "hi", "hello" });
		gBC.ipady = 0;
		gBC.weighty = 1.0;
		gBC.anchor = GridBagConstraints.PAGE_END;
		gBC.insets = new Insets(10, 0, 0, 0); // Padding
		gBC.gridx = 1;
		gBC.gridwidth = 2;
		gBC.gridy = 2;
		pane.add(jcmbSample, gBC);
	}

	private static void createAndShowGUI()
	{

		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("GridBagLayout Source Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set up the content pane.
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
