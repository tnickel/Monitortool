package work1;

import hiflsklasse.SG;

import java.util.Comparator;

public class JTableIndexComperator  implements Comparator<String >
{
	public int compare( String row , String row2 ) 
	 {
		 int val1 =SG.get_zahl(row);
		 int val2 =SG.get_zahl(row2);
		if(val1<val2)
	            return -1;
		else if(val1>val2)
			return 1;
		else
			return 0;
	 }
}
