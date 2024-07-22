package hiflsklasse;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileAccess2 {
    private BufferedWriter bw;
    private String currentFilename;
    
    public boolean appendZeile(String filename, String zeile, boolean carriageReturn) {
        if (filename == null || zeile == null) {
            throw new IllegalArgumentException("Filename and zeile must not be null");
        }
        
        try {
            if (bw == null || !filename.equals(currentFilename)) {
                close(); // Close previous BufferedWriter if filename changes
                bw = openFileForAppend(filename);
                currentFilename = filename;
            }
            bw.write(zeile);
            if (carriageReturn) {
                bw.newLine();
            }
            bw.flush(); // Ensure content is written immediately
        } catch (IOException e) {
            e.printStackTrace();
            Tracer.WriteTrace(10, "E:IOException occurred while writing to file: " + e.getMessage());
            return false;
        }
        
        return true;
    }
    
    public void close() {
        if (bw != null) {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
                Tracer.WriteTrace(10, "E:IOException occurred while closing the BufferedWriter: " + e.getMessage());
            } finally {
                bw = null; // Ensure BufferedWriter is set to null after closing
                currentFilename = null;
            }
        }
    }
    
    private BufferedWriter openFileForAppend(String filename) throws IOException {
        return new BufferedWriter(new FileWriter(filename, true));
    }
}

