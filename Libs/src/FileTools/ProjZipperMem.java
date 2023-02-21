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
import sq4xWorkflow.SqDate;

public class ProjZipperMem_depricated
{
	
	//This class read all files in a bufferarray
	
	//source https://www.straub.as/java/io/io10.html
	private ZipFile zipFile_glob=null;
	private String zipfilename_glob=null;
	private byte[][] buffer_glob=null;
	private byte[][] buffer_backup_glob=null;
	private int anzfiles_glob=0;
	private ZipEntry[] zipstore_glob=null;
	private BufferedReader br_glob = null;
	
	public ProjZipperMem_depricated(String filename)
	{
		try
		{
		   zipfilename_glob=filename;
		   zipFile_glob = new ZipFile(filename) ;
		   ReadZipMem();
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

	private void ReadZipMem()
	{
		//read zipfile in memory
		
		Enumeration<? extends ZipEntry> enu = zipFile_glob.entries()  ;
		int index=0;
		while (enu.hasMoreElements())
		{
		   ZipEntry zipEntry = (ZipEntry)enu.nextElement() ;
		    ReadMemBinaer(index,zipEntry);
		    zipstore_glob[index]=zipEntry;
			index++;
		}
		anzfiles_glob=index;
		Tracer.WriteTrace(20, "zipentry loaded <"+zipfilename_glob+">");
	}
	
	private void ReadMemBinaer(int index,ZipEntry zipEntry)
	{
		//the zipped file will be endcoded and stored as bytearry in memory
		BufferedInputStream bis = null;
		try
		{
		   bis = new BufferedInputStream( zipFile_glob.getInputStream(zipEntry) );
		   
		   int avail = bis.available();
		   if ( avail>0 )
		   {
		      buffer_glob[index] = new byte[avail] ;
		      bis.read(buffer_glob[index], 0, avail) ;
		      //store in the backupstore a clone
		      buffer_backup_glob[index]=buffer_glob[index].clone();
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
		
	}
  public void writeFile(String fileNameoutput)
  {
	//the unzipped bytearry will be stored as plain text on harddisk
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
  public void modifyProject(int daysoffset_i)
	{
	  for(int i=0; i<anzfiles_glob; i++)
	  {
		  byte[] memstring_mod_g =buffer_glob[i];
		
		// jetzt wird das datum gesucht und entsprechend modifiziert
		// Suche <Project name="EURUSD - H1 -2year NEW" version="126.2189">
		
		SqConfXml sxml = new SqConfXml(memstring_mod_g);
		sxml.setSearchpattern("<Project name=\"", "\"");
		String projektname = sxml.getProjectName();
		Tracer.WriteTrace(20, "projektname=" + projektname);
		
		sxml.setSearchpattern("<Setup dateFrom=", " test");
		memstring_mod_g = sxml.modifyAllPatterns(daysoffset_i);
		
		sxml.setSearchpattern("<Range dateFrom=", " />");
		memstring_mod_g = sxml.modifyAllPatterns(daysoffset_i);
		
		// replace back X@X
		memstring_mod_g = SqDate.replaceBack(memstring_mod_g);
	  }
	}
  
}
