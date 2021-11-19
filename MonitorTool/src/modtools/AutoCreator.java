package modtools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import data.Metaconfig;
import hiflsklasse.FileAccess;
import hiflsklasse.Tracer;

public class AutoCreator
{
	static public void copyToAutoCreator(Metaconfig meconf,String comment,String newName,JFreeChart chart)
	{
		String appdata=meconf.getAppdata();
				
		//commentpref=10003
		
		//sourcdir_s=D:\Forex\mt4\Pepperstone\tester\files\AC_Entwicklungen\10003
		String sourcedir_s=appdata+"\\tester\\files\\AC_Entwicklungen\\"+calcCommentPrefix(comment);
		
		copyThreeAutoFiles(sourcedir_s,appdata+"\\tester\\files",comment,newName);
		copyThreeAutoFiles(sourcedir_s,appdata+"\\MQL4\\files",comment,newName);
		
		saveChartPng(appdata+"\\tester\\files\\Speicherort_sys_pics",comment,newName,chart);
	}
	static private void copyThreeAutoFiles(String sourcedir_s, String destdir_s,String comment,String newName)
	{
		String commentpostfix=calcCommentPostfix(comment);
		String fsource_csv=sourcedir_s+"\\"+commentpostfix+".csv";
		String fsource_set=sourcedir_s+"\\"+commentpostfix+"_set.csv";
		String fsource_stats=sourcedir_s+"\\"+commentpostfix+"_stats.csv";
		
		FileAccess.copyFile(fsource_set,destdir_s+"\\Speicherort_Sys_Gruppe_A\\"+newName+".csv");
		FileAccess.copyFile(fsource_stats,destdir_s+"\\Speicherort_Sys_Gruppe_A_Stats\\"+newName+"_stats.csv");
	}
	static String calcCommentPrefix(String comment)
	{
		String commentpref=comment.substring(0,comment.indexOf("_"));
		return commentpref;
	}
	public static String calcCommentPostfix(String comment)
	{
		String commentpostfix=comment.substring(comment.indexOf("_")+1);
		return commentpostfix;
	}
	static private void saveChartPng(String destdir_s,String comment,String newName,JFreeChart chart)
	{
		String outfile=destdir_s+"\\"+newName+".png";
		File outdir=new File(destdir_s);
		if(outdir.exists()==false)
			outdir.mkdir();
		
		try {

		    OutputStream out = new FileOutputStream(outfile);
		    ChartUtilities.writeChartAsPNG(out,
		            chart,
		            1000,
		            500);
		    out.close();
		} catch (IOException ex) {
		    Tracer.WriteTrace(20, "Error:4545454545");
		}
	}
	public  JPanel readPanelPng(String filename)
	{
		/*JPanel jPanel = new JPanel();      
		jPanel.add(new JLabel(new ImageIcon(this.getClass().getClassLoader().getResource(filename))));
		return jPanel;
	*/
		JPanel jPanel = new JPanel(); 
		
		BufferedImage myPicture;
		try
		{
			myPicture = ImageIO.read(new File(filename));
			JLabel picLabel = new JLabel(new ImageIcon(myPicture));
			jPanel.add(picLabel);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return jPanel;
		
		
	}
	
	
}
