package ComData;

import javax.swing.JPanel;

public class PanelResult
{
	//diese Klasse beinhaltet s�mtliche Informationen die f�r das
	//Panel ben�tigt werden
	JPanel panel=null;
	int gdx=0;

	public JPanel getPanel()
	{
		return panel;
	}

	public void setPanel(JPanel panel)
	{
		this.panel = panel;
	}

	public int getGdx()
	{
		return gdx;
	}

	public void setGdx(int gdx)
	{
		this.gdx = gdx;
	}

	
	
}
