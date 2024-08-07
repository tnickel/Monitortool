package csvtools;

import java.io.*;
import java.util.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

public class CSVProcessor
{
	private List<String[]> data;
	private String[] header;
	
	public CSVProcessor(String filePath) throws IOException, CsvValidationException
	{
		//Der Code liest eine attributsmatrix von einer CSV datei ein.
		//aus der Datei sollen bestimmte attribute entfernt werden
		//Es gibt eine Liste von Attributen die bleiben sollen, die nicht auf der Liste stehen sollen entfernt werden
		//Anschliessend soll die matrix wieder auf platte gespeichert werden.
		
		this.data = new ArrayList<>();
		try (CSVReader reader = new CSVReader(new FileReader(filePath)))
		{
			this.header = reader.readNext(); // Read the header line
			String[] line;
			while ((line = reader.readNext()) != null)
			{
				this.data.add(line);
			}
		}
	}
	
	public void filterAttributes(List<String> attributesToKeep)
	{
		Set<String> attributesSet = new HashSet<>(attributesToKeep);
		List<Integer> indicesToKeep = new ArrayList<>();
		//das erste Attribut muss immer rein, das ist der Strategy_name
		indicesToKeep.add(0);
		for (int i = 0; i < header.length; i++)
		{
			if (attributesSet.contains(header[i]))
			{
				indicesToKeep.add(i);
			}
		}
		//Weka_endtest soll auch drin bleiben
		indicesToKeep.add(header.length-1);
		// Create the new header
		String[] newHeader = new String[indicesToKeep.size()];
		for (int i = 0; i < indicesToKeep.size(); i++)
		{
			newHeader[i] = header[indicesToKeep.get(i)];
		}
		
		// Create the new data list
		List<String[]> newData = new ArrayList<>();
		newData.add(newHeader); // Add the new header as the first row
		
		for (String[] row : this.data)
		{
			String[] newRow = new String[indicesToKeep.size()];
			for (int i = 0; i < indicesToKeep.size(); i++)
			{
				newRow[i] = row[indicesToKeep.get(i)];
			}
			newData.add(newRow);
		}
		
		this.data = newData;
	}
	
	public void saveToFile(String filePath) throws IOException
	{
		try (CSVWriter writer = new CSVWriter(new FileWriter(filePath)))
		{
			writer.writeAll(this.data);
		}
	}
	
	public static void main(String[] args)
	{
		try
		{
			CSVProcessor processor = new CSVProcessor(
					"C:\\forex\\Metrikanalyser\\AR\\Q107 EURUSD M5 v1.41 Weka10\\Q107 EURUSD M5 v1.41 weka10_+00000\\exported_for_weka.csv");
			List<String> attributesToKeep = Arrays.asList("Strategy_Name", "Fitness_(IS)1", "Net_profit_(IS)1"); // Replace
																													// with
																													// your
																													// actual
																													// attribute
																													// names
			processor.filterAttributes(attributesToKeep);
			processor.saveToFile(
					"C:\\forex\\Metrikanalyser\\AR\\Q107 EURUSD M5 v1.41 Weka10\\Q107 EURUSD M5 v1.41 weka10_+00000\\exported_for_weka_short.csv");
		} catch (IOException | CsvValidationException e)
		{
			e.printStackTrace();
		}
	}
}
