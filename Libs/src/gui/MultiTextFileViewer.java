package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

public class MultiTextFileViewer
{
	  public void showAll(ArrayList<String> filePaths, Shell parentShell) {
	        // Erstellen einer neuen Shell
	        Shell shell = new Shell(parentShell, SWT.SHELL_TRIM);
	        shell.setText("Multi Text File Viewer");
	        shell.setSize(800, 600);
	        shell.setLayout(new FillLayout());

	        // TabFolder erstellen
	        TabFolder tabFolder = new TabFolder(shell, SWT.BORDER);

	        // Tabs für jede Datei erstellen
	        for (String filePath : filePaths) {
	            createTabForFile(tabFolder, filePath);
	        }

	        shell.open();
	    }

	    private static void createTabForFile(TabFolder tabFolder, String filePath) {
	        // Den Dateinamen ohne Pfad extrahieren
	        String fileName = new File(filePath).getAbsolutePath();
	        fileName=fileName.substring(fileName.lastIndexOf(" "),fileName.length());
	        //fileName=fileName.substring(fileName.lastIndexOf("\\"));
	        
	        // TabItem erstellen
	        TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
	        tabItem.setText(fileName);

	      
	        
	        // Text-Widget erstellen
	        Text text = new Text(tabFolder, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	        text.setEditable(false);
	        tabItem.setControl(text);
	        
	        
	      
	        

	        // Dateiinhalt einlesen und im Text-Widget anzeigen
	        StringBuilder fileContent = new StringBuilder();
	        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                fileContent.append(line).append(System.lineSeparator());
	            }
	            text.setText(fileContent.toString());
	        } catch (IOException e) {
	            text.setText("Error loading file: " + filePath);
	        }
	    }
	}


