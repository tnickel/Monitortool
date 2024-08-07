package gui;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class JLibsProgressWin {
    final int MAX = 100;
    final JFrame frame = new JFrame("JProgress Window");
    final JProgressBar pb = new JProgressBar();

    public JLibsProgressWin(String title, int min, int max) {
        // Create progress bar
        frame.setTitle(title);
        pb.setMinimum(min);
        pb.setMaximum(max);
        pb.setStringPainted(true);

        // Add progress bar
        frame.getContentPane().add(pb, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(974, 110);
        frame.setPreferredSize(new Dimension(974, 110));
        
        // Set always on top
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
    }

    public void update(final int pcount) {
        pb.setValue(pcount);
        pb.repaint();  // repaint the progress bar to reflect the new value

        // Bring the frame to the front
        frame.toFront();
        frame.repaint();
    }

    public void end() {
        frame.dispose();
    }

    public static void main(String[] args) {
        // Example usage
        final JLibsProgressWin progressWin = new JLibsProgressWin("Progress", 0, 100);

        for (int i = 0; i <= 100; i++) {
            final int progress = i;
            try {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        progressWin.update(progress);
                    }
                });
                Thread.sleep(100); // Simulate work
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        progressWin.end();
    }
}
