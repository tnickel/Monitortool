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

    private final int leftMargin = 60; // Linker Rand f�r die Y-Achse
    private final int rightShift = 20; // Zus�tzliche Verschiebung nach rechts

    public ShowGoodBadCounter(String filePath) {
        readDataFromFile(filePath);
        setPreferredSize(new Dimension(1600, 800)); // Standardgr��e des Diagramms
    }

    private void readDataFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                fileContent.append(line).append("\n");
                if (line.contains("goodcounter"))
                    continue;
                if (line.contains("###"))
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
        int scale = Math.max((height - 40) / maxCounter, 1); // Anpassung der Skalierung, min. 1

        Font originalFont = g.getFont();
        Font axisFont = originalFont.deriveFont(originalFont.getSize() * 1.5f);
        g.setFont(axisFont);

        // Y-Achse
        g.setColor(Color.BLACK);
        g.drawLine(leftMargin, 10, leftMargin, height - 30);
        for (int i = 0; i <= maxCounter; i += 20) {
            int y = height - 30 - i * scale;
            g.drawLine(leftMargin - 5, y, leftMargin, y);
            g.drawString(Integer.toString(i), leftMargin - 40, y + 5);
        }

        // X-Achse
        g.drawLine(leftMargin, height - 30, width - rightShift, height - 30);
        for (int i = 0; i < periodNumbers.size(); i++) {
            int x = width - (rightShift + i * 2 * barWidth + barWidth / 2);
            g.drawLine(x, height - 25, x, height - 30);
            g.drawString(Integer.toString(periodNumbers.get(i)), x - 10, height - 10);
        }

        // Zeichne Balken mit Debug-Markierungen
        for (int i = 0; i < goodCounters.size(); i++) {
            int goodHeight = Math.max(1, goodCounters.get(i) * scale);
            int badHeight = Math.max(1, badCounters.get(i) * scale);

            // Debug: Zeichne eine Linie an der Position jedes Balkens
            int xPosition = width - (rightShift + i * 2 * barWidth + barWidth * 2);
            g.setColor(Color.BLUE);
            g.drawLine(xPosition, height - 30 - 10, xPosition, height - 30 + 10);

            // Zeichne goodCounter-Balken (gr�n)
            g.setColor(Color.GREEN);
            g.fillRect(xPosition, height - 30 - goodHeight, barWidth, goodHeight);

            // Zeichne badCounter-Balken (rot)
            xPosition = width - (rightShift + i * 2 * barWidth + barWidth);
            g.setColor(Color.RED);
            g.fillRect(xPosition, height - 30 - badHeight, barWidth, badHeight);
        }

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

        // Panel f�r das Diagramm
        ShowGoodBadCounter chartPanel = new ShowGoodBadCounter(filePath);

        // Panel f�r den Text
        JTextArea textArea = new JTextArea(chartPanel.getFileContent());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(1600, 400)); // Gr��e des Textfensters

        // JSplitPane f�r die Aufteilung
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, chartPanel, scrollPane);
        splitPane.setDividerLocation(600); // Teilt den Platz 600px f�r das Diagramm, Rest f�r Text
        splitPane.setResizeWeight(0.5); // Gleichm��ige Gewichtung f�r Diagramm und Text

        // Layout-Manager f�r das Hauptfenster
        frame.setLayout(new BorderLayout());

        // Einf�gen des SplitPanes in das Hauptfenster
        frame.add(splitPane, BorderLayout.CENTER);

        // Fenster konfigurieren
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 1200);
        frame.setVisible(true);
    }
}

