package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class ProfitTableViewer {

    public void createProfitTable(Display display, String filePath) {
        // Check if the file exists
        File file = new File(filePath);
        if (!file.exists()) {
            // Show message box if the file is not found
            Shell messageShell = new Shell(display, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
            MessageBox messageBox = new MessageBox(messageShell, SWT.ICON_ERROR | SWT.OK);
            messageBox.setText("File Not Found");
            messageBox.setMessage("The specified file could not be found:\n" + filePath);
            messageBox.open();
            return; // Exit the method if the file does not exist
        }

        // Create a new Shell window
        Shell shell = new Shell(display);
        shell.setText("PeriodPredictionTable");
        shell.setSize(800, 600);  // Larger window for a professional look
        shell.setLayout(new FillLayout());

        Table table = new Table(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        double sumAllProfit = 0;
        double sumSelProfit = 0;
        int sumAnzStrat = 0;
        List<Double> allProfits = new ArrayList<>();
        List<Double> selProfits = new ArrayList<>();

        // Create custom colors
        Color darkGreen = new Color(display, 0, 100, 0); // Dark Green
        Color red = display.getSystemColor(SWT.COLOR_RED); // Red
        Color gray = display.getSystemColor(SWT.COLOR_GRAY); // Gray for the background
        Color lightGray = new Color(display, 245, 245, 245); // Light gray for alternating rows
        Color white = display.getSystemColor(SWT.COLOR_WHITE); // White for alternating rows

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();

            // First line is the header, add columns
            if (line != null) {
                String[] headers = line.split("\t");
                for (String header : headers) {
                    TableColumn column = new TableColumn(table, SWT.CENTER);  // Centered column headers
                    column.setText(header);
                    column.setWidth(150);  // Set column width for consistent display
                }
            }

            // Subsequent lines are the data
            int rowIndex = 0;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\t");

                TableItem item = new TableItem(table, SWT.NONE);
                item.setText(data);

                // Alternate row colors
                if (rowIndex % 2 == 0) {
                    item.setBackground(white);
                } else {
                    item.setBackground(lightGray);
                }

                // Sum calculation and gather data for statistics
                try {
                    double allProfit = Double.parseDouble(data[1]);
                    double selProfit = Double.parseDouble(data[2]);
                    int anzStrat = Integer.parseInt(data[3]);

                    sumAllProfit += allProfit;
                    sumSelProfit += selProfit;
                    sumAnzStrat += anzStrat;

                    allProfits.add(allProfit);
                    selProfits.add(selProfit);

                    // Color coding for "allProfitval" and "selProfitval"
                    if (allProfit < 0) {
                        item.setForeground(1, red);
                    } else {
                        item.setForeground(1, darkGreen);
                    }

                    if (selProfit < 0) {
                        item.setForeground(2, red);
                    } else {
                        item.setForeground(2, darkGreen);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing numbers: " + e.getMessage());
                }

                rowIndex++;
            }

            // Add the summary row
            TableItem sumItem = new TableItem(table, SWT.NONE);
            sumItem.setText(new String[]{
                "Total", // The first column (Period) has no sum
                String.format("%.2f", sumAllProfit),
                String.format("%.2f", sumSelProfit),
                String.valueOf(sumAnzStrat)
            });

            // Background color and font size for the summary row
            sumItem.setBackground(gray);
            FontData[] fontData = table.getFont().getFontData();
            for (FontData fd : fontData) {
                fd.setStyle(SWT.BOLD);
                fd.setHeight(12);  // Larger font size for the summary row
            }
            final Font boldFont = new Font(display, fontData);
            sumItem.setFont(boldFont);

            // Color coding for the summary row
            if (sumAllProfit < 0) {
                sumItem.setForeground(1, red);
            } else {
                sumItem.setForeground(1, darkGreen);
            }

            if (sumSelProfit < 0) {
                sumItem.setForeground(2, red);
            } else {
                sumItem.setForeground(2, darkGreen);
            }

            // Add the profit/loss per strategy row only for selProfit
            if (sumAnzStrat > 0) {  // Ensure there is no division by zero
                double selProfitPerStrategy = sumSelProfit / sumAnzStrat;

                TableItem avgItem = new TableItem(table, SWT.NONE);
                avgItem.setText(new String[]{
                    "Profit/Loss per Strategy (selProfit)",
                    "", // Leave allProfitval column empty as it's not applicable
                    String.format("%.2f", selProfitPerStrategy),
                    ""  // No total here
                });

                avgItem.setBackground(gray);
                avgItem.setFont(boldFont);

                // Color coding for the profit/loss per strategy row
                if (selProfitPerStrategy < 0) {
                    avgItem.setForeground(2, red);
                } else {
                    avgItem.setForeground(2, darkGreen);
                }
            }

            // Calculate and add the median profit row
            if (!allProfits.isEmpty()) {
                double medianAllProfit = calculateMedian(allProfits);
                double medianSelProfit = calculateMedian(selProfits);

                TableItem medianItem = new TableItem(table, SWT.NONE);
                medianItem.setText(new String[]{
                    "Median Profit",
                    String.format("%.2f", medianAllProfit),
                    String.format("%.2f", medianSelProfit),
                    ""  // No total here
                });

                medianItem.setBackground(gray);
                medianItem.setFont(boldFont);

                // Color coding for the median profit row
                if (medianAllProfit < 0) {
                    medianItem.setForeground(1, red);
                } else {
                    medianItem.setForeground(1, darkGreen);
                }

                if (medianSelProfit < 0) {
                    medianItem.setForeground(2, red);
                } else {
                    medianItem.setForeground(2, darkGreen);
                }
            }

            // Add listener to dispose resources
            shell.addListener(SWT.Dispose, new Listener() {
                public void handleEvent(org.eclipse.swt.widgets.Event event) {
                    boldFont.dispose();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        shell.open(); // Open the Shell and display it
        shell.layout(); // Apply the layout

        // Event loop for the new window
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        // Dispose custom colors when the window is closed
        darkGreen.dispose();
        lightGray.dispose();
    }

    // Helper method to calculate the median
    private double calculateMedian(List<Double> values) {
        Collections.sort(values);
        int middle = values.size() / 2;
        if (values.size() % 2 == 0) {
            return (values.get(middle - 1) + values.get(middle)) / 2.0;
        } else {
            return values.get(middle);
        }
    }
}
