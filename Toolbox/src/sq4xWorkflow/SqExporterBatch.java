package sq4xWorkflow;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import hiflsklasse.GC;
import hiflsklasse.Inf;

public class SqExporterBatch
{
	private String SqRootpath=null;
	private String SqWorkflowDir=null;
	private String tmp_exportbatch="c:\\tmp\\exportbatch.txt";
	private String databankfile="c:\\tmp\\DatabankExport.csv";

	public String getSqRootpath()
	{
		return SqRootpath;
	}

	public void setSqRootpath(String sqRootpath)
	{
		SqRootpath = sqRootpath;
	}
	
	public String getSqWorkflowDir()
	{
		return SqWorkflowDir;
	}

	public void setSqWorkflowDir(String sqWorkflowDir)
	{
		SqWorkflowDir = sqWorkflowDir;
	}

	public String getTmp_exportbatch()
	{
		return tmp_exportbatch;
	}

	public void setTmp_exportbatch(String tmp_exportbatch)
	{
		this.tmp_exportbatch = tmp_exportbatch;
	}

	public String getDatabankfile()
	{
		return databankfile;
	}

	public void setDatabankfile(String databankfile)
	{
		this.databankfile = databankfile;
	}

	public void exportDatabase()
	{
		File export_f=new File(tmp_exportbatch);
		if(export_f.exists())
			export_f.delete();
		
		Inf inf=new Inf();
		inf.setFilename(tmp_exportbatch);
		inf.writezeile("-project action=status name=Retester");
		inf.writezeile("-databank action=export project=Retester name=Results file="+databankfile +" view=\"Default - Portfolio\"");
		inf.close();
		execExport();
		
	}
	private void execExport()
	{
		//sqcli.exe -run file=C:/exportbatch.txt
		
		try
		{
			String cmd = SqRootpath+"\\sqcli.exe"+ " -run file="+tmp_exportbatch;
			String line = null;

			System.out.println("zeile<" + cmd + ">");
			Process p = Runtime.getRuntime().exec(cmd);

			BufferedReader lsOut = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			while ((line = lsOut.readLine()) != null)
			{
				System.out.println(line);
			}
		} catch (Exception e)
		{
			System.err.println("ls error " + e);
		}
	}
	
}
