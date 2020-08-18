package FileTools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import hiflsklasse.Tracer;

public class ProjZipper
{
	//class to handle ziped project files
	//source https://www.straub.as/java/io/io10.html
	private ZipFile zipFile_glob=null;
	private String zipfilename_glob=null;
	private byte[] buffer_glob=null;
	private BufferedReader br_glob = null;
	
	public ProjZipper(String filename)
	{
		try
		{
		   zipfilename_glob=filename;
		   zipFile_glob = new ZipFile(filename) ;
		   ReadZipMem("config.xml");
		}
		catch(IOException ex)
		{
		   Tracer.WriteTrace(10, "Problem reading zip file <"+filename+">");
		}
	}
	
	public void ShowFiles()
	{
		Enumeration<? extends ZipEntry> enu = zipFile_glob.entries()  ;

		while (enu.hasMoreElements())
		{
		   ZipEntry zipEntry = (ZipEntry)enu.nextElement() ;
		   System.out.println("Dateiname    " +zipEntry.getName());
		   System.out.println("Dateigroesse " + zipEntry.getSize());
		   System.out.println("komprimiert  " + zipEntry.getCompressedSize());
		}
	}

	private void ReadZipMem(String fname)
	{
		//read zipfile in memory
		
		Enumeration<? extends ZipEntry> enu = zipFile_glob.entries()  ;

		while (enu.hasMoreElements())
		{
		   ZipEntry zipEntry = (ZipEntry)enu.nextElement() ;
		   if(zipEntry.getName().contains(fname))
			   ReadMemBinaer(zipEntry);
		}
		Tracer.WriteTrace(20, "zipentry<"+fname+"> loaded <"+zipfilename_glob+">");
	}
	
	private void ReadMemBinaer(ZipEntry zipEntry)
	{
		
		BufferedInputStream bis = null;
		try
		{
		   bis = new BufferedInputStream( zipFile_glob.getInputStream(zipEntry) );
		   
		   int avail = bis.available();
		   if ( avail>0 )
		   {
		      buffer_glob = new byte[avail] ;
		      bis.read(buffer_glob, 0, avail) ;
		   }
		  
		}
		catch(IOException ex)
		{
		  Tracer.WriteTrace(10, "zipentry<"+zipEntry.getName()+"> not found in <"+zipfilename_glob+">");
		}
		finally
		{
		   try
		   {
		      if(bis!=null) bis.close();
		   }
		   catch(Exception ex)
		   {
			   Tracer.WriteTrace(10, "zipentry<"+zipEntry.getName()+"> not found in <"+zipfilename_glob+"> cant close");
		   }
		}
		//error
		
	}
  public void writeFile(String fileNameoutput)
  {
	BufferedOutputStream bos = null;
	try
	{
	   
	   bos = new BufferedOutputStream( new FileOutputStream(fileNameoutput) );
	   bos.write(buffer_glob, 0, buffer_glob.length)  ;
	}
	catch(IOException ex)
	{
	   //...
	}
	finally
	{
	   try
	   {
	      if(bos!=null) bos.close();
	   }
	   catch(Exception ex)
	   {
	   }
	}
	
  }
}
