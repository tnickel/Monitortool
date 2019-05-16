package ftool;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import data.DropRootpath;

public class CheckUser
{
	static public boolean CheckVpsUser()
	{
		String inffile=DropRootpath.getRootpath()+"\\bin\\userinfo.txt";
		if(FileAccess.FileAvailable(inffile))
		   FileAccess.FileDelete(inffile, 0);
		
		try
		{
			String cmd = DropRootpath.getRootpath()
					+ "\\bin\\PsLoggedon.exe \\vmd2741" ;
			String line = null;

			// System.out.println("zeile<"+cmd+">");
			Process p = Runtime.getRuntime().exec(cmd);

			BufferedReader lsOut = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			
			
			while ((line = lsOut.readLine()) != null)
			{
				
				Inf inf = new Inf();
				inf.setFilename(inffile);
				inf.writezeile(line);
				inf.close();
				System.out.println(line);
				/*if(line.contains("logged on"))
					return true;*/
			}
		} catch (Exception e)
		{
			System.err.println("ls error " + e);
		}

		return false;
	}
}
