package hilfsklasse;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Sys
{
	static public String getHostname()
	{
		 
		  String name;
		try
		{
			name = InetAddress.getLocalHost().getHostName();
			return name;
		} catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		 return null;
		  
	}
}
