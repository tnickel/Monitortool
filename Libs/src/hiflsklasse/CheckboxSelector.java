package hiflsklasse;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

import java.util.ArrayList;
import java.util.List;

public class CheckboxSelector
{
	private Display display_glob=null;
	private List<String> selectedCheckboxes;
	
	public CheckboxSelector(Display display)
	{
		display_glob=display;
		selectedCheckboxes = new ArrayList<>();
	}
	
	public List<String> open(final int anzTests)
	{ // `anzTests` als final deklariert
		Display display = display_glob;
		final Shell shell = new Shell(display);
		shell.setText("Testauswahl");
		shell.setLayout(new GridLayout(1, false));
		
		// Text zur Aufforderung an den Benutzer
		Label label = new Label(shell, SWT.NONE);
		label.setText("Please choose the reqired ");
		
		// Checkboxen erstellen und hinzufügen
		final Button[] checkboxes = new Button[anzTests];
		for (int i = 0; i < anzTests-1; i++)
		{
			checkboxes[i] = new Button(shell, SWT.CHECK);
			checkboxes[i].setText("_" + i +"_dir");
		}
	
		
		// OK-Button erstellen
		Button okButton = new Button(shell, SWT.PUSH);
		okButton.setText("OK");
		okButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
		
		// Listener für den OK-Button
		okButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e)
			{
				for (int i = 0; i < anzTests-1; i++)
				{
					if (checkboxes[i].getSelection())
					{
						selectedCheckboxes.add("dir" + (i));
					}
				}
				shell.close(); // Fenster schließen, wenn OK geklickt wird
			}
		});
		
		shell.pack();
		shell.open();
		
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}
		
		
		return selectedCheckboxes; // Rückgabe der ausgewählten Checkboxen
	}
	
	public static void main(String[] args)
	{
		CheckboxSelector selector = new CheckboxSelector(Display.getCurrent());
		List<String> result = selector.open(5); // Beispiel mit 5 Checkboxen
		System.out.println("Ausgewählte Checkboxen: " + result);
	}
}
