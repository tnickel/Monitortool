package data;

import java.text.DecimalFormat;
import java.text.ParseException;

import xml.TnParser;

public class ClusterStrategy
{
	double[] attributevalue = new double[10];
	String stratname = null;

	public String getClustername()
	{
		String nam = "CLUST_pf" + cfs(attributevalue[0]) + "_comp"
				+ cfs(attributevalue[1]) + "_free" + cfs(attributevalue[2])
				+ "_net" + cfs(attributevalue[3]) + "_pct"
				+ cfs(attributevalue[4]) + "_dd" + cfs(attributevalue[5])
				+ "_abw" + cfs(attributevalue[6]) + "_abt"
				+ cfs(attributevalue[7]);
		return nam;
	}

	// hier in dem part wird nur eine Strategie gehalten
	public ClusterStrategy(String fnam)
	{
		TnParser parser = new TnParser();
		parser.loadFile(fnam, 0);

		this.setStratname(fnam);

		attributevalue[0] = parser.getFloatValue("InSample", "profitFactor");
		attributevalue[1] = parser.getFloatValue("InSample", "complexity");
		attributevalue[2] = parser
				.getFloatValue("InSample", "degreesOfFreedom");
		attributevalue[3] = parser.getFloatValue("InSample", "netProfit");
		attributevalue[4] = parser.getFloatValue("InSample", "pctWins");
		attributevalue[5] = parser.getFloatValue("InSample", "drawdown");
		attributevalue[6] = parser.getFloatValue("InSample", "avgBarsWin");
		attributevalue[7] = parser.getFloatValue("InSample", "avgBarsTrade");
	}

	private String cfs(double fl)
	{
		// calc float string with 2 decimal places
		
		DecimalFormat df = new DecimalFormat("##0.##");
		String outstring = "";

		outstring = df.format(new Double(fl));
		outstring=outstring.replace(",", ".");

		return outstring;
	}

	public String getStratname()
	{
		return stratname;
	}

	public void setStratname(String stratname)
	{
		this.stratname = stratname;
	}

	public double[] getAttribarray()
	{
		return attributevalue;
	}

	public double calcCorel(ClusterStrategy cl)
	{
		double[] attrib1 = cl.getAttribarray();

		double val_f = cl.getPearsonCorrelation2(attrib1, attributevalue);
		return val_f;

	}

	private double getPearsonCorrelation2(double[] array1, double[] array2)
	{
		// person Correlation nur mit absolutwerten
		double result = 0;
		double sum_sq_x = 0;
		double sum_sq_y = 0;
		double sum_coproduct = 0;
		double mean_x = array1[0];
		double mean_y = array2[0];
		for (int i = 2; i < array1.length + 1; i += 1)
		{
			double sweep = Double.valueOf(i - 1) / i;
			double delta_x = array1[i - 1] - mean_x;
			double delta_y = array2[i - 1] - mean_y;
			sum_sq_x += delta_x * delta_x * sweep;
			sum_sq_y += delta_y * delta_y * sweep;
			sum_coproduct += delta_x * delta_y * sweep;
			mean_x += delta_x / i;
			mean_y += delta_y / i;
		}
		double pop_sd_x = (double) Math.sqrt(sum_sq_x / array1.length);
		double pop_sd_y = (double) Math.sqrt(sum_sq_y / array1.length);
		double cov_x_y = sum_coproduct / array1.length;
		result = cov_x_y / (pop_sd_x * pop_sd_y);
		return Math.abs(result);
	}
}

/**
 * 
 * <Values Index="0"> <netProfit>-174.09995</netProfit>
 * <numberOfTrades>79</numberOfTrades> <pctWins>41.772152</pctWins>
 * <profitFactor>0.854053</profitFactor> <drawdown>384.50293</drawdown>
 * <pctDrawdown>3.8687992</pctDrawdown> <winLossRatio>0.7173913</winLossRatio>
 * <retDDRatio>-0.45279226</retDDRatio> <avgWin>30.872726</avgWin>
 * <avgLoss>-25.932611</avgLoss> <avgBarsWin>18.636364</avgBarsWin>
 * <avgBarsLoss>10.065217</avgBarsLoss> <avgTrade>27.996204</avgTrade>
 * <avgBarsTrade>13.64557</avgBarsTrade> <payoutRatio>1.1904981</payoutRatio>
 * <numberOfWins>33</numberOfWins> <numberOfLoss>46</numberOfLoss>
 * <grossProfit>1018.8</grossProfit> <grossLoss>-1192.9001</grossLoss>
 * <largestWin>90.9</largestWin> <largestLoss>-83.5</largestLoss>
 * <maxConsecWins>6</maxConsecWins> <maxConsecLoss>11</maxConsecLoss>
 * <avgConsecWin>1.7368422</avgConsecWin>
 * <avgConsecLoss>2.5555556</avgConsecLoss>
 * <correlation>0.11488471</correlation> <symmetry>0.0</symmetry>
 * <ordersHash>-883267826</ordersHash> <exposure>4.318737</exposure>
 * <stagnationFrom>1294132634</stagnationFrom>
 * <stagnationTo>1325152731</stagnationTo>
 * <stagnationPeriod>360</stagnationPeriod>
 * <stagnationPeriodPct>100.0</stagnationPeriodPct>
 * <degreesOfFreedom>52</degreesOfFreedom> <complexity>0</complexity>
 * <AHPR>-0.02157783</AHPR> <sharpeRatio>-0.059736464</sharpeRatio>
 * <pctLoss>58.227848</pctLoss> <expectancy>-2.2037969</expectancy>
 * <standardDev>46.54434</standardDev> <zProbability>75.24984</zProbability>
 * <zScore>-0.68237287</zScore> <avgProfitByDay>-0.48361096</avgProfitByDay>
 * <avgProfitByMonth>-15.827268</avgProfitByMonth>
 * <avgProfitByYear>-189.92722</avgProfitByYear>
 * <rexpectancy>-0.08498168</rexpectancy>
 * <rexpectancyopp>-7.3238754</rexpectancyopp>
 * <aarDDRatio>-0.4562046</aarDDRatio>
 * <avgPctProfitByYear>-1.764964</avgPctProfitByYear>
 * <avgPctProfitByMonth>-0.14828381</avgPctProfitByMonth> <sqn>-0.553068</sqn>
 * <sqn_score>-0.3765488</sqn_score> <reached100PctDD>0</reached100PctDD>
 * <totalDaysOfTrading>360</totalDaysOfTrading>
 * 
 * @author tnickel
 * 
 */
