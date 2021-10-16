package modtools;

import data.Metaconfig;
import hiflsklasse.FileAccess;

public class AutoCreator
{
	static public void copyToAutoCreator(Metaconfig meconf,String comment,String newName)
	{
		String appdata=meconf.getAppdata();
				
		//commentpref=10003
		
		//sourcdir_s=D:\Forex\mt4\Pepperstone\tester\files\AC_Entwicklungen\10003
		String sourcedir_s=appdata+"\\tester\\files\\AC_Entwicklungen\\"+calcCommentPrefix(comment);
		
		copyThreeAutoFiles(sourcedir_s,appdata+"\\tester\\files",comment,newName);
		copyThreeAutoFiles(sourcedir_s,appdata+"\\MQL4\\files",comment,newName);
	}
	static private void copyThreeAutoFiles(String sourcedir_s, String destdir_s,String comment,String newName)
	{
		String commentpostfix=calcCommentPostfix(comment);
		String fsource_csv=sourcedir_s+"\\"+commentpostfix+".csv";
		String fsource_set=sourcedir_s+"\\"+commentpostfix+"_set.csv";
		String fsource_stats=sourcedir_s+"\\"+commentpostfix+"_stats.csv";
		
		FileAccess.copyFile(fsource_csv,destdir_s+"\\Speicherort_Sys_Gruppe_A\\"+newName+".csv");
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
}
