package swingOberfläche;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextArea;

import objects.ThreadDbObj;
import stores.ThreadsDB;

public class MA1 extends MouseAdapter implements MouseListenerInterface
{
	public MA1()
	{
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		int tid = 0;
		String tname = "??";

		//button
		//JButton eastComponent = new JButton();
		//eastComponent.setText("Button");

		//Meldungsfeld
		JTextArea txtfeld = new JTextArea(20,30);
		txtfeld.setFont(Font.decode("Courier bold 12")); // select a non-proportional font
		txtfeld.setLineWrap(true); // do not care about CRs
	
		
		
		int index = 0;
		if (e.getClickCount() == 2)
		{
			index = ((JList) e.getSource()).locationToIndex(e.getPoint());
			System.out.println("Index " + index + " wurde ausgewählt");

			JFrame f = new JFrame("Das Fenster zur Welt");
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			// das symbol bestimmen
			ThreadsDB tdb = new ThreadsDB();
			int anz = tdb.GetanzObj();
			ThreadDbObj tdbo = (ThreadDbObj) tdb.GetObjectIDX(index);

			if (tdbo != null)
			{
				tid = tdbo.getThreadid();
				tname = tdbo.getThreadname();
			}

			f.getContentPane().add(JP7Vertrauen.createDemoPanel(tdb, tdbo,txtfeld),
					BorderLayout.CENTER);
			//f.getContentPane().add(eastComponent, BorderLayout.EAST);
			f.getContentPane().add(txtfeld,BorderLayout.EAST);
			
			
			
			
			f.setSize(600, 400);
			f.setVisible(true);

		}
	}

}
