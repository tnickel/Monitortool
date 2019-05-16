package swingOberfläche;

import hilfsklasse.Prop2;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextArea;

import mainPackage.GC;
import stores.UserSummenGewinneDBI;

public class MA8Stabil extends MouseAdapter implements MouseListenerInterface
{
	
	
	private Prop2 prop2 = new Prop2(GC.rootpath
			+ "\\db\\UserThreadVirtualKonto\\config_usergewinne.prop");
	private int gewintervall = Integer.parseInt(Prop2
			.getprop("usergewinnintervall"));
	
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		//Hier wird der Mausklick (Aktienauswahl) ausgewertet
		
		
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
			
			JList jl=((JList) e.getSource());
			String selstring=(String)jl.getSelectedValue();
			selstring = selstring.substring(0,selstring.indexOf(".db"));
			
			UserSummenGewinneDBI userSummenGewinne = new UserSummenGewinneDBI("UserThreadVirtualKonto\\Bank\\Gewinnverlaufdb",selstring, gewintervall, 0);
			
			JFrame f = new JFrame("Gewinnverlauf");
			f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		

			f.getContentPane().add(Action8Stabil.createDemoPanel(userSummenGewinne,txtfeld),
					BorderLayout.CENTER);
			//f.getContentPane().add(eastComponent, BorderLayout.EAST);
			f.getContentPane().add(txtfeld,BorderLayout.EAST);
			f.setSize(600, 400);
			f.setVisible(true);
		}
	}
}
