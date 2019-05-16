package infobox;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class ShowMessage
{
	private static JList<String> jlist_glob = null;
	private static DefaultListModel<String> listenModell_glob = new DefaultListModel<String>();

	public ShowMessage(JList<String> jList1)
	{

		jlist_glob = jList1;
		jList1.setModel(listenModell_glob);

	}

	public static void addMessage(String message)
	{
		// jlist_glob.ad
		listenModell_glob.addElement(message);
		System.out.println(message);
		jlist_glob.ensureIndexIsVisible(listenModell_glob.size() - 1);
	}
}
