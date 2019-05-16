package jHilfsfenster;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class SelectFile2Strategy
{
	
	private static File lastfile=null;
	
	static public String mainx(String[] args)
	{
		JFileChooser fc = new JFileChooser();

		if(lastfile!=null)
			fc.setCurrentDirectory(lastfile);
		fc.setFileFilter(new FileFilter()
		{
			@Override
			public boolean accept(File f)
			{
				return f.isDirectory()
						|| f.getName().toLowerCase().endsWith(".str")
						|| f.getName().toLowerCase().endsWith(".htm");
			}

			@Override
			public String getDescription()
			{
				return ".str .htm";
			}
		});
		int state = fc.showOpenDialog(null);
		if (state == JFileChooser.APPROVE_OPTION)
		{
			File file = fc.getSelectedFile();
			lastfile=file;
			
			
			return (file.getAbsolutePath());
		} else
			System.out.println("Auswahl abgebrochen");

		return null;
	}
}
