package pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class DecodePdf {
    public static void main(String[] args) {
        try {
            // Input lesen
            File datei = new File("c:\\temp\\input.pdf");
            FileInputStream fis = new FileInputStream(datei);

            byte[] temp = new byte[(int) datei.length()];
            fis.read(temp);
            fis.close();

            // Konvertierung Input in Base64
            String anhangb64 = Base64.getEncoder().encodeToString(temp);

            // Base64 decodieren
            byte[] zielinhalt = Base64.getDecoder().decode(anhangb64);

            // Output schreiben
            FileOutputStream fos = new FileOutputStream(new File("c:\\temp\\output.pdf"));
            fos.write(zielinhalt);
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
