package comperatoren;

import java.util.Comparator;

import sq4xWorkflow.SqBaseElem;

public class SqBaseElemComperator implements Comparator<SqBaseElem>
{
	
	
        public int compare(SqBaseElem e1, SqBaseElem e2)
        {

            return  e1.getStrategyname().compareTo(e2.getStrategyname());
        }
	
}
