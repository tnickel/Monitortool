package WekaTools;

import weka.classifiers.Evaluation;

public class WekaClassifierElem
{
	//directoryname das wir lernen wollen
		private String dirname=null;
		//hier ist die responsemessage drin
		private double evalval=0;
		public WekaClassifierElem( double e)
		{
			evalval=e;
		}
		public String getDirname()
		{
			return dirname;
		}
		public void setDirname(String dirname)
		{
			this.dirname = dirname;
		}
		public double getEvalval()
		{
			return evalval;
		}
		public void setEvalval(double evalval)
		{
			this.evalval = evalval;
		}
		
}
