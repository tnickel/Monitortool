package gui;

import org.eclipse.swt.widgets.Text;

public class Guitools
{
	public static String Text_GetMessageText(Text text)
	{
		
		if(text==null)
			return null;
		String rettext=text.getText();
		String rettextm=text.getMessage();

		//falls text da dann gib zurück
		if(rettext.length()>0)
			return rettext;
		
		//wenn kein text dann versuche message
		return(rettextm);
		
	}
}
