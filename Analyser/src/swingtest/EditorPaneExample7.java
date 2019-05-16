package swingtest;

/*
 Core SWING Advanced Programming 
 By Kim Topley
 ISBN: 0 13 083292 8       
 Publisher: Prentice Hall  
 */

import java.awt.Color;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

public class EditorPaneExample7 extends JFrame
{
	JList list;
	String[] listColorNames =
	{ "black", "blue", "green", "yellow", "white" };
	Color[] listColorValues =
	{ Color.BLACK, Color.BLUE, Color.GREEN, Color.YELLOW, Color.WHITE };
	Container contentpane;

	public JList JListDemo()
	{
		list = new JList(listColorNames);
		list.setSelectedIndex(0);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		return list;
	}

	public EditorPaneExample7()
	{
		super("JEditorPane Example 7");

		pane = new JEditorPane();
		pane.setEditable(false); // Start read-only
		getContentPane().add(new JScrollPane(pane), "Center");
		list = JListDemo();
		getContentPane().add(list, "East");

	}

	public static void main(String[] args)
	{
		try
		{
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception evt)
		{
		}

		JFrame f = new EditorPaneExample7();
		f.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent evt)
			{
				System.exit(0);
			}
		});
		f.setSize(800, 600);
		f.setVisible(true);
	}

	private JEditorPane pane;

}