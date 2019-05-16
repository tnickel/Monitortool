package swingOberfläche;

import hilfsklasse.KeyReader;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import mainPackage.GC;

public class Tab7x
{
	static protected JTextArea textArea;
	public Tab7x()
	{}
	
	static public JPanel createTextFeldUser()
	{
		String line;
		int i = 0;
		textArea = new JTextArea(5, 60);
		KeyReader kr = new KeyReader();
		kr.SetReader(GC.rootpath + "\\db\\userstore.db");
		while ((line = kr.GetLine()) != null)
		{
			i++;
			if (i > 100000)
				break;
			// System.out.println("add line<"+line+">");
			textArea.append(line);
			textArea.append("\n");
		}
		JScrollPane jsPane = new JScrollPane(textArea);
		// Lay out the content pane.
		JPanel jplContentPane = new JPanel();
		jplContentPane.setLayout(new BorderLayout());
		jplContentPane.setPreferredSize(new Dimension(200, 100));
		jplContentPane.add(jsPane, BorderLayout.WEST);
		return (jplContentPane);
	}
}
