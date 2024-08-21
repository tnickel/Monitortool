package gui;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ShowGoodBadCounter extends JPanel {

    private final ArrayList<Integer> periodNumbers = new ArrayList<>();
    private final ArrayList<Integer> goodCounters = new ArrayList<>();
    private final ArrayList<Integer> badCounters = new ArrayList<>();
    private final StringBuilder fileContent = new StringBuilder();

    private final int leftMargin = 60; // Linker Rand für die Y-Achse
    private final int rightShift = 20; // Zusätzliche Verschiebung nach rechts

    public ShowGoodBadCounter(String filePath) {
        readDataFromFile(filePath);
        setPreferredSize(new Dimension(1600, 800)); // Standardgröße des Diagramms
    }

    private void readDataFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                fileContent.append(line).append("\n");
                if (line.contains("goodcounter")) 
                    continue;
                if(line.contains("###"))
                	continue;

                String[] parts = line.split("#");
                if (parts.length == 3) {
                    int periodNumber = Integer.parseInt(parts[0]);
                    int goodCounter = Integer.parseInt(parts[1]);
                    int badCounter = Integer.parseInt(parts[2]);

                    periodNumbers.add(periodNumber);
                    goodCounters.add(goodCounter);
                    badCounters.add(badCounter);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();
        int barWidth = (width - leftMargin - rightShift) / (goodCounters.size() * 2);

        int maxCounter = Math.max(getMax(goodCounters), getMax(badCounters));
        int scale = (height - 50) / maxCounter;

        // Schriftgröße für Achsenbeschriftungen
        Font originalFont = g.getFont();
        Font axisFont = originalFont.deriveFont(originalFont.getSize() * 1.5f);
        g.setFont(axisFont);

        // Y-Achse Beschriftung und Linie
        g.setColor(Color.BLACK);
        g.drawLine(leftMargin, 10, leftMargin, height - 30); // Y-Achse Linie
        for (int i = 0; i <= maxCounter; i += 20) {
            int y = height - 30 - i * scale;
            g.drawLine(leftMargin - 5, y, leftMargin, y); // kleine Markierungen auf der Y-Achse
            g.drawString(Integer.toString(i), leftMargin - 40, y + 5); // Y-Achsenwerte
        }

        // X-Achse Beschriftung und Linie (umgekehrt)
        g.drawLine(leftMargin, height - 30, width - rightShift, height - 30); // X-Achse Linie
        for (int i = 0; i < periodNumbers.size(); i++) {
            int x = width - (rightShift + i * 2 * barWidth + barWidth / 2);
            g.drawLine(x, height - 25, x, height - 30); // kleine Markierungen auf der X-Achse
            g.drawString(Integer.toString(periodNumbers.get(i)), x - 10, height - 10); // X-Achsenwerte (Periodennummer)
        }

        // Zeichne die Balken (umgekehrt und nach rechts verschoben)
        for (int i = 0; i < goodCounters.size(); i++) {
            int goodHeight = goodCounters.get(i) * scale;
            int badHeight = badCounters.get(i) * scale;

            // Zeichne goodCounter-Balken (grün) zuerst
            g.setColor(Color.GREEN);
            g.fillRect(width - (rightShift + i * 2 * barWidth + barWidth * 2), height - 30 - goodHeight, barWidth, goodHeight);

            // Zeichne badCounter-Balken (rot) danach
            g.setColor(Color.RED);
            g.fillRect(width - (rightShift + i * 2 * barWidth + barWidth), height - 30 - badHeight, barWidth, badHeight);
        }

        // Schriftgröße wieder zurücksetzen
        g.setFont(originalFont);
    }

    private int getMax(ArrayList<Integer> list) {
        int max = Integer.MIN_VALUE;
        for (int value : list) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public String getFileContent() {
        return fileContent.toString();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java ShowGoodBadCounter <file_path>");
            return;
        }

        String filePath = args[0];

        // Hauptfenster
        JFrame frame = new JFrame("Show Good and Bad Counter");

        // Panel für das Diagramm
        ShowGoodBadCounter chartPanel = new ShowGoodBadCounter(filePath);

        // Panel für den Text
        JTextArea textArea = new JTextArea(chartPanel.getFileContent());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(1600, 400)); // Größe des Textfensters

        // JSplitPane für die Aufteilung
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, chartPanel, scrollPane);
        splitPane.setDividerLocation(600); // Teilt den Platz 600px für das Diagramm, Rest für Text
        splitPane.setResizeWeight(0.5); // Gleichmäßige Gewichtung für Diagramm und Text

        // Layout-Manager für das Hauptfenster
        frame.setLayout(new BorderLayout());

        // Einfügen des SplitPanes in das Hauptfenster
        frame.add(splitPane, BorderLayout.CENTER);

        // Fenster konfigurieren
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 1200);
        frame.setVisible(true);
    }
}
