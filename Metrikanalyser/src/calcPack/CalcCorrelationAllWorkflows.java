package calcPack;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;

import Metriklibs.Attribliste;
import Metriklibs.CoreWorkflowResult;
import Metriklibs.CorelationMapper;
import Metriklibs.Correlator2;
import Metriklibs.Metriktabellen;
import Metriklibs.Statliste;
import data.ExportedDatabanknotUsefull;
import data.Metrikglobalconf;
import filterPack.Filterfiles;
import gui.Viewer;
import hiflsklasse.FileAccess;
import hiflsklasse.Tracer;

public class CalcCorrelationAllWorkflows
{
	Metriktabellen met_glob = new Metriktabellen();
	// hier sind die Filterzeiträume gespeichert
	Filterfiles filterfiles_glob = new Filterfiles(); // wir brauchen hier keine filterifles
	ExportedDatabanknotUsefull expdatabank_glob = new ExportedDatabanknotUsefull();
	private String endtestattribname_glob = null;
	private String corealgotype_glob = null;
	CorelationMapper cmapperCollect_glob = new CorelationMapper();
	public CalcCorrelationAllWorkflows()
	{
	}
	
	public CorelationMapper CalcCorrelationAllWorkflows(String endtestattribname, String corealgotype,
			boolean showoutputflag)
	{
		// Check Tableroot
		if (Metrikglobalconf.isValidMasterRootpath() == false)
		{
			Tracer.WriteTrace(10,
					"E:Please set correct Tablerootpath, actual=<" + Metrikglobalconf.getFilterpath() + ">");
			return null;
		}
		
		// Idee: 1)mache die Korrelation für jeden Workflow einzelen
		// 2) dann kommt der Mapper und sammelt alles ein und wertet aus.
		
		// wir wollen für alle Workflows die korrelationen für alle Attribute berechnen.
		// Es wird auch _0_dir.... _x_dir ausgewertet.
		endtestattribname_glob = endtestattribname;
		corealgotype_glob = corealgotype;
		// hier im Pack werden alle Correlationen gesammelt, diese werden später
		// ausgewertet.
		//cmapperCollect_glob = new CorelationMapper();
		ArrayList<String> filePaths = new ArrayList<>();
		String rpath = Metrikglobalconf.getFilterpath();
		if ((rpath == null) || (rpath.length() < 5))
			Tracer.WriteTrace(10, "Error: please set config in File/Config");
		
		// gehe durch die workflows
		FileAccess.initFileSystemList(rpath, 0);
		int anzworkflows = FileAccess.holeFileAnz();
		for (int workflownumber = 0; workflownumber < anzworkflows; workflownumber++)
		{
			String dirnam = FileAccess.holeFileSystemName();
			String aktsubdir = rpath + "\\" + dirnam;
			String fnam = aktsubdir + "\\correlation.txt";
			filePaths.add(fnam);
			
			// Wenn workflowname vorkommt ist dies kein workflow, sondern nur der Bereich wo
			// die logfiles gesammelt werden
			if (dirnam.contains("WORKFLOWNAME"))
				continue;
		
			Tracer.WriteTrace(20, "I: calc correlation for workflow subdir<"+aktsubdir+">");
			CoreWorkflowResult corelWorkflowResult = CorelateOneWorkflow(endtestattribname, corealgotype, aktsubdir);
			
			// im cmappper werden alle ergebnisse gesammelt, hier findet dann auch die
			// Auswertung statt
			// i ist die workflownummer
			if (corelWorkflowResult != null)
				cmapperCollect_glob.addListe(corelWorkflowResult);
			
		}
		
		Statliste stat = cmapperCollect_glob.generateWorkflowResults();
		stat.WriteToFile(rpath + "\\WORKFLOWNAME\\metrics_of_all_workflows.txt");
		
		if (showoutputflag == true)
		{
			cmapperCollect_glob.showallWorkflowResults(filePaths);
			Viewer v = new Viewer();
			v.viewTableExtFile(Display.getCurrent(), rpath + "\\WORKFLOWNAME\\metrics_of_all_workflows.txt");
		}
		return cmapperCollect_glob;
	}
	
	public Attribliste getGoodAttribliste()
	{
		Attribliste attribgut=cmapperCollect_glob.CalcgoodAttribliste();
		return attribgut;
		
		
	}
	
	private CoreWorkflowResult CorelateOneWorkflow(String endtestattribname, String corealgotype, String aktsubdir)
	{
		// endtestattribname= name der zu lernenden variable
		// algotype: es gibt 3 algotypen
		// aktsubir: Das ist das Direktory wo wir drin arbeiten
		ExportedDatabanknotUsefull expdatabank_glob = new ExportedDatabanknotUsefull();
		expdatabank_glob.readExportedDatabank(met_glob, null, null, aktsubdir);
		// als nächstes wird korreliert
		CoreWorkflowResult cwres = Correlator2.CalcCorrelation(met_glob, endtestattribname, corealgotype);
		Correlator2.schreibeTabelle(aktsubdir + "\\correlation.txt", corealgotype);
		
		return (cwres);
	}
	
}
