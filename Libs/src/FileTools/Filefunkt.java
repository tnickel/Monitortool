package FileTools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import gui.JLibsProgressWin;
import hiflsklasse.Tracer;

public class Filefunkt
{
	
	private BufferedInputStream in = null;
	private BufferedOutputStream out = null;
	
	public void copyDirAllSqxProjektname(File quelle, File ziel, String projectname)
			throws FileNotFoundException, IOException
	// kopiert alle sqx aus dem quelldir nach zieldir, in den zielnamen wird der
	// projectname eingebaut
	// bsp für zielfile Q63 GBPUSD L0_+00000_portfolio.sqx
	{
		int j = 0;
		File[] files = quelle.listFiles();
		
		// no files
		if ((files == null) || (quelle.getName().contains("Builder")) || (quelle.getName().contains("Retester"))
				|| (quelle.getName().contains("Optimizer")))
		{
			Tracer.WriteTrace(20, "I:I don´t collect <" + quelle.getAbsolutePath() + ">");
			return;
		}
		for (File file : files)
		{
			
			j++;
			if (file.isDirectory())
			{
				copyDirAllSqxProjektname(file,
						new File(ziel.getAbsolutePath() + System.getProperty("file.separator") + file.getName()),
						projectname);
			} else
			{
				// cut "Merged portfolio" out of the name
				copyFile(file, new File(
						ziel.getAbsolutePath() + System.getProperty("file.separator") + projectname + file.getName()));
			}
		}
		
	}
	
	public void copyDir(File quelle, File ziel) throws FileNotFoundException, IOException
	{
		
		File[] files = quelle.listFiles();
		
		ziel.mkdirs();
		if (files == null)
			return;
		// progress slider
		JLibsProgressWin prog = new JLibsProgressWin(
				"copy files from <" + quelle.getName() + "> to <" + ziel.getName() + ">", 0, files.length);
		int j = 0;
		for (File file : files)
		{
			prog.update(j);
			j++;
			if (file.isDirectory())
			{
				copyDir(file, new File(ziel.getAbsolutePath() + System.getProperty("file.separator") + file.getName()));
			} else
			{
				copyFile(file,
						new File(ziel.getAbsolutePath() + System.getProperty("file.separator") + file.getName()));
			}
		}
		prog.end();
	}
	
	public void copyFile(File file, File ziel) throws FileNotFoundException, IOException
	{
		if (ziel.exists())
			ziel.delete();
			
		// System.out.println("Copy " + file.getAbsolutePath() + " to " +
		// ziel.getAbsolutePath());
		in = new BufferedInputStream(new FileInputStream(file));
		out = new BufferedOutputStream(new FileOutputStream(ziel, true));
		int bytes = 0;
		while ((bytes = in.read()) != -1)
		{
			out.write(bytes);
		}
		in.close();
		out.close();
	}
	
	public static void copyFileUsingChannel(File source, File dest) throws IOException
	{
		FileChannel sourceChannel = null;
		FileChannel destChannel = null;
		try
		{
			sourceChannel = new FileInputStream(source).getChannel();
			destChannel = new FileOutputStream(dest).getChannel();
			destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
		} finally
		{
			sourceChannel.close();
			destChannel.close();
		}
	}
	
	public static void deleteSubDir(File path,String keyword)
	{
		//löscht alle unterverzeichnisse wenn im path ein bestimmtes keyword drin ist
		for (File file : path.listFiles())
		{
			if (file.isDirectory())
				deleteSubDir(file,keyword);
			if(path.getAbsolutePath().contains(keyword))
				file.delete();
		}
		
	}
	
}
