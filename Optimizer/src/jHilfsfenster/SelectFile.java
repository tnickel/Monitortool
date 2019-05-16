package jHilfsfenster;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.jdesktop.application.SingleFrameApplication;

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
/**
 * 
 */
public class SelectFile extends SingleFrameApplication
{
	private JPanel topPanel;
	private JFileChooser jFileChooser1;

	@Override
	protected void startup()
	{
		topPanel = new JPanel();
		FlowLayout topPanelLayout = new FlowLayout();
		topPanel.setPreferredSize(new java.awt.Dimension(1221, 574));
		topPanel.setLayout(topPanelLayout);
		{
			jFileChooser1 = new JFileChooser();
			topPanel.add(jFileChooser1);
			jFileChooser1.setPreferredSize(new java.awt.Dimension(1172, 516));
			jFileChooser1.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					jFileChooser1ActionPerformed(evt);
				}
			});

		}
		show(topPanel);
	}

	public static String main(String[] args)
	{
		launch(SelectFile.class, args);
		return ("hallo");
	}

	private void jFileChooser1ActionPerformed(ActionEvent evt)
	{
		System.out.println("jFileChooser1.actionPerformed, event=" + evt);
		// TODO add your code for jFileChooser1.actionPerformed
		String cmd = evt.getActionCommand();
		System.out.println(cmd);

		File file = jFileChooser1.getSelectedFile();
		System.out.println(file.getName());
		
	}
}
