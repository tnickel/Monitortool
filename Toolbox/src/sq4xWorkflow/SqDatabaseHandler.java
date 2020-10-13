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
		float np=super.calcAvrNettoprofit();
		float retdd=super.calcAvrRetDD();
		float pf=super.calcAvrProfitfaktor();
		float stabil=super.calcAvrStability();

		Tracer.WriteTrace(10, "show results retdd="+retdd+" pf="+pf+"  stabil="+stabil);
		
	}
	
	
}
