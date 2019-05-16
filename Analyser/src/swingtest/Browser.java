package swingtest;

import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JTextField;

public class Browser
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

		JEditorPane htmlPane = null;
		JTextField urlField = null;
		String url = null;

		try
		{
			htmlPane.setPage(new URL(url));
			urlField.setText(url);
		} catch (IOException ioe)
		{
		}
	}

}
