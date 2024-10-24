package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SimpleTextEditor {

    private Display display;
    private String filePath;

    public SimpleTextEditor(Display display, String filePath) {
        this.display = display;
        this.filePath = filePath;
    }

    public void openEditor() {
        final Shell shell = new Shell(display);  // Mark shell as final
        shell.setText("Text Editor");
        shell.setSize(500, 400);
        shell.setLayout(new GridLayout(1, false));

        // Create text area
        final Text textArea = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        textArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        // Load file if it exists
        File file = new File(filePath);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                textArea.setText(content.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Create Save button
        Button saveButton = new Button(shell, SWT.PUSH);
        saveButton.setText("Save");
        saveButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

        saveButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                try (FileWriter writer = new FileWriter(filePath)) {
                    writer.write(textArea.getText()); // Save the content
                    shell.close(); // Close the editor after saving
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        shell.open();

        // Event loop
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java SimpleTextEditor <Display> <FilePath>");
            System.exit(1);
        }

        Display display = new Display();
        String filePath = args[1];

        // Check if the file exists, create it if not
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Launch the editor
        SimpleTextEditor editor = new SimpleTextEditor(display, filePath);
        editor.openEditor();

        // Dispose the display after usage
        display.dispose();
    }
}
