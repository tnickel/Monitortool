package swtHilfsfenster;

import gui.Dialog;
import gui.Mbox;
import hiflsklasse.SG;
import hiflsklasse.Tracer;
import modtools.Installer;
import mtools.DisTool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import data.GlobalVar;
import data.Lic;

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
public class SwtEditGlobalConfig
{
	private Display dis_glob = null;
	int exitflag = 0;
	private Label label11;
	private Button askforupdate;

	private Button button1autostart;
	private Label label12maxpongtime;
	private Text text1maxpongtime;
	private Button button1freeware;
	private Button button1mtautomaticstartstop;
	private Label label7;
	private Label label13;
	private Button development;
	private Button release;

	private Text sernumber;
	private Text email;
	private Label label5;
	private Text username;
	private Text text1myfxbookintervall;
	private Text text1myfxbookpassword;
	private Text text1myfxbookeamail;

	private Label label10;
	private Label label9;
	private Label label8;
	private Text updatepath;
	private Label label6;
	private Text tradetablesize;
	private Group group3;
	private Group group2;
	private Group group1;
	private Label label4;
	private Text secondgd;
	private Label label3;
	private Text defaultgd;
	private Button okbutton;

	public void init(Display dis)
	{
		// newflag=1, eine neue config wird hinzugefügt

		dis_glob = dis;

		Shell sh = new Shell(dis);
		sh.setLayout(null);

		sh.pack();
		sh.setSize(1058, 564);

		{
			okbutton = new Button(sh, SWT.PUSH | SWT.CENTER);
			okbutton.setText("ok");
			okbutton.setBounds(948, 460, 60, 30);
			okbutton.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					okbuttonWidgetSelected(evt);
				}
			});
		}

		{
			group1 = new Group(sh, SWT.NONE);
			group1.setLayout(null);
			group1.setText("default gd");
			group1.setBounds(12, 175, 259, 80);
			{
				defaultgd = new Text(group1, SWT.BORDER);
				defaultgd.setBounds(8, 25, 28, 29);
				defaultgd.setText("5");
				if (GlobalVar.getDefaultGd() != 0)
					defaultgd.setText(String.valueOf(GlobalVar.getDefaultGd()));
				defaultgd.addSelectionListener(new SelectionAdapter()
				{
					public void widgetSelected(SelectionEvent evt)
					{
						defaultgdWidgetSelected(evt);
					}
				});
			}
			{
				label3 = new Label(group1, SWT.NONE);
				label3.setText("default gd");
				label3.setBounds(42, 25, 107, 23);
			}

		}
		{
			group2 = new Group(sh, SWT.NONE);
			group2.setLayout(null);
			group2.setText("visualisation");
			group2.setBounds(283, 167, 390, 58);
			{
				secondgd = new Text(group2, SWT.BORDER);
				secondgd.setText("9");
				secondgd.setBounds(8, 25, 28, 27);
				if (GlobalVar.getSecondGd() != 0)
					secondgd.setText((String.valueOf(GlobalVar.getSecondGd())));
			}
			{
				label4 = new Label(group2, SWT.NONE);
				label4.setText("second gd");
				label4.setBounds(48, 25, 121, 21);
			}
		}
		{
			group3 = new Group(sh, SWT.NONE);
			group3.setLayout(null);
			group3.setBounds(12, 261, 259, 64);
			{
				tradetablesize = new Text(group3, SWT.NONE);
				if (GlobalVar.getShowMaxTradetablesize() != 0)
					tradetablesize.setText(String.valueOf(GlobalVar
							.getShowMaxTradetablesize()));
				else
					tradetablesize.setText("100");
				tradetablesize.setBounds(8, 25, 47, 27);
			}
			{
				label6 = new Label(group3, SWT.NONE);
				label6.setText("show max tradetablesize");
				label6.setBounds(61, 25, 194, 20);
			}
		}

		{
			label10 = new Label(sh, SWT.NONE);
			label10.setText("myfxbook email");
			label10.setBounds(469, 245, 107, 21);
			label10.setToolTipText("you can autoconfigure myfxbook. so that your metatrader send the information to myfxbook.");
		}
		{
			label8 = new Label(sh, SWT.NONE);
			label8.setText("myfxbookpassword");
			label8.setBounds(469, 278, 129, 20);
		}
		{
			label9 = new Label(sh, SWT.NONE);
			label9.setText("myfxbook upload intervall");
			label9.setBounds(469, 310, 198, 20);
		}
		{
			text1myfxbookeamail = new Text(sh, SWT.NONE);
			if (GlobalVar.getMyfxbookemail() != null)
				text1myfxbookeamail.setText(GlobalVar.getMyfxbookemail());
			else
				text1myfxbookeamail.setText("...");
			text1myfxbookeamail.setBounds(283, 245, 174, 23);
		}
		{
			text1myfxbookpassword = new Text(sh, SWT.PASSWORD | SWT.NONE);
			if (GlobalVar.getMyfxbookpassword() != null)
				text1myfxbookpassword.setText(GlobalVar.getMyfxbookpassword());
			else
				text1myfxbookpassword.setText("...");
			text1myfxbookpassword.setBounds(283, 278, 174, 25);
		}
		{
			text1myfxbookintervall = new Text(sh, SWT.NONE);
			if (GlobalVar.getMyfxbookintervall() != 0)
				text1myfxbookintervall.setText(String.valueOf(GlobalVar
						.getMyfxbookintervall()));
			else
				text1myfxbookintervall.setText("15");
			text1myfxbookintervall.setBounds(283, 310, 60, 20);
		}
		{
			username = new Text(sh, SWT.NONE);
			String t1 = (String) GlobalVar.getUsername();
			if (t1 != null)
				username.setText(t1);
			else
				username.setText("");
			username.setBounds(12, 12, 154, 23);
		}
		{
			label5 = new Label(sh, SWT.NONE);
			label5.setText("username");
			label5.setBounds(178, 12, 84, 23);
			label5.setToolTipText("please type hier your username or pseudoname. This information is good for bug report.");
		}
		{

			email = new Text(sh, SWT.NONE);
			String t1 = (String) GlobalVar.getEmail();
			if (t1 != null)
				email.setText(t1);
			else
				email.setText("");
			email.setBounds(436, 12, 231, 23);
		}
		{
			label11 = new Label(sh, SWT.NONE);
			label11.setText("emailadress");
			label11.setBounds(673, 12, 115, 30);
			label11.setToolTipText("for bug report. You don´t need to set config emailadress");
		}
		{
			sernumber = new Text(sh, SWT.BORDER);
			sernumber.setText(String.valueOf(GlobalVar.getSernumber()));
			sernumber.setBounds(12, 126, 130, 31);
			sernumber.setEditable(false);
		}

		{
			release = new Button(sh, SWT.RADIO | SWT.LEFT);
			release.setText("release");
			release.setSelection(true);
			release.setBounds(695, 225, 71, 20);

			if (GlobalVar.getUpdatechannel() == null)
				GlobalVar.setUpdatechannel("release");
			if (GlobalVar.getUpdatechannel().equalsIgnoreCase("release") == true)
				release.setSelection(true);
			else
				release.setSelection(false);
			release.addSelectionListener(new SelectionAdapter()
			{
				public void widgetDefaultSelected(SelectionEvent evt)
				{
					releaseWidgetDefaultSelected(evt);
				}
			});
		}
		{
			development = new Button(sh, SWT.RADIO | SWT.LEFT);
			development.setText("development");
			development.setBounds(784, 225, 112, 20);
			if (GlobalVar.getUpdatechannel().equalsIgnoreCase("development") == true)
				development.setSelection(true);
			else
				development.setSelection(false);
			development.addSelectionListener(new SelectionAdapter()
			{
				public void widgetDefaultSelected(SelectionEvent evt)
				{
					developmentWidgetDefaultSelected(evt);
				}
			});
		}
		{
			label13 = new Label(sh, SWT.NONE);
			label13.setText("update channel");
			label13.setBounds(695, 193, 195, 30);
		}
		{
			label7 = new Label(sh, SWT.NONE);
			label7.setText("serialnumber");
			label7.setBounds(148, 126, 123, 23);
		}
		{
			button1freeware = new Button(sh, SWT.RADIO | SWT.LEFT);
			button1freeware.setText("freeware");
			button1freeware.setBounds(695, 251, 109, 30);
			if (GlobalVar.getUpdatechannel().equalsIgnoreCase("freeware") == true)
				button1freeware.setSelection(true);
			else
				button1freeware.setSelection(false);
			button1freeware.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent evt)
				{
					button1freewareWidgetSelected(evt);
				}
			});

		}
		{
			text1maxpongtime = new Text(sh, SWT.NONE);
			text1maxpongtime.setBounds(18, 344, 49, 24);
			if(GlobalVar.getMaxpongtime()>0)
				text1maxpongtime.setText("180");
			else
				text1maxpongtime.setText("90");
		}
		{
			label12maxpongtime = new Label(sh, SWT.NONE);
			label12maxpongtime.setText("max pongtime sec.");
			label12maxpongtime.setBounds(79, 344, 176, 36);
		}
		{
			button1autostart = new Button(sh, SWT.CHECK | SWT.LEFT);
			button1autostart.setText("autostart");
			if(GlobalVar.getAutostartfeature()==1)
				button1autostart.setSelection(true);
			else
				button1autostart.setSelection(false);
			button1autostart.setBounds(290, 349, 190, 30);
			button1autostart.setToolTipText("If the autostart button is activated monitortool do the following after restart\r\n1) Reload all tradelisten\r\n2) Start all activeted Metatrader");
		}
		{
			askforupdate = new Button(sh, SWT.CHECK | SWT.LEFT);
			askforupdate.setText("ask for update");
					askforupdate.setBounds(290, 374, 179, 30);
			if(GlobalVar.getAskforUpdateflag()==1)		
				askforupdate.setSelection(true);
			else
				askforupdate.setSelection(false);
		}
		{
			button1mtautomaticstartstop = new Button(sh, SWT.CHECK | SWT.LEFT);
			button1mtautomaticstartstop.setText("Metatrader automatic start/stop");
			button1mtautomaticstartstop.setBounds(290, 324, 178, 30);
			if(GlobalVar.getMetatraderautostartstop()==1)
				button1mtautomaticstartstop.setSelection(true);
			else
				button1mtautomaticstartstop.setSelection(false);
			
			button1mtautomaticstartstop.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					button1mtautomaticstartstopWidgetSelected(evt);
				}
			});
		}

		init();
		sh.pack();
		sh.open();

		while ((!sh.isDisposed()) && (exitflag == 0))
		{
			if (!dis.readAndDispatch())
				dis.sleep();
		}
		sh.dispose();
	}

	private void init()
	{
		
	
		
		if (5 == 5)
		{
			// freeware version
			if (Lic.getlic() == 0)
			{
				
				GlobalVar.setUpdatechannel("freeware");
			}

			// sq-version
			if ((Lic.getlic() == 0) || (Lic.getlic() == 1))
			{
				text1myfxbookeamail.setVisible(false);
				text1myfxbookeamail.setVisible(false);
				text1myfxbookpassword.setVisible(false);
				label8.setVisible(false);
				text1myfxbookintervall.setVisible(false);
				label9.setVisible(false);
				label10.setVisible(false);
				label13.setVisible(false);
				release.setVisible(false);
				development.setVisible(false);
				button1freeware.setVisible(false);

			}
		}
	}

	private void okbuttonWidgetSelected(SelectionEvent evt)
	{
		System.out.println("okbutton.widgetSelected, event=" + evt);
		// save
		

		GlobalVar.setDefaultGd(SG.get_zahl(defaultgd.getText()));
		GlobalVar.setSecondGd(SG.get_zahl(secondgd.getText()));
		GlobalVar
				.setShowmaxTradetablesize(SG.get_zahl(tradetablesize.getText()));
		GlobalVar.setMyfxbookemail(text1myfxbookeamail.getText());
		GlobalVar.setMyfxbookpassword(text1myfxbookpassword.getText());
		GlobalVar.setMyfxbookintervall(SG.get_zahl(text1myfxbookintervall
				.getText()));

		GlobalVar.setMaxpongtime(SG.get_zahl(text1maxpongtime.getText()));
		
		if (username.getText().length() > 25)
		{
			Mbox.Infobox("username error, please choose usernamelength <=25)");
			return;
		}
		if (username.getText().contains("___"))
		{
			Mbox.Infobox("this character '___' is not allowed in username");
			return;
		}
		if (username.getText().length() < 3)
		{
			Mbox.Infobox("please choose username length >=3");
			return;
		}
		GlobalVar.setUsername(username.getText());
		GlobalVar.setEmail(email.getText());
		if (email.getText().contains("___"))
		{
			Mbox.Infobox("this character '___' is not allowed in email");
			return;
		}

		

		if (development.getSelection() == true)
			GlobalVar.setUpdatechannel("development");
		if (release.getSelection() == true)
			GlobalVar.setUpdatechannel("release");

		//falls freewareversion dann immer freeware channel
		if( (Lic.getlic() == 0))
			GlobalVar.setUpdatechannel("freeware");

		if(button1autostart.getSelection()==true)
			GlobalVar.setAutostartfeature(1);
		else
			GlobalVar.setAutostartfeature(0);
		
		
			GlobalVar.setPortableflag(1);
		
		
		if(askforupdate.getSelection()==true)
			GlobalVar.setAskforUpdateflag(1);
		else
			GlobalVar.setAskforUpdateflag(0);
			
		
		GlobalVar.save();

		exitflag = 1;

	}

	

	

	
	
	
	private void defaultgdWidgetSelected(SelectionEvent evt)
	{
		System.out.println("defaultgd.widgetSelected, event=" + evt);
		// TODO add your code for defaultgd.widgetSelected
	}

	private void button_setupdatepathWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button_setupdatepath.widgetSelected, event=" + evt);
		// set updatepath

		String dirnam = Dialog.DirDialog(dis_glob, updatepath.getText());

		// falls cancel dann wird nix geändert
		if (dirnam != null)
			updatepath.setText(dirnam);
		DisTool.UpdateDisplay();
	}

	private void releaseWidgetDefaultSelected(SelectionEvent evt)
	{
		System.out.println("release.widgetDefaultSelected, event=" + evt);
		// TODO add your code for release.widgetDefaultSelected
		GlobalVar.setUpdatechannel("release");
	}

	private void developmentWidgetDefaultSelected(SelectionEvent evt)
	{
		System.out.println("development.widgetDefaultSelected, event=" + evt);
		// TODO add your code for development.widgetDefaultSelected
		GlobalVar.setUpdatechannel("development");
	}

	private void button1freewareWidgetSelected(SelectionEvent evt)
	{
		System.out.println("button1freeware.widgetSelected, event=" + evt);
		// TODO add your code for button1freeware.widgetSelected
		GlobalVar.setUpdatechannel("freeware");
	}
	
	private void button1mtautomaticstartstopWidgetSelected(SelectionEvent evt) {
		System.out.println("button1mtautomaticstartstop.widgetSelected, event="+evt);
		
		if(button1mtautomaticstartstop.getSelection()==true)
			GlobalVar.setMetatraderautostartstop(1);
		else
			GlobalVar.setMetatraderautostartstop(0);
		
		
	}
}
