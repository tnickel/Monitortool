package work1;

import java.util.Comparator;

public class JTableTradesComperator  implements Comparator<String >
{
	public int compare( String row , String row2 ) 
	 {
		 float val1 =Float.valueOf(row);
		 float val2 =Float.valueOf(row2);
		if(val1<val2)
	            return -1;
		else if(val1>val2)
			return 1;
		else
			return 0;
	 }
}
