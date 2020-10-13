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
		float np=super.calcNettoprofit("workflowname");
		float retdd=super.calcRetDD("workflowname");
		float pf=super.calcProfitfaktor("workflowname");
		float stabil=super.calcStability("workflowname");

		Tracer.WriteTrace(10, "show results retdd="+retdd+" pf="+pf+"  stabil="+stabil);
		
	}
	
	
}
