package work1;

import hiflsklasse.SG;

import java.util.Comparator;

public class JTableNameComperator implements Comparator<String >
{
	public int compare( String row , String row2 ) 
	 {
		//buy/sell <0001a,0001b>
		//sell/buy <0001a,0001b>
		
		 String name1 =row.substring(row.indexOf("<")+1,row.lastIndexOf(">"));
		 String name2 =row2.substring(row2.indexOf("<")+1,row2.lastIndexOf(">"));

	
		 
		 String name1a=name1.substring(0,4);
		 String name2a=name2.substring(0,4);
		 
		 String command1 =row.substring(0,8);
		 String command2= row2.substring(0,8);
		 
		 int val1=SG.get_zahl(name1a);
		 int val2=SG.get_zahl(name2a);
		 
		 if(val1==val2)
		 {
			 if(command1.contains("buy/sell"))
				 return 1;
			 else
				 return -1;
		 }
		 
		if(val1<val2)
	            return -1;
		else if(val1>val2)
			return 1;
		else
			return 0;
	 }
}