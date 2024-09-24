package Metriklibs;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gui.JLibsProgressWin;
import gui.ShowGoodBadCounter;
import hiflsklasse.BasicWekaTools;
import hiflsklasse.FileAccess;
import hiflsklasse.Inf;
import hiflsklasse.Tracer;

public class GeneratorStatistik extends BasicWekaTools
{
	private String logfile_goodbad_glob=null;
	private String workflowname_glob=null;
	private float goodfaktor_glob=0;
	public GeneratorStatistik()
	{
	}
	
	public float getGoodfaktor()
	{
		return goodfaktor_glob;
	}



	public void collectGoodBad(String filterpath,String Workflowname)
	{
		workflowname_glob=Workflowname;
		FileAccessDyn fdirs = new FileAccessDyn();
		fdirs.initFileSystemList(filterpath, 0);
		
		int anz = fdirs.holeFileAnz();
		
		int progcount = 0;
		JLibsProgressWin jp = new JLibsProgressWin("Learn Workflow", 0, anz);
		
		String logfile= filterpath+"\\WORKFLOWNAME\\good_badcounter.txt";
		 logfile_goodbad_glob=logfile;
		FileAccess.FileDelete(logfile, 1);
		Inf inf = new Inf();
		inf.setFilename(logfile);
		inf.writezeile("periodnumber#goodcounter#badcounter");	
		
		int sum_good=0, sum_bad=0;
		
		for (int i = 0; i < anz; i++)
		{
			jp.update(i);
			String workdir = (filterpath + "\\" + fdirs.holeFileSystemName()).toLowerCase();
			if (isValidWorkdir(workdir) == false)
				continue;
			
			String sub=workdir.substring(workdir.lastIndexOf("_"));
			sub=sub.replace("_", "").replace("+", "").replace("-","");
			int number=Integer.valueOf(sub);
			
			String goodcounterfile = workdir + "\\_99_dir\\goodcounter\\DatabankExport.csv";
			if(new File(goodcounterfile).exists()==false)
			{
				Tracer.WriteTrace(20, "E: no goodcounterfile <"+goodcounterfile+"> available");
				
			}
			String badcounterfile = workdir + "\\_99_dir\\badcounter\\DatabankExport.csv";
			if(new File(badcounterfile).exists()==false)
			{
				Tracer.WriteTrace(20, "E: no badcounterfile <"+badcounterfile+"> available");
				
			}
			int goodcounter=1, badcounter=1;
			
			if(FileAccess.FileAvailable(goodcounterfile))
				goodcounter=countLinesInFile(goodcounterfile)-1;
			if(FileAccess.FileAvailable(badcounterfile))
				badcounter=countLinesInFile(badcounterfile)-1;
			inf.writezeile(number+"#"+goodcounter+"#"+badcounter);
			sum_good=sum_good+goodcounter;
			sum_bad=sum_bad+badcounter;
		}
		goodfaktor_glob=(float)sum_good/((float)sum_bad+(float)sum_good);
		jp.end();
		inf.writezeile("### anz good=" + sum_good + " anz bad=" + sum_bad + " faktor= (good/bad)=" + goodfaktor_glob);

		inf.close();
	}
	
	
	
	public int countLinesInFile(String filePath)
	{
		int lines = 0;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
		{
			while (reader.readLine() != null)
			{
				lines++;
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return lines;
	}
	public void showGoodBadcounter() {

	    JFrame frame = new JFrame("Show Good and Bad Counter faktor=" + goodfaktor_glob);
	    frame.setLayout(new BorderLayout());

	    // Erstellen des ShowGoodBadCounter-Panels
	    ShowGoodBadCounter panel = new ShowGoodBadCounter(logfile_goodbad_glob);
	    double totalratio=panel.calculateTotalRatio();
	    
	    // Erstes Textfeld erstellen
	    JTextField textField1 = new JTextField(workflowname_glob);
	    textField1.setHorizontalAlignment(JTextField.LEFT);
	    textField1.setFont(new Font("SansSerif", Font.BOLD, 35));

	    // Den total ratio factor auf 3 Dezimalstellen runden
	    String formattedRatio = String.format("%.3f", totalratio);

	    
	    // Zweites Textfeld erstellen
	    JTextField textField2 = new JTextField("total ratio factor="+formattedRatio);
	    textField2.setHorizontalAlignment(JTextField.LEFT);
	    textField2.setFont(new Font("SansSerif", Font.PLAIN, 35));

	    // Panel erstellen, um die Textfelder zu enthalten
	    JPanel textPanel = new JPanel();
	    textPanel.setLayout(new GridLayout(2, 1)); // 2 Zeilen, 1 Spalte
	    textPanel.add(textField1);
	    textPanel.add(textField2);

	    // Hinzufügen des Panels und des Textfeld-Panels zum Frame
	    frame.add(panel, BorderLayout.CENTER);
	    frame.add(textPanel, BorderLayout.SOUTH);

	    frame.setSize(3200, 1500);
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    frame.setVisible(true);
	}

}
