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
		double np=super.calcAvrNettoprofit();
		double retdd=super.calcAvrRetDD();
		double pf=super.calcAvrProfitfaktor();
		double stabil=super.calcAvrStability();

		Tracer.WriteTrace(10, "show results retdd="+retdd+" pf="+pf+"  stabil="+stabil);
		
	}
	
	
}
