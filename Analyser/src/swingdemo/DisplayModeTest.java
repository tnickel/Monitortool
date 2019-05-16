package swingdemo;

/*
 * Copyright (c) 2006 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */

/*
 test @(#)DisplayModeTest.java  1.4 01/07/17
 @bug 4189326
 @summary Tests changing display mode
 @author martak@eng: area=FullScreen
 @ignore This test enters full-screen mode, if available, and should not
 be run as an applet or as part of the test harness.
 */

/**
 * This test generates a table of all available display modes, enters
 * full-screen mode, if available, and allows you to change the display mode.
 * The application should look fine under each enumerated display mode. On UNIX,
 * only a single display mode should be available, and on Microsoft Windows,
 * display modes should depend on direct draw availability and the type of
 * graphics card.
 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

class DisplayModeModel extends DefaultTableModel
{
	private DisplayMode[] modes;

	public DisplayModeModel(DisplayMode[] modes)
	{
		this.modes = modes;
	}

	public DisplayMode getDisplayMode(int r)
	{
		return modes[r];
	}

	@Override
	public String getColumnName(int c)
	{
		return DisplayModeTest.COLUMN_NAMES[c];
	}

	@Override
	public int getColumnCount()
	{
		return DisplayModeTest.COLUMN_WIDTHS.length;
	}

	@Override
	public boolean isCellEditable(int r, int c)
	{
		return false;
	}

	@Override
	public int getRowCount()
	{
		if (modes == null)
		{
			return 0;
		}
		return modes.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int colIndex)
	{
		DisplayMode dm = modes[rowIndex];
		switch (colIndex)
		{
		case DisplayModeTest.INDEX_WIDTH:
			return Integer.toString(dm.getWidth());
		case DisplayModeTest.INDEX_HEIGHT:
			return Integer.toString(dm.getHeight());
		case DisplayModeTest.INDEX_BITDEPTH:
		{
			int bitDepth = dm.getBitDepth();
			String ret;
			if (bitDepth == DisplayMode.BIT_DEPTH_MULTI)
			{
				ret = "Multi";
			} else
			{
				ret = Integer.toString(bitDepth);
			}
			return ret;
		}
		case DisplayModeTest.INDEX_REFRESHRATE:
		{
			int refreshRate = dm.getRefreshRate();
			String ret;
			if (refreshRate == DisplayMode.REFRESH_RATE_UNKNOWN)
			{
				ret = "Unknown";
			} else
			{
				ret = Integer.toString(refreshRate);
			}
			return ret;
		}
		}
		throw new ArrayIndexOutOfBoundsException("Invalid column value");
	}

}

public class DisplayModeTest extends JFrame implements ActionListener,
		ListSelectionListener
{

	private boolean waiting = false;

	private GraphicsDevice device;

	private DisplayMode originalDM;

	private JButton exit = new JButton("Exit");

	private JButton changeDM = new JButton("Set Display");

	private JLabel currentDM = new JLabel();

	private JTable dmList = new JTable();

	private JScrollPane dmPane = new JScrollPane(dmList);

	private boolean isFullScreen = false;

	public static final int INDEX_WIDTH = 0;

	public static final int INDEX_HEIGHT = 1;

	public static final int INDEX_BITDEPTH = 2;

	public static final int INDEX_REFRESHRATE = 3;

	public static final int[] COLUMN_WIDTHS = new int[]
	{ 100, 100, 100, 100 };

	public static final String[] COLUMN_NAMES = new String[]
	{ "Width", "Height", "Bit Depth", "Refresh Rate" };

	public DisplayModeTest(GraphicsDevice device)
	{
		super(device.getDefaultConfiguration());
		this.device = device;
		setTitle("Display Mode Test");
		originalDM = device.getDisplayMode();
		setDMLabel(originalDM);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// Make sure a DM is always selected in the list
		exit.addActionListener(this);
		changeDM.addActionListener(this);
		changeDM.setEnabled(device.isDisplayChangeSupported());
	}

	public void actionPerformed(ActionEvent ev)
	{
		Object source = ev.getSource();
		if (source == exit)
		{
			device.setDisplayMode(originalDM);
			System.exit(0);
		} else
		{ // if (source == changeDM)
			int index = dmList.getSelectionModel().getAnchorSelectionIndex();
			if (index >= 0)
			{
				DisplayModeModel model = (DisplayModeModel) dmList.getModel();
				DisplayMode dm = model.getDisplayMode(index);
				// device.setDisplayMode(dm); removed while dangerours
				setDMLabel(dm);
				setSize(new Dimension(dm.getWidth(), dm.getHeight()));
				validate();
			}
		}
	}

	public void valueChanged(ListSelectionEvent ev)
	{
		changeDM.setEnabled(device.isDisplayChangeSupported());
		System.out.println("value changed *********************");
	}

	private void initComponents(Container c)
	{
		setContentPane(c);
		c.setLayout(new BorderLayout());
		// Current DM
		JPanel currentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		c.add(currentPanel, BorderLayout.NORTH);
		JLabel current = new JLabel("Current Display Mode : ");
		currentPanel.add(current);
		currentPanel.add(currentDM);
		// Display Modes
		JPanel modesPanel = new JPanel(new GridLayout(1, 2));
		c.add(modesPanel, BorderLayout.CENTER);
		// List of display modes
		for (int i = 0; i < COLUMN_WIDTHS.length; i++)
		{
			TableColumn col = new TableColumn(i, COLUMN_WIDTHS[i]);
			col.setIdentifier(COLUMN_NAMES[i]);
			col.setHeaderValue(COLUMN_NAMES[i]);
			dmList.addColumn(col);
		}
		dmList.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		dmList.getSelectionModel().addListSelectionListener(this);
		modesPanel.add(dmPane);
		// Controls
		JPanel controlsPanelA = new JPanel(new BorderLayout());
		modesPanel.add(controlsPanelA);
		JPanel controlsPanelB = new JPanel(new GridLayout(2, 1));
		controlsPanelA.add(controlsPanelB, BorderLayout.NORTH);
		// Exit
		JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		controlsPanelB.add(exitPanel);
		exitPanel.add(exit);
		// Change DM
		JPanel changeDMPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		controlsPanelB.add(changeDMPanel);
		changeDMPanel.add(changeDM);
		controlsPanelA.add(new JPanel(), BorderLayout.CENTER);
	}

	@Override
	public void setVisible(boolean isVis)
	{
		super.setVisible(isVis);
		if (isVis)
		{
			dmList.setModel(new DisplayModeModel(device.getDisplayModes()));
		}
	}

	public void setDMLabel(DisplayMode newMode)
	{
		int bitDepth = newMode.getBitDepth();
		int refreshRate = newMode.getRefreshRate();
		String bd, rr;
		if (bitDepth == DisplayMode.BIT_DEPTH_MULTI)
		{
			bd = "Multi";
		} else
		{
			bd = Integer.toString(bitDepth);
		}
		if (refreshRate == DisplayMode.REFRESH_RATE_UNKNOWN)
		{
			rr = "Unknown";
		} else
		{
			rr = Integer.toString(refreshRate);
		}
		currentDM.setText(COLUMN_NAMES[INDEX_WIDTH] + ": " + newMode.getWidth()
				+ " " + COLUMN_NAMES[INDEX_HEIGHT] + ": " + newMode.getHeight()
				+ " " + COLUMN_NAMES[INDEX_BITDEPTH] + ": " + bd + " "
				+ COLUMN_NAMES[INDEX_REFRESHRATE] + ": " + rr);
	}

	public void begin()
	{
		isFullScreen = device.isFullScreenSupported();
		setUndecorated(isFullScreen);
		setResizable(!isFullScreen);
		if (isFullScreen)
		{
			// Full-screen mode
			device.setFullScreenWindow(this);
			validate();
		} else
		{
			// Windowed mode
			pack();
			setVisible(true);
		}
	}

	public static void main(String[] args)
	{
		GraphicsEnvironment env = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice[] devices = env.getScreenDevices();
		// REMIND : Multi-monitor full-screen mode not yet supported
		for (int i = 0; i < 1 /* devices.length */; i++)
		{
			DisplayModeTest test = new DisplayModeTest(devices[i]);
			test.initComponents(test.getContentPane());
			test.begin();
		}
	}
}
