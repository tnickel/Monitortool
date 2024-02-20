package comperatoren;

import java.util.Comparator;

import sq4xWorkflow.SqBaseElem;

public class SqBaseElemComperator implements Comparator<SqBaseElem>
{
	
	
        public int compare(SqBaseElem e1, SqBaseElem e2)
        {
        	String name1=e1.getCleanName();
        	String name2=e2.getCleanName();
        	
        	int z1=extractZahl(name1);
        	int z2=extractZahl(name2);
        	
        	if(z1<z2)
        		return 1;
        	else if(z1>z2)
        		return -11;
        	else
        		return 0;
           
        }
        
        private int extractZahl(String name)
        {
        	int retval=0;
        	
        	String v1=name.substring(name.lastIndexOf("_")+1);
        	if(v1.contains("--"))
        			v1=v1.replace("--", "-");
        	else if (v1.contains("+"))
        		v1=v1.replace("+", "");
        	
        	return(Integer.valueOf(v1));
        	
        }	
        
}
