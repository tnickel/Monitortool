package swingOberfläche;

import hilfsklasse.DateExcecption;
import hilfsklasse.Tools;

import java.awt.GridLayout;
import java.util.HashSet;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import objects.UserDbGewinnZeitraumObjI;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import stores.UserSummenGewinneDBI;
import swingtest.DemoPanel;

public class Action8Stabil extends Action
{
	static protected JTextArea textArea;

	public Action8Stabil()
	{
	}

	private static JFreeChart createChart(
			UserSummenGewinneDBI userSummenGewinne, JTextArea txtfeld)
	{
		// Baut das Mainfenster auf
		XYDataset dataset = createDataset(userSummenGewinne, txtfeld);
		String title = "titel";
		JFreeChart chart = ChartFactory.createTimeSeriesChart(title, "Date",
				"Price", dataset, true, true, false);

		// Axis 1 (Kurs)
		Action.GenAxisRenderer(chart);
		ChartUtilities.applyCurrentTheme(chart);

		return chart;
	}

	private static XYDataset createDataset(
			UserSummenGewinneDBI userSummenGewinne, JTextArea txtfeld)
	{
		Float gessum=0f;
		TimeSeries series1 = new TimeSeries("Gewinn");
		HashSet<String> dati = new HashSet<String>();

		// sortiert erst mal nach dem Datum
		userSummenGewinne.sortiereDatum();

		int anz = userSummenGewinne.GetanzObj();

		for (int i = 0; i < anz; i++)
		{
			UserDbGewinnZeitraumObjI gewobj = (UserDbGewinnZeitraumObjI) userSummenGewinne
					.GetObjectIDX(i);

			if (gewobj.getSeqnr() == 0)
			{
				String startdat = gewobj.getStartdatum();
				if (dati.contains(startdat))
					continue;

				dati.add(startdat);
				// Neues Auswertdatum
				Float sum = userSummenGewinne.calcGewinnsumme(0, startdat);
				gessum=gessum+sum;
				int tag;
				try
				{
					tag = Tools.get_day_int(startdat);

					int monat = Tools.get_month_int(startdat);
					int jahr = Tools.get_year_int(startdat);
					if (jahr < 1900)
						jahr = jahr + 2000;

					series1.add(new Day(tag, monat, jahr), sum);
					System.out.println("datensatz datum<" + startdat + ">tag<"
							+ tag + "> monat<" + monat + "> jahr<" + jahr
							+ "> sum<" + sum + ">");
				} catch (DateExcecption e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		}
		txtfeld.append("Sum<"+gessum+">\n");
		return new TimeSeriesCollection(series1);
	}

	public static JPanel createDemoPanel(
			UserSummenGewinneDBI userSummenGewinne, JTextArea txtfeld)
	{
		DemoPanel panel = new DemoPanel(new GridLayout(1, 2));

		// Den Kurs-chart als Panel aufbauen
		JFreeChart chart = createChart(userSummenGewinne, txtfeld);
		panel.add(new ChartPanel(chart));
		panel.addChart(chart);

		return panel;
	}
}
