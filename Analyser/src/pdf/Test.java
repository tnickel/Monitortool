package pdf;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PRIndirectReference;
import com.lowagie.text.pdf.PRStream;
import com.lowagie.text.pdf.PRTokeniser;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;

/**
 * The HelloWorld class demonstrates the basic steps of creating an RTF document
 * with iText.
 * 
 * @version $Revision: 2428 $
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 */
public class Test
{
	/**
	 * Hello World! example
	 * 
	 * @param args
	 *            Unused
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{
		System.out.println("Hello World! example for the RTF format");
		try
		{
			// Step 1: Create a new Document
			Document document = new Document();

			// Step 2: Create a new instance of the RtfWriter2 with the document
			// and target output stream.

			// Step 3: Open the document.
			document.open();
			// my escapade
			PdfReader reader = new PdfReader(
					"G:\\Mail\\Pdf\\HEBEL\\2010-1-8_KW_10_01_DERHEBEL.pdf");
			Map info = reader.getInfo();
			System.out.println("Info ----------" + reader.getPageContent(1));
			String key;
			String value;
			for (Iterator i = info.keySet().iterator(); i.hasNext();)
			{
				key = (String) i.next();
				value = (String) info.get(key);
				System.out.println(key + ": " + value);
			}
			if (reader.getMetadata() == null)
			{
				System.out.println("No XML Metadata.");
			} else
			{
				System.out.println("XML Metadata: "
						+ new String(reader.getMetadata()));
			}
			// escapade 2
			PdfDictionary page = reader.getPageN(2);
			PRIndirectReference objectReference = (PRIndirectReference) page
					.get(PdfName.CONTENTS);
			System.out.println("=== inspecting the stream of page 1 in object "
					+ objectReference.getNumber() + " ===");
			PRStream stream = (PRStream) PdfReader
					.getPdfObject(objectReference);
			byte[] streamBytes = PdfReader.getStreamBytes(stream);
			System.out.println(new String(streamBytes));
			System.out
					.println("=== extracting the strings from the stream ===");
			PRTokeniser tokenizer = new PRTokeniser(streamBytes);
			FileOutputStream stream2 = new FileOutputStream(
					"c:\\temp\\HelloWorld.rtf");
			StringBuffer strbufe = new StringBuffer();
			while (tokenizer.nextToken())
			{
				if (tokenizer.getTokenType() == PRTokeniser.TK_STRING)
				{
					System.out.println(tokenizer.getStringValue());
					strbufe.append(tokenizer.getStringValue());
					System.out.println(strbufe);
				}
			}
			String test = strbufe.toString();
			StringReader reader1 = new StringReader(test);
			int t;
			while ((t = reader1.read()) > 0)

				stream2.write(t);
			// Step 4: Add content to the document.
			document.add(new Paragraph("Hello World!"));

			// Step 5: Close the document. It will be written to the target
			// output stream.
			document.close();
		} catch (FileNotFoundException fnfe)
		{
			// It might not be possible to create the target file.
			fnfe.printStackTrace();
		} catch (DocumentException de)
		{
			// DocumentExceptions arise if you add content to the document
			// before opening or
			// after closing the document.
			de.printStackTrace();
		}
	}
}