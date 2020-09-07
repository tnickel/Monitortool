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

public class Filefunkt
{

	private BufferedInputStream in = null;
	private BufferedOutputStream out = null;

	public void copyDir(File quelle, File ziel) throws FileNotFoundException,
			IOException
	{

		File[] files = quelle.listFiles();

		ziel.mkdirs();
		if (files == null)
			return;
		// progress slider
		JLibsProgressWin prog = new JLibsProgressWin(
				"copy files from <"+quelle.getName()+"> to <"+ziel.getName()+">", 0, files.length);
		int j=0;
		for (File file : files)
		{
			prog.update(j);
			j++;
			if (file.isDirectory())
			{
				copyDir(file,
						new File(ziel.getAbsolutePath()
								+ System.getProperty("file.separator")
								+ file.getName()));
			} else
			{
				copyFile(
						file,
						new File(ziel.getAbsolutePath()
								+ System.getProperty("file.separator")
								+ file.getName()));
			}
		}
		prog.end();
	}

	public void copyFile(File file, File ziel) throws FileNotFoundException,
			IOException
	{
		if(ziel.exists())
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
	public static  void copyFileUsingChannel(File source, File dest) throws IOException {
	    FileChannel sourceChannel = null;
	    FileChannel destChannel = null;
	    try {
	        sourceChannel = new FileInputStream(source).getChannel();
	        destChannel = new FileOutputStream(dest).getChannel();
	        destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
	       }finally{
	           sourceChannel.close();
	           destChannel.close();
	   }
	}
}
