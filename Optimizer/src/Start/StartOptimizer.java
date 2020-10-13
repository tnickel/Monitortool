package Start;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListModel;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.ws.Action;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import charttool.StaticMultiProfitPanel;
import data.Algoliste;
import data.Config;
import data.OptimizeResultliste;
import data.Timefilter;
import data.Tradeliste;
import gui.Mbox;
import hiflsklasse.Tracer;
import jHilfsfenster.JShowIndikator;
import jHilfsfenster.SelectFile2Strategy;
import work1.FrameWorker;
import work1.GuiWorker;

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
public class StartOptimizer extends SingleFrameApplication
{
	private JMenuBar menuBar;
	private JToolBar jToolBar1;
	private JPanel jPanelOneStrategy;
	private JTabbedPane jTabbedPane1;
	private JPanel topPanel;
	private JMenuItem jMenuItem7;
	private JMenuItem jMenuItem6;
	private JMenuItem jMenuItem5;
	private JMenuItem jMenuItem4;
	private JMenu editMenu;
	private JMenuItem jMenuItem3;
	private JMenuItem jMenuItem2;
	private JMenuItem jMenuItem1;
	private JMenu fileMenu;
	private JSeparator jSeparator;
	private JButton saveButton;
	private JButton openButton;
	private JButton jButton4;
	private JList jAlgoliste;
	private JScrollPane jScrollPane2;
	private JInternalFrame jInternalFrameAlgos;
	private JInternalFrame jInternalFrame8;
	private JInternalFrame jInternalFrame7;
	private JInternalFrame jInternalFrame6;
	private JCheckBox jCheckBox23;
	private JCheckBox jCheckBox22;
	private JCheckBox jCheckBox21;
	private JCheckBox jCheckBox20;
	private JCheckBox jCheckBox19;
	private JCheckBox jCheckBox18;
	private JCheckBox jCheckBox17;
	private JCheckBox jCheckBox16;
	private JCheckBox jCheckBox15;
	private JCheckBox jCheckBox14;
	private JCheckBox jCheckBox13;
	private JCheckBox jCheckBox12;
	private JCheckBox jCheckBox11;
	private JCheckBox jCheckBox10;
	private JCheckBox jCheckBox9;
	private JCheckBox jCheckBox8;
	private JCheckBox jCheckBox7;
	private JCheckBox jCheckBox6;
	private JCheckBox jCheckBox5;
	private JCheckBox jCheckBox4;
	private JCheckBox jCheckBox3;
	private JCheckBox jCheckBox2;
	private JCheckBox jCheckBox1;
	private JCheckBox jCheckBox0;
	private JCheckBox jCheckBoxSu;
	private JCheckBox jCheckBoxSa;
	private JCheckBox jCheckBoxFr;
	private JCheckBox jCheckBoxThu;
	private JInternalFrame jInternalFrame5;
	private JCheckBox jCheckBoxWe;
	private JCheckBox jCheckBoxTue;
	private JCheckBox jCheckBoxMo;
	private JTabbedPane OptimizeStep4;
	private JTabbedPane OptimizeStep3;
	private JTabbedPane OptimizeStep2;
	private JTabbedPane OptimizeStep1;
	private JButton jButton1optimizeall;
	private JButton jButton1saveEa;
	private JTextArea jTextArea2;
	private JTextArea jTextArea1;
	private JLabel jLabel18;
	private JLabel jLabel17;
	private JInternalFrame jInternalFrame10;
	private JButton jButton1showindikator;

	private JList jList1;
	private JScrollPane jScrollPane6;
	private JInternalFrame jInternalFrameMessage;
	private JTabbedPane jTabbedPane2;
	private JInternalFrame jInternalFrame9;
	private JLabel jLabel16;
	private JLabel jLabel15;
	private JPanel jPanel11;
	private JLabel jLabel14;
	private JLabel jLabel13;
	private JPanel jPanel10;
	private JLabel jLabel12;
	private JLabel jLabel10;
	private JPanel jPanel9;
	private JLabel jLabel8;
	private JLabel jLabel6;
	private JPanel jPanel8;
	private JLabel jLabel11;
	private JLabel jLabel4;
	private JPanel jPanel7;
	private JPanel jPanel6;
	
	private JButton jButtonF1;
	private JScrollPane jScrollPane5;
	private JLabel jLabel9;
	private JButton jButton10;
	private JButton jButtonF4;
	private JPanel jPanel4;
	private JInternalFrame jInternalFrame3;
	private JScrollPane jScrollPane4;
	private JLabel jLabel7;
	private JButton jButton7;
	private JButton jButtonF3;
	private JPanel jPanel3;
	private JInternalFrame jInternalFrame2;
	private JButton jButton6clear;
	private JPanel jPanel1;
	private JScrollPane jScrollPane1;
	private JPanel jPanel5;

	private JLabel jLabel3;
	private JTextField jTextField2currency;
	private JButton showTradelistbutton;

	private JTable jTableStep2;
	private JButton jButton1comparetradelists;

	private JCheckBox jCheckBox1time;
	private JCheckBox jCheckBox1weekday;
	private JInternalFrame jInternalFrame4;
	private JLabel jLabel2;
	private JTextField jTextField2indiMinMaxDate;
	private JTextField jTextField2stratMinMaxDate;
	private JLabel jLabel1;
	private JButton jButton1clear;
	private JMenuItem jMenuItem8config;
	private JButton jButtonW1SelStrategy;

	private JTextField jTextField1;
	private JInternalFrame jInternalFrameOrgStrat;
	private JInternalFrame jInternalFrameResults1;
	private JInternalFrame jInternalFrameResultStrat;
	private JButton newButton;
	private JToolBar toolBar;
	private JPanel toolBarPanel;
	private SelectFile2Strategy OrgStratFile = new SelectFile2Strategy();
	private Timefilter timefilter = new Timefilter();
	private GuiWorker guiworker1_glob = new GuiWorker();
	private GuiWorker guiworker2_glob = new GuiWorker();
	private GuiWorker guiworker3_glob = new GuiWorker();
	private GuiWorker guiworker4_glob = new GuiWorker();
	private FrameWorker frameworker1_glob = null;
	private FrameWorker frameworker2_glob = null;
	private FrameWorker frameworker3_glob = null;
	private FrameWorker frameworker4_glob = null;
	private int activatedResultFrameNr_glob = 0;
	private JScrollPane jScrollPane3;
	private JLabel jLabel5;
	private JButton jButtonF2;
	private JPanel jPanel2;
	private JInternalFrame jInternalFrame1;
	private String quellstrategy_glob=null;
	  private Timer timer;
	  private int pbcount=0;
	@Action
	public void open()
	{
	}

	@Action
	public void save()
	{
	}

	@Action
	public void newFile()
	{
	}

	private ActionMap getAppActionMap()
	{
		return Application.getInstance().getContext().getActionMap(this);
	}

	@Override
	protected void startup()
	{
		{
			getMainFrame().setSize(1560, 933);
		}
		{
			topPanel = new JPanel();
			BorderLayout panelLayout = new BorderLayout();
			topPanel.setLayout(panelLayout);
			topPanel.setPreferredSize(new java.awt.Dimension(500, 300));
			{
				toolBarPanel = new JPanel();
				topPanel.add(toolBarPanel, BorderLayout.NORTH);
				BorderLayout jPanel1Layout = new BorderLayout();
				toolBarPanel.setLayout(jPanel1Layout);
				{
					toolBar = new JToolBar();
					toolBarPanel.add(toolBar, BorderLayout.CENTER);
					toolBar.setPreferredSize(new java.awt.Dimension(1397, 25));
					{
						newButton = new JButton();
						toolBar.add(newButton);
						newButton.setAction(getAppActionMap().get("newFile"));
						newButton.setName("newButton");
						newButton.setFocusable(false);
					}
					{
						openButton = new JButton();
						toolBar.add(openButton);
						openButton.setAction(getAppActionMap().get("open"));
						openButton.setName("openButton");
						openButton.setFocusable(false);
					}
					{
						saveButton = new JButton();
						toolBar.add(saveButton);
						saveButton.setAction(getAppActionMap().get("save"));
						saveButton.setName("saveButton");
						saveButton.setFocusable(false);
					}
				}
				{
					jSeparator = new JSeparator();
					toolBarPanel.add(jSeparator, BorderLayout.SOUTH);
				}
			}
			{
				jTabbedPane1 = new JTabbedPane();
				topPanel.add(jTabbedPane1, BorderLayout.CENTER);
				jTabbedPane1
						.setPreferredSize(new java.awt.Dimension(1554, 773));
				{
					jPanelOneStrategy = new JPanel();
					jTabbedPane1.addTab("1OneStrategy", null,
							jPanelOneStrategy, null);
					jPanelOneStrategy.setPreferredSize(new java.awt.Dimension(1547, 850));
					jPanelOneStrategy.setLayout(null);
					jPanelOneStrategy.setName("jPanelOneStrategy");
					jPanelOneStrategy.setBorder(BorderFactory
							.createBevelBorder(BevelBorder.LOWERED));
					jPanelOneStrategy.setSize(1524, 713);

					{
						jInternalFrameResultStrat = new JInternalFrame();
						jPanelOneStrategy
								.add(jInternalFrameResultStrat, "1, 0");
						jInternalFrameResultStrat
								.setName("jInternalFrameResultStrat");
						jInternalFrameResultStrat.setBounds(383, 2, 381, 293);

					}
					{
						jInternalFrame4 = new JInternalFrame();
						jPanelOneStrategy.add(jInternalFrame4, "3, 0");
						jInternalFrame4.setName("jInternalFrame4");
						jInternalFrame4.setBounds(979, 7, 377, 282);
						jInternalFrame4.getContentPane().add(getJPanel6(),
								BorderLayout.SOUTH);
						{
							jInternalFrame5 = new JInternalFrame();
							jPanelOneStrategy.add(getJInternalFrame8(), "3, 1");
							jPanelOneStrategy.add(getJInternalFrame7(), "0, 2");
							{
								jInternalFrameOrgStrat = new JInternalFrame();
								jPanelOneStrategy.add(jInternalFrameOrgStrat);
								jPanelOneStrategy.add(getJPanel8(), "North");
								jPanelOneStrategy.add(getJInternalFrame9x());
								jPanelOneStrategy.add(getJInternalFrame10());
								jPanelOneStrategy.add(jInternalFrame5);
								{
									showTradelistbutton = new JButton();
									jInternalFrame7.getContentPane().add(getJButton1showindikator(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
									jInternalFrame7.getContentPane().add(showTradelistbutton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
									jInternalFrame7.getContentPane().add(getJButton1saveEa(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
									showTradelistbutton.setBounds(1189, 640, 135, 23);
									showTradelistbutton.setName("showTradelistbutton");
									showTradelistbutton.addActionListener(new ActionListener()
									{
										public void actionPerformed(ActionEvent evt)
										{
											showTradelistbuttonActionPerformed(evt);
										}
									});
								}
								
								jInternalFrameOrgStrat
								.setName("jInternalFrameOrgStrat");
								jInternalFrameOrgStrat
								.setBounds(2, 1, 381, 316);
								
							}
							jInternalFrame5.setName("jInternalFrame5");
							jInternalFrame5.setBounds(1142, 283, 361, 240);
							{
								jPanel5 = new JPanel();
								jInternalFrame5.getContentPane().add(jPanel5, BorderLayout.NORTH);
								GridBagLayout jPanel5Layout = new GridBagLayout();
								jPanel5.setBounds(1140, 483, 218, 167);
								jPanel5Layout.rowWeights = new double[]
										{ 0.1, 0.1, 0.1, 0.0, 0.1, 0.1, 0.0, 0.1 };
								jPanel5Layout.rowHeights = new int[]
										{ 7, 7, 7, 19, 20, 20, 20, 20 };
								jPanel5Layout.columnWeights = new double[]
										{ 0.0, 0.0, 0.0, 0.0, 0.1 };
								jPanel5Layout.columnWidths = new int[]
										{ 38, 50, 44, 37, 20 };
								jPanel5.setLayout(jPanel5Layout);
								jPanel5.setPreferredSize(new java.awt.Dimension(
										243, 163));
								
								{
									jCheckBoxMo = new JCheckBox();
									jPanel5.add(jCheckBoxMo,
											new GridBagConstraints(0, 0, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBoxMo.setName("jCheckBoxMo");
									jCheckBoxMo.setBounds(856, 604, 47, 25);
									jCheckBoxMo
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBoxMoActionPerformed(evt);
										}
									});
								}
								{
									jCheckBoxTue = new JCheckBox();
									jPanel5.add(jCheckBoxTue,
											new GridBagConstraints(0, 1, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBoxTue.setName("jCheckBoxTue");
									jCheckBoxTue
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBoxTueActionPerformed(evt);
										}
									});
								}
								{
									jCheckBoxWe = new JCheckBox();
									jPanel5.add(jCheckBoxWe,
											new GridBagConstraints(0, 2, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBoxWe.setName("jCheckBoxWe");
									jCheckBoxWe
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBoxWeActionPerformed(evt);
										}
									});
								}
								{
									jCheckBoxThu = new JCheckBox();
									jPanel5.add(jCheckBoxThu,
											new GridBagConstraints(0, 3, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBoxThu.setName("jCheckBoxThu");
									jCheckBoxThu
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBoxThuActionPerformed(evt);
										}
									});
								}
								{
									jCheckBoxFr = new JCheckBox();
									jPanel5.add(jCheckBoxFr,
											new GridBagConstraints(0, 4, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBoxFr.setName("jCheckBoxFr");
									jCheckBoxFr
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBoxFrActionPerformed(evt);
										}
									});
								}
								{
									jCheckBoxSa = new JCheckBox();
									jPanel5.add(jCheckBoxSa,
											new GridBagConstraints(0, 5, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBoxSa.setName("jCheckBoxSa");
									jCheckBoxSa
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBoxSaActionPerformed(evt);
										}
									});
								}
								{
									jCheckBoxSu = new JCheckBox();
									jPanel5.add(jCheckBoxSu,
											new GridBagConstraints(0, 6, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBoxSu.setName("jCheckBoxSu");
									jCheckBoxSu
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBoxSuActionPerformed(evt);
										}
									});
								}
								{
									jCheckBox0 = new JCheckBox();
									jPanel5.add(jCheckBox0,
											new GridBagConstraints(1, 0, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBox0.setName("jCheckBox0");
									jCheckBox0
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBox0ActionPerformed(evt);
										}
									});
								}
								{
									jCheckBox1 = new JCheckBox();
									jPanel5.add(jCheckBox1,
											new GridBagConstraints(1, 1, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBox1.setName("jCheckBox1");
									jCheckBox1
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBox1ActionPerformed(evt);
										}
									});
								}
								{
									jCheckBox2 = new JCheckBox();
									jPanel5.add(jCheckBox2,
											new GridBagConstraints(1, 2, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBox2.setName("jCheckBox2");
									jCheckBox2
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBox2ActionPerformed(evt);
										}
									});
								}
								{
									jCheckBox3 = new JCheckBox();
									jPanel5.add(jCheckBox3,
											new GridBagConstraints(1, 3, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBox3.setName("jCheckBox3");
									jCheckBox3
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBox3ActionPerformed(evt);
										}
									});
								}
								{
									jCheckBox4 = new JCheckBox();
									jPanel5.add(jCheckBox4,
											new GridBagConstraints(1, 4, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBox4.setName("jCheckBox4");
									jCheckBox4
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBox4ActionPerformed(evt);
										}
									});
								}
								{
									jCheckBox5 = new JCheckBox();
									jPanel5.add(jCheckBox5,
											new GridBagConstraints(1, 5, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBox5.setName("jCheckBox5");
									jCheckBox5
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBox5ActionPerformed(evt);
										}
									});
								}
								{
									jCheckBox6 = new JCheckBox();
									jPanel5.add(jCheckBox6,
											new GridBagConstraints(1, 6, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBox6.setName("jCheckBox6");
									jCheckBox6
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBox6ActionPerformed(evt);
										}
									});
								}
								{
									jCheckBox7 = new JCheckBox();
									jPanel5.add(jCheckBox7,
											new GridBagConstraints(2, 0, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBox7.setName("jCheckBox7");
									jCheckBox7
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBox7ActionPerformed(evt);
										}
									});
								}
								{
									jCheckBox8 = new JCheckBox();
									jPanel5.add(jCheckBox8,
											new GridBagConstraints(2, 1, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBox8.setName("jCheckBox8");
									jCheckBox8
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBox8ActionPerformed(evt);
										}
									});
								}
								{
									jCheckBox9 = new JCheckBox();
									jPanel5.add(jCheckBox9,
											new GridBagConstraints(2, 2, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBox9.setName("jCheckBox9");
									jCheckBox9
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBox9ActionPerformed(evt);
										}
									});
								}
								{
									jCheckBox10 = new JCheckBox();
									jPanel5.add(jCheckBox10,
											new GridBagConstraints(2, 3, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBox10.setName("jCheckBox10");
									jCheckBox10
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBox10ActionPerformed(evt);
										}
									});
								}
								{
									jCheckBox11 = new JCheckBox();
									jPanel5.add(jCheckBox11,
											new GridBagConstraints(2, 4, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBox11.setName("jCheckBox11");
									jCheckBox11
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBox11ActionPerformed(evt);
										}
									});
								}
								{
									jCheckBox12 = new JCheckBox();
									jPanel5.add(jCheckBox12,
											new GridBagConstraints(2, 5, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBox12.setName("jCheckBox12");
									jCheckBox12
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBox12ActionPerformed(evt);
										}
									});
								}
								{
									jCheckBox13 = new JCheckBox();
									jPanel5.add(jCheckBox13,
											new GridBagConstraints(2, 6, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBox13.setName("jCheckBox13");
									jCheckBox13
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBox13ActionPerformed(evt);
										}
									});
								}
								{
									jCheckBox14 = new JCheckBox();
									jPanel5.add(jCheckBox14,
											new GridBagConstraints(3, 0, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBox14.setName("jCheckBox14");
									jCheckBox14
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBox14ActionPerformed(evt);
										}
									});
								}
								{
									jCheckBox15 = new JCheckBox();
									jPanel5.add(jCheckBox15,
											new GridBagConstraints(3, 1, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBox15.setName("jCheckBox15");
									jCheckBox15
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBox15ActionPerformed(evt);
										}
									});
								}
								{
									jCheckBox16 = new JCheckBox();
									jPanel5.add(jCheckBox16,
											new GridBagConstraints(3, 2, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBox16.setName("jCheckBox16");
									jCheckBox16
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBox16ActionPerformed(evt);
										}
									});
								}
								{
									jCheckBox17 = new JCheckBox();
									jPanel5.add(jCheckBox17,
											new GridBagConstraints(3, 3, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBox17.setName("jCheckBox17");
									jCheckBox17
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBox17ActionPerformed(evt);
										}
									});
								}
								{
									jCheckBox18 = new JCheckBox();
									jPanel5.add(jCheckBox18,
											new GridBagConstraints(3, 4, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBox18.setName("jCheckBox18");
									jCheckBox18
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBox18ActionPerformed(evt);
										}
									});
								}
								{
									jCheckBox19 = new JCheckBox();
									jPanel5.add(jCheckBox19,
											new GridBagConstraints(3, 5, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBox19.setName("jCheckBox19");
									jCheckBox19
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBox19ActionPerformed(evt);
										}
									});
								}
								{
									jCheckBox20 = new JCheckBox();
									jPanel5.add(jCheckBox20,
											new GridBagConstraints(3, 6, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBox20.setName("jCheckBox20");
									jCheckBox20
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBox20ActionPerformed(evt);
										}
									});
								}
								{
									jCheckBox21 = new JCheckBox();
									jPanel5.add(jCheckBox21,
											new GridBagConstraints(4, 0, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBox21.setName("jCheckBox21");
									jCheckBox21
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBox21ActionPerformed(evt);
										}
									});
								}
								{
									jCheckBox22 = new JCheckBox();
									jPanel5.add(jCheckBox22,
											new GridBagConstraints(4, 1, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBox22.setName("jCheckBox22");
									jCheckBox22
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBox22ActionPerformed(evt);
										}
									});
								}
								{
									jCheckBox23 = new JCheckBox();
									jPanel5.add(jCheckBox23,
											new GridBagConstraints(4, 2, 1, 1,
													0.0, 0.0,
													GridBagConstraints.CENTER,
													GridBagConstraints.NONE,
													new Insets(0, 0, 0, 0), 0,
													0));
									jCheckBox23.setName("jCheckBox23");
									jCheckBox23
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jCheckBox23ActionPerformed(evt);
										}
									});
								}
							}
						}
					}
					{
						jInternalFrame6 = new JInternalFrame();
						jPanelOneStrategy.add(jInternalFrame6, "0, 1");
						jPanelOneStrategy.add(getJInternalFrame9(), "1, 1");
						GridBagLayout jInternalFrame6Layout = new GridBagLayout();
						jInternalFrame6.setName("jInternalFrame6");
						jInternalFrame6Layout.rowWeights = new double[]
						{ 0.0, 0.1 };
						jInternalFrame6Layout.rowHeights = new int[]
						{ 20, 7 };
						jInternalFrame6Layout.columnWeights = new double[] {0.1, 0.0, 0.0, 0.0, 0.1};
						jInternalFrame6Layout.columnWidths = new int[] {7, 433, 169, 196, 20};
						jInternalFrame6.getContentPane().setLayout(jInternalFrame6Layout);
						jInternalFrame6.setBounds(3, 295, 993, 72);
						{
							jTextField2stratMinMaxDate = new JTextField();
							jInternalFrame6.getContentPane().add(
									jTextField2stratMinMaxDate,
									new GridBagConstraints(0, 0, 2, 1, 0.0,
											0.0, GridBagConstraints.CENTER,
											GridBagConstraints.BOTH,
											new Insets(0, 0, 0, 0), 0, 0));
							jTextField2stratMinMaxDate.setBounds(5, 6, 323, 22);
						}
						{
							jLabel1 = new JLabel();
							jInternalFrame6.getContentPane().add(
									jLabel1,
									new GridBagConstraints(2, 0, 1, 1, 0.0,
											0.0, GridBagConstraints.CENTER,
											GridBagConstraints.NONE,
											new Insets(0, 0, 0, 0), 0, 0));
							jLabel1.setBounds(333, 9, 134, 16);
							jLabel1.setName("jLabel1");
						}
						{
							jTextField2indiMinMaxDate = new JTextField();
							jInternalFrame6.getContentPane().add(
									jTextField2indiMinMaxDate,
									new GridBagConstraints(3, 0, 1, 1, 0.0,
											0.0, GridBagConstraints.CENTER,
											GridBagConstraints.BOTH,
											new Insets(0, 0, 0, 0), 0, 0));
							jTextField2indiMinMaxDate
									.setBounds(473, 9, 240, 22);
						}
						{
							jLabel2 = new JLabel();
							jInternalFrame6.getContentPane().add(
									jLabel2,
									new GridBagConstraints(4, 0, 1, 1, 0.0,
											0.0, GridBagConstraints.CENTER,
											GridBagConstraints.NONE,
											new Insets(0, 0, 0, 0), 0, 0));
							jLabel2.setBounds(724, 6, 104, 23);
							jLabel2.setName("jLabel2");
						}
						{
							jTextField1 = new JTextField();
							jInternalFrame6.getContentPane().add(
									jTextField1,
									new GridBagConstraints(0, 1, 2, 1, 0.0,
											0.0, GridBagConstraints.CENTER,
											GridBagConstraints.BOTH,
											new Insets(0, 0, 0, 0), 0, 0));
							jTextField1.setName("jTextField1");
							jTextField1.setBounds(0, 35, 455, 22);
						}
						{
							jButtonW1SelStrategy = new JButton();
							jInternalFrame6.getContentPane().add(
									jButtonW1SelStrategy,
									new GridBagConstraints(2, 1, 1, 1, 0.0,
											0.0, GridBagConstraints.CENTER,
											GridBagConstraints.NONE,
											new Insets(0, 0, 0, 0), 0, 0));
							jButtonW1SelStrategy
									.setName("jButtonW1SelStrategy");
							jButtonW1SelStrategy.setBounds(483, 34, 115, 25);
							jButtonW1SelStrategy
									.addActionListener(new ActionListener()
									{
										public void actionPerformed(
												ActionEvent evt)
										{
											jButtonW1SelStrategyActionPerformed(
													evt);
										}
									});
						}
						{
							jTextField2currency = new JTextField();
							jInternalFrame6.getContentPane().add(
									jTextField2currency,
									new GridBagConstraints(3, 1, 1, 1, 0.0,
											0.0, GridBagConstraints.CENTER,
											GridBagConstraints.BOTH,
											new Insets(0, 0, 0, 0), 0, 0));
							jTextField2currency.setBounds(610, 35, 102, 22);
							jTextField2currency.setName("jTextField2currency");
						}
						{
							jLabel3 = new JLabel();
							jInternalFrame6.getContentPane().add(
									jLabel3,
									new GridBagConstraints(4, 1, 1, 1, 0.0,
											0.0, GridBagConstraints.CENTER,
											GridBagConstraints.NONE,
											new Insets(0, 0, 0, 0), 0, 0));
							jLabel3.setBounds(724, 38, 49, 16);
							jLabel3.setName("jLabel3");
						}
					}
				}
				{
					jToolBar1 = new JToolBar();
					jTabbedPane1.addTab("jToolBar1", null, jToolBar1, null);

				}
			}
		}
		menuBar = new JMenuBar();
		{
			fileMenu = new JMenu();
			menuBar.add(fileMenu);
			fileMenu.setName("fileMenu");
			{
				jMenuItem1 = new JMenuItem();
				fileMenu.add(jMenuItem1);
				jMenuItem1.setAction(getAppActionMap().get("newFile"));
			}
			{
				jMenuItem2 = new JMenuItem();
				fileMenu.add(jMenuItem2);
				jMenuItem2.setAction(getAppActionMap().get("open"));
			}
			{
				jMenuItem3 = new JMenuItem();
				fileMenu.add(jMenuItem3);
				jMenuItem3.setAction(getAppActionMap().get("save"));
			}
			{
				jMenuItem8config = new JMenuItem();
				fileMenu.add(jMenuItem8config);
				jMenuItem8config.setName("jMenuItem8config");
				jMenuItem8config.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						jMenuItem8configActionPerformed(evt);
					}
				});
			}
		}
		{
			editMenu = new JMenu();
			menuBar.add(editMenu);
			editMenu.setName("editMenu");
			{
				jMenuItem4 = new JMenuItem();
				editMenu.add(jMenuItem4);
				jMenuItem4.setAction(getAppActionMap().get("copy"));
			}
			{
				jMenuItem5 = new JMenuItem();
				editMenu.add(jMenuItem5);
				jMenuItem5.setAction(getAppActionMap().get("cut"));
			}
			{
				jMenuItem6 = new JMenuItem();
				editMenu.add(jMenuItem6);
				jMenuItem6.setAction(getAppActionMap().get("paste"));
			}
			{
				jMenuItem7 = new JMenuItem();
				editMenu.add(jMenuItem7);
				jMenuItem7.setAction(getAppActionMap().get("delete"));
			}
		}
		initTimefilter();
		init();
		getMainFrame().setJMenuBar(menuBar);
		show(topPanel);
		

		Config.setJframeroot(getMainFrame());
		Config.repaint();
	}



	private void baueAlgoliste()
	{

	}

	private void init()
	{
		// den worker für das erste resultframe initialisieren
		frameworker1_glob = new FrameWorker(jInternalFrameResults1,
				jScrollPane1, jInternalFrameOrgStrat,
				jInternalFrameResultStrat, jInternalFrame4, guiworker1_glob,
				timefilter, jCheckBox1weekday, jCheckBox1time,
				jTextField2indiMinMaxDate, jLabel11, jLabel4, 0,jInternalFrame10,jLabel17,jTextArea1,jLabel18,jTextArea2);
		setTimefilter();

		frameworker2_glob = new FrameWorker(jInternalFrame1, jScrollPane3,
				jInternalFrameOrgStrat, jInternalFrameResultStrat,
				jInternalFrame4, guiworker2_glob, timefilter,
				jCheckBox1weekday, jCheckBox1time, jTextField2indiMinMaxDate,
				jLabel12, jLabel10, 1,jInternalFrame10,jLabel17,jTextArea1,jLabel18,jTextArea2);

		frameworker3_glob = new FrameWorker(jInternalFrame2, jScrollPane4,
				jInternalFrameOrgStrat, jInternalFrameResultStrat,
				jInternalFrame4, guiworker3_glob, timefilter,
				jCheckBox1weekday, jCheckBox1time, jTextField2indiMinMaxDate,
				jLabel14, jLabel13, 2,jInternalFrame10,jLabel17,jTextArea1,jLabel18,jTextArea2);

		frameworker4_glob = new FrameWorker(jInternalFrame2, jScrollPane5,
				jInternalFrameOrgStrat, jInternalFrameResultStrat,
				jInternalFrame4, guiworker4_glob, timefilter,
				jCheckBox1weekday, jCheckBox1time, jTextField2indiMinMaxDate,
				jLabel16, jLabel15, 3,jInternalFrame10,jLabel17,jTextArea1,jLabel18,jTextArea2);
		setTimefilter();

		// die frameworker connecten
		frameworker1_glob.setSlaveFramework(frameworker2_glob);
		frameworker2_glob.setSlaveFramework(frameworker3_glob);
		frameworker3_glob.setSlaveFramework(frameworker4_glob);
		frameworker4_glob.setSlaveFramework(null);
		
		
		//den timer kreieren
		
	}

	public static void main(String[] args)
	{
		String userdir = System.getProperty("user.dir");
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

		System.out.println("akueller pfad=" + userdir);
		Config conf = new Config(userdir);
		launch(StartOptimizer.class, args);
	}

	private void jButtonW1SelStrategyActionPerformed(ActionEvent evt
			)
	{
		// Hier wurde ne Strategie zum Laden ausgewählt
		clearAll();

		System.out
				.println("jButtonW1SelStrategy.actionPerformed, event=" + evt);

		
		String fnam = SelectFile2Strategy.mainx(null); 
		jTextField1.setText(fnam);
		//strategie in den guiworker einladen
		guiworker1_glob.LoadQuellStrategy(fnam);
		quellstrategy_glob=fnam; 

		/*guiworker1_glob
				.LoadQuellStrategy("c:\\tmp\\bug Strategy 0.67164.str");
*/
		//min-maxdatum beim guiworker setzen
		guiworker1_glob.setMinMaxDateQuellStrat(jTextField2stratMinMaxDate);

		JPanel jp = guiworker1_glob.GenQuellStratJPanel(0);
		jInternalFrameOrgStrat.getContentPane().add(jp, BorderLayout.CENTER);
		//userinterface updaten
		jInternalFrameOrgStrat.updateUI();
	}

	private void jButton1comparetradelistsActionPerformed(ActionEvent evt)
	{
		//CompareTradelist:
		//es wird die Quelltradeliste mit der optimierten Tradeliste verglichen
		// compareBUTTON wurde gedrückt.
		System.out.println("jButton1comparetradelists.actionPerformed, event="
				+ evt);

		// erst wird ermittelt welches frame denn aktiviert ist
		// und dann wird der passende gui-worker aufgerufen

		if (activatedResultFrameNr_glob == 0)
			guiworker1_glob.CompareTradelist();
		else if (activatedResultFrameNr_glob == 1)
			guiworker2_glob.CompareTradelist();
		else if (activatedResultFrameNr_glob == 2)
			guiworker3_glob.CompareTradelist();
		else if (activatedResultFrameNr_glob == 3)
			guiworker4_glob.CompareTradelist();
		else
			Mbox.Infobox("please klick resultlist");

	}

	private ListModel AufbauAlgoliste()
	{
		// hier wird die algoliste aufgebaut,
		// der teilstring muss in dem namen vorkommen

		Algoliste.init("EURUSD"/*
								 * jTextField2currency . getText()
								 */);
		String Algliste[] = Algoliste.calcStringliste();
		ListModel jList1Model = new DefaultComboBoxModel(Algliste);
		return jList1Model;
	}
	
	

	private void showTradelistbuttonActionPerformed(ActionEvent evt)
	{
		// die Tradeliste wird in einem neuen Fenster angezeigt
		System.out.println("showTradelistbutton.actionPerformed, event=" + evt);

		guiworker1_glob.ShowTradelist();
	}

	private void jMenuItem8configActionPerformed(ActionEvent evt)
	{
		System.out.println("jMenuItem8config.actionPerformed, event=" + evt);
		// setConfig

	}

	private void jCheckBox1weekdayActionPerformed(ActionEvent evt)
	{
		// der Time-SelektionButton wurde angeklickt
		System.out.println("jCheckBox1weekday.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox1weekday.actionPerformed
		jCheckBox1time.setSelected(false);
		setTimefilter();
	}

	private void jCheckBox1timeActionPerformed(ActionEvent evt)
	{
		// Der Weekdaybutton wurde angeklickt
		System.out.println("jCheckBox1time.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox1time.actionPerformed
		jCheckBox1weekday.setSelected(false);
		setTimefilter();
	}

	private void initTimefilter()
	{
		jCheckBoxMo.setSelected(true);
		jCheckBoxTue.setSelected(true);
		jCheckBoxWe.setSelected(true);
		jCheckBoxThu.setSelected(true);
		jCheckBoxFr.setSelected(true);
		jCheckBoxSa.setSelected(true);
		jCheckBoxSu.setSelected(true);
		jCheckBox0.setSelected(true);
		jCheckBox1.setSelected(true);
		jCheckBox2.setSelected(true);
		jCheckBox3.setSelected(true);
		jCheckBox4.setSelected(true);
		jCheckBox5.setSelected(true);
		jCheckBox6.setSelected(true);
		jCheckBox7.setSelected(true);
		jCheckBox8.setSelected(true);
		jCheckBox9.setSelected(true);
		jCheckBox10.setSelected(true);
		jCheckBox11.setSelected(true);
		jCheckBox12.setSelected(true);
		jCheckBox13.setSelected(true);
		jCheckBox14.setSelected(true);
		jCheckBox15.setSelected(true);
		jCheckBox16.setSelected(true);
		jCheckBox17.setSelected(true);
		jCheckBox18.setSelected(true);
		jCheckBox19.setSelected(true);
		jCheckBox20.setSelected(true);
		jCheckBox21.setSelected(true);
		jCheckBox22.setSelected(true);
		jCheckBox23.setSelected(true);
	}

	private void setTimefilter()
	{
		timefilter.setday(0, jCheckBoxMo.isSelected());
		timefilter.setday(1, jCheckBoxTue.isSelected());
		timefilter.setday(2, jCheckBoxWe.isSelected());
		timefilter.setday(3, jCheckBoxThu.isSelected());
		timefilter.setday(4, jCheckBoxFr.isSelected());
		timefilter.setday(5, jCheckBoxSa.isSelected());
		timefilter.setday(6, jCheckBoxSu.isSelected());
		timefilter.settime(0, jCheckBox0.isSelected());
		timefilter.settime(1, jCheckBox1.isSelected());
		timefilter.settime(2, jCheckBox2.isSelected());
		timefilter.settime(3, jCheckBox3.isSelected());
		timefilter.settime(4, jCheckBox4.isSelected());
		timefilter.settime(5, jCheckBox5.isSelected());
		timefilter.settime(6, jCheckBox6.isSelected());
		timefilter.settime(7, jCheckBox7.isSelected());
		timefilter.settime(8, jCheckBox8.isSelected());
		timefilter.settime(9, jCheckBox9.isSelected());
		timefilter.settime(10, jCheckBox10.isSelected());
		timefilter.settime(11, jCheckBox11.isSelected());
		timefilter.settime(12, jCheckBox12.isSelected());
		timefilter.settime(13, jCheckBox13.isSelected());
		timefilter.settime(14, jCheckBox14.isSelected());
		timefilter.settime(15, jCheckBox15.isSelected());
		timefilter.settime(16, jCheckBox16.isSelected());
		timefilter.settime(17, jCheckBox17.isSelected());
		timefilter.settime(18, jCheckBox18.isSelected());
		timefilter.settime(19, jCheckBox19.isSelected());
		timefilter.settime(20, jCheckBox20.isSelected());
		timefilter.settime(21, jCheckBox21.isSelected());
		timefilter.settime(22, jCheckBox22.isSelected());
		timefilter.settime(23, jCheckBox23.isSelected());
	}

	private void jButton1refreshActionPerformed(ActionEvent evt)
	{
		// refresh button

		System.out.println("jButton1refresh.actionPerformed, event=" + evt);

		guiworker1_glob.clear();
		// baueAlgoliste();

		frameworker1_glob.OptimizeRefreshListe();
	}

	private void jCheckBoxMoActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBoxMo.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBoxMo.actionPerformed
		setTimefilter();
	}

	private void jCheckBoxTueActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBoxTue.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBoxTue.actionPerformed
		setTimefilter();
	}

	private void jCheckBoxWeActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBoxWe.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBoxWe.actionPerformed
		setTimefilter();
	}

	private void jCheckBoxThuActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBoxThu.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBoxThu.actionPerformed
		setTimefilter();
	}

	private void jCheckBoxFrActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBoxFr.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBoxFr.actionPerformed
		setTimefilter();
	}

	private void jCheckBoxSaActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBoxSa.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBoxSa.actionPerformed
		setTimefilter();
	}

	private void jCheckBoxSuActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBoxSu.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBoxSu.actionPerformed
		setTimefilter();
	}

	private void jCheckBox0ActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBox0.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox0.actionPerformed
		setTimefilter();
	}

	private void jCheckBox1ActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBox1.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox1.actionPerformed
		setTimefilter();
	}

	private void jCheckBox2ActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBox2.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox2.actionPerformed
		setTimefilter();
	}

	private void jCheckBox3ActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBox3.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox3.actionPerformed
		setTimefilter();
	}

	private void jCheckBox4ActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBox4.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox4.actionPerformed
		setTimefilter();
	}

	private void jCheckBox5ActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBox5.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox5.actionPerformed
		setTimefilter();
	}

	private void jCheckBox6ActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBox6.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox6.actionPerformed
		setTimefilter();
	}

	private void jCheckBox7ActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBox7.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox7.actionPerformed
		setTimefilter();
	}

	private void jCheckBox8ActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBox8.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox8.actionPerformed
		setTimefilter();
	}

	private void jCheckBox9ActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBox9.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox9.actionPerformed
		setTimefilter();
	}

	private void jCheckBox10ActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBox10.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox10.actionPerformed
		setTimefilter();
	}

	private void jCheckBox11ActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBox11.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox11.actionPerformed
		setTimefilter();
	}

	private void jCheckBox12ActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBox12.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox12.actionPerformed
		setTimefilter();
	}

	private void jCheckBox13ActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBox13.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox13.actionPerformed
		setTimefilter();
	}

	private void jCheckBox14ActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBox14.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox14.actionPerformed
		setTimefilter();
	}

	private void jCheckBox15ActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBox15.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox15.actionPerformed
		setTimefilter();
	}

	private void jCheckBox16ActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBox16.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox16.actionPerformed
		setTimefilter();
	}

	private void jCheckBox17ActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBox17.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox17.actionPerformed
		setTimefilter();
	}

	private void jCheckBox18ActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBox18.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox18.actionPerformed
		setTimefilter();
	}

	private void jCheckBox19ActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBox19.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox19.actionPerformed
		setTimefilter();
	}

	private void jCheckBox20ActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBox20.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox20.actionPerformed
		setTimefilter();
	}

	private void jCheckBox21ActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBox21.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox21.actionPerformed
		setTimefilter();
	}

	private void jCheckBox22ActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBox22.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox22.actionPerformed
		setTimefilter();
	}

	private void jCheckBox23ActionPerformed(ActionEvent evt)
	{
		System.out.println("jCheckBox23.actionPerformed, event=" + evt);
		// TODO add your code for jCheckBox23.actionPerformed
		setTimefilter();
	}

	private JInternalFrame getJInternalFrame7()
	{
		if (jInternalFrame7 == null)
		{
			jInternalFrame7 = new JInternalFrame();
			GridBagLayout jInternalFrame7Layout = new GridBagLayout();
			jInternalFrame7.setName("jInternalFrame7");
			jInternalFrame7Layout.rowWeights = new double[] {0.1, 0.1, 0.0, 0.1};
			jInternalFrame7Layout.rowHeights = new int[] {7, 7, 24, 7};
			jInternalFrame7Layout.columnWeights = new double[] {0.0, 0.1};
			jInternalFrame7Layout.columnWidths = new int[] {183, 7};
			jInternalFrame7.getContentPane().setLayout(jInternalFrame7Layout);
			jInternalFrame7.setBounds(1096, 450, 340, 105);
			{
				jButton1comparetradelists = new JButton();
				jInternalFrame7.getContentPane().add(jButton1comparetradelists, new GridBagConstraints(0, 2, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jButton1comparetradelists.setBounds(2, 563, 147, 25);
				jButton1comparetradelists.setName("jButton1comparetradelists");
				jButton1comparetradelists
						.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent evt)
							{
								jButton1comparetradelistsActionPerformed(evt);
							}
						});
			}
		}
		return jInternalFrame7;
	}

	private JInternalFrame getJInternalFrame8()
	{
		if (jInternalFrame8 == null)
		{
			jInternalFrame8 = new JInternalFrame();
			GridBagLayout jInternalFrame8Layout = new GridBagLayout();
			jInternalFrame8.setName("jInternalFrame8");
			jInternalFrame8Layout.rowWeights = new double[]
			{ 0.1, 0.1, 0.1, 0.1, 0.1 };
			jInternalFrame8Layout.rowHeights = new int[]
			{ 20, 7, 7, 7, 7 };
			jInternalFrame8Layout.columnWeights = new double[]
			{ 0.1, 0.1, 0.1, 0.1 };
			jInternalFrame8Layout.columnWidths = new int[]
			{ 7, 7, 7, 7 };
			jInternalFrame8.getContentPane().setLayout(jInternalFrame8Layout);
			jInternalFrame8.setBounds(1383, 41, 151, 224);
			{
				jButton1clear = new JButton();
				jInternalFrame8.getContentPane().add(
						jButton1clear,
						new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.NONE,
								new Insets(0, 0, 0, 0), 0, 0));
				jButton1clear.setBounds(1437, 488, 61, 21);
				jButton1clear.setName("jButton1clear");
				jButton1clear.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						jButtonClearAllActionPerformed(evt);
					}
				});
			}
			{
				jCheckBox1weekday = new JCheckBox();
				jInternalFrame8.getContentPane().add(
						jCheckBox1weekday,
						new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.NONE,
								new Insets(0, 0, 0, 0), 0, 0));
				jCheckBox1weekday.setBounds(1047, 529, 86, 25);
				jCheckBox1weekday.setName("jCheckBox1weekday");
				jCheckBox1weekday.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						jCheckBox1weekdayActionPerformed(evt);
					}
				});
			}
			{
				jCheckBox1time = new JCheckBox();
				jInternalFrame8.getContentPane().add(
						jCheckBox1time,
						new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.NONE,
								new Insets(0, 0, 0, 0), 0, 0));
				jCheckBox1time.setBounds(1174, 538, 67, 22);
				jCheckBox1time.setName("jCheckBox1time");
				jCheckBox1time.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent evt)
					{
						jCheckBox1timeActionPerformed(evt);
					}
				});
			}
		}
		return jInternalFrame8;
	}

	private JInternalFrame getJInternalFrame9()
	{
		if (jInternalFrameAlgos == null)
		{
			jInternalFrameAlgos = new JInternalFrame();
			jInternalFrameAlgos.setName("jInternalFrameAlgos");
			jInternalFrameAlgos.setBounds(771, -24, 210, 315);
			jInternalFrameAlgos.getContentPane().add(getJScrollPane2(),
					BorderLayout.CENTER);
			jInternalFrameAlgos.getContentPane().add(getJList1(),
					BorderLayout.WEST);
		}
		return jInternalFrameAlgos;
	}

	private JScrollPane getJScrollPane2()
	{
		if (jScrollPane2 == null)
		{
			jScrollPane2 = new JScrollPane();
		}
		return jScrollPane2;
	}

	private JList getJList1()
	{
		if (jAlgoliste == null)
		{
			ListModel jList1Model = AufbauAlgoliste();
			/*
			 * ListModel jList1Model = new DefaultComboBoxModel( new String[] {
			 * "Item One", "Item Two" });
			 */
			jAlgoliste = new JList();
			jAlgoliste.setModel(jList1Model);
			jAlgoliste.setPreferredSize(new java.awt.Dimension(225, 288));
			jAlgoliste.addKeyListener(new KeyAdapter()
			{
				public void keyPressed(KeyEvent evt)
				{
					jAlgolisteKeyPressed(evt);
				}
			});
			jAlgoliste.addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent evt)
				{
					jList1ValueChanged(evt);
				}
			});
		}
		return jAlgoliste;
	}

	private void jList1ValueChanged(ListSelectionEvent evt)
	{
		System.out.println("jList1.valueChanged, event=" + evt);
		// Auswahl der Algos
		// ein neuer algo wurde angewählt
		updateSelectedIndis();
	}

	private void updateSelectedIndis()
	{
		//die ausgewählten Indikatoren die für die Berechnung verwendet werden werden ermittelt
		String selection = jAlgoliste.getSelectedValuesList().toString();

		System.out.println("selection2=" + selection);

		if (activatedResultFrameNr_glob == 0)
			frameworker1_glob.showFiltername(selection);
		if (activatedResultFrameNr_glob == 1)
			frameworker2_glob.showFiltername(selection);
		if (activatedResultFrameNr_glob == 2)
			frameworker3_glob.showFiltername(selection);
		if (activatedResultFrameNr_glob == 3)
			frameworker4_glob.showFiltername(selection);

		guiworker1_glob.setSelectedAlgoStrategies(selection);
		guiworker2_glob.setSelectedAlgoStrategies(selection);
		guiworker3_glob.setSelectedAlgoStrategies(selection);
		guiworker4_glob.setSelectedAlgoStrategies(selection);
		
	}
	
	
	private JPanel getJPanel1()
	{
		if (jPanel1 == null)
		{
			jPanel1 = new JPanel();
			FlowLayout jPanel1Layout = new FlowLayout();
			jPanel1.setLayout(jPanel1Layout);
			jPanel1.setPreferredSize(new java.awt.Dimension(1043, 30));
			jPanel1.add(getJButtonF1());
			jPanel1.add(getJButton4());
		}
		return jPanel1;
	}

	private JButton getJButtonF1()
	{
		if (jButtonF1 == null)
		{
			jButtonF1 = new JButton();
			jButtonF1.setName("jButton1");
			jButtonF1.setPreferredSize(new java.awt.Dimension(154, 25));
			jButtonF1.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					jButtonF1optimizeActionPerformed(evt);
				}
			});
		}
		return jButtonF1;
	}

	private JScrollPane getJScrollPane1()
	{
		if (jScrollPane1 == null)
		{
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setPreferredSize(new java.awt.Dimension(154, 159));
			// jScrollPane1.setViewportView(getJTable1());
		}
		return jScrollPane1;
	}

	private JInternalFrame getJInternalFrame1()
	{
		if (jInternalFrame1 == null)
		{
			jInternalFrame1 = new JInternalFrame();
			jInternalFrame1.setName("jInternalFrame1");
			jInternalFrame1.setBounds(263, 379, 276, 258);
			jInternalFrame1.addInternalFrameListener(new InternalFrameAdapter()
			{
				public void internalFrameActivated(InternalFrameEvent evt)
				{
					jInternalFrame1InternalFrameActivated(evt);
				}
			});
			jInternalFrame1.getContentPane().add(getJPanel2(),
					BorderLayout.SOUTH);
			jInternalFrame1.getContentPane().add(getJLabel5(),
					BorderLayout.NORTH);
			jInternalFrame1.getContentPane().add(getJScrollPane3(),
					BorderLayout.CENTER);
			jInternalFrame1.getContentPane().add(getJPanel9(),
					BorderLayout.NORTH);
		}
		return jInternalFrame1;
	}

	private JPanel getJPanel2()
	{
		if (jPanel2 == null)
		{
			jPanel2 = new JPanel();
			FlowLayout jPanel2Layout = new FlowLayout();
			jPanel2.setPreferredSize(new java.awt.Dimension(194, 33));
			jPanel2.setLayout(jPanel2Layout);
			jPanel2.add(getJButton2());
			jPanel2.add(getJButton1optimizeall());
			jPanel2.add(getJButton6clear());
		}
		return jPanel2;
	}

	private JButton getJButton2()
	{
		if (jButtonF2 == null)
		{
			jButtonF2 = new JButton();
			jButtonF2.setPreferredSize(new java.awt.Dimension(178, 25));
			jButtonF2.setName("jButton1");
			jButtonF2.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					jButtonF2ActionPerformed(evt);
				}
			});
		}
		return jButtonF2;
	}

	private JLabel getJLabel5()
	{
		if (jLabel5 == null)
		{
			jLabel5 = new JLabel();
			jLabel5.setName("jLabel5");
		}
		return jLabel5;
	}

	private JScrollPane getJScrollPane3()
	{
		if (jScrollPane3 == null)
		{
			jScrollPane3 = new JScrollPane();
			jScrollPane3.setPreferredSize(new java.awt.Dimension(260, 177));
		}
		return jScrollPane3;
	}

	// ******************************************************************************
	// Die optimize Aktionen
	public void jButtonF1optimizeActionPerformed(ActionEvent evt)
	{
		//Hier wurde der optimze Button auf der Oberfläche gedrückt
		updateSelectedIndis();
		//die Tagesfilter setzten und anzeigen
		setTimefilter();
		frameworker1_glob.setChoosenFilter(jAlgoliste.getSelectedValuesList()
				.toString());
		//das Optimize wurde gestartet
		frameworker1_glob.jButton1optimizeActionPerformed(evt);
		
	}
	private void jButtonF2ActionPerformed(ActionEvent evt)
	{
		System.out.println("jButton2.actionPerformed, event=" + evt);
		// OPTIMIZE2 Der optimize Button1 wurde gedrückt
		System.out.println("jButton1optimize.actionPerformed, event=" + evt);

		updateSelectedIndis();
		
		// den timefilter auswerten
		setTimefilter();
		frameworker1_glob.setChoosenFilter(jAlgoliste.getSelectedValuesList()
				.toString());
		frameworker2_glob.jButton1optimizeActionPerformed(evt);
	}
	private void jButtonF3ActionPerformed(ActionEvent evt)
	{
		System.out.println("jButton6.actionPerformed, event=" + evt);
		// TODO add your code for jButton6.actionPerformed
		updateSelectedIndis();
		setTimefilter();
		frameworker3_glob.setChoosenFilter(jAlgoliste.getSelectedValuesList()
				.toString());
		frameworker3_glob.jButton1optimizeActionPerformed(evt);
	}

	private void jButtonF4ActionPerformed(ActionEvent evt)
	{
		System.out.println("jButton9.actionPerformed, event=" + evt);
		// TODO add your code for jButton9.actionPerformed
		updateSelectedIndis();
		setTimefilter();
		frameworker4_glob.setChoosenFilter(jAlgoliste.getSelectedValuesList()
				.toString());
		frameworker4_glob.jButton1optimizeActionPerformed(evt);
	}

	private JButton getJButton6()
	{
		if (jButtonF3 == null)
		{
			jButtonF3 = new JButton();
			jButtonF3.setPreferredSize(new java.awt.Dimension(176, 25));
			jButtonF3.setName("jButton1");
			jButtonF3.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					jButtonF3ActionPerformed(evt);
				}
			});
		}
		return jButtonF3;
	}

	private JButton getJButton9()
	{
		if (jButtonF4 == null)
		{
			jButtonF4 = new JButton();
			jButtonF4.setPreferredSize(new java.awt.Dimension(85, 25));
			jButtonF4.setName("jButton1");
			jButtonF4.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					jButtonF4ActionPerformed(evt);
				}
			});
		}
		return jButtonF4;
	}

	private JButton getJButton4()
	{
		if (jButton4 == null)
		{
			jButton4 = new JButton();
			jButton4.setName("jButton4");
			jButton4.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					jButton4ActionPerformed(evt);
				}
			});
		}
		return jButton4;
	}

	private JButton getJButton6clear()
	{
		if (jButton6clear == null)
		{
			jButton6clear = new JButton();
			jButton6clear.setName("jButton6clear");
			jButton6clear.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					jButton6clearActionPerformed(evt);
				}
			});
		}
		return jButton6clear;
	}

	private JInternalFrame getJInternalFrame2()
	{
		if (jInternalFrame2 == null)
		{
			jInternalFrame2 = new JInternalFrame();
			jInternalFrame2.setName("jInternalFrame2");
			jInternalFrame2.setBounds(538, 379, 276, 258);
			jInternalFrame2.addInternalFrameListener(new InternalFrameAdapter()
			{
				public void internalFrameActivated(InternalFrameEvent evt)
				{
					jInternalFrame2InternalFrameActivated(evt);
				}
			});
			jInternalFrame2.getContentPane().add(getJPanel3(),
					BorderLayout.SOUTH);
			jInternalFrame2.getContentPane().add(getJLabel7(),
					BorderLayout.NORTH);
			jInternalFrame2.getContentPane().add(getJScrollPane4(),
					BorderLayout.CENTER);
			jInternalFrame2.getContentPane().add(getJPanel10(),
					BorderLayout.NORTH);
		}
		return jInternalFrame2;
	}

	private JPanel getJPanel3()
	{
		if (jPanel3 == null)
		{
			jPanel3 = new JPanel();
			FlowLayout jPanel3Layout = new FlowLayout();
			jPanel3.setPreferredSize(new java.awt.Dimension(194, 33));
			jPanel3.setLayout(jPanel3Layout);
			jPanel3.add(getJButton6());
			jPanel3.add(getJButton7());
		}
		return jPanel3;
	}

	private JButton getJButton7()
	{
		if (jButton7 == null)
		{
			jButton7 = new JButton();
			jButton7.setName("jButton6clear");
			jButton7.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					jButton7ActionPerformed(evt);
				}
			});
		}
		return jButton7;
	}

	private JLabel getJLabel7()
	{
		if (jLabel7 == null)
		{
			jLabel7 = new JLabel();
			jLabel7.setName("jLabel5");
		}
		return jLabel7;
	}

	private JScrollPane getJScrollPane4()
	{
		if (jScrollPane4 == null)
		{
			jScrollPane4 = new JScrollPane();
			jScrollPane4.setPreferredSize(new java.awt.Dimension(260, 177));
		}
		return jScrollPane4;
	}

	private JInternalFrame getJInternalFrame3()
	{
		if (jInternalFrame3 == null)
		{
			jInternalFrame3 = new JInternalFrame();
			jInternalFrame3.setName("jInternalFrame3");
			jInternalFrame3.setBounds(814, 379, 276, 258);
			jInternalFrame3.addInternalFrameListener(new InternalFrameAdapter()
			{
				public void internalFrameActivated(InternalFrameEvent evt)
				{
					jInternalFrame3InternalFrameActivated(evt);
				}
			});
			jInternalFrame3.getContentPane().add(getJPanel4(),
					BorderLayout.SOUTH);
			jInternalFrame3.getContentPane().add(getJLabel9(),
					BorderLayout.NORTH);
			jInternalFrame3.getContentPane().add(getJScrollPane5(),
					BorderLayout.CENTER);
			jInternalFrame3.getContentPane().add(getJPanel11(),
					BorderLayout.NORTH);
		}
		return jInternalFrame3;
	}

	private JPanel getJPanel4()
	{
		if (jPanel4 == null)
		{
			jPanel4 = new JPanel();
			FlowLayout jPanel4Layout = new FlowLayout();
			jPanel4.setPreferredSize(new java.awt.Dimension(194, 33));
			jPanel4.setLayout(jPanel4Layout);
			jPanel4.add(getJButton9());
			jPanel4.add(getJButton10());
		}
		return jPanel4;
	}

	private JButton getJButton10()
	{
		if (jButton10 == null)
		{
			jButton10 = new JButton();
			jButton10.setName("jButton6clear");
			jButton10.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					jButton10ActionPerformed(evt);
				}
			});
		}
		return jButton10;
	}

	private JLabel getJLabel9()
	{
		if (jLabel9 == null)
		{
			jLabel9 = new JLabel();
			jLabel9.setName("jLabel5");
		}
		return jLabel9;
	}

	private JScrollPane getJScrollPane5()
	{
		if (jScrollPane5 == null)
		{
			jScrollPane5 = new JScrollPane();
			jScrollPane5.setPreferredSize(new java.awt.Dimension(260, 177));
		}
		return jScrollPane5;
	}

	public void jButtonClearAllActionPerformed(ActionEvent evt)
	{
		// der ClearAll Button wurde betätigt
		System.out.println("jButton1clear.actionPerformed, event=" + evt);

		clearAll();

	}

	private void clearAll()
	{
		//orginal strategie löschen
		jInternalFrameOrgStrat.getContentPane().removeAll();
		jInternalFrameOrgStrat.updateUI();
		

		//das resultframe mit den ganzen übereinandergeschichteten graphiken löschen
		jInternalFrameResultStrat.getContentPane().removeAll();
		jInternalFrameResultStrat.updateUI();
		
		//Alles wird gelöscht da ne neue Strategie reingeladen wurde
		StaticMultiProfitPanel.cleanAll();
		

		// TODO add your code for jButton1clear.actionPerformed
		frameworker1_glob.jButtonClearActionPerformed();
		frameworker2_glob.jButtonClearActionPerformed();
		frameworker3_glob.jButtonClearActionPerformed();
		frameworker4_glob.jButtonClearActionPerformed();
	}
	
	private void jButton4ActionPerformed(ActionEvent evt)
	{
		System.out.println("jButton4.actionPerformed, event=" + evt);
		// TODO add your code for jButton4.actionPerformed
		frameworker1_glob.jButtonClearActionPerformed();
	}

	private void jButton6clearActionPerformed(ActionEvent evt)
	{
		System.out.println("jButton6clear.actionPerformed, event=" + evt);
		// TODO add your code for jButton6clear.actionPerformed
		frameworker2_glob.jButtonClearActionPerformed();
	}

	private void jButton7ActionPerformed(ActionEvent evt)
	{
		System.out.println("jButton7.actionPerformed, event=" + evt);
		// TODO add your code for jButton7.actionPerformed
		frameworker3_glob.jButtonClearActionPerformed();
	}

	private void jButton10ActionPerformed(ActionEvent evt)
	{
		System.out.println("jButton10.actionPerformed, event=" + evt);
		// TODO add your code for jButton10.actionPerformed
		frameworker4_glob.jButtonClearActionPerformed();
	}

	private JPanel getJPanel6()
	{
		if (jPanel6 == null)
		{
			jPanel6 = new JPanel();
		}
		return jPanel6;
	}

	private void jInternalFrameResults1InternalFrameActivated(
			InternalFrameEvent evt)
	{
		System.out
				.println("jInternalFrameResults1.internalFrameActivated, event="
						+ evt);

		// TODO add your code for jInternalFrameResults1.internalFrameActivated
		activatedResultFrameNr_glob = 0;
	}

	private JPanel getJPanel7()
	{
		if (jPanel7 == null)
		{
			jPanel7 = new JPanel();
			BoxLayout jPanel7Layout = new BoxLayout(jPanel7,
					javax.swing.BoxLayout.Y_AXIS);
			jPanel7.setLayout(jPanel7Layout);
			jPanel7.setPreferredSize(new java.awt.Dimension(245, 33));
			jPanel7.add(getJLabel4());
			jPanel7.add(getJLabel11());
		}
		return jPanel7;
	}

	private JLabel getJLabel4()
	{
		if (jLabel4 == null)
		{
			jLabel4 = new JLabel();
			jLabel4.setName("jLabel4");
			
		}
		return jLabel4;
	}

	private JLabel getJLabel11()
	{
		if (jLabel11 == null)
		{
			jLabel11 = new JLabel();
			jLabel11.setName("jLabel11");
		}
		return jLabel11;
	}

	private JPanel getJPanel8()
	{
		if (jPanel8 == null)
		{
			jPanel8 = new JPanel();
			BoxLayout jPanel8Layout = new BoxLayout(jPanel8,
					javax.swing.BoxLayout.Y_AXIS);
			jPanel8.setPreferredSize(new java.awt.Dimension(245, 33));
			jPanel8.setLayout(jPanel8Layout);
			jPanel8.add(getJLabel6());
			jPanel8.add(getJLabel8());
		}
		return jPanel8;
	}

	private JLabel getJLabel6()
	{
		if (jLabel6 == null)
		{
			jLabel6 = new JLabel();
			jLabel6.setName("jLabel4");
		}
		return jLabel6;
	}

	private JLabel getJLabel8()
	{
		if (jLabel8 == null)
		{
			jLabel8 = new JLabel();
			jLabel8.setName("jLabel11");
		}
		return jLabel8;
	}

	private JPanel getJPanel9()
	{
		if (jPanel9 == null)
		{
			jPanel9 = new JPanel();
			BoxLayout jPanel9Layout = new BoxLayout(jPanel9,
					javax.swing.BoxLayout.Y_AXIS);
			jPanel9.setLayout(jPanel9Layout);
			jPanel9.setPreferredSize(new java.awt.Dimension(260, 38));
			jPanel9.add(getJLabel10());
			jPanel9.add(getJLabel12());
		}
		return jPanel9;
	}

	private JLabel getJLabel10()
	{
		if (jLabel10 == null)
		{
			jLabel10 = new JLabel();
			jLabel10.setName("jLabel10");
		}
		return jLabel10;
	}

	private JLabel getJLabel12()
	{
		if (jLabel12 == null)
		{
			jLabel12 = new JLabel();
			jLabel12.setName("jLabel12");
		}
		return jLabel12;
	}

	private JPanel getJPanel10()
	{
		if (jPanel10 == null)
		{
			jPanel10 = new JPanel();
			BoxLayout jPanel10Layout = new BoxLayout(jPanel10,
					javax.swing.BoxLayout.Y_AXIS);
			jPanel10.setLayout(jPanel10Layout);
			jPanel10.setPreferredSize(new java.awt.Dimension(260, 38));
			jPanel10.add(getJLabel13());
			jPanel10.add(getJLabel14());
		}
		return jPanel10;
	}

	private JLabel getJLabel13()
	{
		if (jLabel13 == null)
		{
			jLabel13 = new JLabel();
			jLabel13.setName("jLabel13");
		}
		return jLabel13;
	}

	private JLabel getJLabel14()
	{
		if (jLabel14 == null)
		{
			jLabel14 = new JLabel();
			jLabel14.setName("jLabel14");
		}
		return jLabel14;
	}

	private JPanel getJPanel11()
	{
		if (jPanel11 == null)
		{
			jPanel11 = new JPanel();
			BoxLayout jPanel11Layout = new BoxLayout(jPanel11,
					javax.swing.BoxLayout.Y_AXIS);
			jPanel11.setLayout(jPanel11Layout);
			jPanel11.setPreferredSize(new java.awt.Dimension(260, 38));
			jPanel11.add(getJLabel15());
			jPanel11.add(getJLabel16());
		}
		return jPanel11;
	}

	private JLabel getJLabel15()
	{
		if (jLabel15 == null)
		{
			jLabel15 = new JLabel();
			jLabel15.setName("jLabel15");
		}
		return jLabel15;
	}

	private JLabel getJLabel16()
	{
		if (jLabel16 == null)
		{
			jLabel16 = new JLabel();
			jLabel16.setName("jLabel16");
		}
		return jLabel16;
	}

	private void jInternalFrame1InternalFrameActivated(InternalFrameEvent evt)
	{
		System.out.println("jInternalFrame1.internalFrameActivated, event="
				+ evt);
		// TODO add your code for jInternalFrame1.internalFrameActivated
		activatedResultFrameNr_glob = 1;
	}

	private void jInternalFrame2InternalFrameActivated(InternalFrameEvent evt)
	{
		System.out.println("jInternalFrame2.internalFrameActivated, event="
				+ evt);
		// TODO add your code for jInternalFrame2.internalFrameActivated
		activatedResultFrameNr_glob = 2;
	}

	private void jInternalFrame3InternalFrameActivated(InternalFrameEvent evt)
	{
		System.out.println("jInternalFrame3.internalFrameActivated, event="
				+ evt);
		// TODO add your code for jInternalFrame3.internalFrameActivated
		activatedResultFrameNr_glob = 3;
	}

	private void jAlgolisteKeyPressed(KeyEvent evt)
	{
		System.out.println("jAlgoliste.keyPressed, event=" + evt);
		// TODO add your code for jAlgoliste.keyPressed
	}

	private JInternalFrame getJInternalFrame9x() {
		if(jInternalFrame9 == null) {
			jInternalFrame9 = new JInternalFrame();
			jInternalFrame9.setBounds(2, 366, 1085, 413);
			jInternalFrame9.setName("jInternalFrame9");
			jInternalFrame9.getContentPane().add(getJTabbedPane2(), BorderLayout.CENTER);
			
		}
		return jInternalFrame9;
	}
	
	private JTabbedPane getJTabbedPane2() {
		if(jTabbedPane2 == null) {
			jTabbedPane2 = new JTabbedPane();
			jTabbedPane2.addTab("OptimizeStep1", null, getJTabbedPane3(), null);
			jTabbedPane2.addTab("OptimizeStep2", null, getJTabbedPane4(), null);
			jTabbedPane2.addTab("OptimizeStep3", null, getJTabbedPane5(), null);
			jTabbedPane2.addTab("OptimizeStep4", null, getJTabbedPane6(), null);
		}
		return jTabbedPane2;
	}
	
	private JTabbedPane getJTabbedPane3() {
		if(OptimizeStep1 == null) {
			OptimizeStep1 = new JTabbedPane();
			{
				jInternalFrameResults1 = new JInternalFrame();
				OptimizeStep1.addTab("", null, jInternalFrameResults1, null);
				jInternalFrameResults1
				.setName("jInternalFrameResults1");
				jInternalFrameResults1.setBounds(2, 385, 261, 256);
				jInternalFrameResults1
				.addInternalFrameListener(new InternalFrameAdapter()
				{
					public void internalFrameActivated(
							InternalFrameEvent evt)
					{
						jInternalFrameResults1InternalFrameActivated(evt);
					}
				});
				jInternalFrameResults1.getContentPane().add(
						getJScrollPane1(), BorderLayout.CENTER);
				jInternalFrameResults1.getContentPane().add(
						getJScrollPane1(), BorderLayout.CENTER);
				jInternalFrameResults1.getContentPane().add(getJPanel1(), BorderLayout.SOUTH);
				jInternalFrameResults1.getContentPane().add(
						getJPanel7(), BorderLayout.NORTH);
				
			}
		}
		return OptimizeStep1;
	}
	
	private JTabbedPane getJTabbedPane4() {
		if(OptimizeStep2 == null) {
			OptimizeStep2 = new JTabbedPane();
			OptimizeStep2.addTab("", null, getJInternalFrame1(), null);
		}
		return OptimizeStep2;
	}
	
	private JTabbedPane getJTabbedPane5() {
		if(OptimizeStep3 == null) {
			OptimizeStep3 = new JTabbedPane();
			OptimizeStep3.addTab("", null, getJInternalFrame2(), null);
		}
		return OptimizeStep3;
	}
	
	private JTabbedPane getJTabbedPane6() {
		if(OptimizeStep4 == null) {
			OptimizeStep4 = new JTabbedPane();
			OptimizeStep4.addTab("", null, getJInternalFrame3(), null);
		}
		return OptimizeStep4;
	}
	
	private JButton getJButton1showindikator() {
		if(jButton1showindikator == null) {
			jButton1showindikator = new JButton();
			jButton1showindikator.setName("jButton1showindikator");
			jButton1showindikator.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jButton1showindikatorActionPerformed(evt);
				}
			});
		}
		return jButton1showindikator;
	}
	
	private void jButton1showindikatorActionPerformed(ActionEvent evt) {
		System.out.println("jButton1showindikator.actionPerformed, event="+evt);
		//TODO add your code for jButton1showindikator.actionPerformed
		//show indikator
		String selection = jAlgoliste.getSelectedValuesList().toString();
		
		JShowIndikator.main(selection);
		
	}
	
	private JInternalFrame getJInternalFrame10() {
		if(jInternalFrame10 == null) {
			jInternalFrame10 = new JInternalFrame();
			GridBagLayout jInternalFrame10Layout = new GridBagLayout();
			jInternalFrame10.setBounds(1094, 561, 424, 229);
			jInternalFrame10.setName("jInternalFrame10");
			jInternalFrame10.setTitle("indicator info");
			jInternalFrame10Layout.rowWeights = new double[] {0.0, 0.0};
			jInternalFrame10Layout.rowHeights = new int[] {98, 116};
			jInternalFrame10Layout.columnWeights = new double[] {0.0, 0.1};
			jInternalFrame10Layout.columnWidths = new int[] {115, 7};
			jInternalFrame10.getContentPane().setLayout(jInternalFrame10Layout);
			jInternalFrame10.getContentPane().add(getJScrollPane6(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jInternalFrame10.getContentPane().add(getJLabel17(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jInternalFrame10.getContentPane().add(getJLabel18(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jInternalFrame10.getContentPane().add(getJTextArea2(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jInternalFrame10;
	}

	private JScrollPane getJScrollPane6() {
		if(jScrollPane6 == null) {
			jScrollPane6 = new JScrollPane();
			jScrollPane6.setViewportView(getJTextArea1());
		}
		return jScrollPane6;
	}

	private JLabel getJLabel17() {
		if(jLabel17 == null) {
			jLabel17 = new JLabel();
			jLabel17.setName("jLabel17");
			
			
			
		}
		return jLabel17;
	}
	
	private JLabel getJLabel18() {
		if(jLabel18 == null) {
			jLabel18 = new JLabel();
			jLabel18.setName("jLabel18");
		}
		return jLabel18;
	}
	
	private JTextArea getJTextArea1() {
		if(jTextArea1 == null) {
			jTextArea1 = new JTextArea();
			jTextArea1.setName("jTextArea1");
			
		}
		return jTextArea1;
	}
	
	private JTextArea getJTextArea2() {
		if(jTextArea2 == null) {
			jTextArea2 = new JTextArea();
			jTextArea2.setName("jTextArea2");
		}
		return jTextArea2;
	}
	
	private JButton getJButton1saveEa() {
		if(jButton1saveEa == null) {
			jButton1saveEa = new JButton();
			jButton1saveEa.setName("jButton1saveEa");
			jButton1saveEa.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jButton1saveEaActionPerformed(evt);
				}
			});
		}
		return jButton1saveEa;
	}
	
	private void jButton1saveEaActionPerformed(ActionEvent evt) {
		System.out.println("jButton1saveEa.actionPerformed, event="+evt);
		//TODO add your code for jButton1saveEa.actionPerformed
		//den Ea modifizieren und abspeichern
		frameworker1_glob.modifySaveEa(quellstrategy_glob);
	}
	
	private JButton getJButton1optimizeall() {
		if(jButton1optimizeall == null) {
			jButton1optimizeall = new JButton();
			jButton1optimizeall.setName("jButton1optimizeall");
			jButton1optimizeall.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jButtonF2optimizeAND_ActionPerformed(evt);
				}
			});
		}
		return jButton1optimizeall;
	}
	
	private void jButtonF2optimizeAND_ActionPerformed(ActionEvent evt) {
		System.out.println("jButton1optimizeall.actionPerformed, event="+evt);
		//optimize all strategies (& -verknüpfung)
		//die Optimierungsergebnisse von F1 werden in F2 mit übernommen
		//Es wird dann auf jedes Resultat jeder indikator angewendet
		
		//die internen Werte der selektierten indikatoren refreshen
		updateSelectedIndis();
		
		// den timefilter auswerten, also die checkboxen abfragen
		setTimefilter();
		frameworker2_glob.setChoosenFilter(jAlgoliste.getSelectedValuesList()
				.toString());
		
		OptimizeResultliste ores=frameworker1_glob.HoleOptimizeResultliste();
		Tradeliste trorg=frameworker1_glob.HoleTradelisteOrginal();
		frameworker2_glob.jButton1optimizeAND_ActionPerformed(evt,trorg,ores);
	}

}
