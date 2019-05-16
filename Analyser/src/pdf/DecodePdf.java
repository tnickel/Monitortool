package pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;



public class DecodePdf
{
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
	
			
			//Input lesen
			File datei = new File("c:\\temp\\input.pdf");
			FileInputStream fis = new FileInputStream(datei);

			byte[] temp = new byte[(int)datei.length()];
			fis.read(temp,0,(int)datei.length());
			//Konvertierung Input in Base64
			String anhangb64 = new BASE64Encoder().encode(temp); 
			//Base64 decodieren
			byte[] zielinhalt = new BASE64Decoder().decodeBuffer( anhangb64 );
			//Output schreiben
			FileOutputStream fos = new FileOutputStream(new File("c:\\temp\\output.pdf"));
			fos.write(zielinhalt); 
			
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

}
