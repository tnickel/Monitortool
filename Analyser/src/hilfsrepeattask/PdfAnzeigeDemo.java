package hilfsrepeattask;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PagePanel;

public class PdfAnzeigeDemo
{
	// klasse zur Behandlung von Pdf-Files
	public static void setup() throws IOException
	{
		// set up the frame and panel
		JFrame frame = new JFrame("PDF Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		PagePanel panel = new PagePanel();
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);

		// load a pdf from a byte buffer
		File file = new File("F:\\offline_test\\aktien_pdf\\NFC_2009_44_20091015_Ausgabe.pdf");
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel
				.size());
		PDFFile pdffile = new PDFFile(buf);

		// show the first page
		PDFPage page = pdffile.getPage(2);
	
		panel.showPage(page);

	}

	public static void main(final String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					setup();
				} catch (IOException ex)
				{
					ex.printStackTrace();
				}
			}
		});
	}

}
