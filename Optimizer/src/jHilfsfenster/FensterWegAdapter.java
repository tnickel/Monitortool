package jHilfsfenster;


import gui.Mbox;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;


class FensterWegAdapter extends WindowAdapter
{
  private JFrame frame_glob=null;	
	
  public FensterWegAdapter(JFrame frame)	
  {
	  frame_glob=frame;
  }
  @Override
  public void windowClosing( WindowEvent e ) { System.out.println("nix tun"); 
  
  Mbox.Infobox("please don´t touch this button");
  frame_glob.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
  
  
  }
}