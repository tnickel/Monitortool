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
import hiflsklasse.Inf;
import hiflsklasse.Tracer;

public class AutoCreator
{
	
	static public String getPkz1(Metaconfig meconf, String comment)
	{
		String appdata=meconf.getAppdata();
		String sourcedir_s=appdata+"\\tester\\files\\AC_Entwicklungen\\"+calcCommentPrefix(comment);
		String commentpostfix=calcCommentPostfix(comment);
		String fsource_stats=sourcedir_s+"\\"+commentpostfix+"_stats.csv";

		if(new File(fsource_stats).exists()==false)
			return("nostat");
		
		Inf inf = new Inf();
		inf.setFilename(fsource_stats);
		String mem=inf.readMemFile(1500);
		inf.close();
		if(mem.contains("PZ_01;"))
		{
			mem=mem.substring(mem.indexOf("PZ_01;")+6);
			System.out.println("mem="+mem);
			
			if(mem.contains("New"))
				mem=mem.substring(0,3);
			else
				return "nonew";
			
			
		}
		else return "nopz1";
		
		return mem;
	}
	
	static public void delteEaAutoCreatorFiles(Metaconfig meconf,String comment)
	{
		String appdata=meconf.getAppdata();
		
		//commentpref=10003
		//sourcdir_s=D:\Forex\mt4\Pepperstone\tester\files\AC_Entwicklungen\10003
		String sourcedir_s=appdata+"\\tester\\files\\AC_Entwicklungen\\"+calcCommentPrefix(comment);
		
		deleteThreeAutoFiles(sourcedir_s,appdata+"\\tester\\files",comment);
		deleteThreeAutoFiles(sourcedir_s,appdata+"\\MQL4\\files",comment);
	}
	
	
	static public void copyToAutoCreator(Metaconfig meconf,String comment,String newName,JFreeChart chart)
	{
		String appdata=meconf.getAppdata();
				
		//commentpref=10003
		
		//sourcdir_s=D:\Forex\mt4\Pepperstone\tester\files\AC_Entwicklungen\10003
		String sourcedir_s=appdata+"\\tester\\files\\AC_Entwicklungen\\"+calcCommentPrefix(comment);
		
		copyThreeAutoFiles(sourcedir_s,appdata+"\\tester\\files",comment,newName);
		copyThreeAutoFiles(sourcedir_s,appdata+"\\MQL4\\files",comment,newName);
		
		//del delcomfile
		File f=new File(sourcedir_s,appdata+"\\MQL4\\files\\"+comment+".delcom");
		if(f.exists())
			f.delete();
		
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
	static private void deleteThreeAutoFiles(String sourcedir_s, String destdir_s,String comment)
	{
		
		String fnam=destdir_s+"\\Speicherort_Sys_Gruppe_A\\"+comment+".csv";
		
		File tfile=new File(fnam);
		if(tfile.exists()==false)
			Tracer.WriteTrace(10, "E:File <"+fnam+"> not available to delete");
		else
			tfile.delete();
		
		fnam=destdir_s+"\\Speicherort_Sys_Gruppe_A_Stats\\"+comment+"_stats.csv";
		if(tfile.exists()==true)
			tfile.delete();	
	}
	
	
	static String calcCommentPrefix(String comment)
	{
		String comment_tmp=comment;//example 1008_328
		int anz = comment_tmp.length() - comment_tmp.replace("_", "").length();
		
		String commentpref="";
		
		if(anz==1)
		{	
		 commentpref=comment.substring(0,comment.indexOf("_"));
		}
		else if(anz==2)
		{
			//take the middle part
			 commentpref=comment.substring(comment.indexOf("_")+1);
			 commentpref=commentpref.substring(0,commentpref.indexOf("_"));
			 //return 1008
		}
		else if(anz==0)
			commentpref=comment;
		else if(anz==3)
		{
			//take the middle part
			 commentpref=comment.substring(comment.indexOf("_")+1);
			 commentpref=commentpref.substring(commentpref.indexOf("_")+1);
			 commentpref=commentpref.substring(0,commentpref.indexOf("_"));
			
		}
		else	
			Tracer.WriteTrace(10, "comment<"+comment+"> to much _ , max allowed =2");
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
