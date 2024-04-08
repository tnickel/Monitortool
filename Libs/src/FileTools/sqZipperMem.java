package FileTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class sqZipperMem
{
	
	private final String fileName;
	private final long size;
	private final long compressedSize;
	private File content;
	
	public sqZipperMem(String fileName, long size, long compressedSize)
	{
		this.fileName = fileName;
		this.size = size;
		this.compressedSize = compressedSize;
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	public long getSize()
	{
		return size;
	}
	
	public long getCompressedSize()
	{
		return compressedSize;
	}
	
	public File getContent()
	{
		return content;
	}
	
	public void setContent(File content)
	{
		this.content = content;
	}
	
	public void writeToFile(String destinationPath) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(destinationPath);
		ZipOutputStream zipOut = new ZipOutputStream(fos);
		zipOut.putNextEntry(new ZipEntry(fileName));
		if (content.isFile())
		{
			FileInputStream fis = new FileInputStream(content);
			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0)
			{
				zipOut.write(bytes, 0, length);
			}
			fis.close();
		} else if (content.isDirectory())
		{
			for (File file : content.listFiles())
			{
				sqZipperMem zipFile = new sqZipperMem(file.getPath(), file.length(), 0);
				zipFile.setContent(file);
				zipFile.writeToFile(file.getName());
				FileInputStream fis = new FileInputStream(file.getName());
				byte[] bytes = new byte[1024];
				int length;
				while ((length = fis.read(bytes)) >= 0)
				{
					zipOut.write(bytes, 0, length);
				}
				fis.close();
			}
		}
		zipOut.closeEntry();
		zipOut.close();
		fos.close();
	}
	
	public static List<sqZipperMem> read(String zipFilePath) throws IOException
	{
		//liest ein ganzes Verzeichniss ein und übergibt dann die eingelesene liste zurück
		List<sqZipperMem> fileList = new ArrayList<>();
		FileInputStream fis = new FileInputStream(zipFilePath);
		ZipInputStream zipIn = new ZipInputStream(fis);
		ZipEntry entry = zipIn.getNextEntry();
		while (entry != null)
		{
			if (!entry.isDirectory())
			{
				sqZipperMem zipFile = getZipFileFromZipEntry(entry, zipIn);
				fileList.add(zipFile);
			} else
			{
				File dir = new File(entry.getName());
				dir.mkdir();
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();
		fis.close();
		return fileList;
	}
	
	private static sqZipperMem getZipFileFromZipEntry(ZipEntry entry, ZipInputStream zipIn) throws IOException
	{
		//holt die daten für ein bestimmten zip-entry
		String fileName = entry.getName();
		long size = entry.getSize();
		long compressedSize = entry.getCompressedSize();
		sqZipperMem zipperMem = new sqZipperMem(fileName, size, compressedSize);
		File file = new File(fileName);
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		byte[] bytes = new byte[1024];
		int length;
		while ((length = zipIn.read(bytes)) >= 0)
		{
			fos.write(bytes, 0, length);
		}
		zipperMem.setContent(file);
		zipIn.closeEntry();
		fos.close();
		return zipperMem;
	}
}
