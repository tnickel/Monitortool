package gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;

public class CfxDateViewer {

    // Map to store the taskXMLFile to task title mapping
    private static LinkedHashMap<String, String> taskTitleMap = new LinkedHashMap<>();

    public static void processCfxFile(Display display, String cfxFilePath) throws Exception {
        String outputDir = "extracted_cfx";

        try {
            // Unzip the CFX file
            unzip(cfxFilePath, outputDir);

            // Load the task title mappings from config.xml in the correct order
            loadTaskTitlesFromConfig(outputDir + "/config.xml");

            // Process the extracted XML files
            List<Map<String, String>> results = processXmlFiles(outputDir);

            // Display the results in an SWT table in the correct order
            displayResults(display, results);

        } finally {
            // Clean up extracted files
            deleteDirectoryRecursively(new File(outputDir));
        }
    }

    public static void loadTaskTitlesFromConfig(String configFilePath) throws Exception {
        File configFile = new File(configFilePath);
        if (configFile.exists()) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(configFile);
            doc.getDocumentElement().normalize();

            NodeList taskNodes = doc.getElementsByTagName("Task");
            for (int i = 0; i < taskNodes.getLength(); i++) {
                Element taskElement = (Element) taskNodes.item(i);
                String taskXMLFile = taskElement.getAttribute("taskXMLFile");
                String taskTitle = taskElement.getAttribute("title");
                if (!taskXMLFile.isEmpty() && !taskTitle.isEmpty()) {
                    taskTitleMap.put(taskXMLFile, taskTitle);  // Store in LinkedHashMap to preserve order
                }
            }
        }
    }

    public static void unzip(String zipFilePath, String destDir) throws IOException {
        File dir = new File(destDir);
        if (!dir.exists()) dir.mkdirs();
        byte[] buffer = new byte[1024];
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = newFile(dir, zipEntry);
                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
        return destFile;
    }

    public static List<Map<String, String>> processXmlFiles(String dir) throws Exception {
        List<Map<String, String>> results = new ArrayList<>();
        File folder = new File(dir);
        File[] listOfFiles = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".xml");
            }
        });

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (!file.getName().equals("config.xml")) {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(file);
                    doc.getDocumentElement().normalize();

                    NodeList setupNodes = doc.getElementsByTagName("Setup");
                    NodeList rangeNodes = doc.getElementsByTagName("Range");

                    if (setupNodes.getLength() > 0 || rangeNodes.getLength() > 0) {
                        extractDatesFromNodes(file.getName(), setupNodes, results);
                        extractDatesFromNodes(file.getName(), rangeNodes, results);
                    } else {
                        // If no dates are found, add a noDate entry
                        addNoDateEntry(file.getName(), results);
                    }
                }
            }
        }
        return results;
    }

    public static void extractDatesFromNodes(String fileName, NodeList nodeList, List<Map<String, String>> results) {
        String taskTitle = taskTitleMap.getOrDefault(fileName, "Unknown Task");
        boolean hasDates = false;

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            String dateFrom = element.getAttribute("dateFrom");
            String dateTo = element.getAttribute("dateTo");

            if (!dateFrom.isEmpty() || !dateTo.isEmpty()) {
                Map<String, String> result = new HashMap<>();
                result.put("taskTitle", taskTitle);  // Use taskTitle instead of taskName
                result.put("file", fileName);
                result.put("tag", element.getTagName());
                result.put("dateFrom", dateFrom.isEmpty() ? "noDate" : dateFrom);
                result.put("dateTo", dateTo.isEmpty() ? "noDate" : dateTo);
                results.add(result);
                hasDates = true;
            }
        }

        // If no dates were found, add a noDate entry
        if (!hasDates) {
            addNoDateEntry(fileName, results);
        }
    }

    public static void addNoDateEntry(String fileName, List<Map<String, String>> results) {
        String taskTitle = taskTitleMap.getOrDefault(fileName, "Unknown Task");
        Map<String, String> result = new HashMap<>();
        result.put("taskTitle", taskTitle);  // Use taskTitle instead of taskName
        result.put("file", fileName);
        result.put("tag", "NoTag");
        result.put("dateFrom", "noDate");
        result.put("dateTo", "noDate");
        results.add(result);
    }

    public static void displayResults(Display display, final List<Map<String, String>> results) {
        Shell shell = new Shell(display);
        shell.setText("CFX Analyzer Results (Experimental)");  // Updated title to include "Experimental"
        shell.setSize(600, 400);
        shell.setLayout(new FillLayout());

        Table table = new Table(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        // Colors
        Color darkGreen = display.getSystemColor(SWT.COLOR_DARK_GREEN);
        Color black = display.getSystemColor(SWT.COLOR_BLACK);
        Color white = display.getSystemColor(SWT.COLOR_WHITE);

        // Add a new column for Task Title with numbers
        String[] titles = {"#", "Task Title", "File", "Tag", "DateFrom", "DateTo"};
        for (String title : titles) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText(title);
        }

        // Add numbering to tasks
        int taskNumber = 1;

        // Iterate over the taskTitleMap to ensure the correct order
        for (Map.Entry<String, String> entry : taskTitleMap.entrySet()) {
            String taskXMLFile = entry.getKey();
            String taskTitle = entry.getValue();
            boolean fileFound = false;

            for (Map<String, String> result : results) {
                if (result.get("file").equals(taskXMLFile)) {
                    TableItem item = new TableItem(table, SWT.NONE);
                    item.setText(0, String.valueOf(taskNumber));  // Add task number (1 to 22)
                    item.setText(1, taskTitle);  // Display task title
                    item.setText(2, result.get("file"));
                    item.setText(3, result.get("tag"));
                    item.setText(4, result.get("dateFrom"));
                    item.setText(5, result.get("dateTo"));

                    // Apply color based on date presence
                    if (result.get("dateFrom").equals("noDate") && result.get("dateTo").equals("noDate")) {
                        item.setBackground(black);
                        item.setForeground(white);  // Ensure text is visible on black background
                    } else {
                        item.setBackground(darkGreen);
                        item.setForeground(white);  // Ensure text is visible on dark green background
                    }
                    fileFound = true;
                }
            }

            // If no corresponding file was found, display "noFile" and "noDate"
            if (!fileFound) {
                TableItem item = new TableItem(table, SWT.NONE);
                item.setText(0, String.valueOf(taskNumber));  // Add task number
                item.setText(1, taskTitle);  // Display task title
                item.setText(2, "noFile");  // No file found
                item.setText(3, "NoTag");
                item.setText(4, "noDate");
                item.setText(5, "noDate");

                item.setBackground(black);
                item.setForeground(white);  // Ensure text is visible on black background
            }

            taskNumber++;  // Increment task number for each task
        }

        for (int i = 0; i < titles.length; i++) {
            table.getColumn(i).pack();
        }

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        shell.dispose();  // Dispose the shell, but do not dispose the Display
    }

    public static void deleteDirectoryRecursively(File file) throws IOException {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                deleteDirectoryRecursively(child);
            }
        }
        if (!file.delete()) {
            throw new IOException("Failed to delete file: " + file.getAbsolutePath());
        }
    }
}
