package swingOberfläche;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;

public class Action
{

	static public void GenAxisRenderer(JFreeChart chart)
	{
		
		XYPlot plot = (XYPlot) chart.getPlot();
		// Axis 1 (Kurs)
		NumberAxis rangeAxis1 = (NumberAxis) plot.getRangeAxis();
		rangeAxis1.setLowerMargin(0.1); // to leave room for volume bars
		DecimalFormat format = new DecimalFormat("00.00");
		rangeAxis1.setNumberFormatOverride(format);
		XYItemRenderer renderer1 = plot.getRenderer();

		renderer1.setBaseToolTipGenerator(new StandardXYToolTipGenerator(
				StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
				new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat("0.00")));

		
	}
}
