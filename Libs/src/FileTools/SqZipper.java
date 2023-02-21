package FileTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import hiflsklasse.Tracer;

public class SqZipper
{
	// unzipped ein ganzes Verzeichniss
	
	public  void unzip(String zipFilePath, String destDirectory) throws IOException 
	    {
	        File destDir = new File(destDirectory);
	        if (!destDir.exists()) 
	        {
	            destDir.mkdir();
	        }
	        else
	        {
	        	//exists already, than clean it
	        	
	    		File[] LIST = destDir.listFiles();
	    		for (File FILE : LIST)
	    		{
	    			if(FILE.getAbsoluteFile().getAbsolutePath().contains("tmp\\sq"))
	    				if(FILE.delete()==false)
	    					Tracer.WriteTrace(10, "Error1445: cant delete <"+FILE.getAbsolutePath()+">");
	    		}
	        	
	        }
	        
	        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
	        ZipEntry entry = zipIn.getNextEntry();
	        while (entry != null) 
	        {
	            String filePath = destDirectory + File.separator + entry.getName();
	            if (!entry.isDirectory()) {
	                extractFile(zipIn, filePath);
	            } else {
	                File dir = new File(filePath);
	                dir.mkdir();
	            }
	            zipIn.closeEntry();
	            entry = zipIn.getNextEntry();
	        }
	        zipIn.close();
    
	    }
	
	private void extractFile(ZipInputStream zipIn, String filePath) throws IOException
	{
		byte[] buffer = new byte[1024];
		File newFile = new File(filePath);
		new File(newFile.getParent()).mkdirs();
		newFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(newFile);
		int len;
		while ((len = zipIn.read(buffer)) > 0)
		{
			fos.write(buffer, 0, len);
		}
		fos.close();
	}
	
	public void zip(String sourceDirectoryPath, String zipFilePath) throws IOException
	{
		// zipped an entire directory
		File sourceDir = new File(sourceDirectoryPath);
		FileOutputStream fos = new FileOutputStream(zipFilePath);
		ZipOutputStream zipOut = new ZipOutputStream(fos);
		zipDirectory(sourceDir, sourceDir.getName(), zipOut);
		zipOut.close();
		fos.close();
	}
	
	private void zipDirectory(File dir, String name, ZipOutputStream zipOut) throws IOException
	{
		File[] files = dir.listFiles();
		for (File file : files)
		{
			if (file.isDirectory())
			{
				zipDirectory(file, name + "/" + file.getName(), zipOut);
				
				continue;
			}
			zipFile(file, name, zipOut);
		}
	}
	
	private void zipFile(File file, String name, ZipOutputStream zipOut) throws IOException
	{
		//ZipEntry zipEntry = new ZipEntry(name + "/" + file.getName());
		ZipEntry zipEntry = new ZipEntry( file.getName());
		zipOut.putNextEntry(zipEntry);
		FileInputStream fis = new FileInputStream(file);
		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0)
		{
			zipOut.write(bytes, 0, length);
		}
		fis.close();
	}
}
