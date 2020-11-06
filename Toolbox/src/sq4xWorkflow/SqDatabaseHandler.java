package sq4xWorkflow;

import java.util.ArrayList;

import hiflsklasse.Inf;
import hiflsklasse.Tracer;

public class SqDatabaseHandler extends SqBaseList
{
	
	//übernimmt das handling mit DatabankExport.csv
	SqDatabaseHandler()
	{
		
		
	}
	
	void calcResults(String fnam)
	{
		super.SqReadBaseList(fnam); 
		double np=super.calcAvrNettoprofit(null);
		double retdd=super.calcAvrRetDD(null);
		double pf=super.calcAvrProfitfaktor(null);
		double stabil=super.calcAvrStability(null);

		Tracer.WriteTrace(10, "show results retdd="+retdd+" pf="+pf+"  stabil="+stabil);
		
	}
	
	
}
