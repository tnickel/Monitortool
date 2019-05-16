package attribute;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.data.category.SlidingCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class Diagramm1 extends ApplicationFrame
{
	Diagramm1 demo=null;
	SlidingCategoryDataset datas_g=null;
	
	private static final long serialVersionUID = 7348324611398491900L;
	public Diagramm1(String title,SlidingCategoryDataset datas)
	{
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		datas_g=datas;
		setContentPane(createDemoPanel(datas));
	}

	public  JPanel createDemoPanel(SlidingCategoryDataset datas)
	{
		return new MyDemoPanel1(datas);
	}

	public  void start()
	{
		demo = new Diagramm1("JFreeChart:blup.java",datas_g);
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	}
}
