package swtConf;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class SucheNeuempfehlungen
{
	SucheNeuempfehlungen()
	{
		//Load Config
		
	}
	
	public void StartSuche()
	{
		//Hier werden die Neuempfehlungen gesucht
		
	}
	
	public void ZeigeNeuempfehlungen()
	{
		//Hier werden die Neuempfehlungen in einem externen Fenster angezeigt
		
		
	}
	
	public void editConfig(Display dis)
	{
		init(dis);
	}

	private void init(Display dis)
	{
		
		
			Shell sh = new Shell(dis);
			sh.setLayout(null);

		
			
		sh.pack();
		sh.setSize(787, 540);
		sh.open();
		
		while (!sh.isDisposed())
		{
			if (!dis.readAndDispatch())
				dis.sleep();
		}
	}
	
	
	
	
	
	
	
	

}
