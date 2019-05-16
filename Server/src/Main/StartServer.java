package Main;

import hilfsklasse.Tracer;
import infobox.ShowMessage;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.SwingUtilities;

import org.jdesktop.application.Application;

import soc.ServerExample;
import data.Rootpath;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class StartServer extends javax.swing.JFrame
{

	private JMenuItem helpMenuItem;
	private JMenu jMenu5;
	private JInternalFrame jInternalFrame5;
	private JButton jButton1stop;
	private JLabel jLabel1serverinfo;
	private JInternalFrame jInternalFrame4;
	private JInternalFrame jInternalFrame3;
	private JTree jTree1;
	private JButton jButton1start;
	private JInternalFrame jInternalFrame2;
	private JInternalFrame jInternalFrame1;
	private JMenuItem deleteMenuItem;
	private JSeparator jSeparator1;
	private JMenuItem pasteMenuItem;
	private JMenuItem copyMenuItem;
	private JMenuItem cutMenuItem;
	private JMenu jMenu4;
	private JMenuItem exitMenuItem;
	private JSeparator jSeparator2;
	private JMenuItem closeFileMenuItem;
	private JMenuItem saveAsMenuItem;
	private JMenuItem saveMenuItem;
	private JList jList1;
	private JScrollPane jScrollPane1;
	private JMenuItem openFileMenuItem;
	private JMenuItem newFileMenuItem;
	private JMenu jMenu3;
	private JMenuBar jMenuBar1;

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args)
	{
		String userdir = System.getProperty("user.dir");
		String updatemessage = "";
		System.out.println("akueller pfad=" + userdir);

		if (args.length > 0)
			if (args[0] != null)
				userdir = args[0];
		if (userdir.contains("bin") == false)
		{
			Tracer.WriteTrace(10, "Falscher Verzeichnissaufbau userdir<"
					+ userdir + ">");
			System.exit(0);
		}

		userdir = userdir.substring(0, userdir.lastIndexOf("\\"));
		Rootpath glob = new Rootpath(userdir, userdir + "\\conf");

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				StartServer inst = new StartServer();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public StartServer()
	{
		super();
		initGUI();
	}

	private void initGUI()
	{
		try
		{
			{

				getContentPane().setLayout(null);
				{
					jInternalFrame1 = new JInternalFrame();
					getContentPane().add(
							jInternalFrame1,
							new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER,
									GridBagConstraints.BOTH, new Insets(0, 0,
											0, 0), 0, 0));
					jInternalFrame1.setVisible(true);
					jInternalFrame1.setBounds(1, 41, 444, 284);
					jInternalFrame1.setMaximizable(true);
					jInternalFrame1.setResizable(true);
					{
						jTree1 = new JTree();
						jInternalFrame1.getContentPane().add(jTree1,
								BorderLayout.CENTER);
						jTree1.setPreferredSize(new java.awt.Dimension(442, 458));
					}
				}
				{
					jInternalFrame2 = new JInternalFrame();
					GridBagLayout jInternalFrame2Layout = new GridBagLayout();
					getContentPane().add(jInternalFrame2);
					jInternalFrame2.setBounds(462, 501, 313, 115);
					jInternalFrame2.setVisible(true);
					jInternalFrame2Layout.rowWeights = new double[]
					{ 0.1, 0.1, 0.1, 0.1 };
					jInternalFrame2Layout.rowHeights = new int[]
					{ 7, 7, 7, 7 };
					jInternalFrame2Layout.columnWeights = new double[]
					{ 0.1, 0.1, 0.1, 0.1 };
					jInternalFrame2Layout.columnWidths = new int[]
					{ 7, 7, 7, 7 };
					jInternalFrame2.getContentPane().setLayout(
							jInternalFrame2Layout);
					{
						jButton1start = new JButton();
						jInternalFrame2.getContentPane().add(
								jButton1start,
								new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
										GridBagConstraints.CENTER,
										GridBagConstraints.NONE, new Insets(0,
												0, 0, 0), 0, 0));
						jButton1start.setText("start");
						jButton1start.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent evt)
							{
								jButton1startActionPerformed(evt);
							}
						});
					}
					{
						jButton1stop = new JButton();
						jInternalFrame2.getContentPane().add(
								jButton1stop,
								new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
										GridBagConstraints.CENTER,
										GridBagConstraints.NONE, new Insets(0,
												0, 0, 0), 0, 0));
						jButton1stop.setText("stop");
						jButton1stop.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent evt)
							{
								jButton1stopActionPerformed(evt);
							}
						});
					}
				}
				{
					jInternalFrame3 = new JInternalFrame();
					getContentPane().add(jInternalFrame3);
					jInternalFrame3.setBounds(462, 98, 556, 341);
					jInternalFrame3.setVisible(true);
				}
				{
					jInternalFrame4 = new JInternalFrame();
					GridBagLayout jInternalFrame4Layout = new GridBagLayout();
					getContentPane().add(jInternalFrame4);
					jInternalFrame4.setBounds(559, 0, 554, 71);
					jInternalFrame4.setVisible(true);
					jInternalFrame4Layout.rowWeights = new double[]
					{ 0.1, 0.1 };
					jInternalFrame4Layout.rowHeights = new int[]
					{ 7, 7 };
					jInternalFrame4Layout.columnWeights = new double[]
					{ 0.1, 0.1, 0.1, 0.1 };
					jInternalFrame4Layout.columnWidths = new int[]
					{ 7, 7, 7, 7 };
					jInternalFrame4.getContentPane().setLayout(
							jInternalFrame4Layout);
					{
						jLabel1serverinfo = new JLabel();
						jInternalFrame4.getContentPane().add(
								jLabel1serverinfo,
								new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
										GridBagConstraints.CENTER,
										GridBagConstraints.BOTH, new Insets(0,
												0, 0, 0), 0, 0));
					}
				}
				{
					jInternalFrame5 = new JInternalFrame();
					getContentPane().add(jInternalFrame5);
					jInternalFrame5.setBounds(1044, 129, 247, 310);
					jInternalFrame5.setName("jInternalFrame5");
					{
						jScrollPane1 = new JScrollPane();
						jInternalFrame5.getContentPane().add(jScrollPane1,
								BorderLayout.CENTER);
						{

							jList1 = new JList();
							jScrollPane1.setViewportView(jList1);

						}
					}
				}
			}
			this.setSize(1327, 709);
			{
				jMenuBar1 = new JMenuBar();
				setJMenuBar(jMenuBar1);
				{
					jMenu3 = new JMenu();
					jMenuBar1.add(jMenu3);
					jMenu3.setText("File");
					{
						newFileMenuItem = new JMenuItem();
						jMenu3.add(newFileMenuItem);
						newFileMenuItem.setText("New");
					}
					{
						openFileMenuItem = new JMenuItem();
						jMenu3.add(openFileMenuItem);
						openFileMenuItem.setText("Open");
					}
					{
						saveMenuItem = new JMenuItem();
						jMenu3.add(saveMenuItem);
						saveMenuItem.setText("Save");
					}
					{
						saveAsMenuItem = new JMenuItem();
						jMenu3.add(saveAsMenuItem);
						saveAsMenuItem.setText("Save As ...");
					}
					{
						closeFileMenuItem = new JMenuItem();
						jMenu3.add(closeFileMenuItem);
						closeFileMenuItem.setText("Close");
					}
					{
						jSeparator2 = new JSeparator();
						jMenu3.add(jSeparator2);
					}
					{
						exitMenuItem = new JMenuItem();
						jMenu3.add(exitMenuItem);
						exitMenuItem.setText("Exit");
					}
				}
				{
					jMenu4 = new JMenu();
					jMenuBar1.add(jMenu4);
					jMenu4.setText("Edit");
					{
						cutMenuItem = new JMenuItem();
						jMenu4.add(cutMenuItem);
						cutMenuItem.setText("Cut");
					}
					{
						copyMenuItem = new JMenuItem();
						jMenu4.add(copyMenuItem);
						copyMenuItem.setText("Copy");
					}
					{
						pasteMenuItem = new JMenuItem();
						jMenu4.add(pasteMenuItem);
						pasteMenuItem.setText("Paste");
					}
					{
						jSeparator1 = new JSeparator();
						jMenu4.add(jSeparator1);
					}
					{
						deleteMenuItem = new JMenuItem();
						jMenu4.add(deleteMenuItem);
						deleteMenuItem.setText("Delete");
					}
				}
				{
					jMenu5 = new JMenu();
					jMenuBar1.add(jMenu5);
					jMenu5.setText("Help");
					{
						helpMenuItem = new JMenuItem();
						jMenu5.add(helpMenuItem);
						helpMenuItem.setText("Help");
					}
				}
			}
			Application.getInstance().getContext().getResourceMap(getClass())
					.injectComponents(getContentPane());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		init();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void init()
	{
		// init der jlist die die Meldungen ausgibt
		ShowMessage sm = new ShowMessage(jList1);
	}

	private void jButton1startActionPerformed(ActionEvent evt)
	{
		System.out.println("jButton1start.actionPerformed, event=" + evt);
		// Start Server
		try
		{
			ServerExample.main(null);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		jLabel1serverinfo.setText("server started");
		ShowMessage.addMessage("server started");
	}

	private void jButton1stopActionPerformed(ActionEvent evt)
	{
		System.out.println("jButton1stop.actionPerformed, event=" + evt);
		// stop Server
		jLabel1serverinfo.setText("server stopped");
		ShowMessage.addMessage("server stopped");
		ServerExample.stopServer();

	}

}
