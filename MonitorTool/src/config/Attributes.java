package config;


import java.util.ArrayList;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;

public class Attributes
{
	
	
	
	Set<String> Groups = getData(Contents);

	ArrayList<Button> buttons = new ArrayList<Button>();

	for(String group : contentGroups)
	{
	    Button newButton = new Button(fComposite, SWT.CHECK);  
	    newButton.setText(group);

	    // save the button
	    buttons.add(newButton);
	}

	Button okButton = new Button(lowComposite, SWT.PUSH);

	okButton.addListener(SWT.Selection, new Listener()
	{
	    @Override
	    public void handleEvent(Event e)
	    {
	        // iterate over saved buttons
	        for(Button button : buttons)
	        {
	            System.out.println(button.getText() + ": " + button.getSelection());
	        }       
	    }
	}
}
