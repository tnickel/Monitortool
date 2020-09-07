package FileTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zipper
{
	static public void ZipMultipleFiles(List<String> srcFiles, String destfile) throws IOException
	{
		// zippt die sourcefiles ins destfile
		//
		// List<String> srcFiles = Arrays.asList("test1.txt", "test2.txt");
		// destfile="multiCompressed.zip"
		
		FileOutputStream fos = new FileOutputStream(destfile);
		ZipOutputStream zipOut = new ZipOutputStream(fos);
		for (String srcFile : srcFiles)
		{
			File fileToZip = new File(srcFile);
			FileInputStream fis = new FileInputStream(fileToZip);
			ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
			zipOut.putNextEntry(zipEntry);
			
			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0)
			{
				zipOut.write(bytes, 0, length);
			}
			fis.close();
		}
		zipOut.close();
		fos.close();
	}
	
	static public void ZipFileArchive(String srcFile, String archivefile) throws IOException
	{
		// nimmt das srcFile und baut ein archivefile auf, srcFile wird anschlissend ins
		// archive gepackt
		//
		// List<String> srcFiles = Arrays.asList("test1.txt", "test2.txt");
		// destfile="multiCompressed.zip"
		
		FileOutputStream fos = new FileOutputStream(archivefile);
		ZipOutputStream zipOut = new ZipOutputStream(fos);
		
		File fileToZip = new File(srcFile);
		FileInputStream fis = new FileInputStream(fileToZip);
		ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
		zipOut.putNextEntry(zipEntry);
		
		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0)
		{
			zipOut.write(bytes, 0, length);
		}
		fis.close();
		
		zipOut.close();
		fos.close();
	}
	
}
