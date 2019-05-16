package swingOberfläche;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

public class OberflaecheStart extends JPanel
{
	protected JTextArea textArea;

	public OberflaecheStart()
	{
		ImageIcon icon = new ImageIcon("java-swing-tutorial.JPG");
		JTabbedPane jtbExample = new JTabbedPane();

		JPanel jplInnerPanel1 = Tab1x.createTextFeldUser();
		jtbExample.addTab("User.db", icon, jplInnerPanel1, "Tab 1");
		jtbExample.setSelectedIndex(0);

		JPanel jplInnerPanel2 = Tab2x.createTextFeldThreads();
		jtbExample.addTab("Threads.db", icon, jplInnerPanel2);

		JPanel jplInnerPanel3 = Tab3x.createDemoPanel();
		jtbExample.addTab("Akt.db", icon, jplInnerPanel3, "Tab 3");

		JPanel jplInnerPanel4 = Tab4x
				.createInnerPanel("Tab 4 Contains Text only");
		jtbExample.addTab("Consolewindow", jplInnerPanel4);

		JPanel jplInnerPanel5 = Tab5x.createTraceFeld();
		jtbExample.addTab("Logfile", jplInnerPanel5);

		JPanel jplInnerPanel6 = Tab6Vertrauen.createThreadUebersicht();
		jtbExample.addTab("Vertrauensfaktoren", jplInnerPanel6);
		
		JPanel jplInnerPanel8 = Tab8Stabil.createThreadUebersicht();
		jtbExample.addTab("Gewinnstabilitäten", jplInnerPanel8);
		
	
		setLayout(new GridLayout(1, 1));
		add(jtbExample);
	}

	public static void mainstart()
	{
		JFrame frame = new JFrame("TabbedPane Source Demo");

		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});

		frame.getContentPane().add(new OberflaecheStart(), BorderLayout.CENTER);
		frame.setSize(800, 600);
		frame.setVisible(true);
	}
}