package sq4xWorkflow;

import java.util.ArrayList;

import hiflsklasse.Inf;
import hiflsklasse.Tracer;

public class SqDatabaseHandler extends SqBaseList
{
	private String sqrootdir_g=null;
	//übernimmt das handling mit DatabankExport.csv
	SqDatabaseHandler(String root)
	{
		sqrootdir_g=root;
		
	}
	
	void calcResults(String fnam,String cpart)
	{
		super.SqReadBaseList(fnam,sqrootdir_g,cpart); 
		double np=super.calcAvrNettoprofit(null);
		double retdd=super.calcAvrRetDD(null);
		double pf=super.calcAvrProfitfaktor(null);
		double stabil=super.calcAvrStability(null);

		Tracer.WriteTrace(10, "show results retdd="+retdd+" pf="+pf+"  stabil="+stabil);
		
	}
	
	
}
