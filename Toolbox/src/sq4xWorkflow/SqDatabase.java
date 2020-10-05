package sq4xWorkflow;

import java.util.ArrayList;

import hiflsklasse.Inf;
import hiflsklasse.Tracer;

public class SqDatabase 
{
	
	//übernimmt das handling mit DatabankExport.csv
	SqDatabase(String fnam)
	{
		SqBaseList sb=new SqBaseList(fnam);
		
		float np=sb.calcNettoprofit("workflowname");
		float retdd=sb.calcRetDD("workflowname");
		float pf=sb.calcProfitfaktor("workflowname");
		float stabil=sb.calcStability("workflowname");

		Tracer.WriteTrace(10, "show results retdd="+retdd+" pf="+pf+"  stabil="+stabil);
	}
}
