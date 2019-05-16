package charttool;


	/**
	 * @param args
	 */
	import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
	 
	public class jscrollpanebeispiel
	{
	    // main-Methode
	    public static void main(String[] args)
	    {
	        // Erzeugung eines neuen Dialoges
	        JDialog meinJDialog = new JDialog();
	        meinJDialog.setTitle("JScrollPane Beispiel");
	        meinJDialog.setSize(150,10);
	        // JPanel wird erzeugt
	        JPanel panel = new JPanel();
	        // Unser JPanel erh�lt einen langen Schriftzug
	        panel.add(new JLabel("Dies ist ein viel zu langer Text f�r dieses " +
	                "kleine Fenster, so dass man horizontal " +
	                "scrollen muss, um ihn komplett zu lesen."));
	      
	        // JScrollPane wird erzeugt; dabei wird �ber den 
	        // Konstruktor direkt unser JPanel hinzugef�gt
	        JScrollPane scrollPane = new JScrollPane (panel, 
	            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
	            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	        // JScrollPane wird dem Dialog hinzugef�gt
	        meinJDialog.add(scrollPane);
	        // Wir lassen unseren Dialog anzeigen
	        meinJDialog.setVisible(true);
	 
	    }
	}


