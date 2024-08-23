package WekaTools;

public class WekaClassifierElem
{
	//directoryname das wir lernen wollen
		private String dirname=null;
		//hier ist die responsemessage drin
		private double correlationval=0;
		private int copycounter=0;
		private double selectedFilesProfit=0;
		private double allFilesProfit=0;
		
		public double getAllFilesProfit()
		{
			return allFilesProfit;
		}
		public void setAllFilesProfit(double allFilesProfit)
		{
			this.allFilesProfit = allFilesProfit;
		}
		public WekaClassifierElem( double e)
		{
			correlationval=e;
		}
		public String getDirname()
		{
			return dirname;
		}
		public void setDirname(String dirname)
		{
			this.dirname = dirname;
		}
		public double getCorrelationVal()
		{
			return correlationval;
		}
		public void setCorrelationVal(double evalval)
		{
			this.correlationval = evalval;
		}
		public int getCopycounter()
		{
			return copycounter;
		}
		public void setCopycounter(int copycounter)
		{
			this.copycounter = copycounter;
		}
		public double getSelectedFilesProfit()
		{
			return selectedFilesProfit;
		}
		public void setSelectedFilesProfit(double val)
		{
			this.selectedFilesProfit = val;
		}
		
}
