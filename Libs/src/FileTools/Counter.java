package FileTools;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Counter
{
	static public int countLinesInFile(String filePath)
	{
		int lines = 0;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
		{
			while (reader.readLine() != null)
			{
				lines++;
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return lines;
	}
}
